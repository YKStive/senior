package com.youloft.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.youloft.trans.I18N;

/**
 * Created by 987654 on 2015/6/29.
 */
public class I18NCheckBox extends CheckBox {
    private Drawable mButtonDrawable;

    public I18NCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            super.setText(text, type);
            return;
        }
        if (text instanceof Spanned) {
            super.setText(text, type);
            return;
        }
        super.setText(I18N.convert(text), type);
    }

    @Override
    public void setButtonDrawable(Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        mButtonDrawable = buttonDrawable;
    }

    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (mButtonDrawable != null) {
                padding += mButtonDrawable.getIntrinsicWidth();
            }
        }
        return padding;
    }
}
