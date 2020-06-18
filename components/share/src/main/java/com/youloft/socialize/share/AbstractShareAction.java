package com.youloft.socialize.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.youloft.socialize.SOC_MEDIA;
import com.youloft.socialize.web.WebShareHandler;
import com.youloft.util.ToastMaster;

import java.util.Map;

import static com.youloft.socialize.share.ShareMarker.SHARETYPE_IMAGE;
import static com.youloft.socialize.share.ShareMarker.SHARETYPE_MINAPP;
import static com.youloft.socialize.share.ShareMarker.SHARETYPE_UNKNOW;

/**
 * 设置分享
 */
public abstract class AbstractShareAction {


    protected Activity mActivity;

    public AbstractShareAction(Activity activity) {
        this.mActivity = activity;
    }

    protected SOC_MEDIA mShareMedia = null;

    public AbstractShareAction platform(SOC_MEDIA platform) {
        mShareMedia = platform;
        return this;
    }

    protected ShareEventTracker mTracker;

    public AbstractShareAction eventTracker(ShareEventTracker tracker) {
        this.mTracker = tracker;
        return this;
    }

    public abstract AbstractShareAction text(String text);

    public abstract AbstractShareAction subject(String desc);

    public AbstractShareAction image(ShareImage image) {
        this.shareType = SHARETYPE_IMAGE;
        return this;
    }

    public AbstractShareAction web(ShareWeb web) {
        this.shareType = SHARETYPE_IMAGE;
        return this;
    }

    public AbstractShareAction minApp(ShareMinApp app) {
        this.shareType = SHARETYPE_MINAPP;
        return this;
    }

    public abstract void perform();

    protected ShareMarker mShareMarker = null;

    protected int shareType = SHARETYPE_UNKNOW;

    public AbstractShareAction setShareMarker(ShareMarker mark) {
        this.mShareMarker = mark;
        return this;
    }

    /**
     * 是否有分享水印
     *
     * @return
     */
    protected boolean hasShareMarker() {
        return mShareMarker != null && shareType != SHARETYPE_UNKNOW && mShareMarker.shouldMarker(mShareMedia, shareType);
    }

    public interface PrepareListener {

        Object onWorkThread();

        void onMainThread(Object value);
    }

    public abstract void prepare(PrepareListener listener);

    /**
     * 附加相应的Ｍａｒｋ
     *
     * @param bitmap
     */
    protected Bitmap applyMarkWithResultBitmap(Bitmap bitmap) {
        if (this.mShareMarker != null) {
            return this.mShareMarker.onAttachShareMarker(bitmap);
        }
        return bitmap;
    }

    /**
     * 为系统分享提供分享文本
     *
     * @return
     */
    public abstract String getShareText();

    /**
     * 系统分享链接处理
     *
     * @return
     */
    public abstract String getShareUrl();


    /**
     * 为系统分享提供分享图片
     *
     * @return
     */
    @WorkerThread
    public abstract Bitmap getShareImage();

    protected Map<String, String> mExtraData;

    public AbstractShareAction setExtra(Map<String, String> extra) {
        this.mExtraData = extra;
        return this;
    }

    /**
     * 分享自处理
     */
    public void performSelf() {
        if (mTracker != null) {
            mTracker.onStart(mShareMedia.toString());
        }
        if (mShareMedia == SOC_MEDIA.COPY) {
            //处理复制
            handleCopyShare(this, mTracker, mExtraData);
        } else if (mShareMedia == SOC_MEDIA.SYSTEM) {
            //调起系统分享
            handleSystemShare(this, mTracker, mExtraData);
        } else {
            //其它未知分享
            if (mTracker != null) {
                mTracker.onError(mShareMedia.toString(), new RuntimeException("未知平台"));
            }

        }
    }


    /**
     * 处理复制分享
     *
     * @param content
     * @param tracker
     * @param extra
     */
    private void handleCopyShare(AbstractShareAction content, ShareEventTracker tracker, Map<String, String> extra) {
        try {
            ClipboardManager myClipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            String url = content.getShareUrl();
            if (extra != null) {
                if (TextUtils.isEmpty(url)) {
                    url = extra.get("url");
                }
                if (TextUtils.isEmpty(url)) {
                    url = extra.get("default_url");
                }
            }
            ClipData myClip = ClipData.newPlainText("text", url);
            myClipboard.setPrimaryClip(myClip);
            ToastMaster.showShortToast(mActivity, "成功复制到剪贴板");
            if (tracker != null) {
                tracker.onResult(SOC_MEDIA.COPY.toString());
            }
        } catch (Throwable ignored) {
            if (tracker != null) {
                tracker.onError(SOC_MEDIA.COPY.toString(), new RuntimeException("内部错误"));
            }
        }

    }

    /**
     * 处理系统分享
     *
     * @param content
     * @param tracker
     * @param extra
     */
    private void handleSystemShare(final AbstractShareAction content, final ShareEventTracker tracker, Map<String, String> extra) {
        try {
            content.prepare(new PrepareListener() {
                @Override
                public Object onWorkThread() {
                    return content.getShareImage();
                }

                @Override
                public void onMainThread(Object value) {
                    WebShareHandler.systemShare(mActivity, (Bitmap) value, content.getShareText(), content.getShareUrl());
                    if (tracker != null) {
                        tracker.onResult(SOC_MEDIA.SYSTEM.toString());
                    }
                }
            });
        } catch (Throwable ignored) {
            if (tracker != null) {
                tracker.onError(SOC_MEDIA.SYSTEM.toString(), new RuntimeException("内部错误"));
            }
        }

    }


}
