package com.youloft.util;

import android.text.Editable;

/**
 * 字符串工具
 *
 * @author javen
 * @date 14-11-6
 */
public class StringUtils {
    private static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 是否有文本
     *
     * @return
     */
    public static boolean isBlank(String text) {
        if (text == null)
            return true;
        return text.trim().length() == 0;
    }

    /**
     * 是否有文本
     *
     * @param text
     * @return
     */
    public static boolean isBlank(Editable text) {
        return isBlank(text.toString());
    }

    /**
     * 是否包含某个子串
     *
     * @param content
     * @param wrapChar
     * @param subs
     * @return
     */
    public static boolean contains(String content, String wrapChar, String... subs) {
        for (String sub : subs) {
            if (content.contains(wrapChar + sub + wrapChar)) {
                return true;
            }
        }
        return false;
    }

    /**
     * null 转化 ""
     *
     * @param content
     * @return
     */
    public static String nullToValue(String content) {
        if (content == null) {
            return "";
        }
        return content;
    }

    /**
     * 添加进字符串
     *
     * @param separator
     * @param array
     * @return
     */
    public static String join(final String separator, final String... array) {
        if (array == null) {
            return null;
        }
        final int noOfItems = array.length;
        if (noOfItems <= 0) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        boolean needAdd = false;
        int len = array.length;
        for (int i = 0; i < len; i++) {
            if (needAdd) {
                buf.append(separator);
            }
            if (array[i] != null) {
                needAdd = true;
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 转化string
     *
     * @param param
     * @return
     */
    public static String toString(Object param) {
        if (param == null) {
            return "";
        }
        return param.toString();
    }

    public static String hexString(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = HEX_DIGITS[b >>> 4 & 0xf];
            str[index++] = HEX_DIGITS[b & 0xf];
        }
        return new String(str);
    }

    /**
     * content中是否包含sub
     *
     * @param content
     * @param sub
     * @return
     */
    public static boolean hasString(String content, String sub) {
        return content.indexOf(sub) >= 0;
    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str.trim());
    }

    /**
     * 是否为空或为Null
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return isEmpty(str) || "null".equalsIgnoreCase(str);
    }

    /**
     * 为空
     *
     * @param text
     * @return
     */
    public static boolean isNull(CharSequence text) {
        return null == text || text.equals("null") || text.equals("NULL") || text.equals("-");
    }


    /**
     * 获取首个不为空的Item
     *
     * @param values
     */
    public static String getFistNotEmptyValue(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            if (!isEmpty(value)) {
                return value;
            }
        }

        return "";

    }

    /**
     * 连接string
     *
     * @param text
     * @param subText
     * @param sublen
     * @param maxlen
     * @return
     */
    public static String concat(String text, String subText, int sublen, int maxlen) {
        int len = text.length();
        if (sublen == -1) {
            sublen = subText.length();
        }
        int elen = (len + sublen) - maxlen;
        if (elen <= 0) {
            return text + subText;
        }
        int end = len - elen - 1;
        if (end < 0 || end >= len) {
            return text + subText;
        }
        text = text.substring(0, len - elen - 1) + "…";
        return text + subText;
    }
}
