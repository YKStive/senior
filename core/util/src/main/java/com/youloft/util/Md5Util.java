package com.youloft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author Administrator
 */
public class Md5Util {
    /**
     * 获取文件md5
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {

        if (null == file || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return encodeHex(digest.digest());
    }

    /**
     * 获取输入流md5
     *
     * @param in
     * @return
     */
    public static String getStreamMD5(InputStream in) {
        if (in == null) {
            return null;
        }
        MessageDigest md = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            md = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, len);
            }
        } catch (Exception e) {

            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return encodeHex(md.digest());
    }

    /**
     * 转换md5
     *
     * @param data
     * @return
     */
    public static String encodeHex(byte[] data) {
        if (data == null) {
            return null;
        }
        final String HEXES = "0123456789abcdef";
        int len = data.length;
        StringBuilder hex = new StringBuilder(len * 2);
        for (int i = 0; i < len; ++i) {
            hex.append(HEXES.charAt((data[i] & 0xF0) >>> 4));
            hex.append(HEXES.charAt((data[i] & 0x0F)));
        }
        return hex.toString();
    }
}
