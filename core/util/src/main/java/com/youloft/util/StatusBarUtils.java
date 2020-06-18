package com.youloft.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 此工具类提供了小米，魅族4.4以上以及android6.0以上修改状态栏颜色为黑色的方法
 * version   4.6.1
 * <p>
 * <p>
 * 修订记录:
 * 1. 添加华为挖孔屏的适配    zdj  4.8.0
 *
 * @author slj
 * @time 2017/4/11 13:17
 * @class StatusBarUtils
 */

public class StatusBarUtils {


    private static int _vivoNotchState = -2;

    public static final int VIVO_MASK_NOTCH = 0x00000020;
    public static final int VIVO_MASK_ROUND = 0x00000008;


    /**
     * 判断是否有Vivo某功能
     *
     * @param mask 0x00000020是否有留海VIVO_MASK_NOTCH
     *             0x00000008是否有圆角VIVO_MASK_ROUND
     * @return
     */
    private static final int getVivoNotchState(Context context, int mask) {
        try {
            if (_vivoNotchState != -2) {
                return _vivoNotchState;
            }
            ClassLoader cl = context.getClassLoader();
            Class<?> ftFeatureClass = cl.loadClass("android.util.FtFeature");
            Method supportMethod = ftFeatureClass.getMethod("isFeatureSupport", int.class);
            _vivoNotchState = (boolean) supportMethod.invoke(ftFeatureClass, mask) ? 1 : 0;
        } catch (Throwable e) {
            _vivoNotchState = -1;
        }
        return _vivoNotchState;
    }

    /**
     * 获取Vivo高度
     *
     * @param context
     * @return
     */
    private static final int getVivoNotchHeight(Context context) {
        if (getVivoNotchState(context, VIVO_MASK_NOTCH) <= 0) {
            return -1;
        }
        String fullScreenSetting = Settings.Secure.getString(context.getContentResolver(), "full_screen_app_modified_by_user");

        if (!TextUtils.isEmpty(fullScreenSetting) && fullScreenSetting.contains(String.format(":%s,2", AppUtil.getPackageName(context)))) {
            return UiUtil.dp2Px(context, 27);
        }
        return -1;
    }

    /**
     * 获取Oppo Notch高度
     *
     * @param context
     * @return
     */
    private static final int getOppoNotchHeight(Context context) {
        if (getOppoNotchState(context) <= 0) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            String nonimmersive = Settings.Global.getString(context.getContentResolver(),
                    "key_display_nonimmersive_local_apps");
            if (TextUtils.isEmpty(nonimmersive)
                    || !nonimmersive.contains(AppUtil.getPackageName(context) + ",")) {
                return 80;
            }
        }
        return -1;
    }

    private static int _oppoNotchState = -2;

    /**
     * 判断是否oppo挖孔屏
     *
     * @param context
     * @return
     */
    private static int getOppoNotchState(Context context) {
        if (_oppoNotchState != -2) {
            return _oppoNotchState;
        }
        try {
            _oppoNotchState = context
                    .getPackageManager()
                    .hasSystemFeature("com.oppo.feature.screen.heteromorphism") ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            _oppoNotchState = -1;
        }
        return _oppoNotchState;
    }


    /**
     * 获取小米 Notch高度
     *
     * @param context
     * @return
     */
    private static final int getXiaomiNotchHeight(Context context) {
        //需要先判断是否小米设备
        if (get_xiaomiNotchState() <= 0) {
            return get_xiaomiNotchState();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //判断是否开启隐藏屏幕刘海，如果开启了，不需要适配
            int hideNotch = Settings.Global.getInt(context.getContentResolver(), "force_black", 0);
            if (hideNotch == 1) {
                return 0;
            }
            //没有隐藏刘海屏，那么获取状态兰高度返回
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }


    private static int _xiaomiNotchState = -2;

    /**
     * 是否小米挖孔屏
     *
     * @return
     */
    public static int get_xiaomiNotchState() {
        if (_xiaomiNotchState != -2) {
            return _xiaomiNotchState;
        }
        try {
            Method getIntMethod = Class.forName("android.os.SystemProperties")
                    .getMethod("getInt", String.class, int.class);
            _xiaomiNotchState = (int) getIntMethod.invoke(null, "ro.miui.notch", 0);
        } catch (Exception e) {
            _xiaomiNotchState = -1;
        }
        return _xiaomiNotchState;
    }

    //不可直接访问,仅用于方法缓存
    private static int[] _hwNotchSize = null;


    public static final String DISPLAY_NOTCH_STATUS = "display_notch_status";

    /**
     * 获取华为挖孔大小
     *
     * @param context
     * @return 数组  [0] width [1] height
     */
    private static int getHwNotchSize(Context context) {

        try {
            //小于0表示反射失败，直接返回-1，再看是否是其他类型的手机,等于0，表示非挖孔屏
            if (cacheHwNotch(context) <= 0) {
                return cacheHwNotch(context);
            }
            //判断用户是否关闭了使用刘海空间，如果关闭那么不需要再适配了
            int mIsNotchSwitchOpen = Settings.Secure.getInt(context.getContentResolver(), DISPLAY_NOTCH_STATUS, 0);
            if (mIsNotchSwitchOpen == 1) {
                return 0;
            }
            if (_hwNotchSize == null) {
                ClassLoader cl = context.getClassLoader();
                Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
                Method get = HwNotchSizeUtil.getMethod("getNotchSize");
                _hwNotchSize = (int[]) get.invoke(HwNotchSizeUtil);
            }
        } catch (Throwable unused) {
        }
        if (_hwNotchSize == null) {
            _hwNotchSize = new int[]{0, 0};
        }
        return _hwNotchSize[1];
    }


    private static int _hwNotchState = -2;

    /**
     * 判断是否为华为开孔屏
     *
     * @param context
     * @return -1反射失败，不为华为手机,1是华为挖孔屏，0华为非挖孔屏
     */
    private static int cacheHwNotch(Context context) {
        //反射过了，不再去反射了
        if (_hwNotchState != -2) {
            return _hwNotchState;
        }
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            _hwNotchState = (Boolean) get.invoke(HwNotchSizeUtil) ? 1 : 0;
        } catch (Throwable e) {
            _hwNotchState = -1;
        }
        return _hwNotchState;
    }


    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    static int _resStatusHeightId = -1;

    /**
     * 获取正常的屏幕中的状态栏高度
     *
     * @param context
     * @return
     */
    public static int getNormalStatusHeight(Context context) {
        try {
            if (_resStatusHeightId < 1) {
                Class clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                _resStatusHeightId = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            }
        } catch (Throwable e) {
            return UiUtil.dp2Px(context, 20);
        }
        return context.getResources().getDimensionPixelSize(_resStatusHeightId);
    }

    private static int notchHeight = -1;

    /**
     * 获取开孔屏的高度，计算一遍就可以了
     *
     * @param context
     * @return
     */
    public static int getNotchHeight(Context context) {
        if (notchHeight >= 0) {
            return notchHeight;
        }
        //华为挖孔屏处理,如果返回为null那么不是华为手机，继续后边的判断
        int hwNotchSize = getHwNotchSize(context);
        if (hwNotchSize >= 0) {
            notchHeight = hwNotchSize;
            return notchHeight;
        }

        //Vivo挖孔高度
        int vivoNotchHeight = getVivoNotchHeight(context);
        if (vivoNotchHeight >= 0) {
            notchHeight = vivoNotchHeight;
            return notchHeight;
        }

        //Oppo挖孔高度
        int oppoNotchHeight = getOppoNotchHeight(context);
        if (oppoNotchHeight >= 0) {
            notchHeight = oppoNotchHeight;
            return notchHeight;
        }

        //小米挖孔高度
        int miNotchHeight = getXiaomiNotchHeight(context);
        if (miNotchHeight >= 0) {
            notchHeight = miNotchHeight;
            return notchHeight;
        }
        if (notchHeight < 0) {
            notchHeight = 0;
        }
        return notchHeight;
    }

    private static int S_STATUBAR_HEIGHT = -1;

    /**
     * 获取状态栏的高度
     * 仅获取一次
     *
     * @return
     */
    public static int getStatusHeight(Context context) {
        if (S_STATUBAR_HEIGHT < 1) {
            S_STATUBAR_HEIGHT = getNormalStatusHeight(context); //Math.max(getNotchHeight(context), getNormalStatusHeight(context));
        }
        return S_STATUBAR_HEIGHT;
    }


    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            } else if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void StatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

}