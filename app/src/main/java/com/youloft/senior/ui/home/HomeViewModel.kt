package com.youloft.senior.ui.home

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.coolktx.launchIO
import com.youloft.coolktx.mapInPlace
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageCursor
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.Post.Companion.inviteData
import com.youloft.senior.bean.Post.Companion.localAlbumData
import com.youloft.senior.bean.Post.Companion.punchData
import com.youloft.senior.bean.PostType
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.logD
import com.youloft.senior.utils.moreThanOneDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import java.util.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 首页viewModel
 */
class HomeViewModel : BaseViewModel() {
    private var isPunch by Preference(Preference.IS_PUNCH, false)
    private val albumData: List<List<String>>? by lazy {
        getAlbumDate()
    }
    private var lastPublishAlbumTime by Preference(Preference.LAST_PUBLISH_ALBUM_TIME, "")
    private val mPosts = mutableListOf<Post>()
    private val _data: MutableLiveData<BaseUiModel<List<Post>>> = MutableLiveData()
    val data: LiveData<BaseUiModel<List<Post>>>
        get() = _data

    fun getData(
        direction: Int? = null,
        userId: String? = null
    ) {

        viewModelScope.launchIO(onError = {
            viewModelScope.emitLiveData(showLoading = false, showError = it.message ?: "拉取数据异常")
        }) {
            //每次拉取数据后，把强插的数据清空后，再次强插
            emitLiveData(showLoading = true)
            val index = initIndex(if (mPosts.isEmpty()) null else direction)
            val result = ApiHelper.api.getPosts(index, direction, limit = 30, userId = userId)
            ApiHelper.executeResponse(result, { newPosts ->
                if (newPosts.isNotEmpty()) {
                    clearFocusInsertItems()
                    addNewData(direction, newPosts)
                    focusInsert()
                }
                emitLiveData(showLoading = false, showSuccess = mPosts)
            }, { errorMsg ->
                emitLiveData(showLoading = false, showError = errorMsg)
            })

        }
    }

    /**
     * 清除强插的某种类型的item
     * @param postType Int item的类型
     */
    fun clearPost(postType: Int) {
        mPosts.mapInPlace { post ->
            if (post.postType != postType) post else post.apply {
                this.postType = PostType.HINT
            }
        }
        viewModelScope.emitLiveData(showSuccess = mPosts)
    }

    private fun initIndex(direction: Int?): String? {
        return when (direction) {
            0 -> mPosts[0].id
            1 -> mPosts[mPosts.size - 1].id
            else -> null

        }
    }

    /**
     * 添加新的数据
     * @param direction Int? 方向
     * @param newPosts MutableList<Post> 新帖子
     */
    private fun addNewData(
        direction: Int?,
        newPosts: MutableList<Post>
    ) {
        when (direction) {
            null, 1 -> mPosts.addAll(newPosts)
            0 -> mPosts.addAll(0, newPosts)
        }
    }

    /**
     * 清除强插的条目
     */
    private fun clearFocusInsertItems() {
        mPosts.let {
            val filter = it.filter { post ->
                post.id.isNotBlank()
            }
            it.clear()
            it.addAll(filter)
        }
    }

    private fun CoroutineScope.emitLiveData(
        showLoading: Boolean = false,
        isSuccess: Boolean = false,
        showError: String? = null,
        showSuccess: List<Post>? = null,
        showEnd: Boolean = false, // 加载更多
        isRefresh: Boolean = false
    ) {
        this.launch {
            withContext(Dispatchers.Main) {
                _data.value =
                    BaseUiModel(showLoading, isSuccess, showError, showSuccess, showEnd, isRefresh)
            }
        }
    }

    /**
     * 强插逻辑
     */
    private fun focusInsert() {
        val size = mPosts.size
        if (size < 10) {
            return
        }

        //已签到
        if (isPunch) {
            //影集数据不为空，如果未发布或者发布时间间隔1天,需要插入影集和邀请
            if (!albumData.isNullOrEmpty() && (lastPublishAlbumTime.isEmpty() || Calendar.getInstance()
                    .moreThanOneDay(lastPublishAlbumTime))
            ) {
                var albumIndex = 0
                for (index in 10..size step 10) {
                    when (index / 10 % 2) {
                        //插入影集
                        1 -> {
                            mPosts.add(
                                index,
                                localAlbumData.apply { mediaContent = albumData!![albumIndex] })
                            albumIndex++
                            if (albumIndex >= albumData!!.size) {
                                albumIndex = 0
                            }
                        }
                        //插入邀请
                        0 -> {
                            mPosts.add(index, inviteData)
                        }
                    }

                }

            } else {
                //只插入邀请item
                for (index in 10..size step 10) {
                    mPosts.add(index, inviteData)
                }
            }

        }


        //未签到
        else {
            //影集数据不为空，如果未发布或者发布时间间隔1天,需要插入签到、影集、邀请
            if (!albumData.isNullOrEmpty() && (lastPublishAlbumTime.isEmpty() || Calendar.getInstance()
                    .moreThanOneDay(lastPublishAlbumTime))
            ) {
                var albumIndex = 0
                for (index in 10..size step 10) {
                    when (index / 10 % 3) {
                        //插入邀请
                        0 -> {
                            mPosts.add(index, inviteData)
                        }
                        //插入影集
                        2 -> {
                            mPosts.add(
                                index,
                                localAlbumData.apply { mediaContent = albumData!![albumIndex] })
                            albumIndex++
                            if (albumIndex >= albumData!!.size) {
                                albumIndex = 0
                            }
                        }

                        //插入签到
                        1 -> {
                            mPosts.add(index, punchData)
                        }

                    }

                }

            } else {
                //只插入签到和邀请
                for (index in 10..size step 10) {
                    when (index / 10 % 2) {
                        //插入签到
                        1 -> {
                            mPosts.add(index, punchData)
                        }
                        //插入邀请
                        0 -> {
                            mPosts.add(index, inviteData)
                        }
                    }
                }
            }
        }

    }

    /**
     * 获取影集照片的地址集合
     */
    private fun getAlbumDate(): List<List<String>>? {
        val imagePathByCursor = getImagePathByCursor()
        return filterCursor(imagePathByCursor)
    }

    /**
     * 过滤数据库图片
     * @param imagePathByCursor MutableList<ImageCursor>
     * @return List<List<String>>?
     */
    private fun filterCursor(imagePathByCursor: MutableList<ImageCursor>): List<List<String>>? {
        val publishedAlbumTime by Preference<List<String>>(
            Preference.PUBLISHED_ALBUM_TIME,
            listOf()
        )
        //todo 从已经发表的剔除
//        imagePathByCursor.filter {
//
//        }
        val result = mutableListOf<MutableList<String>>()
        val temp = mutableListOf<String>()
        var now = Calendar.getInstance().timeInMillis
        for (imageCursor in imagePathByCursor) {
            //如果跟已经发表的时间重复，则跳过

            //如果已经有三个时间段，退出循环
            if (result.size == 3) {
                break
            }
            if (com.youloft.util.DateUtil.isSameDay(imageCursor.date, now)) {
                temp.add(imageCursor.path)
                continue
            } else {

                //取前20张图片
                if (temp.size > 3) {
                    val limiter = if (temp.size > 20) temp.subList(0, 20) else temp
                    result.add(limiter.toMutableList())
                }
                //时间增加一天，继续遍历
                else {
                    now -= 24 * 3600 * 1000
                }

                temp.clear()
                continue
            }
        }

        return result
    }

    /**
     * 取出相册图片
     * @return MutableList<ImageCursor>
     */
    private fun getImagePathByCursor(): MutableList<ImageCursor> {

        val cursorResult = mutableListOf<ImageCursor>()
        val resolver = App.instance().contentResolver
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED
        )


        val where =
            StringBuilder().append(MediaStore.Images.Media.DATE_ADDED + ">=? and ")
                .append(MediaStore.Images.Media.MIME_TYPE + "=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE + "=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE + "=? ")


        //6个月的秒数
        val sixMonthLong = 15552000
        val startTime =
            Calendar.getInstance().timeInMillis / 1000 - sixMonthLong
        startTime.toString().logD("6个月前秒数")
        val whereArgs = arrayOf(startTime.toString(), "image/jpeg", "image/png", "image/jpg")

        val cursor = resolver.query(
            uri,
            projection,
            where.toString(),
            whereArgs,
            MediaStore.Images.Media.DATE_MODIFIED + " desc "
        )
        var path: String?
        var date: String?
        while (cursor!!.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
            if (path.isNullOrEmpty()) continue
            cursorResult.add(ImageCursor(date.toLong() * 1000, path))
        }
        cursor.close()

        return cursorResult
    }
}