# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-dontoptimize
#alibc

-keepattributes Signature
-dontwarn org.codehaus.mojo.**
-dontwarn java.nio.file.**
-dontwarn java.lang.reflect.**
-dontwarn sun.misc.**
-dontwarn android.service.media.**
-dontwarn android.content.**
-dontwarn libcore.icu.**
-dontwarn java.lang.invoke.**
-dontwarn android.webkit.**
-dontwarn javax.**
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.* {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class org.json.** {*;}
-dontwarn com.ali.auth.**
-keep class com.ali.auth.** {*;}
#common
-dontwarn com.youloft.core.report.service.*
-keep public class com.youloft.core.report.**{*;}
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *JavascriptInterface*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.app.Activity
-keep public class * extends com.youloft.nad.YLNAModule
-keep public class * extends com.youloft.nad.LenovoSDK
-keep public class * extends com.youloft.nad.INativeAdData
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class * extends android.webkit.WebChromeClient{
        *;
}

-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.youloft.modules.life.mettle.MettleLayoutManager {
    *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn com.cleanmaster.**
-keep class com.cleanmaster.**{*;}
-keep class com.picksinit.**{*;}
-keep class com.download.**{*;}
-keep class com.youloft.nativead.**{*;}
-keep class com.cmcm.adsdk.CMAdManager{*;}

#for GDT
-keep class com.qq.e.**{
   public protected *;
}
-keep class com.tencent.gdt.**{
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}
#for baidu
-keep class com.baidu.** {
   public protected *;
}

#support
-dontwarn android.support.v4.**
-keep class androidx.** { *; }
-keep interface androidx.app.** { *; }
-keep public class * extends android.**

#GSON
-keep class com.youloft.senior.bean.IJsonObject
-keep class com.google.gson.stream.**{*;}
-keep class * implements com.youloft.senior.bean.IJsonObject{*;}

#core
-dontwarn com.youloft.core.sdk.ks.**
-keep class com.youloft.core.sdk.ks.**{*;}
-keep class * implements com.youloft.core.sdk.ad.ISDKInterface
-keep class com.youloft.webview.InternalJavaScriptObject{
   public *;
}

#ad
-keepnames class com.youloft.core.sdk.ad.** {*;}

#gifImageView
-keep class pl.droidsonroids.gif.sample.GifSelectorDrawable { *; }

#okhttp3
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform


#umeng
-dontwarn com.youloft.umeng.**
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep enum com.facebook.**
-keep class com.umeng.** {*;}
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.tencent.** {*;}

-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep public class **.R$*{
    public static final int *;
}
#umeng push
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn org.apache.thrift.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
#talkingdata
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
#mvad
-keep class com.mediav.** {*;}

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
#tsting
-dontwarn com.testin.agent.**
-keep class com.testin.agent.** {*;}
#ttsdk
-keep class com.ckdroid.tt.model.** {*;}
#lenovo
-keep class zplay.**{*;}
-keep class a.a.**{*;}

#market
-dontwarn com.cmcm.support.**
-dontwarn com.ijinshan.**
-dontwarn net.sourceforge.pinyin4j**
-keep class com.cmcm.support.** {*;}
-keep class com.ijinshan.** {*;}
-keep class net.sourceforge.pinyin4j.** {*;}
#qh
-keep class com.qhad.** {*;}
##
-keep class com.squareup.wire.** { *; }
-keep class com.youloft.feedback.model.** { *; }

#tatagou
-keep class cn.tatagou.sdk.** { *; } #实体类不参与混淆
#retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#rxjava

-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep public class **.*ViewPager$*{
   int position;
}
-dontwarn com.xiaomi.push.**
#-keepclass com.xiaomi.push.**{*;}
#-keepclass com.huawei.hms.**{*;}
-keep class com.xiaomi.mipush.sdk.DemoMessageReceiver {*;}
-keep class com.youloft.push.XiaoMiPushActivity{*;}
-keep class com.huawei.android.pushagent.**{*;}
-keep class com.youloft.core.report.service.Daemon{*;}
-keep class com.marswin89.marsdaemon.NativeDaemonBase{*;}
-keep class com.marswin89.marsdaemon.nativ.NativeDaemonAPI20{*;}
-keep class com.marswin89.marsdaemon.nativ.NativeDaemonAPI21{*;}
-keep class com.marswin89.marsdaemon.DaemonApplication{*;}
-keep class com.marswin89.marsdaemon.DaemonClient{*;}
-keep class com.marswin89.marsdaemon.DaemonConfigurations{*;}
-keep class com.marswin89.marsdaemon.DaemonConfigurations$*{*;}
-keepclassmembers class com.facebook.animated.**{*;}
-keepclassmembers class * {
    native <methods>;
}
#Lead
-keep class com.lestore.ad.sdk.**{
    *;
}

-keep class com.chancelib.**{
    *;
}
-dontwarn com.chancelib.net.RequestParams
-dontwarn com.chancelib.util.BitmapLruCache
-dontwarn com.android.volley.**
-dontwarn com.lestore.ad.sdk.**

-dontwarn com.yolanda.nohttp.**
-keep class com.yolanda.nohttp.**{*;}
-dontwarn jp.co.cyberagent.android.gpuimage.**
-dontwarn jp.co.cyberagent.android
-dontwarn com.pili.**
-dontwarn com.qiniu.**
-dontwarn com.handmark.**
-dontwarn com.ngmob.**
-keep class com.qiniu.**{*;}
-keep class jp.co.cyberagent.android.gpuimage.**{*;}
-keep class com.pili.pldroid.player.** { *; }
-keep class com.ngmob.doubo.**{*;}
-keep class com.handmark.**{*;}
-keep class tv.danmaku.ijk.**{*;}
-keep class cnc.cad.**{*;}

-keep class android.support.design.widget.TabLayout{
    <fields>;
}

-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
 *;
}
-keep class com.tencent.mm.opensdk.** {
*;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
public static final int *;
    }
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature

-keep class com.taobao.securityjni.**{*;}
-keep class com.taobao.wireless.security.**{*;}
-keep class com.ut.secbody.**{*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}

#mobvista
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mobvista.** {*; }
-keep interface com.mobvista.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mobvista.**
-keep class **.R$* { public static final int mobvista*; }
-keep class com.alphab.** {*; }
-keep interface com.alphab.** {*; }

#ixin-tui
#-keep class com.ixintui.** {*;}
#-keep public interface com.ixintui.** {*;}
#-keep class com.xiaomi.** {*;}
#-keep class com.huawei.android.pushagent.** {*;}
#-keep class com.meizu.cloud.pushsdk.** {*;}
#-keep class com.youloft.ixintui.**{*;}
#picks
-keep class com.cmcm.orion.picks.down.env.DownLoadApplication{*;}
-keep class com.cmcm.orion.picks.down.**{
 *;
}

#oppo 广告sdk
-dontwarn com.oppo.**
-keep class com.oppo.** {
public protected *;
}
-keep class okio.**{ *; }
-keep class com.squareup.wire.**{ *; }
-keep public class * extends com.squareup.wire.**{ *; }
# Keep methods with Wire annotations (e.g. @ProtoField)
-keepclassmembers class ** {
 @com.squareup.wire.ProtoField public *;
 @com.squareup.wire.ProtoEnum public *;
}
-keep public class com.cdo.oaps.base.**{ *; }
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
#TUIA
-keep class com.db.ta.sdk.** { *; }
#TONGDUN
-dontwarn android.os.**
-dontwarn com.android.internal.**
-keep class cn.tongdun.android.**{*;}

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#头条广告（网盟）
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

#-keep class com.androidquery.callback.** {*;}
#-keep class android.view.** {*;}
#-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
#-keep class com.bytedance.sdk.openadsdk.** { *; }
#-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
#-keep class com.pgl.sys.ces.* {*;}
#-dontwarn com.bytedance.**
#-dontwarn com.androidquery.**


-dontwarn com.youloft.webview.protocol.handler.AbstractCommandHandler
-dontwarn com.youloft.content.h5.**
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.*);
}

#百度广告
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.*.** { *; }
-keep class com.baidu.mobad.** { *; }
-keep class com.bun.miitmdid.core.** {*;}

#欧乐欧推送广告
 -keep public class lab.mob.kert.Bork{
     <fields>;
     <methods>;
 }

#洛米广告
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.hz.yl.** {
    <fields>;
    <methods>;
}
-keep class com.glide.** {
     <fields>;
     <methods>;
 }
-keep class android.support.v4**{
    <fields>;
    <methods>;
}

#oppo推送
-dontwarn com.coloros.mcsdk.**
-keep class com.coloros.mcsdk.** { *; }

-keepclassmembers class ** {
    public void on*Event(...);
}
-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}
-keep class com.tencent.tencentmap.lbssdk.service.**{*;}


-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**

#极光IM开始
-dontoptimize
-dontpreverify
-keepattributes  EnclosingMethod,Signature
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-dontwarn cn.jmessage.**
-keep class cn.jmessage.**{ *; }

-keepclassmembers class ** {
    public void onEvent*(**);
}

#========================gson================================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#========================protobuf================================
-keep class com.google.protobuf.** {*;}
#极光IM结束
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

## Veloce ##
-keep public interface com.baidu.searchbox.veloce.common.INoProGuard
-keep class * implements com.baidu.searchbox.veloce.common.INoProGuard {
    *;
}

# 宿主中保证Veloce keep的类和接口不被混淆
-keep class com.baidu.veloce.** { *; }
-keep interface com.baidu.veloce.** {*;}

-keep class com.baidu.searchbox.veloce.** { *; }
-keep interface com.baidu.searchbox.veloce.** {*;}

-keep class reflect.** { *; }

#

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-dontwarn com.cmic.**
-keep class com.cmic.** { *; }
-dontwarn com.unicom.**
-keep class com.unicom.** { *; }
-dontwarn cn.com.chinatelecom.**
-keep class cn.com.chinatelecom.** { *; }


-keep public class cn.jzvd.JZMediaSystem {*; }
-keep public class cn.jzvd.demo.CustomMedia.CustomMedia {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaIjk {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder {*; }

-dontwarn tmsdk.common.roach.nest.**
-keep class btmsdkobf.** {*;}
-keep class tmsdk.** {*;}
-keep class com.tmsdk.**{*;}


#kotlin
# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembernames class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
-keepclassmembernames class android.support.v7.widget.RecyclerView {
    private <fields>;
    void setScrollState();
    boolean dispatchOnItemTouchIntercept(android.view.MotionEvent);
    void cancelTouch();
}

-dontwarn com.androidquery.**
-keep class com.androidquery.** { *;}

-dontwarn tv.danmaku.**
-keep class tv.danmaku.** { *;}

-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class com.dueeeke.videoplayer.** { *; }
-dontwarn com.dueeeke.videoplayer.**
-keep class com.opensource.svgaplayer.proto.** { *; }

-dontwarn com.umeng.**
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
-keep class org.android.agoo.vivo.PushMessageReceiverImpl{*;}

-keep public class * extends android.app.Service

-keep class * extends com.baidu.searchbox.veloce.common.INoProGuard {
    *;
}