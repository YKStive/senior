package com.youloft.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
 * @author xll
 * @date 2018/7/25 17:09
 */
public class DrawableUtil {
    /**
     * 给图片渲染颜色
     * @param context context
     * @param drawableId 渲染的drawable id
     * @param maskColorID 渲染的颜色id
     * @return
     */
    public static Drawable filter(Context context, int drawableId, int maskColorID) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        return filter(context, drawable, maskColorID);
    }

    /**
     * 给图片渲染颜色
     * @param context context
     * @param drawable 渲染的drawable
     * @param maskColorID 渲染的颜色id
     * @return
     */
    public static Drawable filter(Context context, Drawable drawable, int maskColorID) {
        drawable.mutate().setColorFilter(context.getResources().getColor(maskColorID), PorterDuff.Mode.MULTIPLY);
        return drawable;
    }
}
