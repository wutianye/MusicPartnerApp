package cn.tianyu_studio.musicpartnerapp.fragment.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.adapter.NotificationMultipleItemQuickAdapter;
import cn.tianyu_studio.musicpartnerapp.entity.Notification;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;

@ContentView(R.layout.fragment_notification)
public class NotificationFragment extends BaseFragment {

    @ViewInject(R.id.notice_recycle)
    private RecyclerView recycle_data;



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycleAdapter();
        initNoticeList();
    }

    private void initRecycleAdapter() {
        List<MultiItemEntity> entities = new ArrayList<>();
        Notification notification = new Notification();
        notification.setContent("邀请你参加线下音乐活动");
        // notification.setImgUrl("https://ss0.baidu.com/73t1bjeh1BF3odCf/it/u=1725905378,2821201149&fm=85&s=EB43CF140D907CD0084441C20300E0B2");
       // notification.setTime(new Date());
        entities.add(notification);
//        Comment comment = new Comment();
//        comment.setUserHeadImgUrl("https://ss0.baidu.com/73t1bjeh1BF3odCf/it/u=1725905378,2821201149&fm=85&s=EB43CF140D907CD0084441C20300E0B2");
//        comment.setUserName("周杰伦");
//        comment.setWorkName("爱我别走");
//        comment.setContent("歌词不错");
//        comment.setTime(new Date());
//        // comment.setImgUrl("https://ss0.baidu.com/73F1bjeh1BF3odCf/it/u=1999397370,246435114&fm=85&s=BC8223D5C850E5C662B049EA0300C012");
//        entities.add(comment);
        NotificationMultipleItemQuickAdapter madapter = new NotificationMultipleItemQuickAdapter(getContext(), entities);
        madapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {

            }
        });
        recycle_data.setAdapter(madapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycle_data.setLayoutManager(layoutManager);
    }

    private void initNoticeList() {

    }


}



