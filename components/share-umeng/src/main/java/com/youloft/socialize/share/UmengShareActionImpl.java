package com.youloft.socialize.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMediaObject;
import com.youloft.socialize.SOC_MEDIA;

import java.net.URL;

import static com.youloft.socialize.share.ShareMarker.SHARETYPE_IMAGE;
import static com.youloft.socialize.share.ShareMarker.SHARETYPE_MINAPP;


/**
 * 分享的各平台实现
 */
public class UmengShareActionImpl extends AbstractShareAction {


    private ShareAction mActionImpl;


    public UmengShareActionImpl(Activity activity) {
        super(activity);
        mActionImpl = new ShareAction(activity);
    }

    @Override
    public AbstractShareAction platform(SOC_MEDIA platform) {
        if (!platform.isSelfHandlePlatform()) {
            mActionImpl.setPlatform(SHARE_MEDIA.convertToEmun(platform.name()));
        }
        return super.platform(platform);
    }

    @Override
    public AbstractShareAction text(String text) {
        mActionImpl.withText(text);
        return this;
    }

    @Override
    public AbstractShareAction subject(String subject) {
        mActionImpl.withSubject(subject);
        return this;

    }

    @Override
    public AbstractShareAction image(ShareImage image) {
        mActionImpl.withMedia(si2usi(image));
        return super.image(image);
    }


    @Override
    public AbstractShareAction web(ShareWeb web) {
        mActionImpl.withMedia(sw2usw(web));
        return super.web(web);
    }


    @Override
    public AbstractShareAction minApp(ShareMinApp app) {
        mActionImpl.withMedia(sma2umin(app));
        return super.minApp(app);
    }

    @Override
    protected boolean hasShareMarker() {
        if (mShareMarker != null
                && shareType == SHARETYPE_MINAPP
                && mActionImpl != null
                && mActionImpl.getPlatform() != SHARE_MEDIA.WEIXIN) {
            ShareContent shareContent = mActionImpl.getShareContent();
            if (shareContent != null && (shareContent.mMedia instanceof UMImage || shareContent.mMedia instanceof UMWeb)) {
                return mShareMarker.shouldMarker(mShareMedia, SHARETYPE_IMAGE);
            }
        }
        return super.hasShareMarker();

    }


    /**
     * 准备
     *
     * @param listener
     */
    @Override
    public void prepare(final PrepareListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (hasShareMarker()) {
                    try {
                        ShareContent shareData = mActionImpl.getShareContent();
                        Bitmap bmp;
                        if (shareData != null && shareData.mMedia instanceof UMImage && !isMarked(shareData.mMedia)) {
                            bmp = applyMarkWithResultBitmap(((UMImage) shareData.mMedia).asBitmap());
                            if (bmp != null) {
                                shareData.mMedia = makeUmImage(bmp);
                            }
                        } else if (shareData != null && shareData.mMedia instanceof UMMin && !isMarked(((UMMin) shareData.mMedia).getThumbImage()) && mActionImpl.getPlatform() == SHARE_MEDIA.WEIXIN) {
                            bmp = applyMarkWithResultBitmap(((UMMin) shareData.mMedia).getThumbImage().asBitmap());
                            if (bmp != null) {
                                ((UMMin) shareData.mMedia).setThumb(makeUmImage(bmp));
                            }
                        } else if (shareData != null && shareData.mMedia instanceof UMWeb && !isMarked(((UMWeb) shareData.mMedia).getThumbImage())) {
                            bmp = applyMarkWithResultBitmap(((UMWeb) shareData.mMedia).getThumbImage().asBitmap());
                            if (bmp != null) {
                                ((UMWeb) shareData.mMedia).setThumb(makeUmImage(bmp));
                            }
                        }

                    } catch (Throwable unused) {
                    }
                }
                final Object value = listener.onWorkThread();
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onMainThread(value);
                    }
                });
            }
        }).start();
    }


    @Override
    public void perform() {
        if (mShareMedia.isSelfHandlePlatform()) {
            performSelf();
            return;
        }
        prepare(new PrepareListener() {
            @Override
            public Object onWorkThread() {
                return null;
            }

            @Override
            public void onMainThread(Object value) {
                if (mActionImpl.getPlatform() == SHARE_MEDIA.SMS
                        || mActionImpl.getPlatform() == SHARE_MEDIA.EMAIL) {
                    ShareContent shareContent = mActionImpl.getShareContent();
                    UMediaObject mMedia = shareContent.mMedia;
                    if (mMedia instanceof UMWeb) {
                        mActionImpl.withText(((UMWeb) mMedia).getTitle() + "   \r\n" + mMedia.toUrl());
                        mActionImpl.withMedia(((UMWeb) mMedia).getThumbImage());
                    }
                }
                mActionImpl.share();
            }
        });
    }

    /**
     * 是否已经被打过水印
     *
     * @param mMedia
     * @return
     */
    private boolean isMarked(UMediaObject mMedia) {
        if (mMedia instanceof UMImage && ((UMImage) mMedia).getmExtra() != null) {
            return "true".equals(((UMImage) mMedia).getmExtra().get("_marker_xx"));
        }
        return false;
    }

    private UMImage makeUmImage(Bitmap bmp) {
        UMImage umImage = new UMImage(mActivity, bmp);
        umImage.setmExtra("_marker_xx", "true");
        return umImage;
    }

    @Override
    public String getShareUrl() {
        ShareContent shareContent = mActionImpl.getShareContent();
        if (shareContent.mMedia instanceof UMWeb) {
            return shareContent.mMedia.toUrl();
        }
        return "";
    }

    @Override
    public String getShareText() {
        ShareContent shareContent = mActionImpl.getShareContent();
        if (shareContent.mMedia instanceof UMWeb) {
            return ((UMWeb) shareContent.mMedia).getTitle();
        }
        return shareContent.mText;
    }

    @Override
    public Bitmap getShareImage() {
        UMImage shareResource = getShareResource();
        if (shareResource == null) {
            return null;
        }
        Bitmap bitmap = shareResource.asBitmap();
        if (bitmap != null) {
            return bitmap;
        }
        String s = shareResource.asUrlImage();
        if (!TextUtils.isEmpty(s)) {
            try {
                return BitmapFactory.decodeStream(new URL(s).openStream());
            } catch (Throwable e) {
            }
        }
        return null;
    }


    public UMImage getShareResource() {
        ShareContent shareContent = mActionImpl.getShareContent();
        if (shareContent.mMedia instanceof UMWeb) {
            return ((UMWeb) shareContent.mMedia).getThumbImage();
        } else if (shareContent.mMedia instanceof UMImage) {
            return ((UMImage) shareContent.mMedia);
        }
        return null;
    }


    @Override
    public AbstractShareAction eventTracker(final ShareEventTracker tracker) {
        super.eventTracker(tracker);
        mActionImpl.setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                tracker.onStart(share_media.name());
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                tracker.onResult(share_media.name());
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                tracker.onError(share_media.name(), throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                tracker.onCancel(share_media.name());
            }
        });
        return this;
    }


    private static UMImage si2usi(ShareImage image) {
        if (image == null) {
            return null;
        }
        UMImage umImage = null;
        switch (image.type) {
            case ShareImage.TYPE_URL:
                umImage = new UMImage(image.context, image.imageUrl);
                break;
            case ShareImage.TYPE_BITMAP:
                umImage = new UMImage(image.context, image.bitmap);
                break;
            case ShareImage.TYPE_RESOURCE:
                umImage = new UMImage(image.context, image.resoueceId);
                break;
            case ShareImage.TYPE_FILE:
                umImage = new UMImage(image.context, image.file);
                break;
            default:
                break;
        }
        if (umImage == null) {
            return null;
        }
        try {
            if (image.thumb != null) {
                UMImage thumb = si2usi(image.thumb);
                if (thumb != null) {
                    umImage.setThumb(thumb);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (image.quality) {
            umImage.compressStyle = UMImage.CompressStyle.QUALITY;
        }
        return umImage;
    }

    /**
     * 网页分享
     *
     * @param web
     * @return
     */
    private static UMWeb sw2usw(ShareWeb web) {
        if (null == web) {
            return null;
        }
        UMWeb umWeb = new UMWeb(web.url);
        umWeb.setDescription(web.description);
        umWeb.setTitle(web.title);
        umWeb.setThumb(si2usi(web.thumb));
        return umWeb;
    }

    /**
     * 小程序分享参数转换
     *
     * @param app
     * @return
     */
    private static UMMin sma2umin(ShareMinApp app) {
        if (null == app) {
            return null;
        }
        UMMin umMin = new UMMin(app.url);
        //兼容低版本的网页链接
        umMin.setThumb(si2usi(app.thumb));
        // 小程序消息封面图片
        umMin.setTitle(app.title);
        // 小程序消息title
        umMin.setDescription(app.text);
        // 小程序消息描述
        umMin.setPath(app.path);
        //小程序页面路径
        umMin.setUserName(app.userName);
        // 小程序原始id,在微信平台查询
        return umMin;
    }
}
