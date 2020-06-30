package com.youloft.senior.bean

/**
 * 赞
 * @property avatar String
 * @property commentUserAvatar String
 * @property commentUserId String
 * @property commentUserNickname String
 * @property content String
 * @property createTime String
 * @property nickname String
 * @property postType Int
 * @property praiseType Int
 * @property praiseUserAvatar String
 * @property praiseUserId String
 * @property praiseUserNickname String
 * @property textContent String
 * @property userId String
 * @constructor
 */
data class Praise(
    val avatar: String,
    val commentUserAvatar: String,
    val commentUserId: String,
    val commentUserNickname: String,
    val content: String,
    val createTime: String,
    val nickname: String,
    val postType: Int,
    val postId: String,
    val praiseType: Int,
    val praiseUserAvatar: String,
    val praiseUserId: String,
    val praiseUserNickname: String,
    val textContent: String,
    val userId: String
)