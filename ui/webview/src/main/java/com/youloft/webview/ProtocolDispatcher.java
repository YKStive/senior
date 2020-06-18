package com.youloft.webview;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 协议分发
 */
public class ProtocolDispatcher {

    private static LinkedHashMap<String, Class<? extends AbstractProtocolHandler>> sGlobalHandle = new LinkedHashMap<>();
    private CommonWebView mWebView;

    public ProtocolDispatcher(CommonWebView webView) {
        this.mWebView = webView;
    }

    /**
     * 注册协议处理器
     *
     * @param protocol
     * @param clz
     */
    public static void registerProtocolHandle(String protocol, Class<? extends AbstractProtocolHandler> clz) {
        if (sGlobalHandle.containsKey(protocol)) {
            sGlobalHandle.remove(protocol);
        }
        sGlobalHandle.put(protocol, clz);
    }

    /**
     * 反注册协议处理器
     *
     * @param clz
     */
    public static void unregisterProtocolHandle(Class<? extends AbstractProtocolHandler> clz) {
        sGlobalHandle.remove(clz);
    }


    private HashMap<String, AbstractProtocolHandler> handlerHashMap = new HashMap<>();

    private AbstractProtocolHandler obtainProtocolHandler(String protocol) {
        AbstractProtocolHandler abstractProtocolHandler = handlerHashMap.get(protocol);
        if (abstractProtocolHandler == null) {
            Class<? extends AbstractProtocolHandler> aClass = sGlobalHandle.get(protocol);
            try {
                abstractProtocolHandler = aClass.newInstance();
                abstractProtocolHandler.init(mWebView);
                handlerHashMap.put(protocol, abstractProtocolHandler);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return abstractProtocolHandler;
    }

    /**
     * 接收Activity回调
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        for (AbstractProtocolHandler abstractProtocolHandler : handlerHashMap.values()) {
            if (abstractProtocolHandler != null) {
                abstractProtocolHandler.onActivityResult(activity, requestCode, resultCode, data);
            }
        }
    }


    /**
     * 处理Command
     *
     * @param command
     * @return
     */
    public Object dispatchCommand(JSONObject command) {
        String protocol = command.getString("schema");
        if (TextUtils.isEmpty(protocol)) {
            protocol = "protocol";
        }
        AbstractProtocolHandler abstractProtocolHandler = obtainProtocolHandler(protocol);
        if (abstractProtocolHandler != null) {
            return abstractProtocolHandler.handleCommand(command);
        }
        return "-unsupport-";
    }

    /**
     * 分发urlloading
     *
     * @param view
     * @param url
     * @return
     */
    public boolean dispatchUrlLoading(WebView view, final String url) {
        if (canHandleProtocol(url)) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    dispatchCommand(parseUrl2Cmd(url, "url"));
                }
            });
            return true;
        }

        return false;
    }

    private boolean canHandleProtocol(String url) {
        return url.startsWith("protocol://");
    }

    /**
     * 转换url到cmd
     *
     * @param url
     * @param method
     * @return
     */
    @VisibleForTesting
    static JSONObject parseUrl2Cmd(String url, String method) {
        int schemaIndex = url.indexOf("://");
        String schema = url.substring(0, schemaIndex);
        String commandString = url.substring(schemaIndex + 3);
        String command = "", argString = "";
        int indexArr[] = new int[]{commandString.indexOf("?"), commandString.indexOf(":"), commandString.indexOf("#")};
        Arrays.sort(indexArr);
        int argIndex = 0;
        for (int i = 0; i < indexArr.length; i++) {
            if (indexArr[i] < 0) {
                continue;
            }
            argIndex = indexArr[i];
            break;
        }
        if (argIndex > 0) {
            command = commandString.substring(0, argIndex);
            argString = commandString.substring(argIndex + 1);
        } else {
            command = commandString;
        }
        JSONObject args = null;

        try {
            boolean isJsonBody = false;

            try {
                String guessString = URLDecoder.decode(argString);
                if (!TextUtils.isEmpty(guessString)) {
                    isJsonBody = guessString.startsWith("{") && guessString.endsWith("}");
                }
            }catch (Throwable ignored){}

            //1.尝试原文进行拆分进行参数拆分
            if (!TextUtils.isEmpty(argString)
                    && commandString.contains("?")
                    && argString.contains("=")
                    && !isJsonBody) {
                args = new JSONObject();
                String[] argGroup = argString.contains("&") ? argString.split("&") : new String[]{argString};
                for (String argItem : argGroup) {
                    int eqIndex = argItem.indexOf("=");
                    if (eqIndex < 1 || eqIndex > argItem.length()) {
                        continue;
                    }
                    args.put(argItem.substring(0, eqIndex), eqIndex == argItem.length() ? "" : argItem.substring(eqIndex + 1));
                }
            } else {
                //2.对所有参数文本进行urldecode
                final String aString = URLDecoder.decode(argString, "utf-8");
                try {
                    //2.1尝试对为json的参数进行处理
                    args = JSONObject.parseObject(aString);
                } catch (Exception e) {
                }

                if (null == args || args.isEmpty()) {

                    //2.2 处理对urldecode后的文本进行&=的拆分
                    if (!TextUtils.isEmpty(aString)
                            && commandString.contains("?")
                            && aString.contains("=")) {
                        args = new JSONObject();
                        String[] argGroup = argString.contains("&") ? argString.split("&") : new String[]{argString};
                        for (String argItem : argGroup) {
                            int eqIndex = argItem.indexOf("=");
                            if (eqIndex < 1 || eqIndex > argItem.length()) {
                                continue;
                            }
                            args.put(argItem.substring(0, eqIndex), eqIndex == argItem.length() ? "" : argItem.substring(eqIndex + 1));
                        }
                    } else if (!TextUtils.isEmpty(aString)
                            && aString.contains("#")) {
                        //2.3对全为＃号的参数进行拆分以０，１，２，３这样的字串进行填充
                        args = new JSONObject();
                        String[] split = aString.split("#");
                        for (int i = 0; i < split.length; i++) {
                            args.put(String.valueOf(i), split[i]);
                        }
                    } else {
                        //2.4如果全都失败则使用argString
                        args = new JSONObject();
                        args.put("0", aString);
                    }
                }
            }
        } catch (Throwable e) {

        }
        JSONObject cmdJson = new JSONObject();
        cmdJson.put("command", command);
        cmdJson.put("args", args);
        cmdJson.put("argString", argString);
        cmdJson.put("schema", schema);
        cmdJson.put("_cmd", commandString);
        if (!TextUtils.isEmpty(method)) {
            cmdJson.put("_method", method);
        }
        return cmdJson;
    }

}
