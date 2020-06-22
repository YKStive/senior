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
    val userId: String,
    val viewed: Int
) {

    companion object {
        val testData: Post = Post(
            "", "1992-11-25", "999", listOf("1", "2"), "别名", 0, 20,
            "魔板内容", 898, "电视里的繁华盛开的粉红色跨境电啊就会受到老师", "123", 3000
        )
    }
}