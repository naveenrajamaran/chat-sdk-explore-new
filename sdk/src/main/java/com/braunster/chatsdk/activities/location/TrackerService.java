/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.braunster.chatsdk.activities.location;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.braunster.chatsdk.BuildConfig;
import com.braunster.chatsdk.R;
import com.braunster.chatsdk.activities.FirstMainActivity;
import com.braunster.chatsdk.activities.GPSTracker;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.network.BNetworkManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class TrackerService extends Service implements LocationListener {

    private static final String TAG = TrackerService.class.getSimpleName();
    public static final String STATUS_INTENT = "status";
    private static boolean startPlay;
    Firebase ref,childref;
    private static final int NOTIFICATION_ID = 1;
    private static final int FOREGROUND_SERVICE_ID = 1;
    private static final int CONFIG_CACHE_EXPIRY = 600;  // 10 minutes.
    private GoogleApiClient mGoogleApiClient;
    static MediaPlayer mediaPlayer;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private PowerManager.WakeLock mWakelock;
    static long lastDate;
    public static long date,type;
    public static String payload,senderUserid;
    public static Queue myQueue = new LinkedList();
    public static BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
    public TrackerService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildNotification();
        setStatusMessage("Connected");


        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
                mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);





       authenticate();

    }
    public static  void startListener() {
        if(getLastDate()==0)
        {
            FirstMainActivity.getlastDate();
        }
        else {
            if(mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            doingProcess();
        }

    }

    private static void doingProcess() {
        //Log.e("TrackerService",getLastDate()+"");

        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/threads/" + FirstMainActivity.getCurrentRideEntity());
        final   com.firebase.client.Query childref=ref.child("messages").orderByKey().limitToLast(1);
      childref.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                int i=0;
              for(final com.firebase.client.DataSnapshot datas:dataSnapshot.getChildren()) {

                      if (datas.getKey().equalsIgnoreCase("date")) {
                          date = (long) datas.getValue();

                      }
                      if (datas.getKey().equalsIgnoreCase("type")) {
                          type = (long) datas.getValue();


                      }
                      if (datas.getKey().equalsIgnoreCase("payload")) {
                          payload = datas.getValue().toString();

                      }
                      if (datas.getKey().equalsIgnoreCase("user-firebase-id")) {
                          senderUserid = datas.getValue().toString();
                          i=1;
                          break;

                  }

              }
                  if(i==1)
                  {

                      if(type==3)
                     {

                         if(date>getLastDate())
                         {

                             if(!user.getEntityID().equalsIgnoreCase(senderUserid))
                             {

                                 Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + senderUserid);
                                    ref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                                        @Override
                                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                                int j=0;

                                            String value="";
                                            for(com.firebase.client.DataSnapshot data:dataSnapshot.getChildren()) {
                                                if (data.getKey().equalsIgnoreCase("ride_status")) {
                                                     value = (String) data.getValue();
                                                 j=1;break;
                                                }
                                            }
                                                if(j==1)
                                                {

                                                if (value.equalsIgnoreCase("2") || value.equalsIgnoreCase("3")) {

                                                    if(mediaPlayer.isPlaying()) {
                                                        myQueue.add(payload);
                                                        Log.e("store","queue");
                                                    }
                                                    else
                                                  {

                                                          audioPlay();

                                                  }

                                                }


                                            }

                                        }

                                        private void audioPlay() {
                                            if(!myQueue.isEmpty()) {
                                                payload = myQueue.remove().toString();
                                            }

                                            String st[];

                                            try {
                                                st=payload.split(",");
                                                Log.e("pl",st[0]);
                                                mediaPlayer.setDataSource(st[0]);

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            mediaPlayer.prepareAsync();
                                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    Log.e("audio", "play");
                                                    mediaPlayer.start();
                                                }
                                            });
                                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mp) {
                                                    mediaPlayer.stop();
                                                    Log.e("audio", "stop");
                                                    setLastDate(date);
                                                    mediaPlayer.reset();
                                                    if(!myQueue.isEmpty())
                                                    {
                                                        audioPlay();
                                                    }

                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
//
                             }
                         }

                     }
                     else {
                          setLastDate(date);
                      }


                  }




          }

          @Override
          public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(FirebaseError firebaseError) {

          }
      });


    }




    public static void setLastDate(long val) {
        lastDate=val;
    }
    private static long getLastDate()
    {
        return lastDate;
    }



    @Override
    public void onDestroy() {

        // Stop the persistent notification.
        mNotificationManager.cancel(NOTIFICATION_ID);
        // Stop receiving location updates.
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    TrackerService.this);
        }
        // Release the wakelock
        if (mWakelock != null) {
            mWakelock.release();
        }
        super.onDestroy();
    }

    private void authenticate() {

                            fetchRemoteConfig();
                            loadPreviousStatuses();

    }

    private void fetchRemoteConfig() {
        long cacheExpiration = CONFIG_CACHE_EXPIRY;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Remote config fetched");
                        mFirebaseRemoteConfig.activateFetched();
                    }
                });
    }

    /**
     * Loads previously stored statuses from Firebase, and once retrieved,
     * start location tracking.
     */
    private void loadPreviousStatuses() {
     /*Automaticallu update the location LatLan value to th firebase */
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
            ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());
           childref = ref.child("user mail");
            childref.setValue(user.getMetaEmail());
            childref = ref.child("user name");
            childref.setValue(user.getMetaName());
            childref = ref.child("latitude");
            childref.setValue(String.valueOf(latitude));
            childref = ref.child("langitude");
            childref.setValue(String.valueOf(longitude));
            Log.e("update","update");




        }
                startLocationTracking();



    }




    private GoogleApiClient.ConnectionCallbacks mLocationRequestCallback = new GoogleApiClient
            .ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle bundle) {
            LocationRequest request = new LocationRequest();
            request.setInterval(mFirebaseRemoteConfig.getLong("LOCATION_REQUEST_INTERVAL"));
            request.setFastestInterval(mFirebaseRemoteConfig.getLong
                    ("LOCATION_REQUEST_INTERVAL_FASTEST"));
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    request, TrackerService.this);


            // Hold a partial wake lock to keep CPU awake when the we're tracking location.
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
            mWakelock.acquire();
        }

        @Override
        public void onConnectionSuspended(int reason) {
            // TODO: Handle gracefully
        }
    };

    /**
     * Starts location tracking by creating a Google API client, and
     * requesting location updates.
     */
    private void startLocationTracking() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(mLocationRequestCallback)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Determines if the current location is approximately the same as the location
     * for a particular status. Used to check if we'll add a new status, or
     * update the most recent status of we're stationary.
     */
    private boolean locationIsAtStatus(Location location, int statusIndex) {

        final Location locationForStatus = new Location("");
        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();

        //Get co-ordinates from each group members.
        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equalsIgnoreCase("langitude")) {
                    String value = (String) dataSnapshot.getValue();

                    locationForStatus.setLatitude(Double.parseDouble(value));
                }
                if (dataSnapshot.getKey().equalsIgnoreCase("latitude")) {
                    String value = (String) dataSnapshot.getValue();


                    locationForStatus.setLongitude(Double.parseDouble(value));
                }


            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });





        float distance = location.distanceTo(locationForStatus);
        Log.d(TAG, String.format("Distance from status %s is %sm", statusIndex, distance));
        return distance < mFirebaseRemoteConfig.getLong("LOCATION_MIN_DISTANCE_CHANGED");
    }



    private void shutdownAndScheduleStartup(int when) {
        Log.i(TAG, "overnight shutdown, seconds to startup: " + when);
        com.google.android.gms.gcm.Task task = new OneoffTask.Builder()
                .setService(TrackerTaskService.class)
                .setExecutionWindow(when, when + 60)
                .setUpdateCurrent(true)
                .setTag(TrackerTaskService.TAG)
                .setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .build();
        GcmNetworkManager.getInstance(this).schedule(task);
        stopSelf();
    }

    /**
     * Pushes a new status to Firebase when location changes.
     */
    @Override
    public void onLocationChanged(Location location) {

        fetchRemoteConfig();

        long hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int startupSeconds = (int) (mFirebaseRemoteConfig.getDouble("SLEEP_HOURS_DURATION") * 3600);
        if (hour == mFirebaseRemoteConfig.getLong("SLEEP_HOUR_OF_DAY")) {
            shutdownAndScheduleStartup(startupSeconds);
            return;
        }



        if (locationIsAtStatus(location, 1) && locationIsAtStatus(location, 0)) {
            // If the most recent two statuses are approximately at the same
            // location as the new current location, rather than adding the new
            // location, we update the latest status with the current. Two statuses
            // are kept when the locations are the same, the earlier representing
            // the time the location was arrived at, and the latest representing the
            // current time.
            Log.e("1","111");

        } else {
            // Maintain a fixed number of previous statuses.

            // We push the entire list at once since each key/index changes, to
            // minimize network requests.
            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());
                Firebase childref = ref.child("user mail");
                childref.setValue(user.getMetaEmail());
                childref = ref.child("user name");
                childref.setValue(user.getMetaName());
                childref = ref.child("latitude");
                childref.setValue(String.valueOf(latitude));
                childref = ref.child("langitude");
                childref.setValue(String.valueOf(longitude));
            }        }

        if (BuildConfig.DEBUG) {
        }

        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        boolean connected = info != null && info.isConnectedOrConnecting();
        setStatusMessage(connected ? "Connected" : "Not Connected");
    }

    private void buildNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, FirstMainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.revsmartlogo)
                .setColor(0xFF5733)
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
                .setContentIntent(resultPendingIntent);
        startForeground(FOREGROUND_SERVICE_ID, mNotificationBuilder.build());
    }

    /**
     * Sets the current status message (connecting/tracking/not tracking).
     */
    private void setStatusMessage(String stringId) {

        mNotificationBuilder.setContentText(("Connected"));
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());

        // Also display the status message in the activity.
        Intent intent = new Intent(STATUS_INTENT);
        intent.putExtra(getString(R.string.status), stringId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



}

