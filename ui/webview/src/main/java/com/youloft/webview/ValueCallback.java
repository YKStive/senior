package com.youloft.webview;

public interface ValueCallback<T> {

    /**
     * JS结果响应
     * @param value
     */
    void onJavaScriptResult(T value);

}
