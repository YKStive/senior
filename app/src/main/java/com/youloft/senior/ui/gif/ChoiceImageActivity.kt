package com.youloft.senior.ui.gif

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.CheckBox
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.youloft.core.base.BaseActivity
import com.youloft.core.jump.JumpResult
import com.youloft.senior.R
import com.youloft.senior.bean.ImageRes
import com.youloft.senior.itembinder.ChoiceSingleImageItemBinder
import com.youloft.senior.widgt.ItemViewHolder
import kotlinx.android.synthetic.main.activity_choice_image.*

class ChoiceImageActivity : BaseActivity() {

    private val TAG = "ChoiceImageActivity"
    private var mItems = ArrayList<ImageRes>()
    private var mAdapter = MultiTypeAdapter(mItems)
    private lateinit var mBinder: ChoiceSingleImageItemBinder

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
            layoutManager = GridLayoutManager(applicationContext, 4)
            adapter = mAdapter
        }
    }

    override fun initData() {
        reqPermissions()
        mBinder =
            ChoiceSingleImageItemBinder(onItemClick = { old, new ->
                if (old == -1) {
                    mItems[new].isSelected = true
                    mAdapter.notifyItemChanged(new)
                    mBinder.lastSelectedPosition = new

                } else if (old == new) {
                    mItems[new].isSelected = !mItems[new].isSelected
                    mAdapter.notifyItemChanged(new)
                    if (mItems[new].isSelected) {
                        mBinder.lastSelectedPosition = new
                    } else {
                        mBinder.lastSelectedPosition = -1
                    }
                } else {
                    mItems[old].isSelected = false
                    mItems[new].isSelected = true
                    mAdapter.notifyItemChanged(old)
                    mAdapter.notifyItemChanged(new)
                    mBinder.lastSelectedPosition = new
                }
            })

        mAdapter.run {
            register(
                ImageRes::class,
                mBinder
            )
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