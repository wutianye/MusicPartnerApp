package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.entity.Folder;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;

@ContentView(R.layout.dialog_play_add_to_folder)
public class AddToFolderDialogFragment extends BaseDialogFragment {

    public static final String KEY_MAP = "map";
    public static final String KEY_AUTHOR_HEAD_IMG = "authorHeadImg";
    public static final String KEY_AUTHOR_NAME = "authorName";
    public static final String KEY_WORK_NAME = "workName";

    public static final String MV = "MV";
    public static final String MUSIC = "song_red";

    @ViewInject(R.id.play_dialog_addToFolder_recycle)
    private RecyclerView recyclerView;
    @ViewInject(R.id.play_dialog_addToFolder_iv_newFolder)
    private ImageView iv_newFolder;
    @ViewInject(R.id.play_dialog_addToFolder_tv_newFolder)
    private TextView tv_newFolder;
    @ViewInject(R.id.play_dialog_addToFolder_tv_cancel)
    private TextView tv_cancel;

    private BaseQuickAdapter<Folder, BaseViewHolder> adapter;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        initAdapter();
        initFolderList();
    }

    private void initFolderList() {

        List<Folder> folders = new ArrayList<>();
        Folder folder = new Folder();
        folder.setCoverUrl("https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=1064153686,4175869584&fm=85&s=0110CD33536273011C6885D90300D021");
        folder.setName("我喜欢的音乐");
        folder.setSize(100);
        for (int i = 0; i < 9; i++) {
            folders.add(folder);
        }
        adapter.setNewData(folders);
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<Folder, BaseViewHolder>(R.layout.item_play_dialog_add_to_folder, null) {
            @Override
            protected void convert(BaseViewHolder helper, Folder item) {
                ImageView imageView = helper.getView(R.id.item_play_dialog_addToFolder_iv_folderCover);
                x.image().bind(imageView, item.getCoverUrl());

                helper.setText(R.id.item_play_dialog_addToFolder_tv_folderName, item.getName())
                        .setText(R.id.item_play_dialog_addToFolder_tv_folderSize, item.getSize() + "首");
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Event(value = {R.id.play_dialog_addToFolder_iv_newFolder, R.id.play_dialog_addToFolder_tv_newFolder})
    private void newFolder(View view) {

    }
}
