package com.youloft.net.bean


/**
 *
 * @Description:  获取评论列表
 * @Author:         slh
 * @CreateDate:     2020/6/23 10:07
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 10:07
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
data class CommentBean(
    var avatar: String,
    var content: String,
    var createTime: String,
    var id: String,
    var isPraised: Boolean,
    var nickname: String,
    var praised: Int,
    var userId: String
)

