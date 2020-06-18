package com.youloft.socialize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.youloft.socialize.ShareBoard.ShareUIHandler;
import com.youloft.socialize.auth.AuthListener;
import com.youloft.socialize.share.AbstractShareAction;
import com.youloft.util.ToastMaster;

import java.util.ArrayList;
import java.util.List;

public abstract class Socialize {

    private static Socialize sInsatance = null;

    static Class<? extends ShareUIHandler> sUIClass;
    //登陆成功后自动删除三方授权
    public static boolean autoRemoveAuth = true;

    public static void setUIHandleClass(Class<? extends ShareUIHandler> uiHandleClas) {
        sUIClass = uiHandleClas;
    }

    /**
     * 获取一个实例
     *
     * @return
     */
    public static Socialize getIns() {
        if (sInsatance == null) {
            synchronized (Socialize.class) {
                try {
                    if (sInsatance == null) {
                        sInsatance = Class.forName("com.youloft.socialize.UmengSocialize").asSubclass(Socialize.class).newInstance();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return sInsatance;

    }

    /**
     * 分享
     *
     * @param activity
     * @return
     */
    public abstract AbstractShareAction share(Activity activity);


    /**
     * 有UI的分享
     *
     * @param activity
     * @return
     */
    public ShareBoard newShareBoard(Activity activity) {
        return new ShareBoard(activity);
    }


    /**
     * 获取授权组件
     *
     * @return
     */
    public abstract void auth(Activity activity, SOC_MEDIA media, AuthListener listener);

    public abstract void removeAuth(Activity activity, SOC_MEDIA media, final AuthListener listener);

    private static List<SOC_MEDIA> mediaList = new ArrayList<>();

    static {
        mediaList.add(SOC_MEDIA.QQ);
        mediaList.add(SOC_MEDIA.WEIXIN);
        mediaList.add(SOC_MEDIA.WEIXIN_CIRCLE);
        mediaList.add(SOC_MEDIA.QZONE);
        mediaList.add(SOC_MEDIA.SINA);
        mediaList.add(SOC_MEDIA.SMS);
        mediaList.add(SOC_MEDIA.EMAIL);
    }

    /**
     * 获取受支持的平台
     *
     * @return
     */
    public List<SOC_MEDIA> getSupportPlatform() {
        return mediaList;
    }

    /**
     * 检查平台是否安装
     *
     * @param platform
     * @return
     */
    public boolean checkPlatformInstall(Activity activity, SOC_MEDIA platform, boolean toastTip) {
        if (platform == null) {
            return true;
        }
        SOC_MEDIA installMedia = platform.getInstallMedia();
        if (installMedia == null) {
            return true;
        }
        if (!hasInstall(activity, installMedia)) {
            if (toastTip) {
                ToastMaster.showShortToast(activity, String.format("你没有安装最新版%s，请先下载并安装", platform.getPlatformName()));
            }
            return false;
        }
        return true;
    }

    /**
     * 是否安装
     * @param activity
     * @param media
     * @return
     */
    public abstract boolean hasInstall(Activity activity,SOC_MEDIA media);

    /**
     * 回调
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
    }
}
