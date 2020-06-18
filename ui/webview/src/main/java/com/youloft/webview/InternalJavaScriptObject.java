package com.youloft.webview;


import android.util.Pair;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;
import com.youloft.webview.protocol.handler.CoreCommandHandler;

/**
 * 内转JavaScriptObject
 */
public class InternalJavaScriptObject {

    private CommonWebView mWebView;

    public InternalJavaScriptObject(CommonWebView webView) {
        this.mWebView = webView;
    }

    /**
     * 检查是否安装
     *
     * @param pn
     * @return
     */
    @JavascriptInterface
    public int checkInstall(String pn) {
        try {
            return (int) execute(CoreCommandHandler.COMMAND_QUERY_APP, new Pair<>("0", pn));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    @JavascriptInterface
    public String getUser() {
        try {
            return (String) execute(CoreCommandHandler.COMMAND_GET_USER);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取宽度
     *
     * @return
     */
    @JavascriptInterface
    public int getWidth() {
        try {
            return Math.round((float) execute(CoreCommandHandler.COMMAND_GET_WIDTH));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取高度
     *
     * @return
     */
    @JavascriptInterface
    public int getHeight() {
        try {
            return Math.round((int) execute(CoreCommandHandler.COMMAND_GET_HEIGHT));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 打开应用市场
     *
     * @param pn
     */
    @JavascriptInterface
    public void jumpMarket(String pn) {
        try {
            execute(CoreCommandHandler.COMMAND_JUMP_MARKET, new Pair<>("pn", pn), new Pair<>("dl", "true"));
        } catch (Throwable e) {
        }
    }


    /**
     * 跳转应用市场
     *
     * @param pn
     */
    @JavascriptInterface
    public void jumpMarket2(String pn) {
        try {
            execute(CoreCommandHandler.COMMAND_JUMP_MARKET, new Pair<>("pn", pn), new Pair<>("dl", "false"));
        } catch (Throwable e) {
        }
    }

    /**
     * 下载应用
     *
     * @param icon
     * @param title
     * @param url   todo验证实现
     */
    @JavascriptInterface
    public void downloadApk(String icon, String title, String url) {
        try {
            execute(CoreCommandHandler.COMMAND_DOWNLOAD_APK,
                    new Pair<>("icon", icon),
                    new Pair<>("title", title),
                    new Pair<>("url", url));
        } catch (Throwable e) {
        }
    }

    /**
     * 启动应用
     *
     * @param pn
     * @return
     */
    @JavascriptInterface
    public void bootApp(String pn) {
        try {
            if (!WebComponent.isActive(mWebView)) {
                return;
            }
            this.execute(CoreCommandHandler.COMMAND_BOOT_APP, new Pair<>("pn", pn));
        } catch (Throwable e) {
        }
    }

    /**
     * 获取对应Key的Value
     *
     * @param key
     * @return
     */
    @JavascriptInterface
    public String getDValue(String key) {
        try {
            return (String) this.execute(CoreCommandHandler.COMMAND_GET_DVALUE, new Pair<>("key", key));
        } catch (Throwable e) {
        }
        return "";
    }

    /**
     * 获取挖孔高度
     *
     * @return
     */
    @JavascriptInterface
    public int getNotchSize() {
        try {
            return (int) this.execute(CoreCommandHandler.COMMAND_GET_NOTCH_SIZE);
        } catch (Throwable e) {
            return 0;
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    @JavascriptInterface
    public int getStatusBarHeight() {
        try {
            return (int) this.execute(CoreCommandHandler.COMMAND_GET_STATUSBARHEIGHT);
        } catch (Throwable e) {
            return 0;
        }
    }

    /**
     * 开启分享
     *
     * @param show
     */
    @JavascriptInterface
    public void enableShare(boolean show) {
        try {
            if (!WebComponent.isActive(mWebView)) {
                mWebView.setStoreValue("pending_enableShare", show);
                return;
            }
            mWebView.clearStoreValue("pending_enableShare");
            this.execute(CoreCommandHandler.COMMAND_SHOW_SHAREBUTTON, new Pair<>("show", String.valueOf(show)));
        } catch (Throwable e) {
        }
    }

    /**
     * 启用收藏夹
     *
     * @param show
     */
    @JavascriptInterface
    public void enableCollect(boolean show) {

        try {
            if (!WebComponent.isActive(mWebView)) {
                mWebView.setStoreValue("pending_enableCollect", show);
                return;
            }
            mWebView.clearStoreValue("pending_enableCollect");
            this.execute(CoreCommandHandler.COMMAND_SHOW_COLLECT, new Pair<>("show", String.valueOf(show)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示今天
     *
     * @param show
     */
    @JavascriptInterface
    public void showToday(boolean show) {
        try {
            if (!WebComponent.isActive(mWebView)) {
                mWebView.setStoreValue("pending_showToday", show);
                return;
            }
            mWebView.clearStoreValue("pending_showToday");
            this.execute(CoreCommandHandler.COMMAND_SHOW_TODAY, new Pair<>("show", String.valueOf(show)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String formatUrlWithArgs(String url) {
        try {
            return (String) execute(CoreCommandHandler.COMMAND_FORMAT_URL, new Pair<>("url", url));
        } catch (Exception e) {
        }
        return url;
    }


    /**
     * 上报是否有返回历史的功能
     * <p>
     * 489开始不再使用
     *
     * @param webBack
     */
    @Deprecated
    @JavascriptInterface
    public void reportHasBack(boolean webBack) {

    }

    /**
     * 上报是否有Share
     * <p>
     * 489开始不再使用
     *
     * @param webShare
     */
    @Deprecated
    @JavascriptInterface
    public void reportHasShare(boolean webShare) {

    }

    /**
     * 具体执行逻辑
     *
     * @param command
     * @param args
     * @return
     */
    private Object execute(String command, Pair<String, String>... args) {
        JSONObject comObj = new JSONObject();
        comObj.put("command", command);
        if (args != null) {
            JSONObject argsObj = new JSONObject();
            for (Pair<String, String> arg : args) {
                argsObj.put(arg.first, arg.second);
            }
            comObj.put("args", argsObj);
        }

        return mWebView.getProtocolDispatcher().dispatchCommand(comObj);
    }

}
