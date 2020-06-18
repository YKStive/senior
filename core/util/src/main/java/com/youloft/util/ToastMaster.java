package com.youloft.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 提示master
 *
 * @author javen
 * @date 14-7-21
 */
public class ToastMaster {

    private static Toast mToast;

    /**
     * 显示短提示
     *
     * @param context
     * @param message
     * @param args
     */
    public static void showShortToast(final Context context, final Object message, final Object... args) {
        cancelToast(mToast);
        if (message == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mToast = showToast(context, message, args);
                }
            });
        } else {
            mToast = showToast(context, message, args);
        }
    }

    private static void setToastView(Context context, CharSequence convert, Toast toast) {
        //加载Toast布局
        TextView toastRoot = (TextView) LayoutInflater.from(context).inflate(R.layout.common_toast_ui, null);
        toastRoot.setText(convert);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toast.setGravity(Gravity.TOP, 0, (int) (height * 0.6f));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
    }

    /**
     * 显示长提示
     *
     * @param context
     * @param message
     * @param args
     */
    public static void showLongToast(final Context context, final Object message, final Object... args) {
        cancelToast(mToast);
        if (message == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mToast = showToast(context, message, args);
                }
            });
        } else {
            mToast = showToast(context, message, args);
        }
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     * @param args
     * @return
     */
    private static Toast showToast(Context context, Object message, Object[] args) {
        try {
            Toast toast = new Toast(context);
            setToastView(context, I18NUtil.convert(String.format(message.toString(), args)), toast);
            toast.show();
            return toast;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 取消toast
     *
     * @param toast
     */
    public static void cancelToast(final Toast toast) {
        if (toast != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 100);
        }
    }

    /**
     * 清空toast
     */
    public static void clean() {
        mToast = null;
    }
}
