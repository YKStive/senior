package com.youloft.core.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.youloft.core.R

/**
 * @author xll
 * @date 2020/6/23 13:30
 */
abstract class BaseBottomDialog(ctx: Context, style: Int = R.style.DialogTheme) :
    Dialog(ctx, style) {
    init {
        if (Build.VERSION.SDK_INT >= 19) {
            val window = window
            window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= 21) {
                window!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window!!.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window!!.statusBarColor = Color.TRANSPARENT
            }
        }

    }
}