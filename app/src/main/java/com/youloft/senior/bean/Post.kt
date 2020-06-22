package com.youloft.senior.bean

data class Post(
    val avatar: String,
    val createTime: String,
    val id: String,
    val mediaContent: List<String>,
    val nickname: String,
    val postType: Int,
    val praised: Int,
    val template: String,
    val templateId: Int,
    val textContent: String,
    val timestamps: Int,
    val userId: String,
    val viewed: Int
)