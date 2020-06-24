package com.youloft.senior.cash;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youloft.senior.R;
import com.youloft.senior.base.App;
import com.youloft.senior.widgt.AZTitleDecoration;
import com.youloft.senior.widgt.AZWaveSideBarView;
import com.youloft.util.UiUtil;

/**
 * 号码归属地选择
 */
public class PhoneLocationFragment extends DialogFragment {

    public static final JSONArray PL_DAT = JSON.parseArray("[{\"c\":\"中国\",\"i\":\"#\",\"n\":\"+86\"},{\"c\":\"中国澳门\",\"i\":\"#\",\"n\":\"+853\"},{\"c\":\"中国台湾\",\"i\":\"#\",\"n\":\"+886\"},{\"c\":\"中国香港\",\"i\":\"#\",\"n\":\"+852\"},{\"c\":\"阿尔巴尼亚\",\"i\":\"A\",\"n\":\"+355\"},{\"c\":\"阿尔及利亚\",\"i\":\"A\",\"n\":\"+213\"},{\"c\":\"阿富汗\",\"i\":\"A\",\"n\":\"+93\"},{\"c\":\"阿根廷\",\"i\":\"A\",\"n\":\"+54\"},{\"c\":\"阿拉伯联合酋长国\",\"i\":\"A\",\"n\":\"+971\"},{\"c\":\"阿曼\",\"i\":\"A\",\"n\":\"+968\"},{\"c\":\"阿塞拜疆\",\"i\":\"A\",\"n\":\"+994\"},{\"c\":\"阿森松岛\",\"i\":\"A\",\"n\":\"+247\"},{\"c\":\"埃及\",\"i\":\"A\",\"n\":\"+20\"},{\"c\":\"埃塞俄比亚\",\"i\":\"A\",\"n\":\"+251\"},{\"c\":\"爱尔兰\",\"i\":\"A\",\"n\":\"+353\"},{\"c\":\"爱沙尼亚\",\"i\":\"A\",\"n\":\"+372\"},{\"c\":\"安道尔共和国\",\"i\":\"A\",\"n\":\"+376\"},{\"c\":\"安哥拉\",\"i\":\"A\",\"n\":\"+244\"},{\"c\":\"安圭拉岛\",\"i\":\"A\",\"n\":\"+1264\"},{\"c\":\"安提瓜和巴布达\",\"i\":\"A\",\"n\":\"+1268\"},{\"c\":\"奥地利\",\"i\":\"A\",\"n\":\"+43\"},{\"c\":\"澳大利亚\",\"i\":\"A\",\"n\":\"+61\"},{\"c\":\"巴巴多斯\",\"i\":\"B\",\"n\":\"+1246\"},{\"c\":\"巴布亚新几内亚\",\"i\":\"B\",\"n\":\"+675\"},{\"c\":\"巴哈马\",\"i\":\"B\",\"n\":\"+1242\"},{\"c\":\"巴基斯坦\",\"i\":\"B\",\"n\":\"+92\"},{\"c\":\"巴拉圭\",\"i\":\"B\",\"n\":\"+595\"},{\"c\":\"巴林\",\"i\":\"B\",\"n\":\"+973\"},{\"c\":\"巴拿马\",\"i\":\"B\",\"n\":\"+507\"},{\"c\":\"巴西\",\"i\":\"B\",\"n\":\"+55\"},{\"c\":\"白俄罗斯\",\"i\":\"B\",\"n\":\"+375\"},{\"c\":\"百慕大群岛\",\"i\":\"B\",\"n\":\"+1441\"},{\"c\":\"保加利亚\",\"i\":\"B\",\"n\":\"+359\"},{\"c\":\"贝宁\",\"i\":\"B\",\"n\":\"+229\"},{\"c\":\"比利时\",\"i\":\"B\",\"n\":\"+32\"},{\"c\":\"冰岛\",\"i\":\"B\",\"n\":\"+354\"},{\"c\":\"波多黎各\",\"i\":\"B\",\"n\":\"+1787\"},{\"c\":\"波兰\",\"i\":\"B\",\"n\":\"+48\"},{\"c\":\"玻利维亚\",\"i\":\"B\",\"n\":\"+591\"},{\"c\":\"伯利兹\",\"i\":\"B\",\"n\":\"+501\"},{\"c\":\"博茨瓦纳\",\"i\":\"B\",\"n\":\"+267\"},{\"c\":\"布基纳法索\",\"i\":\"B\",\"n\":\"+226\"},{\"c\":\"布隆迪\",\"i\":\"B\",\"n\":\"+257\"},{\"c\":\"丹麦\",\"i\":\"D\",\"n\":\"+45\"},{\"c\":\"德国\",\"i\":\"D\",\"n\":\"+49\"},{\"c\":\"多哥\",\"i\":\"D\",\"n\":\"+228\"},{\"c\":\"多明尼加共和国\",\"i\":\"D\",\"n\":\"+1809\"},{\"c\":\"俄罗斯\",\"i\":\"E\",\"n\":\"+7\"},{\"c\":\"厄瓜多尔\",\"i\":\"E\",\"n\":\"+593\"},{\"c\":\"法国\",\"i\":\"F\",\"n\":\"+33\"},{\"c\":\"法属波利尼西亚\",\"i\":\"F\",\"n\":\"+689\"},{\"c\":\"法属圭亚那\",\"i\":\"F\",\"n\":\"+594\"},{\"c\":\"菲律宾\",\"i\":\"F\",\"n\":\"+63\"},{\"c\":\"斐济\",\"i\":\"F\",\"n\":\"+679\"},{\"c\":\"芬兰\",\"i\":\"F\",\"n\":\"+358\"},{\"c\":\"冈比亚\",\"i\":\"G\",\"n\":\"+220\"},{\"c\":\"刚果民主共和国\",\"i\":\"G\",\"n\":\"+243\"},{\"c\":\"哥伦比亚\",\"i\":\"G\",\"n\":\"+57\"},{\"c\":\"哥斯达黎加\",\"i\":\"G\",\"n\":\"+506\"},{\"c\":\"格林纳达\",\"i\":\"G\",\"n\":\"+1473\"},{\"c\":\"格鲁吉亚\",\"i\":\"G\",\"n\":\"+995\"},{\"c\":\"古巴\",\"i\":\"G\",\"n\":\"+53\"},{\"c\":\"瓜地马拉\",\"i\":\"G\",\"n\":\"+502\"},{\"c\":\"关岛\",\"i\":\"G\",\"n\":\"+1671\"},{\"c\":\"圭亚那\",\"i\":\"G\",\"n\":\"+592\"},{\"c\":\"哈萨克斯坦\",\"i\":\"H\",\"n\":\"+7\"},{\"c\":\"海地\",\"i\":\"H\",\"n\":\"+509\"},{\"c\":\"韩国\",\"i\":\"H\",\"n\":\"+82\"},{\"c\":\"荷兰\",\"i\":\"H\",\"n\":\"+31\"},{\"c\":\"洪都拉斯\",\"i\":\"H\",\"n\":\"+504\"},{\"c\":\"吉布提\",\"i\":\"J\",\"n\":\"+253\"},{\"c\":\"吉尔吉斯斯坦\",\"i\":\"J\",\"n\":\"+996\"},{\"c\":\"几内亚\",\"i\":\"J\",\"n\":\"+224\"},{\"c\":\"加拿大\",\"i\":\"J\",\"n\":\"+1\"},{\"c\":\"加纳\",\"i\":\"J\",\"n\":\"+233\"},{\"c\":\"加蓬\",\"i\":\"J\",\"n\":\"+241\"},{\"c\":\"柬埔寨\",\"i\":\"J\",\"n\":\"+855\"},{\"c\":\"捷克\",\"i\":\"J\",\"n\":\"+420\"},{\"c\":\"津巴布韦\",\"i\":\"J\",\"n\":\"+263\"},{\"c\":\"喀麦隆\",\"i\":\"K\",\"n\":\"+237\"},{\"c\":\"卡塔尔\",\"i\":\"K\",\"n\":\"+974\"},{\"c\":\"开曼群岛\",\"i\":\"K\",\"n\":\"+1345\"},{\"c\":\"科特迪瓦\",\"i\":\"K\",\"n\":\"+225\"},{\"c\":\"科威特\",\"i\":\"K\",\"n\":\"+965\"},{\"c\":\"肯尼亚\",\"i\":\"K\",\"n\":\"+254\"},{\"c\":\"库克群岛\",\"i\":\"K\",\"n\":\"+682\"},{\"c\":\"拉脱维亚\",\"i\":\"L\",\"n\":\"+371\"},{\"c\":\"莱索托\",\"i\":\"L\",\"n\":\"+266\"},{\"c\":\"老挝\",\"i\":\"L\",\"n\":\"+856\"},{\"c\":\"黎巴嫩\",\"i\":\"L\",\"n\":\"+961\"},{\"c\":\"立陶宛\",\"i\":\"L\",\"n\":\"+370\"},{\"c\":\"利比里亚\",\"i\":\"L\",\"n\":\"+231\"},{\"c\":\"利比亚\",\"i\":\"L\",\"n\":\"+218\"},{\"c\":\"列支敦士登\",\"i\":\"L\",\"n\":\"+423\"},{\"c\":\"卢森堡\",\"i\":\"L\",\"n\":\"+352\"},{\"c\":\"罗马尼亚\",\"i\":\"L\",\"n\":\"+40\"},{\"c\":\"马达加斯加\",\"i\":\"M\",\"n\":\"+261\"},{\"c\":\"马尔代夫\",\"i\":\"M\",\"n\":\"+960\"},{\"c\":\"马耳他\",\"i\":\"M\",\"n\":\"+356\"},{\"c\":\"马拉维\",\"i\":\"M\",\"n\":\"+265\"},{\"c\":\"马来西亚\",\"i\":\"M\",\"n\":\"+60\"},{\"c\":\"马里\",\"i\":\"M\",\"n\":\"+223\"},{\"c\":\"马提尼克\",\"i\":\"M\",\"n\":\"+596\"},{\"c\":\"毛里求斯\",\"i\":\"M\",\"n\":\"+230\"},{\"c\":\"毛里塔尼亚\",\"i\":\"M\",\"n\":\"+222\"},{\"c\":\"美国\",\"i\":\"M\",\"n\":\"+1\"},{\"c\":\"蒙古\",\"i\":\"M\",\"n\":\"+976\"},{\"c\":\"蒙特塞拉特岛\",\"i\":\"M\",\"n\":\"+1664\"},{\"c\":\"孟加拉国\",\"i\":\"M\",\"n\":\"+880\"},{\"c\":\"秘鲁\",\"i\":\"M\",\"n\":\"+51\"},{\"c\":\"缅甸\",\"i\":\"M\",\"n\":\"+95\"},{\"c\":\"摩尔多瓦\",\"i\":\"M\",\"n\":\"+373\"},{\"c\":\"摩洛哥\",\"i\":\"M\",\"n\":\"+212\"},{\"c\":\"摩纳哥\",\"i\":\"M\",\"n\":\"+377\"},{\"c\":\"莫桑比克\",\"i\":\"M\",\"n\":\"+258\"},{\"c\":\"墨西哥\",\"i\":\"M\",\"n\":\"+52\"},{\"c\":\"纳米比亚\",\"i\":\"N\",\"n\":\"+264\"},{\"c\":\"南非\",\"i\":\"N\",\"n\":\"+27\"},{\"c\":\"尼泊尔\",\"i\":\"N\",\"n\":\"+977\"},{\"c\":\"尼加拉瓜\",\"i\":\"N\",\"n\":\"+505\"},{\"c\":\"尼日尔\",\"i\":\"N\",\"n\":\"+227\"},{\"c\":\"尼日利亚\",\"i\":\"N\",\"n\":\"+234\"},{\"c\":\"挪威\",\"i\":\"N\",\"n\":\"+47\"},{\"c\":\"葡萄牙\",\"i\":\"P\",\"n\":\"+351\"},{\"c\":\"日本\",\"i\":\"R\",\"n\":\"+81\"},{\"c\":\"瑞典\",\"i\":\"R\",\"n\":\"+46\"},{\"c\":\"瑞士\",\"i\":\"R\",\"n\":\"+41\"},{\"c\":\"萨尔瓦多\",\"i\":\"S\",\"n\":\"+503\"},{\"c\":\"塞尔维亚共和国\",\"i\":\"S\",\"n\":\"+381\"},{\"c\":\"塞拉利昂\",\"i\":\"S\",\"n\":\"+232\"},{\"c\":\"塞内加尔\",\"i\":\"S\",\"n\":\"+221\"},{\"c\":\"塞浦路斯\",\"i\":\"S\",\"n\":\"+357\"},{\"c\":\"塞舌尔\",\"i\":\"S\",\"n\":\"+248\"},{\"c\":\"沙特阿拉伯\",\"i\":\"S\",\"n\":\"+966\"},{\"c\":\"圣多美和普林西比\",\"i\":\"S\",\"n\":\"+239\"},{\"c\":\"圣露西亚\",\"i\":\"S\",\"n\":\"+1758\"},{\"c\":\"圣马力诺\",\"i\":\"S\",\"n\":\"+378\"},{\"c\":\"斯里兰卡\",\"i\":\"S\",\"n\":\"+94\"},{\"c\":\"斯洛伐克\",\"i\":\"S\",\"n\":\"+421\"},{\"c\":\"斯洛文尼亚\",\"i\":\"S\",\"n\":\"+386\"},{\"c\":\"斯威士兰\",\"i\":\"S\",\"n\":\"+268\"},{\"c\":\"苏丹\",\"i\":\"S\",\"n\":\"+249\"},{\"c\":\"苏里南\",\"i\":\"S\",\"n\":\"+597\"},{\"c\":\"所罗门群岛\",\"i\":\"S\",\"n\":\"+677\"},{\"c\":\"索马里\",\"i\":\"S\",\"n\":\"+252\"},{\"c\":\"塔吉克斯坦\",\"i\":\"T\",\"n\":\"+992\"},{\"c\":\"泰国\",\"i\":\"T\",\"n\":\"+66\"},{\"c\":\"坦桑尼亚\",\"i\":\"T\",\"n\":\"+255\"},{\"c\":\"汤加\",\"i\":\"T\",\"n\":\"+676\"},{\"c\":\"特立尼达和多巴哥\",\"i\":\"T\",\"n\":\"+1868\"},{\"c\":\"突尼斯\",\"i\":\"T\",\"n\":\"+216\"},{\"c\":\"土耳其\",\"i\":\"T\",\"n\":\"+90\"},{\"c\":\"土库曼斯坦\",\"i\":\"T\",\"n\":\"+993\"},{\"c\":\"委内瑞拉\",\"i\":\"W\",\"n\":\"+58\"},{\"c\":\"文莱\",\"i\":\"W\",\"n\":\"+673\"},{\"c\":\"乌干达\",\"i\":\"W\",\"n\":\"+256\"},{\"c\":\"乌克兰\",\"i\":\"W\",\"n\":\"+380\"},{\"c\":\"乌拉圭\",\"i\":\"W\",\"n\":\"+598\"},{\"c\":\"乌兹别克斯坦\",\"i\":\"W\",\"n\":\"+998\"},{\"c\":\"西班牙\",\"i\":\"X\",\"n\":\"+34\"},{\"c\":\"希腊\",\"i\":\"X\",\"n\":\"+30\"},{\"c\":\"新加坡\",\"i\":\"X\",\"n\":\"+65\"},{\"c\":\"新西兰\",\"i\":\"X\",\"n\":\"+64\"},{\"c\":\"匈牙利\",\"i\":\"X\",\"n\":\"+36\"},{\"c\":\"叙利亚\",\"i\":\"X\",\"n\":\"+963\"},{\"c\":\"牙买加\",\"i\":\"Y\",\"n\":\"+1876\"},{\"c\":\"亚美尼亚\",\"i\":\"Y\",\"n\":\"+374\"},{\"c\":\"也门\",\"i\":\"Y\",\"n\":\"+967\"},{\"c\":\"伊拉克\",\"i\":\"Y\",\"n\":\"+964\"},{\"c\":\"伊朗\",\"i\":\"Y\",\"n\":\"+98\"},{\"c\":\"以色列\",\"i\":\"Y\",\"n\":\"+972\"},{\"c\":\"意大利\",\"i\":\"Y\",\"n\":\"+39\"},{\"c\":\"印度\",\"i\":\"Y\",\"n\":\"+91\"},{\"c\":\"印度尼西亚\",\"i\":\"Y\",\"n\":\"+62\"},{\"c\":\"英国\",\"i\":\"Y\",\"n\":\"+44\"},{\"c\":\"约旦\",\"i\":\"Y\",\"n\":\"+962\"},{\"c\":\"越南\",\"i\":\"Y\",\"n\":\"+84\"},{\"c\":\"赞比亚\",\"i\":\"Z\",\"n\":\"+260\"},{\"c\":\"乍得\",\"i\":\"Z\",\"n\":\"+235\"},{\"c\":\"直布罗陀\",\"i\":\"Z\",\"n\":\"+350\"},{\"c\":\"智利\",\"i\":\"Z\",\"n\":\"+56\"},{\"c\":\"中非共和国\",\"i\":\"Z\",\"n\":\"+236\"}]");

    RecyclerView mDataListView;

    AZWaveSideBarView mIndexBarView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_phone_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataListView = view.findViewById(R.id.dataList);
        mIndexBarView = view.findViewById(R.id.barList);
        view.findViewById(R.id.actionbar_back).setOnClickListener(v -> dismiss());
    }

    private TextView mResultView;

    /**
     * 绑定选择
     *
     * @param fragmentManager
     * @param tv
     * @return
     */
    public static PhoneLocationFragment bindChoose(final FragmentManager fragmentManager, final TextView tv) {
        final PhoneLocationFragment plf = new PhoneLocationFragment();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plf.bindResuleView(tv).bindPhoneLocation(tv.getText()).show(fragmentManager, "phone-location");
            }
        });
        return plf;
    }

    private String phoneLocation = "";

    private PhoneLocationFragment bindPhoneLocation(CharSequence text) {
        this.phoneLocation = text.toString();
        return this;
    }


    public PhoneLocationFragment bindResuleView(TextView resultView) {
        this.mResultView = resultView;
        return this;
    }

    /**
     * 城市选择
     *
     * @param data
     */
    private void onCityChoose(JSONObject data) {
        if (mResultView != null) {
            mResultView.setText(data.getString("n"));
        }
        dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDataListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataListView.setAdapter(new PhoneAdapter(this));
        mDataListView.addItemDecoration(new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(getContext())));
        mIndexBarView.setOnLetterChangeListener(new AZWaveSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                for (int index = 0; index < PL_DAT.size(); index++) {
                    JSONObject itemData = PL_DAT.getJSONObject(index);
                    String iLetter = itemData.getString("i");
                    if (iLetter.equals(letter)) {
                        if (mDataListView.getLayoutManager() instanceof LinearLayoutManager) {
                            LinearLayoutManager manager = (LinearLayoutManager) mDataListView.getLayoutManager();
                            manager.scrollToPositionWithOffset(index, 0);
                        } else {
                            mDataListView.getLayoutManager().scrollToPosition(index);
                        }
                        break;
                    }
                }
            }
        });
        mDataListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isUserDrag = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserDrag = true;
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE && isUserDrag) {
                    isUserDrag = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isUserDrag) {

                    RecyclerView.LayoutManager layoutManager = mDataListView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        if (firstVisibleItemPosition == RecyclerView.NO_POSITION) {
                            return;
                        }
                        JSONObject jsonObject = PL_DAT.getJSONObject(firstVisibleItemPosition);
                        mIndexBarView.updateSelect(jsonObject.getString("i"));
                    }

                }
            }
        });

        mDataListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(phoneLocation)) {
                    for (int index = 0; index < PL_DAT.size(); index++) {
                        JSONObject itemData = PL_DAT.getJSONObject(index);
                        String iLetter = itemData.getString("n");
                        if (iLetter.equals(phoneLocation)) {
                            mIndexBarView.updateSelect(itemData.getString("i"));
                            if (mDataListView.getLayoutManager() instanceof LinearLayoutManager) {
                                LinearLayoutManager manager = (LinearLayoutManager) mDataListView.getLayoutManager();
                                manager.scrollToPositionWithOffset(index, UiUtil.dp2Px(App.Companion.instance(), 22));
                            } else {
                                mDataListView.getLayoutManager().scrollToPosition(index);
                            }
                            break;
                        }
                    }
                }
            }
        }, 200);


    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DalogAnim_LR;
        dialog.getWindow().getAttributes().dimAmount = 0f;
        return dialog;
    }


    final static class PhoneAdapter extends RecyclerView.Adapter<PhoneItemHolder> {

        PhoneLocationFragment mHost;

        public PhoneAdapter(PhoneLocationFragment fragment) {
            this.mHost = fragment;
        }

        @NonNull
        @Override
        public PhoneItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_item_phone, parent, false);
            return new PhoneItemHolder(inflate, this.mHost);

        }

        @Override
        public void onBindViewHolder(@NonNull PhoneItemHolder holder, int position) {
            holder.bind(PL_DAT.getJSONObject(position));
        }

        @Override
        public int getItemCount() {
            return PL_DAT.size();
        }
    }

    final static class PhoneItemHolder extends RecyclerView.ViewHolder {
        public PhoneItemHolder(View itemView, final PhoneLocationFragment mHost) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHost.onCityChoose(data);
                }
            });
        }


        JSONObject data;

        public void bind(JSONObject data) {
            ((TextView) itemView.findViewWithTag("areaCode")).setText(data.getString("n"));
            ((TextView) itemView.findViewWithTag("areaName")).setText(data.getString("c"));
            this.data = data;
        }
    }


}
