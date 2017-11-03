/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.braunster.chatsdk.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braunster.chatsdk.BuildConfig;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.Utils.Debug;
import com.braunster.chatsdk.activities.abstracted.ChatSDKAbstractLoginActivity;
import com.braunster.chatsdk.adapter.ChatSDKThreadsListAdapter;
import com.braunster.chatsdk.adapter.abstracted.ChatSDKAbstractThreadsListAdapter;
import com.braunster.chatsdk.dao.BThread;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.dao.core.DaoCore;
import com.braunster.chatsdk.dao.entities.BThreadEntity;
import com.braunster.chatsdk.network.BDefines;
import com.braunster.chatsdk.network.BNetworkManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by itzik on 6/8/2014.
 */
//Testing class for group names retriving from firebase.
public class HelpInfo extends Activity {
    ListView groups;
    ArrayList<String> list = new ArrayList<String>();

    private ChatSDKThreadsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.helpinfo);
        groups = (ListView) findViewById(R.id.groups);
        initList();
    }

    private void initList() {
        adapter = new ChatSDKThreadsListAdapter(this);
        loadData();

        groups.setAdapter(adapter);
        groups.setVisibility(View.GONE);
        getAllValues();

    }


    public void loadData() {

        adapter.setThreadItems(BNetworkManager.sharedManager().getNetworkAdapter().threadItemsWithType(BThread.Type.Private, adapter.getItemMaker()));
    }


    public void getAllValues() {
        View parentView = null;

        for (int i = 0; i < groups.getCount(); i++) {
            parentView = getViewByPosition(i, groups);

            String getString = ((TextView) parentView
                    .findViewById(R.id.chat_sdk_txt)).getText().toString();
            Log.e(i+"",getString);


        }

    }

    public View getViewByPosition(int pos, ListView listView) {
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

}
