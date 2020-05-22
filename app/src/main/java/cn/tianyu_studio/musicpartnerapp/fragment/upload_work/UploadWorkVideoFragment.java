package cn.tianyu_studio.musicpartnerapp.fragment.upload_work;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.filter.entity.VideoFile;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.UploadWorkActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TItem;

@ContentView(R.layout.fragment_upload_work_file_list)
public class UploadWorkVideoFragment extends BaseFragment {

    public BaseQuickAdapter<TItem<VideoFile>, BaseViewHolder> adapter;
    UploadWorkActivity uploadWorkActivity;
    // 上一个选中项
    TItem<VideoFile> previousSelectItem;
    int previousSelectItemPosition = -1;
    @ViewInject(R.id.uploadWork_fileList_recycle)
    private RecyclerView recycle_music;
    @ViewInject(R.id.uploadWork_fileList_srl)
    private SwipeRefreshLayout srl;

    private boolean isMe = true;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadWorkActivity = (UploadWorkActivity) getActivity();
        initAdapter();
        srl.setOnRefreshListener(this::loadData);
        loadData();
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<TItem<VideoFile>, BaseViewHolder>(R.layout.item_upload_work_file_list, null) {
            @Override
            protected void convert(BaseViewHolder helper, TItem<VideoFile> item) {
                helper.setText(R.id.item_uploadWork_fileList_tv_fileName, item.getObject().getName());

                RadioButton rb = helper.getView(R.id.item_uploadWork_fileList_rb);
                    rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (!rb.isPressed()) return;
                        if (!uploadWorkActivity.musicPath.isEmpty() || !uploadWorkActivity.photoPaths.isEmpty()) {
                            Toast.makeText(getActivity(), "您只能选择音乐或者视频或图片其中的一种上传", Toast.LENGTH_SHORT).show();
                            rb.setChecked(false);
                        } else if (isChecked) {
                            if (previousSelectItem != null) {
                                previousSelectItem.setChecked(false);
                                if (previousSelectItemPosition != helper.getLayoutPosition())
                                    adapter.notifyItemChanged(previousSelectItemPosition);
                            }
                            previousSelectItem = item;
                            previousSelectItemPosition = helper.getLayoutPosition();
                            uploadWorkActivity.setVideoPath(item.getObject().getPath());
                            uploadWorkActivity.position[1] = helper.getLayoutPosition();
                        }
                        item.setChecked(isChecked);
                    });
                helper.setChecked(R.id.item_uploadWork_fileList_rb, item.isChecked());
            }
        };
        recycle_music.setAdapter(adapter);
        recycle_music.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadData() {
        srl.setRefreshing(true);
        FileFilter.getVideos(getActivity(), directories -> {
            List<VideoFile> videoFiles = new ArrayList<>(32);
            for (Directory<VideoFile> directory : directories) {
                videoFiles.addAll(directory.getFiles());
            }
            adapter.setNewData(TItem.toTItemList(videoFiles));
            srl.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (uploadWorkActivity.position[1] >= 0) {
            TItem<VideoFile> item = adapter.getItem(uploadWorkActivity.position[1]);
            item.setChecked(true);
            previousSelectItem = item;
            previousSelectItemPosition = uploadWorkActivity.position[1];
            adapter.notifyDataSetChanged();
        }

    }
}
