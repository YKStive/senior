package com.youloft.senior.bean

import com.youloft.senior.base.App

data class Post(
    val avatar: String = App.instance().avatar,
    val createTime: String = "",
    val id: String = App.instance().userId,
    var mediaContent: List<String> = listOf(),
    val nickname: String = App.instance().nickName,
    val postType: Int,
    val praised: Int = 0,
    val template: String = "",
    val templateId: Int = 0,
    val textContent: String = "",
    val userId: String = "",
    val viewed: Int = 0
) {


    companion object {

        val imagePath =
            "https://pic-bucket.ws.126.net/photo/0009/2019-04-19/ED4HKOFA0AI20009NOS.jpg"

        val videoPath =
            "https://haokan.baidu.com/v?vid=11226516248969224954&pd=bjh&fr=bjhauthor&type=video"

        val gifPath =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Rotating_earth_%28large%29.gif/200px-Rotating_earth_%28large%29.gif"
        var avatar = "https://pic4.zhimg.com/v2-5fd62f20577029e9418233411e3ef24f_b.jpg"
        val multiData: Post = Post(
            avatar,
            "1992-11-25",
            "999",
            listOf(
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath
            ),
            "别名",
            PostType.IMAGE_TEXT,
            20,
            "魔板内容",
            898,
            "电视里的繁华盛开的粉红色跨境电啊就会受到老师",
            "123",
            3000
        )

        val albumData: Post = Post(
            avatar,
            "1992-11-25",
            "999",
            listOf(
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath
            ),
            "别名",
            PostType.ALBUM,
            20,
            "魔板内容",
            898,
            "电视里的繁华盛开的粉红色跨境电啊就会受到老师",
            "123",
            3000
        )


        val singleData: Post = Post(
            avatar,
            "1992-11-25",
            "999",
            listOf(
                imagePath
            ),
            "别名",
            PostType.IMAGE_TEXT,
            20,
            "魔板内容",
            898,
            "电视里的繁华盛开的粉红色跨境电啊就会受到老师",
            "123",
            3000
        )

        val gifData: Post = Post(
            avatar,
            "1992-11-25",
            "999",
            listOf(
                gifPath
            ),
            "别名",
            PostType.GIF,
            20,
            "魔板内容",
            898,
            "电视里的繁华盛开的粉红色跨境电啊就会受到老师",
            "123",
            3000
        )

        val localAlbumData: Post = Post(
            avatar, "", "", listOf(
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath
            ), "", PostType.LOCAL_ALBUM, 0,
            "", 0, "", "", 0
        )

        val inviteData: Post = Post(
            avatar, "", "", listOf(""), "", PostType.INVITE, 0,
            "", 0, "", "", 0
        )

        val punchData: Post = Post(
            avatar, "", "", listOf(""), "", PostType.PUNCH, 0,
            "", 0, "", "", 0
        )
    }
}