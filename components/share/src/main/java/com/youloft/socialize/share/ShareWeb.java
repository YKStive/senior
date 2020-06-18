package com.youloft.socialize.share;

/**
 * 网页分享
 *
 * @author coder
 */
public class ShareWeb {
    String url;
    String description;
    String title;
    ShareImage thumb;

    /**
     * 构造方法
     *
     * @param url
     */
    public ShareWeb(String url) {
        this.url = url;
    }

    /**
     * 网页分享标题
     *
     * @param title
     * @return
     */
    public ShareWeb setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 网页分享缩略图
     *
     * @param image
     * @return
     */
    public ShareWeb setThumb(ShareImage image) {
        this.thumb = image;
        return this;
    }

    /**
     * 网页分享描述
     *
     * @param description
     * @return
     */
    public ShareWeb setDescription(String description) {
        this.description = description;
        return this;
    }
}
