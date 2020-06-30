package com.youloft.senior.ui.graphic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.coolktx.launchIO
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.bean.Post
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.POST

/**
 * @author you
 * @create 2020/6/30
 * @desc 上传帖子
 */
class PostViewModel : BaseViewModel() {

    val liveData = MutableLiveData<BaseUiModel<String>>()

    /**
     * 上传文件
     * @param filePaths MutableList<String> 文件路径
     * @return List<String> 远程地址列表
     */
    private fun uploadFile(filePaths: MutableList<String>): List<String> {
        return arrayListOf()
    }

    /**
     * 上传帖子
     * @param post Post 帖子
     * @param filePaths MutableList<String> 文件地址列表
     */
    fun publishPost(post: Post, filePaths: MutableList<String>) {
        liveData.value = BaseUiModel(showLoading = true)
        viewModelScope.launchIO(onError = {
            liveData.value = BaseUiModel(showLoading = false, showError = "上传失败")
        }) {
            var fileRemoteResult: List<String> = listOf()
            if (filePaths.isNotEmpty()) {
                fileRemoteResult = uploadFile(filePaths)
            }
            post.mediaContent = fileRemoteResult
            val result = ApiHelper.api.publishPost(post)
            withContext(Dispatchers.Main) {
                ApiHelper.executeResponse(result, {
                    liveData.value = BaseUiModel(showLoading = false)
                }, {
                    liveData.value = BaseUiModel(showLoading = false, showError = "上传失败")
                })
            }
        }
    }

}