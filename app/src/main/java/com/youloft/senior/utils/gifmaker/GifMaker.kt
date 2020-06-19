package com.youloft.senior.utils.gifmaker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.resource.gif.GifDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        context: Context,
        template: GifDrawable,
        resource: Bitmap,
        onSuccess: (path: String?) -> Unit
    ) {

        GlobalScope.launch(Dispatchers.IO) {
            val gifDecoder = getGifDecoder(template)
            gifDecoder?.apply {
                val frames = mutableListOf<ResFrame>()
                for (i in 0..template.frameCount) {
                    val nextFrame = nextFrame
                    nextFrame?.let {

                        val resourceDrawable = BitmapDrawable(context.resources, resource)
                        val templateFrameDrawable = BitmapDrawable(context.resources, it)

                        resourceDrawable.setBounds(0, 0, 200, 200)
                        templateFrameDrawable.setBounds(0, 0, 200, 200)

                        val bitmap: Bitmap = Bitmap.createBitmap(
                            200,
                            200,
                            Bitmap.Config.RGB_565
                        )
                        val canvas = Canvas(bitmap)

                        resourceDrawable.draw(canvas)
                        templateFrameDrawable.draw(canvas)

                        val path = IOTool.saveBitmap2Box(context, bitmap, "pic_$i")
                        frames.add(ResFrame(getDelay(i), path))
                    }
                    advance()
                }

                val newGifPath = genGifByFramesWithGPU(context, frames)
                withContext(Dispatchers.Main) {
                    onSuccess.invoke(newGifPath)
                }
            }
        }
    }

    private fun genGifByFramesWithGPU(context: Context, frames: List<ResFrame>): String {
        val path = IOTool.provideRandomPath("test")
        val os = FileOutputStream(File(path))
        val animatedGIFWriter = AnimatedGIFWriter()
        animatedGIFWriter.prepareForWrite(os, -1, -1)
        for (value in frames) {
            val bitmap = BitmapFactory.decodeFile(value.path)
            animatedGIFWriter.writeFrame(os, bitmap, value.delay)
        }
        animatedGIFWriter.finishWrite(os)
        IOTool.notifySystemGallery(context, path)
        return path
    }

    data class ResFrame(var delay: Int, var path: String)

}