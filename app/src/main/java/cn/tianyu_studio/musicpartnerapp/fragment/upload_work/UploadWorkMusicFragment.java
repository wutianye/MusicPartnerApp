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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.UploadWorkActivity;
import cn.tianyu_studio.musicpartnerapp.file.FileManager;
import cn.tianyu_studio.musicpartnerapp.file.bean.Music;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TItem;

@ContentView(R.layout.fragment_upload_work_file_list)
public class UploadWorkMusicFragment extends BaseFragment {

    public BaseQuickAdapter<TItem<Music>, BaseViewHolder> adapter;
    UploadWorkActivity uploadWorkActivity;
    // 上一个选中项
    TItem<Music> previousSelectItem;
    int previousSelectItemPosition = -1;
    @ViewInject(R.id.uploadWork_fileList_recycle)
    private RecyclerView recycle_music;
    @ViewInject(R.id.uploadWork_fileList_srl)
    private SwipeRefreshLayout srl;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadWorkActivity = (UploadWorkActivity) getActivity();
        initAdapter();
        initSrl();
        loadData();
    }

    private void initSrl() {
        srl.setOnRefreshListener(this::loadData);
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<TItem<Music>, BaseViewHolder>(R.layout.item_upload_work_file_list, null) {
            @Override
            protected void convert(BaseViewHolder helper, TItem<Music> item) {
                helper.setText(R.id.item_uploadWork_fileList_tv_fileName, item.getObject().getName());
                RadioButton rb = helper.getView(R.id.item_uploadWork_fileList_rb);
                rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!rb.isPressed()) return;
                    if (!uploadWorkActivity.videoPath.isEmpty() || !uploadWorkActivity.photoPaths.isEmpty()) {
                        Toast.makeText(getActivity(), "您只能选择音乐或者视频或图片其中的一种上传", Toast.LENGTH_SHORT).show();
                        rb.setChecked(false);
                    } else if (isChecked) {
                        if (previousSelectItem != null) {
                            if (previousSelectItemPosition != helper.getLayoutPosition()) {
                                previousSelectItem.setChecked(false);
                                adapter.notifyItemChanged(previousSelectItemPosition);
                            }
                        }
                        previousSelectItem = item;
                        previousSelectItemPosition = helper.getLayoutPosition();
                        uploadWorkActivity.setMusicPath(item.getObject().getPath());
                        uploadWorkActivity.position[0] = helper.getLayoutPosition();
                    }
                    item.setChecked(isChecked);
                    // LogUtil.e("change: " + helper.getAdapterPosition() + JSON.toJSONString(item));
                });
                // 这条语句的时序非常重要，必须要放在listener下面，否则会先触发复用view的listener
                helper.setChecked(R.id.item_uploadWork_fileList_rb, item.isChecked());

            }
        };

        recycle_music.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle_music.setAdapter(adapter);
        adapter.bindToRecyclerView(recycle_music);

    }

    private void loadData() {
        srl.setRefreshing(true);
//        FileFilter.getAudios(getActivity(), directories -> {
//            List<AudioFile> audioFiles = new ArrayList<>(32);
//            for (Directory<AudioFile> directory : directories) {
//                audioFiles.addAll(directory.getFiles());
//            }
//            adapter.setNewData(TItem.toTItemList(audioFiles));
//            srl.setRefreshing(false);
//        });
        List<Music> files = new ArrayList<>();
        files = FileManager.getInstance(getActivity()).getMusics();
        adapter.setNewData(TItem.toTItemList(files));
        srl.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (uploadWorkActivity.position[0] >= 0) {
            TItem<Music> item = adapter.getItem(uploadWorkActivity.position[0]);
            item.setChecked(true);
            previousSelectItem = item;
            previousSelectItemPosition = uploadWorkActivity.position[0];
            adapter.notifyDataSetChanged();
        }

    }


}