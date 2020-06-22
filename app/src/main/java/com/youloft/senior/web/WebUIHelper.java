package com.youloft.senior.web;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.youloft.senior.R;
import com.youloft.webview.WebComponent;

/**
 * 相关的UIHelper
 *
 * @author Administrator
 */
public class WebUIHelper extends WebBaseUIHelper {
    /**
     * 进度view
     */
    private ProgressBar mLoadingLayer;
    /**
     * 失败页面
     */
    private View retryView;
    /**
     * 失败页面
     */
    private View mFailedView;

    /**
     * 处理失败页面的runnable
     */
    Runnable hideRetryView = new Runnable() {
        @Override
        public void run() {
            if (retryView != null && !failed) {
                retryView.setVisibility(View.INVISIBLE);
            }
        }
    };

    /**
     * 是否失败
     */
    private boolean failed = false;
    /**
     * 加载中
     */
    private boolean loading = false;

    private boolean loadSuccess = false;

    public WebUIHelper(WebCallBack webCallBack, View titleView, WebComponent component) {
        super(webCallBack, titleView, component);
        this.mLoadingLayer = webCallBack.getActivity().findViewById(R.id.web_component_loading_layer);
        this.retryView = webCallBack.getActivity().findViewById(R.id.web_component_retry_layout);
        this.mFailedView = webCallBack.getActivity().findViewById(R.id.web_component_fail_layout);
        if (mFailedView != null) {
            mFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                }
            });
        }
    }

    public WebUIHelper(WebCallBack webCallBack, View titleView, WebComponent component, View failedView) {
        super(webCallBack, titleView, component);
        this.mLoadingLayer = webCallBack.getActivity().findViewById(R.id.web_component_loading_layer);
        this.retryView = failedView;
        if (this.retryView == null) {
            return;
        }
        this.mFailedView = failedView.findViewById(R.id.web_component_fail_layout);
        if (mFailedView != null) {
            mFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                }
            });
        }
    }

    /**
     * 是否加载成功
     *
     * @return
     */
    public boolean isLoadSuccess() {
        return loadSuccess;
    }

    /**
     * 隐藏retry view
     *
     * @param deplay
     */
    private void hideRetryView(int deplay) {
        if (retryView == null || failed) {
            return;
        }
        barHandler.removeCallbacks(hideRetryView);
        if (deplay > 0) {
            barHandler.postDelayed(hideRetryView, deplay);
            return;
        }
        retryView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
        if (!TextUtils.isEmpty(finalUrl) && !finalUrl.equals(url)) {
            failed = false;
            hideRetryView(0);
        } else {
            hideRetryView(2000);
        }
        if (!loadSuccess) {
            loadSuccess = !failed;
        }
        loading = false;
        barHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoadingLayer != null) {
                    mLoadingLayer.setVisibility(View.GONE);
                }
            }
        }, 400);
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        failed = false;
        loading = true;
        if (mFailedView != null) {
            mFailedView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loading = false;
    }

    /**
     * 页面加载失败
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        failed = true;
        loading = false;
        barHandler.post(new Runnable() {
            @Override
            public void run() {
                if (barHandler != null) {
                    barHandler.removeCallbacks(hideRetryView);
                }
                if (failed && mFailedView != null && retryView != null) {
                    retryView.setVisibility(View.VISIBLE);
                    mFailedView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 重刷界面，点击刷新
     */
    public void reload() {
        failed = false;
        loading = true;
        mComponent.reload();
        barHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (retryView != null && retryView.getVisibility() == View.VISIBLE && !failed) {
                    retryView.setVisibility(View.INVISIBLE);
                }
            }
        }, 1000);
    }

    /**
     * 进度改变
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (mLoadingLayer == null) {
            return;
        }
        if (newProgress == 100) {
            loading = false;
            barHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadingLayer.setVisibility(View.GONE);
                }
            }, 400);
        } else {
            if (loading && mLoadingLayer.getVisibility() == View.GONE) {
                mLoadingLayer.setVisibility(View.VISIBLE);
            }
        }
        mLoadingLayer.setProgress(newProgress);
    }
}
