package com.youloft.senior.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseViewModel
import com.youloft.net.bean.CommentBean
import com.youloft.senior.bean.ItemData
import com.youloft.senior.bean.PraiseBean
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.login.LoginDialog
import com.youloft.senior.utils.UserManager
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.logE
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.fragment_text_and_picture_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


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
    var addFavorite = MutableLiveData<Int>()
    var addComment = MutableLiveData<Int>()

    //结果
    var commentResultData = MutableLiveData<List<CommentBean>>()
    fun addFavorite(params: PraiseBean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = ApiHelper.api.parsePost(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {
                        addFavorite.value = 200
                    }
                }
            } catch (e: Exception) {
                e.toString().logE(TAG)
            }
        }
    }

    fun comment(params: PraiseBean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = ApiHelper.api.commnet(params)
                withContext(Dispatchers.Main) {
                    if (res.status == 200) {
                        addComment.value = 200
                    }
                }
            } catch (e: Exception) {
                e.toString().logE(TAG)
            }
        }
    }


    fun getCommentList(
        params: Map<String, String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
//            val listData = ApiHelper.api.getMineList(index, direction, limit)
            val listData = ApiHelper.api.getCommentList(params)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(listData, {
                    commentResultData.value = it
                })
            }

        }
    }


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


}