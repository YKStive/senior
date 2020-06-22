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

        val multiData: Post = Post(
            "", "1992-11-25", "999", listOf("1", "2"), "别名", PostType.IMAGE_TEXT.type, 20,
            "魔板内容", 898, "电视里的繁华盛开的粉红色跨境电啊就会受到老师", "123", 3000
        )


        val localAlbumData: Post = Post(
            "", "", "", listOf(""), "", PostType.LOCAL_ALBUM.type, 0,
            "", 0, "", "", 0
        )

        val inviteData: Post = Post(
            "", "", "", listOf(""), "", PostType.INVITE.type, 0,
            "", 0, "", "", 0
        )

        val punchData: Post = Post(
            "", "", "", listOf(""), "", PostType.PUNCH.type, 0,
            "", 0, "", "", 0
        )
    }
}