package com.youloft.senior.bean

/**
 * @author you
 * @create 2020/6/30
 * @desc 未读的评论
 */
data class Comment(
    val avatar: String,
    val commentUserAvatar: String,
    val commentUserId: String,
    val commentUserNickname: String,
    val content: String,
    val createTime: String,
    val nickname: String,
    val postType: Int,
    val postId: String,
    val textContent: String,
    val userId: String
)