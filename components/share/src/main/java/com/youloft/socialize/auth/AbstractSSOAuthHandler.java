package com.youloft.socialize.auth;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.youloft.socialize.SOC_MEDIA;
import com.youloft.socialize.Socialize;
import com.youloft.webview.CommonWebView;
import com.youloft.webview.protocol.handler.AbstractCommandHandler;

import java.util.Map;

/**
 * 二次验证主要用于获取三方平台信息后进行自家服务器请求
 *
 * @author coder
 */
public abstract class AbstractSSOAuthHandler extends AbstractCommandHandler implements AuthListener {

    static final String CMD_AUTH_QQ = "snsoauth.QQ";
    static final String CMD_AUTH_WECHAT = "snsoauth.Wechat";
    static final String CMD_AUTH_WEIBO = "snsoauth.Weibo";

    public AbstractSSOAuthHandler(){
        registeCommand(CMD_AUTH_QQ);
        registeCommand(CMD_AUTH_WECHAT);
        registeCommand(CMD_AUTH_WEIBO);
    }


    protected Activity activity;

    public AbstractSSOAuthHandler(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onStart(SOC_MEDIA platform) {

    }

    @Override
    public void onComplete(SOC_MEDIA platform, int action, Map<String, String> data) {
        onThirdPlatformSuccess(platform,data);
    }

    @Override
    public void onError(SOC_MEDIA platform, int action, Throwable t) {
        onLoginFailed(platform);
    }

    @Override
    public void onCancel(SOC_MEDIA platform, int action) {
        onLoginFailed(platform);
    }

    /**
     * 登陆失败
     * @param platform
     */
    protected void onLoginFailed(SOC_MEDIA platform) {

    }

    /**
     * 请求应用登陆服务器
     * @param platform
     * @param data
     */
    protected abstract void onThirdPlatformSuccess(SOC_MEDIA platform, Map<String, String> data);

    @Override
    public Object handleCommand(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        SOC_MEDIA soc_media = null;
        if(CMD_AUTH_QQ.equalsIgnoreCase(command)){
            soc_media = SOC_MEDIA.QQ;
        }else if(CMD_AUTH_WECHAT.equalsIgnoreCase(command)){
            soc_media = SOC_MEDIA.WEIXIN;
        }else if(CMD_AUTH_WEIBO.equalsIgnoreCase(command)){
            soc_media = SOC_MEDIA.SINA;
        }
        activity = webView.getWebViewInterceptor().getActivity();
        if(soc_media!=null && null!=activity ){
            Socialize.getIns().auth(activity,soc_media,this);
        }
        return null;
    }
}
