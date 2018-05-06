package com.lagbe.bua.aflan.bualagbe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aflan on 5/2/2018.
 */

public class ServicesActivity extends AbsRuntimeAcitivty {
    private static final String TAG = "Debug";
    AnimationDrawable animationDrawable;
    CoordinatorLayout coordinatorLayout;
    LocationManager locationManager;
    View decorView;
    public double latitude;
    public double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        requestAppPermissions(new String[]{
                        Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, R.string.name
                , 1);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
             decorView = getWindow().getDecorView();
            // Hide the status bar.
            //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            // decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            //  ActionBar actionBar = getActionBar();
//            actionBar.hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_service);
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getLocation();


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        animationDrawable = (AnimationDrawable) coordinatorLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //Do anything when permisson granted
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_find_bua:
                    //TODO: NEW ACTIVITY THAT GETS THE USERS LOCATION AND AUTO FILLS UP THE FORM AND SAVE IT AS PRESET
                    Snackbar home = Snackbar
                            .make(coordinatorLayout, "Welcome to Home", Snackbar.LENGTH_LONG);

                    home.show();
                    return true;
                case R.id.navigation_preset:
                    //TODO: NEW ACTIVITY THAT SHOWS THE ALREADY SAVED PRESET
                    //mTextMessage.setText(R.string.title_dashboard);
                    Snackbar preset = Snackbar
                            .make(coordinatorLayout, "Welcome to Preset", Snackbar.LENGTH_LONG);

                    preset.show();
                    return true;
                case R.id.navigation_about:
                    //TODO: NEW ACTIVITY THAT HAS ALL THE DETAILS ABOUT APP AND THE DEVELOPER(S)
                    //mTextMessage.setText(R.string.title_notifications);
                    Snackbar about = Snackbar
                            .make(coordinatorLayout, "Welcome to About", Snackbar.LENGTH_LONG);

                    about.show();
                    return true;
            }
            return false;
        }
    };

    public boolean canGetLocation() {
        boolean result;
        locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (locationManager == null)

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            network_enabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled == false || network_enabled == false) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.error_title);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.location_needed);

        // On pressing Settings button
        alertDialog.setPositiveButton(
                getResources().getString(R.string.button_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.show();
    }

    private void getLocation() {
        if (canGetLocation()) {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
             decorView = getWindow().getDecorView();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestAppPermissions(new String[]{
                                Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, R.string.name
                        , 1);
                return;
            }

            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String lat = String.valueOf(latitude);
                String lon = String.valueOf(longitude);

                String cityName = null;
                Geocoder gcd = new Geocoder(getBaseContext(),
                        Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    Snackbar about = Snackbar
                            .make(decorView, add, Snackbar.LENGTH_LONG);

                    about.show();
                   /* add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    // SUBLOCALITY FOR A LOCATION
                    add = add + "\n" + obj.getSubThoroughfare();*/

                    if (addresses.size() > 0)
                        System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getSubLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String s = "Lat "+longitude +" "+ "Lon "+latitude
                        + "\n" + "YOU ARE IN: " + cityName;
                Snackbar about = Snackbar
                        .make(decorView, s, Snackbar.LENGTH_LONG);

                about.show();


            } else {
                //This is what you need:
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, LocationListener);

            }

        } else {
            showSettingsAlert();
        }
    }

    private LocationListener LocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);
    /*----------to get City-Name from coordinates ------------- */

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
