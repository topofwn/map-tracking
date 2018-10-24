package com.luan.maptracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;


/**
 * Created by tuan.giao on 2/2/2018.
 */

public class ActivityUtils {
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void startActivity(Activity activity, Class<?> cls, boolean isClearTask, boolean isAnim) {
        Intent intent = new Intent(activity, cls);
        if (isClearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
        if (isAnim) {
            activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }


    public static void startActivity(Activity activity, Class<?> cls, boolean isClearTask) {
        Intent intent = new Intent(activity, cls);
        if (isClearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public static void startActivityWithData(Activity activity, Class<?> cls, boolean isClearTask, Bundle extra) {
        Intent intent = new Intent(activity, cls);
        if (isClearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtras(extra);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, permission) == 0;
    }

    public static boolean isPermissionListGranted(Context context, String[] permissionList) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean isGranted = false;
            for (int i = 0; i < permissionList.length; ++i) {
                isGranted = isPermissionGranted(context, permissionList[i]);
                if (!isGranted) {
                    return false;
                }
            }
            return isGranted;
        } else {
            return true;
        }
    }

    public static void addPermission(Activity activity, String permissionName, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted(activity, permissionName)) {
            ActivityCompat.requestPermissions(activity, new String[]{permissionName}, requestCode);
        }
    }



    public static void addPermissionList(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList addPermissions = new ArrayList();
            for (int finalPermissions = 0; finalPermissions < permissions.length; ++finalPermissions) {
                if (!isPermissionGranted(activity, permissions[finalPermissions])) {
                    addPermissions.add(permissions[finalPermissions]);
                }
            }
            if (!addPermissions.isEmpty()) {
                String[] var6 = new String[addPermissions.size()];
                for (int i = 0; i < addPermissions.size(); ++i) {
                    var6[i] = (String) addPermissions.get(i);
                }
                ActivityCompat.requestPermissions(activity, var6, requestCode);
            }
        }
    }
}
