package com.articles;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.articles.fragments.BookmarkedArticles;
import com.articles.fragments.NearbyArticles;
import com.articles.fragments.RecentArticles;
import com.braunster.chatsdk.R;

/**
 * Created by DELL on 6/7/2017.
 */

public class MainFragment extends Fragment {

    private RecentArticles fragmentOne;
    private NearbyArticles fragmentTwo;
    private BookmarkedArticles fragmentThree;
    private TabLayout allTabs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.articles_main_fragment_layout, container, false);
        allTabs = (TabLayout) inflatedView.findViewById(R.id.tabs);

        bindWidgetsWithAnEvent();
        setupTabLayout();


        return inflatedView;
    }


    private void setupTabLayout() {
        fragmentOne = new RecentArticles();
        fragmentTwo = new NearbyArticles();
        fragmentThree = new BookmarkedArticles();
        allTabs.addTab(allTabs.newTab().setText("RECENT"),true);
        allTabs.addTab(allTabs.newTab().setText("NEARBY"));
        allTabs.addTab(allTabs.newTab().setText("BOOKMARKS"));
    }
    private void bindWidgetsWithAnEvent()
    {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(fragmentOne);
                break;
            case 1 :
                replaceFragment(fragmentTwo);
                break;
            case 2 :
                replaceFragment(fragmentThree);
                break;

        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}