package com.youloft.senior.bean


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/24 19:11
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/24 19:11
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */


data class CommentData(
    var avatar: String,
    var content: String,
    var createTime: String,
    var id: String,
    var nickname: String,
    var userId: String
)