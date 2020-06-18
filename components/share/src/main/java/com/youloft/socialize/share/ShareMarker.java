package com.youloft.socialize.share;

import android.graphics.Bitmap;

import com.youloft.socialize.SOC_MEDIA;

/**
 * 分享Mark
 */
public abstract class ShareMarker {

    public static final int SHARETYPE_UNKNOW = 0;
    public static final int SHARETYPE_IMAGE = 1;
    public static final int SHARETYPE_WEB = 2;
    public static final int SHARETYPE_MINAPP = 3;

    /**
     * 是否需要附加水印
     *
     * @param platform
     * @param shareType  分享类型{
     * @link ShareMarker#SHARETYPE_UNKNOW  未知类型
     * @link ShareMarker#SHARETYPE_IMAGE　　图片分享
     * @link ShareMarker#SHARETYPE_WEB　　　网页分享
     * @link ShareMarker#SHARETYPE_MINAPP　　小程序分享
     * }
     * @return
     */
    public boolean shouldMarker(SOC_MEDIA platform, int shareType) {
        return true;
    }

    /**
     * 开始附加水印
     *
     * @param bitmap
     * @return
     */
    public abstract Bitmap onAttachShareMarker(Bitmap bitmap);
}
