package com.youloft.socialize.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youloft.socialize.SOC_MEDIA;
import com.youloft.socialize.ShareBoard;
import com.youloft.socialize.Socialize;
import com.youloft.socialize.share.AbstractShareAction;
import com.youloft.socialize.share.ShareEventTracker;
import com.youloft.socialize.share.ShareImage;
import com.youloft.socialize.share.ShareWeb;
import com.youloft.util.ScreenshotUtil;
import com.youloft.webview.CommonWebView;
import com.youloft.webview.protocol.handler.AbstractCommandHandler;

import java.net.URLDecoder;
import java.util.Iterator;

/**
 * 网页协议处理
 * <p>
 * ＠author coder
 */
public class WebShareHandler extends AbstractCommandHandler {

    private static class InstanceHolder {
        public static WebShareHandler sInstance = new WebShareHandler();
    }

    static final String CMD_SHARE = "share";

    private WebShareHandler() {
        registeCommand(CMD_SHARE);
    }

    /**
     * 处理
     *
     * @return
     */
    public static WebShareHandler getInstance() {
        return InstanceHolder.sInstance;
    }


    private static final String TAG = "WebShareHandler";

    @Override
    public Object handleCommand(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        String cmd = cmdObj.getString("_cmd");
        if (TextUtils.isEmpty(cmd)) {
            return null;
        }
        if (cmd.startsWith("share#")) {
            return handleNewShare(webView, command, args, cmdObj);
        } else if (cmd.startsWith("share:")) {
            return handleDepShare(webView, command, args, cmdObj);
        }
        return null;

    }

    /**
     * 处理旧版的分享将参数转换之后交由新版分享Panel进行相关的处理
     *
     * @param webView
     * @param command
     * @param args
     * @param cmdObj
     * @return
     */
    private Object handleDepShare(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        String argString = cmdObj.getString("argString");
        if (TextUtils.isEmpty(argString)) {
            return null;
        }
        if (webView.getWebViewInterceptor() != null) {
            webView.getWebViewInterceptor().startHandleShare();
        }
        try {
            JSONObject shareParams = JSON.parseObject(URLDecoder.decode(argString, "utf-8"));
            String text = shareParams.getString("text");
            String title = shareParams.getString("title");
            String url = shareParams.getString("targetUrl");
            if (TextUtils.isEmpty(url)) {
                url = shareParams.getString("url");
            }
            String prefix = shareParams.getString("prefix");
            if (!TextUtils.isEmpty(prefix)) {
                text = prefix + " " + text;
            }
            String imageUrl = shareParams.getString("imageURL");
            boolean shotFlag = shareParams.getIntValue("image") == 1;
            JSONObject shareData = new JSONObject();
            shareData.put("text", text);
            shareData.put("title", title);
            shareData.put("url", url);
            shareData.put("image", shotFlag ? "shot" : imageUrl);
            JSONObject shareDatas = new JSONObject();
            shareDatas.put("default", shareData);

            shareWithPanel(webView.getWebViewInterceptor().getActivity(), webView, shareDatas, "shareCallback");
        } catch (Throwable e) {

        }
        return null;
    }

//    /**
//     * 其他系统分享点击
//     *
//     * @param shareParam
//     * @param webView
//     */
//    private void handleSystemClick(final JSONObject shareParam, final CommonWebView webView) {
//        try {
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Bitmap screenshot = TextUtils.isEmpty(shareParam.getString("image")) ?
//                            null : ScreenshotUtil.screenshot(webView.getWebViewInterceptor().getActivity(), true);
//                    String url = shareParam.getString("url");
//                    if (TextUtils.isEmpty(url)) {
//                        url = webView.getUrl();
//                    }
//                    systemShare(webView.getWebViewInterceptor().getActivity(), screenshot, shareParam.getString("text"), url);
//                }
//            }, 300);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//    }

    /**
     * 新版分享
     * {
     * "shareData": {
     * "qq": {
     * "image": "shot",
     * "text": "",
     * "title": "fuck",
     * "preload": false,
     * "url": ""
     * }
     * },
     * "direct": true,
     * "callback": "shareCallback",
     * "platform": "qq"
     * }
     *
     * @param webView
     * @param command
     * @param args
     * @param cmdObj
     * @return
     */
    private Object handleNewShare(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        String argString = cmdObj.getString("argString");
        if (TextUtils.isEmpty(argString)) {
            return null;
        }

        try {
            JSONObject shareParams = JSON.parseObject(URLDecoder.decode(argString, "utf-8"));
            JSONObject shareDatas = shareParams.getJSONObject("shareData");
            String callback = TextUtils.isEmpty(shareParams.getString("callback")) ? "shareCallback" : shareParams.getString("callback");
            //直接分享
            if (shareParams.getBooleanValue("direct")) {
                String platform = shareParams.getString("platform");
                JSONObject shareParam = shareDatas.getJSONObject(platform);
                if (null == shareParam) {
                    shareParam = shareDatas.getJSONObject("default");
                }
                //直接分享
                return directShareWithParams(
                        webView.getWebViewInterceptor().getActivity(),
                        webView,
                        shareParams.getString("platform"),
                        shareParam,
                        callback
                );
            } else if (shareDatas != null) {
                shareWithPanel(webView.getWebViewInterceptor().getActivity(), webView, shareDatas, callback);
            } else {
                //有分享面板的
                handleDepShare(webView, command, args, cmdObj);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 系统分享
     *
     * @param activity
     * @param bitmap
     * @param shareText
     * @param mShareUrl
     */
    public static void systemShare(Activity activity, Bitmap bitmap, String shareText, String mShareUrl) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            if (shareText == null) {
                shareText = "";
            }
            if (mShareUrl != null) {
                shareText += " " + mShareUrl;
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                //uri 是图片的地址
                String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null);
                if (!TextUtils.isEmpty(path)) {
                    Uri uri = Uri.parse(path);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/*");
                    //当用户选择短信时使用sms_body取得文字
                    shareIntent.putExtra("sms_body", shareText);
                } else {
                    shareIntent.setType("text/plain");
                }
            } else {
                shareIntent.setType("text/plain");
            }
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //自定义选择框的标题
            activity.startActivity(Intent.createChooser(shareIntent, "万年历分享"));
            //系统默认标题
        } catch (Exception ignored) {
        }
    }

    /**
     * 使用面板启动分享
     *
     * @param activity
     * @param webView
     * @param shareDatas
     */
    private void shareWithPanel(Activity activity, final CommonWebView webView, JSONObject shareDatas, final String callback) {
        ShareBoard shareBoard = Socialize.getIns().newShareBoard(activity);
        shareBoard.setEventTracker(new ShareEventTracker(webView.getWebViewInterceptor()) {
            @Override
            public void onSuccess(String name) {
                webView.getJsBridge().executeJavaScriptNow(String.format("%s(%s,'%s')", callback, 1, name), null);
            }

            @Override
            public void onFailed(String name) {
                webView.getJsBridge().executeJavaScriptNow(String.format("%s(%s,'%s')", callback, 0, name), null);
            }
        });
        Iterator<String> iterator = shareDatas.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            AbstractShareAction action = null;
            if ("default".equalsIgnoreCase(next)) {
                action = shareBoard.withDefault();
            } else {
                action = shareBoard.withPlatform(SOC_MEDIA.from(next));
            }
            bindActionWithData(activity, shareDatas.getJSONObject(next), action);
        }
        shareBoard.putExtra("url", webView.getUrl());
        shareBoard.shareWithUI();
    }


    /**
     * 直接分享
     *
     * @param activity
     * @param webView
     * @param platform
     * @param shareData
     * @param callback
     * @return
     */
    private Object directShareWithParams(Activity activity, final CommonWebView webView, String platform, JSONObject shareData, final String callback) {
        AbstractShareAction action = Socialize.getIns().share(activity).platform(SOC_MEDIA.from(platform));
        action.eventTracker(new ShareEventTracker(webView.getWebViewInterceptor()) {
            @Override
            public void onSuccess(String name) {
                webView.getJsBridge().executeJavaScriptNow(String.format("%s(%s,'%s')", callback, 1, name), null);
                webView.getWebViewInterceptor().onShareEvent(name, true);
            }

            @Override
            public void onFailed(String name) {
                webView.getJsBridge().executeJavaScriptNow(String.format("%s(%s,'%s')", callback, 0, name), null);
                webView.getWebViewInterceptor().onShareEvent(name, false);
            }
        });

        bindActionWithData(activity, shareData, action);
        action.perform();
        return null;

    }

    /**
     * 绑定数据到Action上
     *
     * @param activity
     * @param shareData
     * @param action
     */
    private void bindActionWithData(Activity activity, JSONObject shareData, AbstractShareAction action) {
        String imageUrl = shareData.getString("image");
        ShareImage shareImage = null;
        if (!TextUtils.isEmpty(imageUrl)) {
            if ("shot".equalsIgnoreCase(imageUrl)) {
                Bitmap screenshot = ScreenshotUtil.screenshot(activity, true);
                shareImage = new ShareImage(activity, screenshot);
                shareImage.setThumb(new ShareImage(activity, ThumbnailUtils.extractThumbnail(screenshot, 300, 300)));
            } else {
                shareImage = new ShareImage(activity, imageUrl);
                shareImage.setThumb(new ShareImage(activity, imageUrl));
            }
        }
        String text = shareData.getString("text");
        String title = shareData.getString("title");
        String url = shareData.getString("url");

        if (!TextUtils.isEmpty(title)) {
            action.subject(title);
        }

        if (!TextUtils.isEmpty(text)) {
            action.text(text);
        }

        if (shareImage != null) {
            action.image(shareImage);
        }

        if (!TextUtils.isEmpty(url)) {
            ShareWeb web = new ShareWeb(url).setTitle(title).setThumb(shareImage).setDescription(text);
            if (web != null) {
                web.setThumb(shareImage);
            }
            action.web(web);
        }
    }

}
