package com.youloft.senior.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.youloft.senior.base.App;
import com.youloft.util.AppUtil;
import com.youloft.util.MD5;
import com.youloft.util.NetUtil;
import com.youloft.util.StringUtils;

import java.util.UUID;

/**
 * @author xll
 * @date 2020/7/2 17:43
 */
public class CommonUtils {

    public static String getDeviceId() {
        SharedPreferences sp = getSP();
        String deviceId = sp.getString("deviceId", null);
        if (StringUtils.isEmpty(deviceId)) {
            synchronized (CommonUtils.class) {//加同步锁防止多线程下生成多个设置ID
                deviceId = obtainDeviceId();
                sp.edit().putString("deviceId", deviceId).apply();
            }
        }
        return deviceId;
    }

    private static SharedPreferences getSP() {
        return App.Companion.instance().getSharedPreferences("common_data_sp", Context.MODE_PRIVATE);
    }

    /**
     * 获取新的TOken
     *
     * @return
     */
    private synchronized static String obtainDeviceId() {
        return MD5.encode(
                String.valueOf(android.os.Build.DEVICE
                        + android.os.Build.VERSION.RELEASE
                        + AppUtil.getAndroidId(App.Companion.instance())
                        + AppUtil.getIMEI(App.Companion.instance())
                        + NetUtil.getLocalMacAddress(App.Companion.instance())
                        + UUID.randomUUID().toString()
                        + System.currentTimeMillis()));
    }
}
