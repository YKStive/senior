package com.youloft.senior.web;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.youloft.core.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xll
 * @date 2018/9/17 16:40
 */
public class WebUrlHelper {

    /**
     * 添加version code 和 替换餐宿
     *
     * @return
     */
    public static String appendVersionCodeAndReplaceParams(String url, String cityCode, String astro, Map<String, String> params) {
        HashMap<String, String> p = new HashMap<>();
        if (!TextUtils.isEmpty(cityCode) && !"0".equals(cityCode)) {
            p.put("CITYID", cityCode);
        }
        if (!TextUtils.isEmpty(astro)) {
            p.put("ASTRO", astro);
        }

        /**
         * 处理文章CNZZ统计在所有URL中包含/wnl/的地方添加VersionCode
         */
        if (url.contains("51wnl-cq.com")
                || url.contains("youloft.com")
                || url.contains("wnl/")) {
            if (!url.contains("#") && !url.contains("versioncode")) {
                if (url.indexOf("?") > 0) {
                    url += "&versioncode=[VERINT]";
                } else {
                    url += "?versioncode=[VERINT]";
                }
            }
        }
        if (params != null) {
            p.putAll(params);
        }
        return url;
    }

    /**
     * 获取正常可识别的UA
     *
     * @param value
     * @return
     */
    private static String getValueEncoded(String value) {
        try {
            if (value == null) {
                return "null";
            }
            String newValue = value.replace("\n", "");
            for (int i = 0, length = newValue.length(); i < length; i++) {
                char c = newValue.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    return URLEncoder.encode(newValue, "UTF-8");
                }
            }
            return newValue;
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * 修改UA
     *
     * @param rawUA
     * @return
     */
    public static String wrapUserAgent(String rawUA) {
        if (TextUtils.isEmpty(rawUA) || rawUA.contains("wnlver/")) {
            return rawUA;
        }
        String userAgentString = getValueEncoded(rawUA);
        try {
            String commonUA = "seniorver/" + BuildConfig.VERSION_CODE + " wnl " + BuildConfig.VERSION_NAME;
            userAgentString += " " + commonUA;
        } catch (Exception e) {
        }
        return userAgentString;
    }

    /**
     * 处理UA问题
     */
    public static void modifyUA(WebView webView) {
        webView.getSettings().setUserAgentString(WebUrlHelper.wrapUserAgent(webView.getSettings().getUserAgentString()));
        String userAgentString = webView.getSettings().getUserAgentString();
    }

    /**
     * 获取参数（处理 Uri.getQueryParameter(String) +不见了的问题）
     *
     * @param uri
     * @param key
     * @return
     */
    public static String getParamsFromUri(Uri uri, String key) {
        if (uri == null) {
            return null;
        }
        try {
            return uri.getQueryParameters(key).get(0);
        } catch (Exception e) {
            return uri.getQueryParameter(key);
        }
    }

}
