package com.youloft.senior.bean


/**
 *
 * @Description:  帖子详情
 * @Author:         slh
 * @CreateDate:     2020/6/22 16:26
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/22 16:26
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
//{
// "id": "5eeb18dbc910966380f23028",//贴子id
// "userId": "string",//发贴者id
// "avatar": "string",//发贴者头
// "nickname": "string",//发贴者昵称
// "postType": 0,//贴子类型
// "textContent": "string",//文本内容
// "templateId": 0,//模版id
// "template": "string",//模版内容，表情时为底图
// "mediaContent": [
// "string"
// ],//流媒体，照片或视频集合
// "viewed": 0,//浏览次数
// "praised": 0,//点赞次数
// "createTime": "2020-06-18T15:25:40.865+08:00",//创建时间
//}
// data class ItemDetailBean(
//    var `data`: ItemData?,
//    var msg: String,
//    var sign: String,
//    var status: Int
//)

data class ItemData(
    var avatar: String,
    var commented: Int,
    var createTime: String,
    var id: String,
    var mediaContent: List<String>,
    var nickname: String,
    var postType: Int,
    var praised: Int,
    var template: String,
    var templateId: String,
    var textContent: String,
    var userId: String,
    var viewed: Int
)