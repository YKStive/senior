package com.youloft.senior.utils.gifmaker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.resource.gif.GifDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

/**
 * @author you
 * @create 2020/6/18
 * @desc
 */
class GifMaker {

    /**
     * 获取gifLoader
     * @param resource GifDrawable gif资源
     */
    fun getGifDecoder(resource: GifDrawable): GifDecoder? {
        var decoder: GifDecoder? = null
        val state = resource.constantState
        if (state != null) {
            val frameLoader = ReflectTool.getAnyByReflect(state, "frameLoader")
            if (frameLoader != null) {
                val any = ReflectTool.getAnyByReflect(frameLoader, "gifDecoder")
                if (any is GifDecoder) {
                    decoder = any
                }
            }
        }
        return decoder
    }

    /**
     * 合成新的gif
     * @param context Context
     * @param template GifDrawable  模板
     * @param resource Bitmap 选择的图片
     * @param onSuccess Function1<[@kotlin.ParameterName] String, Unit> 成功回调新gif路径
     */
    fun makeNeGif(
        context: Context,
        template: GifDrawable,
        resource: Bitmap,
        onSuccess: (path: String?) -> Unit
    ) {

        GlobalScope.launch(Dispatchers.IO) {
            val gifDecoder = getGifDecoder(template)

            gifDecoder?.apply {
                val os = ByteArrayOutputStream()
                val encoder = AnimatedGifEncoder()
                encoder.start(os)
                encoder.setRepeat(0)
                for (i in 0..template.frameCount) {
                    val nextFrame = nextFrame
                    nextFrame?.let {
                        val resourceDrawable = BitmapDrawable(context.resources, resource)
                        val templateFrameDrawable = BitmapDrawable(context.resources, it)
                        val bitmap: Bitmap = Bitmap.createBitmap(
                            resourceDrawable.intrinsicWidth,
                            resourceDrawable.intrinsicHeight,
                            Bitmap.Config.RGB_565
                        )
                        val canvas = Canvas(bitmap)
                        resourceDrawable.draw(canvas)
                        templateFrameDrawable.draw(canvas)

                        //绘制了模板和资源新图片插入到gif生成器
                        encoder.setDelay(getDelay(i))
                        encoder.addFrame(bitmap)
                    }
                }

                encoder.finish()
                val newGifPath = IOTool.saveStreamToSDCard("newGif", os)
                withContext(Dispatchers.Main) {
                    onSuccess.invoke(newGifPath)
                }
            }
        }
    }


}