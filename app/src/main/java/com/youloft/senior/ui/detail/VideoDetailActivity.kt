package com.youloft.senior.ui.detail

import android.content.Intent
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.youloft.core.base.BaseActivity
import com.youloft.core.base.BaseFragment
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.share.ShareImage
import com.youloft.socialize.share.ShareWeb
import com.youloft.socialize.share.UmengShareActionImpl
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @Description:     影集详情
 * @Author:         slh
 * @CreateDate:     2020/6/18 15:19
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/18 15:19
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class VideoDetailActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_video_detail

    override fun initView() {
        btn_share_com.setOnClickListener {
            Toast.makeText(this@VideoDetailActivity, "点击", Toast.LENGTH_SHORT).show()
            UmengShareActionImpl(this@VideoDetailActivity).platform(SOC_MEDIA.WEIXIN_CIRCLE).web(
                ShareWeb("www.baidu.com").setThumb(ShareImage(this@VideoDetailActivity, ""))
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
            var fragments = ArrayList<Fragment>()
            fragments.add(ItemCommentFragment.newInstance())
            fragments.add(FavoriteFragment.newInstance())

            tablayout.setViewPager(viewpager, arrayOf("全部评论", "点赞"), this, fragments)
//            tablayout.getTitleView()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initData() {
    }
}

