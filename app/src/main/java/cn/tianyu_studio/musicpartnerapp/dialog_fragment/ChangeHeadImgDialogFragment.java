package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.IntentUtils;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.io.File;
import java.util.ArrayList;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.MyInfoActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;

@ContentView(R.layout.dialog_personal_info_change_head_img)
public class ChangeHeadImgDialogFragment extends BaseDialogFragment {
    private static final int REQUEST_CODE_TAKE_PHOTO = 0;
    // 拍照的图片存储路径
    String photoStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp-photo.jpg";
    private MyInfoActivity myInfoActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myInfoActivity = (MyInfoActivity) getActivity();
    }

    @Event(R.id.dialog_personalInfo_changeHeadImg_btn_takePhoto)
    private void takePhoto(View view) {
        Uri photoUri = Uri.fromFile(new File(photoStoragePath));
        startActivityForResult(IntentUtils.getCaptureIntent(photoUri), REQUEST_CODE_TAKE_PHOTO);

    }

    @Event(R.id.dialog_personalInfo_changeHeadImg_btn_album)
    private void openAlbum(View view) {
        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

    }

    @Event(R.id.dialog_personalInfo_changeHeadImg_btn_cancel)
    private void cancel(View view) {
        dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO:
                myInfoActivity.setHeadImgByLocalImage(photoStoragePath);
                dismiss();
                break;
            case Constant.REQUEST_CODE_PICK_IMAGE:
                Log.d("我来改头像", "change");
                ArrayList<ImageFile> imageList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                if (imageList.size() > 0)
                    myInfoActivity.setHeadImgByLocalImage(imageList.get(0).getPath());
                dismiss();
                break;
        }
    }
}
