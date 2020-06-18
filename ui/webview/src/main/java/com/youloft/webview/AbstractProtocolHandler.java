package com.youloft.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;

/**
 * 协议处理器
 */
public abstract class AbstractProtocolHandler {

    protected JavaScriptBridge jsBridge;

    protected CommonWebView webView;

    protected Context context;

    protected void init(CommonWebView webView) {
        this.webView = webView;
        this.jsBridge = this.webView.getJsBridge();
        this.context = webView.getContext();
    }

    public abstract Object handleCommand(JSONObject command);

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }
}
