package com.youloft.util;

import android.content.Context;

/**
 * @author xll
 * @date 2018/8/30 18:25
 */
public class UtilContext {
    private static Context APP_CONTEXT;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        APP_CONTEXT = context;
    }

    static Context getContext() {
        return APP_CONTEXT;
    }
}
