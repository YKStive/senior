package com.youloft.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youloft.webview.utils.PayHelper;

/**
 * @author coder
 */
public class CommonWebViewClient extends WebViewClient {

    private JavaScriptBridge jsBridge;

    private ProtocolDispatcher dispatcher;

    private CommonWebView webView;

    private WebViewInterceptor interceptor;

    public CommonWebViewClient(CommonWebView webView) {
        updateVariable(webView);
    }

    /**
     * 设置Interceptor
     *
     * @param webView
     */
    public void updateVariable(CommonWebView webView) {
        this.jsBridge = webView.getJsBridge();
        this.dispatcher = webView.getProtocolDispatcher();
        this.interceptor = webView.webViewInterceptor;
        this.webView = webView;
    }


    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, String url) {
        //是否需要拦截
        if (this.interceptor != null && this.interceptor.shouldOverrideUrlLoading(view, url)) {
            return true;
        }

        if (this.dispatcher != null && this.dispatcher.dispatchUrlLoading(view, url)) {
            return true;
        }

        if (url.startsWith("mailto:")) {
            handleMailto(view, url);
            return true;
        }

        /**
         * 兼容对于点击时使用新Tab进行打开的情况
         */
        if (WebViewInterceptor.isHttpUrl(url)) {
            WebView.HitTestResult result = view.getHitTestResult();
            boolean notTab = url.contains("[NOTAB]");
            boolean useTab = false;
            if (!notTab) {
                useTab = url.contains("[TAB]");
            }
            if (useTab//必须为tab打开
                    || (result != null && (result.getType() == 7 || result.getType() == 8) && !notTab && !url.toLowerCase().contains("score.51wnl-cq.com"))) {
                ViewParent parent = webView.getParent();
                if (parent instanceof WebComponent) {
                    final String loadUrl = ((CommonWebView) view).getWebViewInterceptor().replacePlaceHolder(url);
                    if (useTab) {
                        ((WebComponent) parent).forceLoadUrl(loadUrl, true);
                    } else {
                        ((WebComponent) parent).loadUrl(loadUrl, true);
                    }
                    return true;
                }
            }
        }

        /**
         * 处理本地支付功能用于网页透传
         */
        if (PayHelper.handleNativePay(view, url)) {
            return true;
        }
        int i = url.indexOf(":");
        if (i > 0) {
            String schema = url.substring(0, i);
            if ("http".equalsIgnoreCase(schema)
                    || "https".equalsIgnoreCase(schema)) {
                final String formatUrl = ((CommonWebView) view).getWebViewInterceptor().replacePlaceHolder(url);
                if (!formatUrl.equalsIgnoreCase(url)) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.loadUrl(formatUrl);
                        }
                    });
                    return true;
                }
                return false;
            }
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        } catch (Throwable e) {
        }
        return true;
    }

    /**
     * 处理邮箱链接
     *
     * @param view
     * @param url
     */
    private void handleMailto(WebView view, String url) {
        try {
            MailTo parse = MailTo.parse(url);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{parse.getTo()});
            intent.putExtra(Intent.EXTRA_TEXT, parse.getBody());
            intent.putExtra(Intent.EXTRA_SUBJECT, parse.getSubject());
            intent.putExtra(Intent.EXTRA_CC, parse.getCc());
            intent.setType("message/rfc822");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        } catch (Throwable e) {
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (this.jsBridge != null) {
            this.jsBridge.onPageStart(view, url);
        }
        if (this.interceptor != null) {
            this.interceptor.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onInternalPageCommitVisible(view, url);
        }
        if (!WebComponent.isActive(view)) {
            return;
        }
        if (this.interceptor != null) {
            this.interceptor.onPageFinished(view, url);
        }
    }

    /**
     * 内部对PageCommitVisible做兼容
     *
     * @param view
     * @param url
     */
    private void onInternalPageCommitVisible(WebView view, String url) {
        if (this.jsBridge != null) {
            this.jsBridge.onPageLoaded(view, url);
        }
        if (!WebComponent.isActive(view)) {
            return;
        }
        if (this.interceptor != null) {
            this.interceptor.onPageCommitVisible(view, url);
        }
    }


    @Override
    public void onPageCommitVisible(WebView view, String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            onInternalPageCommitVisible(view, url);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (this.interceptor != null && this.interceptor.onReceivedSslError(view, handler, error)) {
            return;
        }
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (!WebComponent.isActive(view)) {
            super.onReceivedError(view, request, error);
            return;
        }
        if (this.interceptor != null && this.interceptor.onReceivedError(view, request, error)) {
            return;
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (!WebComponent.isActive(view)) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            return;
        }
        if (this.interceptor != null) {
            this.interceptor.onReceivedError(view, errorCode, description, failingUrl);
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}
