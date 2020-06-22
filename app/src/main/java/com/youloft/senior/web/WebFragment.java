package com.youloft.senior.web;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.youloft.core.base.BaseFragment;
import com.youloft.senior.R;
import com.youloft.socialize.SOC_MEDIA;
import com.youloft.webview.ValueCallback;
import com.youloft.webview.WebComponent;


/**
 * 用于web访问
 *
 * @author xll
 * @date 2018/9/11 14:00
 */
public class WebFragment extends BaseFragment implements WebCallBack {
    /**
     * webView
     */
    WebComponent mWebComponent;

    /**
     * web ui辅助类，包括执行web命令
     */
    protected WebUIHelper webUIHelper;
    /**
     * 外部回调
     */
    private OutWebCallBack outWebCallBack;
    /**
     * 加载的url
     */
    String webUrl = null;
    /**
     * 默认的title
     */
    protected String enterTitle;
    /**
     * 是否固定title
     */
    protected boolean fixTitle;
    /**
     * 需要分享
     */
    private boolean needShare;
    /**
     * 需要收藏
     */
    private boolean needCollect;
    /**
     * 是否打开新的页面
     */
    private boolean openNewPage = false;
    /**
     * 是否来自tab，需要打开新页面
     */
    private boolean needTab = false;

    public WebFragment() {
        super();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.web_component_fragment_layout;
    }

    /**
     * 设置callBack，用于与外部沟通
     *
     * @param callBack
     */
    public void setOutWebCallBack(OutWebCallBack callBack) {
        this.outWebCallBack = callBack;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebComponent = view.findViewById(R.id.web_view);
        init();
    }

    /**
     * 是否加载完成
     *
     * @return
     */
    public boolean isLoaded() {
        return webUIHelper != null && webUIHelper.isLoadSuccess();
    }

    public void reload() {
        if (webUIHelper == null) {
            return;
        }
        webUIHelper.reload();
    }

    /**
     * 初始化
     */
    private void init() {
        if (getArguments() != null) {
            needTab = getArguments().getBoolean("needTab", needTab);
        }
        if (outWebCallBack == null) {
            webUIHelper = new WebUIHelper(this, null, mWebComponent);
        } else {
            webUIHelper = outWebCallBack.createUIHelper(this, mWebComponent);
        }
        //如果是neeTab为true，需要点击时跳转新页面
        webUIHelper.setNeedTab(needTab);
        webUIHelper.initTitleBar();
        mWebComponent.setWebInterceptor(webUIHelper);
        mWebComponent.setTitlePlaceHolder(enterTitle, fixTitle);
        mWebComponent.setActionButtonInitState(needShare, needCollect);
        webUIHelper.setOpenNewPage(openNewPage);
        if (!TextUtils.isEmpty(webUrl)) {
            mWebComponent.loadUrl(webUrl);
        }
    }

    /**
     * 设置固定Title
     *
     * @param title   Title
     * @param isFixed 是否固定为此Title
     */
    public void setTitlePlaceHolder(String title, boolean isFixed) {
        this.enterTitle = title;
        this.fixTitle = isFixed;
        if (mWebComponent != null) {
            mWebComponent.setTitlePlaceHolder(enterTitle, fixTitle);
        }
    }

    /**
     * 设置button默认值s
     *
     * @param needShare
     * @param needCollect
     */
    public void setActionButtonInitState(boolean needShare, boolean needCollect) {
        this.needShare = needShare;
        this.needCollect = needCollect;
        if (mWebComponent != null) {
            mWebComponent.setActionButtonInitState(needShare, needCollect);
        }
    }

    /**
     * 加载url
     */
    public void loadUrl(String url) {
        this.loadUrl(url, false);
    }

    public void loadUrl(String url, boolean newTab) {
        this.webUrl = url;
        if (mWebComponent != null) {
            mWebComponent.loadUrl(url, newTab);
        }
    }

    /**
     * 是否有返回
     *
     * @return
     */
    @Override
    public boolean isBack() {
        if (mWebComponent == null) {
            return false;
        }
        return mWebComponent.canGoBack();
    }

    @Override
    public void onPageFinish() {
        if (outWebCallBack != null) {
            outWebCallBack.onPageFinish();
        }
    }

    @Override
    public boolean needClose() {
        if (outWebCallBack != null) {
            return outWebCallBack.needClose();
        }
        return false;
    }

    @Override
    public void openShare() {
        if (outWebCallBack != null) {
            outWebCallBack.openShare();
        }
    }

    public void onShareResult(int i, SOC_MEDIA platform) {
        if (webUIHelper == null || mWebComponent == null) {
            return;
        }
        try {
            if (i == 0) {
                webUIHelper.onShareEvent(platform == null ? "" : platform.name(), true);
            }
            if (webUIHelper.hasShare()) {
                mWebComponent.executeJavaScriptNow("shareCallback(" + i + ")", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回处理
     *
     * @return
     */
    public void onBackPressed() {
        if (webUIHelper != null) {
            webUIHelper.onWebBack();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWebComponent != null) {
            mWebComponent.onActivityResult(getActivity(), requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webUIHelper == null || mWebComponent == null) {
            return;
        }
        webUIHelper.onDestroy();
        mWebComponent.onDestory();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webUIHelper == null || mWebComponent == null) {
            return;
        }
        mWebComponent.onResume();
        webUIHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webUIHelper == null || mWebComponent == null) {
            return;
        }
        mWebComponent.onPause();
        webUIHelper.onPause();
    }

    /**
     * 重设titles
     *
     * @param title
     */
    protected void resetTitle(String title) {
        enterTitle = title;
        if (mWebComponent != null) {
            mWebComponent.setTitlePlaceHolder(enterTitle, fixTitle);
        }
    }

    /**
     * 获取网页url
     *
     * @return
     */
    public String getUrl() {
        if (mWebComponent == null) {
            return webUrl;
        }
        return mWebComponent.getUrl();
    }

    /**
     * 返回网页title
     *
     * @return
     */
    public String getWebTitle() {
        if (mWebComponent == null) {
            return "";
        }
        return mWebComponent.getWebTitle();
    }

    /**
     * 开始分享
     */
    public void startHandleShare() {
        if (webUIHelper == null) {
            return;
        }
        webUIHelper.startHandleShare();
    }

    public void setOpenNewPage(boolean openNewPage) {
        this.openNewPage = openNewPage;
        if (webUIHelper != null) {
            webUIHelper.setOpenNewPage(this.openNewPage);
        }
    }

    /**
     * 清空历史
     */
    public void clearHistory() {
        if (mWebComponent != null) {
            mWebComponent.clearHistory();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (mWebComponent != null) {
                mWebComponent.executeJavaScriptNow(isVisibleToUser ? "onAppWillLoad()" : "onAppDispear()", null);
            }
        } catch (Throwable e) {

        }
    }

    /**
     * 执行js
     *
     * @param format
     * @param o
     */
    public void executeJavaScriptNow(String format, ValueCallback<String> o) {
        if (mWebComponent != null) {
            mWebComponent.executeJavaScriptNow(format, o);
        }
    }

    /**
     * 滚动回顶部w
     */
    public void handleScrollToTop() {
        if (mWebComponent != null) {
            mWebComponent.executeJavaScriptNow("triggerScrollTop()", null);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
