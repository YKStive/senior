package com.youloft.senior.bean

/**
 * @author you
 * @create 2020/6/22
 * @desc
 */
class PostType(val type: Int) {
    companion object {
        //图文
        const val IMAGE_TEXT = 0

        //视频
        const val VIDEO = 1

        //表情
        const val GIF = 2

        //影集
        const val ALBUM = 3

        //本地影集
        const val LOCAL_ALBUM = 4

        //签到
        const val PUNCH = 5

        //邀请
        const val INVITE = 6

        //隐藏
        const val HINT = 7

    }

}