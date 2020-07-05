package com.youloft.senior.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.net.bean.CommentBean
import com.youloft.senior.bean.FavoriteHeadBean
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.PraiseBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/7/2 15:00
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/7/2 15:00
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class DetailViewModel : BaseViewModel() {
    companion object {
        private const val TAG = "DetailViewModel"
    }

    //帖子信息
    var postInfo = MutableLiveData<ItemData>()

    //添加帖子赞
    var addOrCancleFavorite = MutableLiveData<String>()
    var addOrDeleteComment = MutableLiveData<String>()

    //结果
    var commentResultData = MutableLiveData<List<CommentBean>>()

    /**
     * 点赞帖子
     */
    fun addFavorite(params: PraiseBean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = ApiHelper.api.praisePost(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {
                        addOrCancleFavorite.value = res.data
                    }
                }
            } catch (e: Exception) {
                e.toString().logE(TAG)
            }
        }
    }

    /**
     * 添加帖子评论
     */
    fun comment(params: PraiseBean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = ApiHelper.api.commnet(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {
                        addOrDeleteComment.value = "发表评论"
                    }
                }
            } catch (e: Exception) {
                e.toString().logE(TAG)
            }
        }
    }

    /**
     * 获取帖子评论列表
     */
    fun getCommentList(
        params: Map<String, String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val listData = ApiHelper.api.getCommentList(params)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(listData, {
                    commentResultData.value = it
                })
            }

        }
    }

    /**
     * 帖子详情
     */
    fun getDetailData(
        id: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = ApiHelper.api.getItem(id)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(res, {
                    postInfo.value = it
                })
            }

        }
    }


    //点赞结果
    var favoriteResultData = MutableLiveData<List<FavoriteHeadBean>>()

    /**
     * 点赞评论
     */
    fun favorite(
        params: Map<String, String>,
        error: (msg: String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listData = ApiHelper.api.getFavoritetList(params)
                withContext(Dispatchers.Main) {
                    ApiHelper.executeResponse(listData, {
                        favoriteResultData.value = it
                    }, error)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error(e.message.toString())
                }
                e.message.toString().logE(TAG)
            }


        }
    }


}