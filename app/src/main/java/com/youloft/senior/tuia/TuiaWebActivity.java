package com.youloft.senior.tuia;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.youloft.core.base.BaseActivity;
import com.youloft.senior.R;

public class TuiaWebActivity extends BaseActivity {
    String activityUrl = null;

    WebView activityWebView;

    TextView titleView;

    String tuiaData;

    @Override
    public int getLayoutResId() {
        return R.layout.tuia_activity_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuia_activity_layout);
        activityWebView = findViewById(R.id.web);
        titleView = findViewById(R.id.actionbar_title);
        if (getIntent() != null) {
            activityUrl = getIntent().getStringExtra("url");
        }
        if (TextUtils.isEmpty(activityUrl)) {
            finish();
            return;
        }
        initWeb(activityWebView);
        activityWebView.loadUrl(activityUrl);
        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWeb(final WebView webView) {
        webView.addJavascriptInterface(new TAHandler(), "TAHandler");
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(false);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSetting.setMediaPlaybackRequiresUserGesture(false);
        }
        //发奖必须加
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    titleView.setText(title);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url == null) {
                        return false;
                    }
                    //处理普通请求
                    if (url.startsWith("http") || url.startsWith("https")) {
                        /**
                         * 如果是8.0以上的,则不调用loadUrl,并返回false
                         */
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            return false;
                        }
                        if (webView != null) {
                            webView.loadUrl(url);
                        }
                        return true;
                    } else {
                        /**
                         * 防止  找不到网页net:err_unknown_url_scheme
                         * 支持scheme协议
                         */
                        startActivity(TuiaWebActivity.this, Uri.parse(url));
                        return true;
                    }
                } catch (Exception e) {
                    //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    e.printStackTrace();
                    //返回true 代表让webview自己执行
                    return true;
                }
            }
        });
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> startDown(url));
    }

    @Override
    public void finish() {
        if (tuiaData != null) {
            setResult(RESULT_OK, new Intent().putExtra("tuia_data", tuiaData));
        }
        super.finish();
    }

    private void startDown(String downloadUrl) {
        //判断类型，但是没有传下来
    }


    /**
     * 根据Uri 调起应用
     *
     * @param context
     * @param uri
     * @return
     */
    public boolean startActivity(Context context, Uri uri) {
        if (context == null) {
            return false;
        }
        if (uri == null) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = context.getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo == null) {
            return false;
        }
        context.startActivity(intent);
        return true;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    /**
     * js交互类
     */
    public class TAHandler {

        /**
         * 发奖接口
         *
         * @param data
         */
        @JavascriptInterface
        public void reward(String data) {
            //奖励上报接口
            tuiaData = data;
        }

        /**
         * 关闭事件
         */
        @JavascriptInterface
        public void close() {
            runOnUiThread(TuiaWebActivity.this::finish);
        }
    }

    public static void start(Activity context, String url) {
        start(context, url, 10202);
    }

    public static void start(Activity context, String url, int requestCode) {
        context.startActivityForResult(new Intent(context, TuiaWebActivity.class)
                .putExtra("url", url), requestCode);
    }
}
