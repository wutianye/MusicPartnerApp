package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.ChangeHeadImgDialogFragment;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.LoadingDialogFragment;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.LogoutDialogFragment;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.SexChooseDialogFragment;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_my_info)
public class MyInfoActivity extends BaseActivity {

    String headUrl = "";
    LoadingDialogFragment loadingDialogFragment;
    @ViewInject(R.id.myInfo_iv_headImg)
    private ImageView iv_headImg;
    @ViewInject(R.id.myInfo_tv_nickname)
    private TextView nick;
    @ViewInject(R.id.myInfo_tv_gender)
    private TextView sex;
    @ViewInject(R.id.myInfo_tv_personalitySignature)
    private TextView sig;
    @ViewInject(R.id.myInfo_tv_label)
    private TextView label;
    @ViewInject(R.id.myInfo_tv_bindingPhone)
    private TextView phone;
    private UploadManager uploadManager;
    private String qiniuToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        nick.setText(intent.getStringExtra("nickname"));
        phone.setText(intent.getStringExtra("phone"));
        sex.setText(intent.getStringExtra("sex"));
        sig.setText(intent.getStringExtra("signature"));
        label.setText(intent.getStringExtra("tag"));
        x.image().bind(iv_headImg, intent.getStringExtra("headUrl"));
    }

    public void setHeadImgByLocalImage(String imagePath) {
        Log.d("我来改头像", "change");
        uploadManager = new UploadManager();
        getQiniuToken(imagePath);
        x.image().bind(iv_headImg, imagePath);
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.setHintMsg("上传头像中...");
        loadingDialogFragment.show(getFragmentManager(), "headUrl");
    }

    @Event(R.id.myInfo_rl_headImgSetting)
    private void openChangeImgDialog(View view) {
        ChangeHeadImgDialogFragment changeHeadImgDialogFragment = new ChangeHeadImgDialogFragment();
        changeHeadImgDialogFragment.show(getFragmentManager(), "changeHeadImg");
    }

    @Event(R.id.myInfo_btn_logout)
    private void openLogout(View view) {
        LogoutDialogFragment logoutDialogFragment = new LogoutDialogFragment();
        logoutDialogFragment.show(getFragmentManager(), "logout");
    }

    @Event(R.id.myInfo_tv_back)
    private void back(View view) {
        //startActivity(new Intent(this,MainActivity.class));
        Intent intent = new Intent();
        intent.putExtra("nickname", nick.getText().toString());
        intent.putExtra("sig", sig.getText().toString());
        intent.putExtra("tag", label.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Event(value = {R.id.myInfo_rl_signature,R.id.myInfo_rl_tag,R.id.myInfo_rl_nickname})
    private void edit(View view){
        String title = "";
        switch (view.getId()){
            case R.id.myInfo_rl_nickname: title="设置昵称"; break;
            case R.id.myInfo_rl_signature: title="设置签名";break;
            case R.id.myInfo_rl_tag: title="设置标签";break;
        }
        Intent intent = new Intent(this,MyInfoEditActivity.class);
        intent.putExtra("title",title);
        startActivityForResult(intent,1);
    }

    @Event(R.id.myInfo_rl_gender)
    private void setSex(View view){
        SexChooseDialogFragment dialogFragment = new SexChooseDialogFragment();
        dialogFragment.attachSex(t -> sex.setText(t));
        dialogFragment.show(getFragmentManager(),"sex_choose");
    }


   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String title = data.getStringExtra("title");
                    String message = data.getStringExtra("message");
                    switch (title){
                        case "设置昵称": nick.setText(message);break;
                        case "设置签名": sig.setText(message);break;
                        case "设置标签": label.setText(message);break;
                    }
                    Toast.makeText(MyInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
        }
   }

    private void headImgUp(String path) {
        uploadManager.put(path, null, qiniuToken, new UpCompletionHandler() {
            public void complete(String key, ResponseInfo rinfo, JSONObject response) {
                try {
                    headUrl = SysConsts.QINIU_RESOURCES_PREFIX + java.net.URLEncoder.encode(response.getString("key"), "UTF-8");
                    updateHead();
                    Log.d("头像改变", headUrl);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new UploadOptions(null, "test-type", true, null, null));
    }

    private void getQiniuToken(String path) {
        TRequestParams params = new TRequestParams(URLEnum.Qiniu.getUrl());
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Log.d("success", "成功");
                // 登录成功
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    qiniuToken = result.getData().toString();
                    headImgUp(path);
                }
            }
        });
    }

    private void updateHead() {
        TRequestParams params = new TRequestParams(URLEnum.Users.getUrl());
        params.addBodyParameter("headImgUrl", headUrl);
        Log.d("瞧瞧我的改变", headUrl);
        params.addBodyParameter("nickname", nick.getText().toString());
        params.addBodyParameter("signature", sig.getText().toString());
        params.addBodyParameter("tag", label.getText().toString());
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                loadingDialogFragment.dismiss();
                if (result.getCode().equals(TResultCode.SUCCESS.getCode()))
                    Toast.makeText(MyInfoActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MyInfoActivity.this, "头像更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
