package com.youloft.socialize.auth;

import com.youloft.socialize.SOC_MEDIA;

import java.util.Map;

/**
 * 验证回调
 */
public interface AuthListener {

    /**
     * @param platform 平台名称
     */
    void onStart(SOC_MEDIA platform);

    /**
     * @param platform 平台名称
     * @param action   行为序号，开发者用不上
     * @param data     用户资料返回
     */
    void onComplete(SOC_MEDIA platform, int action, Map<String, String> data);

    /**
     * @param platform 平台名称
     * @param action   行为序号，开发者用不上
     * @param t        错误原因
     */
    void onError(SOC_MEDIA platform, int action, Throwable t);

    /**
     * @param platform 平台名称
     * @param action   行为序号，开发者用不上
     */
    void onCancel(SOC_MEDIA platform, int action);
}