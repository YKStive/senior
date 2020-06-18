package com.youloft.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Jie Zhang on 2016/5/19.
 */
public class ImageUtils {
    public static Bitmap readZoomImage(String path, int maxSize) {
        if (maxSize > 1024) {
            maxSize = 1024;
        }
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;

        Bitmap localBitmap = BitmapFactory.decodeFile(path, localOptions);
        int i = localOptions.outHeight > localOptions.outWidth ? localOptions.outHeight : localOptions.outWidth;
        int j = Math.round(i / maxSize);
        if (j < 1) {
            j = 1;
        }
        localOptions.inSampleSize = j;

        localOptions.inJustDecodeBounds = false;
        if (localBitmap != null) {
            localBitmap.recycle();
        }
        return BitmapFactory.decodeFile(path, localOptions);
    }

    public static byte[] bitmapToBytes(Bitmap paramBitmap, Bitmap.CompressFormat paramCompressFormat) {
        if (paramBitmap != null) {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            paramBitmap.compress(paramCompressFormat, 50, localByteArrayOutputStream);
            return localByteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public static String bitmapToString(String path, int maxSize) {
        Bitmap bitmap = readZoomImage(path, maxSize);
        byte[] arrayOfByte = bitmapToBytes(bitmap, Bitmap.CompressFormat.JPEG);
        return new String(Base64.encode(arrayOfByte, 0)).replaceAll("[\r|\n]", "");
    }
}
