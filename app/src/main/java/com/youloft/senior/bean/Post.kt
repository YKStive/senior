package com.youloft.senior.bean

import com.youloft.coolktx.dp2px
import com.youloft.senior.base.App
import com.youloft.util.UiUtil

data class Post(
    val height: Int = 200.dp2px,
    val width: Int = UiUtil.getScreenHeight(App.instance()),
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
    val viewed: Int = 0,
    val commented: Int = 20,
    val isPraised: Boolean = true

    ) {


    companion object {

        val imagePath =
            "https://pic-bucket.ws.126.net/photo/0009/2019-04-19/ED4HKOFA0AI20009NOS.jpg"

        val videoPath =
            "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"

        val gifPath =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Rotating_earth_%28large%29.gif/200px-Rotating_earth_%28large%29.gif"
        var avatar = "https://pic4.zhimg.com/v2-5fd62f20577029e9418233411e3ef24f_b.jpg"
//        var avatar = gifPath
        val multiData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
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
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
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
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
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

        val videoData: Post = Post(
            500.dp2px,
            200.dp2px,
            avatar,
            "1992-11-25",
            "999",
            listOf(
                videoPath
            ),
            "视频",
            PostType.VIDEO,
            20,
            "魔板内容",
            898,
            "这是测试视频~",
            "123",
            3000
        )

        val gifData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
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
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
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
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar, "", "", listOf(""), "", PostType.INVITE, 0,
            "", 0, "", "", 0
        )

        val punchData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar, "", "", listOf(""), "", PostType.PUNCH, 0,
            "", 0, "", "", 0
        )
    }
}