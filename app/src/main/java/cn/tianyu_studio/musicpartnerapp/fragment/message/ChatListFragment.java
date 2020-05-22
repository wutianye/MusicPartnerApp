package cn.tianyu_studio.musicpartnerapp.fragment.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.entity.Chat;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;

@ContentView(R.layout.fragment_chat_list)
public class ChatListFragment extends BaseFragment {

    @ViewInject(R.id.chatList_recycle)
    private RecyclerView recycle_chatList;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Chat> chatList = new ArrayList<>();
        Chat chat = new Chat();
        chat.setImgUrl("https://upload.jianshu.io/users/upload_avatars/972352/8432d981-ac19-450c-bb25-e134d7f26385.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96");
        chat.setUserName("小飞侠");
        chat.setContent("你的作品很棒");
        chatList.add(chat);
        chatList.add(chat);
        chatList.add(chat);

        BaseQuickAdapter<Chat, BaseViewHolder> adapter = new BaseQuickAdapter<Chat, BaseViewHolder>(R.layout.item_chat_list, chatList) {
            @Override
            protected void convert(BaseViewHolder helper, Chat item) {
                helper.setText(R.id.item_chat_list_tv_userName, item.getUserName())
                        .setText(R.id.item_chat_list_tv_content, item.getContent())
                        .setText(R.id.item_chat_list_tv_time, "20:05");
                ImageView imageView = helper.getView(R.id.item_chat_list_iv_userHeadImg);
                x.image().bind(imageView, item.getImgUrl(), new ImageOptions.Builder().setCircular(true).build());
            }
        };
        recycle_chatList.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Intent intent = new Intent(getActivity(), ChatActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        recycle_chatList.setAdapter(adapter);
        recycle_chatList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
