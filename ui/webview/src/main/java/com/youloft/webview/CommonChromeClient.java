package com.youloft.webview;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;


/**
 * 基类
 */
public class CommonChromeClient extends WebChromeClient {


    private ProtocolDispatcher mDispatcher;

    private JavaScriptBridge mJSBridge;

    private WebViewInterceptor mInterceptor;


    public CommonChromeClient(CommonWebView webView) {
        updateVariable(webView);
    }

    /**
     * 设置Interceptor
     *
     * @param webView
     */
    public void updateVariable(CommonWebView webView) {
        this.mDispatcher = webView.getProtocolDispatcher();
        this.mJSBridge = webView.getJsBridge();
        this.mInterceptor = webView.webViewInterceptor;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        this.onShowCustomView(view, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (this.mInterceptor.onShowCustomView(view, requestedOrientation, callback)) {
            return;
        }
        super.onShowCustomView(view, requestedOrientation, callback);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        this.mInterceptor.onHideCustomView();
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (message.startsWith("__invoke__")) {
            try {
                result.confirm(JSON.toJSONString(this.mDispatcher.dispatchCommand(JSON.parseObject(message.substring(10)))));
            } catch (Exception e) {
                result.cancel();
            }
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequest(PermissionRequest request) {
        if (this.mInterceptor != null && this.mInterceptor.onPermissionRequest(request)) {
            return;
        }
        super.onPermissionRequest(request);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (this.mInterceptor != null) {
            this.mInterceptor.onReceivedTitle(view, view.getTitle());
            return;
        }
        super.onReceivedTitle(view, title);
    }

    private static final String TAG = "CommonChromeClient";

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        try {
            if (message.startsWith("_ret_")) {
                if (this.mJSBridge != null) {
                    message = message.substring(5);
                    int splitIndex = message.indexOf("_");
                    String reqCode = message.substring(0, splitIndex);
                    String value = message.substring(splitIndex + 1);
                    this.mJSBridge.onResponseFromWebView(reqCode, value);
                }
                result.confirm();
                return true;
            } else if (message.startsWith("protocol:")) {
                if (this.mDispatcher != null && this.mDispatcher.dispatchUrlLoading(view, message)) {
                    result.confirm();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("WEB-TAG", message, e);
            result.cancel();
            return true;
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public void onCloseWindow(WebView window) {
        if (this.mInterceptor != null) {
            this.mInterceptor.onCloseWindow(window);
        }
    }


    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if (this.mInterceptor != null) {
            return this.mInterceptor.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (!WebComponent.isActive(view)) {
            return;
        }
        if (this.mInterceptor != null) {
            this.mInterceptor.onProgressChanged(view, newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }
}
