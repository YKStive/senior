package com.youloft.senior.itembinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.youloft.senior.R
import com.youloft.senior.base.App
import com.youloft.senior.bean.Post
import com.youloft.senior.utils.isByUser
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.home_post_bottom_share.view.*
import kotlinx.android.synthetic.main.item_post_header_other.view.*
import kotlinx.android.synthetic.main.item_post_remote.view.*
import kotlinx.android.synthetic.main.item_post_punch.view.*
import kotlin.random.Random

/**
 * @author you
 * @create 2020/6/22
 * @desc 签到item
 */
open class PunchViewBinder(
    val onPunch: (btnPunch: Button) -> Unit

) :
    ItemViewBinder<Post, PunchViewBinder.ViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        val root: ConstraintLayout =
            inflater.inflate(R.layout.item_post_punch, parent, false) as ConstraintLayout
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, item: Post
    ) {

        holder.itemView.run {

            iv_avatar.setImageResource(R.drawable.ic_placeholder_error)
            tv_name.text = context.resources.getString(R.string.punch_every_day_money)
            tv_view_amount.text = String.format(
                App.instance().resources.getString(R.string.punch_amount),
                Random.nextInt(100000, 300000)
            )


            btn_punch.run {
                setOnClickListener {
                    onPunch(this)
                }
            }


        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }


}