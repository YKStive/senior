package com.youloft.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * WebFragment用于多窗口实现
 *
 * @author coder
 */
public class WebComponent extends FrameLayout {

    /**
     * 触碰回调监听
     */
    protected OnTouchListener touchListener;

    public static final int MAX_WEBVIEW = 7;

    public WebComponent(@NonNull Context context) {
        this(context, null);
    }

    private WebViewInterceptor mWebInterceptor;

    public WebComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mWebInterceptor = new WebViewInterceptor(this) {
            @Override
            public String getUserId() {
                return null;
            }

            @Override
            public String getPlaceHolderValue(String key) {
                return null;
            }

            @Override
            public Activity getActivity() {
                return null;
            }

            @Override
            public void onShareEvent(String name, boolean isSuccess) {

            }

            @Override
            public String replacePlaceHolder(String url) {
                return url;
            }

            @Override
            public void handleWebDownload(Context context, String iconUrl, String title, String downloadUrl) {

            }
        };
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.touchListener = l;
        if (getChildCount() > 0) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                CommonWebView webView = (CommonWebView) getChildAt(i);
                if (webView != null) {
                    webView.setOnTouchListener(touchListener);
                }
            }
        }
    }

    /**
     * 设置网页拦截器
     *
     * @param interceptor
     */
    public void setWebInterceptor(WebViewInterceptor interceptor) {
        this.mWebInterceptor = interceptor;
    }

    /**
     * 设置固定Title
     *
     * @param title   Title
     * @param isFixed 是否固定为此Title
     */
    public void setTitlePlaceHolder(String title, boolean isFixed) {
        CommonWebView webView = getWebView();
        webView.setTitlePlaceHolder(title, isFixed);
    }

    /**
     * 加载url
     *
     * @param url
     */
    public void loadUrl(String url) {
        getWebView().loadUrl(url);
    }

    /**
     * 加载数据
     *
     * @param data
     * @param mimeType
     * @param encoding
     */
    public void loadData(String data, String mimeType, String encoding) {
        getWebView().loadData(data, mimeType, encoding);
    }

    /**
     * 加载数据
     *
     * @param baseUrl
     * @param data
     * @param mimeType
     * @param encoding
     * @param historyUrl
     */
    public void loadDataWithBaseURL(String baseUrl, String data,
                                    String mimeType, String encoding, String historyUrl) {
        getWebView().loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }
    /**
     * 强制加载
     *
     * @param url
     * @param forceUseTab
     */
    public void forceLoadUrl(String url, boolean forceUseTab) {
        if (!forceUseTab) {
            loadUrl(url);
            return;
        }
        final CommonWebView webView = newWebView(forceUseTab);
        webView.loadUrl(url);
        installWebView(webView);
    }

    /**
     * 打开网页
     *
     * @param url       url
     * @param useNewTab 是否使用新窗口
     */
    public void loadUrl(String url, boolean useNewTab) {
        if (!useNewTab) {
            loadUrl(url);
            return;
        }
        final CommonWebView webView = newWebView();
        webView.loadUrl(url);
        installWebView(webView);
    }

    /**
     * 获取WebView
     * <p>
     * 如果没有则安装一个
     *
     * @return
     */
    protected CommonWebView getWebView() {
        final int tabCount = getChildCount();
        CommonWebView curWebView = tabCount < 1 ? null : (CommonWebView) getChildAt(tabCount - 1);
        if (curWebView == null) {
            curWebView = installWebView();
        }
        return curWebView;
    }

    /**
     * 获取当前顶层的Webview没有即返回空
     *
     * @return
     */
    @Nullable
    public CommonWebView peekWebView() {
        return (CommonWebView) getChildAt(getChildCount() - 1);
    }


    /**
     * 安装WebView
     *
     * @param webView
     */
    void installWebView(CommonWebView webView) {
        CommonWebView preWebView = peekWebView();
        if (preWebView != webView) {
            if (preWebView != null) {
                preWebView.onPause();
                preWebView.setTag(R.id.WEB_TAG, null);
                preWebView.setVisibility(View.GONE);
            }
            webView.setWebViewInterceptor(mWebInterceptor);
            webView.setTag(R.id.WEB_TAG, sActiveObject);
            if (webView.getParent() == null) {
                onPause();
                addView(webView);
                if (getChildCount() == MAX_WEBVIEW) {
                    webView.getSettings().setSupportMultipleWindows(false);
                } else {
                    webView.getSettings().setSupportMultipleWindows(!mDisableTabFeature);
                }
            }
        }
    }


    private boolean mDisableTabFeature = false;

    /**
     * 屏幕Disable
     *
     * @param enabled true 开启
     *                false 关闭
     * @return
     */
    public void setTabEnabled(boolean enabled) {
        mDisableTabFeature = !enabled;
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.getSettings().setSupportMultipleWindows(enabled);
        }
    }

    /**
     * 新建一个WebView
     *
     * @return
     */
    protected CommonWebView newWebView(boolean forced) {
        if (getChildCount() > MAX_WEBVIEW || (!forced && mDisableTabFeature && getChildCount() > 0)) {
            return (CommonWebView) getChildAt(getChildCount() - 1);
        }
        CommonWebView webView = new CommonWebView(getContext());
        if (touchListener != null) {
            webView.setOnTouchListener(touchListener);
        }
        return webView;
    }

    /**
     * 新建一个WebView
     *
     * @return
     */
    protected CommonWebView newWebView() {
        return newWebView(false);
    }

    /**
     * 安装WebView
     *
     * @return
     */
    private CommonWebView installWebView() {
        CommonWebView webView = newWebView();
        installWebView(webView);
        return webView;
    }

    static final Object sActiveObject = new Object();

    /**
     * 弹出历史WebView
     */
    public boolean popWebView(WebView webView) {
        if (getChildCount() == 1) {
            return false;
        }
        if (webView == null) {
            webView = peekWebView();
        }
        freeWebView(webView);
        onResume();
        CommonWebView topView = peekWebView();
        if (topView != null) {
            topView.setWebViewInterceptor(mWebInterceptor);
            topView.setTag(R.id.WEB_TAG, sActiveObject);
            topView.onResume();
        }
        if (mWebInterceptor != null && topView != null) {
            this.mWebInterceptor.refreshPageState(topView);
            this.mWebInterceptor.onPageCommitVisible(topView, topView.getUrl());
        }
        return true;
    }

    /**
     * 释放WebView
     *
     * @param webView
     */
    private void freeWebView(WebView webView) {
        removeView(webView);
        View viewWithTag = webView.getRootView().findViewWithTag(webView);
        if (viewWithTag instanceof ReportViewGroup) {
            ((ReportViewGroup) viewWithTag).detachFromParent();
        }
        webView.destroy();
    }


    /**
     * 是否有历史记录
     */
    public boolean canGoBack() {
        CommonWebView webView = peekWebView();
        if (webView == null) {
            return getChildCount() > 1;
        }
        return getChildCount() > 1 || webView.canGoBackOrForward(-1);
    }

    /**
     * 是否可以前进
     *
     * @return
     */
    public boolean canGoForward() {
        CommonWebView webView = peekWebView();
        if (webView == null) {
            return false;
        }
        return webView.canGoForward();
    }

    /**
     * 前进
     */
    public void goForward() {
        CommonWebView webView = peekWebView();
        if (webView != null) {
            webView.goForward();
        }
    }


    /**
     * 恢复
     */
    public void onResume() {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.onResume();
            commonWebView.setVisibility(VISIBLE);
        }
    }

    /**
     * 暂停
     */
    public void onPause() {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.onPause();
        }
    }

    /**
     * 清除web历史
     */
    public void clearHistory() {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.clearHistory();
        }
    }

    /**
     * 回收
     */
    public void onDestory() {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.destroy();
        }
    }

    /**
     * <p>
     * 调用JS的Callback用于页面支持JS中的返回键拦截
     * <p>
     * 当Callback回调值为1时表示网页在处理这个返回操作
     */
    public void goBackWithJs(ValueCallback<String> callback) {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView == null) {
            callback.onJavaScriptResult(null);
            return;
        }
        commonWebView.jsBridge.executeJavaScriptNow("ylappCallback_back()", callback);
    }


    /**
     * 调用JS分享
     * <p>
     * 1表示网页有自己的分享意图
     *
     * @param callback
     */
    public void shareWithJS(ValueCallback<String> callback) {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView == null) {
            callback.onJavaScriptResult(null);
            return;
        }
        commonWebView.jsBridge.executeJavaScriptNow("appCallback_share()", callback);
    }


    private int collectState = 1;
    private int shareState = 1;

    /**
     * 设置Button初始状态
     *
     * @param showShare
     * @param showCollect
     */
    public void setActionButtonInitState(boolean showShare, boolean showCollect) {
        this.collectState = showCollect ? 1 : 0;
        this.shareState = showShare ? 1 : 0;
    }

    /**
     * 获取按钮显示状态
     *
     * @param callback json: collect 收藏
     *                 json: share 分享
     */
    public void fetchButtonState(final ValueCallback<JSONObject> callback) {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView == null) {
            callback.onJavaScriptResult(null);
            return;
        }
        commonWebView.jsBridge.executeJavaScriptNow("(function(){var ret={'collect':" + collectState + ",'share':" + shareState + "};if(window.appCallback_showCollect){ret['collect']=window.appCallback_showCollect()}if(window.appCallback_showShare){ret['share']=window.appCallback_showShare()}return ret})()", new ValueCallback<String>() {
            @Override
            public void onJavaScriptResult(String value) {
                if (TextUtils.isEmpty(value)) {
                    callback.onJavaScriptResult(null);
                    return;
                }
                callback.onJavaScriptResult(JSON.parseObject(value));
            }
        });
    }

    /**
     * 刷新Meta相关的界面改变
     */
    public void refreshWithMeta() {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.onPageCommitVisible();
        }
    }

    /**
     * 调用回到今天的功能
     */
    public void invokeTodayFunction() {
        CommonWebView commonWebView = peekWebView();
        if (commonWebView != null) {
            commonWebView.jsBridge.executeJavaScriptNow("appCallback_today()", null);
        }
    }


    /**
     * 返回历史
     * <p>
     * ＠return true:表示可以返回
     * false:表示己到尽头可关闭
     */
    public boolean goBack() {
        final CommonWebView webView = peekWebView();
        if (webView == null) {
            return false;
        }

        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        //如果只有一个则进行相应的goBack
        if (getChildCount() > 1) {
            popWebView(webView);
            return true;
        }

        return false;
    }

    /**
     * 获取当前在加载的Url
     *
     * @return
     */
    public String getUrl() {
        final CommonWebView webView = peekWebView();
        if (webView == null) {
            return "";
        }
        return webView.getUrl();
    }

    /**
     * 获取当前在加载的Url
     *
     * @return
     */
    public String getOriginalUrl() {
        final CommonWebView webView = peekWebView();
        if (webView == null) {
            return "";
        }
        return webView.getOriginalUrl();
    }


    /**
     * 获取标题
     *
     * @return
     */
    public String getTitle() {
        final CommonWebView webView = peekWebView();
        if (webView == null) {
            return "";
        }
        return webView.getTitle();
    }

    /**
     * 获取网页标题
     *
     * @return
     */
    public String getWebTitle() {
        final CommonWebView webView = peekWebView();
        if (webView == null) {
            return "";
        }
        return webView.getH5Title();
    }

    /**
     * 重新加载
     */
    public void reload() {
        CommonWebView webView = peekWebView();
        if (null != webView) {
            webView.reload();
        }
    }

    /**
     * 等待onPageFinish后执行JS
     *
     * @param script
     */
    public void executeJavaScript(String script) {
        CommonWebView webView = peekWebView();
        if (null != webView) {
            webView.getJsBridge().executeJavaScript(script);
        }
    }

    /**
     * 等待onPageFinish后执行JS
     *
     * @param script
     * @param callback
     */
    public void executeJavaScript(String script, ValueCallback<String> callback) {
        CommonWebView webView = peekWebView();
        if (null != webView) {
            webView.getJsBridge().executeJavaScript(script, callback);
        }
    }

    /**
     * 立即执行JS
     *
     * @param script
     * @param callback
     */
    public void executeJavaScriptNow(String script, ValueCallback<String> callback) {
        CommonWebView webView = peekWebView();
        if (null != webView) {
            webView.getJsBridge().executeJavaScriptNow(script, callback);
        }
    }

    /**
     * onActivityResult
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        final CommonWebView webView = peekWebView();
        if (webView == null) {
            return;
        }
        webView.getProtocolDispatcher().onActivityResult(activity, requestCode, resultCode, data);
    }

    public static boolean isActive(WebView view) {
        return view.getTag(R.id.WEB_TAG) == sActiveObject;
    }

}
