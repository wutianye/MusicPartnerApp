package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_my_info_edit)
public class MyInfoEditActivity extends BaseActivity {

    @ViewInject(R.id.myInfoedit_et_message)
    private TextView message;
    @ViewInject(R.id.myInfoedit_tv_title)
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));

    }

    @Event(R.id.myInfoedit_btn_save)
    private void save(View view) {
        updateMe();
        Intent intent = new Intent();
        intent.putExtra("message", message.getText().toString());
        intent.putExtra("title", title.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Event(R.id.myInfoedit_tv_back)
    private void back(View view) {
        finish();
    }


    public void updateMe() {
        TRequestParams params = new TRequestParams(URLEnum.Users.getUrl());
        switch (title.getText().toString()) {
            case "设置昵称":
                params.addBodyParameter("nickname", message.getText().toString());
                break;
            case "设置签名":
                params.addBodyParameter("signature", message.getText().toString());
                break;
            case "设置标签":
                params.addBodyParameter("tag", message.getText().toString());
                break;
        }
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {

            }
        });
    }


}
