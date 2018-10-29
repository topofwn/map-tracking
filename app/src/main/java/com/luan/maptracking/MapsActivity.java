package com.luan.maptracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import static com.luan.maptracking.SplashActivity.LOCATION_KEY_LATN;
import static com.luan.maptracking.SplashActivity.LOCATION_KEY_LONGT;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener, GoogleMap.OnMapClickListener {
    private static final int REQUEST_ACCESS_LOCATION_CODE = 16;
    private boolean checkBtn = false;
    private GoogleMap mMap;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters
    private MLocation mLocation;
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 seconds
    private Button starButton;
    private ImageButton search;
    private List<MLocation> mLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bd = this.getIntent().getExtras();
        if (bd != null) {
            mLocation = new MLocation(bd.getDouble(LOCATION_KEY_LATN), bd.getDouble(LOCATION_KEY_LONGT));
        } else {
            mLocation = new MLocation(10.7505117, 106.647176117);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            getLastUnknownLocation();
        } else {
            requestPermissionsSafely(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION_CODE);
        }
        starButton = findViewById(R.id.btnTracking);
        starButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (!checkBtn) {
                    starButton.setText("STOP");
                    beginTracking();
                    mLine = new ArrayList<>();
                    starButton.setBackgroundResource(R.drawable.bg_button_color_pressed);
                } else if (checkBtn) {
                    starButton.setText("START");

                    stopTracking();
                    starButton.setBackgroundResource(R.drawable.bg_button_color_unpressed);
                }
                LatLng na = new LatLng(mLocation.getLat(), mLocation.getLongt());
                addMarker(na);
                checkBtn = !checkBtn;
            }
        });
        search = findViewById(R.id.btnFind);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputLocationDialog dialog = new InputLocationDialog(MapsActivity.this,(MLocation loc)->{
                   LatLng lt = new LatLng(loc.getLat(),loc.getLongt());
                   addMarker(lt);
                });
                dialog.show();
            }
        });
        mapFragment.getMapAsync(this);

    }

    private void gotoThisPosition(MLocation loc) {
    }

    private void stopTracking() {
        mMap.clear();
    }

    private void beginTracking() {
        mMap.clear();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        // Add a marker in Sydney and move the camera

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissionsSafely(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION_CODE);

        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);

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
                Toast.makeText(this, "Location found", Toast.LENGTH_SHORT).show();
                //mPresenter.getWeatherData(location.getLatitude(), location.getLongitude());
//                LatLng mLatLng = new LatLng(location.getLatitude(),location.getLongitude());
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15.0f));

            } else {
                Toast.makeText(this, "No location found", Toast.LENGTH_SHORT).show();
                //showMessage(getResources().getString(R.string.no_location), MessageType.ERROR, AlertType.TOAST);
            }
        }

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Toast.makeText(this, "Location found", Toast.LENGTH_SHORT).show();
//              LatLng mLatLng = new LatLng(location.getLatitude(),location.getLongitude());
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15.0f));
                //mPresenter.getWeatherData(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "No location found", Toast.LENGTH_SHORT).show();
                //showMessage(getResources().getString(R.string.no_location), MessageType.ERROR, AlertType.TOAST);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_LOCATION_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // user rejected the permission
                    getLastUnknownLocation();
                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLocation = new MLocation(location.getLatitude(), location.getLongitude());
        if (checkBtn) {
            if (mLine.size() == 0) {
                MLocation location1 = new MLocation(location.getLatitude(), location.getLongitude());
                mLine.add(location1);
            } else {
                int ss = mLine.size() - 1;
                MLocation origin = mLine.get(ss);
                MLocation dest = new MLocation(location.getLatitude(), location.getLongitude());
                mLine.add(dest);

                LatLng mLatLng = new LatLng(dest.getLat(), dest.getLongt());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
                PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                options.add(new LatLng(origin.getLat(), origin.getLongt()), new LatLng(dest.getLat(), dest.getLongt()));
                mMap.addPolyline(options);
            }
        }
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

    public void addMarker(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        mMap.addMarker(options);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    @Override
    public void onMapClick(LatLng latLng) {
  //
            mMap.clear();
        addMarker(latLng);

    }
}
