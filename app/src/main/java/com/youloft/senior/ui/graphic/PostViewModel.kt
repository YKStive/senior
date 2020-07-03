package com.youloft.senior.ui.graphic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.coolktx.launchIO
import com.youloft.coolktx.mapInPlace
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.bean.Post
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.qiniu.UploadFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * @author you
 * @create 2020/6/30
 * @desc 上传帖子
 */
class PostViewModel : BaseViewModel() {

    val liveData = MutableLiveData<BaseUiModel<String>>()

    /**
     * 上传帖子
     * @param post Post 帖子
     * @param filePaths MutableList<String> 文件地址列表
     */
    fun publishPost(post: Post, filePaths: List<String>) {
        liveData.value = BaseUiModel(showLoading = true)
        viewModelScope.launchIO(onError = {
            liveData.value = BaseUiModel(showLoading = false, showError = it.message)
        }) {
            val filter = filePaths.filter {
                !it.startsWith("http")
            }
            if (filter.isNotEmpty()) {
                UploadFileManager.uploadFile(filter, {
                    viewModelScope.launchIO {
                        post.mediaContent = it
                        realPost(post)
                    }
                }, {
                    throw IOException(it)
                })
            } else {
                realPost(post)
            }
        }
    }

    private suspend fun realPost(post: Post) {
        val result = ApiHelper.api.publishPost(post)
        withContext(Dispatchers.Main) {
            ApiHelper.executeResponse(result, {
                liveData.value = BaseUiModel(showLoading = false, isSuccess = true)
            }, {
                liveData.value = BaseUiModel(showLoading = false, showError = it)
            })
        }
    }

}