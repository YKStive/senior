package com.youloft.senior.cash

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.youloft.senior.R
import kotlinx.android.synthetic.main.money_apply_progress_record_item.view.*
import java.text.SimpleDateFormat

/**
 * @author xll
 * @date 2020/6/28 16:36
 */
internal class ApplyProgressAdapter :
    RecyclerView.Adapter<ApplyProgressAdapter.BaseViewHolder>() {
    val datas = JSONArray()

    fun refreshData(datas: JSONArray?) {
        this.datas.clear()
        if (datas != null) {
            this.datas.addAll(datas)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        if (viewType == 1) {
            return EmptyHolder(parent)
        }
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int
    ) {
        if (holder is EmptyHolder) {
            return
        }
        holder.bindItem(datas.getJSONObject(position), position == itemCount - 1)
    }

    override fun getItemCount(): Int {
        return if (datas.size == 0) 1 else datas.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && !hasData()) {
            return 1;
        }
        return 0
    }

    fun hasData(): Boolean {
        return !datas.isEmpty()
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