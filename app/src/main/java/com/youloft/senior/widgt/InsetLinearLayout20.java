package com.youloft.senior.widgt;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;

/**
 * Created by Jie Zhang on 2017/2/3.
 */

public class InsetLinearLayout20 extends InsetLinearLayout {
    public InsetLinearLayout20(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets wi = insets.replaceSystemWindowInsets(
                insets.getSystemWindowInsetLeft(),
                0,
                insets.getSystemWindowInsetRight(),
                insets.getSystemWindowInsetBottom());
        return super.dispatchApplyWindowInsets(wi);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
        }
        return super.fitSystemWindows(insets);
    }

}
