package com.youloft.webview.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.URLDecoder;

public class PayHelper {
    private PayHelper(){}
    /**
     * 处理Alipay及微信支付
     *
     * @param view
     * @param url
     * @return
     */
    public static boolean handleNativePay(WebView view, String url) {
        if (url.startsWith("https://ds.alipay.com")) {
            try {
                String newUrl = URLDecoder.decode(url);
                url = reConstructionAlipayUrl(newUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final Context context = view.getContext();
        //老版支付宝调用
        if (url.startsWith("alipays:") || url.startsWith("alipay")) {// ------  对alipays:相关的scheme处理 -------
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            } catch (Exception e) {
                new AlertDialog.Builder(context)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                            }
                        }).setNegativeButton("取消", null).show();
            }
            return true;
        }//新版支付宝：扫码支付
        else if (url != null && url.contains("intent://platformapi/startapp") && url.contains("scheme=alipays")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    intent.setSelector(null);
                }
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        // 如下方案可在非微信内部WebView的H5页面中调出微信支付
        else if (url.startsWith("weixin://wap/pay?")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "请安装微信最新版", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (url.startsWith("mqqapi://forward")) {//QQ钱包处理
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "请安装QQ最新版", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
    /*
     * 由于部分手机(小米5，小米note都可以，小米4不可以)无法调起支付宝支付，所以需要自己构造一遍url
     * oldUrl:https://ds.alipay.com/?from=mobilecodec&scheme=alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fbax02309y07r4txtezbw4026%3F_s%3Dweb-other
     * newUrl:intent://platformapi/startapp?from=mobilecodec&scheme=alipays&saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fbax02309y07r4txtezbw4026%3F_s%3Dweb-other
     */

    private static String reConstructionAlipayUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("://platformapi/startapp?")) {
                url = url.replace("://platformapi/startapp?", "&");
            }
            if (url.contains("https://ds.alipay.com/?")) {
                url = url.replace("https://ds.alipay.com/?", "intent://platformapi/startapp?");
            }
            url = url + "#Intent;scheme=alipays;package=com.eg.android.AlipayGphone;end";
        }

        return url;
    }

}
