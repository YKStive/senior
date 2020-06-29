package com.youloft.senior.ui.graphic

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.coolktx.dp2px
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.cash.dp
import com.youloft.senior.itembinder.ContextImageItemBinder
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.ui.gif.GifActivity
import com.youloft.senior.widgt.GridSpaceItemDecoration
import kotlinx.android.synthetic.main.activity_choice_image.*
import kotlinx.android.synthetic.main.activity_content_publish.*

/**
 * @author you
 * @create 2020/6/29
 * @desc
 */
class ContentPublishActivity : BaseVMActivity() {

    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter()
    private val mItem: MutableList<ImageRes> = mutableListOf()
    override fun getLayoutResId(): Int {
        return R.layout.activity_content_publish
    }

    override fun initView() {
        rv_image.layoutManager = GridLayoutManager(applicationContext, 3)
        rv_image.addItemDecoration(GridSpaceItemDecoration(3, 5.dp2px, 5.dp2px))

        mAdapter.register(ImageRes::class, ContextImageItemBinder(mItem) { isAddImage, position ->
            if (isAddImage) {
                ChoiceImageActivity.start(this, 20) {
                    val startSize:Int = if(mItem.size==1)(mItem.size - 1) else mItem.size-2
                    mItem.addAll(startSize, it)
                    mAdapter.notifyItemRangeInserted(startSize, it.size)
                }
            }
        })

        rv_image.adapter = mAdapter
        mAdapter.items = mItem
        mAdapter.notifyDataSetChanged()

    }

    override fun initData() {
    }

    override fun startObserve() {
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, ContentPublishActivity::class.java)
            context.startActivity(intent)
        }
    }
}