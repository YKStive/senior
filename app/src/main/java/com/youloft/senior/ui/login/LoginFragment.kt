package com.youloft.senior.ui.login

import androidx.core.graphics.Insets.of
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseVMFragment
import com.youloft.senior.R
import com.youloft.senior.bean.LoginBean
import com.youloft.senior.bean.LoginUploadData
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.net.NetResponse
import com.youloft.senior.ui.home.HomeModel
import com.youloft.senior.utils.TAG
import com.youloft.senior.utils.UserManager
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import com.youloft.socialize.SOC_MEDIA
import com.youloft.socialize.Socialize
import com.youloft.socialize.auth.AuthListener
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.activity_login.*
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
private const val TAG = "LoginFragment"

class LoginFragment : BaseVMFragment() {
    lateinit var id: String
    lateinit var mViewModel: HomeModel

    //    private val mViewModel by viewModels<HomeModel>()
    lateinit var name: String
    lateinit var description: String
    override fun getLayoutResId(): Int =
        R.layout.activity_login

    override fun initView() {
        tv_login.setOnClickListener {
            var userPhone = edt_phone.text.toString()
            if (userPhone.isEmpty() || userPhone.length < 11) {
                ToastMaster.showShortToast(activity, "请输入手机号")
                return@setOnClickListener
            }
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
                    for ((key, value) in weichatData.entries) {
                        "${key}  =  ${value}".logE(TAG)
                    }
                    lifecycleScope.launchIOWhenCreated({
                        it.message?.logD()
                    }, {

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
                                    mViewModel.isLogin.value = true
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

    override fun initData() {
//        ViewModelProvider.of(activity).get(HomeModel::class.java)
        activity?.let { mViewModel = ViewModelProvider(it).get(HomeModel::class.java) }
    }

    override fun startObserve() {

    }

}