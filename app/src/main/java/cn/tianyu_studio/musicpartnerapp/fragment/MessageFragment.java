package cn.tianyu_studio.musicpartnerapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Timer;
import java.util.TimerTask;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.MainActivity;
import cn.tianyu_studio.musicpartnerapp.adapter.NotificationMultipleItemQuickAdapter;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.Notification;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.fragment_message)
public class MessageFragment extends BaseFragment {

    private static Timer timer;
    private static MyTask task;
    int totalCurrent = 1;
    int current = 1;
    NotificationMultipleItemQuickAdapter mAdapter;
    @ViewInject(R.id.message_recycler)
    private RecyclerView recyclerView;
    @ViewInject(R.id.message_srl)
    private SwipeRefreshLayout refreshLayout;
    private MainActivity mainActivity;

    public static void cancelLunxun() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        //getNotificationDataFromWeb();
        initLunxun();
    }

    private void initRecycler(List<MultiItemEntity> entities){
        mAdapter = new NotificationMultipleItemQuickAdapter(getContext(), entities);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TextView tv_unread = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.item_message_unread);
            if (tv_unread.getVisibility() != View.GONE) {
                tv_unread.setVisibility(View.GONE);
                Notification n = (Notification) adapter.getItem(position);
                setRead(n.getNotificationId());
            }
        });
        //madapter.bindToRecyclerView(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(() -> recyclerView.postDelayed(() -> {
            ++current;
            if (current >= totalCurrent) {
                getNotificationDataFromWeb();
                mAdapter.loadMoreEnd();
            } else {
                getNotificationDataFromWeb();
                mAdapter.loadMoreComplete();
            }
        }, 2000), recyclerView);
        mAdapter.disableLoadMoreIfNotFullPage();

    }

    private void getNotificationDataFromWeb() {
        refreshLayout.setRefreshing(false);
        List<MultiItemEntity> entities = new ArrayList<>();
        TRequestParams params = new TRequestParams(URLEnum.Notifiction.getUrl() + "?current=" + current);
        x.http().request(HttpMethod.GET,params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    totalCurrent = jsonObject.getInteger("pages");
                    JSONArray jsonArray = jsonObject.getJSONArray("records");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Notification notification = JSON.parseObject(jsonArray.get(i).toString(), Notification.class);
                        entities.add(notification);
                    }
                    if (current == 1) {
                        initRecycler(entities);

                    }
                    else
                        mAdapter.addData(entities);
                }
            }
        });
    }

    @Event(value = R.id.message_srl, type = SwipeRefreshLayout.OnRefreshListener.class)
    private void refreshData() {
        current = 1;
        getNotificationDataFromWeb();

    }


    public void initLunxun() {
        timer = new Timer();
        task = new MyTask();
        //schedule 计划安排，时间表
        timer.schedule(task, 5000, 100000); //100s轮询一次
    }

    private void setRead(long id) {
        TRequestParams params = new TRequestParams(URLEnum.Notifiction.getUrl() + "/" + id);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    mainActivity.getUnReadNotifiction();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        current = 1;
        getNotificationDataFromWeb();
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            Log.i("AAA", "开始执行执行timer定时任务...");
            //定时获取未读数量，onresume中更新通知列表
            mainActivity.getUnReadNotifiction();
        }
    }
}



