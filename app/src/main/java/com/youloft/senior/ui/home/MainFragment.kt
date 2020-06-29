package com.youloft.senior.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cc.shinichi.library.ImagePreview
import cc.shinichi.library.bean.ImageInfo
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.ui.adapter.MineItemAdapter
import com.youloft.senior.ui.detail.DetailActivity
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 我的 列表三种类型，影集 图文照片 GIF
 */
class MainFragment : BaseVMFragment() {
    private val mViewModel by viewModels<MainViewModel>()
    private val mAdapter: MineItemAdapter =
        MineItemAdapter(::itemClick, ::itemShare, ::itemFavorite, ::imageClick)

    //    const val IMAGE_TYPE = 0;
//    const val VIDEO_TYPE = 1;
//    const val MOVIE_TYPE = 2;
//    const val GIF_TYPE = 3;
    fun itemClick(id: String, type: Int) {
        activity?.let { it1 ->
//            DetailActivity.start(it1, id, type)
            DetailActivity.start(it1, id, MineDataBean.IMAGE_TYPE)
        }
    }

    fun itemShare(id: String, type: Int) {
        ToastMaster.showShortToast(activity, "分享")
    }

    fun itemFavorite(id: String, type: Int) {
        ToastMaster.showShortToast(activity, "点赞")
    }

    fun imageClick(posi: Int, imgageList: List<String>?) {
        var imageInfo: ImageInfo?
        val imageInfoList: MutableList<ImageInfo?> = ArrayList()
        if (imgageList == null) {
            return
        }
        for (i in 0..imgageList.lastIndex) {
            imageInfo = ImageInfo()
            imageInfo.setOriginUrl(imgageList.get(i)) // 原图
            imageInfo.setThumbnailUrl(
                imgageList.get(i)
            ) // 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
            imageInfoList.add(imageInfo)
            imageInfo = null
        }
        activity?.let {
            ImagePreview
                .getInstance()
                .setContext(it)
                .setIndex(posi)// 默认显示第几个
                .setImageInfoList(imageInfoList)// 图片集合
                .setShowDownButton(true)// 是否显示下载按钮
//                    .setShowOriginButton(true)// 是否显示查看原图按钮
                .setFolderName("BigImageViewDownload")// 设置下载到的文件夹名（保存到根目录）
//                    .setScaleLevel(1, 3, 8)// 设置三级放大倍数，分别是最小、中等、最大倍数。
                .setZoomTransitionDuration(500)// 设置缩放的动画时长
                .start();// 开始跳转 }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        tv_browse_number.setOnClickListener {
            activity?.let { it1 ->
                DetailActivity.start(it1, "3", MineDataBean.VIDEO_TYPE)
            }
        }
    }

    override fun initData() {
//        var data = mutableListOf<MineData>()
//        for (i in 0..10) {
//            data.add(
//                MineData(
//                    "2020", "3", mutableListOf(), 2, 55, "ssss", 3, "koskofkodkodkodk"
//                    , 455, 48848
//                )
//            )
//        }
        mViewModel.getData(1, 0, 10,"")
//mViewModel
    }

    override fun startObserve() {
        mViewModel.resultData.observe(this, Observer {
            mAdapter.setList(it)
        })
    }

}