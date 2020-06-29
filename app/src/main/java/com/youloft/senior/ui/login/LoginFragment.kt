package com.youloft.senior.ui.login

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseActivity
import com.youloft.core.base.BaseFragment
import com.youloft.senior.R
import com.youloft.senior.Repository
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.net.NetResponse
import com.youloft.senior.utils.logD
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.auth.AuthListener
import com.youloft.util.ToastMaster.showShortToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie_detail.iv_head
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_browse_number
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_name
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @Description:     登陆
 * @Author:         slh
 * @CreateDate:     2020/6/19 16:06
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/19 16:06
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class LoginFragment : BaseFragment() {
    lateinit var id: String

    //     var platform: Int
    lateinit var gender: String
    lateinit var headIconUrl: String
    lateinit var name: String
    lateinit var description: String
    lateinit var unionid: String
    override fun getLayoutResId(): Int =
        R.layout.activity_login

    override fun initView() {
        tv_login.setOnClickListener {
            if (!Socialize.getIns()
                    .checkPlatformInstall(activity, SOC_MEDIA.WEIXIN, false)
            ) {
                Toast.makeText(activity, "微信未安装", Toast.LENGTH_SHORT).show()
            }
            Socialize.getIns().auth(activity, SOC_MEDIA.WEIXIN, object : AuthListener {
                override fun onComplete(
                    platform: SOC_MEDIA?,
                    action: Int,
                    data: MutableMap<String, String>?
                ) {
//                    Preference<String>("accessToken","" ).setValue()
//                    CApp.getInstance()
//                        .getSharedPreferences("temp_token_xx", Context.MODE_PRIVATE)
//                        .edit()
//                        .putString("accessToken", info.get("accessToken"))
//                        .putString("openid", info.get("openid"))
//                        .putString("appid", BuildConfig.WX_SHARE_ID)
//                        .putString("loginType", "wx")
//                        .commit()
                    unionid = data?.get("unionid").toString()
                    id = data?.get("openid").toString()
                    headIconUrl = data?.get("profile_image_url").toString()
                    name = data?.get("screen_name").toString()
                    description = ""
                    val sex: String = data?.get("gender").toString()
                    gender = if ("男" == sex) {
                        "0"
                    } else {
                        "1"
                    }
                }

                override fun onCancel(platform: SOC_MEDIA?, action: Int) {
                }

                override fun onError(platform: SOC_MEDIA?, action: Int, t: Throwable?) {
                }

                override fun onStart(platform: SOC_MEDIA?) {
                }
            })
        }
    }

    override fun initData() {
    }


    fun toLogin(loginParams: HashMap<String, String>) {
        lifecycleScope.launchIOWhenCreated({
            it.message?.logD()
        }, {
//            val stickers = ApiHelper.api.getStickers()
            val stickers =
                NetResponse<LoginBean>(ApiHelper.api.login(loginParams).data, "", "", 200)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(stickers, {
//                    activity?.let { it1 ->
//                        Glide.with(it1).load(it?.avatar).into(iv_head) }
//                    tv_name.setText(it.nickname)
//                    tv_browse_number.setText("${it.viewed}次浏览")
//                    tv_content_video.setText(it.textContent)
//                    tv_create_time.setText(it.createTime)
                })
            }
        })
    }
}