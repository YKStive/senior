package com.youloft.senior.web;

import com.alibaba.fastjson.JSONObject;
import com.youloft.socialize.web.WebShareHandler;
import com.youloft.webview.CommonWebView;
import com.youloft.webview.protocol.MultiCommandProtocolHandler;

/**
 * @author xll
 * @date 2018/9/11 17:34
 */
public class WebComponentHandle extends MultiCommandProtocolHandler {
    @Override
    public void initProtocolHandler(CommonWebView webView) {
//        addCommandHandler(new WebRecorderHandle());
//        addCommandHandler(new PayWebHandler());
        addCommandHandler(WebShareHandler.getInstance());
//        addCommandHandler(new WnlSSOHandle());
//        addCommandHandler(new ContentWebCommandHandler());
//        addCommandHandler(new FileSelectorCommandHandler());
//        addCommandHandler(new ADWebCommandHandler());
    }

    @Override
    public Object handleCommand(JSONObject command) {
        return super.handleCommand(command);
    }
}
