package com.youloft.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

/**
 * 截图辅助工具类
 *
 * @author xll
 * @date 2018/8/30 15:59
 */
public class ScreenshotUtil {

    /**
     * 截屏
     *
     * @param activity     截取activity
     * @param removeStatus 是否需要移除状态栏
     * @return 截图
     */
    public static Bitmap screenshot(Activity activity, boolean removeStatus) {
        try {
            View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Bitmap b1 = loadBitmapFromView(view);
            Bitmap dest = null;
            if (removeStatus) {
                Rect frame = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                dest = Bitmap.createBitmap(b1, 0, statusBarHeight, b1.getWidth(), b1.getHeight()
                        - statusBarHeight);
            } else {
                dest = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight());
            }
            return dest;
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 截图view
     *
     * @param view 截取view
     * @return 截图的图
     */
    public static Bitmap screenshot(View view) {
        if (view == null) {
            return null;
        }
        try {
            return loadBitmapFromView(view);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 截图view
     *
     * @param v 截取view
     * @return 截取的图
     */
    private static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }
}
