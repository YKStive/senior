package com.youloft.net.bean


/**
 *
 * @Description:     
 * @Author:         slh
 * @CreateDate:     2020/6/23 10:07
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/23 10:07
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
data class CommnetBean(
    var `data`: List<CommenttData>,
    var msg: String,
    var sign: String,
    var status: Int
)

data class CommenttData(
    var avatar: String,
    var content: String,
    var createTime: String,
    var id: String,
    var nickname: String,
    var userId: String
)