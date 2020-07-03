package com.youloft.senior.bean


/**
 *
 * @Description:    点赞帖子传参
 * @Author:         slh
 * @CreateDate:     2020/7/2 18:42
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/2 18:42
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
data class PraiseBean(
    var avatar: String?,
    var nickname: String?,
    var postId: String,
    var postUserId: String?,
    var userId: String,
    var content: String=""
)