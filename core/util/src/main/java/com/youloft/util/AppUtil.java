package com.youloft.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * app信息工具类
 *
 * @author xll
 * @date 2018/7/25 18:03
 */
public class AppUtil {
    private static String VERSION_NAME = "-1";
    private static int VERSION_CODE = -1;

    /**
     * 更新获取不到数据时返回的versionName 和 versionCode
     *
     * @param vname
     * @param vcode
     */
    public static void updateMacro(String vname, int vcode) {
        VERSION_NAME = vname;
        VERSION_CODE = vcode;
    }

    /**
     * 是否有Intent指定的Activity
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean hasActivity(Context context, Intent intent) {
        List<ResolveInfo> s = context.getPackageManager().queryIntentActivities(intent, 0);
        return !s.isEmpty();
    }

    /**
     * 是否包含Activity
     *
     * @param context context
     * @param actClz  查询的activity
     * @return
     */
    public static boolean hasActivity(Context context, Class<? extends Activity> actClz) {
        if (context == null || actClz == null)
            return false;
        ComponentName cname = new ComponentName(context, actClz);
        try {
            ActivityInfo info = context.getPackageManager().getActivityInfo(cname, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    /**
     * 返回应用版本
     *
     * @param context
     * @return
     */
    public static int getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用Model
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取操作系统版本
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前语言
     *
     * @param context
     * @return
     */
    public static Locale getDisplayLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /**
     * 获取缓存目录
     *
     * @param appContext
     * @return
     */
    public static File getCacheDir(Context appContext, String type) {
        File baseDir = null;
        if (hasSDCard()) {
            baseDir = new File(Environment.getExternalStorageDirectory(), "/data/Android/" + appContext.getPackageName());
        } else {
            baseDir = appContext.getCacheDir();
        }

        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return new File(baseDir, type);
    }

    /**
     * 打开系统浏览器
     *
     * @param context
     * @param url
     */
    public static void showWebBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (hasActivity(context, intent)) {
            context.startActivity(intent);
        }
    }

    /**
     * 是否有SDCard
     *
     * @return
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取meta数据
     *
     * @param appContext
     * @return
     */
    public static Bundle getMetaData(Context appContext) {
        try {
            ApplicationInfo pinfo = appContext.getPackageManager().getApplicationInfo(
                    appContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            return pinfo.metaData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Bundle();
    }

    /**
     * 包是否存在
     *
     * @param c           context
     * @param packageName 查询包
     * @return
     */
    public static boolean isHasPackage(Context c, String packageName) {
        if (null == c || null == packageName) {
            return false;
        }

        boolean bHas = true;
        try {
            c.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
        } catch (Exception e) {
            bHas = false;
        }
        return bHas;
    }

    /**
     * 启动app
     *
     * @param context context
     * @param pkg     启动包名
     */
    public static void startApp(Context context, String pkg) {
        Intent intent = getLaunchIntentForPackage(context, pkg);
        intent = getLauncherComponment(context, intent);
        try {
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    /**
     * 获取启动应用的intent
     *
     * @param context context
     * @param pkgname 包名
     * @return
     */
    public static Intent getLaunchIntentForPackage(Context context, String pkgname) {
        PackageManager pm = context.getPackageManager();
        Intent intent = null;
        try {
            intent = pm.getLaunchIntentForPackage(pkgname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }

    /**
     * 能启动的
     *
     * @param context context
     * @param intent  启动的intent
     * @return
     */
    public static Intent getLauncherComponment(Context context, Intent intent) {
        PackageManager localPackageManager = context.getPackageManager();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        ResolveInfo resolveInfo = localPackageManager.resolveActivity(intent, 0);
        String packageName = null;
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            packageName = resolveInfo.activityInfo.packageName;
        }
        if (packageName != null) {
            return localPackageManager.getLaunchIntentForPackage(packageName);
        } else {
            return null;
        }
    }

    /**
     * 能不能打开DeepLink
     *
     * @param applicationContext
     * @param landUrl
     * @return
     */
    public static boolean canOpenWithDeeplink(Context applicationContext, String landUrl) {
        try {
            if (TextUtils.isEmpty(landUrl)) {
                return false;
            }
            if (landUrl.startsWith("http") || landUrl.startsWith("youloft")) {
                return true;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(landUrl));
            List<ResolveInfo> resolveInfos = applicationContext.getPackageManager().queryIntentActivities(intent, 0);
            boolean ret = resolveInfos != null && !resolveInfos.isEmpty();
            return ret;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否可以打开Deeplink
     *
     * @param context     上下文
     * @param deeplink    链接地址
     * @param packageName 应用包名
     * @param packageVer  应用版本号
     * @return
     */
    public static boolean canOpenWithDeeplink(Context context, String deeplink, String packageName, String packageVer) {
        try {
            if (TextUtils.isEmpty(deeplink)) {
                return false;
            }
            //checkPackageRule
            if (!TextUtils.isEmpty(packageName)) {
                int appVer = 0;
                try {
                    appVer = Integer.parseInt(packageVer);
                } catch (Throwable unused) {
                }
                PackageManager pm = context.getPackageManager();
                if (appVer > 0) {
                    try {
                        PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                        if (packageInfo.versionCode < appVer) {
                            return false;
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
            String lowerDeeplink = deeplink.toLowerCase();
            if (lowerDeeplink.startsWith("http") || lowerDeeplink.startsWith("youloft")) {
                return true;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(deeplink));
            if (!TextUtils.isEmpty(packageName)) {
                intent.setPackage(packageName);
            }
            List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
            return null != resolveInfos && !resolveInfos.isEmpty();
        } catch (Throwable e) {
            return false;
        }
    }

    private static final String MANUFACTURER = Build.MANUFACTURER;

    /**
     * 是否是魅族
     *
     * @return
     */
    public static boolean isMeizu() {
        return "Meizu".equalsIgnoreCase(MANUFACTURER);
    }

    /**
     * 是否三星手机
     *
     * @return
     */
    public static boolean isSamsung() {
        return "Samsung".equalsIgnoreCase(MANUFACTURER);
    }

    /**
     * 是否是oppo
     *
     * @return
     */
    public static boolean isOppo() {
        return "OPPO".equalsIgnoreCase(MANUFACTURER);
    }

    /**
     * 是否在某个版本前
     *
     * @param sdkApi
     * @return
     */
    public static boolean beforeSDK(int sdkApi) {
        return Build.VERSION.SDK_INT < sdkApi;
    }

    /**
     * 是否在UI线程
     *
     * @return
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 获取当前应用版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(getPackageName(context), context);
        if (packageInfo == null) {
            return VERSION_CODE;
        }
        return packageInfo.versionCode;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(getPackageName(context), context);
        if (packageInfo == null) {
            return VERSION_NAME;
        }
        return packageInfo.versionName;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取包信息
     *
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfo(String packageName, Context context) {
        if (null == context || null == packageName) {
            return null;
        }
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
        } catch (/* NameNotFoundException */Exception e) {
            // 抛出找不到的异常，说明该程序已经被卸载
            return null;
        }
        return info;
    }

    /**
     * 获取cc
     *
     * @return
     */
    public static String getCC() {
        return Locale.getDefault().getCountry();
    }

    /**
     * 获取lang
     *
     * @return
     */
    public static String getLang() {
        return Locale.getDefault().getLanguage();
    }


    /**
     * 获取AndroidID
     */
    public static String getAndroidId(Context context) {
        try {
            ContentResolver cr = context.getContentResolver();
            final String aid = Settings.System.getString(cr, Settings.System.ANDROID_ID);
            if (TextUtils.isEmpty(aid)) {
                return "";
            }
            return aid;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 大于MiuiV7
     *
     * @return
     */
    public static boolean isAfterMiuiV7() {
        String miuiV = getSystemProp("ro.miui.ui.version.name", "UNKNOWN");
        if (TextUtils.isEmpty(miuiV) || "unknow".equalsIgnoreCase(miuiV)) {
            return false;
        }
        return "V7".compareTo(miuiV.toUpperCase()) < 0;
    }

    private static Method sGET_SYSTEM_PROP_METHOD = null;

    /**
     * 获取系统属性
     *
     * @param var0 名称Key
     * @param var1 默认值
     * @return
     */
    public static String getSystemProp(String var0, String var1) {
        try {
            if (sGET_SYSTEM_PROP_METHOD == null) {
                sGET_SYSTEM_PROP_METHOD = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
            }
            if (sGET_SYSTEM_PROP_METHOD != null) {
                return (String) sGET_SYSTEM_PROP_METHOD.invoke(null, var0, var1);
            }
            return var1;
        } catch (Throwable var3) {
            return var1;
        }
    }

    /**
     * 是否是mi ui os
     *
     * @return
     */
    public static boolean isMIUIOS() {
        return !TextUtils.isEmpty(getSystemProp("ro.miui.ui.version.name", null));
    }

    /**
     * 是否是EMUI
     */
    private static Boolean S_EMUI = null;

    /**
     * 是否是EMUI
     *
     * @return
     */
    public static boolean isEMUI() {
        if (S_EMUI == null) {
            S_EMUI = !TextUtils.isEmpty(getSystemProp("ro.build.hw_emui_api_level", null));
        }
        return S_EMUI;
    }


    /**
     * 是否是MIUIF
     *
     * @return
     */
    public static boolean isMIUI() {
        return a() || b() || c();
    }

    /**
     * 判断版本
     *
     * @return
     */
    public static boolean a() {
        return getSystemProp("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V5");
    }

    /**
     * 判断版本
     *
     * @return
     */
    public static boolean b() {
        return getSystemProp("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V6");
    }

    /**
     * 判断版本
     *
     * @return
     */
    public static boolean c() {
        return getSystemProp("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V7");
    }

    /**
     * 获取IMEI
     */
    public static String getIMEI(Context context) {
        return getIMEI(context, "unknow");
    }

    /**
     * 获取IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context, String defaultValue) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return defaultValue;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(telephonyManager.getDeviceId())) {
            return defaultValue;
        }
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取ICCID
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getICCID(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "unknow";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "unknow";
        }
        if (TextUtils.isEmpty(telephonyManager.getSimSerialNumber())) {
            return "unknow";
        }
        return telephonyManager.getSimSerialNumber();
    }

    /**
     * 获取 Operator
     *
     * @param context
     * @return
     */
    public static String getSimOperator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "";
        }
        return telephonyManager.getSimOperator();
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "unknow";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "unknow";
        }
        if (TextUtils.isEmpty(telephonyManager.getSubscriberId())) {
            return "unknow";
        }
        return telephonyManager.getSubscriberId();
    }

    /**
     * 获取MCC
     *
     * @return
     */
    public static String getMCC(Context context) {
        String imsi = getIMSI(context);
        if (TextUtils.isEmpty(imsi) || "unknow".equalsIgnoreCase(imsi) || imsi.length() < 3) {
            return "";
        }
        return imsi.substring(0, 3);
    }

    /**
     * 获取MNC
     *
     * @return
     */
    public static String getMNC(Context context) {
        String imsi = getIMSI(context);
        if (TextUtils.isEmpty(imsi) || "unknow".equalsIgnoreCase(imsi) || imsi.length() < 5) {
            return "";
        }
        return imsi.substring(3, 5);
    }

    /**
     * 获取运营商¬
     *
     * @return 0-移动  1-联通  2-电信 99-unknow
     */
    public static String getCarrier(Context context) {
        String imsi = getIMSI(context);
        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            //移动
            return "0";
        } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
            //联通
            return "1";
        } else if (imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011")) {
            //电信
            return "2";
        } else {
            return "99";
        }
    }

    /**
     * 应用市场
     */
    static final ArrayList<String> MarketPackageList = new ArrayList<>();

    static {
        MarketPackageList.add("com.huawei.appmarket");//华为
        MarketPackageList.add("com.oppo.market");//Oppo
        MarketPackageList.add("com.bbk.appstore");//Vivo
        MarketPackageList.add("com.xiaomi.market");//小米
        MarketPackageList.add("com.meizu.mstore");//Flyme
        MarketPackageList.add("zte.com.market");//中兴
        MarketPackageList.add("com.lenovo.leos.appstore");//联想
        MarketPackageList.add("com.wandoujia.phoenix2");//碗豆荚
        MarketPackageList.add("com.tencent.android.qqdownloader");//应用宝
        MarketPackageList.add("com.qihoo.appstore");//360
        MarketPackageList.add("com.baidu.appsearch");//百度
    }

    /**
     * 打开应用市场
     *
     * @param context
     * @param packageName
     * @param isDownload  是否是为了下载，如果仅是为了下载，如果应用已经安装我们会自动调起这个应用，如果不是为了下载我们会始终打开应用市场
     */
    public static void openMarket(Context context, String packageName, boolean isDownload) {
        try {
            if (isDownload) {
                Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntentForPackage != null) {
                    launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntentForPackage);
                    return;
                }
            }
        } catch (Exception e) {
        }
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        for (String pname : MarketPackageList) {
            intent.setPackage(pname);
            try {
                context.startActivity(intent);
                return;
            } catch (Throwable e) {
            }
        }
        //三星市场的处理
        try {
            Intent goToMarket = new Intent();
            goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
            goToMarket.setData(Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName));
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goToMarket);
            return;
        } catch (Throwable e) {
        }

        //乐视市场的处理
        try {
            Intent goToMarket = new Intent();
            goToMarket.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
            goToMarket.setAction("com.letv.app.appstore.appdetailactivity");
            goToMarket.putExtra("packageName", packageName);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goToMarket);
            return;
        } catch (Throwable e) {
        }


        //默认市场不进行对应处理
        try {
            intent.setPackage(null);
            context.startActivity(intent);
        } catch (Throwable e) {
        }
    }

    static final ArrayList<String> BrowserPackageList = new ArrayList<>();

    static {
        BrowserPackageList.add("com.android.browser");
        BrowserPackageList.add("com.android.chrome");
        BrowserPackageList.add("com.tencent.mtt");
        BrowserPackageList.add("com.uc.browser");
        BrowserPackageList.add("com.opera.mini.android");
    }

    /**
     * 打开系统浏览器
     *
     * @param url
     */
    private static void openWithSystemBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        for (String pname : BrowserPackageList) {
            intent.setPackage(pname);
            try {
                context.startActivity(intent);
                return;
            } catch (Throwable e) {
            }
        }
        intent.setPackage(null);
        context.startActivity(intent);
    }
}
