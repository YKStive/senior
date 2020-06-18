package com.youloft.senior.ui.gif

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.yanzhenjie.permission.runtime.Permission.READ_EXTERNAL_STORAGE
import com.youloft.core.base.BaseActivity
import com.youloft.senior.R
import com.youloft.senior.bean.ResFrame
import com.youloft.senior.itembinder.ChoiceImageItemBinder
import kotlinx.android.synthetic.main.activity_choice_image.*

class ChoiceImageActivity : BaseActivity() {

    private var mItems = mutableListOf<ResFrame>()
    private var mAdapter = MultiTypeAdapter(mItems)

//    private val mStateView by lazy {
//        NiceStateView.builder()
//            .registerLoading(NiceSampleLoadingView())
//            .wrapContent(rv_images)
//    }


    override fun getLayoutResId(): Int {
        return R.layout.activity_choice_image
    }

    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }

        mAdapter.register(
            ResFrame::class,
            ChoiceImageItemBinder(onItemClick = { position, item ->
                val data = Intent()
                data.putExtra("image_item", item)
                setResult(Activity.RESULT_OK, data)
                finish()
            })
        )
        rv_images.run {
            layoutManager = GridLayoutManager(applicationContext, 4)
            adapter = mAdapter
        }
    }

    override fun initData() {
        reqPermissions()
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
//                toast("please authorize sd card permissions")
            }
            .start()
    }

    private fun getImages() {
//        mStateView.showLoading()
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
        )
            ?: return
//        var name: String?
        var path: String?
        var bucketName: String?

        while (cursor.moveToNext()) {
//            name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            bucketName =
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))

            if (path.isNullOrEmpty()) continue

            mItems.add(ResFrame(null, path))
        }
        cursor.close()

//        mStateView.showContent()
        mAdapter.notifyDataSetChanged()
    }

    companion object {
        fun start(
            context: FragmentActivity,
            onResult: (bean: ResFrame) -> Unit
        ) {
        }
    }
}