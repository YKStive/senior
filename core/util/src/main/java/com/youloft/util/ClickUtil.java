package com.youloft.util;

import android.view.View;

/**
 * @author xll
 * @date 2018/7/25 17:15
 */
public class ClickUtil {
    /**
     * 判断重复点击的间隔时间，此时间以内算重复点击
     */
    private static final int FAST_CLICK_TIME = 500;
    /**
     * 防止多次点击
     */
    private static long lastClickTime;

    /**
     * 是否是连续点击
     * 当次点击与上次点击间隔在500毫秒以内是重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < FAST_CLICK_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否能进行点击
     *
     * @return true 能点击， other  不能点击
     */
    public static boolean canClick() {
        if (Math.abs(lastClickTime - System.currentTimeMillis()) > FAST_CLICK_TIME) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * 处理点击后，在多次点击的时间内部允许点击
     *
     * @param view
     */
    public static void fastDoubleClick(final View view) {
        fastDoubleClick(view, FAST_CLICK_TIME);
    }

    /**
     * 处理点击后，在多次点击的时间内部允许点击
     *
     * @param view
     */
    public static void fastDoubleClick(final View view, long timeValue) {
        if (view == null) {
            return;
        }
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, timeValue);
    }
}
