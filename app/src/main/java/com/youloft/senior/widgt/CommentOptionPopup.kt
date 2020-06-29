package com.youloft.senior.widgt

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.youloft.senior.R
import kotlinx.android.synthetic.main.popup_comment_option.view.*

/**
 * @Description: 帖子评论长按弹出
 * @Author: slh
 * @CreateDate: 2020/6/29 15:12
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/6/29 15:12
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
class CommentOptionPopup(
    mContext: Activity?
) : PopupWindow() {
    private val mContext: Activity? = null
    private val view: View


    init {
        view = LayoutInflater.from(mContext)
            .inflate(R.layout.popup_comment_option, null)
        view.tv_copy.setOnClickListener(View.OnClickListener {

        })
        view.tv_report.setOnClickListener(View.OnClickListener {

        })
        view.tv_cancle.setOnClickListener(View.OnClickListener {
            dismiss()
        })
        // 设置外部可点击
        this.isOutsideTouchable = true
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener { v, event ->
            val height =
                view.findViewById<View>(R.id.pop_layout)
                    .top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            v.performClick()
            true
        }


        /* 设置弹出窗口特征 */
        // 设置视图
        this.contentView = view
        // 设置弹出窗体的宽和高
        this.height = RelativeLayout.LayoutParams.MATCH_PARENT
        this.width = RelativeLayout.LayoutParams.MATCH_PARENT

        // 设置弹出窗体可点击
        this.isFocusable = true

        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(-0x50000000)
        // 设置弹出窗体的背景
        setBackgroundDrawable(dw)

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.animationStyle = R.style.take_photo_anim
    }
}