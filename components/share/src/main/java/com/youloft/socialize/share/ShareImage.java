package com.youloft.socialize.share;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

/**
 * 分享图片
 */
public class ShareImage {

    public static final int TYPE_URL = 0;
    public static final int TYPE_BITMAP = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_FILE = 1;

    ShareImage thumb;
    int type;
    String imageUrl;
    File file;
    Bitmap bitmap;
    Context context;
    int resoueceId;
    boolean quality;

    /**
     * 分享网络图片
     *
     * @param context
     * @param imageUrl
     */
    public ShareImage(Context context, String imageUrl) {
        this.imageUrl = imageUrl;
        this.context = context;
        this.type = 0;
    }

    /**
     * 分享文件
     *
     * @param context
     * @param file
     */
    public ShareImage(Context context, File file) {
        this.context = context;
        this.file = file;
        this.type = 1;
    }

    public ShareImage(Context context, int resourceId) {
        this.context = context;
        this.resoueceId = resourceId;
        this.type = 2;
    }

    public ShareImage(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
        this.type = 3;
    }

    public ShareImage setThumb(ShareImage thumb) {
        this.thumb = thumb;
        return this;
    }

    public ShareImage setQuality() {
        quality = true;
        return this;
    }

}
