package com.youloft.net.bean


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/23 14:49
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 14:49
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */


data class MineDataBean(
    var `data`: List<MineData>,
    var msg: String,
    var sign: String,
    var status: Int
)

data class MineData(
    var createTime: String,
    var id: String,
    var mediaContent: List<String>,
    var postType: Int,
    var praised: Int,
    var template: String,
    var templateId: Int,
    var textContent: String,
    var timestamps: Int,
    var viewed: Int
) {
    companion object {
        const val IMAGE_TYPE = 0;
        const val VIDEO_TYPE = 1;
        const val MOVIE_TYPE = 2;
        const val GIF_TYPE = 3;
    }
}