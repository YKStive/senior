package com.youloft.senior.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author you
 * @create 2020/6/18
 * @desc 图片bean
 */
@Parcelize
data class ImageRes(var path: String) : Parcelable {
    var isSelected = false
}