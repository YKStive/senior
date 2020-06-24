package com.youloft.senior.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author you
 * @create 2020/6/18
 * @desc 图片地址+时间
 */
@Parcelize
data class ImageCursor(var date: Long, var path: String) : Parcelable {
}