package com.youloft.senior.coin;

import com.alibaba.fastjson.JSONObject;

/**
 * 激励视频回调
 */
public abstract class RewardListener {

    public static final String EVENT_SKIP = "Skip";
    public static final String EVENT_CLOSE = "OFF";
    public static final String EVENT_CLICK = "CK";
    public static final String EVENT_EXPOSED = "IM";
    public static final String EVENT_LOAD_SUCCESS = "Req.S";
    public static final String EVENT_LOAD_ERR = "Req.F";
    public static final String EVENT_LOAD_REQ = "Req";
    public static final String EVENT_VIEW_SUCCESS = "Success";

    /**
     * reward 结果处理
     *
     * @param isSuccess
     * @param reward
     * @param args
     */
    public abstract void onRewardResult(boolean isSuccess, boolean reward, JSONObject args);

    /**
     * 通知 Reward 结果
     *
     * @param reward
     * @param args
     */
    public void notifyRewardResult(boolean isSuccess, boolean reward, JSONObject args) {
        onRewardResult(isSuccess, reward, args);
    }

    public final void emmitEvent(String platform, String appId, String posId, String eventName, JSONObject args) {
        onEvent(platform, appId, posId, eventName, args);
    }

    /**
     * 事件上报
     *
     * @param platform
     * @param appId
     * @param posId
     * @param eventName
     * @param args
     */
    protected void onEvent(String platform, String appId, String posId, String eventName, JSONObject args) {

    }

}
