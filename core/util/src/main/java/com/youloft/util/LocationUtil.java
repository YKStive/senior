package com.youloft.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

/**
 * @author xll
 * @date 2018/7/26 11:29
 */
public class LocationUtil {

    /**
     * @return
     */
    public static String getLat(Context context) {
        Location location = getLastKnowLocation(context);
        if (location != null) {
            return String.valueOf(location.getLatitude());
        }
        return "0";
    }

    /**
     * @return
     */
    public static String getLot(Context context) {
        Location location = getLastKnowLocation(context);
        if (location != null) {
            return String.valueOf(location.getLongitude());
        }
        return "0";
    }


    /**
     * 获取最后的位置
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static Location getLastKnowLocation(Context context) {
        if (ActivityCompat
                .checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            LocationManager locationManager = getLocationManager(context);
            if (locationManager == null) {
                return null;
            }
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } catch (Throwable e) {
            // try-catch  only fix gonee error
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取location manager
     *
     * @param context
     * @return
     */
    public static LocationManager getLocationManager(Context context) {
        if (ActivityCompat
                .checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
