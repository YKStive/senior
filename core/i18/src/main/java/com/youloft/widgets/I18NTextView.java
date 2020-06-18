package com.youloft.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.youloft.trans.I18N;

/**
 * Created by javen on 14-8-18.
 */
public class I18NTextView extends TextView {
    public I18NTextView(Context context) {
        super(context);
    }

    boolean underLine = false;

    public I18NTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(attrs == null){
            return;
        }
        underLine = attrs.getAttributeBooleanValue(null, "underline", false);
    }

    public I18NTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(attrs == null){
            return;
        }
        underLine = attrs.getAttributeBooleanValue(null, "underline", false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (underLine) {
            getPaint().setUnderlineText(underLine);
        }
        super.onDraw(canvas);

    }

    String rawText = "";

    public String getRawText() {
        if (TextUtils.isEmpty(rawText) && getText() != null) {
            return getText().toString();
        }
        return rawText;

    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            rawText = "";
            super.setText(text, type);
            return;
        }
        rawText = text.toString();
        if (text instanceof Spanned) {
            super.setText(text, type);
            return;
        }
        super.setText(I18N.convert(text), type);

    }


}
