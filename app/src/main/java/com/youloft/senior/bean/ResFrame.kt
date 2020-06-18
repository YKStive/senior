package com.youloft.senior.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author you
 * @create 2020/6/18
 * @desc gif每帧数据
 */
@Parcelize
data class ResFrame(var delay: Int?, var path: String) : Parcelable