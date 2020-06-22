package com.youloft.senior.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youloft.senior.BuildConfig;
import com.youloft.senior.R;
import com.youloft.senior.base.App;
import com.youloft.senior.coin.CoinManager;
import com.youloft.util.StatusBarUtils;
import com.youloft.webview.CommonWebView;
import com.youloft.webview.ValueCallback;
import com.youloft.webview.WebComponent;
import com.youloft.webview.WebViewInterceptor;

/**
 * 相关的UIHelper
 *
 * @author Administrator
 */
public class WebBaseUIHelper extends WebViewInterceptor {

    /**
     * 红包任务过来的，
     * 1 为广告红包
     * 2 为补量红包
     */
    protected int redPackageType = 0;
    /**
     * 进入页面时候的url
     */
    protected String enterUrl = null;

    /**
     * 设置红包类型
     *
     * @param redPackageType
     */
    public void setRedPackageType(int redPackageType) {
        this.redPackageType = redPackageType;
    }

    /**
     * 设置进入页面的url
     *
     * @param enterUrl
     */
    public void setEnterUrl(String enterUrl) {
        this.enterUrl = enterUrl;
    }

    /**
     * 今日运程回调code
     */
    private static final int CODE_JRYC = 9010;
    /**
     * 登录回调
     */
    private static final int CODE_LOGIN = 10010;

    public static final int CODE_ALARM = 10030;
    /**
     * 微信用于判断支持情况
     */
    private IWXAPI mMsgApi = null;
    /**
     * web activity
     */
    protected WebCallBack webCallBack;


    protected Handler barHandler = new Handler();
    /**
     * 打开新的页面
     */
    protected boolean openNewPage = false;
    /**
     * 是否来自tab
     */
    private boolean needTab = false;
    /**
     * 最后显示的url
     */
    protected String finalUrl = null;

    public WebBaseUIHelper(WebCallBack webCallBack, View titleView, WebComponent component) {
        super(component);
        this.webCallBack = webCallBack;
        this.mTitleBar = titleView;
        mMsgApi = WXAPIFactory.createWXAPI(webCallBack.getActivity(), null);
    }

    /**
     * 设置是否在shouldOverrideUrlLoading里面直接跳转新的页面
     *
     * @param openNewPage
     */
    public void setOpenNewPage(boolean openNewPage) {
        this.openNewPage = openNewPage;
    }

    /**
     * 来自tab
     *
     * @param needTab
     */
    public void setNeedTab(boolean needTab) {
        mComponent.setTabEnabled(needTab);
        this.needTab = needTab;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (BuildConfig.DEBUG) {
            Log.d("UIHelper", "url:" + url);
        }
        finalUrl = url;
        if (isTouch() && openNewPage && (url.startsWith("http://") || url.startsWith("https://"))) {
            WebHelper.create(getActivity())
                    .showWeb(url, "", true, false)
                    .fixTitle(false)
                    .show();
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 是否触碰过
     *
     * @return
     */
    protected boolean isTouch() {
        return true;
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
    }

    @Override
    public void finishActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        handleCollect(url);
        refreshPageState(view);
        webCallBack.onPageFinish();
    }

    @Override
    public void onPermissionRequestFromHandle(final com.youloft.webview.PermissionRequest request) {

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    /**
     * 进度改变
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public String getAppUA() {
        return "";
    }

    @Override
    public String getUserId() {
        //todo 待处理  获取用户id
        return "";
    }

    @Override
    public String getPlaceHolderValue(String key) {
        //todo 待处理  替换参数
        return "";
    }

    @Override
    public Activity getActivity() {
        return webCallBack.getActivity();
    }

    @Override
    public void onShareEvent(String platform, boolean isSuccess) {

    }

    @Override
    public String replacePlaceHolder(String url) {
        //todo 待处理  替换参数
        return url;
    }

    @Override
    public void handleWebDownload(Context context, String iconUrl, String title, final String downloadUrl) {
        //判断类型，但是没有传下来

    }

    @Override
    public void saveImageToPhotoLibrary(final Context context, final String url, boolean needMenu) {

    }

    /**
     * 处理是否收藏过
     *
     * @param url
     */
    public void handleCollect(String url) {
        if (mActionCollectView == null) {
            return;
        }
        url = replaceCollectUrl(url);
    }

    /**
     * 点击收藏
     */
    public void onClickCollect() {
    }

    /**
     * 替换收藏url
     */
    private String replaceCollectUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return url;
    }

    /**
     * 初始化TitleBar
     */
    public void initTitleBar() {
        mNotchCompatView = webCallBack.getActivity().findViewById(R.id.notch_compat_layer);
        mCustomViewLayer = webCallBack.getActivity().findViewById(R.id.web_custom_layer);
        if (mTitleBar == null) {
            return;
        }
        mActionTitleView = mTitleBar.findViewById(R.id.actionbar_title);
        mActionBackView = mTitleBar.findViewById(R.id.actionbar_back);
        if (mActionBackView != null) {
            mActionBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onWebBack();
                }
            });
        }
        mActionCloseView = mTitleBar.findViewById(R.id.actionbar_close);
        if (mActionCloseView != null) {
            mActionCloseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webCallBack.getActivity().finish();
                }
            });
        }

        mActionTodayView = mTitleBar.findViewById(R.id.today);
        if (mActionTodayView != null) {
            mActionTodayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mComponent.invokeTodayFunction();
                }
            });
        }

        mActionCollectView = null;//mTitleBar.findViewById(R.id.collect);
        if (mActionCollectView != null) {
            mActionCollectView.setVisibility(View.GONE);
            mActionCollectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCollect();
                }
            });
        }
        mActionShareView = mTitleBar.findViewById(R.id.item_image);
        if (mActionShareView != null) {
            mActionShareView.setVisibility(View.GONE);
            mActionShareView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mComponent.shareWithJS(new ValueCallback<String>() {
                        @Override
                        public void onJavaScriptResult(String value) {
                            if (!"1".equalsIgnoreCase(value)) {
                                webCallBack.openShare();
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 网页返回
     */
    public void onWebBack() {
        mComponent.goBackWithJs(new ValueCallback<String>() {
            @Override
            public void onJavaScriptResult(String value) {
                if (!"1".equalsIgnoreCase(value)) {
                    if (isCutomViewShown()) {
                        onHideCustomView();
                        return;
                    }
                    if (mComponent != null && mComponent.goBack()) {
                        return;
                    }
                    finishActivity();
                }
            }
        });
    }

    /**
     * 是否有分享
     *
     * @return
     */
    public boolean hasShare() {
        return mActionShareView.isShown();
    }

    /**
     * activity返回回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_LOGIN:
                mComponent.reload();
                return true;
            case CODE_JRYC:
                mComponent.executeJavaScriptNow("ycinfoCallback()", null);
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * activity销毁
     */
    public void onDestroy() {
        if (mMsgApi != null) {
            mMsgApi.unregisterApp();
        }
    }

    @Override
    public Object handleAppCommand(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        //公共处理没有处理掉的都放入此处处理
        try {
            return handleAppCommandReal(webView, command, args, cmdObj);
        } catch (Throwable e) {
            return super.handleAppCommand(webView, command, args, cmdObj);
        }
    }

    private Object handleAppCommandReal(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        switch (command) {
            case "daysign2019":
                return null;
            case "getlocalsdksupport":
                handleSupport(webView, args.getString("0"));
                break;
            case "jumptoscorecenter":
                return null;
            case "taebaichuan":
                if (cmdObj == null) {
                    return null;
                }
                return null;
            case "view_goldtask":
                handleGoldTask();
                return null;
            case "GYLQSuccessPay":
                mComponent.reload();
                return null;
            case "hide_web_title":
                if (mTitleBar != null) {
                    mTitleBar.setVisibility(View.GONE);
                }
                hideTitle = true;
                String callback = args.getString("0");
                if (TextUtils.isEmpty(callback)) {
                    return null;
                }
                int height = 0;
                if (Build.VERSION.SDK_INT >= 19) {
                    height = StatusBarUtils.getStatusHeight(getActivity());
                }
                webView.getJsBridge().executeJavaScriptNow(String.format(callback + "(%s)", height), null);
                return null;
            case "show_web_title":
                hideTitle = false;
                if (mTitleBar != null) {
                    mTitleBar.setVisibility(View.VISIBLE);
                }
                return null;
            default:
                break;
        }
        return super.handleAppCommand(webView, command, args, cmdObj);
    }

    /**
     * 设置初始化title是否显示
     *
     * @param hideTitle
     */
    public void setHideTitle(boolean hideTitle) {
        this.hideTitle = hideTitle;
        if (mTitleBar != null) {
            mTitleBar.setVisibility(hideTitle ? View.GONE : View.VISIBLE);
        }
    }

    private boolean hideTitle = false;

    @Override
    public void handleFullScreen(String value) {
        if (hideTitle) {
            return;
        }
        super.handleFullScreen(value);
    }


    /**
     * 处理金币任务
     * ps：不知道是什么鬼
     */
    private void handleGoldTask() {
        webCallBack.getActivity().finish();
        CoinManager.Companion.getInstance().loadData();
    }

    @Override
    public void onNativeDatePicker(String dateStr, final ValueCallback<String> callback) {
    }


    /**
     * 处理登录或者支付的支持情况
     *
     * @param webView
     * @param args    回调
     */
    private void handleSupport(CommonWebView webView, String args) {
        //登录支持
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("wechat", 1);
        jsonObject.addProperty("qq", 1);
        jsonObject.addProperty("weibo", 1);
        //下面是支付支持
        jsonObject.addProperty("wechatpay", (mMsgApi.isWXAppInstalled() && mMsgApi.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.PAY_SUPPORTED_SDK_INT) ? 1 : 0);
        jsonObject.addProperty("alipay", 1);
        webView.getJsBridge().executeJavaScriptNow(String.format(args + "(\'%s\')", jsonObject.toString()), null);
    }

    @Override
    public void destroyWeb(CommonWebView webView) {
        super.destroyWeb(webView);
    }

    @Override
    public void updateWebSetting(CommonWebView webView) {
        if (webView.isInEditMode()) {
            return;
        }
        webView.setVerticalScrollBarEnabled(false);
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        super.updateWebSetting(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setNeedInitialFocus(false);
        settings.setAllowFileAccess(true);

        String dbPath = App.Companion.instance().getDir("web-cache", Context.MODE_PRIVATE).getAbsolutePath();
        // support android API 7-
        try {
            // API 7, LocalStorage/SessionStorage
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setDatabasePath(dbPath);
            // API 7， Web SQL Database, 需要重载方法（WebChromeClient）才能生效，无法只通过反射实现
        } catch (Exception e) {
        }


        try {
            // API 7， Application Storage
            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(dbPath);
            settings.setAppCacheMaxSize(100 * 1024 * 1024);
        } catch (Exception e) {
        }

        try {
            // API 5， Geolocation
            settings.setGeolocationEnabled(true);
            settings.setGeolocationDatabasePath(dbPath);
        } catch (Exception e) {
        }
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(false);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setTextZoom(100);
        settings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        WebUrlHelper.modifyUA(webView);
        if (needTab) {
            settings.setSupportMultipleWindows(false);
        }
    }

    /**
     * 是否默认同意接下来的SSL错误
     */
    private static boolean ignoreSSLError = false;

    @Override
    public boolean onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        if (ignoreSSLError) {
            handler.proceed();
            return true;
        }
        final Activity activity = getActivity();
        if (activity == null) {
            return false;
        }
        return false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }


    /**
     * 页面回显
     */
    public void onResume() {
        if (!(webCallBack.getActivity() instanceof WebActivity)) {
            return;
        }
    }

    /**
     * 页面暂停
     */
    public void onPause() {
    }

}
