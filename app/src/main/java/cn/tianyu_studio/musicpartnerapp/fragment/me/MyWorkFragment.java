package cn.tianyu_studio.musicpartnerapp.fragment.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.http.HttpMethod;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.PlayActivity;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.Work;
import cn.tianyu_studio.musicpartnerapp.fragment.HomeFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;


/**
 * 作品集fragment
 */
@ContentView(R.layout.fragment_my_work)
public class MyWorkFragment extends BaseFragment {
    BaseQuickAdapter<Work, BaseViewHolder> workAdapter;
    int totalCurrent = 1;
    int current = 1;
    @ViewInject(R.id.me_ll_dynamic)
    RelativeLayout relativeLayout;
    @ViewInject(R.id.myWork_recycle)
    private RecyclerView recycle_myWork;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayout.setVisibility(View.GONE);
        getDataFromWeb();
    }

    private void initHomeAdapter(List<Work> list) {
        ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build(); //圆形图片
        workAdapter = new BaseQuickAdapter<Work, BaseViewHolder>(R.layout.item_work_list, list) {
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
                Glide.with(getActivity()).load(item.getCoverUrl())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                rl_bg.setBackground(resource);
                            }
                        });
                ImageView img_head = helper.getView(R.id.item_workList_img_head);
                x.image().bind(img_head, item.getAuthor().getHeadImgUrl(), imageOptions);
                helper.addOnClickListener(R.id.item_workList_btn_menu).addOnClickListener(R.id.item_workList_rl);
            }
        };

//        workAdapter.bindToRecyclerView(recycle_myWork);
        workAdapter.setOnItemChildClickListener((adapter1, view, position) -> {
            switch (view.getId()) {
                case R.id.item_workList_btn_menu:
                    showMenu(view, workAdapter.getItem(position));
                    break;
                case R.id.item_workList_rl:
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    // TODO 更改为work id
                    HomeFragment.selectWorkId = workAdapter.getItem(position).getWorkId();
                    startActivity(intent);
                    break;
            }
        });

        recycle_myWork.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycle_myWork.setAdapter(workAdapter);

        workAdapter.setOnLoadMoreListener(() -> recycle_myWork.postDelayed(() -> {
            ++current;
            if (current >= totalCurrent) {
                getDataFromWeb();
                workAdapter.loadMoreEnd();
            } else {
                getDataFromWeb();
                workAdapter.loadMoreComplete();
            }
        }, 3000), recycle_myWork);
        workAdapter.disableLoadMoreIfNotFullPage();
    }


    private void getDataFromWeb() {
        TRequestParams params = new TRequestParams(URLEnum.MyWork.getUrl() + "?current=" + current);
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    totalCurrent = jsonObject.getInteger("pages");
                    if (current > 1) {
                        workAdapter.addData(JSON.parseArray(jsonObject.getString("records"), Work.class));
                    } else {
                        initHomeAdapter(JSON.parseArray(jsonObject.getString("records"), Work.class));
                    }
                }
            }
        });
    }

    public void refreshWork() {
        getDataFromWeb();
    }
}
