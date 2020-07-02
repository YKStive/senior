package com.youloft.senior.qiniu

import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import com.youloft.senior.net.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author you
 * @create 2020/7/2
 * @desc
 */

object UploadFileManager {

    private val token = "http://shequ.51wnl-cq.com/api/user/getqnuploadtoken"

    private val config: Configuration by lazy {
        Configuration.Builder()
            .connectTimeout(30)           // 链接超时。默认10秒
            .useHttps(true)               // 是否使用https上传域名
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .build()
    }


    private val uploadManager: UploadManager by lazy {
        UploadManager(config, 3)
    }


    suspend fun uploadFile(
        paths: List<String>,
        onComplete: (remotePath: List<String>) -> Unit,
        onError: ((msg: String) -> Unit)?
    ) {
        withContext(Dispatchers.IO) {
            val qnToken = ApiHelper.api.getQNToken()
            ApiHelper.executeResponse(qnToken, { tokenJson ->
                val token = tokenJson.getString("token")
                val baseUrl = tokenJson.getString("BaseUrl")
                val result = mutableListOf<String>()
                paths.forEach {
                    val file = File(it)
                    val key = createKey(it)
                    uploadManager.put(
                        file, key, token,
                        { remoteKey, info, _ ->
                            if (info.isOK) {
                                synchronized(UploadFileManager.javaClass) {
                                    result.add("${baseUrl}/${remoteKey}")
                                    if (result.size == paths.size) {
                                        onComplete(result)
                                    }
                                }
                            } else {
                                onError?.invoke("QN${info.error}")
                            }

                        }, null
                    )
                }
            }, {
                onError?.invoke("请求token异常--${it}")
            })
        }


    }

    private fun createKey(path: String): String {
        return if (path.contains("/") && path.contains(".")) {
            path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."))
        } else {
            path
        }
    }


}