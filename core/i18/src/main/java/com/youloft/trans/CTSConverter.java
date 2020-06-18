package com.youloft.trans;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;


/**
 * 简繁转换
 * <p/>
 * Created by javen on 14-8-16.
 */
public class CTSConverter {

    private static HashMap<String, String> S2TMap = new HashMap<String, String>();

    private static HashMap<String, String> T2SMap = new HashMap<String, String>();

    public static void initConvert(Context context) {
        if (S2TMap.isEmpty() || T2SMap.isEmpty()) {
            S2TMap.clear();
            T2SMap.clear();
            String sStr = Strings.readStringFromAssets(context, "ts/SStr.txt", "utf-8", "");
            String tStr = Strings.readStringFromAssets(context, "ts/TStr.txt", "utf-8", "");
            String[] sStrArray = sStr.split(",");
            String[] tStrArray = tStr.split(",");
            fillMap(S2TMap, sStrArray, tStrArray);
            fillMap(T2SMap, tStrArray, sStrArray);
        }
    }

    /**
     * 填充map
     *
     * @param map
     * @param keys
     * @param values
     */
    public static void fillMap(Map map, Object[] keys, Object[] values) {
        if (map == null)
            return;
        int count = Math.min(keys.length, values.length);

        /**
         * Count
         */
        for (int i = 0; i < count; i++) {
            map.put(keys[i], values[i]);
        }
    }


    /**
     * 简体到繁体
     *
     * @param sStr
     * @return
     */
    public static String S2T(String sStr) {
        StringBuilder sb = new StringBuilder();
        String cha;
        for (int i = 0; i < sStr.length(); i++) {
            cha = Strings.subString(sStr, i, 1);
            if (S2TMap.containsKey(cha)) {
                sb.append(S2TMap.get(cha));
            } else {
                sb.append(cha);
            }
        }
        return sb.toString();
    }

    /**
     * 繁体繁体简体
     *
     * @param tStr
     * @return
     */
    public static String T2S(String tStr) {
        StringBuilder sb = new StringBuilder();
        String cha;
        for (int i = 0; i < tStr.length(); i++) {
            cha = Strings.subString(tStr, i, 1);
            if (T2SMap.containsKey(cha)) {
                sb.append(T2SMap.get(cha));
            } else {
                sb.append(tStr.charAt(i));
            }
        }
        return sb.toString();
    }

}
