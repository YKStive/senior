package com.youloft.senior.ui.detail

import androidx.lifecycle.ViewModelProvider
import com.youloft.core.base.BaseVMFragment


/**
 *
 * @Description:     帖子评论
 * @Author:         slh
 * @CreateDate:     2020/6/22 18:05
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/22 18:05
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ItemCommentFragment : BaseVMFragment<ItemCommnetViewModel>() {
    override fun getLayoutResId(): Int = 0

    override fun initVM(): ItemCommnetViewModel =
        ViewModelProvider(this).get(ItemCommnetViewModel::class.java)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}