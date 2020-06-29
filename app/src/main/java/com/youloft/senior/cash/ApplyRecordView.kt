package com.youloft.senior.cash

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.youloft.senior.R
import kotlinx.android.synthetic.main.money_apply_progress_record_item.view.*
import java.text.SimpleDateFormat

/**
 * @author xll
 * @date 2020/6/29 15:10
 */
internal class ApplyRecordView(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {
    init {
        orientation = VERTICAL
    }

    val datas = JSONArray()

    fun refreshData(datas: JSONArray?) {
        this.datas.clear()
        if (datas != null) {
            this.datas.addAll(datas)
        }
        notifyDataSetChanged()
    }

    private fun notifyDataSetChanged() {
        removeAllViews()
        if (this.datas.isEmpty()) {
            bindEmpty()
            return
        }
        for (i in 0 until datas.size) {
            bindItem(datas.getJSONObject(i), i == datas.size - 1)
        }
    }

    private fun bindItem(itemData: JSONObject, last: Boolean) {
        val holder = ViewHolder(this)
        holder.bindItem(itemData, last)
        addView(holder.itemView)
    }

    private fun bindEmpty() {
        val holder = EmptyHolder(this)
        addView(holder.itemView)
    }

    abstract class BaseViewHolder(parent: ViewGroup, layout: Int) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
    ) {

        abstract fun bindItem(itemData: JSONObject, last: Boolean);
    }

    internal inner class EmptyHolder(parent: ViewGroup) :
        BaseViewHolder(
            parent, R.layout.money_apply_progress_record_empty
        ) {
        override fun bindItem(itemData: JSONObject, last: Boolean) {
        }

    }

    internal inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder(
            parent, R.layout.money_apply_progress_record_item
        ) {

        override fun bindItem(itemData: JSONObject, last: Boolean) {
            itemView.historyTitle.text = itemData.getString("title")
            itemView.historyDate.text =
                    // HH:mm:ss
                itemData.getString("time").wrapDateFmt("yyyy-MM-dd", "MM-dd HH:mm")
                    .orEmpty()
            itemView.historyDetail.text = itemData.getString("txt")
            itemView.historyLine.visibility = if (last) View.VISIBLE else View.GONE
            itemView.historyLine2.visibility = if (last) View.GONE else View.VISIBLE
        }

    }

    /**
     * 转换日期格式
     */
    @SuppressLint("SimpleDateFormat")
    fun String?.wrapDateFmt(infmt: String, outFmt: String) =
        kotlin.runCatching {
            this?.let {
                SimpleDateFormat(outFmt).format(SimpleDateFormat(infmt).parse(it))
            }
        }.getOrNull()

}