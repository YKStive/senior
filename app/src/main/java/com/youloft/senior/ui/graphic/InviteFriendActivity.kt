package com.youloft.senior.ui.graphic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.base.URL_INVITE_FRIEND
import com.youloft.senior.net.Api
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.UserManager
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.UmengSocialize
import com.youloft.socialize.share.ShareImage
import com.youloft.socialize.share.ShareWeb
import com.youloft.socialize.share.UmengShareActionImpl
import kotlinx.android.synthetic.main.activity_invite_friend.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#F23854"))
        spannableString.setSpan(
            foregroundColorSpan,
            front.length - improve.length - 2,
            spannableString.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv_invite_improve.text = spannableString

        tv_invite_wx.setOnClickListener {
            UmengShareActionImpl(this).platform(SOC_MEDIA.WEIXIN).image(
                ShareImage(App.instance(), URL_INVITE_FRIEND)
            ).perform()
        }
        tv_invite_pyq.setOnClickListener {
            UmengShareActionImpl(this).platform(SOC_MEDIA.WEIXIN_CIRCLE).image(
                ShareImage(App.instance(), URL_INVITE_FRIEND)
            ).perform()
        }

    }

    override fun initData() {
        showLoading()
        lifecycleScope.launchIOWhenCreated {
            val jsonObject = kotlin.runCatching {
                ApiHelper.api.getUserCoinInfo()
            }.getOrNull()
            withContext(Dispatchers.Main) {
                dismissLoading()
                val data = jsonObject?.getJSONObject("data")
                tv_invite_code.text = data?.getString("promotecode") ?: "null"
            }

        }
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, InviteFriendActivity::class.java)
            context.startActivity(intent)
        }
    }
}