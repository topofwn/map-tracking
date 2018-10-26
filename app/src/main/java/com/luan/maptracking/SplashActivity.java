package com.luan.maptracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

public class SplashActivity extends BaseActivity implements android.location.LocationListener {
    private static final int REQUEST_ACCESS_LOCATION_CODE = 11;
    public static final String LOCATION_KEY_LATN = "LOCATION_KEY_LATN";
    public static final String LOCATION_KEY_LONGT = "LOCATION_KEY_LONGT";
    private ProgressBar progress;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters
    private Location mLocation;
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 seconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();

    }

    @Override
    protected void initView() {
        progress = findViewById(R.id.prbar);
        progress.setProgress(50);
    }

    @Override
    protected void initData() {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            getLastUnknownLocation();

        } else {
            requestPermissionsSafely(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION_CODE);
        }
    }


    @SuppressLint("MissingPermission")
    private void getLastUnknownLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                gotoMapScreen(location);
                //mPresenter.getWeatherData(location.getLatitude(), location.getLongitude());
//                LatLng mLatLng = new LatLng(location.getLatitude(),location.getLongitude());
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));

            } else {
                //showMessage(getResources().getString(R.string.no_location), MessageType.ERROR, AlertType.TOAST);
            }
        } else if (isGPSEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                gotoMapScreen(location);
//                LatLng mLatLng = new LatLng(location.getLatitude(),location.getLongitude());
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
                //mPresenter.getWeatherData(location.getLatitude(), location.getLongitude());
            } else {

                //showMessage(getResources().getString(R.string.no_location), MessageType.ERROR, AlertType.TOAST);
            }
        }
    }

    private void gotoMapScreen(Location loc) {
        progress.setProgress(90);
        Bundle bd = new Bundle();
        bd.putDouble(LOCATION_KEY_LATN, loc.getLatitude());
        bd.putDouble(LOCATION_KEY_LONGT, loc.getLongitude());
        ActivityUtils.startActivityWithData(SplashActivity.this, MapsActivity.class, true, bd);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_LOCATION_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLastUnknownLocation();

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
