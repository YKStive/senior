package com.youloft.webview.protocol.handler;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.youloft.util.AppUtil;
import com.youloft.util.StatusBarUtils;
import com.youloft.util.UiUtil;
import com.youloft.webview.CommonWebView;
import com.youloft.webview.ValueCallback;
import com.youloft.webview.WebComponent;

import java.net.URLDecoder;

/**
 * 核心命令处理即与应用无关的处理
 */
public class CoreCommandHandler extends AbstractCommandHandler {

    private static final String COMMAND_COPY_TEXT = "copytext";
    private static final String COMMAND_RATE_APP = "rate";
    public static final String COMMAND_QUERY_APP = "queryApp";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_CLOSETAB = "closeTab";//关闭tab
    private static final String COMMAND_BACK = "back";
    private static final String COMMAND_SAVE_IMAGE = "saveimage";
    private static final String COMMAND_PICK_DATE = "pickdate";
    private static final String COMMAND_WEIXIN_SUCCESS = "weixin_success";
    private static final String COMMAND_FULLSCREEN = "enablefullscreen";


    public static final String COMMAND_GET_USER = "getuser";
    public static final String COMMAND_GET_WIDTH = "getwidth";
    public static final String COMMAND_GET_HEIGHT = "getHeight";
    public static final String COMMAND_JUMP_MARKET = "jumpMarket";
    public static final String COMMAND_DOWNLOAD_APK = "downloadApk";
    public static final String COMMAND_BOOT_APP = "bootApp";
    public static final String COMMAND_GET_DVALUE = "getDValue";
    public static final String COMMAND_GET_NOTCH_SIZE = "getNotchSize";
    public static final String COMMAND_SHOW_SHAREBUTTON = "enableShare";
    public static final String COMMAND_SHOW_COLLECT = "enableCollect";
    public static final String COMMAND_FORMAT_URL = "formaturl";
    public static final String COMMAND_SHOW_TODAY = "enableToday";
    public static final String COMMAND_GET_STATUSBARHEIGHT = "getStatusBarHeight";

    public CoreCommandHandler() {
        registeCommand(COMMAND_COPY_TEXT);
        registeCommand(COMMAND_RATE_APP);
        registeCommand(COMMAND_QUERY_APP);
        registeCommand(COMMAND_EXIT);
        registeCommand(COMMAND_BACK);
        registeCommand(COMMAND_SAVE_IMAGE);
        registeCommand(COMMAND_PICK_DATE);
        registeCommand(COMMAND_CLOSETAB);
        registeCommand(COMMAND_FULLSCREEN);

        registeCommand(COMMAND_GET_USER);
        registeCommand(COMMAND_GET_WIDTH);
        registeCommand(COMMAND_GET_HEIGHT);
        registeCommand(COMMAND_GET_STATUSBARHEIGHT);
        registeCommand(COMMAND_JUMP_MARKET);
        registeCommand(COMMAND_DOWNLOAD_APK);
        registeCommand(COMMAND_BOOT_APP);
        registeCommand(COMMAND_GET_DVALUE);
        registeCommand(COMMAND_GET_NOTCH_SIZE);
        registeCommand(COMMAND_SHOW_SHAREBUTTON);
        registeCommand(COMMAND_SHOW_COLLECT);
        registeCommand(COMMAND_FORMAT_URL);
        registeCommand(COMMAND_SHOW_TODAY);

    }

    @Override
    public boolean accept(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        if ("gesture".equalsIgnoreCase(command)) {
            webView.hasArrivedTop = null != args && "1".equalsIgnoreCase(args.getString("attop"));
        }
        return super.accept(webView, command, args, cmdObj);
    }

    @Override
    public Object handleCommand(final CommonWebView webView, String command, JSONObject args, final JSONObject cmdObj) {
        if (COMMAND_COPY_TEXT.equalsIgnoreCase(command)) {
            return handleCopyCommand(webView, cmdObj.getString("argString"));
        } else if (COMMAND_RATE_APP.equalsIgnoreCase(command)) {
            AppUtil.openMarket(webView.getContext(), webView.getContext().getPackageName(), false);
        } else if (COMMAND_QUERY_APP.equalsIgnoreCase(command)) {
            return handleQueryApp(webView, args, cmdObj);
        } else if (COMMAND_EXIT.equalsIgnoreCase(command)) {
            finishHostActivity(webView);
        } else if (COMMAND_CLOSETAB.equalsIgnoreCase(command)) {
            if (webView.getParent() instanceof WebComponent) {
                if (!((WebComponent) webView.getParent()).popWebView(webView)) {
                    finishHostActivity(webView);
                }
            }
        } else if (COMMAND_BACK.equalsIgnoreCase(command)) {
            //对于通过protocol协议使用url形式的需要这样处理对于其它方式如confirm的不需要这样处理
            final int step = /*"url".equalsIgnoreCase(cmdObj.getString("_method")) ? -2 : */-1;
            if (webView.canGoBackOrForward(step)) {
                webView.goBackOrForward(step);
                return null;
            }
            if (webView.getParent() instanceof WebComponent) {
                if (!((WebComponent) webView.getParent()).popWebView(webView)) {
                    finishHostActivity(webView);
                }
            } else {
                finishHostActivity(webView);
            }
        } else if (COMMAND_SAVE_IMAGE.equalsIgnoreCase(command)) {
            webView.getWebViewInterceptor().saveImageToPhotoLibrary(webView.getContext(), cmdObj.getString("argString"), false);
        } else if (COMMAND_PICK_DATE.equalsIgnoreCase(command)) {
            pickDate(webView, command, args, cmdObj);
        } else if (COMMAND_WEIXIN_SUCCESS.equalsIgnoreCase(command)) {
            webView.getWebViewInterceptor().getActivity().setResult(Activity.RESULT_OK);
            finishHostActivity(webView);
        } else if (COMMAND_GET_USER.equalsIgnoreCase(command)) {
            return webView.getWebViewInterceptor().getUserId();
        } else if (COMMAND_GET_WIDTH.equalsIgnoreCase(command)) {
            return webView.getWidth() / UiUtil.getScaled(webView.getContext());
        } else if (COMMAND_GET_HEIGHT.equalsIgnoreCase(command)) {
            return webView.getHeight();
        } else if (COMMAND_GET_DVALUE.equalsIgnoreCase(command)) {
            return webView.getWebViewInterceptor().getPlaceHolderValue(args.getString("key"));
        } else if (COMMAND_GET_NOTCH_SIZE.equalsIgnoreCase(command)) {
            return StatusBarUtils.getNotchHeight(webView.getContext());
        } else if (COMMAND_GET_STATUSBARHEIGHT.equalsIgnoreCase(command)) {
            if (Build.VERSION.SDK_INT >= 19) {
                return StatusBarUtils.getStatusHeight(webView.getContext());
            }
            return 0;
        } else if (COMMAND_JUMP_MARKET.equalsIgnoreCase(command)) {
            String packageName = args.getString("pn");
            boolean forDownload = args.getBooleanValue("dl");
            AppUtil.openMarket(webView.getContext(), packageName, forDownload);
        } else if (COMMAND_DOWNLOAD_APK.equalsIgnoreCase(command)) {
            String iconUrl = args.getString("icon");
            String title = args.getString("title");
            String downloadUrl = args.getString("url");
            webView.getWebViewInterceptor().handleWebDownload(webView.getContext(), iconUrl, title, downloadUrl);
        } else if (COMMAND_BOOT_APP.equalsIgnoreCase(command)) {
            String pname = args.getString("pn");
            if (TextUtils.isEmpty(pname)) {
                return null;
            }
            AppUtil.startApp(webView.getContext(), pname);
        } else if (COMMAND_SHOW_COLLECT.equalsIgnoreCase(command)) {
            boolean show = args.getBooleanValue("show");
            webView.getWebViewInterceptor().handleCollectVisible(show);
        } else if (COMMAND_SHOW_SHAREBUTTON.equalsIgnoreCase(command)) {
            boolean show = args.getBooleanValue("show");
            webView.getWebViewInterceptor().handleShareVisible(show);
        } else if (COMMAND_FORMAT_URL.equalsIgnoreCase(command)) {
            String url = args.getString("url");
            String callback = args.getString("cb");
            String ret = webView.getWebViewInterceptor().replacePlaceHolder(url);
            if (!TextUtils.isEmpty(callback)) {
                webView.getJsBridge().executeJavaScriptNow(String.format("%s(%s)", callback, ret), null);
            }
            return ret;
        } else if (COMMAND_SHOW_TODAY.equalsIgnoreCase(command)) {
            webView.getWebViewInterceptor().handleTodayVisible(args.getBooleanValue("show"));
        } else if (COMMAND_FULLSCREEN.equalsIgnoreCase(command)) {//全屏处理
            webView.enableFullScreen(Boolean.valueOf(args.getString("0")));
        }
        return null;
    }

    /**
     * 调用应用的日期选择器进行日期选择
     *
     * @param webView
     * @param command
     * @param args
     * @param cmdObj
     */
    private void pickDate(final CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        String dateStr = args.getString("0");
        final String tag = args.getString("1");
        webView.getWebViewInterceptor().onNativeDatePicker(dateStr, new ValueCallback<String>() {
            @Override
            public void onJavaScriptResult(String value) {
                JSONObject result = new JSONObject();
                result.put("value", value);
                result.put("tag", tag);
                webView.getJsBridge().executeJavaScriptNow(String.format("selectedDateFromNative(%s)", result.toJSONString()), null);
            }
        });
    }


    protected void finishHostActivity(CommonWebView webView) {
        if (webView.getWebViewInterceptor() != null) {
            webView.getWebViewInterceptor().finishActivity();
        }
    }

    /**
     * 处理App查询
     *
     * @param webView
     * @param args
     * @param cmdObj
     * @return
     */
    private Object handleQueryApp(CommonWebView webView, JSONObject args, JSONObject cmdObj) {
        final Context context = webView.getContext();
        if (args.isEmpty()) {
            return null;
        }
        String packageName = args.getString("0");
        String callback = args.getString("1");
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                if (!TextUtils.isEmpty(callback)) {
                    webView.getJsBridge().executeJavaScriptNow(String.format("%s('%s',%s)", callback, packageName, packageInfo.versionCode), null);
                }
                return packageInfo.versionCode;
            }
        } catch (Throwable e) {
        }
        if (!TextUtils.isEmpty(callback)) {
            webView.getJsBridge().executeJavaScriptNow(String.format("%s('%s',%s)", callback, packageName, 0), null);
        }
        return null;
    }


    /**
     * 文本复制
     *
     * @param webView
     * @param copyText
     */
    private Object handleCopyCommand(final CommonWebView webView, String copyText) {
        if (TextUtils.isEmpty(copyText)) {
            return false;
        }
        try {
            copyText = URLDecoder.decode(copyText, "utf-8");
            ClipboardManager clipboardManager = (ClipboardManager) webView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("text", copyText));
            webView.getJsBridge().executeJavaScriptNow("(function(){if(window._wnl_copy_callback){window._wnl_copy_callback(true);return 1;}return 0;})()", new ValueCallback<String>() {
                @Override
                public void onJavaScriptResult(String value) {
                    if ("1".equalsIgnoreCase(value)) {
                        return;
                    }
                    Toast.makeText(webView.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } catch (Throwable ignored) {
        }
        webView.getJsBridge().executeJavaScriptNow("(function(){if(window._wnl_copy_callback){window._wnl_copy_callback(false);return 1;}return 0;})()", new ValueCallback<String>() {
            @Override
            public void onJavaScriptResult(String value) {
                if ("1".equalsIgnoreCase(value)) {
                    return;
                }
                Toast.makeText(webView.getContext(), "复制失败", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }
}
