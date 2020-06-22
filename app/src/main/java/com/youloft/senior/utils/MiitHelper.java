package com.youloft.senior.utils;

import android.content.Context;
import android.os.Build;

import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;

import okhttp3.HttpUrl;

/**
 * Created by zheng on 2019/8/22.
 */

public class MiitHelper {

    //    5、应⽤匿名设备标识符
    private static String AAID = "";
    //3、匿名设备标识符
    private static String OAID = "";
    //4、开发者匿名设备标识符
    private static String VAID = "";
    //2、应⽤匿名设备标识符
//    private static String UDID = "";

    private static int state = -1;

    public static void updateDeviceIdByMiit(Context cxt) {
        if (state != -1) {
            return;
        }
        if (Build.VERSION.SDK_INT < 29) {
            return;
        }

        try {
            JLibrary.InitEntry(cxt);
            state = MdidSdkHelper.InitSdk(cxt, false, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean b, IdSupplier idSupplier) {
                    try {
                        if (!idSupplier.isSupported()) {
                            return;
                        }
                        try {
                            MiitHelper.AAID = idSupplier.getAAID();
                        } catch (Throwable ignored) {
                        }
                        try {
                            MiitHelper.OAID = idSupplier.getOAID();
                        } catch (Throwable ignored) {
                        }
                        try {
                            MiitHelper.VAID = idSupplier.getVAID();
                        } catch (Throwable ignored) {
                        }
                    } catch (Throwable ignored) {
                    }
                }
            });
        } catch (Throwable e) {
        }
    }


    public static String getAAID() {
        return AAID;
    }

//    public static String getUDID() {
//        return UDID;
//    }

    public static String getOAID() {
        return OAID;
    }

    public static String getVAID() {
        return VAID;
    }

    public static void fillInQuery(HttpUrl.Builder urlBuilder) {
        urlBuilder.addQueryParameter("vaid", VAID);
    }
}
