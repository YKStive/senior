package com.youloft.senior.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.R
import com.youloft.core.base.BaseBottomDialog
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.bean.LoginUploadData
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.net.NetResponse
import com.youloft.senior.utils.UserManager
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.auth.AuthListener
import com.youloft.util.ToastMaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/30 19:16
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/30 19:16
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class LoginDialog(
    var activity: Activity,
    var lifecycleScop: LifecycleCoroutineScope,
    style: Int = R.style.loginDialogStyle
) : BaseBottomDialog(activity, style) {
    lateinit var view: View
    lateinit var edtPhone: EditText
    lateinit var tvLogin: TextView
    lateinit var ivClose: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserManager.instance.showLogin = true
        view = LayoutInflater.from(activity).inflate(com.youloft.senior.R.layout.popup_login, null)
        setContentView(view)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        edtPhone = view.findViewById(com.youloft.senior.R.id.edt_phone)
        tvLogin = view.findViewById(com.youloft.senior.R.id.tv_login)
        ivClose = view.findViewById(com.youloft.senior.R.id.iv_close)
        // 取消按钮
        ivClose.setOnClickListener { // 销毁弹出框
            dismiss()
        }
        // 设置按钮监听
        tvLogin.setOnClickListener {
            var userPhone = edtPhone.text.toString()
            if (userPhone.isEmpty() || userPhone.length < 11) {
                ToastMaster.showShortToast(context, "请输入手机号")
                return@setOnClickListener
            }
            dismiss()
            if (!Socialize.getIns()
                    .checkPlatformInstall(activity, SOC_MEDIA.WEIXIN, false)
            ) {
                ToastMaster.showShortToast(activity, "微信未安装")
                return@setOnClickListener
            }
            Socialize.getIns().auth(activity, SOC_MEDIA.WEIXIN, object : AuthListener {
                override fun onStart(platform: SOC_MEDIA) {
                }

                override fun onComplete(
                    platform: SOC_MEDIA,
                    action: Int,
                    weichatData: Map<String, String>
                ) {
                    lifecycleScop.launchIOWhenCreated({
                        it.message?.logD()
                    }, {
                        for ((key, value) in weichatData.entries) {
                            "${key}  =  ${value}".logE(com.youloft.senior.utils.TAG)
                        }
//            val stickers = ApiHelper.api.getStickers()
                        var loginData = LoginUploadData()
                        loginData.type = "0"
                        loginData.openid = weichatData.get("openid")
                        loginData.accessToken = weichatData.get("accessToken")
                        loginData.phone = userPhone
                        val stickers =
                            NetResponse<LoginBean>(ApiHelper.api.login(loginData).data, "", "", 200)
                        withContext(Dispatchers.Main) {
                            ApiHelper.executeResponse(stickers, {
                                if (stickers.isSuccess()) {
                                    ToastMaster.showShortToast(activity, "登录成功");
                                    UserManager.instance.login(it)
                                }
                            })
                        }
                    })
                }

                override fun onError(
                    platform: SOC_MEDIA,
                    action: Int,
                    t: Throwable
                ) {
                }

                override fun onCancel(platform: SOC_MEDIA, action: Int) {
                }
            })
        }
    }

    override fun dismiss() {
        super.dismiss()
        UserManager.instance.showLogin = false
    }
}