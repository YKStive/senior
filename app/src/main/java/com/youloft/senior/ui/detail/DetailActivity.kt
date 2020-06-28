package com.youloft.senior.ui.detail

import android.content.Intent
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.youloft.core.base.BaseActivity
import com.youloft.net.bean.CommentData
import com.youloft.net.bean.MineData
import com.youloft.senior.R
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.widgt.CustomLinearLayoutManager
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.share.ShareImage
import com.youloft.socialize.share.ShareWeb
import com.youloft.socialize.share.UmengShareActionImpl
import com.youloft.util.StatusBarUtils.getStatusHeight
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.activity_video_detail.recyclerView
import kotlinx.android.synthetic.main.conmon_title.*
import kotlinx.android.synthetic.main.fragment_item_comment.*

/**
 *
 * @Description:     所有类型的详情
 * @Author:         slh
 * @CreateDate:     2020/6/18 15:19
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/18 15:19
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class DetailActivity : BaseActivity() {
    private var stickScrollHeight=0
    lateinit var informationId: String
    var informationType: Int = 0
    lateinit var adapterr: CommentAdapterr

    companion object {
        fun start(context: FragmentActivity, informationId: String, informationType: Int) {
            val starter = Intent(context, DetailActivity::class.java)
            starter.putExtra("informationId", informationId)
            starter.putExtra("informationType", informationType)
            context.startActivity(starter)
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_video_detail

    override fun initView() {
        var fragments = ArrayList<Fragment>()
        fragments.add(ItemCommentFragment.newInstance())
        fragments.add(FavoriteFragment.newInstance())

//        tablayout.setViewPager(viewpager, arrayOf("全部评论", "点赞"), this, fragments)
        informationId = intent.getStringExtra("informationId")
        informationType = intent.getIntExtra("informationType", 0)
        if (informationType == MineData.MOVIE_TYPE) {
            //影集
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    MovieAndGifDetailFragment.newInstance(MineData.MOVIE_TYPE, "")
                )
                .commit()
        } else if (informationType == MineData.GIF_TYPE) {
            //gif
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    MovieAndGifDetailFragment.newInstance(MineData.GIF_TYPE, "")
                )
                .commit()
        } else if (informationType == MineData.IMAGE_TYPE) {
            //图文
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    PictureAndTextFragment.newInstance()
                )
                .commit()
        } else if (informationType == MineData.VIDEO_TYPE) {
            //视频
//            supportFragmentManager.beginTransaction()
//                .add(
//                    R.id.framelayout_detail,
//                    VideoD.newInstance(MovieDetailFragment.isMovie)
//                )
//                .commit()
        }
        btn_share_com.setOnClickListener {
            Toast.makeText(this@DetailActivity, "点击", Toast.LENGTH_SHORT).show()
            UmengShareActionImpl(this@DetailActivity).platform(SOC_MEDIA.WEIXIN_CIRCLE).web(
                ShareWeb("www.baidu.com").setThumb(ShareImage(this@DetailActivity, ""))
                    .setDescription("内容").setTitle("标题")
            ).perform()


//                .prepare(object :
//                    AbstractShareAction.PrepareListener {
//                    override fun onWorkThread(): Any {
//                        return Any();
//                    }
//
//                    override fun onMainThread(value: Any?) {
//                    }
//
//                })
//            )
//           var board= Socialize.getIns().newShareBoard(this@VideoDetailActivity);
//            board.withPlatform(SOC_MEDIA.WEIXIN).web(
//                ShareWeb("www.baidu.com").setThumb(ShareImage(this@VideoDetailActivity, ""))
//                    .setDescription("内容").setTitle("标题")
//            )
//            board.shareWithUI()

//            tablayout.getTitleView()
        }

        var data =mutableListOf<CommentData>()
        for(i in 0..10){
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
            data.add(CommentData("","","","","",""))
        }

        adapterr = CommentAdapterr(data)
        var manager= CustomLinearLayoutManager(this)
        recyclerView.layoutManager =manager
        recyclerView.adapter = adapterr
        initListeners()
    }
    private fun initListeners() {
        //获取内容总高度
        val vto: ViewTreeObserver = ll_content.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                stickScrollHeight = ll_content.getHeight()
                //注意要移除
                ll_content.getViewTreeObserver()
                    .removeOnGlobalLayoutListener(this)
            }
        })

//        //获取Fragment高度
//        val viewTreeObserver: ViewTreeObserver = viewpager.getViewTreeObserver()
//        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                stickScrollHeight = stickScrollHeight - viewpager.getHeight()
//                //注意要移除
//                viewpager.getViewTreeObserver()
//                    .removeOnGlobalLayoutListener(this)
//            }
//        })

        //获取title高度
        val viewTreeObserver1: ViewTreeObserver = constraintlayout_title.getViewTreeObserver()
        viewTreeObserver1.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                stickScrollHeight = stickScrollHeight - constraintlayout_title.getHeight() - getStatusHeight(this@DetailActivity) //计算滑动的总距离
                //                stickyScrollView.setStickTop(llTitle.getHeight() + getStatusHeight());//设置距离多少悬浮
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initData() {

    }
}

