package com.youloft.senior.push;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.tag.TagManager;
import com.youloft.util.AppUtil;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * 注册推送信息
 *
 * @VERSION 1.0.0
 * @AUTHOR: xll create 2018/6/21 16:00
 */
public class PushWrapper {
    static Context context;

    //初始化极光推送
    public static void init(Application context) {
        PushWrapper.context = context;
        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(context);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.setNotificationClickHandler(new UMPushClickHandler());
        mPushAgent.setMessageHandler(new UmPushHandler());
        mPushAgent.setNotificationChannelName("消息推送");
        mPushAgent.setMuteDurationSeconds(0);
        mPushAgent.setDisplayNotificationNumber(0);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                updateTags();
                updateAlias();
                System.out.println("umpush   " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });

        //        //注册小米推送
        MiPushRegistar.register(context, "2882303761517138370", "5311713861370");
//        //注册华为推送
        HuaWeiRegister.register(context);
//        //注册魅族推送
        MeizuRegister.register(context, "113855", "95933be55b5c4a2582aef8351b7bd2d4");

        OppoRegister.register(context, "9or4i5V2178ckkS48sc4S4448", "9717bac8553DCad275cf60794187d6b4");

        //vivo 通道
        VivoRegister.register(context);
    }

    private static void updateAlias() {

    }

    public static void onActivityCreate() {
        PushAgent.getInstance(context).onAppStart();
    }

    private static boolean checkDeviceForHuawei() {
        boolean result = false;
        if (Build.BRAND.equalsIgnoreCase("huawei") || Build.BRAND.equalsIgnoreCase("honor")) {
            result = true;
        }
        return result;
    }

    /**
     * 获取PushId
     *
     * @return
     */
    public static String getPushId() {
        return PushAgent.getInstance(context).getRegistrationId();
    }

    /**
     * 设置推送开关
     */
    public static void setPushEnabled() {
        updateTags();
    }

    /**
     * 更新推送的标签
     */
    public static void updateTags() {
        //查询标签

        List<String> tagList = new ArrayList<>();
        //加入用户id
        String imei = AppUtil.getIMEI(context);
        if (!TextUtils.isEmpty(imei)) {
            tagList.add(imei);
        }
        updateTags(toArray(tagList))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .onExceptionResumeNext(Observable.empty())
                .onErrorResumeNext(Observable.empty())
                .doOnError(throwable -> {

                })
                .subscribe();
    }


    static Executor ANALYTICS_EXECUTOR = Executors.newSingleThreadExecutor();


    /**
     * 检查安装的包名
     */
    private static final HashSet<String> CHECK_INFO_APPS = new HashSet<String>() {
        {
            add("com.songheng.eastnews");//东方头条
            add("com.cashtoutiao");//惠头条
            add("com.ss.android.article.lite");//今日头条极速版
            add("cn.weli.story");//微鲤看看
            add("com.jifen.qukan");//趣头条
        }
    };

    private static String[] toArray(List<String> items) {
        return items.toArray(new String[]{});
    }

    /**
     * 设置别名
     *
     * @param alias
     */
    public static void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        PushAgent.getInstance(context).setAlias(alias, "alias_tag", (isSuccess, s) -> {

        });
    }

    /**
     * 更新所有标签
     *
     * @param tags
     * @return
     */
    public static Observable<ContentValues> updateTags(final String... tags) {
        return Observable.create(subscriber -> {
            subscriber.onStart();
            final ContentValues contentValues = new ContentValues();
            resetTags(tags).subscribe(new Observer<ContentValues>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    contentValues.put("isSuccess", false);
                    subscriber.onNext(contentValues);
                    subscriber.onCompleted();
                }

                @Override
                public void onNext(ContentValues values) {
                    if (values == null || !values.getAsBoolean("isSuccess")) {
                        onError(null);
                        return;
                    }
                    PushAgent.getInstance(context).getTagManager().addTags((b, result) -> {
                        contentValues.put("isSuccess", b);
                        if (b) {
                            contentValues.put("status", result.status);
                            contentValues.put("remain", result.remain);
                            contentValues.put("errors", result.errors);
                        }
                        subscriber.onNext(contentValues);
                        subscriber.onCompleted();
                    }, tags);
                }
            });

        });
    }

    /**
     * 重置标签
     *
     * @return
     */
    public static Observable<ContentValues> resetTags(final String... tags) {
        return Observable.create(subscriber -> {
            subscriber.onStart();
            final ContentValues contentValues = new ContentValues();

            final TagManager tagManager = PushAgent.getInstance(context).getTagManager();
            tagManager.getTags((lb, list) -> {
                if (lb) {
                    if (list != null && !list.isEmpty()) {
                        List<String> oldTags = Arrays.asList(tags);
                        boolean needAddTag = false;
                        for (String item : oldTags) {
                            if (!list.contains(item)) {
                                needAddTag = true;
                                break;
                            }
                        }
                        for (String item : list) {
                            if (!oldTags.contains(item)) {
                                needAddTag = true;
                                break;
                            }
                        }
                        if (!needAddTag) {
                            //标签没有发生、变化
                            contentValues.put("isSuccess", false);
                            subscriber.onNext(contentValues);
                            subscriber.onCompleted();
                            return;
                        }
                    }
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    tagManager.deleteTags((db, result) -> {
                        contentValues.put("isSuccess", db);
                        if (db) {
                            contentValues.put("status", result.status);
                            contentValues.put("remain", result.remain);
                            contentValues.put("errors", result.errors);
                        }
                        subscriber.onNext(contentValues);
                        subscriber.onCompleted();
                    }, list.toArray(new String[]{}));
                } else {
                    contentValues.put("isSuccess", false);
                    subscriber.onNext(contentValues);
                    subscriber.onCompleted();
                }
            });
        });
    }
}