package com.luan.maptracking;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

public class SplashActivity extends BaseActivity {
    private static final int REQUEST_ACCESS_LOCATION_CODE = 11;
    private ProgressBar progress;

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
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)&& hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            gotoMapScreen();
        }else{
            requestPermissionsSafely(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION_CODE);
        }
    }

    private void gotoMapScreen() {
        progress.setProgress(90);
        ActivityUtils.startActivity(SplashActivity.this,MapsActivity.class,true,true);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_LOCATION_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                   gotoMapScreen();
                }
            }
        }
    }
}
