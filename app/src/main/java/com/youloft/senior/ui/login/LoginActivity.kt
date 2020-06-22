package com.youloft.senior.ui.login

import android.widget.Toast
import androidx.lifecycle.Observer
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.Repository
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.auth.AuthListener
import com.youloft.util.ToastMaster.showShortToast
import kotlinx.android.synthetic.main.activity_login.*


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
class LoginActivity : BaseActivity() {
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
                    .checkPlatformInstall(this@LoginActivity, SOC_MEDIA.WEIXIN, false)
            ) {
                Toast.makeText(this@LoginActivity, "微信未安装", Toast.LENGTH_SHORT).show()
            }
            Socialize.getIns().auth(this@LoginActivity, SOC_MEDIA.WEIXIN, object : AuthListener {
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

    private fun toLogin() {
        Repository.getLogin(mutableMapOf()).observe(this, Observer {
            if (it.isSuccess) {
                val projectTree = it.getOrNull()
                if (projectTree != null) {
                    projectTree.data
//                    showToast("登录成功")
                } else {
//                    showToast("账号密码不匹配！")
                }
            } else {
//                showToast("账号密码不匹配！")
            }
        })
    }
}