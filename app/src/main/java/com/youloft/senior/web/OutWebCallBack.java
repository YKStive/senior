package com.youloft.senior.web;

import com.youloft.webview.WebComponent;

/**
 * 在web fragment外部使用，用于web frament与外面沟通
 * 如在web Activity实现此接口
 *
 * @author xll
 * @date 2018/9/17 13:48
 */
public interface OutWebCallBack {
    /**
     * 创建WebUIHelper,并初始化值
     *
     * @param callBack
     * @param webComponent
     * @return
     */
    WebUIHelper createUIHelper(WebCallBack callBack, WebComponent webComponent);

    /**
     * 打开分享
     */
    void openShare();

    /**
     * 下载完毕是否需要关闭
     *
     * @return
     */
    boolean needClose();

    /**
     * 页面加载完成
     */
    void onPageFinish();
}
