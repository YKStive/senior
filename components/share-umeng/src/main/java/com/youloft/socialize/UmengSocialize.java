package com.youloft.socialize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youloft.socialize.auth.AuthListener;
import com.youloft.socialize.share.AbstractShareAction;
import com.youloft.socialize.share.UmengShareActionImpl;

import java.util.Map;

/**
 * 友盟实现有社交化
 */
public class UmengSocialize extends Socialize {

    @Override
    public AbstractShareAction share(Activity activity) {
        return new UmengShareActionImpl(activity);
    }

    @Override
    public void removeAuth(Activity activity, SOC_MEDIA media, final AuthListener listener) {
        getUMShare(activity).deleteOauth(activity, SHARE_MEDIA.convertToEmun(media.name()), new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (listener != null)
                    listener.onStart(SOC_MEDIA.from(share_media.name()));
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (listener != null)
                    listener.onComplete(SOC_MEDIA.from(share_media.name()), i, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (listener != null)
                    listener.onError(SOC_MEDIA.from(share_media.name()), i, throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (listener != null)
                    listener.onCancel(SOC_MEDIA.from(share_media.name()), i);
            }
        });
    }

    @NonNull
    private UMShareAPI getUMShare(Activity activity) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI umShareAPI = UMShareAPI.get(activity.getApplicationContext());
        umShareAPI.setShareConfig(config);
        return umShareAPI;
    }

    /**
     * 获取登陆信息
     *
     * @param activity
     * @param media
     * @param listener
     */
    @Override
    public void auth(final Activity activity, final SOC_MEDIA media, final AuthListener listener) {
        getUMShare(activity).getPlatformInfo(activity, SHARE_MEDIA.convertToEmun(media.name()), new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (listener != null)
                    listener.onStart(SOC_MEDIA.from(share_media.name()));
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (listener != null)
                    listener.onComplete(SOC_MEDIA.from(share_media.name()), i, map);
                if (Socialize.autoRemoveAuth) {
                    removeAuth(activity, media, null);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (listener != null)
                    listener.onError(SOC_MEDIA.from(share_media.name()), i, throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (listener != null)
                    listener.onCancel(SOC_MEDIA.from(share_media.name()), i);
            }
        });
    }

    @Override
    public boolean hasInstall(Activity context, SOC_MEDIA media) {
        return getUMShare(context).isInstall(context, SHARE_MEDIA.convertToEmun(media.name()));
    }


    /**
     * 回调
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }
}
