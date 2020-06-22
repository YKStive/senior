package com.youloft.senior.web;

import android.app.Activity;

/**
 * @author xll
 * @date 2018/9/13 13:59
 */
public interface WebCallBack {


    /**
     * 返回activity
     *
     * @return
     */
    Activity getActivity();

    /**
     * 下载完是否需要关闭
     *
     * @return
     */
    boolean needClose();

    /**
     * 处理分享
     */
    void openShare();

    /**
     * 是否有返回
     *
     * @return
     */
    boolean isBack();

    /**
     * page加载完成
     */
    void onPageFinish();
}
