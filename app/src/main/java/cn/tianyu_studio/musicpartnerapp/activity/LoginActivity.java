package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.LoadingDialogFragment;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.global.TApplication;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    SharedPreferences sharedPreferences;
    @ViewInject(R.id.login_et_username)
    private EditText et_username;
    @ViewInject(R.id.login_et_pwd)
    private EditText et_pwd;
    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));

        sharedPreferences = TApplication.getContext().getSharedPreferences(SysConsts.SP_NAME, Context.MODE_PRIVATE);
        setBackToFinish(true);
        // checkToken();
    }


    @Event(R.id.login_btn_login)
    private void login(View view) {
        String username = et_username.getText().toString();
        String pwd = et_pwd.getText().toString();
        et_username.setError(null);
        et_pwd.setError(null);
        if (TextUtils.isEmpty(username)) {
            et_username.setError(getString(R.string.register_field_required));
            et_username.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            et_pwd.setError(getString(R.string.register_field_required));
            et_pwd.requestFocus();
            return;
        }

        RequestParams params = new RequestParams(URLEnum.Login.getUrl());
        params.addBodyParameter("phone", username);
        params.addBodyParameter("pwd", pwd);
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(getFragmentManager(), "loginLoding");
        x.http().post(params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                // 登录成功
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SPEnum.Token.getKey(), result.getData().toString());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.getLocalizedMessage());
                Toast.makeText(LoginActivity.this, "error: 登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                loadingDialogFragment.dismiss();
            }
        });
    }

    @Event(R.id.login_tv_registerNow)
    private void registerNow(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Event(R.id.login_tv_forgetPwd)
    private void forgetPwd(View view) {
        //startActivity(new Intent(this, MainActivity.class));
    }

    @Event(R.id.login_iv_wechat)
    private void loginByWechat(View view) {

    }

    @Event(R.id.login_iv_qq)
    private void loginByQQ(View view) {

    }

    @Event(R.id.login_iv_weibo)
    private void loginByWeibo(View view) {

    }
}
