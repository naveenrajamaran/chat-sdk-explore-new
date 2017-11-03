package com.braunster.chatsdk.activities;


import android.util.Log;
import android.view.View;


import com.braunster.chatsdk.dao.BThread;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.network.BNetworkManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by DELL on 7/17/2017.
 */

public class LocationSharing {
    int i = 0;
     int t=0;
    static  int t1=0;
    String str;
    public static  int m=0,n=0;
    public static int count=0;
    public static String value;
    public static ArrayList<String> usersKey = new ArrayList<String>();
   public static ArrayList<String> userDetails = new ArrayList<String>();
    public static ArrayList<String> adding = new ArrayList<String>();
     public static ArrayList<String> groupNames = new ArrayList<String>();
    public static String userValues[]=new String[10] ;


    LocationSharing() {

        if(FirstMainActivity.currentRide.getVisibility()== View.VISIBLE) {
            Log.e("123456790","Group location");

            t1=0;
            getGroupLocation();//Get all group members location for ride traking.
            display();//display and check the group details.

        }

    else

    {
        Log.e("123456790","Single location");
       // countUsers();

        t1=0;
        currentUserLocation();//Get current user location.
        //display();

    }


    }
//if needs display all locations and assign all values to variable for show location in map.
    private void display() {
    int y=0;int t1;

        for(int k=0;k<usersKey.size();k++) {
                adding.add(usersKey.get(k));
           Log.e("value key",adding.get(k));
            for ( t1 = y; t1 < userDetails.size(); t1++) {
                adding.add(userDetails.get(t1));
                Log.e("value Details",adding.get(t1));

                if((t1+1)%4==0)
                {
                    y=t1+1;
                    break;
                }

            }

        }



//       userValues = new String[adding.size()];
//        userValues = adding.toArray(userValues);
//
//
//        for (int t2 = 0; t2 < adding.size(); t2++) {
//            Log.e("" + (t2 + 1), adding.get(t2) + " ");

      //  }
    }


/*get all group users entity id*/

    private void getGroupLocation() {


         str=FirstMainActivity.getCurrentRideEntity();
          /*Get all users entity value from corresponding group.*/
        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/threads/"+str+"/users");

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


           //     Log.e("gggggggrrrrooadded", dataSnapshot.getKey()+ " ");
                currentUserLocation(dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });



    }


/*get all lat and lan value from each user in group*/
     static void setGroupStatus(final Boolean RideStatus) {


        final String str=FirstMainActivity.getCurrentRideEntity();
        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/threads/"+str+"/users");

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                setRideStatus(dataSnapshot.getKey(),FirstMainActivity.getCurrentRideGroupName(),RideStatus);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });



    }

    /*set the status and send invitation status for all group members when ride admin starts the ride*/
    private static void setRideStatus(final String key, final String groupName, Boolean RideStatus) {

        if(RideStatus==true) {
            final BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();


            Firebase ref1 = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" +user.getEntityID() );

            ref1.addChildEventListener(new ChildEventListener() {



                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                    if(dataSnapshot.getKey().equalsIgnoreCase("ride_status")) {
                        String values = (String) dataSnapshot.getValue();

                        if(values.equalsIgnoreCase("0")||values.equalsIgnoreCase("null"))
                        {
                            if (!user.getEntityID().equalsIgnoreCase(key)) {
                                Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + key);
                                Firebase childref = ref.child("ride_status");
                                childref.setValue("1");
                                childref = ref.child("ride_group");
                                childref.setValue(groupName);

                            } else {

                                Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + key);
                                Firebase childref = ref.child("ride_status");
                                childref.setValue("3");
                                childref = ref.child("ride_group");
                                childref.setValue(groupName);


                            }
                        }

                    }
                }



                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }


            });







        }
        else
        {
            Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + key);
            Firebase childref = ref.child("ride_status");
            childref.setValue("0");
            childref = ref.child("ride_group");
            childref.setValue("null");
        }

    }

/*get current user location to view in the map*/
    public void currentUserLocation()
    {
        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();

        //Get co-ordinates from each group members.
        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());



        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.getKey().equalsIgnoreCase("langitude")) {
                    String values = (String) dataSnapshot.getValue();
                    userValues[t1]=values;
                    Log.e(""+t1,userValues[t1]);

                    t1=t1+1;
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
         ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());



        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                if(dataSnapshot.getKey().equalsIgnoreCase("latitude")) {
                    String values = (String) dataSnapshot.getValue();
                    userValues[t1]=values;
                    Log.e(""+t1,userValues[t1]);

                    t1=t1+1;
                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
         ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" + user.getEntityID());



        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                if(dataSnapshot.getKey().equalsIgnoreCase("user name")) {
                    String values = (String) dataSnapshot.getValue();
                    userValues[t1]=values;
                    Log.e(""+t1,userValues[t1]);


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

  /*get current user location to view in the map when ride starts*/


    public void currentUserLocation(final String key)
    {



        final BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();


        Firebase ref1 = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" +key );

        ref1.addChildEventListener(new ChildEventListener() {



            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                if(dataSnapshot.getKey().equalsIgnoreCase("ride_status")) {
                    String values = (String) dataSnapshot.getValue();
                    if(values.equalsIgnoreCase("2")||values.equalsIgnoreCase("3"))
                    {
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/sharedlocation/" +key);

                        usersKey.add(key);

                        t=t+1;
                        ref.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                String value = dataSnapshot.getValue(String.class);

                                userDetails.add(value);


                                t=t+1;


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }

                        });

                    }


                }
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });










    }

    //Displaying all the datas and add group name in spinner.
    public static void displaying()
    {
            if(count==0) {
                for (int t2 = groupNames.size() / 2; t2 < groupNames.size(); t2++) {
                    Log.e("" + (t2), groupNames.get(t2) + " ");
                    FirstMainActivity.texts.add(groupNames.get(t2));
                }
                for (int t2 = 0; t2 < groupNames.size(); t2++) {
                //    Log.e("" + (t2), groupNames.get(t2) + " ");

                }

            }
        //FirstMainActivity.getAllValues();
    }
/*get ride status value from firebase */

    public static void getRideStatus(BUser user) {




        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" +user.getEntityID() );

        ref.addChildEventListener(new ChildEventListener() {



            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                if(dataSnapshot.getKey().equalsIgnoreCase("ride_status")) {
                    String value = (String) dataSnapshot.getValue();

                     setvalue(value);

                }
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


    }
    //set and get status value
    public static void setvalue(String values) {
        value=values;
    }

        public static String getValue()
        {
            return value;
        }







}