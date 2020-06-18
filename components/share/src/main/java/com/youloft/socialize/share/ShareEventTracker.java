package com.youloft.socialize.share;

import com.youloft.webview.WebViewInterceptor; /**
 * 分享事件监听器
 */
public class ShareEventTracker {

    protected WebViewInterceptor mInterceptor;

    public ShareEventTracker(){}


    public ShareEventTracker(WebViewInterceptor webViewInterceptor) {
        this.mInterceptor = webViewInterceptor;
    }

    public void onStart(String name) {
    }

    public void onResult(String name) {
        onSuccess(name);
    }

    public void onError(String name, Throwable throwable) {
        onFailed(name);
    }

    public void onCancel(String name) {
        onFailed(name);
    }

    public void onSuccess(String name){

    }

    public void onFailed(String name){

    }

}
