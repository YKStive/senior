package com.youloft.senior.bean

/**
 * @author you
 * @create 2020/6/22
 * @desc
 */
enum class PostType(val type: Int) {
    //图文
    IMAGE_TEXT(0),

    //视频
    VIDEO(1),

    //gif图
    GIF(2),

    //本地影集
    LOCAL_ALBUM(3),

    //签到
    PUNCH(4),

    //邀请好友
    INVITE(5)
}