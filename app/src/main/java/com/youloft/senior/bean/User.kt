package com.youloft.senior.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * @author you
 * @create 2020/6/28
 * @desc
 */
data class User(
    val avatar: String,
    val createTime: String,
    val id: String,
    val nickname: String,
    val userId: String
) : Serializable {
    companion object {
        fun test(): User {
            return User(
                "https://pic4.zhimg.com/v2-5fd62f20577029e9418233411e3ef24f_b.jpg",
                "1992-02-00",
                "123",
                "肯打鸡",
                "123"
            )
        }
    }
}