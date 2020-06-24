package com.youloft.senior.ui.gif

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseViewModel
import com.youloft.senior.base.App
import com.youloft.senior.utils.gifmaker.GifMaker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.zibin.luban.Luban

/**
 * @author you
 * @create 2020/6/24
 * @desc
 */
class GifViewModel : BaseViewModel() {
    //结果
    var resultData = MutableLiveData<String>()

    /**
     * 处理资源照片，压缩、裁剪
     * @param path String
     */
    fun getData(resPath: String, tempPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val submit =
                Glide.with(App.instance()).asGif().load(tempPath).submit()
            val tempGifDrawable = submit.get()
            val resBitmap = fixRes(resPath, tempGifDrawable.intrinsicWidth)

            GifMaker().makeNeGif(this, App.instance(), tempGifDrawable, resBitmap) {
                resultData.value = it
            }

        }


    }

    /**
     * 处理照片，压缩、裁剪
     * @param resPath String
     * @return Bitmap
     */
    private fun fixRes(resPath: String, targetWith: Int): Bitmap {
        //压缩
        val compressPath = Luban.with(App.instance()).load(resPath).get()[0].absolutePath

        val compressBitmap = BitmapFactory.decodeFile(compressPath)

        return compressBitmap

    }

}