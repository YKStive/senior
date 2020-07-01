package com.youloft.coolktx

import android.content.res.Resources
import android.util.TypedValue

/**
 * dp 2 px
 */
val Int.dp2px: Int
    get() = this.toFloat().dp2px.toInt()

val Long.dp2px: Long
    get() = this.toFloat().dp2px.toLong()

val Double.dp2px: Double
    get() = this.toFloat().dp2px.toDouble()

val Float.dp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

fun Int.addZero(): String {
    return if (this < 10) "0${this}" else this.toString()
}


/**
 * sp 2 px
 */
val Int.sp2px: Int
    get() = this.toFloat().sp2px.toInt()

val Long.sp2px: Long
    get() = this.toFloat().sp2px.toLong()

val Double.sp2px: Double
    get() = this.toFloat().sp2px.toDouble()

val Float.sp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

