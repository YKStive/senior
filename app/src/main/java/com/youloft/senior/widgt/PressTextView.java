package com.youloft.senior.widgt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.youloft.senior.R;

/**
 * @author xll
 * @date 2020/7/3 9:33
 */
public class PressTextView extends AppCompatTextView {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    final PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
//        postInvalidate();
    }

//    @Override
//    public void draw(Canvas canvas) {
//        if (!isPressed()) {
//            super.draw(canvas);
//            return;
//        }
//        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
//        super.draw(canvas);
//        mPaint.setColor(0xff000000);
//        mPaint.setAlpha(14);
//        mPaint.setXfermode(xfermode);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
//        mPaint.setXfermode(null);
//        canvas.restore();
//    }

}
