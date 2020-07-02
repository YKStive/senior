package com.youloft.senior.widgt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.youloft.senior.R;
import com.youloft.senior.utils.DefaultTrans;


public class ProgressHUD extends Dialog {
    public ProgressHUD(Context context) {
        super(context);
    }

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }


    public static ProgressHUD show(Context context) {
        return show(context, "");
    }

    public static ProgressHUD show(Context context, CharSequence message) {
        return show(context, message, false, false, null);
    }

    public static ProgressHUD show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                                   OnCancelListener cancelListener) {
        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.base_progress_loading);
//        if (dialog.findViewById(R.id.message) != null) {
//            if (message == null || message.length() == 0) {
//                dialog.findViewById(R.id.message).setVisibility(View.GONE);
//            } else {
//                dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
//                TextView txt = (TextView) dialog.findViewById(R.id.message);
//                txt.setText(message);
//            }
//        }
        Transformation<Bitmap> circleCrop = new DefaultTrans();
        Glide.with(context)
                .load(R.drawable.ic_progress_loading)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(circleCrop))
                .into((ImageView) dialog.findViewById(R.id.iv_loading));
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }
}
