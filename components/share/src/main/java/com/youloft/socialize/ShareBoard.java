package com.youloft.socialize;

import android.app.Activity;
import android.util.Log;

import com.youloft.socialize.share.AbstractShareAction;
import com.youloft.socialize.share.ShareEventTracker;
import com.youloft.socialize.share.ShareMarker;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象的分享菜单
 * <p>
 * <p>
 * WNL496处理：
 * 1.增加COPY&System
 * 对于COPY如果分享没有配置渠道则使用EMAIL，关于ShareUrl的使用 渠道节点中的url->使用Extra:url->Extra:defaultUrl
 */
public class ShareBoard {

    protected Activity mActivity;

    public ShareBoard(Activity activity) {
        this.mActivity = activity;
    }

    protected AbstractShareAction mDefaultAction;

    protected HashMap<SOC_MEDIA, AbstractShareAction> mActionMap;

    protected ShareEventTracker mEventTracker;

    protected Map<String, String> extra = new HashMap<>();


    /**
     * 新建一个Action并附加到当前Board
     *
     * @param soc_media
     * @return
     */
    protected AbstractShareAction newActionWithPlatform(SOC_MEDIA soc_media) {
        AbstractShareAction share = Socialize.getIns().share(mActivity);
        if (null == soc_media) {
            this.mDefaultAction = share;
        } else {
            if (null == mActionMap) {
                mActionMap = new HashMap<>();
            }
            mActionMap.put(soc_media, share);
        }
        return share;
    }

    /**
     * 添加额外参数
     *
     * @param key
     * @param data
     */
    public void putExtra(String key, String data) {
        extra.put(key, data);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public String getExtra(String key) {
        return extra.get(key);
    }

    /**
     * 默认分享动作
     */
    public AbstractShareAction withDefault() {
        return newActionWithPlatform(null);
    }

    /**
     * 设置平台分享内容
     *
     * @param media
     */
    public AbstractShareAction withPlatform(SOC_MEDIA media) {
        return newActionWithPlatform(media);
    }

    private ShareMarker mMarker;

    /**
     * 设置分享回调的Ｍａｒｋｅｒ
     *
     * @param imageMarker
     * @return
     */
    public ShareBoard setShareMarker(ShareMarker imageMarker) {
        this.mMarker = imageMarker;
        return this;
    }


    public AbstractShareAction peekAction(SOC_MEDIA soc_media) {
        AbstractShareAction action = (null == mActionMap || null == mActionMap.get(soc_media)) ? mDefaultAction : mActionMap.get(soc_media);
        if (action != null && this.mMarker != null) {
            action.setShareMarker(mMarker);
        }
        return action;
    }

    /**
     * 分享指定Platform
     *
     * @param soc_media
     */
    public void shareWithPlatform(SOC_MEDIA soc_media) {
        if (mActivity == null) {
            return;
        }
        if (!Socialize.getIns().checkPlatformInstall(mActivity, soc_media, true)) {
            return;
        }
        AbstractShareAction action = peekAction(soc_media);
        if (action == null) {
            if (soc_media.isSelfHandlePlatform()) {
                action = peekAction(SOC_MEDIA.EMAIL);
            }
        }

        if (action == null) {
            return;
        }
        if (mEventTracker != null) {
            action.eventTracker(mEventTracker);
        }
        action.setExtra(extra)
                .platform(soc_media)
                .perform();
    }


    /**
     * 事件跟踪
     *
     * @param tracker
     */
    public ShareBoard setEventTracker(ShareEventTracker tracker) {
        this.mEventTracker = tracker;
        return this;
    }

    /**
     * 使用XXUI进行分享
     */
    public void shareWithUI() {
        if (Socialize.sUIClass == null) {
            return;
        }
        try {
            Socialize.sUIClass.newInstance().showShareUI(mActivity, this);
        } catch (Throwable e) {
            Log.e("Socialize", "没有实现UI分享器");
        }
    }


    /**
     * 分享UI处理器
     */
    public interface ShareUIHandler {
        void showShareUI(Activity activity, ShareBoard board);
    }
}
