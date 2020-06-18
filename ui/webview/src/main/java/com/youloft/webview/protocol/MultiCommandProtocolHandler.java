package com.youloft.webview.protocol;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.youloft.webview.AbstractProtocolHandler;
import com.youloft.webview.CommonWebView;
import com.youloft.webview.protocol.handler.AbstractCommandHandler;
import com.youloft.webview.protocol.handler.CoreCommandHandler;

import java.util.LinkedList;

/**
 * 命令处理
 */
public abstract class MultiCommandProtocolHandler extends AbstractProtocolHandler {

    private LinkedList<AbstractCommandHandler> handlerLinkedList = new LinkedList<>();

    @Override
    protected void init(CommonWebView webView) {
        super.init(webView);
        addCommandHandler(new CoreCommandHandler());
        initProtocolHandler(webView);
    }

    public abstract void initProtocolHandler(CommonWebView webView);

    /**
     * 添加命令处理器
     *
     * @param handler
     */
    public void addCommandHandler(AbstractCommandHandler handler) {
        handlerLinkedList.add(handler);
    }

    /**
     * 添加命令处理器
     *
     * @param index
     * @param handler
     */
    public void insertCommandHandler(int index, AbstractCommandHandler handler) {
        handlerLinkedList.add(index, handler);
    }


    @Override
    public Object handleCommand(JSONObject command) {
        String cmd = command.getString("command");
        JSONObject args = command.getJSONObject("args");
        for (AbstractCommandHandler cmdHandle : handlerLinkedList) {
            if (cmdHandle != null && cmdHandle.accept(webView, cmd, args, command)) {
                return cmdHandle.handleCommand(webView, cmd, args, command);
            }
        }
        return webView.getWebViewInterceptor().handleAppCommand(webView, cmd, args, command);
    }

    @Override
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        for (AbstractCommandHandler cmdHandle : handlerLinkedList) {
            if (cmdHandle.onActivityResult(activity, requestCode, resultCode, data)) {
                return true;
            }
        }
        return webView.getWebViewInterceptor().onActivityResult(activity,requestCode,resultCode,data);
    }
}
