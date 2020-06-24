package com.youloft.net.bean


/**
 *
 * @Description: 获取评论的头像
 * @Author:         slh
 * @CreateDate:     2020/6/23 18:31
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 18:31
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
data class FavoriteHeadBean(
    var `data`: List<FavoriteHeadData>,
    var msg: String,
    var sign: String,
    var status: Int
)

data class FavoriteHeadData(
    var avatar: String,
    var createTime: String,
    var id: String,
    var userId: String
)