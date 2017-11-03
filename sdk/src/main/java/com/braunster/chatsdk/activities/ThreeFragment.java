package com.braunster.chatsdk.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.pagersslidingtabstrip.PagerSlidingTabStrip;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.adapter.AbstractChatSDKTabsAdapter;
import com.braunster.chatsdk.adapter.ChatSDKUsersListAdapter;
import com.braunster.chatsdk.adapter.PagerAdapterTabs;
import com.braunster.chatsdk.fragments.ChatcatProfileFragment;

import java.lang.reflect.Field;

/**
 * Created by DELL on 6/7/2017.
 */

public class ThreeFragment extends Fragment {

    Context context;
    protected AbstractChatSDKTabsAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstantState) {

           View v=inflater.inflate(R.layout.chat_sdk_activity_view_pager,container,false);


        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.pager);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)v.findViewById(R.id.tabs);

        // Only creates the adapter if it wasn't initiated already
        if (adapter == null)
            adapter = new PagerAdapterTabs(getFragmentManager());

        mViewPager.setAdapter(adapter);

        tabs.setViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new PagerAdapterTabs(getChildFragmentManager()));



        return v;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}