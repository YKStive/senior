package com.youloft.senior.cash

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.alibaba.fastjson.JSONObject
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.net.ApiHelper
import com.youloft.util.ToastMaster
import kotlinx.android.synthetic.main.activity_bind_phone_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author xll
 * @date 2020/6/24 16:19
 */
internal class BindPhoneActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_bind_phone_layout
    }

    private var time: Int = 0
    private var phonePrefix: String = "+86"
    override fun initView() {
        phone_prefix.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phonePrefix = phone_prefix.text.toString()
                checkButton()
            }
        })

        phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButton()
            }
        })

        write_code_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bind_phone_button2.isEnabled = write_code_edit.text.toString().length >= 4
            }
        })

        bind_phone_button.setOnClickListener {
            val phone = phone_number.text.toString()
            if (!phone.startsWith("1") || phone.length != 11 || !TextUtils.isDigitsOnly(phone)) {
                phone_number.requestFocus()
                ToastMaster.showLongToast(this, "电话号码格式不正确")
                return@setOnClickListener
            }
            //发送验证码
            val params = JSONObject()
            params["phone"] = phone
            params["smsType"] = 7
            sendCode(params)
        }
        bind_phone_button2.setOnClickListener { verifyPhoneCode() }
        resend.setOnClickListener {
            val params = JSONObject()
            val phone = phone_number.text.toString()
            params["phone"] = phone
            params["smsType"] = 7
            sendCode(params)
        }

        ic_back.setOnClickListener { finish() }
    }

    fun checkButton() {
        val phone = phone_number.text.toString()
        if (!phone.startsWith("1") || phone.length != 11 || !TextUtils.isDigitsOnly(phone)) {
            bind_phone_button.isEnabled = false
            return
        }
        bind_phone_button.isEnabled = true
    }

    override fun initData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun sendFailed() {
        ToastMaster.showLongToast(this, "发送失败")
    }

    private fun sendCode(body: JSONObject) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    ApiHelper.api.sendPhoneCode(body)
                }.getOrNull()
            }
            if (result == null) {
                //发送失败
                writeCode(false)
                return@launch
            }
            if (result.getBooleanValue("data")) {
                writeCode(true)
                return@launch
            }
            writeCode(false)
        }
    }

    private fun verifyPhoneCode() {
        val params = JSONObject()
        val phone = phone_number.text.toString()
        params["phone"] = phone
        params["code"] = write_code_edit.text.toString()
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    ApiHelper.api.verifyPhoneCode(params)
                }.getOrNull()
            }
            if (result == null) {
                //发送失败
                verifyPhoneCodeFailed()
                return@launch
            }
            if (!result.getBooleanValue("data")) {
                val msg = result.getString("msg")
                verifyPhoneCodeFailed(msg)
                return@launch
            }
            //绑定成功
            bindSuccess()
        }
    }

    fun bindSuccess() {
        ToastMaster.showLongToast(this, "绑定成功")
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun verifyPhoneCodeFailed(message: String = "验证失败") {
        ToastMaster.showLongToast(this, if (TextUtils.isEmpty(message)) "验证失败" else message)
    }

    val handler = Handler()
    val runable = Runnable {
        time--
        if (time > 0) {
            resend_code.text = time.toString() + "秒"
        } else {
            resend.visibility = View.VISIBLE
            resend_code.visibility = View.GONE
        }
        startNext()
    }

    private fun startTime() {
        handler.removeCallbacks(runable)
        handler.postDelayed(runable, 1000)
    }

    private fun startNext() {
        handler.postDelayed(runable, 1000)
    }

    private fun stop() {
        handler.removeCallbacks(runable)
        time = -1
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    /**
     * 切换到填写验证码界面
     */
    private fun writeCode(success: Boolean) {
        write_code.visibility = View.VISIBLE
        write_phone.visibility = View.GONE
        if (success) {
            resend.visibility = View.GONE
            resend_code.visibility = View.VISIBLE
            time = 60
            resend_code.text = "${time}秒"
            startTime();
        } else {
            ToastMaster.showLongToast(this, "发送失败，请重新发送")
            resend.visibility = View.VISIBLE
            resend_code.visibility = View.GONE
        }
    }
}