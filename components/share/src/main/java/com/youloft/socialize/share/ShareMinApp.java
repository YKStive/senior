package com.youloft.socialize.share;

/**
 * 小程序分享实体
 */
public class ShareMinApp {
    String url;
    ShareImage thumb;
    String title;
    String text;
    String path;
    String userName;

    /**
     * 构造函数
     * @param url 网页地址为了兼容不支持小程序的版本
     */
    public ShareMinApp(String url) {
        this.url = url;
    }

    /**
     * 小程序缩略图
     * @param image
     * @return
     */
    public ShareMinApp setThumb(ShareImage image) {
        this.thumb = image;
        return this;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public ShareMinApp setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置描述
     * @param description
     * @return
     */
    public ShareMinApp setDescription(String description) {
        this.text = text;
        return this;
    }

    /**
     * 设置小程序路径
     * @param path
     * @return
     */
    public ShareMinApp setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * 设置小程序用户名
     * @param userName
     * @return
     */
    public ShareMinApp setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
