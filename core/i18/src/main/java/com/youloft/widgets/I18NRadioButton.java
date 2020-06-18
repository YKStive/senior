package com.youloft.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;

import com.youloft.trans.I18N;

/**
 * Created by javen on 15/5/20.
 */
public class I18NRadioButton extends RadioButton {
    private Drawable mButtonDrawable;


    public I18NRadioButton(Context context) {
        super(context);
    }

    public I18NRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public I18NRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
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
