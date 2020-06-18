package com.youloft.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author xll
 * @date 2018/7/25 14:37
 */
public class UiUtil {
    /**
     * dp转px
     *
     * @param context context
     * @param dp      转化的值
     * @return 转化后px值
     */
    public static int dp2Px(Context context, float dp) {
        return (int) dp2PxF(context, dp);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context context
     * @param sp      （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2Px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * 缩放比
     *
     * @param context
     * @return
     */
    public static float getScaled(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * px转化到dp
     *
     * @param context context
     * @param px      转化的值
     * @return 转化后dp
     */
    public static float px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context context
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    /**
     * 获取屏幕高度
     *
     * @param context context
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }

    /**
     * 缓存屏幕Size
     */
    final static Point mScreenSize = new Point();

    /**
     * 获取屏幕大小（真实大小）
     *
     * @param context
     * @return
     */
    public static Point getScreenSize(Context context) {
        if (mScreenSize.x != 0 && mScreenSize.y != 0) {
            return mScreenSize;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        int mWidthPixels = displayMetrics.widthPixels;
        int mHeightPixels = displayMetrics.heightPixels;

        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                display.getRealSize(mScreenSize);
                mWidthPixels = mScreenSize.x;
                mHeightPixels = mScreenSize.y;
            } catch (Exception ignored) {
            }
        }
        mScreenSize.set(mWidthPixels, mHeightPixels);
        return mScreenSize;
    }

    public static double getDiagonalInch(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // 屏幕宽度（像素）
        int width = dm.widthPixels;
        // 屏幕高度（像素）
        int height = dm.heightPixels;
        // 屏幕密度DPI（120 / 160 / 240）
        int densityDpi = dm.densityDpi;
        double screenSize = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) / densityDpi;
        return screenSize;
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dp      转化的值
     * @return 转化后px值
     */
    public static float dp2PxF(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 停止ListView滚动
     *
     * @param view 需要停止滚动的view
     */
    public static void stopListViewScroll(ListView view) {
        Field mFlingEndField = null;
        Method mFlingEndMethod = null;
        try {
            mFlingEndField = AbsListView.class.getDeclaredField("mFlingRunnable");
            mFlingEndField.setAccessible(true);
            mFlingEndMethod = mFlingEndField.getType().getDeclaredMethod("endFling");
            mFlingEndMethod.setAccessible(true);
        } catch (Exception e) {
            mFlingEndMethod = null;
        }
        if (mFlingEndMethod != null) {
            try {
                mFlingEndMethod.invoke(mFlingEndField.get(view));
            } catch (Exception e) {
            }
        }
    }

    /**
     * 点亮屏幕
     *
     * @param context 上下文
     */
    public static void lightScreen(Context context) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                //点亮屏幕
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wnl:alarm");
                wl.acquire();
                wl.release();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 收起软键盘字
     */
    public static void collapseSoftInputMethod(EditText et, Context context) {
        if (et == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInputMethod(EditText et, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }


    private static Method GET_REALMETRICS_METHOD = null;

    /**
     * 获取虚拟导航的高度
     *
     * @param context
     * @return
     */
    public static int getVirtualBarHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            if (GET_REALMETRICS_METHOD == null) {
                @SuppressWarnings("rawtypes")
                Class c = Class.forName("android.view.Display");
                GET_REALMETRICS_METHOD = c.getMethod("getRealMetrics", DisplayMetrics.class);
            }
            if (GET_REALMETRICS_METHOD != null) {
                GET_REALMETRICS_METHOD.invoke(display, dm);
                return dm.heightPixels - display.getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
