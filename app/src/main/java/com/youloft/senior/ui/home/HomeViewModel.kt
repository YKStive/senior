package com.youloft.senior.ui.home

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.base.App
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.bean.Post
import com.youloft.senior.bean.Post.Companion.inviteData
import com.youloft.senior.bean.Post.Companion.localAlbumData
import com.youloft.senior.bean.Post.Companion.punchData
import com.youloft.senior.utils.DateUtil
import com.youloft.senior.utils.Preference
import com.youloft.senior.utils.moreThanOneDay
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author you
 * @create 2020/6/18
 * @desc 首页viewModel
 */
class HomeViewModel : BaseViewModel() {

    private var index = 1
    private val limit = 10
    private var isPunch by Preference(Preference.IS_PUNCH, false)
    private val albumData: List<List<String>>? by lazy {
        getAlbumDate()
    }
    private var lastPublishAlbumTime by Preference(Preference.LAST_PUBLISH_ALBUM_TIME, "")
    private val posts = mutableListOf<Post>()
    private val _data: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val data: LiveData<MutableList<Post>>
        get() = _data

    fun getData() {
        viewModelScope.launch {
            for (index in 0..30) {
                when {
                    index % 2 == 1 -> posts.add(Post.multiData)
                    index % 3 == 1 -> posts.add(Post.singleData)
                    index % 5 == 1 -> posts.add(Post.gifData)
                }
            }
            focusInsert()
            _data.value = posts
        }
    }

    /**
     * 强插逻辑
     */
    private fun focusInsert() {
        val size = posts.size
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
                            posts.add(index, localAlbumData)
                            albumIndex++
                            if (albumIndex >= albumData!!.size) {
                                albumIndex = 0
                            }
                        }
                        //插入邀请
                        0 -> {
                            posts.add(index, inviteData)
                        }
                    }

                }

            } else {
                //只插入邀请item
                for (index in 10..size step 10) {
                    posts.add(index, inviteData)
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
                            posts.add(index, inviteData)
                        }
                        //插入影集
                        2 -> {
                            posts.add(index, localAlbumData)
                            albumIndex++
                            if (albumIndex >= albumData!!.size) {
                                albumIndex = 0
                            }
                        }

                        //插入签到
                        1 -> {
                            posts.add(index, punchData)
                        }

                    }

                }

            } else {
                //只插入签到和邀请
                for (index in 10..size step 10) {
                    when (index / 10 % 2) {
                        //插入签到
                        1 -> {
                            posts.add(index, punchData)
                        }
                        //插入邀请
                        0 -> {
                            posts.add(index, inviteData)
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

//        getImagePathByCursor()
        val mutableListOf = mutableListOf<List<String>>()
        for (index in 0..2) {
            mutableListOf.addAll(listOf(listOf("1", "2", "3")))
        }
        return mutableListOf
    }

    private fun getImagePathByCursor() {
        val cursorResult = mutableListOf<String>()
        val resolver = App.instance().contentResolver
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )

        val where =
            MediaStore.Images.Media.DATE_ADDED + ">=? and" +
                    MediaStore.Images.Media.MIME_TYPE + "=? or " +
                    MediaStore.Images.Media.MIME_TYPE + "=? or " +
                    MediaStore.Images.Media.MIME_TYPE + "=?"

        val startTime =
            DateUtil.getMillon(Calendar.getInstance().timeInMillis - (6 * 30 * 24 * 3600))
        val whereArgs = arrayOf(startTime, "image/jpeg", "image/png", "image/jpg")

        val cursor = resolver.query(
            uri,
            projection,
            where,
            whereArgs,
            MediaStore.Images.Media.DATE_MODIFIED + " desc "
        )
        var path: String?
        while (cursor!!.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            if (path.isNullOrEmpty()) continue
            cursorResult.add(path)
        }
        cursor.close()
    }
}