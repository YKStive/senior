package com.youloft.senior.ui.gif

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.coolktx.toast
import com.youloft.core.base.BaseActivity
import com.youloft.core.jump.JumpResult
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.itembinder.ChoiceMultiImageItemBinder
import com.youloft.senior.itembinder.ChoiceSingleImageItemBinder
import com.youloft.senior.utils.logD
import com.youloft.senior.widgt.GridSpaceItemDecoration
import kotlinx.android.synthetic.main.activity_choice_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChoiceImageActivity : BaseActivity() {

    private val TAG = "ChoiceImageActivity"
    private var mItems = ArrayList<ImageRes>()
    private var mAdapter = MultiTypeAdapter(mItems)

    override fun getLayoutResId(): Int {
        return R.layout.activity_choice_image
    }

    override fun initView() {
        ic_back.setOnClickListener {
            finish()
        }

        btn_confirm.setOnClickListener {
            val filter = mItems.filter {
                it.isSelected
            }
            filter.toString().logD()
            finishWithResult(filter as ArrayList<ImageRes>)

        }


        rv_images.run {
            layoutManager = GridLayoutManager(applicationContext, 3)
            addItemDecoration(GridSpaceItemDecoration(3, 2.dp2px, 2.dp2px))
            adapter = mAdapter
            itemAnimator = null

        }
    }

    override fun initData() {
        reqPermissions()
        mAdapter.run {
            if (mCountLimit == 1) {
                register(
                    ImageRes::class,
                    ChoiceSingleImageItemBinder(mItems)
                )
            } else {
                val choiceMultiImageItemBinder = ChoiceMultiImageItemBinder(mCountLimit, mItems)
                choiceMultiImageItemBinder.selectedCount.observe(
                    this@ChoiceImageActivity,
                    Observer {
                        btn_confirm.text = "完成(${it})"
                    })
                register(
                    ImageRes::class,
                    choiceMultiImageItemBinder
                )
            }
        }
    }

    private fun finishWithResult(item: ArrayList<ImageRes>) {
        val data = Intent()
        data.putParcelableArrayListExtra(KEY_RESULT, item)
        setResult(Activity.RESULT_OK, data)
        finish()
    }


    private fun reqPermissions() {
        AndPermission.with(this)
            .runtime()
            .permission(
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE
            )
            .onGranted {
                getData()
            }
            .onDenied {
                Log.d(TAG, "please authorize sd card permissions")
            }
            .start()
    }

    private fun getData() {
        showLoading()
        lifecycleScope.launchIOWhenCreated(onError = {
            toast("查询异常")
        }) {
            val result = mutableListOf<ImageRes>()
            when (mType) {
                TYPE_ALL -> {
                    val images = getImages()
                    val video = getVideo()
                    sortByTime(result, images, video)

                }
                TYPE_IMAGE -> {
                    val images = getImages()
                    result.addAll(images)
                }
            }


            withContext(Dispatchers.Main) {
                dismissLoading()
                mItems.clear()
                mItems.addAll(result)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 视频、相片按时间排序
     * @param result MutableList<ImageRes> 结果
     * @param images List<ImageRes> 相片
     * @param video List<ImageRes> 视频
     * @return List<ImageRes> 结果
     */
    private fun sortByTime(
        result: MutableList<ImageRes>,
        images: List<ImageRes>,
        video: List<ImageRes>
    ): MutableList<ImageRes> {
        return result.apply {
            result.addAll(video)
            result.addAll(images)
        }
    }

    /**
     * 查询视频
     * @return List<ImageRes>
     */
    private fun getVideo(): List<ImageRes> {
        val result = mutableListOf<ImageRes>()
        val resolver = contentResolver
        val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Thumbnails._ID,
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_MODIFIED
        )
        val where =
            MediaStore.Images.Media.MIME_TYPE + "=?"

        val whereArgs = arrayOf("video/mp4")

        val mCursor = resolver?.query(
            videoUri,
            projection,
            where,
            whereArgs,
            MediaStore.Images.Media.DATE_MODIFIED + " desc "
        )

        mCursor?.apply {
            while (mCursor.moveToNext()) {
                val videoId =
                    mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID))
                val videoPath =
                    mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val videoDuration =
                    mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                val videoSize =
                    mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE)) / 1024
                val videoDisplayName =
                    mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                val videoUpdateTime =
                    mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))

                //提前获取缩略图
                MediaStore.Video.Thumbnails.getThumbnail(
                    resolver,
                    videoId.toLong(),
                    MediaStore.Video.Thumbnails.MICRO_KIND,
                    null
                )

                val thumbProj = arrayOf(
                    MediaStore.Video.Thumbnails._ID,
                    MediaStore.Video.Thumbnails.DATA
                )

                var thumbPath: String? = null
                val thumbCursor = resolver.query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbProj,
                    MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                    arrayOf(videoId.toString()),
                    null
                )
                thumbCursor?.apply {
                    while (thumbCursor.moveToNext()) {
                        thumbPath =
                            thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                    }
                    thumbCursor.close()
                    thumbPath?.apply {
                        ImageRes(this).run {
                            previewPath = videoPath
                            duration = videoDuration
                            type = ImageRes.TYPE_VIDEO
                            displayName = videoDisplayName
                            time = videoUpdateTime
                            result.add(this)

                        }
                    }
                }
            }
            close()
        }

        return result
    }

    /**
     * 获取相片
     * @return List<ImageRes>
     */
    private fun getImages(): List<ImageRes> {
        val result = mutableListOf<ImageRes>()
        val resolver = contentResolver
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED
        )

        val where =
            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?"

        val whereArgs = arrayOf("image/jpeg", "image/png", "image/jpg")

        val cursor = resolver?.query(
            uri,
            projection,
            where,
            whereArgs,
            MediaStore.Images.Media.DATE_MODIFIED + " desc "
        )

        cursor?.apply {
            var path: String?
            while (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val imageUpdateTime =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                if (path.isNullOrEmpty()) continue
                ImageRes(path).run {
                    type = ImageRes.TYPE_IMAGE
                    time = imageUpdateTime
                    result.add(this)
                }
            }
            cursor.close()
        }

        return result
    }

    companion object {
        private var mCountLimit: Int = 1
        private var mType: Int = -1
        private const val KEY_RESULT: String = "images"
        const val TYPE_ALL: Int = 1
        const val TYPE_IMAGE: Int = 2
        fun start(
            context: FragmentActivity,
            count: Int = 1,
            type: Int = TYPE_IMAGE,
            onResult: (
                bean: ArrayList<ImageRes>
            ) -> Unit
        ) {
            this.mCountLimit = count
            this.mType = type
            JumpResult(context).startForResult(ChoiceImageActivity::class.java) { requestCode, data ->
                data?.apply {
                    val imageRes = data.getParcelableArrayListExtra<ImageRes>(KEY_RESULT)
                    imageRes?.apply {
                        onResult.invoke(imageRes)
                    }
                }
            }
        }
    }
}