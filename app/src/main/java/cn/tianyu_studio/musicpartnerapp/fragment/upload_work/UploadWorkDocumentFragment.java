package cn.tianyu_studio.musicpartnerapp.fragment.upload_work;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.filter.entity.NormalFile;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.UploadWorkActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TItem;

@ContentView(R.layout.fragment_upload_work_file_list)
public class UploadWorkDocumentFragment extends BaseFragment {

    BaseQuickAdapter<TItem<NormalFile>, BaseViewHolder> adapter;
    UploadWorkActivity uploadWorkActivity;
    // 上一个选中项
    TItem<NormalFile> previousSelectItem;
    int previousSelectItemPosition = -1;
    // 歌词文件后缀
    String[] suffix = new String[]{".txt", ".lrc"};
    @ViewInject(R.id.uploadWork_fileList_recycle)
    private RecyclerView recycle_document;
    @ViewInject(R.id.uploadWork_fileList_srl)
    private SwipeRefreshLayout srl;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadWorkActivity = (UploadWorkActivity) getActivity();
        initAdapter();
        srl.setOnRefreshListener(this::loadData);
        loadData();
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<TItem<NormalFile>, BaseViewHolder>(R.layout.item_upload_work_file_list, null) {
            @Override
            protected void convert(BaseViewHolder helper, TItem<NormalFile> item) {
                helper.setText(R.id.item_uploadWork_fileList_tv_fileName, item.getObject().getName());

                RadioButton rb = helper.getView(R.id.item_uploadWork_fileList_rb);
                rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (previousSelectItem != null) {
                            previousSelectItem.setChecked(false);
                            LogUtil.e("pos" + previousSelectItemPosition);
                            if (previousSelectItemPosition != helper.getLayoutPosition())
                                adapter.notifyItemChanged(previousSelectItemPosition);
                        }
                        previousSelectItem = item;
                        previousSelectItemPosition = helper.getLayoutPosition();
                        //uploadWorkActivity.setDocumentPath(item.getObject().getPath());
                    }
                    item.setChecked(isChecked);
                    LogUtil.e("change: " + helper.getAdapterPosition() + JSON.toJSONString(item));
                });
                // 这条语句的时序非常重要，必须要放在listener下面，否则会先触发复用view的listener
                helper.setChecked(R.id.item_uploadWork_fileList_rb, item.isChecked());

            }
        };
        recycle_document.setAdapter(adapter);
        recycle_document.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadData() {
        srl.setRefreshing(true);
        FileFilter.getFiles(getActivity(), directories -> {
            List<NormalFile> normalFiles = new ArrayList<>(32);
            for (Directory<NormalFile> directory : directories) {
                normalFiles.addAll(directory.getFiles());
            }
            adapter.setNewData(TItem.toTItemList(normalFiles));
            srl.setRefreshing(false);
        }, suffix);
    }
}
