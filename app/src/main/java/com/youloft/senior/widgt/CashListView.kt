package com.youloft.senior.widgt

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.youloft.senior.R
import com.youloft.senior.base.App.Companion.instance
import com.youloft.util.UiUtil
import kotlinx.android.synthetic.main.cash_item_layout.view.*

/**
 * @author xll
 * @date 2020/6/24 11:27
 */
internal class CashListView(
    context: Context?,
    attrs: AttributeSet?
) : ViewGroup(context, attrs) {
    var array = JSONArray()
    private var itemWidth = 0
    private val itemHeight = UiUtil.dp2Px(instance(), 50f)
    private val padding = UiUtil.dp2Px(instance(), 15f)

    /**
     * 选中的项
     */
    var selectPosition = 1

    var newPeopleTag: Drawable = resources.getDrawable(R.drawable.tx_xrzx_bq)

    var newPeoplePosition = -1;

    fun refresh(array: JSONArray?) {
        this.array.clear()
        this.array.addAll(array!!)
        notifyDataChange()
    }

    init {
        setWillNotDraw(false)
    }

    var callback: ((JSONObject?) -> Unit)? = null

    public fun setSelectCallBack(callback: (JSONObject?) -> Unit) {
        this.callback = callback
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childCount = childCount
        if (childCount <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val width = MeasureSpec.getSize(widthMeasureSpec)
        itemWidth = (width - padding * 4) / 3
        val line = childCount / 3 + if (childCount % 3 == 0) 0 else 1
        val height = line * itemHeight + (line + 1) * padding
        for (i in 0 until childCount) {
            getChildAt(i).measure(
                MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)
            )
        }
        setMeasuredDimension(width, height)
    }

    private fun notifyDataChange() {
        removeAllViews()
        newPeoplePosition = -1
        for (i in array.indices) {
            val item = array.getJSONObject(i)
            createView(item, i)
        }
    }

    fun getSelection(): Int {
        return selectPosition
    }

    private fun createView(item: JSONObject, position: Int) {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.cash_item_layout, null)
        itemView.isSelected = selectPosition == position
        itemView.select_tag.visibility =
            if (selectPosition == position) View.VISIBLE else View.GONE
        itemView.item_cash.text = item.getString("price")
        itemView.tag = item
        if (item.getIntValue("type") == 0) {
            newPeoplePosition = position
        }
        itemView.setOnClickListener {
            println("点击选中了  " + item.getString("price"))
            selectPosition = position
            refreshSelect()
            if (callback != null) {
                callback!!(getSelectItem())
            }
        }
        addView(itemView)
    }

    fun getSelectItem(): JSONObject? {
        if (selectPosition < 0) {
            return null
        }
        if (selectPosition >= array.size) {
            return null
        }
        return array.getJSONObject(selectPosition)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (newPeoplePosition < 0) {
            return
        }
        val line = newPeoplePosition / 3
        val left = (newPeoplePosition % 3 * (itemWidth + padding)) + padding
        val top = (line * (itemHeight + padding)) + padding
        val right = left + itemWidth

        val tagRight = right + padding
        val tagTop = top - UiUtil.dp2Px(context, 13f)
        newPeopleTag.setBounds(
            tagRight - newPeopleTag.intrinsicWidth,
            tagTop,
            tagRight,
            newPeopleTag.intrinsicHeight + tagTop
        )
        newPeopleTag.draw(canvas)
    }

    private fun refreshSelect() {
        val count = childCount
        if (count == 0) {
            return
        }
        for (i in 0 until count) {
            getChildAt(i).isSelected = selectPosition == i
            getChildAt(i).select_tag.visibility =
                if (selectPosition == i) View.VISIBLE else View.GONE
        }
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        val count = childCount
        if (count <= 0) {
            return
        }
        for (i in 0 until count) {
            val itemView = getChildAt(i)
            val line = i / 3
            val left = (i % 3 * (itemWidth + padding)) + padding
            val top = (line * (itemHeight + padding)) + padding
            itemView.layout(left, top, left + itemWidth, top + itemHeight)
        }
    }
}