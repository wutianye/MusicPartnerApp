package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.zhouwei.library.CustomPopWindow;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.Tag;
import cn.tianyu_studio.musicpartnerapp.entity.Work;
import cn.tianyu_studio.musicpartnerapp.entity.WorkTag;
import cn.tianyu_studio.musicpartnerapp.fragment.HomeFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_fill_information)
public class FillInformationActivity extends BaseActivity {
    @ViewInject(R.id.fillInformation_tv_allchoose)
    TextView choose_tv;
    @ViewInject(R.id.fillInformation_radio)
    RadioGroup radioGroup;
    @ViewInject(R.id.fillInformation_fenlei)
    LinearLayout linearLayout;
    @ViewInject(R.id.fillInformation_name)
    TextView tv_name;
    @ViewInject(R.id.fillInformation_et_content)
    TextView et_content;
    @ViewInject(R.id.fillInformation_radio_geren)
    RadioButton rb_geren;
    @ViewInject(R.id.ufillInformation_radio_tuandui)
    RadioButton rb_tuandui;
    List<Tag> tagList;
    CustomPopWindow headerPopWindow;
    RelativeLayout filterLayout;
    int type = 2;
    long id;
    Work work;
    private int[] select_btn = new int[16];
    private int select_num = 0;
    private String select_content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        id = i.getLongExtra("workId", 0);
        initWorkData();
        initTags();
        monitoringRadioGrop();
    }

    @Event(R.id.fillInformation_publish)
    private void startUpload(View view) {
        judgeInformation();
    }

    @Event(R.id.fillInformation_fenlei)
    private void fenlei(View view) {
        select_content = "";
        headerPopWindow.showAsDropDown(linearLayout);
    }

    @Event(R.id.fillInformation_tv_close)
    private void close(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Event(R.id.fillInformation_tv_return)
    private void go(View view) {
        finish();
    }

    private void judgeInformation() {
        if (type == 2) {
            Toast.makeText(FillInformationActivity.this, "请选择你的作品类型为个人或团队", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == 0 && select_content.isEmpty()) {
            Toast.makeText(FillInformationActivity.this, "请完整填写作品标签", Toast.LENGTH_SHORT).show();
            return;
        }
        changeWorkData();
    }


    private void changeWorkData() {
        List<WorkTag> workTagList = new ArrayList<>();
        for (int k = 0; k < 16; k++) {
            if (select_btn[k] == 1) {
                WorkTag tag = new WorkTag();
                tag.setTagId(tagList.get(k).getTagId());
                tag.setTagName(tagList.get(k).getName());
                workTagList.add(tag);
            }
        }
        TRequestParams params = new TRequestParams(URLEnum.Work.getUrl());
        params.addParameter("workId", id);
        params.addBodyParameter("name", tv_name.getText().toString());
        params.addBodyParameter("introduction", et_content.getText().toString());
        String json = JSON.toJSONString(workTagList);
        params.addParameter("type", type);
        params.addParameter("workTags", json);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult tMessage) {
                Toast.makeText(FillInformationActivity.this, "修改成功", Toast.LENGTH_SHORT);
                HomeFragment.selectWorkId = work.getWorkId();
                Intent intent = new Intent(FillInformationActivity.this, PlayActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initTags() {
        TRequestParams params = new TRequestParams(URLEnum.Tag.getUrl());
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    Log.d("我拿到tag了", result.getData().toString());
                    tagList = JSON.parseArray(result.getData().toString(), Tag.class);
                    initFilterLayout();
                    initHeaderPopWindow();
                }
            }
        });
    }

    private BaseQuickAdapter<Tag, BaseViewHolder> initFilterAdapter() {

        return new BaseQuickAdapter<Tag, BaseViewHolder>(R.layout.item_home_filter, tagList) {
            @Override
            protected void convert(BaseViewHolder helper, Tag item) {
                helper.setText(R.id.item_home_head_filter_btn, item.getName());
                Button btn = helper.getView(R.id.item_home_head_filter_btn);
                helper.getView(R.id.item_home_head_filter_btn).setOnClickListener(v -> {

                    if (select_btn[helper.getAdapterPosition()] == 0) {
                        if (select_num < 3) {
                            select_num++;
                            btn.setBackgroundResource(R.drawable.mybutton_selected);
                            //btn.setTextColor(getResources().getColor(R.color.app_color));
                            select_btn[helper.getAdapterPosition()] = 1;
                            select_content += item.getName() + "、";
                            choose_tv.setText(select_content);
                        } else
                            Toast.makeText(FillInformationActivity.this, "最多选择三项", Toast.LENGTH_SHORT).show();
                    } else {
                        select_num--;
                        select_content = "";
                        btn.setBackgroundResource(R.drawable.home_filter_item);
                        //btn.setTextColor(Color.parseColor("#000000"));
                        select_btn[helper.getAdapterPosition()] = 0;
                        for (int i1 = 0; i1 < 16; i1++) {
                            if (select_btn[i1] == 1)
                                select_content += tagList.get(i1).getName() + "、";
                        }
                        choose_tv.setText(select_content);
                    }
                });
            }
        };
    }

    private void initFilterLayout() {
        filterLayout = new RelativeLayout(FillInformationActivity.this);
        filterLayout.setBackgroundColor(getResources().getColor(R.color.white));
        filterLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DensityUtil.dip2px(5), 0, DensityUtil.dip2px(20));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        RecyclerView recycle_filter = new RecyclerView(FillInformationActivity.this);
        recycle_filter.setLayoutManager(new GridLayoutManager(FillInformationActivity.this, 4));    //网格布局，一行显示4个item
        recycle_filter.setAdapter(initFilterAdapter());
        recycle_filter.setLayoutParams(params);

        recycle_filter.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                //headerPopWindow.dissmiss();

            }
        });

        filterLayout.addView(recycle_filter);
    }

    private void initHeaderPopWindow() {
        headerPopWindow = new CustomPopWindow.PopupWindowBuilder(FillInformationActivity.this)
                .setView(filterLayout)    //显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)  //是否PopupWindow 以外触摸dissmiss
                .create();                    //创建PopupWindow
    }

    private void monitoringRadioGrop() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.uploadNext_radio_geren:
                            linearLayout.setVisibility(View.VISIBLE);
                            type = 0;
                            break;
                        case R.id.uploadNext_radio_tuandui:
                            linearLayout.setVisibility(View.GONE);
                            type = 1;
                            break;
                    }
                }
        );
    }

    private void initWorkData() {
        TRequestParams tRequestParams = new TRequestParams(URLEnum.Work.getUrl() + "/" + id);
        x.http().request(HttpMethod.GET, tRequestParams, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    work = JSON.parseObject(result.getData().toString(), Work.class);
                    tv_name.setText(work.getName());
                    select_content = work.getIntroduction();
                    et_content.setText(work.getIntroduction());
                    type = work.getType();
                    if (work.getType() == 0)
                        rb_geren.setChecked(true);
                    else
                        rb_tuandui.setChecked(true);
                    List<WorkTag> list = work.getWorkTags();
                    StringBuilder content = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0)
                            content.append(list.get(i).getTagName());
                        else
                            content.append(" ").append(list.get(i).getTagName());
                    }
                    choose_tv.setText(content.toString());
                }
            }
        });
    }
}
