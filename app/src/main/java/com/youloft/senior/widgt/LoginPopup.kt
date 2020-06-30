package com.youloft.senior.widgt

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.senior.R
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.bean.LoginUploadData
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.net.NetResponse
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.logD
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.auth.AuthListener
import com.youloft.util.ToastMaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Description: 快捷登录弹出
 * @Author: slh
 * @CreateDate: 2020/6/29 15:12
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/6/29 15:12
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
class LoginPopup(
    mContext: Activity?,
    lifecycleScop: LifecycleCoroutineScope
) : PopupWindow() {
    private val mContext: Activity? = null
    private val view: View
    private val edtPhone: EditText
    private val tvLogin: TextView
    private val ivClose: ImageView

    companion object {
        private const val TAG = "LoginPopup"
    }

    init {
        view =
            LayoutInflater.from(mContext).inflate(R.layout.popup_login, null)
        edtPhone = view.findViewById(R.id.edt_phone)
        tvLogin = view.findViewById(R.id.tv_login)
        ivClose = view.findViewById(R.id.iv_close)
        // 取消按钮
        ivClose.setOnClickListener { // 销毁弹出框
            dismiss()
        }
        // 设置按钮监听
        tvLogin.setOnClickListener {
            var userPhone = edtPhone.text.toString()
            if (userPhone.isEmpty() || userPhone.length < 11) {
                ToastMaster.showShortToast(mContext, "请输入手机号")
                return@setOnClickListener
            }
            if (!Socialize.getIns()
                    .checkPlatformInstall(mContext, SOC_MEDIA.WEIXIN, false)
            ) {
                ToastMaster.showShortToast(mContext, "微信未安装")
            }
            Socialize.getIns().auth(mContext, SOC_MEDIA.WEIXIN, object : AuthListener {
                override fun onStart(platform: SOC_MEDIA) {
                    Log.e(TAG, "微信登录onStart")
                }

                override fun onComplete(
                    platform: SOC_MEDIA,
                    action: Int,
                    weichatData: Map<String, String>
                ) {
                    lifecycleScop.launchIOWhenCreated({
                        it.message?.logD()
                    }, {

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
                                    ToastMaster.showShortToast(mContext, "登录成功");
                                    var userId: String by Preference(Preference.USER_ID, it.userId)
                                    var accessToken: String by Preference(
                                        Preference.ACCESSTOKEN,
                                        it.accessToken
                                    )
                                    var refreshToken: String by Preference(
                                        Preference.REFRESHTOKEN,
                                        it.refreshToken
                                    )
                                    var expiration: String by Preference(
                                        Preference.EXPIRATION,
                                        it.expiration
                                    )
                                    var userPhone: String by Preference(
                                        Preference.USER_PHONE,
                                        userPhone
                                    )
                                    var isLogin: Boolean by Preference(
                                        Preference.IS_LOGIN,
                                        true
                                    )
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
                    Log.e(TAG, "onError" + t.message)
                }

                override fun onCancel(platform: SOC_MEDIA, action: Int) {
                    Log.e(TAG, "onCancel")
                } //                    Preference<String>("accessToken","" ).setValue()
                //                    CApp.getInstance()
                //                        .getSharedPreferences("temp_token_xx", Context.MODE_PRIVATE)
                //                        .edit()
                //                        .putString("accessToken", info.get("accessToken"))
                //                        .putString("openid", info.get("openid"))
                //                        .putString("appid", BuildConfig.WX_SHARE_ID)
                //                        .putString("loginType", "wx")
                //                        .commit()
            })
        }

        // 设置外部可点击
        this.isOutsideTouchable = true
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener { v, event ->
            val height =
                view.findViewById<View>(R.id.pop_layout)
                    .top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            v.performClick()
            true
        }


        /* 设置弹出窗口特征 */
        // 设置视图
        this.contentView = view
        // 设置弹出窗体的宽和高
        this.height = RelativeLayout.LayoutParams.MATCH_PARENT
        this.width = RelativeLayout.LayoutParams.MATCH_PARENT

        // 设置弹出窗体可点击
        this.isFocusable = true

        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(-0x50000000)
        // 设置弹出窗体的背景
        setBackgroundDrawable(dw)

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.animationStyle = R.style.take_photo_anim
    }
}