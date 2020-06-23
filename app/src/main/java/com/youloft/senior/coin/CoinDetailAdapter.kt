package com.youloft.senior.coin

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.youloft.senior.R
import kotlinx.android.synthetic.main.coin_detail_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author xll
 * @date 2020/6/23 16:13
 */
class CoinDetailAdapter :
    RecyclerView.Adapter<CoinDetailAdapter.ViewHolder>() {
    var datas: JsonArray? = null
    fun refreshData(array: JsonArray?) {
        datas = array
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindView(getItemData(position), getItemData(position - 1), getItemData(position + 1))
    }

    private fun getItemData(position: Int): JsonObject? {
        if (datas == null) {
            return null
        }
        if (datas!!.size() <= position) {
            return null
        }
        return if (position < 0) {
            null
        } else datas!![position].asJsonObject
    }

    override fun getItemCount(): Int {
        return if (datas == null) 0 else datas!!.size()
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.coin_detail_item_layout, parent, false)
    ) {
        fun bindView(
            item: JsonObject?,
            lastItem: JsonObject?,
            nextItem: JsonObject?
        ) {
            if (item == null) {
                return
            }
            itemView.item_content.text = getItemValueForString(item, "content", "codeDesc")
            itemView.item_time.text = getItemValueForString(item, "createTime", null)
            itemView.item_coin.text = getItemValueForString(item, "coin", null)

            val head = showHeader(item, lastItem)
            val foot = showFoot(item, nextItem)
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val time: Long =
                    format.parse(getItemValueForString(item, "createTime", null))!!.time
                val cCalendar = Calendar.getInstance()
                cCalendar.timeInMillis = time
                val format2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val format3 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                itemView.item_top_date.text = format2.format(cCalendar.timeInMillis)
                itemView.item_time.text = format3.format(cCalendar.timeInMillis)
            } catch (e: Exception) {

            }
            if (head && !foot) {
                itemView.top_group.visibility = View.VISIBLE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_top_bg)
            } else if (!head && foot) {
                itemView.top_group.visibility = View.GONE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_bottom_bg)
            } else if (!head && !foot) {
                itemView.top_group.visibility = View.GONE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_center_bg)
            } else {
                itemView.top_group.visibility = View.GONE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_all_bg)
            }
        }

        private fun showFoot(
            item: JsonObject,
            nextItem: JsonObject?
        ): Boolean {
            if (nextItem == null) {
                return true
            }
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val time: Long =
                    format.parse(getItemValueForString(item, "createTime", null))!!.time
                val lastTime = format.parse(getItemValueForString(item, "createTime", null))!!.time
                val cCalendar = Calendar.getInstance()
                cCalendar.timeInMillis = time
                val lCalendar = Calendar.getInstance()
                lCalendar.timeInMillis = lastTime
                if (cCalendar.get(Calendar.YEAR) == lCalendar.get(Calendar.YEAR)
                    && cCalendar.get(Calendar.MONTH) == lCalendar.get(Calendar.MONTH)
                    && cCalendar.get(Calendar.DAY_OF_MONTH) == lCalendar.get(Calendar.DAY_OF_MONTH)
                ) {
                    return false
                }
                return true
            } catch (e: Exception) {
                return false
            }
        }

        private fun showHeader(
            item: JsonObject,
            lastItem: JsonObject?
        ): Boolean {
            if (lastItem == null) {
                return true
            }
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val time: Long =
                    format.parse(getItemValueForString(item, "createTime", null))!!.time
                val lastTime = format.parse(getItemValueForString(item, "createTime", null))!!.time
                val cCalendar = Calendar.getInstance()
                cCalendar.timeInMillis = time
                val lCalendar = Calendar.getInstance()
                lCalendar.timeInMillis = lastTime
                if (cCalendar.get(Calendar.YEAR) == lCalendar.get(Calendar.YEAR)
                    && cCalendar.get(Calendar.MONTH) == lCalendar.get(Calendar.MONTH)
                    && cCalendar.get(Calendar.DAY_OF_MONTH) == lCalendar.get(Calendar.DAY_OF_MONTH)
                ) {
                    return false
                }
                return true
            } catch (e: Exception) {
                return false
            }
        }

        private fun getItemValueForString(
            item: JsonObject?,
            key: String,
            otherKey: String?
        ): String {
            if (item == null) {
                return ""
            }
            if (item.has(key)) {
                return item.get(key).asString
            }
            if (TextUtils.isEmpty(otherKey)) {
                return ""
            }
            if (item.has(otherKey)) {
                return item.get(otherKey).asString
            }
            return ""
        }
    }
}