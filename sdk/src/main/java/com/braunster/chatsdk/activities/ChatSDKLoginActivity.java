/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.braunster.chatsdk.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.braunster.chatsdk.BuildConfig;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.Utils.Debug;
import com.braunster.chatsdk.activities.abstracted.ChatSDKAbstractLoginActivity;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.dao.core.DaoCore;
import com.braunster.chatsdk.google_signin.CustomVolleyRequest;
import com.braunster.chatsdk.google_signin.GoogleSignin;
import com.braunster.chatsdk.network.BDefines;
import com.braunster.chatsdk.network.BNetworkManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by itzik on 6/8/2014.
 */
public class ChatSDKLoginActivity extends ChatSDKAbstractLoginActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ChatSDKLoginActivity.class.getSimpleName();
    private static boolean DEBUG = Debug.LoginActivity;
    private PopupWindow pw;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    Button googeSignin;
    private Button btnLogin, btnTwitter;
    private ImageView appIconImage;
    TextView  btnReg, btnAnon;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableFacebookIntegration(getNetworkAdapter().facebookEnabled());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_sdk_activty_login);

        setExitOnBackPressed(true);

        View view = findViewById(R.id.chat_sdk_root_view);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
           // actionBar.setTitle("Ride");
            actionBar.setLogo(R.drawable.background_standard_orange);
        }
        
        setupTouchUIToDismissKeyboard(view);

        initViews();
        googeSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("dhdhjd","djjd");
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("396629937189-on5d259uan7k6qke6ndvve5u99tr5qqs.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                googeSignin.setBackgroundResource(R.drawable.googlepluslogo);

                Intent iso=new Intent(getApplicationContext(), GoogleSignin.class);
                startActivity(iso);
            }
        });

        ((TextView) findViewById(R.id.chat_sdk_txt_version)).setText(String.valueOf(BuildConfig.VERSION_NAME));
    }

    @Override
    protected void initViews(){
        super.initViews();
        facebookLogin.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        facebookLogin.setBackgroundResource(R.drawable.facebooklogo);

        if (integratedWithFacebook)
        {

            facebookLogin.setReadPermissions(Arrays.asList("email", "user_friends"));
        }
        googeSignin=(Button)findViewById(R.id.chat_sdk_btn_google_login);
        btnLogin = (Button) findViewById(R.id.chat_sdk_btn_login);
        btnAnon = (TextView) findViewById(R.id.chat_sdk_btn_anon_login);
        btnTwitter = (Button) findViewById(R.id.chat_sdk_btn_twitter_login);
        btnReg = (TextView) findViewById(R.id.chat_sdk_btn_register);
        etEmail = (EditText) findViewById(R.id.chat_sdk_et_mail);
        etPass = (EditText) findViewById(R.id.chat_sdk_et_password);

        appIconImage = (ImageView) findViewById(R.id.app_icon);

        appIconImage.post(new Runnable() {
            @Override
            public void run() {
                appIconImage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initListeners(){
        /* Registering listeners.*/
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        btnAnon.setOnClickListener(this);
        btnTwitter.setOnClickListener(this);

        etPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    btnLogin.callOnClick();

                }
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        initListeners();
    }

    /* Dismiss dialog and open main activity.*/
    @Override
    protected void afterLogin(){
        super.afterLogin();

        // Updating the version name.
        BUser curUser = getNetworkAdapter().currentUserModel();
        
        String version = BDefines.BAppVersion,
                metaVersion = curUser.metaStringForKey(BDefines.Keys.BVersion);
        
        if (StringUtils.isNotEmpty(version))
        {
            if (StringUtils.isEmpty(metaVersion) || !metaVersion.equals(version))
            {
                curUser.setMetadataString(BDefines.Keys.BVersion, version);
            }

            DaoCore.updateEntity(curUser);
        }
        
        startMainActivity();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.chat_sdk_btn_login) {
            passwordLogin();
        }
        else if (i == R.id.chat_sdk_btn_anon_login) {

            initiatePopupWindow();


        }
        else if (i == R.id.chat_sdk_btn_register)
        {
           // register();
            initiatePopupWindow1();        }
        else if (i == R.id.chat_sdk_btn_twitter_login){
            twitterLogin();
        }
        else if(i==R.id.chat_sdk_btn_google_login)
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("396629937189-on5d259uan7k6qke6ndvve5u99tr5qqs.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

             mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            signIn();


//            Intent iso=new Intent(getApplicationContext(), GoogleSignin.class);
//            startActivity(iso);
        }
        else if (i == R.id.chat_sdk_facebook_button){
            setFacebookLogin();
        }
        else
        {
            Log.e("Hai","Google");

        }
    }

    private void signIn() {


        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }


    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("Name",acct.getDisplayName());
            Log.e("Name",acct.getEmail());


//            //Displaying name and email
//            textViewName.setText(acct.getDisplayName());
//            textViewEmail.setText(acct.getEmail());
//
//            //Initializing image loader
//            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//
//            imageLoader.get(acct.getPhotoUrl().toString(),
//                    ImageLoader.getImageListener(profilePhoto,
//                            R.mipmap.ic_launcher,
//                            R.mipmap.ic_launcher));
//
//            //Loading image
//            profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) ChatSDKLoginActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout1 = inflater.inflate(R.layout.forgotpass,
                    (ViewGroup) findViewById(R.id.popup_element_forgot));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(layout1, Gravity.CENTER, 0, 0);

            final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            final Button cancelButton = (Button) layout1.findViewById(R.id.oldcancel);
            final TextView oldmail=(TextView)layout1.findViewById(R.id.oldmail);
            Button oldsend=(Button)layout1.findViewById(R.id.oldsend);
            oldsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!oldmail.getText().toString().equalsIgnoreCase("")) {
                        if(oldmail.getText().toString().trim().matches(emailPattern)){

                        BNetworkManager.sharedManager().getNetworkAdapter().sendPasswordResetMail(oldmail.getText().toString());
                        Toast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "The Email Address is Badly Formated...", Toast.LENGTH_SHORT).show();

                        }

                    }
                              else
                        {
                            Toast.makeText(getApplicationContext(), "Field Empty...", Toast.LENGTH_SHORT).show();

                        }
                    }

            });
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };


    private void initiatePopupWindow1() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) ChatSDKLoginActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.signup,
                    (ViewGroup) findViewById(R.id.popup_element_signup));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button cancelButton = (Button) layout.findViewById(R.id.newcancel);
            final TextView newmail=(TextView)layout.findViewById(R.id.newmail);
            Button newsend=(Button)layout.findViewById(R.id.newsend);
          final  TextView pass=(TextView) layout.findViewById(R.id.newpass);
            final TextView conpass=(TextView) layout.findViewById(R.id.newconpass);
            newsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!newmail.getText().toString().equalsIgnoreCase("")&&!pass.getText().toString().equalsIgnoreCase("")&&!conpass.getText().toString().equalsIgnoreCase("")) {
                        if (pass.getText().toString().equalsIgnoreCase(conpass.getText().toString())) {

                            etEmail.setText(newmail.getText().toString());
                            etPass.setText(pass.getText().toString());
                            register();
                            etEmail.setText("");
                            etPass.setText("");

                        } else {
                            Toast.makeText(getApplicationContext(), "Passwords mismatched...", Toast.LENGTH_SHORT).show();
                            conpass.setText("");
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Fields Empty...", Toast.LENGTH_SHORT).show();

                    }
                }
            });



            cancelButton.setOnClickListener(cancel_button_click_listener1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button_click_listener1 = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
