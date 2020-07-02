package com.youloft.senior.ui.detail

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.MineDataBean
import com.youloft.senior.bean.PraiseBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.adapter.CommentAdapterr
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.utils.UserManager
import com.youloft.senior.utils.logD
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.share.ShareImage
import com.youloft.socialize.share.ShareWeb
import com.youloft.socialize.share.UmengShareActionImpl
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.conmon_title.*
import kotlinx.android.synthetic.main.pop_post_more.view.*
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
class DetailActivity : BaseVMActivity() {
    lateinit var informationId: String
    var informationType: Int = 0
    lateinit var adapterr: CommentAdapterr
    lateinit var videoFragment: GIFDetailFragment
    lateinit var mPopupWindow: PopupWindow
    lateinit var mViewModel: DetailViewModel

    //帖子发布人id
    var postData: ItemData? = null
//    var postUserId: String = ""


    companion object {
        fun start(
            context: FragmentActivity,
            informationId: String,
            informationType: Int,
            isComment: Boolean? = false
        ) {
            val starter = Intent(context, DetailActivity::class.java)
            starter.putExtra("informationId", informationId)
            starter.putExtra("informationType", informationType)
            isComment?.apply {
                starter.putExtra("isComment", isComment)
            }
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
        fragments.add(FavoriteFragment.newInstance(informationId))
        tablayout.setViewPager(viewPager, arrayOf("全部评论(0)", "点赞(0)"), this, fragments)
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
        //分享到朋友圈全是链接
        ll_share_to_circle.setOnClickListener(View.OnClickListener {
            shareWithH5(SOC_MEDIA.WEIXIN_CIRCLE)

        })
        ll_share_to_friend.setOnClickListener {
            if (informationType != MineDataBean.GIF_TYPE) {
                shareWithH5(SOC_MEDIA.WEIXIN)
            } else {
                //todo 分享给好友，除了表情是一张图外，其它也是链接；
            }
        }
        //点赞帖子
        ll_favorite.setOnClickListener {
            val userManager = UserManager.instance
            mViewModel.addFavorite(
                PraiseBean(
                    userManager.getAvatar(),
                    userManager.getNickname(),
                    informationId,
                    postData?.userId,
                    userManager.getUserId()
                )
            )
        }


        //发表评论
        tv_sen_comment.setOnClickListener {
            if (!UserManager.instance.hasLogin()) {
                LoginDialog(this, lifecycleScope).show()
                return@setOnClickListener
            }
            var userMaster = UserManager.instance
            mViewModel.comment(
                PraiseBean(
                    userMaster.getAvatar(),
                    userMaster.getNickname(),
                    informationId,
                    postData?.userId,
                    userMaster.getUserId(),
                    edt_comment.text.toString()
                )
            )
        }
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

    private fun shareWithH5(target: SOC_MEDIA) {
        UmengShareActionImpl(this).platform(target).web(
            ShareWeb("http://www.baidu.com").setThumb(
                ShareImage(
                    this,
                    "http://mmbiz.qpic.cn/mmbiz/PwIlO51l7wuFyoFwAXfqPNETWCibjNACIt6ydN7vw8LeIwT7IjyG3eeribmK4rhibecvNKiaT2qeJRIWXLuKYPiaqtQ/0"
                )
            )
                .setDescription("内容").setTitle("标题")
        ).perform()
    }

    override fun initData() {

    }

    override fun startObserve() {
        mViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        mViewModel.postInfo.observe(this, Observer {
            postData = it
            tablayout.getTitleView(0).setText("全部评论(${it.commented})")
            tablayout.getTitleView(1).setText("点赞(${it.praised})")
        })
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
        if (!UserManager.instance.hasLogin()) {
            LoginDialog(this, lifecycleScope).show()
            return
        }
        mPopupWindow = PopupWindow(this)
        var popView = LayoutInflater.from(this).inflate(R.layout.pop_post_more, null)
        // 设置布局文件
        mPopupWindow.setContentView(popView)
        if (postData?.userId.equals(UserManager.instance.getUserId())) {
            popView.tv_delete_report.setText("删除")
            popView.tv_delete_report.setOnClickListener {
                lifecycleScope.launchIOWhenCreated({
                    ToastMaster.showShortToast(this, it.message)
                    it.message?.logD()
                }, {
                    val deleteRes = ApiHelper.api.deletePost(informationId)
                    withContext(Dispatchers.Main) {
                        ApiHelper.executeResponse(deleteRes, {
                            if (it) {
                                finish()
                            }
                        })
                    }
                })
            }
        } else {
            popView.tv_delete_report.setOnClickListener {
                //todo 举报
            }
        }

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

