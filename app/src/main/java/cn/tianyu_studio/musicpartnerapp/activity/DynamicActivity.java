package cn.tianyu_studio.musicpartnerapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.adapter.NotificationMultipleItemQuickAdapter;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.Dynamic;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_dynamic)
public class DynamicActivity extends BaseActivity {

    @ViewInject(R.id.dynamic_recycler)
    RecyclerView recyclerView;
    @ViewInject(R.id.dynamic_srl)
    SwipeRefreshLayout refreshLayout;
    int totalCurrent = 1;
    int current = 1;
    NotificationMultipleItemQuickAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromeb();
    }

    @Event(value = R.id.dynamic_srl, type = SwipeRefreshLayout.OnRefreshListener.class)
    private void refreshData() {
        current = 1;
        getDataFromeb();

    }

    @Event(R.id.dynamic_tv_back)
    private void back(View view) {
        finish();
    }

    private void initRecycler(List<MultiItemEntity> entities) {
        madapter = new NotificationMultipleItemQuickAdapter(this, entities);

        madapter.setOnItemClickListener((adapter, view, position) -> {
            TextView tv_unread = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.item_notification_unread);
            tv_unread.setVisibility(View.GONE);
            Dynamic dynamic = (Dynamic) adapter.getItem(position);
            setRead(dynamic.getDynamicId());

        });
        // madapter.bindToRecyclerView(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(madapter);

        madapter.setOnLoadMoreListener(() -> recyclerView.postDelayed(() -> {
            ++current;
            if (current == totalCurrent) {
                getDataFromeb();
                madapter.loadMoreEnd();
            } else {
                getDataFromeb();
                madapter.loadMoreComplete();
            }
        }, 2000), recyclerView);
        madapter.disableLoadMoreIfNotFullPage();
    }

    private void getDataFromeb() {
        refreshLayout.setRefreshing(false);
        List<MultiItemEntity> entities = new ArrayList<>();
        TRequestParams params = new TRequestParams(URLEnum.Dynamic.getUrl() + "?current=" + current);
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    totalCurrent = jsonObject.getInteger("pages");
                    JSONArray jsonArray = jsonObject.getJSONArray("records");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Dynamic dynamic = JSON.parseObject(jsonArray.get(i).toString(), Dynamic.class);
                        entities.add(dynamic);
                    }
                    if (current == 1)
                        initRecycler(entities);
                    else
                        madapter.addData(entities);
                }
            }
        });
    }

    private void setRead(long id) {
        TRequestParams params = new TRequestParams(URLEnum.Dynamic.getUrl() + "/" + id);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {

                }
            }
        });
    }

}
