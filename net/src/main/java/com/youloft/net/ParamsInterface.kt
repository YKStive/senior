package com.youloft.net

import okhttp3.HttpUrl

/**
 * @author xll
 * @date 2020/6/23 14:41
 */

interface ParamsInterface {
    /**
     * 添加公共参数接口
     * params 为已有的参数
     */
    fun bindParams(urlBuilder: HttpUrl.Builder, params: Set<String>?)
}