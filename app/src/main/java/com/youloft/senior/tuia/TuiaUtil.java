package com.youloft.senior.tuia;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.youloft.senior.base.App;
import com.youloft.senior.utils.MiitHelper;
import com.youloft.util.AppUtil;
import com.youloft.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

public class TuiaUtil {
    private static String getTuiaUrl(String slotId) {
        try {
            String appSecret = "3W5HzW15iUVqMkLPcJQWxQmDm6VaMinxDwMGZ8J";
            String appKey = "4FoJR6iArBPdvHFZ65Prr38GuMgY";
            //todo 待处理
//            try {
//                org.json.JSONObject tuiaConfig = YLConfigure.getInstance(AppContext.getContext()).getItemConfig("tuia_config", "{}");
//                appSecret = tuiaConfig.optString("appSecret", "3W5HzW15iUVqMkLPcJQWxQmDm6VaMinxDwMGZ8J");
//            } catch (Throwable e) {
//            }
//            if (TextUtils.isEmpty(appSecret)) {
//                appSecret = "3W5HzW15iUVqMkLPcJQWxQmDm6VaMinxDwMGZ8J";
//            }
//
//            try {
//                org.json.JSONObject tuiaConfig = YLConfigure.getInstance(AppContext.getContext()).getItemConfig("tuia_config", "{}");
//                appKey = tuiaConfig.optString("appKey", "4FoJR6iArBPdvHFZ65Prr38GuMgY");
//            } catch (Throwable e) {
//            }
            if (TextUtils.isEmpty(appKey)) {
                appKey = "4FoJR6iArBPdvHFZ65Prr38GuMgY";
            }

            String appInfo = getAppinfoForTuia();
            String md = Base64.encodeBytes(zip(appInfo.getBytes()));
            long timestamp = System.currentTimeMillis();
            String nonce = "123456";
            String signatureStr = "appSecret=" + appSecret + "&md=" + md + "&nonce=" + nonce + "&timestamp=" + timestamp;
            String signature = sha1(signatureStr);
            return "https://engine.lvehaisen.com/index/serving?appKey=" + appKey + "&adslotId=" + slotId + "&md=" + URLEncoder.encode(md, "UTF-8") + "&signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce
                    + "&isimageUrl=1&device_id=" + getImei();

        } catch (IOException e) {
        }
        return null;
    }

    public static Observable<JSONObject> requestTuiaAd(String slotId) {
//        if (!UserContext.hasLogin()) {
//            return Observable.empty();
//        }
        //todo 待处理 没有登录的情况
        final String url = getTuiaUrl(slotId);
        if (TextUtils.isEmpty(url)) {
            return Observable.empty();
        }
        return Observable.unsafeCreate(subscriber -> {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            JSONObject result = null;
            try {
                Response response = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build().newCall(request).execute();
                if (response != null && response.isSuccessful()) {
                    String data = response.body().string();
                    result = JSONObject.parseObject(data);
                }
            } catch (IOException e) {
            }
            subscriber.onNext(result);
            subscriber.onCompleted();
        });
    }

    public static String getActivityUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String imei = getImei();
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        } else {
            sb.append("&");
        }
        sb.append("device_id=").append(imei);
        //todo 待处理 传入userid
        sb.append("&userId=").append("xxxxx");
        return url;
    }

    public static String getReportUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String imei = getImei();
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        } else {
            sb.append("&");
        }
        sb.append("device_id=").append(imei);
        return url;
    }

    /**
     * 获取imei
     *
     * @return
     */
    private static String getImei() {
        String imei = AppUtil.getIMEI(App.Companion.instance(), "");
        if (TextUtils.isEmpty(imei)) {
            return "";
        }
        return imei;
    }

    private static String getAppinfoForTuia() {
        JSONObject jsonObject = new JSONObject();
        String imei = getImei();
        jsonObject.put("imei", imei);
        String oaid = MiitHelper.getOAID();
        if (oaid == null) {
            oaid = "";
        }
        jsonObject.put("oaid", oaid);
        if (TextUtils.isEmpty(imei)) {
            //todo 待处理 传入userid
            jsonObject.put("device_id", "xxx");
        } else {
            jsonObject.put("device_id", imei);
        }
        jsonObject.put("api_version", "1.0.0");
        return jsonObject.toJSONString();
    }

    static String sha1(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Throwable e) {
        }
        return input;
    }

    public static void reportUrl(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            return;
        }
        //todo 待处理 tuia的数据上报url
//        ApiDal.getInstance().requestFeed(getReportUrl(url));
    }

    /**
     * zip
     *
     * @param value
     * @return byte[]
     */
    private static byte[] zip(byte[] value) throws IOException {
        if (value == null || value.length == 0) {
            return new byte[0];
        }

        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             GZIPOutputStream gzipOut = new GZIPOutputStream(byteOut)) {
            gzipOut.write(value);
            gzipOut.finish();
            return byteOut.toByteArray();
        }
    }
}
