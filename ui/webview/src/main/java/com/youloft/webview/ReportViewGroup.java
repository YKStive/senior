package com.youloft.webview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * 上报View
 */
public class ReportViewGroup extends ViewGroup {
    public ReportViewGroup(Context context) {
        super(context);
    }

    public View newAdView(Object tag) {
        View view = new View(getContext());
        addViewInLayout(view, 0, new LayoutParams(2, 2));
        view.measure(MeasureSpec.makeMeasureSpec(2, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(2, MeasureSpec.EXACTLY));
        view.layout(0, 0, 2, 2);
        view.setTag(tag);
        return view;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }


    @Override
    public void addView(View child) {
        super.addView(child, new LayoutParams(2, 2));
    }

    @Override
    public void requestLayout() {
        if (false) {
            super.requestLayout();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    public void attachParent(CommonWebView webView) {
        View rootView = webView.getRootView();
        if (rootView != null && rootView instanceof ViewGroup) {
            ((ViewGroup) rootView).addView(this);
        }
    }

    public void detachFromParent(){
        ViewParent parent = getParent();
        if(parent!=null && parent instanceof ViewGroup){
            ((ViewGroup) parent).removeView(this);
        }
    }


}
