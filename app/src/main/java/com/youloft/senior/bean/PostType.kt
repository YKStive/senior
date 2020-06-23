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
        const val VIDEO = 1
        const val GIF = 2
        const val ALBUM = 3
        const val LOCAL_ALBUM = 4
        const val PUNCH = 5
        const val INVITE = 6

    }

}