package com.youloft.senior.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

/**
 * Created by Jie Zhang on 2016/4/25.
 */
public class WebHelper {

    Intent intent = null;
    Context context = null;
    Class<?> webChild = WebActivity.class;

    public static WebHelper create(Context context) {
        return new WebHelper(context);
    }

    public static WebHelper create(Context context, Class<?> webChild) {
        return new WebHelper(context, webChild);
    }

    public WebHelper(Context context) {
        this.context = context;
    }

    public WebHelper(Context context, Class<?> webChild) {
        this.context = context;
        this.webChild = webChild;
    }

    public void show() {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public void showForResult(int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            show();
        }
    }

    /**
     * 产生一个WebIntent
     *
     * @param activity
     * @return
     */
    public Intent makeWebIntent(Context activity, String url, String title, boolean showShare, boolean showNavFoot) {
        Intent intent = new Intent(activity, webChild);
        intent.putExtra("title", title);
        intent.putExtra("showShare", showShare);
        intent.putExtra("showNavFoot", showNavFoot);
        intent.putExtra("url", url);
        intent.putExtra("reportModel", "");
        return intent;
    }

    /**
     * 产生一个WebIntent
     *
     * @param activity
     * @return
     */
    public Intent makeWebIntent2(Context activity, String url, String title, boolean showShare, boolean showNavFoot) {
        Intent intent = new Intent(activity, webChild);
        intent.putExtra("title1", title);
        intent.putExtra("showShare", showShare);
        intent.putExtra("showNavFoot", showNavFoot);
        intent.putExtra("url", url);
        intent.putExtra("reportModel", "");
        return intent;
    }

    public WebHelper putCityCode(String cityId) {
        intent.putExtra("cityId", cityId);
        return this;
    }

    public WebHelper putCollect(boolean collect) {
        intent.putExtra("showCollect", collect);
        return this;
    }

    public WebHelper put(String key, String data) {
        intent.putExtra(key, data);
        return this;
    }

    public WebHelper put(String key, Serializable data) {
        intent.putExtra(key, data);
        return this;
    }

    public WebHelper put(String key, int data) {
        intent.putExtra(key, data);
        return this;
    }

    public WebHelper putExtra(String key, boolean value) {
        intent.putExtra(key, value);
        return this;
    }

    /**
     * 显示内部的URL
     *
     * @param url
     */
    public WebHelper showInternalWeb(String url) {
//        if (TextUtils.isEmpty(url)) {
//            return null;
//        }
        intent = makeWebIntent(context, url == null ? "" : url, null, true, false);
        intent.putExtra("fixTitle", false);
        return this;
    }

    /**
     * 显示Web
     *
     * @param url
     * @param title
     * @param showShare
     * @param showNavFoot
     */
    public WebHelper showWeb(String url, String title, boolean showShare, boolean showNavFoot) {
        intent = makeWebIntent2(context, url, title, showShare, showNavFoot);
        return this;
    }

    public WebHelper fixTitle(boolean fixTitle) {
        intent.putExtra("fixTitle", fixTitle);
        return this;
    }

    /**
     * 设置打开类型，比如打开的提醒帮助中心，等等
     *
     * @param type
     * @return
     */
    public WebHelper putOpenWebType(int type) {
        intent.putExtra("open_web_type", type);
        return this;
    }

    /**
     * @param url
     * @param title
     * @param shareUrl
     * @param shareContent
     * @param shareImage   如果使用截图请穿"default"
     */

    public WebHelper showWebForShare(String url, String title, String shareUrl, String shareContent, String shareImage) {
        intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        return this;
    }

    public WebHelper showWebForShareFixTitle(String url, String title, String shareUrl, String shareContent, String shareImage, boolean fixTitle) {
        intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        intent.putExtra("fixTitle", fixTitle);
        return this;
    }

    public WebHelper showWebForShareFixTitle(String url, String title, String shareUrl, String shareContent, String shareImage) {
        intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        intent.putExtra("fixTitle", false);
        return this;
    }

    /**
     * @param url
     * @param title
     * @param shareUrl
     * @param shareContent
     * @param shareImage   如果使用截图请穿"default"
     */

    public WebHelper showWebForShare(String analyticsName, String url, String title, String shareUrl, String shareContent, String shareImage) {
        intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("analyticsName", analyticsName);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        return this;
    }

    /**
     * @param url
     * @param title
     * @param shareUrl
     * @param shareContent
     * @param shareImage   如果使用截图请穿"default"
     */
    public WebHelper showWebForShare2(String url, String title, String shareUrl, String shareContent, String shareImage, String urlTxt) {
        intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        intent.putExtra("urlTxt", urlTxt);
        return this;
    }

    public WebHelper showWebForShare2(String analyticsName, String url, String title, String shareUrl, String shareContent, String shareImage, String urlTxt) {
        intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        intent.putExtra("analyticsName", analyticsName);
        intent.putExtra("urlTxt", urlTxt);
        intent.putExtra("fixTitle", false);
        return this;
    }


    /**
     * @param url
     * @param title
     * @param shareUrl
     * @param shareContent
     * @param shareImage   如果使用截图请穿"default"
     */

    public WebHelper showWebForShare(String url, String title, String shareUrl, String shareContent, String shareImage, boolean isShare) {
        intent = makeWebIntent2(context, url, title, isShare, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        return this;
    }

    public WebHelper showWebForShare(String analyticsName, String url, String title, String shareUrl, String shareContent, String shareImage, boolean isShare) {
        intent = makeWebIntent2(context, url, title, isShare, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("analyticsName", analyticsName);
        intent.putExtra("shareImage", shareImage);
        return this;
    }
    /////////////////以上是适应446版本以前打开WebActivity

    public WebHelper putReportModel(String model) {
        intent.putExtra("reportModel", model);
        return this;
    }

    public WebHelper putFlowReport(boolean isFlowReport) {
        intent.putExtra("isFlowReport", isFlowReport);
        return this;
    }

    public WebHelper putAnalyticsName(String analyticsName) {
        intent.putExtra("analyticsName", analyticsName);
        return this;
    }

    public WebHelper putDefaultUrl(String defaultUrl) {
        intent.putExtra("defaultUrl", defaultUrl);
        return this;
    }


    public Intent toIntent() {
        return intent;
    }

    public Intent makeShowWebForShare2FixTitle(String url, String title, String shareUrl, String shareContent, String shareImage, String urlTxt) {
        Intent intent = makeWebIntent2(this.context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        intent.putExtra("urlTxt", urlTxt);
        intent.putExtra("fixTitle", false);
        return intent;
    }

    /**
     * @param url
     * @param title
     * @param shareUrl
     * @param shareContent
     * @param shareImage   如果使用截图请穿"default"
     */
    public Intent makeShowWebForShare2(String url, String title, String shareUrl, String shareContent, String shareImage, String urlTxt) {
        Intent intent = makeWebIntent2(context, url, title, true, false);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareModes", shareContent);
        intent.putExtra("shareImage", shareImage);
        intent.putExtra("urlTxt", urlTxt);
        return intent;
    }

    public WebHelper setFlags(int flags) {
        intent.setFlags(flags);
        return this;
    }

    public WebHelper setSource(String from, String o) {
        intent.putExtra("from", o);
        return this;
    }
}