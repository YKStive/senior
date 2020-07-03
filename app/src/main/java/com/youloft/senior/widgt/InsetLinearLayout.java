package com.youloft.senior.widgt;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Jie Zhang on 2017/2/3.
 */

public class InsetLinearLayout extends LinearLayout {
    public InsetLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
        }
        return super.fitSystemWindows(insets);
    }

}
