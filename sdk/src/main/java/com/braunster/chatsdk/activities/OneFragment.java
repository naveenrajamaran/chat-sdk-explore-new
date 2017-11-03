package com.braunster.chatsdk.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braunster.chatsdk.R;

/**
 * Created by DELL on 6/7/2017.
 */

public class OneFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstantState)
    {

        View v=inflater.inflate(R.layout.layout_one,container,false);

        return v;
    }
}
