package com.ndtv.explore.views.explore;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ndtv.explore.views.explore.fragment.SlidingFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ndtv.explore.R.layout.activity_main);

        toolbar = (Toolbar) findViewById(com.ndtv.explore.R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);


        }





       /* if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
          //  transaction.addToBackStack(null);
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }*/

        if(savedInstanceState==null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingFragment fragment = new SlidingFragment();
            transaction.replace(com.ndtv.explore.R.id.container, fragment);
            transaction.commit();
        }




    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.ndtv.explore.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.ndtv.explore.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}



