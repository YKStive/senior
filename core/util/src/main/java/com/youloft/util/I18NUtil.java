package com.youloft.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

/**
 * 繁简转化
 *
 * @author xll
 * @date 2018/9/7 10:52
 */
public class I18NUtil {
    /**
     * 是否有i18N
     */
    private static boolean hasI18N = false;

    static {
        try {
            //检查是否有i18N组件
            Class<?> aClass = Class.forName("com.youloft.trans.I18N");
            if (aClass != null) {
                hasI18N = true;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 转化繁体
     *
     * @param string
     * @return
     */
    public static CharSequence convert(CharSequence string) {
        if (string instanceof Spannable) {
            return string;
        }
        if (hasI18N) {
            return I18NConvert.convert(string);
        }
        return String.valueOf(string);
    }
}
