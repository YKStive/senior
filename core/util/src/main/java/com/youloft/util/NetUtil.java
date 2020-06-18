package com.youloft.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

/**
 * 网络工具类
 *
 * @author xll
 * @date 2018/7/26 9:41
 */
public class NetUtil {

    /**
     * 转化全局context，避免泄漏
     *
     * @param appContext
     * @return
     */
    private static Context getAppContext(Context appContext) {
        if (appContext.getApplicationContext() != null) {
            return appContext.getApplicationContext();
        }
        return appContext;
    }

    /**
     * 网络是否OK
     *
     * @param appContext 上线文
     * @return true : 网络可用  false：网络不可用
     */
    public static boolean isNetOK(Context appContext) {
        ConnectivityManager connManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return false;
        }
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 获取Mac地址
     *
     * @param appContext
     * @return
     */
    public static String getLocalMacAddress(Context appContext) {
        if (appContext.getApplicationContext() != null) {
            appContext = appContext.getApplicationContext();
        }
        WifiManager wifi = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return "00000000";
        }
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null || TextUtils.isEmpty(info.getMacAddress())) {
            return "00000000";
        }
        if ("::::".equals(info.getMacAddress())) {
            return "00000000";
        }
        return info.getMacAddress();
    }

    /**
     * 网络环境是否是WI-FI
     *
     * @return
     */
    public static boolean isWiFi(Context context) {
        return getNetWorkType(context) == 0;
    }

    /**
     * 网络环境是否不是wifi且网络可用
     *
     * @return
     */
    public static boolean isNetOkAndNoWiFi(Context context) {
        int type = getNetWorkType(context);
        return type != 0 && type != 99;
    }

    /**
     * 获取网络速度类型
     *
     * @return 0-Wifi
     * 1-2G
     * 2-3G
     * 3-4G
     * 99-unknow
     */
    public static int getNetWorkType(Context context) {
        int strNetworkType = 99;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return strNetworkType;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = 0;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        //api<8 : replace by 11
                        strNetworkType = 1;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        //api<13 : replace by 15
                        strNetworkType = 2;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        //api<11 : replace by 13
                        strNetworkType = 3;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = 2;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 判断是否是3G+的移动网络
     *
     * @param context ...
     * @return ...
     */
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                // ~ 50-100 kbps
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                // ~ 14-64 kbps
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                // ~ 50-100 kbps
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                // ~ 400-1000 kbps
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                // ~ 600-1400 kbps
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                // ~ 100 kbps
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                // ~ 2-14 Mbps
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                // ~ 700-1700 kbps
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                // ~ 1-23 Mbps
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                // ~ 400-7000 kbps
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                // ~ 1-2 Mbps
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                // ~ 5 Mbps
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                // ~ 10-20 Mbps
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                // ~25 kbps
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                // ~ 10+ Mbps
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * 获取网络类型
     *
     * @param appContext
     * @return
     */
    public static String getNetType(Context appContext) {
        if (appContext.getApplicationContext() != null) {
            appContext = appContext.getApplicationContext();
        }
        ConnectivityManager connManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null) {
            return "None";
        }
        return info.getTypeName();
    }

    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)) {
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    /**
     * 获取mac
     *
     * @param context
     * @return
     */
    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
