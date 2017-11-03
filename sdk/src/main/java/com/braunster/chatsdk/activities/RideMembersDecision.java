package com.braunster.chatsdk.activities;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.security.PrivateKey;
import java.security.acl.Group;

/**
 * Created by DELL on 10/5/2017.
 */

public class RideMembersDecision {
    private String UserEntityId;
    private String GroupEntityId;
    private String GroupName;
    private String NoRide="0";
    private String SendInvitation="1";
    private String AcceptInvitation="2";
    private String RideAdmin="4";

    RideMembersDecision(String GroupEntityId,String UserEntityId,String GroupName)
    {
        this.GroupEntityId=GroupEntityId;
        this.UserEntityId=UserEntityId;
        this.GroupName=GroupName;
        Log.e("gid,uid,gn",GroupEntityId+ " "+ UserEntityId+" "+ GroupName);
    }
     void setRideAssosiation() {



        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/threads/" + GroupEntityId + "/users");

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.e("Ride Group User id",dataSnapshot.getKey());
                Boolean decision=setRideAssosiationValue(dataSnapshot.getKey());
                if(decision==true) {
                    if (UserEntityId.equalsIgnoreCase(dataSnapshot.getKey())) {
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + dataSnapshot.getKey());
                        Firebase childref = ref.child("ride_status");
                        childref.setValue(RideAdmin);
                        childref = ref.child("ride_group ");
                        childref.setValue(GroupName);

                    } else {
                        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + dataSnapshot.getKey());
                        Firebase childref = ref.child("ride_status");
                        childref.setValue(SendInvitation);
                        childref = ref.child("ride_group ");
                        childref.setValue(GroupName);
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

    private boolean setRideAssosiationValue(String key) {
        Firebase ref = new Firebase("https://revsmarttech.firebaseio.com/testRoot/threads/" + GroupEntityId + "/users/"+key+"/Ride Status");
        final int[] t = {0};

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.e("Ride Group Users name",(String) dataSnapshot.getValue());
                if(((String) dataSnapshot.getValue()).equalsIgnoreCase("0"))
                {
                  t[0] =1;
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


            if(t[0]==1)
            {
                return true;
            }
            else
            {
                return false;
            }




    }

}
