package com.braunster.chatsdk.adapter;

import android.app.FragmentManager;

import com.astuetz.pagersslidingtabstrip.PagerSlidingTabStrip;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.adapter.abstracted.AbstractProfileAdapter;
import com.braunster.chatsdk.fragments.ChatSDKBaseFragment;
import com.braunster.chatsdk.fragments.ChatSDKContactsFragment;
import com.braunster.chatsdk.fragments.ChatSDKConversationsFragment;
import com.braunster.chatsdk.fragments.ChatSDKProfileFragment;
import com.braunster.chatsdk.fragments.ChatSDKThreadsFragment;

/**
 * Created by DELL on 9/16/2017.
 */

public class ProfileAdapter extends AbstractProfileAdapter implements PagerSlidingTabStrip.IconTabProvider{

    public ProfileAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ChatSDKBaseFragment[] {
                ChatSDKProfileFragment.newInstance()};

        icnns = new int[] {R.drawable.ic_action_user };
    }
}
