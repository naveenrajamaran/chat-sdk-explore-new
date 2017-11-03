package com.braunster.chatsdk.activities;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.braunster.chatsdk.Utils.Debug;
import com.braunster.chatsdk.Utils.FileUtils;
import com.braunster.chatsdk.Utils.helper.ChatSDKChatHelper;
import com.braunster.chatsdk.activities.abstracted.ChatSDKAbstractChatActivity;
import com.braunster.chatsdk.dao.core.DaoCore;
import com.braunster.chatsdk.network.BDefines;
import com.github.johnpersano.supertoasts.SuperToast;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by DELL on 9/29/2017.
 */

public class ChatMessageBox  {

    public static final String TAG = ChatMessageBox.class.getSimpleName();
    public static final boolean DEBUG = Debug.ChatMessageBoxView;

    public static final int MODE_SEND = 100;
    public static final int MODE_RECORD = 101;
    public static final int MODE_RECORDING = 101;

     int currentMode = MODE_SEND;

    private MediaRecorder recorder;
    private String recordingPath;
    Toast toastRecording;



    /** The alert toast that the app will use to alert the user.*/
    protected SuperToast alertToast;



    void outit(Context context)
    {
        if(DEBUG) Timber.v("recording finished");
        Toast.makeText(context,"Recording finished",Toast.LENGTH_SHORT).show();
        currentMode = MODE_RECORD;

        try {

            recorder.stop();
        } catch(RuntimeException rte) { }

        recorder.release();

        // Get and check the duration of the recorded audio message
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(recordingPath);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int duration = Integer.valueOf(durationStr);
        Log.e("duration",durationStr);

        if(duration < BDefines.Options.minAudioLength) {
            Toast.makeText(context, "Minimum audio message length is 2 seconds", Toast.LENGTH_SHORT).show();
        } else {
            String selectedFilePath = FileUtils.getPath(context.getApplicationContext(), Uri.parse(recordingPath));
            Log.e("send",FirstMainActivity.threadId+"");

            ChatSDKChatHelper.sendFileMessage1(selectedFilePath, ChatSDKChatHelper.SEND_AUDIO,FirstMainActivity.threadId);
        }

        recordingPath = context.getCacheDir().getAbsolutePath() + "/" + DaoCore.generateEntity() + BDefines.Options.AudioFormat;
        setupRecorder();
    }


     void init(Context context) {
        currentMode = MODE_RECORD;

        if(DEBUG) Timber.v("recording started");
        Log.e("Start","Recording");
        Toast.makeText(context,"Recording",Toast.LENGTH_SHORT).show();

        currentMode = MODE_RECORDING;

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }







     void onFinishInflate(Context context) {



        if (BDefines.Options.AudioEnabled) {

            currentMode = MODE_RECORD;

            recordingPath = context.getCacheDir().getAbsolutePath() + "/" + DaoCore.generateEntity() + BDefines.Options.AudioFormat;

            setupRecorder();
        }


    }






    private void setupRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(recordingPath);
    }

    /** Send a text message when the done button is pressed on the keyboard.*/



//    public void setMessageBoxOptionsListener(MessageBoxOptionsListener messageBoxOptionsListener) {
//        this.messageBoxOptionsListener = messageBoxOptionsListener;
//    }
//
//    public void setMessageSendListener(ChatMessageBox.MessageSendListener messageSendListener) {
//        this.messageSendListener = messageSendListener;
//    }



    /*Getters and Setters*/
    public void setAlertToast(SuperToast alertToast) {
        this.alertToast = alertToast;
    }

    public SuperToast getAlertToast() {
        return alertToast;
    }


}

