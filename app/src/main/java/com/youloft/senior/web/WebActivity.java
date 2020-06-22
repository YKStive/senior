package com.youloft.senior.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.youloft.core.base.BaseActivity;
import com.youloft.senior.R;
import com.youloft.webview.WebComponent;

import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * @author xll
 * @date 2018/9/11 14:00
 */
public class WebActivity extends BaseActivity implements OutWebCallBack {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_ALARM_HELP = 1;
    public boolean hanldDestoryOperation = false;


    protected TextView mTitleView;

    protected View mTitleGroup;
    /**
     * 固定title
     */
    public boolean fixTitle;
    /**
     * 传入title
     */
    public String enterTitle;
    /**
     * 当前url
     */
    public String currentUrl;
    /**
     * 当前url
     */
    public String enterUrl;
    /**
     * 传入的城市code
     */
    public String cityCode;
    /**
     * 星座
     */
    private String astro;
    /**
     * 分享信息
     */
    private String shareInfo;
    /**
     * 分享id
     */
    private String shareid;
    /**
     * 退出时是否打开主页面
     */
    private boolean openMain;
    /**
     * 分享mode
     */
    private String reportModel;
    /**
     * 是否是a方案过来的信息流上报
     */
    private boolean isFlowReport;
    /**
     * 是否需要关闭
     */
    public boolean needClose;
    /**
     * 打开web类型
     */
    private int openWebType = TYPE_NORMAL;

    protected WebFragment webFragment;

    /**
     * title部分是否隐藏
     */
    private boolean hideTitle = false;

    CompositeSubscription subscription = new CompositeSubscription();

    /**
     * 红包任务过来的，
     * 1 为广告红包
     * 2 为补量红包
     */
    private int redPackageType = 0;

    boolean isWrittenOff = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleView = findViewById(R.id.actionbar_title);
        mTitleGroup = findViewById(R.id.web_title_bar);
        webFragment = new WebFragment();
        webFragment.setOutWebCallBack(this);
        boolean needTab = true;
        Bundle args = new Bundle();
        if (getIntent() != null) {
            if (getIntent().hasExtra("needTab")) {
                needTab = getIntent().getBooleanExtra("needTab", true);
            }
            hideTitle = getIntent().getBooleanExtra("is_hide_title", false);
            redPackageType = getIntent().getIntExtra("red_package_type", -1);
            enterUrl = getIntent().getStringExtra("url");
            isWrittenOff = "writtenoff".equalsIgnoreCase(getIntent().getStringExtra("from"));
        }
        args.putBoolean("needTab", needTab);
        webFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.web_content, webFragment).commit();
        initData1();
        webFragment.setTitlePlaceHolder(enterTitle, fixTitle);
        if (!TextUtils.isEmpty(currentUrl)) {
            openUrl(currentUrl, false);
        }
    }

    @Override
    protected void onDestroy() {
        subscription.clear();
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    protected void initData1() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        enterTitle = intent.getStringExtra("title1");
        boolean needShare = intent.getBooleanExtra("showShare", true);
        boolean needCollect = intent.getBooleanExtra("showCollect", true);
        currentUrl = intent.getStringExtra("url");
        cityCode = intent.getStringExtra("cityId");
        astro = intent.getStringExtra("astro");
        shareInfo = intent.getStringExtra("shareInfo");
        shareid = intent.getStringExtra("shareid");
        fixTitle = intent.getBooleanExtra("fixTitle", true);
        openMain = intent.getBooleanExtra("main", false);
        reportModel = intent.getStringExtra("reportModel");
        isFlowReport = intent.getBooleanExtra("isFlowReport", false);
        openWebType = intent.getIntExtra("open_web_type", TYPE_NORMAL);
        webFragment.setActionButtonInitState(needShare, needCollect);
        //处理Title
        if (!TextUtils.isEmpty(enterTitle)) {
            mTitleView.setText(enterTitle);
        }
    }

    public void openUrl(String url) {
        this.openUrl(url, false);
    }

    /**
     * 加载url
     *
     * @param url
     */
    public void openUrl(String url, boolean newTab) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        needClose = !url.contains("[KEEPVIEW]");
        String reqUrl = url;
        if (!reqUrl.toLowerCase().startsWith("http")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(reqUrl));
                startActivity(intent);
                if (redPackageType == 2) {
                    setResult(RESULT_OK);
                }
            } catch (Throwable e) {
            }
            finish();
            return;
        }
        webFragment.loadUrl(reqUrl, newTab);
        if (TextUtils.isEmpty(reqUrl)) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        if (!TextUtils.isEmpty(currentUrl)) {
            openUrl(currentUrl, true);
        }
    }

    /**
     * 填充参数
     */
    protected Map<String, String> fillParams() {
        return null;
    }

    /**
     * 处理分享回调
     */
    public void onShare() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageFinish() {

    }

    @Override
    public boolean needClose() {
        return needClose;
    }

    @Override
    public WebUIHelper createUIHelper(WebCallBack callBack, WebComponent webComponent) {
        WebUIHelper webUIHelper = new WebUIHelper(callBack, mTitleGroup, webComponent);
        webUIHelper.setHideTitle(hideTitle);
        webUIHelper.setRedPackageType(redPackageType);
        webUIHelper.setEnterUrl(enterUrl);
        return webUIHelper;
    }

    @Override
    public void openShare() {
        onShare();
    }


    @Override
    public void onBackPressed() {
        webFragment.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 是否拦截返回
     *
     * @return true:拦截,false:不拦截
     */
    public boolean interceptBack() {
        return false;
    }


    /**
     * 重设titles
     *
     * @param title
     */
    protected void resetTitle(String title) {
        enterTitle = title;
        mTitleView.setText(enterTitle);
        webFragment.setTitlePlaceHolder(enterTitle, fixTitle);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_web_component_layout;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
