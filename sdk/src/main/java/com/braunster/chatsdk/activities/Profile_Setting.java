package com.braunster.chatsdk.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.astuetz.pagersslidingtabstrip.PagerSlidingTabStrip;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.adapter.AbstractChatSDKTabsAdapter;
import com.braunster.chatsdk.adapter.ChatSDKUsersListAdapter;
import com.braunster.chatsdk.adapter.PagerAdapterTabs;
import com.braunster.chatsdk.adapter.ProfileAdapter;
import com.braunster.chatsdk.adapter.abstracted.AbstractProfileAdapter;
import com.braunster.chatsdk.fragments.ChatSDKBaseFragment;
import com.braunster.chatsdk.fragments.ChatcatProfileFragment;

import java.lang.reflect.Field;

/**
 * Created by DELL on 6/7/2017.
 */

public class Profile_Setting extends FragmentActivity {
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_setting);
         mViewPager = (ViewPager) findViewById(R.id.profile_pager);
        ActionBar action =getActionBar();
        action.setLogo(R.color.navigation_tab_pressed);
        action.setTitle("Profile");
        createprofile();


    }
    //set profile page as a fragment page.
    private void createprofile() {
        final FragmentManager mFragmentManager;


        AbstractProfileAdapter adapter1;

        adapter1 = new ProfileAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter1);


        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ProfileAdapter(getFragmentManager()));





    }
}
