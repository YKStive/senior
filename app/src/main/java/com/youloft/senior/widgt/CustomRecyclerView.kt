package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.youloft.senior.utils.logE


/**
 *
 * @Description:     java类作用描述
 * @Author:         slh
 * @CreateDate:     2020/6/28 13:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/28 13:29
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
private const val TAG = "CustomRecyclerView"

class CustomRecyclerView(
    context: Context,
    attrs: AttributeSet?
) : RecyclerView(context, attrs) {
    var lastx: Int = 0;
    var lasty: Int = 0;
    var countY: Int = 0


    override fun onTouchEvent(e: MotionEvent): Boolean {
//        return super.onTouchEvent(e)
//        var x: Int = e.x.toInt()
        return false
        var y = e.y.toInt()
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                countY += y;
                TAG.logE("总共滑动=${countY}")
                TAG.logE("scrollY=${scrollY}")
                TAG.logE((countY>2000).toString())
                return countY>2000
            }
        }
//            MotionEvent.ACTION_DOWN -> {
//
//            }
//        }
//        lastx = x;
//        lasty = y
        TAG.logE(super.onTouchEvent(e).toString())
        return super.onTouchEvent(e)
    }
}