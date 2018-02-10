package com.example.alexander.edadarom;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alexander on 16.01.2018.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    double latitude, longitude;
    LatLng userLatLng;
    public static String LOCALITY = "locality";
    public static String LOCATION_LAT = "location_lat";
    public static String LOCATION_LON = "location_lon";
    public static String COMMENT = "cv_comment";
    public String locality, country, comment;

    EditText find_location, text_comment;
    CardView btn_pinMarker, cv_comment;
    TextView comment_complete;

    final static String TAG = "myLogs_MapsActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public float markerLocationLat, markerLocationLon;
    private Marker marker;
    private double userLat, userLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        if(!CheckGooglePlayServices()) {
            finish();
        } else {

        }
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        find_location = findViewById(R.id.TF_location);
        btn_pinMarker = findViewById(R.id.btn_pinmarker);
        cv_comment = findViewById(R.id.comment);
        comment_complete = findViewById(R.id.comment_complete);
        text_comment = findViewById(R.id.text_comment);
        find_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchObject();
                    Log.d(TAG,"onEditorAction");
                    return true;
                }
                return false;
            }
        });
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Нет доступа к местоположению", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        userLatLng = latLng;
        userLat = latLng.latitude;
        userLng = latLng.longitude;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(13)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position = marker.getPosition();
        marker.setSnippet(position.toString());
        Log.d(TAG, String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_shadow_on));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position = marker.getPosition();
        marker.setSnippet(position.toString());
        Log.d(TAG, String.format("Dragging to %f:%f", position.latitude,
                position.longitude));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position=marker.getPosition();
        marker.setSnippet(position.toString());
        Log.d(TAG, String.format("Dragged to %f:%f",
                position.latitude,
                position.longitude));
        getGeocoder(position);

        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_on));
    }

    private void getGeocoder(LatLng position) {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                locality = addressList.get(0).getAddressLine(0);
                if (!locality.isEmpty())
                    find_location.setText(locality);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        onMapClick();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void searchObject() {
        String location = find_location.getText().toString();
        Log.d(TAG,"searchObject enter");
        List<Address> addressList = null;
        MarkerOptions markerOptions = new MarkerOptions();
        if (!location.equals("")) {
            Log.d(TAG,"searchObject location is not empty");
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList != null) {
                Log.d(TAG,"searchObject addressList is not empty");
                for (int i = 0; i < addressList.size(); i++) {
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.imageView4: {
                finish();
            }
            break;
            case R.id.btn_pinmarker: {
                animateView(cv_comment);
            }
            break;
            case R.id.comment_complete:
                if(text_comment.getText().toString() == null){
                    comment="";
                } else {
                    comment = text_comment.getText().toString();
                }
                executeDataSending();
                break;
            case R.id.imageView6: {
                find_location.setText("");
            }
            break;
            case R.id.show_my_location: {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                Toast.makeText(this, "Текущее местоположение", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void animateView(View view) {
        cv_comment.animate().translationY(cv_comment.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        cv_comment.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void onMapClick() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null){
                    marker.setPosition(latLng);
                } else {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.draggable(true);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_on));
                    marker = mMap.addMarker(markerOptions);
                }
                markerLocationLat = (float) latLng.latitude;
                markerLocationLon = (float) latLng.longitude;
                getGeocoder(latLng);
            }
        });
    }

    private void executeDataSending() {
        // Fake data sending effect
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    if (locality != null) {
                        intent.putExtra(COMMENT, comment);
                        intent.putExtra(LOCALITY, locality);
                        intent.putExtra(LOCATION_LAT, markerLocationLat);
                        intent.putExtra(LOCATION_LON, markerLocationLon);
                    } else if (locality == null) {
                        ifLocalityEmpty();
                        intent.putExtra(COMMENT, comment);
                        intent.putExtra(LOCALITY, locality);
                        intent.putExtra(LOCATION_LAT, userLat);
                        intent.putExtra(LOCATION_LON, userLng);
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void ifLocalityEmpty() {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocation(userLat, userLng, 1);
            if (addressList != null && addressList.size() > 0) {
                locality = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}