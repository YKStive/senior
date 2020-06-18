package com.youloft.util;

import com.youloft.trans.I18N;

/**
 * @author xll
 * @date 2018/9/7 11:00
 */
class I18NConvert {
    /**
     * 转化繁体
     *
     * @param string
     * @return
     */
    static String convert(CharSequence string) {
        return I18N.convert(string);
    }
}
