package com.youloft.senior.ui.otherUsers

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cc.shinichi.library.ImagePreview
import cc.shinichi.library.bean.ImageInfo
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.ui.adapter.MineItemAdapter
import com.youloft.senior.ui.detail.DetailActivity
import com.youloft.senior.ui.home.MainViewModel
import com.youloft.senior.utils.UserManager
import com.youloft.senior.widgt.LoginPopup
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.fragment_main.*


/**
 *
 * @Description:     他人 & 我自己   个人信息页面
 * @Author:         slh
 * @CreateDate:     2020/6/30 12:43
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/30 12:43
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */

class OtherUserInfoActivity : BaseVMActivity() {
    lateinit var userId: String

    companion object {
        private const val TAG = "OtherUserInfoActivity"

        @JvmStatic
        fun start(context: Context, userId: String) {
            val starter = Intent(context, OtherUserInfoActivity::class.java)
            starter.putExtra("userId", userId)
            context.startActivity(starter)
        }
    }

    private val mViewModel by viewModels<MainViewModel>()
    private val mAdapter: MineItemAdapter =
        MineItemAdapter(::itemClick, ::itemShare, ::itemFavorite, ::imageClick)

    override fun getLayoutResId(): Int {
        return R.layout.activity_other_user_info
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        tv_browse_number.setOnClickListener {
            DetailActivity.start(this, "3", MineDataBean.VIDEO_TYPE)
        }

    }

    override fun initData() {
        userId = intent.getStringExtra("userId")
        mViewModel.getData(1, 0, 10, userId)
    }

    override fun startObserve() {
        mViewModel.resultData.observe(this, Observer {
            mAdapter.setList(it)
        })
    }

    fun itemClick(id: String, type: Int) {
        //DetailActivity.start(it1, id, type)
        DetailActivity.start(this, id, MineDataBean.IMAGE_TYPE)
    }

    fun itemShare(id: String, type: Int) {
        ToastMaster.showShortToast(this, "分享")
    }

    fun itemFavorite(id: String, type: Int) {
        ToastMaster.showShortToast(this, "点赞")
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
        ImagePreview
            .getInstance()
            .setContext(this)
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