package com.youloft.senior.widgt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.youloft.senior.R

/**
 * @author you
 * @create 2020/6/23
 * @desc
 */
class PunchMoneyView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    private var date: String? = null
    private var money: String? = null
    private var tvDate: TextView
    private var tvMoney: TextView

    init {
        val types = context.obtainStyledAttributes(attributeSet, R.styleable.PunchMoneyView)
        date = types.getString(R.styleable.PunchMoneyView_pv_date)
        money = types.getString(R.styleable.PunchMoneyView_pv_money)
        types.recycle()
        val root =
            LayoutInflater.from(context).inflate(R.layout.item_post_content_punch_money, this)
        tvDate = root.findViewById(R.id.tv_date)
        tvMoney = root.findViewById(R.id.tv_money)

        date?.apply {
            tvDate.text = date
        }
        money?.apply {
            tvMoney.text = money
        }
    }

    fun setMoney(money:String){
        tvMoney.text = money
    }
}