package com.youloft.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * 各种系统支持情况
 *
 * @author javen
 * @date 14-11-4
 */
public final class SupportUtils {


    /**
     * 是否支持CameraFlash
     *
     * @return
     */
    public static boolean supportCameraFlash(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * 是否支持Focus
     *
     * @return
     */
    public static boolean supportCameraFocus(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    /**
     * 是否支持摇晃
     *
     * @return
     */
    public static boolean supportShake(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }

    /**
     * 是否支持指南针
     *
     * @return
     */
    public static boolean supportCompass(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
    }

}
