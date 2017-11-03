package com.braunster.chatsdk.activities;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.braunster.chatsdk.R;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.network.BNetworkManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TwoFragment extends Fragment  {

    MapView mMapView;
    private GoogleMap googleMap;
    public int i=0;
    LatLng sydney;
    CameraPosition cameraPosition;
    View locationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_two, container, false);


        mMapView = (MapView) rootView.findViewById(R.id.ridemap);
        mMapView.onCreate(savedInstanceState);
        new LocationSharing();

        mMapView.onResume(); // needed to get the map to display immediately

        try {
        MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
        e.printStackTrace();
    }
       
        mMapView.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap mMap) {
            googleMap = mMap;
            GPSTracker gpsTracker = new GPSTracker(getActivity());
            Drawable circleDrawableUser = getResources().getDrawable(R.drawable.users);
            BitmapDescriptor markerIconUser = getMarkerIconFromDrawable(circleDrawableUser);
            Drawable circleDrawableFriend = getResources().getDrawable(R.drawable.friend);
            BitmapDescriptor markerIconUserFriend = getMarkerIconFromDrawable(circleDrawableFriend);

            // For dropping a marker at a point on the Map
            BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();

//
//            for(int t=0;t<LocationSharing.userValues.length;t++)
//            {
//                Log.e("" + (t + 1), LocationSharing.userValues[t] + " ");
//
//            }
            
            if( LocationSharing.t1>0 )
            {
                sydney = new LatLng(Double.parseDouble(LocationSharing.userValues[1]), Double.parseDouble(LocationSharing.userValues[0]));
                googleMap.addMarker(new MarkerOptions().position(sydney)
                        .title("My Location").icon(markerIconUser));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                // For zooming automatically to the location of the marker
                googleMap.setTrafficEnabled(true);
                // Enable / Disable zooming controls
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);
                locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

                googleMap.getUiSettings().setCompassEnabled(true);

                // Enable / Disable Rotate gesture
                googleMap.getUiSettings().setRotateGesturesEnabled(true);

                // Enable / Disable zooming functionality
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                //   cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(LocationSharing.userValues[1]), Double.parseDouble(LocationSharing.userValues[0])), 12.0f));
                LocationSharing.t1=0;

            }
            else
            {
            int y = 0;
            for (int k = 0; k < LocationSharing.usersKey.size(); k++) {
                int t = 0;
                //    Log.e("" + (k + 1), LocationSharing.usersKey.get(k) + " ");

                for (int t1 = y; t1 < LocationSharing.userDetails.size(); t1++) {
                    if (user.getEntityID().equalsIgnoreCase(LocationSharing.usersKey.get(k))) {
                        if (t == 0) {
                            t = t + 1;
                            sydney = new LatLng(Double.parseDouble(LocationSharing.userDetails.get(t1 + 1)), Double.parseDouble(LocationSharing.userDetails.get(t1)));
                            googleMap.addMarker(new MarkerOptions().position(sydney)
                                    .title("My Location").icon(markerIconUser));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                            // For zooming automatically to the location of the marker
                            googleMap.setTrafficEnabled(true);
                            // Enable / Disable zooming controls
                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            googleMap.setMyLocationEnabled(true);
                            locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

                            googleMap.getUiSettings().setCompassEnabled(true);

                            // Enable / Disable Rotate gesture
                            googleMap.getUiSettings().setRotateGesturesEnabled(true);

                            // Enable / Disable zooming functionality
                            googleMap.getUiSettings().setZoomGesturesEnabled(true);
                         //   cameraPosition = new CameraPosition.Builder().target(sydney).zoom(20).build();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( Double.parseDouble(LocationSharing.userDetails.get(t1 + 1)), Double.parseDouble(LocationSharing.userDetails.get(t1))), 12.0f));


                        }

                    } else {
                        if (t == 0) {
                            t = t + 1;

                            sydney = new LatLng(Double.parseDouble(LocationSharing.userDetails.get(t1 + 1)), Double.parseDouble(LocationSharing.userDetails.get(t1)));
                            googleMap.addMarker(new MarkerOptions().position(sydney)
                                    .title(LocationSharing.userDetails.get(t1 + 3)).icon(markerIconUserFriend));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                            // For zooming automatically to the location of the marker
                            googleMap.setTrafficEnabled(true);
                            // Enable / Disable zooming controls
                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            googleMap.setMyLocationEnabled(true);
                            locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

                            googleMap.getUiSettings().setCompassEnabled(true);

                            // Enable / Disable Rotate gesture
                            googleMap.getUiSettings().setRotateGesturesEnabled(true);

                            // Enable / Disable zooming functionality
                            googleMap.getUiSettings().setZoomGesturesEnabled(true);
                         //   cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( Double.parseDouble(LocationSharing.userDetails.get(t1 + 1)), Double.parseDouble(LocationSharing.userDetails.get(t1))), 12.0f));


                        }


                    }
                    if ((t1 + 1) % 4 == 0) {
                        y = t1 + 1;
                        break;
                    }

                }

            }
        }







           LocationSharing. usersKey.clear();
            LocationSharing.userDetails.clear();
            LocationSharing.adding.clear();

        }
    });

        return rootView;
}

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}