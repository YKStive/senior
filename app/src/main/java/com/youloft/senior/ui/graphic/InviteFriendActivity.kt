package com.youloft.senior.ui.graphic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.FragmentActivity
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.UmengSocialize
import kotlinx.android.synthetic.main.activity_invite_friend.*

/**
 * @author you
 * @create 2020/7/1
 * @desc 好友邀请
 */
class InviteFriendActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_invite_friend
    }


    override fun initView() {
        common_title.onBack { finish() }

        tv_invite_rule.setOnClickListener {
            InviteRuleActivity.start(this)
        }

        tv_invite_code.text = App.instance().userId

        tv_copy_code.setOnClickListener {
            val clipboardManager: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("邀请码", tv_invite_code.text)
            clipboardManager.setPrimaryClip(clip)
            toast("复制成功")
        }

        val improve = "提升300%"
        val front = getString(R.string.invite_improve)
        val spannableString = SpannableString("${front}${improve}")
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#FC6C71"))
        spannableString.setSpan(
            foregroundColorSpan,
            front.length - improve.length,
            spannableString.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv_invite_improve.text = spannableString

        tv_invite_wx.setOnClickListener {
            val checkPlatformInstall =
                UmengSocialize.getIns().checkPlatformInstall(this, SOC_MEDIA.WEIXIN, true)
            if (checkPlatformInstall) {
                val action =
                    Socialize.getIns().share(this)
                        .platform(SOC_MEDIA.from(SOC_MEDIA.WEIXIN.platformName))

            }
            //todo 微信分享
        }
        tv_invite_pyq.setOnClickListener {
            //todo 朋友圈分享
        }

    }

    override fun initData() {
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, InviteFriendActivity::class.java)
            context.startActivity(intent)
        }
    }
}