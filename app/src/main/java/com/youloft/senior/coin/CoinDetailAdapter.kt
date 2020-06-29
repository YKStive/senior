package com.youloft.senior.coin

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
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
    var datas: JSONArray? = null
    fun refreshData(array: JSONArray?) {
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

    private fun getItemData(position: Int): JSONObject? {
        if (datas == null) {
            return null
        }
        if (datas!!.size <= position) {
            return null
        }
        return if (position < 0) {
            null
        } else datas!!.getJSONObject(position)
    }

    override fun getItemCount(): Int {
        return if (datas == null) 0 else datas!!.size
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.coin_detail_item_layout, parent, false)
    ) {
        fun bindView(
            item: JSONObject?,
            lastItem: JSONObject?,
            nextItem: JSONObject?
        ) {
            if (item == null) {
                return
            }
            itemView.item_content.text = getItemValueForString(item, "content", "codeDesc")
            itemView.item_time.text = getItemValueForString(item, "createTime", null)
            itemView.item_coin.text = item.getIntValue("coin").toString()

            val head = showHeader(item, lastItem)
            val foot = showFoot(item, nextItem)
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val time: Long =
                    format.parse(getItemValueForString(item, "createTime", null))!!.time
                val cCalendar = Calendar.getInstance()
                cCalendar.timeInMillis = time
                val format2 = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                val format3 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                itemView.item_top_date.text = format2.format(cCalendar.timeInMillis)
                itemView.item_time.text = format3.format(cCalendar.timeInMillis)
            } catch (e: Exception) {

            }
            if (head && !foot) {
                itemView.line.visibility = View.VISIBLE
                itemView.top_group.visibility = View.VISIBLE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_top_bg)
            } else if (!head && foot) {
                itemView.top_group.visibility = View.GONE
                itemView.line.visibility = View.GONE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_bottom_bg)
            } else if (!head && !foot) {
                itemView.top_group.visibility = View.GONE
                itemView.line.visibility = View.VISIBLE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_center_bg)
            } else {
                itemView.line.visibility = View.GONE
                itemView.top_group.visibility = View.VISIBLE
                itemView.content_group.setBackgroundResource(R.drawable.coin_detail_item_all_bg)
            }
        }

        private fun showFoot(
            item: JSONObject,
            nextItem: JSONObject?
        ): Boolean {
            if (nextItem == null) {
                return true
            }
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val time: Long =
                    format.parse(getItemValueForString(item, "createTime", null))!!.time
                val lastTime =
                    format.parse(getItemValueForString(nextItem, "createTime", null))!!.time
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
            item: JSONObject,
            lastItem: JSONObject?
        ): Boolean {
            if (lastItem == null) {
                return true
            }
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val time: Long =
                    format.parse(getItemValueForString(item, "createTime", null))!!.time
                val lastTime =
                    format.parse(getItemValueForString(lastItem, "createTime", null))!!.time
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
            item: JSONObject?,
            key: String,
            otherKey: String?
        ): String {
            if (item == null) {
                return ""
            }
            if (item.containsKey(key)) {
                return item.getString(key)
            }
            if (TextUtils.isEmpty(otherKey)) {
                return ""
            }
            if (item.containsKey(otherKey)) {
                return item.getString(otherKey)
            }
            return ""
        }
    }
}