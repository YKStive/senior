package com.youloft.senior.ui.graphic

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.shinichi.library.ImagePreview
import cc.shinichi.library.bean.ImageInfo
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.toast
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.itembinder.ContextImageItemBinder
import com.youloft.senior.ui.gif.ChoiceImageActivity
import com.youloft.senior.widgt.GridSpaceItemDecoration
import kotlinx.android.synthetic.main.activity_content_publish.*
import java.util.ArrayList

/**
 * @author you
 * @create 2020/6/29
 * @desc
 */
class ContextImageView(context: Context, attributeSet: AttributeSet?) :
    RecyclerView(context, attributeSet) {


    private val imageCountLimit = 20
    private val mItem: MutableList<ImageRes> = mutableListOf()
    val emptyData: MutableLiveData<Boolean> = MutableLiveData()
    val imageData = MutableLiveData(mItem)
    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter(mItem)

    init {
        if (attributeSet == null) {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        layoutManager = GridLayoutManager(App.instance(), 3)
        addItemDecoration(GridSpaceItemDecoration(3, 5.dp2px, 5.dp2px))

        mAdapter.register(ImageRes::class, ContextImageItemBinder(mItem, { isAddImage, position ->
            if (isAddImage) {
                if (mItem.size - 1 == imageCountLimit) {
                    context.toast("图片最多只能选择${imageCountLimit}张")
                } else {
                    ChoiceImageActivity.start(
                        getContext() as FragmentActivity,
                        imageCountLimit,
                        ChoiceImageActivity.TYPE_ALL
                    ) {
                        setImages(it)
                    }
                }
            } else {
                val result = mutableListOf<ImageInfo>()
                mItem.forEach { imageRes ->
                    if (imageRes.path.isNotEmpty()) {
                        result.add(ImageInfo().apply {
                            originUrl = imageRes.path
                            thumbnailUrl = imageRes.path
                        })
                    }
                }
                ImagePreview
                    .getInstance()
                    .setContext(getContext() as FragmentActivity)
                    .setIndex(position)
                    .setImageInfoList(result)
                    .setShowDownButton(false)
                    .setZoomTransitionDuration(500)
                    .start()
            }
        }, {
            emptyData.value = true
        }))

        adapter = mAdapter
    }


    fun setImages(it: ArrayList<ImageRes>) {
        val startSize: Int = mItem.size - 1
        mItem.addAll(startSize, it)
        mAdapter.notifyItemRangeInserted(startSize, it.size)
        imageData.value = mItem

    }

    fun getData(): List<ImageRes> {
        return mItem.filter {
            !it.isAddIcon
        }
    }
}