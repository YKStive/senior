package com.youloft.senior.qiniu

import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UploadManager
import org.json.JSONObject
import java.io.File

/**
 * @author you
 * @create 2020/7/2
 * @desc
 */

object UploadFileManager {

    private val token = ""

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

    fun uploadFile(paths: List<String>) {
        paths.forEach {
            uploadManager.put(it, File(it).path, token,
                { key, info, response ->
                    if(info.isOK){
                        info.duration
                    }

                }, null
            )
        }
    }


}