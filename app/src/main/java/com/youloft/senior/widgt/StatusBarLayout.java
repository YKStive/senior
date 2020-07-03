package com.youloft.senior.widgt;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.core.view.WindowInsetsCompat;

import com.youloft.util.UiUtil;

/**
 * 状态栏Layout
 * <p>
 * Created by Jie Zhang on 2017/1/24.
 */
public class StatusBarLayout extends FrameLayout {

    public StatusBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImmerseToolbar();
    }

    private boolean isAddPadding = false;

    /**
     * 设置支持沉浸式状态栏
     */
    public void setImmerseToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !isAddPadding) {
            isAddPadding = true;
            setPadding(getPaddingLeft(), getPaddingTop() + getStatusHeight(getContext()), getPaddingRight(), getPaddingBottom());
        }
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        return super.dispatchApplyWindowInsets(insets);
    }

    private static int S_STATUBAR_HEIGHT = -1;

    /**
     * 获取状态栏的高度
     * 仅获取一次
     *
     * @return
     */
    public static int getStatusHeight(Context context) {
        if (S_STATUBAR_HEIGHT < 1) {
            //Math.max(getNotchHeight(context), getNormalStatusHeight(context));
            S_STATUBAR_HEIGHT = getNormalStatusHeight(context);
        }
        return S_STATUBAR_HEIGHT;
    }

    static int _resStatusHeightId = -1;

    /**
     * 获取正常的屏幕中的状态栏高度
     *
     * @param context
     * @return
     */
    public static int getNormalStatusHeight(Context context) {
        try {
            if (_resStatusHeightId < 1) {
                Class clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                _resStatusHeightId = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            }
        } catch (Throwable e) {
            return UiUtil.dp2Px(context, 20);
        }
        return context.getResources().getDimensionPixelSize(_resStatusHeightId);
    }
}
