package com.youloft.webview;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;


/**
 * JS调用桥
 */
public class JavaScriptBridge {

    private static AtomicLong sSequreToken = new AtomicLong(1);
    private CommonWebView webView;
    private HashMap<String, ValueCallback<String>> mResultQueue = new HashMap<>();

    public JavaScriptBridge(CommonWebView commonWebView) {
        this.webView = commonWebView;
    }

    private boolean hasPageLoaded = false;

    /**
     * 页面开始时
     *
     * @param webView
     * @param url
     */
    void onPageStart(final WebView webView, String url) {
        this.hasPageLoaded = false;
    }


    /**
     * WebView
     *
     * @param webView
     * @param url
     */
    void onPageLoaded(final WebView webView, String url) {
        webView.loadUrl("javascript:(function(){window._eval=function(js,_reqcode){var ret=undefined;try{ret=eval(js)}catch(e){ret='exception'}if(_reqcode){setTimeout(function(){confirm('_ret_'+_reqcode+'_'+(ret==undefined?'':JSON.stringify(ret)))},200)}}})()");
        this.hasPageLoaded = true;
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasPageLoaded) {
                    return;
                }

                while (!mPendingAction.isEmpty()) {
                    try {
                        Runnable poll = mPendingAction.poll();
                        if (poll != null) {
                            poll.run();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 300);
    }

    static String acquired() {
        return String.valueOf(sSequreToken.getAndAdd(1));
    }

    /**
     * 执行没有返回值的JavaScript
     *
     * @param script
     */
    public void executeJavaScript(String script) {
        executeJavaScript(script, null);
    }

    private ConcurrentLinkedQueue<Runnable> mPendingAction = new ConcurrentLinkedQueue<>();

    /**
     * 执行JavaScript方法并获取返回值
     *
     * @param script
     * @param callback
     */
    public void executeJavaScript(final String script, final ValueCallback<String> callback) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            safeExecuteJavaScript(script, callback);
            return;
        }
        if (!hasPageLoaded) {
            mPendingAction.add(new Runnable() {
                @Override
                public void run() {
                    executeJavaScript(script, callback);
                }
            });
            return;
        }
        executeJavaScriptNow(script, callback);
    }

    private static final HashSet<Class<?>> sNUMBER_CLASS = new HashSet<Class<?>>() {
        {
            add(boolean.class);
            add(byte.class);
            add(short.class);
            add(int.class);
            add(long.class);
            add(float.class);
            add(double.class);

            add(Boolean.class);
            add(Byte.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);

            add(BigInteger.class);
            add(BigDecimal.class);
        }
    };


    /**
     * 调用 JS 方法
     *
     * @param jsCallback js 回调
     * @param callback   执行后回调
     * @param args       参数
     */
    public void invokeJsFunction(String jsCallback, ValueCallback<String> callback, Object... args) {
        try {
            StringBuilder scriptBuilder = new StringBuilder(jsCallback).append("(");
            if (args != null && args.length > 0) {
                boolean isFirst = true;
                for (Object arg : args) {
                    if (!isFirst) {
                        scriptBuilder.append(",");
                    }
                    if (sNUMBER_CLASS.contains(arg.getClass())) {
                        scriptBuilder.append(arg);
                    } else if (arg.getClass() == String.class) {
                        scriptBuilder.append("'").append(arg).append("'");
                    } else {
                        scriptBuilder.append(JSON.toJSONString(arg));
                    }
                    isFirst = false;
                }
            }
            scriptBuilder.append(")");
            executeJavaScriptNow(scriptBuilder.toString(), callback);
        } catch (Exception e) {
            if (callback != null) {
                callback.onJavaScriptResult(null);
            }
        }
    }

    private void safeExecuteJavaScript(final String script, final ValueCallback<String> callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                executeJavaScript(script, callback);
            }
        });
    }

    private static final String TAG = "JavaScriptBridge";

    /**
     * 从webview进行响应
     *
     * @param reqCode
     * @param value
     */
    public void onResponseFromWebView(String reqCode, String value) {
        if ("\"exception\"".equalsIgnoreCase(value) || "exception".equalsIgnoreCase(value)) {
            value = null;
        }
        ValueCallback<String> callback = mResultQueue.remove(reqCode);
        if (callback != null) {
            callback.onJavaScriptResult(value);
        }
    }

    /**
     * 即时执行
     *
     * @param script
     * @param callback
     */
    public void executeJavaScriptNow(final String script, final ValueCallback<String> callback) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    executeJavaScriptNow(script, callback);
                }
            });
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, callback == null ? null : new android.webkit.ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if ("null".equalsIgnoreCase(value)) {
                        value = "";
                    }
                    if (callback != null) {
                        callback.onJavaScriptResult(value);
                    }
                }
            });
            return;
        }

        try {
            if (callback == null) {
                webView.loadUrl("javascript:" + script);
            } else {
                String acquired = acquired();
                mResultQueue.put(acquired, callback);
                webView.loadUrl("javascript:_eval(" + script + "'," + acquired + ")");
            }
        } catch (Throwable e) {
            if (callback != null) {
                callback.onJavaScriptResult(null);
            }
        }


    }
}
