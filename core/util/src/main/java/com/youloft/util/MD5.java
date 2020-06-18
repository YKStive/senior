/*******************************************************************************
 * Copyright (C) 2009-2010 eoeMobile.
 * All rights reserved.
 * http://www.eoeMobile.com/
 * <p>
 * CHANGE LOG:
 * DATE			AUTHOR			COMMENTS
 * =============================================================================
 * 2010MAY11		Waznheng Ma		Refine for Constructor and error handler.
 *******************************************************************************/

package com.youloft.util;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * MD5工具类
 *
 * @author Javen V4.5.9
 *         处理多线程下MD5相同内容加密后密文不一致
 */
public final class MD5 {
    private static final String ALGORITHM = "MD5";

    private MD5() {
    }

    final public static String encode(String source) {
        if (TextUtils.isEmpty(source)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(source.getBytes("utf-8"));
            return StringUtils.hexString(digest.digest());
        } catch (Throwable e) {
        }
        return "";
    }
}
