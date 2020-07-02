package com.youloft.senior.widgt;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youloft.senior.R;


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
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        Glide.with(context)
                .load(R.drawable.ic_progress_loading)
                .into((ImageView) dialog.findViewById(R.id.spinnerImageView));
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
