package com.youloft.senior.bean


/**
 *
 * @Description:     登录返回数据
 * @Author:         slh
 * @CreateDate:     2020/6/19 18:20
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/19 18:20
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
data class LoginBean(
    val accessToken: String,
    val expiration: Long,
    val refreshToken: String,
    val userId: String = "",
    val avatar: String? = "",
    val nickname: String? = ""
)

