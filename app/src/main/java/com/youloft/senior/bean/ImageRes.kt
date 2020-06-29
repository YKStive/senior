package com.youloft.senior.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author you
 * @create 2020/6/18
 * @desc 图片bean
 */
@Parcelize
data class ImageRes(
    var path: String,
    var isSelected: Boolean = false,
    var isAddIcon: Boolean = false,
    var previewPath: String? = null,
    var displayName: String? = null,
    var duration: Int? = null,
    var type: Int = TYPE_IMAGE,
    var time: Long = 0
) :
    Parcelable {

    companion object {
        const val TYPE_IMAGE = 0
        const val TYPE_VIDEO = 1
    }
}