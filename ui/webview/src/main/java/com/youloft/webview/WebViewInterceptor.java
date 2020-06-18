package com.youloft.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.youloft.util.StatusBarUtils;
import com.youloft.util.ToastMaster;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网页事件拦截器
 */
public abstract class WebViewInterceptor {

    protected ViewGroup mCustomViewLayer;

    protected View mActionShareView;

    protected View mActionCollectView;

    protected View mActionTodayView;

    protected View mNotchCompatView;

    protected View mTitleBar;

    protected TextView mActionTitleView;

    protected View mActionBackView;

    protected View mActionCloseView;

    protected WebComponent mComponent;

    boolean isCustomViewShown = false;

    private WebChromeClient.CustomViewCallback callback;

    public WebViewInterceptor(WebComponent component) {
        this.mComponent = component;
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public abstract String getUserId();

    public abstract String getPlaceHolderValue(String key);

    /**
     * 刷新页面相关的状态
     *
     * @param view
     */
    public void refreshPageState(WebView view) {
        mComponent.fetchButtonState(new ValueCallback<JSONObject>() {
            @Override
            public void onJavaScriptResult(JSONObject value) {
                handleCollectVisible(value.getBooleanValue("collect"));
                handleShareVisible(value.getBooleanValue("share"));
            }
        });
        if (mActionCloseView != null) {
            mActionCloseView.setVisibility(mComponent.canGoBack() ? View.VISIBLE : View.INVISIBLE);
        }
        mComponent.refreshWithMeta();

        if (mActionTitleView != null) {
            mActionTitleView.setText(mComponent.getTitle());
        }

        if (view instanceof CommonWebView) {
            ConcurrentHashMap<String, Object> storeData = ((CommonWebView) view).getStoreData();
            for (Map.Entry<String, Object> entry : storeData.entrySet()) {
                try {
                    if ("pending_enableCollect".equalsIgnoreCase(entry.getKey())) {
                        handleCollectVisible((Boolean) entry.getValue());
                    } else if ("pending_enableShare".equalsIgnoreCase(entry.getKey())) {
                        handleShareVisible((Boolean) entry.getValue());
                    } else if ("pending_showToday".equalsIgnoreCase(entry.getKey())) {
                        handleTodayVisible((Boolean) entry.getValue());
                    }
                } catch (Exception e) {
                }
            }
            storeData.clear();
        }

    }

    /**
     * 接收到SSL错误
     *
     * @param view
     * @param handler
     * @param error
     */
    public boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        return false;
    }

    /**
     * 页面展现到界面上
     *
     * @param view
     * @param url
     */
    public void onPageCommitVisible(WebView view, String url) {
    }

    /**
     * 页面加载
     *
     * @param view
     * @param url
     */
    public void onPageFinished(WebView view, String url) {

    }

    /**
     * 开始加载
     *
     * @param view
     * @param url
     * @param favicon
     */
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    /**
     * Activity回调用于默认协议处理
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }

    /**
     * 是否拦截Url加载
     *
     * @param view
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    /**
     * 是否为Http协议Url
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;
        return url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://");
    }

    /**
     * 创建窗口
     *
     * @param view
     * @param isDialog
     * @param isUserGesture
     * @param resultMsg
     * @return
     */
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        final CommonWebView webView = mComponent.newWebView(true);
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(webView);
        resultMsg.sendToTarget();
        mComponent.installWebView(webView);
        return true;
    }

    /**
     * 关闭窗口
     *
     * @param window
     */
    public void onCloseWindow(WebView window) {
        mComponent.popWebView(window);
        try {
            window.loadUrl("about:blank");
            window.destroy();
        } catch (Exception e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onPermissionRequest(PermissionRequest request) {
        return false;
    }

    /**
     * 标题改变
     *
     * @param view
     * @param title
     */
    public void onReceivedTitle(WebView view, String title) {
        if (mActionTitleView != null) {
            mActionTitleView.setText(title);
        }
    }

    /**
     * 处理权限请求
     *
     * @param request
     */
    public void onPermissionRequestFromHandle(com.youloft.webview.PermissionRequest request) {
        request.grant();
    }

    /**
     * 获取调用方Activity
     *
     * @return
     */
    public abstract Activity getActivity();

    /**
     * 分享事件上报
     *
     * @param name
     * @param isSuccess
     */
    public abstract void onShareEvent(String name, boolean isSuccess);


    /**
     * 替换URL中的PlaceHolder
     *
     * @param url
     * @return
     */
    public abstract String replacePlaceHolder(String url);

    /**
     * 处理全屏
     *
     * @param value
     */
    public void handleFullScreen(String value) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        String[] flagArr = TextUtils.isEmpty(value) ? null : value.split("#");
        String screenFlag = (flagArr != null && flagArr.length > 0) ? flagArr[0] : "";
        String notchFlag = (flagArr != null && flagArr.length > 1) ? flagArr[1] : "";

        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if ("true".equalsIgnoreCase(screenFlag)) {
            if (mTitleBar != null) {
                mTitleBar.setVisibility(View.GONE);
            }
            handleScreenNotchSupport(notchFlag);
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            window.setAttributes(params);
        } else {
            if (mTitleBar != null) {
                mTitleBar.setVisibility(View.VISIBLE);
            }
            handleScreenNotchSupport("true");
            params.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            params.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            window.setAttributes(params);
        }
    }

    /**
     * 处理屏幕方向
     *
     * @param value
     */
    public void handleScreenOrientation(String value) {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        if ("landscape".equalsIgnoreCase(value)) {
            if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    /**
     * 处理异形屏幕
     *
     * @param supportNotch
     */
    public void handleScreenNotchSupport(final String supportNotch) {
        if (null == getActivity()) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("true".equalsIgnoreCase(supportNotch) || "yes".equalsIgnoreCase(supportNotch)) {
                    if (mNotchCompatView != null) {
                        mNotchCompatView.setVisibility(View.GONE);
                    }
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                            Window window = getActivity().getWindow();
                            WindowManager.LayoutParams params = window.getAttributes();
                            Field layoutInDisplayCutoutMode = params.getClass().getField("layoutInDisplayCutoutMode");
                            layoutInDisplayCutoutMode.setInt(params, 1);
                            window.setAttributes(params);
                        }
                    } catch (Throwable e) {
                    }
                } else {
                    int notchHeight = StatusBarUtils.getNotchHeight(getActivity());
                    if (mNotchCompatView != null && mNotchCompatView.getVisibility() != View.VISIBLE) {
                        ViewGroup.LayoutParams layoutParams = mNotchCompatView.getLayoutParams();
                        layoutParams.height = notchHeight;
                        mNotchCompatView.setLayoutParams(layoutParams);
                        mNotchCompatView.setVisibility(View.VISIBLE);
                    }
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                            Window window = getActivity().getWindow();
                            WindowManager.LayoutParams params = window.getAttributes();
                            Field layoutInDisplayCutoutMode = params.getClass().getField("layoutInDisplayCutoutMode");
                            layoutInDisplayCutoutMode.setInt(params, notchHeight <= 0 ? 2 : 1);
                            window.setAttributes(params);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * 响应滑动返回
     *
     * @param value
     */
    public void handlePageSwipe(String value) {

    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param url
     * @param needMenu
     */
    public void saveImageToPhotoLibrary(Context context, String url, boolean needMenu) {

    }

    /**
     * 处理网页下载
     *
     * @param context
     * @param url
     * @param userAgent
     * @param contentDisposition
     * @param mimetype
     * @param contentLength
     */
    public void handleWebDownload(Context context, String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        handleWebDownload(context, null, null, url);
    }

    /**
     * 调用应用内部的日期选择器
     *
     * @param dateStr
     * @param callback
     */
    public void onNativeDatePicker(String dateStr, ValueCallback<String> callback) {
        ToastMaster.showShortToast(getActivity(), "dateStr not support");
        callback.onJavaScriptResult("");
    }

    /**
     * 处理Meta改变
     *
     * @param key
     * @param value
     * @return
     */
    public boolean handleMetaChange(String key, String value) {
        return false;
    }

    /**
     * 隐藏自定义View
     */
    public void onHideCustomView() {
        if (mCustomViewLayer != null) {
            mComponent.refreshWithMeta();
            mCustomViewLayer.setVisibility(View.GONE);
            if (this.callback != null) {
                this.callback.onCustomViewHidden();
                this.callback = null;
            }
            mCustomViewLayer.removeAllViews();
        }
        isCustomViewShown = false;
    }

    /**
     * 显示自定义View
     *
     * @param view
     * @param requestedOrientation
     * @param callback
     * @return
     */
    public boolean onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        if (mCustomViewLayer != null) {
            if (null == view) {
                callback.onCustomViewHidden();
                return false;
            }
            this.callback = callback;
            mCustomViewLayer.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mCustomViewLayer.setVisibility(View.VISIBLE);
            handleScreenOrientation(requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? "LANDSCAPE" : "");
            handleFullScreen(requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? "true" : "");
            isCustomViewShown = true;
            return true;
        }
        return false;
    }

    /**
     * 自定义View是否被展示
     * 即视频全屏的时候
     *
     * @return
     */
    public boolean isCutomViewShown() {
        return isCustomViewShown;
    }

    /**
     * 处理页面模式为APp时态隐藏mTitleBars
     *
     * @param value
     */
    public void handleAppVisible(String value) {
        if (mTitleBar == null) {
            return;
        }
        if ("app".equalsIgnoreCase(value)) {
            mTitleBar.setVisibility(View.GONE);
        }
    }


    /**
     * 下载文件实现
     *
     * @param context
     * @param iconUrl
     * @param title
     * @param downloadUrl
     */
    public abstract void handleWebDownload(Context context, String iconUrl, String title, String downloadUrl);

    /**
     * 处理分享按钮显示
     *
     * @param show
     */
    public void handleShareVisible(final boolean show) {
        if (null != mActionShareView) {
            mActionShareView.post(new Runnable() {
                @Override
                public void run() {
                    mActionShareView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    /**
     * 处理收藏按钮是否展示
     *
     * @param show
     */
    public void handleCollectVisible(final boolean show) {
        if (null != mActionCollectView) {
            mActionCollectView.post(new Runnable() {
                @Override
                public void run() {
                    mActionCollectView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    /**
     * 是否显示回到今天
     *
     * @param show
     */
    public void handleTodayVisible(final boolean show) {
        if (null != mActionTodayView) {
            mActionTodayView.post(new Runnable() {
                @Override
                public void run() {
                    mActionTodayView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        ToastMaster.showShortToast(getActivity(), "showToday:" + show);
    }

    /**
     * 处理应用独有协议
     *
     * @param webView
     * @param command
     * @param args
     * @param cmdObj
     * @return
     */
    public Object handleAppCommand(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        return null;
    }

    /**
     * 退出Activity
     */
    public void finishActivity() {
        getActivity().finish();
    }

    /**
     * 更新WebView的Setting
     *
     * @param webView
     */
    @SuppressLint("AddJavascriptInterface")
    public void updateWebSetting(CommonWebView webView) {
        webView.addJavascriptInterface(new InternalJavaScriptObject(webView), "ylwindow");
        WebSettings settings = webView.getSettings();
        String userAgentString = settings.getUserAgentString();
        String appUa = getAppUA();
        if (!TextUtils.isEmpty(appUa) && !userAgentString.contains(appUa)) {
            settings.setUserAgentString(userAgentString + " " + appUa);
        }
    }

    /**
     * web销毁
     */
    public void destroyWeb(CommonWebView webView) {
    }

    /**
     * 获取应用内部UA
     *
     * @return
     */
    public String getAppUA() {
        return "";
    }

    /**
     * 处理进度改变
     *
     * @param view
     * @param newProgress
     */
    public void onProgressChanged(WebView view, int newProgress) {

    }

    /**
     * 处理错误
     *
     * @param view
     * @param request
     * @param error
     * @return
     */
    public boolean onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        return false;
    }

    /**
     * 处理错误
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    }

    /**
     * 支付完成
     */
    public void onPayFinish(int platform, String memo, boolean success, String finalOrderId) {

    }

    /**
     * 开始处理分享
     */
    public void startHandleShare() {

    }

    /**
     * 能否滚动
     *
     * @return
     */
    public boolean canScrollVertically(int direction) {
        return true;
    }

    /**
     * web 滚动触发
     *
     * @param scrollUp 是否向上滚动
     */
    public void onWebScroll(boolean scrollUp) {

    }

    public void onPageResume(CommonWebView commonWebView) {
        try {
            commonWebView.jsBridge.executeJavaScript("if(window.onAppVisible){window.onAppVisible();}");
        } catch (Exception e) {
        }
    }

    public void onPagePause(CommonWebView commonWebView) {
        try {
            commonWebView.jsBridge.executeJavaScript("if(window.onAppDisapear){window.onAppDisapear();}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
