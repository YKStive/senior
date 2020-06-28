package com.youloft.senior.widgt

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class GridSpaceItemDecoration
    (
    private val mSpanCount: Int,
    private val mRowSpacing: Int,
    private val mColumnSpacing: Int
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % mSpanCount
        outRect.left = column * mColumnSpacing / mSpanCount
        outRect.right =
            mColumnSpacing - (column + 1) * mColumnSpacing / mSpanCount

        if (position >= mSpanCount) {
            outRect.top = mRowSpacing // item top
        }
    }

}