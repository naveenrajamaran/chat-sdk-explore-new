package com.braunster.chatsdk.adapter.abstracted;

import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.astuetz.pagersslidingtabstrip.PagerSlidingTabStrip;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.fragments.ChatSDKBaseFragment;
import com.braunster.chatsdk.fragments.ChatSDKContactsFragment;
import com.braunster.chatsdk.fragments.ChatSDKConversationsFragment;
import com.braunster.chatsdk.fragments.ChatSDKThreadsFragment;
import com.braunster.chatsdk.fragments.ChatcatProfileFragment;

/**
 * Created by DELL on 9/16/2017.
 */

public abstract class AbstractProfileAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    protected final String[] titles = { "Profile"};

    protected ChatSDKBaseFragment[] fragments = new ChatSDKBaseFragment[] { ChatcatProfileFragment.newInstance()};

    protected int[] icnns = new int[] {R.drawable.ic_action_user };

    public static final int Profile = 3, ChatRooms = 1, Contacts = 2, Conversations = 0;

    public AbstractProfileAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public ChatSDKBaseFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getPageIconResId(int position) {
        return icnns[position];
    }



}
