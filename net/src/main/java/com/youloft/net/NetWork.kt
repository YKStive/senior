package com.youloft.net

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/19 18:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/19 18:29
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
object NetWork {

    suspend fun getLogin(params:Map<String,String>) =
        ApiHelper.api.login(params).await()



    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        it.resume(body)
                    } else {
                        it.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }
}