package com.youloft.trans;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by javen on 14-8-11.
 */
public class Strings {

    private static final String DEF_CHARSET = "utf-8";

    /**
     * encode参数
     *
     * @param param
     * @param encode
     * @return
     */
    public static String encodeParams(String param, boolean encode) {
        try {
            if (encode && !isNull(param))
                return URLEncoder.encode(param, DEF_CHARSET);
        } catch (UnsupportedEncodingException e) {

        }
        return param;
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

    private static Pattern pattern = Pattern.compile("\\[\\w+\\]");//Apply的模式

    /**
     * 格式化
     *
     * @param template
     * @param params
     * @return
     */
    public static String apply(String template, Map<String, String> params) {

        if (TextUtils.isEmpty(template) || params == null || params.isEmpty())
            return template;

        boolean needEncode = false;
        if (hasString(template, "[DONTURLENCODE]")) {
            needEncode = true;
        }
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        if (matcher.find()) {
            do {
                String item = matcher.group().replaceAll("(\\[|\\])", "").toUpperCase();
                if (params != null && params.containsKey(item)) {
                    if (needEncode) {
                        try {
                            String encoded = URLEncoder.encode(params.get(item), "utf-8");
                            matcher.appendReplacement(sb, encoded);
                        } catch (UnsupportedEncodingException e) {
                            matcher.appendReplacement(sb, params.get(item));
                        }
                    } else {
                        matcher.appendReplacement(sb, params.get(item) == null ? "" : params.get(item));
                    }

                }
            } while (matcher.find());
        } else {
            sb.append(template);
        }
        return sb.toString();
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

    public static String getYiJiString(String str) {
        if (isNull(str)) {
            return "-";
        }
        return str;
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
     * toString
     * <p/>
     * 做了为空的判断
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return object == null ? "" : object.toString();
    }


    /**
     * 读文件内容到String中
     *
     * @param name
     * @param charset
     * @return
     */
    public static String readStringFromAssets(Context context, String name, String charset, String failValue) {
        try {
            InputStream in = context.getAssets().open(name);
            return readStringFromStream(in, charset, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return failValue;
    }

    /**
     * 从文件中读取String
     *
     * @param filePath
     * @param charset
     * @param failValue
     * @return
     */
    public static String readStringFromFile(String filePath, String charset, String failValue) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory() && file.canRead()) {
            try {
                return readStringFromStream(new FileInputStream(file), charset, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return failValue;
    }

    /**
     * 从文件中读取String
     *
     * @param filePath
     * @param charset
     * @param failValue
     * @return
     */
    public static String readStringFromFile(File filePath, String charset, String failValue) {
        File file = filePath;
        if (file.exists() && !file.isDirectory() && file.canRead()) {
            try {
                return readStringFromStream(new FileInputStream(file), charset, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return failValue;
    }

    /**
     * 从输入流中读取String
     *
     * @param in
     * @param charset
     * @return
     */
    public static String readStringFromStream(InputStream in, String charset, boolean autoclose) throws IOException {
        BufferedReader br = null;
        String line = null;
        StringBuilder content = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(in, charset));
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } finally {
            if (autoclose && in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }


    /**
     * Returns the given string if it is non-null; the empty string otherwise.
     *
     * @param string the string to test and possibly return
     * @return {@code string} itself if it is non-null; {@code ""} if it is null
     */
    public static String nullToEmpty(String string) {
        return (string == null) ? "" : string;
    }


    /**
     * 截字符 串
     *
     * @param text
     * @param begin
     * @param len
     * @return
     */
    public static String subString(String text, int begin, int len) {
        if (begin < 0) {
            return "";
        }
        int end = Math.min(begin + len, text.length());
        return text.substring(begin, end);
    }

    /**
     * 写广本到文件
     *
     * @param content
     * @param charset
     * @param file
     */
    public static void writeStringToFile(String content, String charset, File file) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (TextUtils.isEmpty(charset)) {
            charset = DEF_CHARSET;
        }
        FileOutputStream fos = null;
        try {
            if (!file.exists() && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(content.getBytes(charset));
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }
}
