package com.youloft.senior.bean

import android.os.Parcelable
import com.youloft.coolktx.dp2px
import com.youloft.senior.base.App
import com.youloft.util.UiUtil
import java.io.Serializable

data class Post(
    var height: Int = 200.dp2px,
    var width: Int = UiUtil.getScreenHeight(App.instance()),
    var avatar: String = App.instance().avatar,
    var createTime: String = "",
    var id: String = "",
    var mediaContent: List<String> = arrayListOf(),
    var nickname: String = App.instance().nickName,
    var postType: Int = 0,

    var praised: Int = 0,
    var template: String = "",
    var templateId: Int = 0,
    var textContent: String = "",
    var userId: String = App.instance().userId,
    var viewed: Int = 0,
    var commented: Int = 20,
    var isPraised: Boolean = true

) : Serializable {


    companion object {

        var imagePath =
            "https://pic-bucket.ws.126.net/photo/0009/2019-04-19/ED4HKOFA0AI20009NOS.jpg"

        var videoPath =
            "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"

        var gifPath =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Rotating_earth_%28large%29.gif/200px-Rotating_earth_%28large%29.gif"
        var avatar = "https://pic4.zhimg.com/v2-5fd62f20577029e9418233411e3ef24f_b.jpg"

        //        var avatar = gifPath
        var multiData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar,
            "1992-11-25",
            "999",
            arrayListOf(
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

        var albumData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar,
            "1992-11-25",
            "999",
            arrayListOf(
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


        var singleData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar,
            "1992-11-25",
            "999",
            arrayListOf(
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

        var videoData: Post = Post(
            500.dp2px,
            200.dp2px,
            avatar,
            "1992-11-25",
            "999",
            arrayListOf(
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

        var gifData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar,
            "1992-11-25",
            "999",
            arrayListOf(
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

        var localAlbumData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar, "", "", arrayListOf(
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath,
                imagePath
            ), "", PostType.LOCAL_ALBUM, 0,
            "", 0, "", "", 0
        )

        var inviteData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar, "", "", arrayListOf(""), "", PostType.INVITE, 0,
            "", 0, "", "", 0
        )

        var punchData: Post = Post(
            200.dp2px,
            UiUtil.getScreenHeight(App.instance()),
            avatar, "", "", arrayListOf(""), "", PostType.PUNCH, 0,
            "", 0, "", "", 0
        )
    }
}