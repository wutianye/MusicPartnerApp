package cn.tianyu_studio.musicpartnerapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyco.tablayout.SlidingTabLayout;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.fragment.upload_work.UploadWorkMusicFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.upload_work.UploadWorkPhotoFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.upload_work.UploadWorkVideoFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;

import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

@ContentView(R.layout.activity_upload_work)
public class UploadWorkActivity extends BaseActivity {

    public String musicPath = "";
    public String videoPath = "";
    //public String documentPath = "";
    public List<String> photoPaths = new ArrayList<>(4);
    public int position[] = new int[]{-1, -1, -1};
    @ViewInject(R.id.uploadWork_tabLayout)
    SlidingTabLayout tabLayout;
    @ViewInject(R.id.uploadWork_viewPager)
    ViewPager viewPager;
    ImageFile coverFile;
    ArrayList<Fragment> fragments = new ArrayList<>();
    @ViewInject(R.id.uploadWork_iv_cover)
    private ImageView iv_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragments.add(new UploadWorkMusicFragment());
        fragments.add(new UploadWorkVideoFragment());
        //fragments.add(new UploadWorkDocumentFragment());
        fragments.add(new UploadWorkPhotoFragment());
        tabLayout.setViewPager(viewPager, getResources().getStringArray(R.array.uploadWork_tab_title), UploadWorkActivity.this, fragments);
    }

    @Event(R.id.uploadWork_tv_close)
    private void close(View view) {
        finish();
    }

    @Event(R.id.uploadWork_tv_next)
    private void next(View view){
        if (coverFile == null) {
            Toast.makeText(this, "请先上传封面", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this,UploadNextActivity.class);
        i.putExtra("cover",coverFile.getPath());
        i.putExtra("music",musicPath);
        i.putExtra("video",videoPath);
        i.putStringArrayListExtra("photo", (ArrayList<String>) photoPaths);
        startActivity(i);

    }

//    @Event(R.id.uploadWork_btn_startUpload)
//    private void start(View view) {
//        if (coverFile == null) {
//            Toast.makeText(this, "请先上传封面", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(UploadWorkActivity.this, FillInformationActivity.class);
//        intent.putExtra("imageFile", coverFile);
//        startActivity(intent);
//    }

    @Event(R.id.uploadWork_iv_cover)
    private void chooseCover(View view) {
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_CAMERA, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    public void setMusicPath(String path) {
        this.musicPath = path;
    }

    public void setVideoPath(String path) {
        this.videoPath = path;
    }

//    public void setDocumentPath(String path) {
//        this.documentPath = path;
//    }

    public void addPhoto(String path) {
        photoPaths.add(path);
    }

    public void removePhoto(String path) {
        photoPaths.remove(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_PICK_IMAGE:
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    if (list.size() > 0) {
                        coverFile = list.get(0);
                        Glide.with(UploadWorkActivity.this).load(coverFile.getPath()).apply(new RequestOptions()
                                .fitCenter())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        iv_cover.setBackground(resource);
                                    }
                                });
                    }
                    break;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Log.d("path",musicPath);
    }



}
