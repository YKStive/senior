package com.youloft.senior.utils.gifmaker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.resource.gif.GifDrawable
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * @author you
 * @create 2020/6/18
 * @desc gif生成器
 */
class GifMaker {

    /**
     * 获取gifLoader
     * @param resource GifDrawable gif资源
     */
    private fun getGifDecoder(resource: GifDrawable): GifDecoder? {
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
        scope: CoroutineScope,
        context: Context,
        template: GifDrawable,
        resource: Bitmap,
        onSuccess: (path: String?) -> Unit
    ) {

        scope.launch(Dispatchers.IO) {
            val taskTime = TaskTime()
            val gifDecoder = getGifDecoder(template)
            gifDecoder?.apply {
                val path = IOTool.provideRandomPath("test")
                val os = FileOutputStream(File(path))
                val animatedGIFWriter = AnimatedGIFWriter()
                animatedGIFWriter.prepareForWrite(os, -1, -1)

                val bitmap: Bitmap = Bitmap.createBitmap(
                    200,
                    200,
                    Bitmap.Config.RGB_565
                )
                val canvas = Canvas(bitmap)
                for (i in 0..template.frameCount) {
                    val nextFrame = nextFrame
                    nextFrame?.let {
                        val resourceDrawable = BitmapDrawable(context.resources, resource)
                        val templateFrameDrawable = BitmapDrawable(context.resources, it)

                        resourceDrawable.setBounds(0, 0, 200, 200)
                        templateFrameDrawable.setBounds(0, 0, 200, 200)

                        resourceDrawable.draw(canvas)
                        templateFrameDrawable.draw(canvas)

                        animatedGIFWriter.writeFrame(os, bitmap, getDelay(i))
                    }
                    advance()
                }

                animatedGIFWriter.finishWrite(os)
                taskTime.release("makeNeGif---${template.frameCount}")
                IOTool.notifySystemGallery(context, path)
                withContext(Dispatchers.Main) {
                    onSuccess.invoke(path)
                }
            }
        }
    }


}