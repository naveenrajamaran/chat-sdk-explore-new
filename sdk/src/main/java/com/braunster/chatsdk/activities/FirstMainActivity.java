package com.braunster.chatsdk.activities;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.articles.MainFragment;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.Utils.ExitHelperone;
import com.braunster.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.braunster.chatsdk.activities.abstracted.ChatSDKAbstractLoginActivity;
import com.braunster.chatsdk.activities.bluetooth.BluetoothLeService;
import com.braunster.chatsdk.activities.location.TrackerService;
import com.braunster.chatsdk.adapter.ChatSDKThreadsListAdapter;
import com.braunster.chatsdk.dao.BThread;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.network.AbstractNetworkAdapter;
import com.braunster.chatsdk.network.BNetworkManager;
import com.braunster.chatsdk.network.events.BatchedEvent;
import com.braunster.chatsdk.network.events.Event;
import com.braunster.chatsdk.object.Batcher;
import com.braunster.chatsdk.object.ChatSDKThreadPool;
import com.braunster.chatsdk.object.UIUpdater;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class FirstMainActivity extends FragmentActivity  {
    private static final String TAG1 = FirstMainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    ChatMessageBox cmb;
    String batval;
    public static TextView counttime;
    TextView ridetimer;
    String value;
    Boolean rideAdmin=false;
    static String GnameGid[]=new String[2];
    static Boolean RideStatus=false;
    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Snackbar mSnackbarPermissions;
    private Snackbar mSnackbarGps;
    public MediaRecorder recordering;
    public String recordingPath;
    TelephonyManager telephonyManager;
    boolean broadcastConnected;

    Intent gattServiceIntent;
    static  String gName="";
    ArrayAdapter<String> adapter1=null;
   static ListView hidelist;
     static String currentRideThreadId="";
    static String RideGroupName="";
    private final int REQ_CODE_SPEECH_INPUT = 1234;
    static int threadId;
    private   android.support.v4.app.Fragment fragment1;
    android.support.v4.app.FragmentManager fragmentManager1;
    ExitHelperone exitHelperOne;
    static int check;
    ActionBar actionBar;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    List<String>adding;
    float dX;
    Toast toastRecording;
    float dY;
    private PopupWindow pw;
    Firebase ref1,ref2;
    BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();

    int lastAction;
    TextView ridetitle,selectedgroup, mBatteryLevelText,devname,devsts;
    Spinner ridegroup;
    static String data1;
    public static FloatingActionButton currentRide;
    Button startride,scan,stop;;
    private ProgressBar loading,batbar,progbat;
    protected ChatSDKUiHelper chatSDKUiHelper;
    private FragmentManager fragmentManager;
    private static final String MANUFACTURER_HTC = "HTC";

    private KeyguardManager keyguardManager;
    private AudioManager audioManager;
    private CallStateReceiver callStateReceiver;
    static int k=0;
    public static LocationSharing ls;


    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    ListView lv;

   static List<String> texts;

    private static final String TAG = RideFragment.class.getSimpleName();
    public static final String APP_EVENT_TAG= "ChatRoomsFrag";
    Handler mHandler ;
    private ChatSDKThreadsListAdapter adapter;
    private UIUpdater uiUpdater;

    public boolean mConnected = false;
    private BluetoothLeService mBluetoothLeService;


    public static String address;
    private BroadcastReceiver mReceiver;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    BluetoothDevice device=null;
    static int pos;

    /*Bluetooth service initialization */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("TAG", "Unable to initialize Bluetooth");
                finish();
            }
            device = mLeDeviceListAdapter.getDevice(pos);

            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(device.getAddress());
            address=device.getAddress();
            mLeDeviceListAdapter.clear();
            // scanLeDevice(false);
            mLeDeviceListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    public static int startaudio=0;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exitHelperOne = new ExitHelperone(this);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        mReceiver = new BatteryBroadcastReceiver();
        Firebase.setAndroidContext(this);
        setContentView(R.layout.first_main_activity);
        chatSDKUiHelper = ChatSDKUiHelper.getInstance().get(getApplicationContext());
        /*Automaticallu update the location LatLan value to th firebase */
        GPSTracker gpsTracker = new GPSTracker(this);
        final RideTimer r=new RideTimer();

//        if (gpsTracker.canGetLocation()) {
//            double latitude = gpsTracker.getLatitude();
//            double longitude = gpsTracker.getLongitude();
//            BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
//            Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());
//            Firebase childref=ref.child("user mail");
//            childref.setValue(user.getMetaEmail());
//            childref=ref.child("user name");
//            childref.setValue(user.getMetaName());
//            childref= ref.child("latitude");
//            childref.setValue(String.valueOf(latitude));
//            childref = ref.child("langitude");
//            childref.setValue(String.valueOf(longitude));
//
//
//        }


        if(ChatSDKAbstractLoginActivity.isRegister==true)
        {
            BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();

            Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + user.getEntityID());
            Firebase childref=ref.child("ride_status");
            childref.setValue("0");
            childref=ref.child("ride_group");
            childref.setValue("null");
            ChatSDKLoginActivity.isRegister=false;


        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        broadcastConnected = MANUFACTURER_HTC.equalsIgnoreCase(Build.MANUFACTURER)
                && !audioManager.isWiredHeadsetOn();
        if (broadcastConnected) {
            broadcastHeadsetConnected(false);
        }
        mBatteryLevelText = (TextView) findViewById(R.id.batval);
        loading = (ProgressBar) findViewById(R.id.loading);
        mHandler = new Handler();

        lv = (ListView) findViewById(R.id.list);
        scan = (Button) findViewById(R.id.button);
        stop = (Button) findViewById(R.id.button1);

        cmb=new ChatMessageBox();
        progbat=(ProgressBar)findViewById(R.id.progbat);

        currentRide = (FloatingActionButton) findViewById(R.id.currentRide);
        ridegroup = (Spinner) findViewById(R.id.selectridegroup);
        startride = (Button) findViewById(R.id.startride);
        ridetitle = (TextView) findViewById(R.id.ridetitle);
        devname = (TextView) findViewById(R.id.devname);
        devsts = (TextView) findViewById(R.id.devsts);
        counttime= (TextView) findViewById(R.id.counttime);
        batbar=(ProgressBar)findViewById(R.id.batbar);
        hidelist = (ListView) findViewById(R.id.hidelist);
        ridetimer= (TextView) findViewById(R.id.ridetimer);
        selectedgroup = (TextView) findViewById(R.id.selectedgroup);
        stop.setVisibility(View.GONE);
        scan.setVisibility(View.VISIBLE);
        counttime.setVisibility(View.INVISIBLE);

        batbar.setVisibility(View.VISIBLE);
        loading.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        batbar.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.VISIBLE);
        mBatteryLevelText.setVisibility(View.INVISIBLE);
        progbat.setVisibility(View.INVISIBLE);
        devname.setVisibility(View.INVISIBLE);
        devsts.setVisibility(View.INVISIBLE);
        selectedgroup.setVisibility(View.INVISIBLE);
        ridetitle.setVisibility(View.INVISIBLE);
        ridetimer.setVisibility(View.INVISIBLE);
        ridegroup.setVisibility(View.INVISIBLE);
        startride.setVisibility(View.INVISIBLE);
    //Initiate Ride Status value
        LocationSharing.getRideStatus(user);


//Add spinner value

        initList();

        if (isServiceRunning(TrackerService.class)) {

            // Inputs have previously been stored, start validation.
            checkLocationPermission();

        }
        else
        {
            Log.e("Location","check Location Permission");
            checkLocationPermission();

        }

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        scanLeDevice(true);
        /* select pairing device in the list*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                pos = position;

                if (device == null) return;


                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
               value=LocationSharing.getValue();

                //Binding the paring device to the service
                 gattServiceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                startService(gattServiceIntent);
              //  Log.e("Address #######", device.getAddress());
                if(value!=null) {
                    if (value.equalsIgnoreCase("0")) {
                        batbar.setVisibility(View.VISIBLE);
                        scan.setVisibility(View.INVISIBLE);
                        stop.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                        stop.setText("Disconnect");
                        devsts.setText("Device Status : Connected");
                        devname.setText("Device Name : " + device.getName());
                        progbat.setVisibility(View.VISIBLE);
                        devname.setVisibility(View.VISIBLE);
                        devsts.setVisibility(View.VISIBLE);
                        ridetitle.setVisibility(View.VISIBLE);
                        ridegroup.setVisibility(View.VISIBLE);
                        startride.setVisibility(View.VISIBLE);
                        mBatteryLevelText.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.INVISIBLE);
                    }
                    else  if (value.equalsIgnoreCase("3")) {
                        r.starttime();
                        ridetimer.setVisibility(View.VISIBLE);

                        counttime.setVisibility(View.VISIBLE);
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" +user.getEntityID() );
                        new LocationSharing();
                        ref.addChildEventListener(new ChildEventListener() {



                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                                if(dataSnapshot.getKey().equalsIgnoreCase("ride_group")) {
                                    String values = (String) dataSnapshot.getValue();
                                    Log.e("3",values);
                                    GnameGid=values.split(",");
                                    ThreadIdStore t = new ThreadIdStore();
                                    BThread thread=new BThread();
                                    thread.setId((long)ThreadIdStore.getid());
                                    String st = GnameGid[0];
                                    currentRide.setVisibility(View.VISIBLE);
                                    startride.setText("Stop Ride");
                                    selectedgroup.setVisibility(View.VISIBLE);
                                    selectedgroup.setText(st);

                                    ridegroup.setVisibility(View.INVISIBLE);
                                    t.setid(Integer.parseInt(GnameGid[1]));
                                    RideStatus=true;
                                    int id = ThreadIdStore.getid();
                                    threadId=(int)adapter.getItem(id).getId();
                                    Log.e("position id thread",threadId+"");
                                    setCurrentRideEntity(adapter.getItem(id).getEntityId(),st+","+id);
                                    getlastDate();
                                    rideAdmin=true;
                                    batbar.setVisibility(View.VISIBLE);
                                    scan.setVisibility(View.INVISIBLE);
                                    stop.setVisibility(View.VISIBLE);
                                    loading.setVisibility(View.INVISIBLE);
                                    stop.setText("Disconnect");
                                    devsts.setText("Device Status : Connected");
                                    devname.setText("Device Name : " + device.getName());
                                    progbat.setVisibility(View.VISIBLE);
                                    devname.setVisibility(View.VISIBLE);
                                    devsts.setVisibility(View.VISIBLE);
                                    ridetitle.setVisibility(View.VISIBLE);
                                    startride.setVisibility(View.VISIBLE);
                                    mBatteryLevelText.setVisibility(View.VISIBLE);
                                    lv.setVisibility(View.INVISIBLE);
                                 //  TrackerService. startListener();

                                }
                            }



                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }


                        });

                    }
                    else  if (value.equalsIgnoreCase("1")) {
                        initiatePopupWindow();

                    }

                    else  if (value.equalsIgnoreCase("2")) {
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" +user.getEntityID() );
                        new LocationSharing();
                        r.starttime();
                        ridetimer.setVisibility(View.VISIBLE);

                        counttime.setVisibility(View.VISIBLE);
                        ref.addChildEventListener(new ChildEventListener() {



                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {




                                if(dataSnapshot.getKey().equalsIgnoreCase("ride_group")) {
                                    String values;
                                    values  = (String) dataSnapshot.getValue();
                                    Log.e("2",values);
                                    GnameGid=values.split(",");
                                    ThreadIdStore t = new ThreadIdStore();
                                    BThread thread=new BThread();
                                    thread.setId((long)ThreadIdStore.getid());
                                    String st = GnameGid[0];

                                    currentRide.setVisibility(View.VISIBLE);
                                    startride.setText("Exit Ride");
                                    selectedgroup.setVisibility(View.VISIBLE);
                                    selectedgroup.setText(st);
                                    rideAdmin=true;
                                    ridegroup.setVisibility(View.INVISIBLE);
                                    t.setid(Integer.parseInt(GnameGid[1]));
                                    RideStatus=true;
                                    int id = ThreadIdStore.getid();
                                    threadId=(int)adapter.getItem(id).getId();
                                    Log.e("position id thread",threadId+"");
                                    setCurrentRideEntity(adapter.getItem(id).getEntityId(),st+","+id);
                                    getlastDate();
                                    batbar.setVisibility(View.VISIBLE);
                                    scan.setVisibility(View.INVISIBLE);
                                    stop.setVisibility(View.VISIBLE);
                                    loading.setVisibility(View.INVISIBLE);
                                    stop.setText("Disconnect");
                                    devsts.setText("Device Status : Connected");
                                    devname.setText("Device Name : " + device.getName());
                                    progbat.setVisibility(View.VISIBLE);
                                    devname.setVisibility(View.VISIBLE);
                                    devsts.setVisibility(View.VISIBLE);
                                    ridetitle.setVisibility(View.VISIBLE);
                                    startride.setVisibility(View.VISIBLE);
                                    mBatteryLevelText.setVisibility(View.VISIBLE);
                                    lv.setVisibility(View.INVISIBLE);
                               //     TrackerService.  startListener();

                                }
                            }



                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }

                        });


                    }
                }




            }
        });

        /*Remove firs item(Choose group) in a list when button is clicked */
        ridegroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (k == 0) {

                    texts.remove(0);
                    k = 1;

                }
                adapter1.notifyDataSetChanged();
                LocationSharing.displaying();
                LocationSharing.count=1;
                return false;
            }
        });

        //choose riding group position
        ridegroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                   // Log.e("Position", i + "");
                    threadId = i - 1;
                   // Log.e("Private###########", threadId + "");

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing

            }
        });

//        currentRide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id = ThreadIdStore.getid();
//
//                       startChatActivityForID(adapter.getItem(id).getId());
//
//            }
//        });



        //open riding group thread activity
        currentRide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        v.setY(event.getRawY() + dY);
                        v.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            int id = ThreadIdStore.getid();
                            startChatActivityForID(adapter.getItem(id).getId());

                        }

                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        int id = ThreadIdStore.getid();
                        startChatActivityForID(adapter.getItem(id).getId());
                        break;

                    default:
                        return false;

                }
                return true;
            }
        });
//
        /*start riding group when riding grop is selected*/
        startride.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

       //
        if(ridegroup.getSelectedItemPosition()!=0||ridegroup.getSelectedItem().toString().trim().equalsIgnoreCase("Create New Group")||rideAdmin==true) {
        if (startride.getText().toString().equalsIgnoreCase("Start Ride") && ridegroup.getSelectedItemPosition()!=0) {
        new LocationSharing();
        ThreadIdStore t = new ThreadIdStore();
            BThread thread=new BThread();
            thread.setId((long)ThreadIdStore.getid());
        String st = ridegroup.getSelectedItem().toString();
        currentRide.setVisibility(View.VISIBLE);
        startride.setText("Stop Ride");
        selectedgroup.setVisibility(View.VISIBLE);
        selectedgroup.setText(st);
            r.starttime();
            ridetimer.setVisibility(View.VISIBLE);

            counttime.setVisibility(View.VISIBLE);
        ridegroup.setVisibility(View.INVISIBLE);
        t.setid(threadId);
            RideStatus=true;

            int id = ThreadIdStore.getid();
            threadId=(int)adapter.getItem(id).getId();

            setCurrentRideEntity(adapter.getItem(id).getEntityId(),st+","+id);
            getlastDate();
           LocationSharing. setGroupStatus(RideStatus);
        Toast.makeText(getApplicationContext(), "Ride Started", Toast.LENGTH_SHORT).show();
           // TrackerService. startListener();


    } else if (startride.getText().toString().equalsIgnoreCase("Stop Ride")) {
        ThreadIdStore t = new ThreadIdStore();
        currentRide.setVisibility(View.INVISIBLE);
        startride.setText("Start Ride");
        selectedgroup.setVisibility(View.INVISIBLE);
        ridegroup.setAdapter(adapter1);
        ridegroup.setVisibility(View.VISIBLE);
            RideStatus=false;
            r.stoptime();
            counttime.setVisibility(View.INVISIBLE);
            ridetimer.setVisibility(View.INVISIBLE);

            LocationSharing. setGroupStatus(RideStatus);
        t.setid(-1);
        Toast.makeText(getApplicationContext(), "Ride Stoped", Toast.LENGTH_SHORT).show();
    }


        else if (startride.getText().toString().equalsIgnoreCase("Exit Ride")) {
            ThreadIdStore t = new ThreadIdStore();
            currentRide.setVisibility(View.INVISIBLE);
            startride.setText("Start Ride");
            selectedgroup.setVisibility(View.INVISIBLE);
            ridegroup.setAdapter(adapter1);
            ridegroup.setVisibility(View.VISIBLE);
            RideStatus=false;
            r.stoptime();
            counttime.setVisibility(View.INVISIBLE);
            ridetimer.setVisibility(View.INVISIBLE);

            Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + user.getEntityID());
            Firebase childref = ref.child("ride_status");
            childref.setValue("0");
            childref = ref.child("ride_group");
            childref.setValue("null");
            t.setid(-1);
            Toast.makeText(getApplicationContext(), "Ride Stoped", Toast.LENGTH_SHORT).show();
        }

        else {

        startPickFriendsActivity();


    }
}
        else
        {
            Toast.makeText(getApplicationContext(), "Choose Ride Group", Toast.LENGTH_SHORT).show();
//            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

        }


    }
});

        //Scan bluetooth device
        scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                mLeDeviceListAdapter.clear();

                mLeDeviceListAdapter.notifyDataSetChanged();
                scanLeDevice(true);

            }
        });

        //Stop bluetooth device
        stop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                mLeDeviceListAdapter.clear();
                // scanLeDevice(false);
                mLeDeviceListAdapter.notifyDataSetChanged();
                mBluetoothLeService.disconnect();

             //   Toast.makeText(getApplicationContext(), user.metaStringForKey(BDefines.Keys.BMac),Toast.LENGTH_SHORT).show();


                stop.setVisibility(View.GONE);

                scan.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);
                lv.setVisibility(View.VISIBLE);
                mBatteryLevelText.setVisibility(View.INVISIBLE);
                progbat.setVisibility(View.INVISIBLE);
                selectedgroup.setVisibility(View.INVISIBLE);
                batbar.setVisibility(View.INVISIBLE);
                devname.setVisibility(View.INVISIBLE);
                devsts.setVisibility(View.INVISIBLE);
                currentRide.setVisibility(View.INVISIBLE);
                counttime.setVisibility(View.INVISIBLE);
                ridetimer.setVisibility(View.INVISIBLE);

                ridetitle.setVisibility(View.INVISIBLE);
                ridegroup.setVisibility(View.INVISIBLE);
                startride.setVisibility(View.INVISIBLE);

            }
        });

        //Initiate location of the current user and selected riding group
        new LocationSharing();
        actionBar = getActionBar();
        check=R.id.Bottomone;

        if (actionBar != null) {
            actionBar.setTitle("Ride");
            actionBar.setLogo(R.color.navigation_tab_pressed);
        }
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
        // bottomNavigation.inflateMenu(R.menu.menu_firstmain);
        fragmentManager = this.getFragmentManager();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                fragment=null;
                //Explore Fragment
                if(id==R.id.Bottomfour)
                {

                    for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                        fragmentManager.popBackStack();
                    }
                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    if (gpsTracker.canGetLocation()) {

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();


                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());

                        Firebase childref=ref.child("user mail");
                        childref.setValue(user.getMetaEmail());
                        childref=ref.child("user name");
                        childref.setValue(user.getMetaName());
                         childref = ref.child("latitude");
                        childref.setValue(String.valueOf(latitude));
                        childref = ref.child("langitude");
                        childref.setValue(String.valueOf(longitude));

                    }

                    lv.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    stop.setVisibility(View.INVISIBLE);
                    selectedgroup.setVisibility(View.INVISIBLE);
                    ridetitle.setVisibility(View.INVISIBLE);
                    ridegroup.setVisibility(View.INVISIBLE);
                    startride.setVisibility(View.INVISIBLE);
                    progbat.setVisibility(View.INVISIBLE);
                    mBatteryLevelText.setVisibility(View.INVISIBLE);
                    devname.setVisibility(View.INVISIBLE);
                    devsts.setVisibility(View.INVISIBLE);
                    batbar.setVisibility(View.INVISIBLE);
                    counttime.setVisibility(View.INVISIBLE);

                    ridetimer.setVisibility(View.INVISIBLE);


                    if(check!=id) {

                        fragment1 = new MainFragment();
                        actionBar = getActionBar();
                        check=id;
                        if (actionBar != null) {
                            actionBar.setTitle("Explore");

                        }
                        if(fragment1!=null) {
                            fragmentManager1=getSupportFragmentManager() ;
                            final android.support.v4.app.FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                            transaction1.addToBackStack(null);
                            transaction1.replace(R.id.main_container, fragment1).commit();

                        }
                    }
                }
                //Chat Fragment
                if(id==R.id.Bottomthree)
                {

                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    if (gpsTracker.canGetLocation()) {

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();


                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());

                        Firebase childref=ref.child("user mail");
                        childref.setValue(user.getMetaEmail());
                        childref=ref.child("user name");
                        childref.setValue(user.getMetaName());
                      childref = ref.child("latitude");
                        childref.setValue(String.valueOf(latitude));
                        childref = ref.child("langitude");
                        childref.setValue(String.valueOf(longitude));
                        // Toast.makeText(getApplicationContext(), "value added...", Toast.LENGTH_LONG).show();
                    }
                    batbar.setVisibility(View.INVISIBLE);

                    lv.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    stop.setVisibility(View.INVISIBLE);
                    selectedgroup.setVisibility(View.INVISIBLE);
                    ridetitle.setVisibility(View.INVISIBLE);
                    ridegroup.setVisibility(View.INVISIBLE);
                    startride.setVisibility(View.INVISIBLE);
                    progbat.setVisibility(View.INVISIBLE);
                    devname.setVisibility(View.INVISIBLE);
                    devsts.setVisibility(View.INVISIBLE);
                    counttime.setVisibility(View.INVISIBLE);
                    ridetimer.setVisibility(View.INVISIBLE);

                    mBatteryLevelText.setVisibility(View.INVISIBLE);
                    if(check!=id) {
                        fragment = new ThreeFragment();
                        check=id;
                        actionBar = getActionBar();
                        if (actionBar != null) {
                            actionBar.setTitle("Chat");
                        }
                    }

                }
                //Location Fragment
                else if(id==R.id.Bottomtwo)
                {
                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    if (gpsTracker.canGetLocation()) {

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();


                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());

                        Firebase childref=ref.child("user mail");
                        childref.setValue(user.getMetaEmail());
                        childref=ref.child("user name");
                        childref.setValue(user.getMetaName());
                         childref = ref.child("latitude");
                        childref.setValue(String.valueOf(latitude));
                        childref = ref.child("langitude");
                        childref.setValue(String.valueOf(longitude));
                        //  Toast.makeText(getApplicationContext(), "value added...", Toast.LENGTH_LONG).show();
                    }
                    lv.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    stop.setVisibility(View.INVISIBLE);
                    counttime.setVisibility(View.INVISIBLE);
                    ridetimer.setVisibility(View.INVISIBLE);

                    selectedgroup.setVisibility(View.INVISIBLE);
                    ridetitle.setVisibility(View.INVISIBLE);
                    ridegroup.setVisibility(View.INVISIBLE);
                    startride.setVisibility(View.INVISIBLE);
                    devname.setVisibility(View.INVISIBLE);
                    devsts.setVisibility(View.INVISIBLE);
                    progbat.setVisibility(View.INVISIBLE);
                    mBatteryLevelText.setVisibility(View.INVISIBLE);
                    batbar.setVisibility(View.INVISIBLE);

                    if(check!=id) {
                        fragment = new TwoFragment();
                        check=id;
                        actionBar = getActionBar();
                        if (actionBar != null) {
                            actionBar.setTitle("Locate");
                        }
                    }
                }
                //Bluetooth Activity Fragment
                else if (id==R.id.Bottomone){

                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    if (gpsTracker.canGetLocation()) {

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();


                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());

                        Firebase childref=ref.child("user mail");
                        childref.setValue(user.getMetaEmail());
                        childref=ref.child("user name");
                        childref.setValue(user.getMetaName());
                         childref = ref.child("latitude");
                        childref.setValue(String.valueOf(latitude));
                        childref = ref.child("langitude");
                        childref.setValue(String.valueOf(longitude));
                        //  Toast.makeText(getApplicationContext(), "value added...", Toast.LENGTH_LONG).show();
                    }

                    if(check!=id) {
                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                            fragmentManager.popBackStack();
                        }
                        if(mBluetoothLeService!=null){
                            lv.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            stop.setText("Disconnect");
                            batbar.setVisibility(View.VISIBLE);
                            devname.setVisibility(View.VISIBLE);
                            devsts.setVisibility(View.VISIBLE);
                            stop.setVisibility(View.VISIBLE);
                            progbat.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                            batbar.setVisibility(View.VISIBLE);
                            counttime.setVisibility(View.VISIBLE);
                            ridetimer.setVisibility(View.VISIBLE);

                            selectedgroup.setVisibility(View.INVISIBLE);
                            mBatteryLevelText.setVisibility(View.VISIBLE);
                            ridetitle.setVisibility(View.VISIBLE);
                            ridegroup.setVisibility(View.VISIBLE);
                            startride.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            stop.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.VISIBLE);
                            batbar.setVisibility(View.INVISIBLE);
                            counttime.setVisibility(View.INVISIBLE);
                            ridetimer.setVisibility(View.INVISIBLE);

                            selectedgroup.setVisibility(View.INVISIBLE);
                            progbat.setVisibility(View.INVISIBLE);
                            mBatteryLevelText.setVisibility(View.INVISIBLE);
                            ridetitle.setVisibility(View.INVISIBLE);
                            ridegroup.setVisibility(View.INVISIBLE);
                            devname.setVisibility(View.INVISIBLE);
                            devsts.setVisibility(View.INVISIBLE);
                            startride.setVisibility(View.INVISIBLE);
                            batbar.setVisibility(View.INVISIBLE);

                        }
                        actionBar = getActionBar();
                        check = id;
                        if (actionBar != null) {
                            actionBar.setTitle("Ride");
                        }
                    }
                }
                if(id==R.id.Bottomone||id==R.id.Bottomtwo||id==R.id.Bottomthree){



                    if(fragment1!=null){
                        for (int i = 0; i < fragmentManager1.getBackStackEntryCount(); ++i) {
                            fragmentManager1.popBackStack();
                        }

                    }

                }
                if(fragment!=null&& id!=R.id.Bottomfour) {
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.main_container, fragment).commit();

                }
                return true;
            }
        });




    }




    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.


    private void initList() {
        adapter=null;
        adapter1=null;
        loadData();

      adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, texts);
        adapter1.setDropDownViewResource(R.layout.spinner_item);

        ridegroup.setAdapter(adapter1);



    }


    /*load group names to spinner data value*/

    public void loadData() {
        adapter=null;
        adapter = new ChatSDKThreadsListAdapter(this);

        adapter.setThreadItems(BNetworkManager.sharedManager().getNetworkAdapter().threadItemsWithType(BThread.Type.Private, adapter.getItemMaker()));
        hidelist.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        hidelist.setVisibility(View.VISIBLE);

        getAllValues();
    }
    public static  void getAllValues() {

        View parentView;
//        texts=null;
        texts = new ArrayList<String>();

        texts.add("Choose Group");
        texts.add("Create New Group ");


//        for (int i = 0; i < hidelist.getCount(); i++) {
//            parentView = getViewByPosition(i, hidelist);
//
//            String getString1 = ((TextView) parentView
//                    .findViewById(R.id.chat_sdk_txt)).getText().toString();
//            texts.add(getString1);
//
//
//
//
//        }
    }
    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {

            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    protected void onStart() {
        this.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(gattServiceIntent);
//
//        mBluetoothLeService = null;
//        unbindService(mServiceConnection);


    }

    public void setCurrentRideEntity(String currentRideEntity,String groupName) {
        this.currentRideThreadId = currentRideEntity;
        this.RideGroupName=groupName;
    }
    public static String getCurrentRideEntity()
    {
        return currentRideThreadId;
    }
    public static String getCurrentRideGroupName()
    {
        return RideGroupName;
    }



    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);


//            mBatteryLevelText.setText(resourceTemp + " "+level  );
//            mBatteryLevelProgress.setProgress(level);
            //  Log.e("battery",resourceTemp+": "+level);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(TrackerService.STATUS_INTENT));
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(device.getAddress());
            Log.d(TAG, "Connect request result=" + result);
        }
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        lv.setAdapter(mLeDeviceListAdapter);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        if (mBluetoothLeService != null) {
            //  final BluetoothDevice device = mLeDeviceListAdapter.getDevice(pos);

            final boolean result = mBluetoothLeService.connect(address);
            Log.e("TAG", "Connect request result=" + result);
        }

        BatchedEvent batchedEvents = new BatchedEvent(APP_EVENT_TAG, "", Event.Type.AppEvent, handler);

        batchedEvents.setBatchedAction(Event.Type.AppEvent, 3000, new Batcher.BatchedAction<String>() {
            @Override
            public void triggered(List<String> list) {
                loadDataOnBackground();
            }
        });

        getNetworkAdapter().getEventManager().removeEventByTag(APP_EVENT_TAG);
        getNetworkAdapter().getEventManager().addEvent(batchedEvents);
        //scanLeDevice(true);
    }
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 1:
                    adapter.setThreadItems((List<ChatSDKThreadsListAdapter.ThreadListItem>) msg.obj);

                    break;
            }
        }
    };

    public AbstractNetworkAdapter getNetworkAdapter() {
        return BNetworkManager.sharedManager().getNetworkAdapter();
    }
    /*Load group names from updating process*/
    public void loadDataOnBackground() {



        final boolean isFirst;
        if (uiUpdater != null)
        {
            isFirst = false;
            uiUpdater.setKilled(true);
            ChatSDKThreadPool.getInstance().removeSchedule(uiUpdater);
        }
        else
        {
            isFirst = true;
        }

        final boolean noItems = adapter != null && adapter.getThreadItems().size() == 0;
        if (isFirst && noItems) {

        }

        uiUpdater = new UIUpdater() {
            @Override
            public void run() {

                if (isKilled() && !isFirst && noItems)
                    return;

                Message message = new Message();
                message.what = 1;
                message.obj =BNetworkManager.sharedManager().getNetworkAdapter().threadItemsWithType(BThread.Type.Private, adapter.getItemMaker());


                handler.sendMessageAtFrontOfQueue(message);

                uiUpdater = null;
            }
        };

        ChatSDKThreadPool.getInstance().scheduleExecute(uiUpdater, noItems && isFirst ? 0 : isFirst ? 1 : 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.



        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {

            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                }
                break;
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
      //  unregisterReceiver(mGattUpdateReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        mLeDeviceListAdapter.clear();
        if (callStateReceiver != null) {
          //  unregisterReceiver(callStateReceiver);
            callStateReceiver = null;
        }
    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    loading.setVisibility(View.INVISIBLE);

                        lv.setVisibility(View.VISIBLE);


                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);
            lv.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            new LocationSharing();
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

//
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = FirstMainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

        /**
          *set Bluetooth device batery level
          */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayData(String data) {
        if (data != null) {
               // Log.e("battery", data);
            batval=data.replace("c","").trim();
          //  Log.e("battery", batval);
            mBatteryLevelText.setText(batval+" %");
            set();



        }
            }


            void set(){
                progbat.setProgress(Integer.parseInt(batval));


            }
//Received bluetooth button pressed status for Google Voice Assistance
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayData1(String data)  {
        if (data != null) {
            Log.e("a001", data);
            data1=data;
//            if(currentRide.getVisibility()== View.VISIBLE) {
//
//
//                if (data.contains("01")) {
//                    startaudio = 1;
//
//                    cmb.onFinishInflate(getApplicationContext());
//                    cmb.init(getApplicationContext());
//                } else {
//                    if (startaudio == 1) {
//                        cmb.outit(getApplicationContext());
//                        startaudio = 0;
//
//                    }
//                }
//            }
                final PhoneStateListener callStateListener = new PhoneStateListener() {
                    public void onCallStateChanged(int state, String incomingNumber) {
                        if (state == TelephonyManager.CALL_STATE_RINGING) {

                            if (data1.contains("01")) {

                                registerCallStateReceiver();
                                updateWindowFlags();
                                acceptCall();
                            }
//


                        }
                       else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {


                            if (data1.contains("01")) {
                                try {
                                    disconnectCall();
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                       else if (state == TelephonyManager.CALL_STATE_IDLE) {

                            if (data1.contains("01")) {
//                                Intent googleNowIntent = new Intent("android.intent.action.VOICE_ASSIST");
//                                startActivity(googleNowIntent);


                                startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
                            }
                        }
                    }
                };
                telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);






//            if(data.contains("01"))
//            {
//                Intent googleNowIntent = new Intent("android.intent.action.VOICE_ASSIST");
//                startActivity(googleNowIntent);
//            }
        }

    }
//received bluetooth utton pressed status for audio recording


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayData2(String data) {
        if (data != null) {
             Log.e("a008", data);
            if(currentRide.getVisibility()== View.VISIBLE) {


                if (data.contains("01")) {
                    startaudio = 1;

                    cmb.onFinishInflate(getApplicationContext());
                    cmb.init(getApplicationContext());
                } else {
                    if (startaudio == 1) {
                        cmb.outit(getApplicationContext());
                        startaudio = 0;

                    }
                }
            }
//
//            if (data.contains("01")) {
//                Intent googleNowIntent = new Intent("android.intent.action.VOICE_ASSIST");
//                startActivity(googleNowIntent);
//            }
        }


            }


    public void startChatActivityForID(long id){
        if (chatSDKUiHelper != null)
            chatSDKUiHelper.startChatActivityForID(id);
    }

    public void startPickFriendsActivity() {
        if (chatSDKUiHelper != null)
            chatSDKUiHelper.startPickFriendsActivity();
    }

//Receiveind device value from service.
    BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;


            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).substring(0, 1).equals("c")) {
                    displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }
                if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).substring(0, 1).equals("a")) {
                    displayData1(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }
                if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).substring(0, 1).equals("b")) {
                    displayData2(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }
            }
        }
    };


    //Discover and update the bluetooth service to the intent

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        return true;
    }

    public void setChatSDKUiHelper(ChatSDKUiHelper chatSDKUiHelper) {
        this.chatSDKUiHelper = chatSDKUiHelper;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//        Intent i=new Intent(getApplicationContext(),HelpInfo.class);
//           startActivity(i);
            logout();

            return true;
        }
        if (id == R.id.action_setting) {
        Intent i=new Intent(getApplicationContext(),Profile_Setting.class);
           startActivity(i);
           // initiatePopupWindow1();

           // createprofile();
            return true;





        }
        return super.onOptionsItemSelected(item);
    }




    public void logout()
    {
        BNetworkManager.sharedManager().getNetworkAdapter().logout();
        Intent intent=new Intent(getApplicationContext(),ChatSDKLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        exitHelperOne.triggerExit();
    }


    private void setTrackingStatus(int status) {
        boolean tracking = status == R.string.tracking;


    }

    /**
     * Receives status messages from the tracking service.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTrackingStatus(intent.getIntExtra(getString(R.string.status), 0));
        }
    };


    /**
     * First validation check - ensures that required inputs have been
     * entered, and if so, store them and runs the next check.
     * Second validation check - ensures the app has location permissions, and
     * if not, requests them, otherwise runs the next check.
     */
    private void checkLocationPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (locationPermission != PackageManager.PERMISSION_GRANTED
                || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST);
        } else {


            checkGpsEnabled();
        }
    }

    /**
     * Third and final validation check - ensures GPS is enabled, and if not, prompts to
     * enable it, otherwise all checks pass so start the location tracking service.
     */
    private void checkGpsEnabled() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            reportGpsError();
        } else {
            resolveGpsError();


            startLocationService();
        }
    }
    /*accept or decline the ride when ride invitation received from ride admin*/
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) FirstMainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout1 = inflater.inflate(R.layout.ridestatus,
                    (ViewGroup) findViewById(R.id.popup_element_forgot1));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(layout1, Gravity.CENTER, 0, 0);

            final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            final Button cancelButton = (Button) layout1.findViewById(R.id.oldcancel);
            final TextView oldmail=(TextView)layout1.findViewById(R.id.oldmail);
            Button oldsend=(Button)layout1.findViewById(R.id.oldsend);
            //accept ride invitation status
            oldsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + user.getEntityID());
                    Firebase childref = ref.child("ride_status");
                    childref.setValue("2");
                    pw.dismiss();
                    Firebase ref1 = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" +user.getEntityID() );

                    ref1.addChildEventListener(new ChildEventListener() {



                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {




                            if(dataSnapshot.getKey().equalsIgnoreCase("ride_group")) {
                                String values;
                                values  = (String) dataSnapshot.getValue();
                                Log.e("2",values);
                                GnameGid=values.split(",");
                                ThreadIdStore t = new ThreadIdStore();
                                BThread thread=new BThread();
                                new LocationSharing();
                                thread.setId((long)ThreadIdStore.getid());
                                String st = GnameGid[0];
                                currentRide.setVisibility(View.VISIBLE);
                                startride.setText("Exit Ride");
                                selectedgroup.setVisibility(View.VISIBLE);
                                selectedgroup.setText(st);
                                rideAdmin=true;
                                ridegroup.setVisibility(View.INVISIBLE);
                                t.setid(Integer.parseInt(GnameGid[1]));
                                RideStatus=true;
                                int id = ThreadIdStore.getid();
                                threadId=(int)adapter.getItem(id).getId();
                                Log.e("position id thread",threadId+"");
                                setCurrentRideEntity(adapter.getItem(id).getEntityId(),st+","+id);
                                RideTimer r=new RideTimer();
                                r.starttime();
                                batbar.setVisibility(View.VISIBLE);
                                scan.setVisibility(View.INVISIBLE);
                                stop.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.INVISIBLE);
                                stop.setText("Disconnect");
                                devsts.setText("Device Status : Connected");
                                devname.setText("Device Name : " + device.getName());
                                progbat.setVisibility(View.VISIBLE);
                                devname.setVisibility(View.VISIBLE);
                                devsts.setVisibility(View.VISIBLE);
                                ridetitle.setVisibility(View.VISIBLE);
                                startride.setVisibility(View.VISIBLE);
                                counttime.setVisibility(View.VISIBLE);
                                ridetimer.setVisibility(View.VISIBLE);
                                mBatteryLevelText.setVisibility(View.VISIBLE);
                                lv.setVisibility(View.INVISIBLE);


                            }
                        }



                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }

                    });

                }

            });
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //popupmenu close button action when ridestatus is 0
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + user.getEntityID());
            Firebase childref = ref.child("ride_status");
            childref.setValue("0");
            childref = ref.child("ride_group");
            childref.setValue("null");

            pw.dismiss();
            batbar.setVisibility(View.VISIBLE);
            scan.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            stop.setText("Disconnect");
            devsts.setText("Device Status : Connected");
            devname.setText("Device Name : " + device.getName());
            progbat.setVisibility(View.VISIBLE);
            devname.setVisibility(View.VISIBLE);
            devsts.setVisibility(View.VISIBLE);
            ridetitle.setVisibility(View.VISIBLE);
            ridegroup.setVisibility(View.VISIBLE);
            startride.setVisibility(View.VISIBLE);
            mBatteryLevelText.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
        }
    };
    /**
     * Callback for location permission request - if successful, run the GPS check.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            // We request storage perms as well as location perms, but don't care
            // about the storage perms - it's just for debugging.
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        reportPermissionsError();
                    } else {
                        resolvePermissionsError();
                        checkGpsEnabled();
                    }
                }
            }
        }
    }

    private void startLocationService() {
        //  we start the service
        Log.e("Location","we started service");

        startService(new Intent(this, TrackerService.class));
    }




    private void reportPermissionsError() {

        Snackbar snackbar = Snackbar
                .make(
                        findViewById(R.id.activity_main),
                        getString(R.string.location_permission_required),
                        Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.provider.Settings
                                .ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(
                android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void resolvePermissionsError() {
        if (mSnackbarPermissions != null) {
            mSnackbarPermissions.dismiss();
            mSnackbarPermissions = null;
        }
    }

    private void reportGpsError() {

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.activity_main), getString(R.string
                        .gps_required), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    private void resolveGpsError() {
        if (mSnackbarGps != null) {
            mSnackbarGps.dismiss();
            mSnackbarGps = null;
        }
    }


    public static void getlastDate() {




        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/threads/" + FirstMainActivity.getCurrentRideEntity());
        final   com.firebase.client.Query childref=ref.child("messages").orderByKey().limitToLast(1);
        childref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                for(com.firebase.client.DataSnapshot datas:dataSnapshot.getChildren())
                {
//
                    for(com.firebase.client.DataSnapshot datas1:datas.getChildren()) {

                        if(datas1.getKey().equalsIgnoreCase("date")){

                            long value=(long)datas1.getValue();
                            Log.e("Firstmainactivity",value+"");

                            TrackerService. setLastDate(value);
                            TrackerService.startListener();



                        }

                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    private void registerCallStateReceiver() {
        callStateReceiver = new CallStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
    }

    private void updateWindowFlags() {
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }

    public void disconnectCall() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        Method m1 = tm.getClass().getDeclaredMethod("getITelephony");
        m1.setAccessible(true);
        Object iTelephony = m1.invoke(tm);

        Method m3 = iTelephony.getClass().getDeclaredMethod("endCall");

        m3.invoke(iTelephony);
    }

    private void acceptCall() {

        // for HTC devices we need to broadcast a connected headset
        boolean broadcastConnected = MANUFACTURER_HTC.equalsIgnoreCase(Build.MANUFACTURER)
                && !audioManager.isWiredHeadsetOn();

        if (broadcastConnected) {
            broadcastHeadsetConnected(false);
        }

        try {
            try {
                Log.d("","execute input keycode headset hook");
                Runtime.getRuntime().exec("input keyevent " +
                         Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

            } catch (IOException e) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                Log.d("","send keycode headset hook intents");
                String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_HEADSETHOOK));
                Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_HEADSETHOOK));

                sendOrderedBroadcast(btnDown, enforcedPerm);
                sendOrderedBroadcast(btnUp, enforcedPerm);
            }
        } finally {
            if (broadcastConnected) {
                broadcastHeadsetConnected(false);
            }
        }
    }

    private void broadcastHeadsetConnected(boolean connected) {
        Intent i = new Intent(Intent.ACTION_HEADSET_PLUG);
        i.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        i.putExtra("state", connected ? 1 : 0);
        i.putExtra("name", "mysms");
        try {
            sendOrderedBroadcast(i, null);
        } catch (Exception e) {
        }
    }

    private class CallStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}





