package com.youloft.senior.widgt;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youloft.senior.R;
import com.youloft.socialize.SOC_MEDIA;
import com.youloft.socialize.Socialize;
import com.youloft.socialize.auth.AuthListener;
import com.youloft.util.ToastMaster;

import java.util.Map;

import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * @Description: 快捷登录弹出
 * @Author: slh
 * @CreateDate: 2020/6/29 15:12
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/6/29 15:12
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class LoginPopup extends PopupWindow {
    private Activity mContext;

    private View view;

    private EditText edtPhone;
    private TextView tvLogin;
    private ImageView ivClose;


    public LoginPopup(Activity mContext, View.OnClickListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_login, null);

        edtPhone = view.findViewById(R.id.edt_phone);
        tvLogin = view.findViewById(R.id.tv_login);
        ivClose = view.findViewById(R.id.iv_close);
        // 取消按钮
        ivClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Socialize.getIns()
                        .checkPlatformInstall(mContext, SOC_MEDIA.WEIXIN, false)
                ) {
                    ToastMaster.showShortToast(mContext, "微信未安装");
                }
                Socialize.getIns().auth(mContext, SOC_MEDIA.WEIXIN, new AuthListener() {
                    @Override
                    public void onStart(SOC_MEDIA platform) {

                    }

                    @Override
                    public void onComplete(SOC_MEDIA platform, int action, Map<String, String> data) {
//                        unionid = data .get("unionid").toString()
//                        id = data .get("openid").toString()
//                        headIconUrl = data .get("profile_image_url").toString()
//                        name = data .get("screen_name").toString()
//                        description = ""
//                        val sex:String = data .get("gender").toString()
//                        gender = if ("男" == sex) {
//                            "0"
//                        } else {
//                            "1"
//                        }
                    }

                    @Override
                    public void onError(SOC_MEDIA platform, int action, Throwable t) {

                    }

                    @Override
                    public void onCancel(SOC_MEDIA platform, int action) {

                    }

//                    Preference<String>("accessToken","" ).setValue()
//                    CApp.getInstance()
//                        .getSharedPreferences("temp_token_xx", Context.MODE_PRIVATE)
//                        .edit()
//                        .putString("accessToken", info.get("accessToken"))
//                        .putString("openid", info.get("openid"))
//                        .putString("appid", BuildConfig.WX_SHARE_ID)
//                        .putString("loginType", "wx")
//                        .commit()

                });

            }
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }

}
