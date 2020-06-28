package com.youloft.senior.cash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.youloft.senior.R
import com.youloft.senior.base.App

val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        App.instance().resources.displayMetrics
    ).toInt()

val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.instance().resources.displayMetrics
    )

/**
 * 提现加速动画 View
 */
class ApplyProgressAnimView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    /**
     * 动画人的大小
     */
    private val indicatorFrame = Rect(0, 0, 39.dp, 42.dp)

    /**
     * 条的高度
     */
    private val barHeight = 12f.dp

    /**
     * 条的圆角大小
     */
    private val barRadius = 11f.dp

    /**
     * 日期文字大小
     */
    private val dayTextSize = 12f.dp

    //bar绘制大小
    private val barFrame = RectF()

    private var maxValue = 7

    private var value = 0

    private var drawValue = 0f

    fun getValue() = this.value

    fun setValue(value: Int, max: Int = maxValue, animate: Boolean = true) {
        this.maxValue = max
        this.value = value.coerceAtMost(max)
        val targetValue = if (value >= maxValue) {
            maxValue + 1
        } else {
            value
        }
        animator?.cancel()
        tipAnim?.cancel()
        if (animate) {
            animateChangeValue(targetValue)
        } else {
            stopManRun()
            tipAlpha = 0f
            drawValue = targetValue.toFloat()
            invalidate()
        }
    }

    private var animator: Animator? = null


    private fun animateChangeValue(value: Int) {
        tipAlpha = 0f
        animator = ValueAnimator.ofFloat(0f, value.toFloat()).apply {
            addUpdateListener {
                drawValue = it.animatedValue as Float
                invalidate()
            }
            duration = (value * 600).toLong()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStart(animation: Animator?) {
                    //小人开努
                    startManRun()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    stopManRun()
                    playTipAnim()
                }

            })
        }
        animator?.start()
    }

    var tipAnim: Animator? = null
    private fun playTipAnim() {
        tipAnim?.cancel()
        tipAnim = ValueAnimator.ofFloat(tipAlpha, 1f).apply {
            addUpdateListener {
                tipAlpha = it.animatedValue as Float
                invalidate()
            }
            duration = 400
            start()
        }
    }

    private val manRun: Runnable = Runnable {
        //动画结束小人别动
        indicatorDrawable?.loopCount = 1
    }

    private fun startManRun() {
        removeCallbacks(manRun)
        indicatorDrawable?.loopCount = 0
        indicatorDrawable?.start()
    }

    private fun stopManRun() {
        postDelayed(manRun, if (value == 0) 1000 else 200)
    }

    /**
     * 动画人素材
     */
    private val indicatorDrawable by lazy {
        kotlin.runCatching {
            pl.droidsonroids.gif.GifDrawable(context.assets, "red.gif")?.apply {
                stop()
                setImageDrawable(this)
                addAnimationListener {
                    if (loopCount == 1) {
                        stop()
                        seekToFrame(0)
                    }
                }
            }
        }.getOrNull()
    }

    /**
     * 三角形边
     */
    private val triangleDrawable by lazy {
        resources.getDrawable(R.drawable.theme_txjd_day_triangle).apply {
            bounds = Rect(0, 0, 6.dp, 2.dp)
        }
    }

    /**
     * Bar的画笔
     */
    private val barPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFF8E8D8")
            style = Paint.Style.FILL
        }
    }

    /**
     * 文字 Paint
     */
    private val dayTextPaint by lazy {
        TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FF777777")
            textSize = dayTextSize
            textAlign = Paint.Align.CENTER
            textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        }
    }

    /**
     * 左页
     */
    private val tipLeftDrawable by lazy {
        resources.getDrawable(R.drawable.txjd_buble)
    }

    /**
     * 右页
     */
    private val tipRightDrawable by lazy {
        resources.getDrawable(R.drawable.txjd_buble2)
    }

    /**
     * 提示文本的那啥
     */
    private val tipTextPaint by lazy {
        TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFCE8C3B")
            textSize = 14f.dp
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
    }

    /**
     * Bar的画笔
     */
    private val barActiveDrawable by lazy {
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor("#FFFFC868"), Color.parseColor("#FFFF8631"))
        ).apply {
            cornerRadius = barRadius
        }
    }

    private var tipAlpha = 0f


    private var progressText = "现金还有${maxValue - value}天到账"
    private var finishText = "提现已提交，请注意查收"

    /**
     * 设置提示文本
     */
    fun setTipTexts(finishText: String, progressText: String) {
        this.progressText = progressText
        this.finishText = finishText
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val barBaseLine = height - 16f.dp
        val segWidth = width / (maxValue + 1)
        //绘制底色
        barFrame.set(0f, barBaseLine - barHeight, width.toFloat(), barBaseLine)
        canvas.drawRoundRect(barFrame, barRadius, barRadius, barPaint)

        //绘制人物
        indicatorFrame.offsetTo(
            ((segWidth * drawValue).toInt()).coerceAtMost(segWidth * maxValue)
                .coerceAtLeast(8.dp) - indicatorFrame.width() / 2,
            barFrame.bottom.toInt() - indicatorFrame.height()
        )
        indicatorDrawable?.bounds = indicatorFrame
        //绘制进度色
        barActiveDrawable.setBounds(
            0,
            barFrame.top.toInt(),
            ((segWidth * drawValue).toInt() + (indicatorFrame.width() * 0.3f).toInt()).coerceAtMost(
                width
            ).coerceAtLeast(22.dp),
            barFrame.bottom.toInt()
        )
        barActiveDrawable.draw(canvas)
        indicatorDrawable?.draw(canvas)

        //绘制几天几天几天
        for (dayIndex in 1..maxValue) {
            val daySegX = dayIndex * segWidth
            //绘制三角形
            val left = daySegX - triangleDrawable.bounds.width() / 2
            val top = (barFrame.bottom - triangleDrawable.bounds.height()).toInt()
            triangleDrawable.setBounds(
                left,
                top,
                left + triangleDrawable.bounds.width(),
                top + triangleDrawable.bounds.height()
            )
            triangleDrawable.draw(canvas)
            //绘制文字
            canvas.drawText("${dayIndex}天", daySegX.toFloat(), height.toFloat(), dayTextPaint)
        }
        if (tipAlpha > 0) {
            val tipMsg = if (value >= maxValue) finishText else progressText
            if (TextUtils.isEmpty(tipMsg)) {
                return
            }
            tipTextPaint.alpha = (tipAlpha * 255).toInt()
            //开始绘制
            val textWidth = tipTextPaint.measureText(tipMsg)
            val bgWidth = textWidth + 24.dp
            if (indicatorFrame.centerX() + bgWidth + 24.dp > width &&
                indicatorFrame.centerX() - bgWidth - 23.dp > 0
            ) {
                tipRightDrawable.alpha = (tipAlpha * 255).toInt()
                tipRightDrawable.setBounds(
                    (indicatorFrame.right - bgWidth).toInt() - 5.dp,
                    indicatorFrame.top - tipRightDrawable.intrinsicHeight - 2.dp,
                    indicatorFrame.right - 5.dp,
                    indicatorFrame.top - 2.dp
                )
                tipRightDrawable.draw(canvas)
                canvas.drawText(
                    tipMsg,
                    tipRightDrawable.bounds.centerX().toFloat(),
                    tipRightDrawable.bounds.centerY().toFloat() + 2f.dp,
                    tipTextPaint
                )
            } else {
                tipLeftDrawable.alpha = (tipAlpha * 255).toInt()
                tipLeftDrawable.setBounds(
                    indicatorFrame.left + 10.dp,
                    indicatorFrame.top - tipRightDrawable.intrinsicHeight - 2.dp,
                    (indicatorFrame.left + bgWidth).toInt() + 10.dp,
                    indicatorFrame.top - 2.dp
                )
                tipLeftDrawable.draw(canvas)
                canvas.drawText(
                    tipMsg,
                    tipLeftDrawable.bounds.centerX().toFloat(),
                    tipLeftDrawable.bounds.centerY().toFloat() + 2f.dp,
                    tipTextPaint
                )
            }

        }
    }

}