package com.youloft.socialize;

public enum SOC_MEDIA {
    SMS("短信"),
    EMAIL("邮件"),
    SINA("新浪微博"),
    QZONE("QQ"),
    QQ("QQ"),
    WEIXIN("微信"),
    WEIXIN_CIRCLE("微信"),
    WEIXIN_FAVORITE("微信"),
    TENCENT("腾讯微博"),
    COPY("复制"),
    SYSTEM("系统分享");

    String platform;

    SOC_MEDIA(String platform) {
        this.platform = platform;
    }

    public static SOC_MEDIA from(String s) {
        if ("SMS".equalsIgnoreCase(s)) {
            return SOC_MEDIA.SMS;
        }
        if ("EMAIL".equalsIgnoreCase(s)) {
            return SOC_MEDIA.EMAIL;
        }
        if ("SINA".equalsIgnoreCase(s)) {
            return SOC_MEDIA.SINA;
        }
        if ("QZONE".equalsIgnoreCase(s)) {
            return SOC_MEDIA.QZONE;
        }
        if ("QQ".equalsIgnoreCase(s)) {
            return SOC_MEDIA.QQ;
        }
        if ("WEIXIN".equalsIgnoreCase(s)) {
            return SOC_MEDIA.WEIXIN;
        }
        if ("WEIXIN_CIRCLE".equalsIgnoreCase(s)) {
            return SOC_MEDIA.WEIXIN_CIRCLE;
        }
        if ("WEIXIN_FAVORITE".equalsIgnoreCase(s)) {
            return SOC_MEDIA.WEIXIN_FAVORITE;
        }
        if ("TENCENT".equalsIgnoreCase(s)) {
            return SOC_MEDIA.TENCENT;
        }
        if ("SYSTEM".equalsIgnoreCase(s)) {
            return SOC_MEDIA.SYSTEM;
        }
        if ("COPY".equalsIgnoreCase(s)) {
            return SOC_MEDIA.COPY;
        }
        return null;
    }

    /**
     * 获取安装媒体
     *
     * @return
     */
    public SOC_MEDIA getInstallMedia() {
        switch (this) {
            case QQ:
                return this;
            case SMS:
                return null;
            case SINA:
                return this;
            case EMAIL:
                return null;
            case QZONE:
                return SOC_MEDIA.QQ;
            case WEIXIN:
                return this;
            case TENCENT:
                return SOC_MEDIA.TENCENT;
            case WEIXIN_CIRCLE:
                return WEIXIN;
            case WEIXIN_FAVORITE:
                return WEIXIN;
            default:
                return null;
        }
    }

    /**
     * 自处理平台
     *
     * @return
     */
    public boolean isSelfHandlePlatform() {
        return this == COPY || this == SYSTEM;
    }

    /**
     * 获取平台名称
     *
     * @return
     */
    public String getPlatformName() {
        return platform;
    }
}
