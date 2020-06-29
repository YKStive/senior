package com.youloft.senior.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youloft.senior.R
import com.youloft.util.UiUtil

/**
 *
 * @Description:
 * @Author:         slh
 * @CreateDate:     2020/6/22 17:12
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/6/22 17:12
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class PictureAdapter(var context: Context, var data: List<String>) :
    RecyclerView.Adapter<PictureAdapter.MyViewHolder>() {
    lateinit var imageOpertor: ImageOpertor

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutInflater.from(context).inflate(R.layout.imageview, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        var params = holder.image.layoutParams
//        var params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,UiUtil.dp2Px(context,114f))
//        params.
//        holder.image.layoutParams=setViewMargin()
        Glide.with(context).load(data[position]).into(holder.image)
        holder.image.setOnClickListener {
            imageOpertor.showImage(data, position)
        }
    }

    interface ImageOpertor {
        fun showImage(imageData: List<String>, pos: Int)
    }

    fun setViewMargin(
        view: View?,
        left: Int,
        right: Int,
        top: Int,
        bottom: Int
    ): ViewGroup.LayoutParams? {
        if (view == null) {
            return null
        }
        val params = view.layoutParams
        var marginParams: MarginLayoutParams? = null
        //获取view的margin设置参数
        marginParams = if (params is MarginLayoutParams) {
            params
        } else {
            //不存在时创建一个新的参数
            MarginLayoutParams(params)
        }
        //设置margin
        marginParams!!.setMargins(left, top, right, bottom)
        view.layoutParams = marginParams
        return marginParams
    }

}