package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.LoginActivity;
import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.dialog_personal_info_logout)
public class LogoutDialogFragment extends BaseDialogFragment {

    @Event(R.id.dialog_personalInfo_logout_btn_logout)
    private void logout(View view) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SysConsts.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SPEnum.Token.getKey());
        editor.apply();
        logOutFromWeb();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    @Event(R.id.dialog_personalInfo_logout_btn_cancel)
    private void cancel(View view) {
        dismiss();
    }

    private void logOutFromWeb(){
        TRequestParams params = new TRequestParams(URLEnum.LogOut.getUrl());
        x.http().request(HttpMethod.POST,params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
