package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.view.View;

import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

/**
 * Created by admin on 2018/11/15.
 */
@ContentView(R.layout.dialog_sex_choose)
public class SexChooseDialogFragment extends BaseDialogFragment {

    public static String sex = "";
    private SexChoose sexListener;
    @Event(R.id.dialog_sex_female)
    private void Female(View view){
        sex = "女";
        updateSex("1");
        dismiss();
    }

    @Event(R.id.dialog_sex_male)
    private void Male(View view){
        sex = "男";
        updateSex("0");
        dismiss();
    }

    public void attachSex(SexChoose sexChoose){
        this.sexListener = sexChoose;
    }

    @Override
    public void dismiss(){
        super.dismiss();
        sexListener.setSex(sex);
    }

    private void updateSex(String sex) {
        TRequestParams params = new TRequestParams(URLEnum.Users.getUrl());
        params.addParameter("sex", sex);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {

            }
        });
    }

    public interface SexChoose {
        void setSex(String t);
    }


}
