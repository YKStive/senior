package com.youloft.senior.widgt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager


/**
 *
 * @Description:     java类作用描述
 * @Author:         slh
 * @CreateDate:     2020/6/28 14:20
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/28 14:20
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class CustomLinearLayoutManager(context: FragmentActivity?) : LinearLayoutManager(context) {
    public var canScroll = true
    override fun canScrollVertically(): Boolean {
        return canScroll && super.canScrollVertically()
    }
}