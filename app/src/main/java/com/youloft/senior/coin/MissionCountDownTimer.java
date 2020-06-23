package com.youloft.senior.coin;

import android.widget.TextView;

import com.youloft.net.bean.MissionResult;
import com.youloft.senior.R;


/**
 * CountDownTimer
 */
public class MissionCountDownTimer {


    /**
     * 更新 CountDown
     *
     * @param textView
     * @param remainTime
     */
    public static void updateCountDown(TextView textView, long remainTime) {
        if (textView == null) {
            return;
        }
        MissionCountDownTimer tag = (MissionCountDownTimer) textView.getTag(R.id.unique_tag_store);
        if (tag == null) {
            tag = new MissionCountDownTimer();
            tag.normalText = textView.getText();
            textView.setTag(R.id.unique_tag_store, tag);
        }
        tag.bindView(textView, remainTime);
    }

    public CharSequence normalText;
    public String progressTextFormat;

    private UICountDownTimer uiCountDownTimer;

    public static void cancelTimer(TextView textView) {
        if (textView == null) {
            return;
        }
        MissionCountDownTimer tag = (MissionCountDownTimer) textView.getTag(R.id.unique_tag_store);
        if (tag != null) {
            tag.cancel();
        }
    }

    public void bindView(TextView tv, long remainTime) {
        if (remainTime <= 0) {
            reset(tv);
        } else {
            startRemainTime(tv, remainTime);
        }
    }

    private void startRemainTime(final TextView tv, long remainTime) {
        cancel();
        tv.setSelected(true);
        uiCountDownTimer = new UICountDownTimer(remainTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv.setText(String.format("%02d:%02d", millisUntilFinished / 1000 / 60, millisUntilFinished / 1000 % 60));
            }

            @Override
            public void onFinish() {
                reset(tv);
            }
        };
        uiCountDownTimer.start();
    }

    private void cancel() {
        if (uiCountDownTimer != null) {
            uiCountDownTimer.cancel();
            uiCountDownTimer = null;
        }
    }

    /**
     * 重置当前 TextView
     *
     * @param tv
     */
    private void reset(TextView tv) {
        cancel();
        tv.setText(normalText);
        tv.setSelected(false);
    }

}
