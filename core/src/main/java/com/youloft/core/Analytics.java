package com.youloft.core;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.meituan.android.walle.WalleChannelReader;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 * auth: xll
 * date: 2017/1/20
 */

public class Analytics {

    private static final String TAG = "Analytics";
    private static List<String> mAnalyticsList = new ArrayList();

    private static Application application;

    public static void init(Application application, String channel) {
        Analytics.application = application;
        TCAgent.init(application, "2D1E09A6E9E64B9688C3034343950789", channel);
    }

    /**
     * 上报缓存（如果需要，记录是否上报）
     *
     * @param key
     */
    public static void putAnalytics(String key) {
        mAnalyticsList.add(key);
    }

    /**
     * 清楚所有缓存的上报
     */
    public static void clearAnalytics() {
        mAnalyticsList.clear();
    }

    /**
     * 是否上报过
     *
     * @param key
     * @return
     */
    public static boolean isAnalytics(String key) {
        return mAnalyticsList.contains(key);
    }

    /**
     * 只上报一次
     *
     * @param key
     */
    public static void reportOnce(String key, String... module) {
        if (!isAnalytics(key)) {
            Analytics.reportEvent(key, null, module);
            Analytics.putAnalytics(key);
        }
    }

    /**
     * Activity重置
     *
     * @param activity
     */
    public static void postActivityResume(Activity activity) {
        TCAgent.onResume(activity);

    }

    /**
     * Activity 暂停
     *
     * @param activity
     */
    public static void postActivityPause(Activity activity) {
//        MobclickAgent.onPause(activity);
        TCAgent.onPause(activity);
    }

    public static void reportEvent(String name) {
        reportEvent(name, null);
    }

    /**
     * 暂时不要打开TalkingData的上报
     *
     * @param name
     * @param key
     */
    public static void reportEvent(String name, String key, String... module) {
        try {
            StringBuilder eventId = new StringBuilder(name.trim());

            if (module != null) {
                for (int i = 0; i < module.length; i++) {
                    if (TextUtils.isEmpty(module[i]) || module[i].startsWith("non-report")) {
                        continue;
                    }
                    eventId.append(".").append(module[i]);
                }
            }
            if (key == null) {
                Log.i(TAG, "reportEvent: eventID = " + eventId.toString());
                TCAgent.onEvent(application, eventId.toString());
            } else {
                Log.i(TAG, "reportEvent: eventID = " + eventId.toString() + ", key = " + key);
                TCAgent.onEvent(application, eventId.toString(), key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
