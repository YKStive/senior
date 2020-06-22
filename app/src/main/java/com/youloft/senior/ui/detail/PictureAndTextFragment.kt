package com.youloft.senior.ui.detail

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import cc.shinichi.library.ImagePreview
import cc.shinichi.library.bean.ImageInfo
import com.youloft.core.base.BaseFragment
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.PictureAdapter
import com.youloft.senior.ui.adapter.PictureAdapter.ImageOpertor
import kotlinx.android.synthetic.main.fragment_text_and_picture_layout.*

/**
 *
 * @Description:     图文详情
 * @Author:         slh
 * @CreateDate:     2020/6/22 14:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/22 14:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class PictureAndTextFragment : BaseFragment() {
    lateinit var myAdapter: PictureAdapter

    //TODO 需要初始化
    lateinit var imageList: List<String>
    override fun getLayoutResId(): Int = R.layout.fragment_text_and_picture_layout

    override fun initView() {
        myAdapter = activity?.let {
            PictureAdapter(it, mutableListOf())
        }!!
        myAdapter.imageOpertor = object : ImageOpertor {
            override fun showImage(imageData: List<String>, pos: Int) {
                var imageInfo: ImageInfo?
                val imageInfoList: MutableList<ImageInfo?> = ArrayList()
                for (i in 0 until imageList.size) {
                    imageInfo = ImageInfo()
                    imageInfo.setOriginUrl(imageList.get(i)) // 原图
                    imageInfo.setThumbnailUrl(
                        imageList.get(i)
                    ) // 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
                    imageInfoList.add(imageInfo)
                    imageInfo = null
                }
                activity?.let {
                    ImagePreview
                        .getInstance()
                        .setContext(it)
                        .setIndex(pos)// 默认显示第几个
                        .setImageInfoList(imageInfoList)// 图片集合
                        .setShowDownButton(true)// 是否显示下载按钮
//                    .setShowOriginButton(true)// 是否显示查看原图按钮
                        .setFolderName("BigImageViewDownload")// 设置下载到的文件夹名（保存到根目录）
//                    .setScaleLevel(1, 3, 8)// 设置三级放大倍数，分别是最小、中等、最大倍数。
                        .setZoomTransitionDuration(500)// 设置缩放的动画时长
                        .start();// 开始跳转 }
                }
            }
        }
        recycler_view.run {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = myAdapter
        }
    }


    override fun initData() {
    }

    companion object {
        fun newInstance(): PictureAndTextFragment {
            val args = Bundle()
            val fragment = PictureAndTextFragment()
            fragment.arguments = args
            return fragment
        }
    }

//    fun addImg(img: MutableList<String>) {
//        var view = LayoutInflater.from(activity).inflate(R.layout.imageview, rl_imagcontent, false)
//        for (index in 1..3) {//几层
//            var v: View
//            for ()
//        }
//    }

}

