package com.youloft.senior.ui.gif

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.CheckBox
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.youloft.coolktx.dp2px
import com.youloft.core.base.BaseActivity
import com.youloft.core.jump.JumpResult
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.itembinder.ChoiceMultiImageItemBinder
import com.youloft.senior.itembinder.ChoiceSingleImageItemBinder
import com.youloft.senior.widgt.GridSpaceItemDecoration
import com.youloft.senior.widgt.ItemViewHolder
import kotlinx.android.synthetic.main.activity_choice_image.*
import kotlinx.android.synthetic.main.activity_content_publish.*

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
            finishWithResult((mItems.filter {
                it.isSelected
            }) as ArrayList<ImageRes>)

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
            if (mCount == 1) {
                register(
                    ImageRes::class,
                    ChoiceSingleImageItemBinder(mItems)
                )
            } else {
                val choiceMultiImageItemBinder = ChoiceMultiImageItemBinder(mItems)
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
                getImages()
            }
            .onDenied {
                Log.d(TAG, "please authorize sd card permissions")
            }
            .start()
    }

    private fun getImages() {
        mItems.clear()
        val resolver = contentResolver
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
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
        ) ?: return
        var path: String?

        while (cursor.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            if (path.isNullOrEmpty()) continue

            mItems.add(ImageRes(path))
        }
        cursor.close()

        mAdapter.notifyDataSetChanged()
    }

    companion object {
        private var mCount: Int = 1
        private const val KEY_RESULT: String = "images"
        fun start(
            context: FragmentActivity,
            count: Int = 1,
            onResult: (bean: ArrayList<ImageRes>) -> Unit
        ) {
            this.mCount = count
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