package com.youloft.senior.widgt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.youloft.senior.base.App;
import com.youloft.senior.cash.PhoneLocationFragment;
import com.youloft.util.UiUtil;

public class AZTitleDecoration extends RecyclerView.ItemDecoration {

    private TextPaint mTitleTextPaint;
    private Paint mBackgroundPaint;
    private TitleAttributes mTitleAttributes;
    private Paint linePaint;

    public AZTitleDecoration(TitleAttributes attributes) {
        mTitleAttributes = attributes;
        mTitleTextPaint = new TextPaint();
        mTitleTextPaint.setAntiAlias(true);
        mTitleTextPaint.setTextSize(mTitleAttributes.mTextSize);
        mTitleTextPaint.setColor(mTitleAttributes.mTextColor);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(mTitleAttributes.mBackgroundColor);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xffeaeaea);
        linePaint.setStrokeWidth(UiUtil.dp2Px(App.Companion.instance(), 1));
    }

    /**
     * 绘制标题
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getAdapter() == null) {
            return;
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter.getItemCount() <= 0) {
            return;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (titleAttachView(child, parent)) {
                drawTitleItem(c, parent, child, getSortLetters(position));
            } else {
                c.drawLine(parent.getPaddingLeft() + child.getPaddingLeft(), child.getTop(), child.getWidth() - child.getPaddingLeft(), child.getTop(), linePaint);
            }
        }
    }

    private String getSortLetters(int position) {
        if (PhoneLocationFragment.PL_DAT == null || PhoneLocationFragment.PL_DAT.isEmpty()) {
            return "";
        }
        return PhoneLocationFragment.PL_DAT.getJSONObject(position).getString("i");
    }

    private int getNextSortLetterPosition(int position) {
        if (PhoneLocationFragment.PL_DAT == null || PhoneLocationFragment.PL_DAT.isEmpty() || PhoneLocationFragment.PL_DAT.size() <= position + 1) {
            return -1;
        }
        int resultPosition = -1;
        for (int index = position + 1; index < PhoneLocationFragment.PL_DAT.size(); index++) {
            if (!PhoneLocationFragment.PL_DAT.getJSONObject(position).getString("i").equals(PhoneLocationFragment.PL_DAT.getJSONObject(index).getString("i"))) {
                resultPosition = index;
                break;
            }
        }
        return resultPosition;
    }

    private Object getItemData(int position) {
        return PhoneLocationFragment.PL_DAT.getJSONObject(position);
    }

    /**
     * 绘制悬浮标题
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getAdapter() == null) {
            return;
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter.getItemCount() <= 0) {
            return;
        }
        View firstView = parent.getChildAt(0);
        int firstAdapterPosition = parent.getChildAdapterPosition(firstView);
        c.save();
        //找到下一个标题对应的adapter position
        int nextLetterAdapterPosition = getNextSortLetterPosition(firstAdapterPosition);
        if (nextLetterAdapterPosition != -1) {
            //下一个标题view index
            int nextLettersViewIndex = nextLetterAdapterPosition - firstAdapterPosition;
            if (nextLettersViewIndex < parent.getChildCount()) {
                View nextLettersView = parent.getChildAt(nextLettersViewIndex);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) nextLettersView.getLayoutParams();
                int nextToTop = nextLettersView.getTop() - params.bottomMargin - parent.getPaddingTop();
                if (nextToTop < mTitleAttributes.mItemHeight * 2) {
                    //有重叠
                    c.translate(0, nextToTop - mTitleAttributes.mItemHeight * 2);
                }
            }
        }
        mBackgroundPaint.setColor(mTitleAttributes.mBackgroundColor);
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mTitleAttributes.mItemHeight, mBackgroundPaint);
        mTitleTextPaint.setTextSize(mTitleAttributes.mTextSize);
        mTitleTextPaint.setColor(mTitleAttributes.mTextColor);
        c.drawText(getSortLetters(firstAdapterPosition),
                parent.getPaddingLeft() + firstView.getPaddingLeft() + mTitleAttributes.mTextPadding,
                TextDrawUtils.getTextBaseLineByCenter(parent.getPaddingTop() + mTitleAttributes.mItemHeight / 2, mTitleTextPaint),
                mTitleTextPaint);
        c.restore();
    }


    /**
     * 设置空出绘制标题的区域
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (titleAttachView(view, parent)) {
            outRect.set(0, mTitleAttributes.mItemHeight, 0, 0);
        } else {
            outRect.set(0, (int) linePaint.getStrokeWidth(), 0, 0);
        }
    }

    /**
     * 绘制标题信息
     */
    private void drawTitleItem(Canvas c, RecyclerView parent, View child, String letters) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        //绘制背景
        c.drawRect(parent.getPaddingLeft(), child.getTop() - params.bottomMargin - mTitleAttributes.mItemHeight,
                parent.getWidth() - parent.getPaddingRight(), child.getTop() - params.bottomMargin, mBackgroundPaint);

        float textCenterY = child.getTop() - params.bottomMargin - mTitleAttributes.mItemHeight / 2;
        //绘制标题文字
        c.drawText(letters, parent.getPaddingLeft() + child.getPaddingLeft() + mTitleAttributes.mTextPadding,
                TextDrawUtils.getTextBaseLineByCenter(textCenterY, mTitleTextPaint), mTitleTextPaint);
    }

    /**
     * 判断指定view的上方是否要空出绘制标题的位置
     *
     * @param view   　指定的view
     * @param parent 父view
     */
    private boolean titleAttachView(View view, RecyclerView parent) {
        if (parent.getAdapter() == null) {
            return false;
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter.getItemCount() <= 0) {
            return false;
        }
        int position = parent.getChildAdapterPosition(view);
        //第一个一定要空出区域 + 每个都和前面一个去做判断，不等于前一个则要空出区域
        return position == 0 ||
                null != getItemData(position) && !getSortLetters(position).equals(getSortLetters(position - 1));

    }


    public static class TitleAttributes {

        Context mContext;
        /**
         * 标题高度
         */
        int mItemHeight;
        /**
         * 文字的padding
         */
        int mTextPadding;
        /**
         * 标题文字大小
         */
        int mTextSize;
        /**
         * 标题文字颜色
         */
        int mTextColor;
        int mTextColorResId;
        /**
         * 标题背景
         */
        int mBackgroundColor;
        int mBackgroundColorResId;

        public TitleAttributes(Context context) {
            mContext = context;
            mItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, context.getResources().getDisplayMetrics());
            mTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
            mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, context.getResources().getDisplayMetrics());
            mTextColor = 0xff999999;
            mBackgroundColor = 0xffffffff;
        }

        public TitleAttributes setItemHeight(int heightDp) {
            mItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp,
                    mContext.getResources().getDisplayMetrics());
            return this;
        }

        public TitleAttributes setTextPadding(int paddingDp) {
            mTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingDp,
                    mContext.getResources().getDisplayMetrics());
            return this;
        }

        public TitleAttributes setTextSize(int sizeSp) {
            mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sizeSp, mContext.getResources().getDisplayMetrics());
            return this;
        }

        public TitleAttributes setTextColor(int color) {
            mTextColor = color;
            return this;
        }
    }
}
