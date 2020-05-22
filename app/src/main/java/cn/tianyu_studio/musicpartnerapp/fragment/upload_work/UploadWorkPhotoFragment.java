package cn.tianyu_studio.musicpartnerapp.fragment.upload_work;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.UploadWorkActivity;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.divider.ChooseCoverDivider;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TItem;

@ContentView(R.layout.fragment_upload_work_photo)
public class UploadWorkPhotoFragment extends BaseFragment {

    UploadWorkActivity uploadWorkActivity;
    int currSelectedNum = 0;
    @ViewInject(R.id.uploadWork_photo_recycle)
    private RecyclerView recycle_photo;
    private BaseQuickAdapter<TItem<ImageFile>, BaseViewHolder> adapter;
    private int position[] = new int[]{-1, -1, -1, -1};

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        loadImages();
        uploadWorkActivity = (UploadWorkActivity) getActivity();
    }

    private void initAdapter() {
        int divideHeight = 10;
        adapter = new BaseQuickAdapter<TItem<ImageFile>, BaseViewHolder>(R.layout.item_upload_work_image_list,null) {
            @Override
            protected void convert(BaseViewHolder helper, TItem<ImageFile> item) {
                ImageView imageView = helper.getView(R.id.item_uploadWork_imageList_iv);
               // x.image().bind(imageView, item.getObject().getPath());
                Glide.with(getActivity())
                        .load(item.getObject().getPath())
                        .into(imageView);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                // 减去span
                params.height = params.width = (ScreenUtils.getScreenWidth() - divideHeight) / 4 - divideHeight;

                CheckBox cb = helper.getView(R.id.item_uploadWork_imageList_cb);
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!cb.isPressed()) return;
                    if (!uploadWorkActivity.musicPath.isEmpty() || !uploadWorkActivity.videoPath.isEmpty()) {
                        Toast.makeText(getActivity(), "您只能选择音乐或者视频或图片其中的一种上传", Toast.LENGTH_SHORT).show();
                        cb.setChecked(false);
                        cb.setClickable(false);
                    } else if (isChecked) {
                        if (currSelectedNum == SysConsts.MAX_IMAGE_UPLOAD_SIZE) {
                            buttonView.setChecked(false);
                            Toast.makeText(getContext(), "上传图片数量达到上限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currSelectedNum++;
                        //TODO 这个地方不起作用
                        helper.setVisible(R.id.item_uploadWork_imageList_iv_shadow, true);
                        uploadWorkActivity.addPhoto(item.getObject().getPath());
                        position[currSelectedNum - 1] = helper.getAdapterPosition();

                    } else {
                        currSelectedNum--;
                        // 不起作用
                        helper.setVisible(R.id.item_uploadWork_imageList_iv_shadow, false);
                        uploadWorkActivity.removePhoto(item.getObject().getPath());
                    }
                    item.setChecked(isChecked);
                });
                helper.setChecked(R.id.item_uploadWork_imageList_cb, item.isChecked());
            }
        };
        recycle_photo.setAdapter(adapter);
        recycle_photo.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recycle_photo.addItemDecoration(new ChooseCoverDivider(divideHeight, 4));
    }

    private void loadImages() {
        FileFilter.getImages(getActivity(), directories -> {
            List<ImageFile> imageFiles = new ArrayList<>(32);
            for (Directory<ImageFile> directory : directories) {
                imageFiles.addAll(directory.getFiles());
            }
            adapter.setNewData(TItem.toTItemList(imageFiles));
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        for (int i = 0; i < 4; i++) {
            if (position[i] >= 0) {
                TItem<ImageFile> imageFileTItem = adapter.getItem(position[i]);
                imageFileTItem.setChecked(true);
                adapter.notifyDataSetChanged();
            }
        }

    }
}
