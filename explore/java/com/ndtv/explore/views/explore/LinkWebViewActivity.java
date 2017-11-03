package com.ndtv.explore.views.explore;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class LinkWebViewActivity extends AppCompatActivity {


    private WebView webView1;
    private Toolbar toolbar;
    private String title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ndtv.explore.R.layout.activity_webview);
        //  this.getWindow().requestFeature(Window.FEATURE_PROGRESS);

        toolbar = (Toolbar) findViewById(com.ndtv.explore.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = getIntent().getExtras().getString("title");
         url = getIntent().getExtras().getString("url");


        getSupportActionBar().setTitle(title);

        if (savedInstanceState != null) {
            ((WebView) findViewById(com.ndtv.explore.R.id.webView1)).restoreState(savedInstanceState);
        } else {

            webView1 = (WebView) findViewById(com.ndtv.explore.R.id.webView1);

            webView1.getSettings().setJavaScriptEnabled(true);

            webView1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


            final Activity activity = this;


            webView1.setWebViewClient(new WebViewClient()

            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view,
                                                        String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }
            });

            webView1.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    activity.setProgress(progress * 1000);
                }
            });

            webView1.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Code for WebView goes here
                    webView1.loadUrl(url);
                }
            });



        }


    }



    @Override
    protected void onSaveInstanceState(Bundle outState ){
        ((WebView) findViewById(com.ndtv.explore.R.id.webView1)).saveState(outState);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack()) {
            webView1.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
