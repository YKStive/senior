package com.youloft.net.bean;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by slj on 2018/5/2.
 */

public class MissionResult implements IJsonObject {


    /**
     * data : [{"type":1,"continued":2,"coin_signin_contents":[50,100,100,100,100,200,500],"status":1,"code":"signin","gold":300,"cash":319,"read":9},{"type":2,"title":"活动任务","missions":[{"code":"hd_001","startTime":1525671626,"endTime":1585671626,"content":"参加XXXX活动(5月1日-5月5日)","subContent":"任务详情","subItems":[{"code":"A_51_01","content":"参加xxxxx活动","url":"https://www.baidu.com","coin":100,"status":1},{"code":"A_51_02","content":"分享XXXX活动给您的朋友","url":"https://www.baidu.com","coin":100,"status":1},{"code":"A_51_03","content":"在活动页面分享给朋友","url":"https://www.baidu.com","coin":100,"status":1}]},{"code":"hd_002","startTime":1525671626,"endTime":1585671626,"content":"参加XXXX活动(5月1日-5月5日)","subContent":"任务详情","subItems":[{"code":"hd_001001","content":"参加xxxxx活动","url":"https://www.baidu.com","coin":100,"status":1},{"code":"hd_001002","content":"分享XXXX活动给您的朋友","url":"https://www.baidu.com","coin":100,"status":1},{"code":"hd_001003","content":"在活动页面分享给朋友","url":"https://www.baidu.com","coin":100,"status":1}]}]},{"type":3,"title":"新手任务","subContent":"任务详情","missions":[{"code":"xs_001","content":"首次成功邀请好友","subContent":"任务详情","subItems":[{"code":"Inviter_new","content":"首次成功邀请好友下载登录万年历","url":"","coin":17000,"status":0}]},{"code":"xs_002","content":"首次阅读三篇资讯","subContent":"任务详情","subItems":[{"code":"Read_three","content":"阅读首页精品推荐三篇资讯信息","url":"","coin":300,"status":0}]},{"code":"xs_003","content":"首次分享赚到的零钱","subContent":"任务详情","subItems":[{"code":"Share_money","content":"在零钱详情页面分享您赚到的零钱","url":"","coin":100,"status":1}]},{"code":"xs_004","content":"填写邀请人邀请码","subContent":"任务详情","subItems":[{"code":"Inviter_code","content":"填写您好友的邀请码","url":"","coin":300,"status":1}]}]},{"type":4,"title":"日常任务","subContent":"任务详情","missions":[{"code":"rc_001","content":"邀请第1个好友","subContent":"任务详情","subItems":[{"code":"Inviter","content":"您首次邀请好友除了奖励6000金币，还将额外获得奖励的2000金币，后续邀请好友，每逢五的倍数，将在额外获得2000金币","url":"","coin":15000,"status":1}]},{"code":"rc_002","content":"将邀请好友分享到朋友圈","subContent":"任务详情","subItems":[{"code":"Share_inviter_py","content":"将邀请好友分享到您的朋友圈","url":"","coin":100,"status":1}]},{"code":"rc_003","content":"将邀请好友分享给微信好友","subContent":"任务详情","subItems":[{"code":"Share_inviter_wx","content":"将邀请好友分享给您的微信好友","url":"","coin":100,"status":1}]},{"code":"rc_004","content":"分享一篇资讯","subContent":"任务详情","subItems":[{"code":"Share_news","content":"分享一篇资讯给您的好友","url":"","coin":100,"status":1}]},{"code":"rc_005","content":"阅读10条资讯","subContent":"任务详情","subItems":[{"code":"Read_news","content":"每阅读首页精品推荐一条资讯奖励100金币，最多可阅读10条","url":"","coin":100,"status":1}]},{"code":"rc_006","content":"使用任意一款工具","subContent":"任务详情","subItems":[{"code":"Use_tool","content":"使用任意一款小工具","url":"","coin":100,"status":1}]}]}]
     * sign :
     */
    @SerializedName("sign")
    @Expose
    public String sign;
    @SerializedName("data")
    @Expose
    public List<DataBean> data;


    public static class DataBean {
        /**
         * type : 1
         * continued : 2
         * coin_signin_contents : [50,100,100,100,100,200,500]
         * status : 1
         * code : signin
         * gold : 300
         * cash : 319
         * read : 9
         * title : 活动任务
         * missions : [{"code":"hd_001","startTime":1525671626,"endTime":1585671626,"content":"参加XXXX活动(5月1日-5月5日)","subContent":"任务详情","subItems":[{"code":"A_51_01","content":"参加xxxxx活动","url":"https://www.baidu.com","coin":100,"status":1},{"code":"A_51_02","content":"分享XXXX活动给您的朋友","url":"https://www.baidu.com","coin":100,"status":1},{"code":"A_51_03","content":"在活动页面分享给朋友","url":"https://www.baidu.com","coin":100,"status":1}]},{"code":"hd_002","startTime":1525671626,"endTime":1585671626,"content":"参加XXXX活动(5月1日-5月5日)","subContent":"任务详情","subItems":[{"code":"hd_001001","content":"参加xxxxx活动","url":"https://www.baidu.com","coin":100,"status":1},{"code":"hd_001002","content":"分享XXXX活动给您的朋友","url":"https://www.baidu.com","coin":100,"status":1},{"code":"hd_001003","content":"在活动页面分享给朋友","url":"https://www.baidu.com","coin":100,"status":1}]}]
         * subContent : 任务详情
         */
        @SerializedName("type")
        @Expose
        public int type;
        @SerializedName("continued")
        @Expose
        public int continued;
        @SerializedName("status")
        @Expose
        public int status;
        @SerializedName("allCoin")
        @Expose
        public int allCoin;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("gold")
        @Expose
        public int gold;
        @SerializedName("todayCoin")
        @Expose
        public int todayCoin;
        @SerializedName("cash")
        @Expose
        public int cash;

        @SerializedName("read")
        @Expose
        public int read;

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("txjfhl")
        @Expose
        public float txjfhl;

        @SerializedName("subContent")
        @Expose
        public String subContent;

        @SerializedName("coin_signin_contents")
        @Expose
        public List<Integer> coin_signin_contents;
        @SerializedName("coin_signin_contents_doublecode")
        @Expose
        public List<String> coinSigninContentsDoublecode;

        @SerializedName("missions")
        @Expose
        public List<MissionsBean> missions;

        @SerializedName("content")
        @Expose
        public List<String> contents;

        @SerializedName("interval")
        @Expose
        public int interval;

//        @SerializedName("toolBarContent")
//        @Expose
//        public List<CoinTask> toolBarContent;

        @SerializedName("hongbaos")
        @Expose
        public List<Integer> hongbaos;

        @SerializedName("count")
        @Expose
        public int count;

        @SerializedName("hongBaoContent")
        @Expose
        public String hongbaoContent;

        @SerializedName("isGetHongBaoToday")
        @Expose
        public int isGetHongBaoToday;

        @SerializedName("signMaxCoin")
        @Expose
        public int signMaxCoin;
        @SerializedName("posId")
        @Expose
        public String posId;
        @SerializedName("platformId")
        @Expose
        public String platformId;
        @SerializedName("appId")
        @Expose
        public String appId;

        public Object tencentMission;


        public static class MissionsBean {
            /**
             * code : hd_001
             * startTime : 1525671626
             * endTime : 1585671626
             * content : 参加XXXX活动(5月1日-5月5日)
             * subContent : 任务详情
             * subItems : [{"code":"A_51_01","content":"参加xxxxx活动","url":"https://www.baidu.com","coin":100,"status":1},{"code":"A_51_02","content":"分享XXXX活动给您的朋友","url":"https://www.baidu.com","coin":100,"status":1},{"code":"A_51_03","content":"在活动页面分享给朋友","url":"https://www.baidu.com","coin":100,"status":1}]
             */
            @SerializedName("code")
            @Expose
            public String code;

            @SerializedName("missionStart")
            @Expose
            public int missionStart;

            @SerializedName("startTime")
            @Expose
            public long startTime;

            @SerializedName("endTime")
            @Expose
            public long endTime;

            @SerializedName("content")
            @Expose
            public String content;

            @SerializedName("subContent")
            @Expose
            public String subContent;

            @SerializedName("subItems")
            @Expose
            public List<SubItemsBean> subItems;

            @SerializedName("url")
            @Expose
            public String url;

            @SerializedName("coin")
            @Expose
            public int coin;

            @SerializedName("done")
            @Expose
            public boolean done;

            @SerializedName("button")
            @Expose
            public String button;

            @SerializedName("isVipShowAd")
            @Expose
            public boolean isVipShowAd = false;

            public JSONObject tuiaData;

            public int getCoin() {
                if (subItems == null || subItems.size() == 0) {
                    return 0;
                }
                int _coin = this.coin;
                for (int i = 0; i < subItems.size(); i++) {
                    _coin += subItems.get(i).coin;
                }
                return _coin;
            }

            public boolean hasDone() {
                if (subItems == null || subItems.size() == 0) {
                    return true;
                }
                for (int i = 0; i < subItems.size(); i++) {
                    if (subItems.get(i).status == 1) {
                        return false;
                    }
                }
                return true;
            }

            /**
             * 处理完成
             *
             * @return
             */
            public void done() {
                if (subItems == null || subItems.size() == 0) {
                    return;
                }
                for (int i = 0; i < subItems.size(); i++) {
                    subItems.get(i).status = 0;
                }
            }

            /**
             * 是否是H5任务
             *
             * @return
             */
            public boolean isH5Task() {
                return !TextUtils.isEmpty(code) && code.endsWith("_jumpurl");
            }

            /**
             * 是否是H5任务
             *
             * @return
             */
            public boolean isNewActivityTask() {
                return !TextUtils.isEmpty(code) && code.endsWith("_NoCoin");
            }


            /**
             * 是否是H5任务
             *
             * @return
             */
            public boolean isVideoTask() {
                return !TextUtils.isEmpty(code) && code.startsWith("XiaoShiPingJinBin");
            }

            /**
             * 是否是 RewardTask
             *
             * @return
             */
            public boolean isRewardTask() {
                return !TextUtils.isEmpty(code) && code.endsWith("_reward");
            }

            /**
             * 是否是移动积分任务
             *
             * @return
             */
            public boolean isYDJFTask() {
                return !TextUtils.isEmpty(code) && code.startsWith("YiDongJiFen_");
            }

            /**
             * imagetext_xx 图文
             * photo_xx 影集
             * emoj_xx 表情
             * zan_photo_xx 看xxx
             * zan_emoj_xx
             * zan_imagetext_xx
             * activity_xx 活动
             */
            public boolean isImageText() {
                return !TextUtils.isEmpty(code) && code.startsWith("imagetext_");
            }

            public boolean isPhoto() {
                return !TextUtils.isEmpty(code) && code.startsWith("photo_");
            }

            public boolean isEmoj() {
                return !TextUtils.isEmpty(code) && code.startsWith("emoj_");
            }

            public boolean isZanImageText() {
                return !TextUtils.isEmpty(code) && code.startsWith("zan_imagetext_");
            }

            public boolean isZanPhoto() {
                return !TextUtils.isEmpty(code) && code.startsWith("zan_photo_");
            }

            public boolean isZanEmoj() {
                return !TextUtils.isEmpty(code) && code.startsWith("zan_emoj_");
            }

            public boolean isActivity() {
                return !TextUtils.isEmpty(code) && code.startsWith("activity_");
            }

            public boolean isInvite() {
                return !TextUtils.isEmpty(code) && code.equalsIgnoreCase("xs_001");
            }

            public boolean isWriteCode() {
                return !TextUtils.isEmpty(code) && code.equalsIgnoreCase("xs_004");
            }

            /**
             * 是否是 RewardTask
             *
             * @return
             */
            public boolean isFastAppTask() {
                return !TextUtils.isEmpty(code) && code.startsWith("fastapp_") && subItems != null && !subItems.isEmpty();
            }

            public boolean isDownloadTask() {
                return !TextUtils.isEmpty(code) && code.startsWith("download_") && subItems != null && !subItems.isEmpty();
            }

            public boolean isTuiaTask() {
                return !TextUtils.isEmpty(code) && code.endsWith("_tuia") && subItems != null && !subItems.isEmpty();
            }

            public boolean shareImageTask() {
                return !TextUtils.isEmpty(code) && code.startsWith("Share_WX") && subItems != null && !subItems.isEmpty();
            }

            public String getUnIdenty() {
                if (TextUtils.isEmpty(code)) {
                    return "";
                }
                if (subItems == null || subItems.isEmpty()) {
                    return code;
                }
                return code + subItems.get(0).code;
            }

            public static class SubItemsBean {
                /**
                 * code : A_51_01
                 * content : 参加xxxxx活动
                 * url : https://www.baidu.com
                 * coin : 100
                 * status : 1
                 */
                @SerializedName("code")
                @Expose
                public String code;

                @SerializedName("content")
                @Expose
                public String content;

                @SerializedName("url")
                @Expose
                public String url;

                @SerializedName("coin")
                @Expose
                public int coin;

                @SerializedName("status")
                @Expose
                public int status;

                @SerializedName("startTime")
                @Expose
                public long startTime;

                @SerializedName("endTime")
                @Expose
                public long endTime;

                @Expose
                @SerializedName("playInterval")
                public long interval;
                @SerializedName("platformId")
                @Expose
                public String platform;
                @Expose
                @SerializedName("posId")
                public String posId;
                @SerializedName("appId")
                @Expose
                public String appId;
                @SerializedName("shareImg")
                @Expose
                public String shareImg;
                @SerializedName("doubleCode")
                @Expose
                public String doubleCode;

                @SerializedName("dayCount")
                @Expose
                public int dayCount;
            }
        }
    }
}
