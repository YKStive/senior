package com.youloft.senior.coin;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.youloft.senior.base.App;

import java.lang.reflect.Field;

/**
 * @author xll
 * @date 2020/6/22 19:12
 */
public class TTRewardManager {
    static {
        TTAdSdk.init(App.Companion.instance(),
                new TTAdConfig.Builder()
                        .appId("5000975")
                        .appName("万年历")
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_NO_TITLE_BAR)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(false) //是否在锁屏场景支持展示广告落地页
                        .useTextureView(true)
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_MOBILE) //允许直接下载的网络状态集合
                        .supportMultiProcess(false) //是否支持多进程，true支持
                        .build());
    }

    public static void requestReword(Activity context, String posId, RewardListener listener, JSONObject extra) {
        AdSlot.Builder builder = new AdSlot.Builder()
                .setCodeId(posId)
                .setSupportDeepLink(true)
                .setAdCount(1)
                .setImageAcceptedSize(1080, 1920);

        if (extra != null) {
            builder.setRewardName(extra.getString("rewardName"))
                    .setRewardAmount(extra.getIntValue("rewardAmount"))
                    .setUserID(extra.getString("uid"))
                    .setMediaExtra(extra.getString("media_extra"));
        } else {
            builder.setRewardName("")
                    .setRewardAmount(1);
        }
        TTAdSdk.getAdManager().createAdNative(context).loadRewardVideoAd(builder.build(), new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                listener.notifyRewardResult(false, false, extra);
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    private boolean hasSendResult;

                    @Override
                    public void onAdShow() {
                    }

                    @Override
                    public void onAdVideoBarClick() {
                    }

                    @Override
                    public void onAdClose() {
                        if (!hasSendResult) {
                            listener.notifyRewardResult(true, false, extra);
                            hasSendResult = true;
                        }

                    }

                    @Override
                    public void onVideoComplete() {

                    }

                    @Override
                    public void onVideoError() {

                    }

                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        if (!hasSendResult) {
                            hasSendResult = true;
                            listener.notifyRewardResult(true, rewardVerify, extra);
                        }

                    }

                    @Override
                    public void onSkippedVideo() {
                    }
                });
                ttRewardVideoAd.setShowDownLoadBar(true);
                try {
                    Field g = ttRewardVideoAd.getClass().getDeclaredField("g");
                    g.setAccessible(true);
                    g.setBoolean(ttRewardVideoAd, false);
                } catch (Throwable e) {
                }
                ttRewardVideoAd.showRewardVideoAd(context);
            }

            @Override
            public void onRewardVideoCached() {

            }
        });
    }
}
