package com.youloft.senior.bean


/**
 *
 * @Description:    点赞评论
 * @Author:         slh
 * @CreateDate:     2020/7/2 19:08
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/2 19:08
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
data class PraiseComment(
    var commentId: String,
    var commentUserAvatar: String,
    var commentUserId: String,
    var commentUserNickname: String,
    var content: String,
    var postId: String,
    var Avatar: String?,
    var userId: String,
    var nickname: String?,
    var PostUserId: String

)