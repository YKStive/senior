package com.youloft.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 *
 * @author xll
 * @date 2018/7/25 14:37
 */
public class BasicUtil {


    /**
     * 邮箱匹配规则
     */
    private static final String EMAIL_MATCH = "^[A-Za-z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]+)";

    /**
     * 匹配邮箱
     *
     * @param email 需要判断的邮箱
     * @return 是否是邮箱格式
     */
    public static boolean isEmailMatcher(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Pattern regex = Pattern.compile(EMAIL_MATCH);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * 手机正则校验
     */
    private static final String PHONE_REGEX = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$";
    /**
     * 手机号码长度
     */
    private static final int PHONE_LENGTH = 11;

    /**
     * 判断手机号码是否合法
     *
     * @param phone
     * @return true 号码合法  返回false 不合法
     */
    public static boolean isPhoneNumber(String phone) {
        phone = phone.trim();
        if (phone.length() != PHONE_LENGTH) {
            return false;
        }
        if (phone.matches(PHONE_REGEX)) {
            return true;
        } else {
            return false;
        }
    }
}
