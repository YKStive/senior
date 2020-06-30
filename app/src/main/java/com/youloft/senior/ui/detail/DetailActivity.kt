package com.youloft.senior.ui.detail

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.utils.logD
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.share.ShareImage
import com.youloft.socialize.share.ShareWeb
import com.youloft.socialize.share.UmengShareActionImpl
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.conmon_title.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    lateinit var informationId: String
    var informationType: Int = 0
    lateinit var adapterr: CommentAdapterr
    lateinit var videoFragment: GIFDetailFragment
    lateinit var mPopupWindow: PopupWindow

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
        image_more.visibility = View.VISIBLE
        var fragments = ArrayList<Fragment>()
        informationId = intent.getStringExtra("informationId")
        informationType = intent.getIntExtra("informationType", 0)
        fragments.add(ItemCommentFragment.newInstance(informationId))
        fragments.add(FavoriteFragment.newInstance())
        tablayout.setViewPager(viewPager, arrayOf("全部评论", "点赞"), this, fragments)
        if (informationType == MineDataBean.MOVIE_TYPE) {
            //影集
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    MovieAndGifDetailFragment.newInstance(
                        MineDataBean.MOVIE_TYPE,
                        "",
                        informationId
                    )
                )
                .commit()
        } else if (informationType == MineDataBean.GIF_TYPE) {
            //gif
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    MovieAndGifDetailFragment.newInstance(MineDataBean.GIF_TYPE, "", informationId)
                )
                .commit()
        } else if (informationType == MineDataBean.IMAGE_TYPE) {
            //图文
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    PictureAndTextFragment.newInstance(informationId)
                )
                .commit()
        } else if (informationType == MineDataBean.VIDEO_TYPE) {
            //视频
            videoFragment = GIFDetailFragment.newInstance(informationId)
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.framelayout_detail,
                    videoFragment
                )
                .commit()
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
//        supportFragmentManager.beginTransaction()
//            .add(R.id.fl_tab_containar, ItemCommentFragment.newInstance())
////            .add(R.id.fl_tab_containar, FavoriteFragment.newInstance())
//            .commit()
//
//        initListeners()

        ll_share_to_circle.setOnClickListener(View.OnClickListener {


        })
        //点赞帖子
        ll_favorite.setOnClickListener(View.OnClickListener {
            lifecycleScope.launchIOWhenCreated({
                it.message?.logD()
            }, {
//            val stickers = ApiHelper.api.getStickers()
                var params = HashMap<String, String>()
//TODO
//                params.put("postId", informationId)
//                params.put("userId", itemBean.id)
//                params.put("avatar", itemBean.id)
//                params.put("nickname", itemBean.id)
//                            val res = NetResponse<String>(ApiHelper.api.parse(params).data, "", "", 200)
                val res = ApiHelper.api.parse(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {

                    }
//                                ApiHelper.executeResponse(res, {
//                                    if (res.status==200)
//                                })
                }
            })
        })


//发表评论
        tv_sen_comment.setOnClickListener(View.OnClickListener {
            lifecycleScope.launchIOWhenCreated({
                it.message?.logD()
            }, {
//            val stickers = ApiHelper.api.getStickers()
                var params = HashMap<String, String>()
//TODO
//                params.put("postId", informationId)
//                params.put("userId", itemBean.id)
//                params.put("avatar", itemBean.id)
//                params.put("nickname", itemBean.id)
                params.put("content", edt_comment.text.toString())
//                            val res = NetResponse<String>(ApiHelper.api.parse(params).data, "", "", 200)
                val res = ApiHelper.api.commnet(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {

                    }
//                                ApiHelper.executeResponse(res, {
//                                    if (res.status==200)
//                                })
                }
            })
        })
        edt_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    ll_favorite.visibility = View.GONE
                    tv_sen_comment.visibility = View.VISIBLE
                } else {
                    ll_favorite.visibility = View.VISIBLE
                    tv_sen_comment.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        image_more.setOnClickListener {
            showPop()
        }
    }

//    private fun initListeners() {
//        //获取内容总高度
//        val vto: ViewTreeObserver = ll_content.getViewTreeObserver()
//        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                stickScrollHeight = ll_content.getHeight()
//                //注意要移除
//                ll_content.getViewTreeObserver()
//                    .removeOnGlobalLayoutListener(this)
//            }
//        })
//
////        //获取Fragment高度
////        val viewTreeObserver: ViewTreeObserver = viewpager.getViewTreeObserver()
////        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
////            override fun onGlobalLayout() {
////                stickScrollHeight = stickScrollHeight - viewpager.getHeight()
////                //注意要移除
////                viewpager.getViewTreeObserver()
////                    .removeOnGlobalLayoutListener(this)
////            }
////        })
//
//        //获取title高度
//        val viewTreeObserver1: ViewTreeObserver = constraintlayout_title.getViewTreeObserver()
//        viewTreeObserver1.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                stickScrollHeight =
//                    stickScrollHeight - constraintlayout_title.getHeight() - getStatusHeight(this@DetailActivity) //计算滑动的总距离
//                scrollView.setStickTop(constraintlayout_title.getHeight());//设置距离多少悬浮
//            }
//        })
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initData() {

    }

    override fun onBackPressed() {
        if (informationType == MineDataBean.VIDEO_TYPE) {
            if (videoFragment.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
//        finish()
    }

    private fun showPop() {
        mPopupWindow= PopupWindow(this)
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_post_more, null))
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(ColorDrawable(0x0000))
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add)
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true)
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true)        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失

        mPopupWindow.setOutsideTouchable(true)
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAsDropDown(image_more)
        // 设置pop关闭监听，用于改变背景透明度
//        mPopupWindow.setOnDismissListener(PopupWindow.OnDismissListener { toggleBright() })

    }
}

