package com.youloft.webview.protocol.handler;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.youloft.webview.CommonWebView;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 抽象命令处理器
 */
public abstract class AbstractCommandHandler {


    protected HashSet<String> mSupportCommand = new HashSet<>();

    protected void registeCommand(String command) {
        mSupportCommand.add(command);
    }

    protected void registeCommand(String[] command) {
        mSupportCommand.addAll(Arrays.asList(command));
    }

    /**
     * 是否可以处理这个命令
     *
     * @param command
     * @param args
     * @param cmdObj
     * @return
     */
    public boolean accept(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj) {
        return mSupportCommand.contains(command);
    }

    public abstract Object handleCommand(CommonWebView webView, String command, JSONObject args, JSONObject cmdObj);

    /**
     * Activity结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        return false;
    }
}
