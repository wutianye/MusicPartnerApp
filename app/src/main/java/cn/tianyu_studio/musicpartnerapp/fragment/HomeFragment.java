package cn.tianyu_studio.musicpartnerapp.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhouwei.library.CustomPopWindow;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.HttpMethod;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.PlayActivity;
import cn.tianyu_studio.musicpartnerapp.activity.UserPageActivity;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.Tag;
import cn.tianyu_studio.musicpartnerapp.entity.Work;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;


@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    /**
     * 上一次点击的workId，在play界面中使用
     */
    public static long selectWorkId = 0;

    BaseQuickAdapter<Work, BaseViewHolder> homeAdapter;
    RelativeLayout filterLayout;
    CustomPopWindow headerPopWindow;
    // List<Work> workList = new ArrayList<>();
    List<Tag> tagList = new ArrayList<>();
    // 筛选条件，包括分页数据
    int selectTagId = -1, sex = -1, distance = -1, minAge = 0, maxAge = 0, type = 0, current = 1, size = 10;
    int totalCurrent = 1;
    @ViewInject(R.id.home_spinner_ll)
    LinearLayout ll_spinner;
    @ViewInject(R.id.home_recycle_data)
    private RecyclerView recycle_data;
    @ViewInject(R.id.home_rl_head)
    private RelativeLayout rl_head;
    @ViewInject(R.id.home_spinner_gender)
    private fr.ganfra.materialspinner.MaterialSpinner spinner_gender;
    @ViewInject(R.id.home_spinner_distance)
    private fr.ganfra.materialspinner.MaterialSpinner spinner_distance;
    @ViewInject(R.id.home_spinner_age)
    private fr.ganfra.materialspinner.MaterialSpinner spinner_age;
    @ViewInject(R.id.home_srl)
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTags();
        //initWorks();
        initSpinner();

    }

    @Event(value = R.id.home_srl, type = SwipeRefreshLayout.OnRefreshListener.class)
    private void refreshData() {
        current = 1;
        initWorks();
    }

    /**
     * 设置头部filter
     * 设置spinner的宽度，并令其文字居中
     */
    private void initSpinner() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        int width = dm.widthPixels;

        // 设置下拉菜单宽度铺满全屏
        spinner_gender.setDropDownWidth(width);
        spinner_distance.setDropDownWidth(width);
        spinner_age.setDropDownWidth(width);

        ArrayAdapter<String> genderAdapter;
        genderAdapter = new ArrayAdapter<>(getContext(), R.layout.item_home_spinner, getResources().getStringArray(R.array.home_filter_gender));
        spinner_gender.setAdapter(genderAdapter);


        ArrayAdapter<String> distanceAdapter = new ArrayAdapter<>(getContext(), R.layout.item_home_spinner, getResources().getStringArray(R.array.home_filter_distance));
        spinner_distance.setAdapter(distanceAdapter);

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(getContext(), R.layout.item_home_spinner, getResources().getStringArray(R.array.home_filter_age));
        spinner_age.setAdapter(ageAdapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sex = 0;
                        type = 0;
                        current = 1;
                        break;
                    case 1:
                        sex = 1;
                        type = 0;
                        current = 1;
                        break;
                    case 2:
                        sex = -1;
                        type = 0;
                        current = 1;
                        break;
                }
                initWorks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        minAge = 0;
                        maxAge = 20;
                        type = 0;
                        current = 1;
                        break;
                    case 1:
                        minAge = 20;
                        maxAge = 35;
                        type = 0;
                        current = 1;
                        break;
                    case 2:
                        minAge = 35;
                        maxAge = 55;
                        type = 0;
                        current = 1;
                        break;
                    case 3:
                        minAge = 0;
                        maxAge = 0;
                        type = 0;
                        current = 1;
                        break;
                }
                initWorks();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 切换标题显示状态
     */
    @Event(value = {R.id.home_tv_title, R.id.home_img_drop})
    private void toggleTitle(View view) {
        headerPopWindow.showAsDropDown(rl_head);
    }

    private void initHeaderPopWindow() {
        headerPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(filterLayout)    //显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)  //是否PopupWindow 以外触摸dissmiss
                .create();                    //创建PopupWindow
    }

    /**
     * 初始化头部filter数据，并初始化filter点击事件
     */
    private void initFilterLayout() {
        filterLayout = new RelativeLayout(getContext());
        filterLayout.setBackgroundColor(getResources().getColor(R.color.white));
        filterLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DensityUtil.dip2px(5), 0, DensityUtil.dip2px(20));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        RecyclerView recycle_filter = new RecyclerView(getContext());
        recycle_filter.setLayoutManager(new GridLayoutManager(getContext(), 4));    //网格布局，一行显示4个item
        recycle_filter.setAdapter(initFilterAdapter());
        recycle_filter.setLayoutParams(params);

        filterLayout.addView(recycle_filter);
    }

    /**
     * 切换为团队展作品
     */
    @Event(R.id.home_tv_teamShow)
    private void teamShow(View view) {
        ll_spinner.setVisibility(View.GONE);
        selectTagId = -1;
        type = 1;
        current = 1;
        initWorks();
    }

    private BaseQuickAdapter<Tag, BaseViewHolder> initFilterAdapter() {
        return new BaseQuickAdapter<Tag, BaseViewHolder>(R.layout.item_home_filter, tagList) {
            @Override
            protected void convert(BaseViewHolder helper, Tag item) {
                helper.setText(R.id.item_home_head_filter_btn, item.getName());
                helper.getView(R.id.item_home_head_filter_btn).setOnClickListener(v -> {
                    ll_spinner.setVisibility(View.VISIBLE);
                    selectTagId = item.getTagId();
                    type = 0;
                    current = 1;
                    initWorks();
                    headerPopWindow.dissmiss();
                });
            }
        };
    }

    private void initHomeAdapter(List<Work> workList) {
        ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build(); //圆形图片
        homeAdapter = new BaseQuickAdapter<Work, BaseViewHolder>(R.layout.item_work_list, workList) {
            @Override
            protected void convert(BaseViewHolder helper, Work item) {

                StringBuilder workTagText = new StringBuilder();
                for (int i = 0; i < item.getWorkTags().size(); i++) {
                    workTagText.append(item.getWorkTags().get(i).getTagName()).append(" ");
                }
                helper.setText(R.id.item_workList_tv_title, item.getName());
                helper.setText(R.id.item_workList_tv_type, workTagText.toString());
                helper.setText(R.id.item_workList_tv_like_num, item.getFavCount() + "");
                helper.setText(R.id.item_workList_tv_play_num, item.getBrowseCount() + "");
                helper.setText(R.id.item_workList_tv_username, item.getAuthor().getNickname());
                helper.setText(R.id.item_workList_tv_intro, item.getAuthor().getTag());
                RelativeLayout rl_bg = helper.getView(R.id.item_workList_rl);
//                x.image().loadDrawable(item.getCoverUrl(), null, new BaseCallback<Drawable>() {
//
//                    @Override
//                    public void onSuccess(Drawable drawable) {
//                        rl_bg.setBackground(drawable);
//                    }
//                });
                //这里因为xutils加载图片总是不加载  这里采用glide
                Glide.with(HomeFragment.this).load(item.getCoverUrl()).apply(new RequestOptions()
                        .fitCenter().skipMemoryCache(false))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                rl_bg.setBackground(resource);
                            }
                        });
                ImageView img_head = helper.getView(R.id.item_workList_img_head);
                if (item.getAuthor() != null)
                    x.image().bind(img_head, item.getAuthor().getHeadImgUrl(), imageOptions);
                helper.addOnClickListener(R.id.item_workList_btn_menu)
                        .addOnClickListener(R.id.item_workList_rl)
                        .addOnClickListener(R.id.item_workList_img_head);
            }
        };
        homeAdapter.setOnItemChildClickListener((adapter1, view, position) -> {
            switch (view.getId()) {
                case R.id.item_workList_btn_menu:
                    showMenu(view, homeAdapter.getItem(position));
                    break;
                case R.id.item_workList_rl:
                    // 记录点击的workId信息，并跳转到play界面
                    selectWorkId = workList.get(position).getWorkId();
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    startActivity(intent);
                    break;

                case R.id.item_workList_img_head:
                    Intent i = new Intent(getActivity(), UserPageActivity.class);
                    i.putExtra("id", workList.get(position).getAuthorId());
                    startActivity(i);
                    break;
            }
        });

        //homeAdapter.bindToRecyclerView(recycle_data);

        // homeAdapter.setPreLoadNumber(4);

        recycle_data.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycle_data.setAdapter(homeAdapter);

        homeAdapter.setOnLoadMoreListener(() -> recycle_data.postDelayed(() -> {
            ++current;
            initWorks();
            if (current >= totalCurrent)
                homeAdapter.loadMoreEnd();
            else
                homeAdapter.loadMoreComplete();
        }, 3000), recycle_data);
        homeAdapter.disableLoadMoreIfNotFullPage();
    }



    /**
     * 根据条件初始化作品数据
     */
    private void initWorks() {
        refreshLayout.setRefreshing(true);
        TRequestParams params = new TRequestParams(URLEnum.Work.getUrl());
        params.addQueryParameter("tagId", selectTagId);
        params.addQueryParameter("sex", sex);
        params.addQueryParameter("distance", distance);
        params.addQueryParameter("minAge", minAge);
        params.addQueryParameter("maxAge", maxAge);
        params.addQueryParameter("current", current);
        params.addQueryParameter("type", type);
        params.addQueryParameter("size", size);
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    totalCurrent = jsonObject.getInteger("pages");
                    // 如果是上拉加载更多状态
                    if (current > 1) {
                        homeAdapter.addData(JSON.parseArray(jsonObject.getString("records"), Work.class));
                    } else
                        initHomeAdapter(JSON.parseArray(jsonObject.getString("records"), Work.class));
                }
            }

            @Override
            public void onFinished() {
                refreshLayout.setRefreshing(false);
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
                Tag tag = new Tag();
                tag.setTagId(-1);
                tag.setName("全部");
                tagList.add(tag);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        selectTagId = -1;
        sex = -1;
        distance = -1;
        minAge = 0;
        maxAge = 0;
        type = 0;
        current = 1;
        size = 10;
        initWorks();
    }
}
