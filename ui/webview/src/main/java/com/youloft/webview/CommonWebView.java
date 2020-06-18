package com.youloft.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用WebView
 */
public class CommonWebView extends WebView {


    final static String META_FULLSCREEN = "full-screen";
    final static String META_ORIENTATION = "x5-orientation";
    final static String META_ORIENTATION_G = "screen-orientation";
    final static String META_PAGEMODE = "x5-page-mode";
    final static String META_NOTCH = "wnl-notch-support";
    final static String META_SWIPE = "wnl-back-swipe";
    final static String META_CLOSE = "wnl-back-close";
    /**
     * 移动判断值
     */
    private int scaledTouchSlop = 0;

    /**
     * JS桥
     */
    protected JavaScriptBridge jsBridge;

    /**
     * 协议转发
     */
    protected ProtocolDispatcher protocolDispatcher;

    /**
     * 方法拦截
     */
    protected WebViewInterceptor webViewInterceptor;


    public WebViewInterceptor getWebViewInterceptor() {
        return webViewInterceptor;
    }

    /**
     * 设置拦截器
     *
     * @param interceptor
     */
    public void setWebViewInterceptor(WebViewInterceptor interceptor) {
        this.webViewInterceptor = interceptor;
        mChromeClient.updateVariable(this);
        mWebViewClient.updateVariable(this);
        if (this.webViewInterceptor != null) {
            this.webViewInterceptor.updateWebSetting(this);
        }
    }

    /**
     * 是否已经滑动到页面顶部
     */
    public boolean hasArrivedTop = false;


    public CommonWebView(Context context) {
        super(context);
        initInternal(context);
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInternal(context);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private CommonChromeClient mChromeClient;

    private CommonWebViewClient mWebViewClient;

    private String fixTitle;

    private boolean isTitleFixed = false;

    /**
     * 设置固定标题
     *
     * @param title
     * @param isFixed
     */
    public void setTitlePlaceHolder(String title, boolean isFixed) {
        this.fixTitle = title;
        this.isTitleFixed = isFixed;
        getWebViewInterceptor().onReceivedTitle(this, fixTitle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (webViewInterceptor != null && !webViewInterceptor.canScrollVertically(direction)) {
            return false;
        }
        boolean canScroll = super.canScrollVertically(1) || super.canScrollVertically(-1);
        if (canScroll) {
            return super.canScrollVertically(direction);
        }
        if (direction == -1) {
            //向下滑动
            return !hasArrivedTop;
        } else if (direction == 1) {
            return true;
        } else {
            return super.canScrollVertically(direction);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int dy = t - oldt;
        if (webViewInterceptor == null) {
            return;
        }
        if (Math.abs(dy) >= scaledTouchSlop) {
            if (dy > 0) {
                webViewInterceptor.onWebScroll(false);
            } else if (dy < 0) {
                webViewInterceptor.onWebScroll(true);
            }
        }
    }


    /**
     * 获取网页标题
     * @return
     */
    public String getH5Title(){
        return super.getTitle();
    }

    /**
     * 内部获取标题
     * @return
     */
    @Override
    public String getTitle() {
        if (isTitleFixed && !TextUtils.isEmpty(fixTitle)) {
            return fixTitle;
        }
        String title = super.getTitle();
        if (!TextUtils.isEmpty(title) && title.startsWith("http")) {
            return fixTitle;
        }
        return title;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (webViewInterceptor != null) {
            webViewInterceptor.destroyWeb(this);
        }
    }

    /**
     * 内部初始化
     *
     * @param context
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void initInternal(Context context) {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        jsBridge = new JavaScriptBridge(this);
        protocolDispatcher = new ProtocolDispatcher(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
        mChromeClient = new CommonChromeClient(this);
        mWebViewClient = new CommonWebViewClient(this);
        super.setWebChromeClient(mChromeClient);
        super.setWebViewClient(mWebViewClient);
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                webViewInterceptor.handleWebDownload(getContext(), url, userAgent, contentDisposition, mimetype, contentLength);
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                HitTestResult result = getHitTestResult();
                final String url = result.getExtra();
                if (!TextUtils.isEmpty(url)) {
                    final int type = result.getType();
                    final int dotIndex = url.lastIndexOf(".");
                    if (dotIndex > -1) {
                        String prefix = url.substring(dotIndex);
                        if (type == 5
                                || ".png".equalsIgnoreCase(prefix)
                                || ".jpeg".equalsIgnoreCase(prefix)
                                || ".jpg".equalsIgnoreCase(prefix)) {
                            webViewInterceptor.saveImageToPhotoLibrary(getContext(), url, true);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        //hook webviewclient method
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        //hook chromeclient method
    }

    /**
     * 获取协议处理分发器
     *
     * @return
     */
    public ProtocolDispatcher getProtocolDispatcher() {
        return protocolDispatcher;
    }

    /**
     * 获取Javascript桥
     *
     * @return
     */
    public JavaScriptBridge getJsBridge() {
        return jsBridge;
    }


    /**
     * 获取Meta标签
     *
     * @param valueCallback
     */
    public void getMetas(final ValueCallback<JSONObject> valueCallback) {
        jsBridge.executeJavaScript("(function () {\n" +
                "    var metaList = {};\n" +
                "    var metas = document.getElementsByTagName('meta');\n" +
                "    metaList['full-screen'] = false;\n" +
                "    metaList['x5-orientation'] = '';\n" +
                "    metaList['wnl-notch-support'] = '';\n" +
                "    if (metas) {\n" +
                "        for (var index = 0; index < metas.length; index++) {\n" +
                "            var meta = metas[index];\n" +
                "            if (meta.name) {\n" +
                "                metaList[meta.name] = meta.content;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    return metaList;\n" +
                "})()", new ValueCallback<String>() {
            @Override
            public void onJavaScriptResult(String value) {
                try {
                    valueCallback.onJavaScriptResult(JSONObject.parseObject(value));
                } catch (Exception e) {
                    valueCallback.onJavaScriptResult(null);
                }
            }
        });
    }

    /**
     * 页面即将被展示
     */
    void onPageCommitVisible() {
        getMetas(new ValueCallback<JSONObject>() {
            @Override
            public void onJavaScriptResult(JSONObject value) {
                if (value.containsKey(META_ORIENTATION) && value.containsKey(META_ORIENTATION_G)) {
                    value.remove(META_ORIENTATION);
                }
                if (value.containsKey(META_FULLSCREEN)) {
                    if ("yes".equalsIgnoreCase(value.getString(META_FULLSCREEN))) {
                        value.put(META_FULLSCREEN, "true");
                    }
                    boolean hasNotchSupport = value.containsKey(META_NOTCH) && "true".equalsIgnoreCase(value.getString(META_NOTCH));
                    if(!hasNotchSupport){
                        value.put(META_NOTCH,"false");
                    }
                    value.put(META_FULLSCREEN,value.getString(META_FULLSCREEN)+"#"+value.getString("META_NOTCH"));
                }
                value.remove(META_NOTCH);
                for (String key : value.keySet()) {
                    handleMetaChange(key, value.getString(key));
                }
            }
        });
    }

    /**
     * 开启全屏
     *
     * @param fullScreen
     */
    public void enableFullScreen(boolean fullScreen) {
        String screenFlag = String.valueOf(fullScreen).toLowerCase();
        jsBridge.executeJavaScriptNow("(function(){var fs=document.getElementsByName('full-screen');if(fs&&fs.length>0){fs[0].content='"+screenFlag+"'}else{var meta=document.createElement('meta');meta.name='full-screen';meta.setAttribute('content','"+screenFlag+"');document.getElementsByTagName('head')[0].appendChild(meta)}})()", null);
        onPageCommitVisible();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.webViewInterceptor == null) return;
        this.webViewInterceptor.onPageResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.webViewInterceptor == null) return;
        this.webViewInterceptor.onPagePause(this);
    }

    /**
     * 处理Meta改变
     *
     * @param key
     * @param value
     */
    private void handleMetaChange(String key, String value) {
        if (this.webViewInterceptor.handleMetaChange(key, value)) {
            return;
        }
        if (META_FULLSCREEN.equalsIgnoreCase(key)) {
            this.webViewInterceptor.handleFullScreen(value);
        } else if (META_ORIENTATION.equalsIgnoreCase(key) || META_ORIENTATION_G.equalsIgnoreCase(key)) {
            this.webViewInterceptor.handleScreenOrientation(value);
        } else if (META_PAGEMODE.equalsIgnoreCase(key)) {
            post(new Runnable() {
                @Override
                public void run() {
                    clearHistory();
                }
            });
            this.webViewInterceptor.handleAppVisible(value);
        } else if (META_SWIPE.equalsIgnoreCase(key)) {
            this.webViewInterceptor.handlePageSwipe(value);
        } else if (META_CLOSE.equalsIgnoreCase(key)) {
            this.canUseHistory = false;
        }
    }

    boolean canUseHistory = true;

    @Override
    public boolean canGoBack() {
        if (!canUseHistory) {
            return false;
        }
        return super.canGoBack();
    }

    @Override
    public boolean canGoBackOrForward(int steps) {
        if (!canUseHistory) {
            return false;
        }
        return super.canGoBackOrForward(steps);
    }

    private ConcurrentHashMap<String, Object> mStoreData = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Object> getStoreData() {
        return mStoreData;
    }
    /**
     * 设置StoreValue
     *
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void setStoreValue(String key, T value) {
        mStoreData.put(key, value);
    }

    /**
     * 获取StoreValue
     * <p>
     * 这是填补Tag的不足
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getStoreValue(String key) {
        return (T) mStoreData.get(key);
    }

    public void clearStoreValue(String key) {
        mStoreData.remove(key);
    }
}
