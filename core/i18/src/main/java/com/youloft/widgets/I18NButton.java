package com.youloft.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.youloft.trans.I18N;

/**
 * Created by javen on 14-8-18.
 */
public class I18NButton extends Button {
    public I18NButton(Context context) {
        super(context);
    }

    public I18NButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public I18NButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(I18N.convert(text), type);
    }
}
