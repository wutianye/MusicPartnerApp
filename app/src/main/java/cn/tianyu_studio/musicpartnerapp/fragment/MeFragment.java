package cn.tianyu_studio.musicpartnerapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;

import org.xutils.http.HttpMethod;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.MyInfoActivity;
import cn.tianyu_studio.musicpartnerapp.activity.SettingActivity;
import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.User;
import cn.tianyu_studio.musicpartnerapp.fragment.me.MyFavWorkFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.me.MyFollowWorkFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.me.MyWorkFragment;
import cn.tianyu_studio.musicpartnerapp.global.TApplication;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

import static android.app.Activity.RESULT_OK;


@ContentView(R.layout.fragment_me)
public class MeFragment extends BaseFragment {


    @ViewInject(R.id.me_tabLayout)
    SlidingTabLayout tabLayout;
    @ViewInject(R.id.me_viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.me_tv_userName)
    TextView username;
    @ViewInject(R.id.me_tv_qianm)
    TextView tv_signature;
    @ViewInject(R.id.me_tv_tag)
    TextView tv_tag;
    @ViewInject(R.id.me_iv_userHeadImg)
    ImageView img_head;
    @ViewInject(R.id.me_tv_fensi)
    TextView tv_fensi;
    @ViewInject(R.id.me_tv_guanzhu)
    TextView tv_guanzhu;

    MyFollowWorkFragment myFollowWorkFragment;
    MyFavWorkFragment myFavWorkFragment;
    MyWorkFragment myWorkFragment;
    private String phone;
    private String nickname;
    private String sex = "";
    private String signature = "";
    private String tag = "";
    private String headUrl = "";

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Fragment> fragments = new ArrayList<>();
        myFollowWorkFragment = new MyFollowWorkFragment();
        fragments.add(myFollowWorkFragment);
        myWorkFragment = new MyWorkFragment();
        fragments.add(myWorkFragment);
        myFavWorkFragment = new MyFavWorkFragment();
        fragments.add(myFavWorkFragment);
        tabLayout.setViewPager(viewPager, getResources().getStringArray(R.array.me_tab_title), this.getActivity(), fragments);
        getDataFromWeb();
        //getDynamicUnRead();
    }

    @Event(R.id.me_tv_setting)
    private void click(View view) {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    @Event(R.id.me_iv_userHeadImg)
    private void click1(View view) {
        Intent intent = new Intent(getActivity(), MyInfoActivity.class);
        intent.putExtra("nickname",nickname);
        intent.putExtra("sex",sex);
        intent.putExtra("signature",signature);
        intent.putExtra("tag",tag);
        intent.putExtra("phone",phone);
        intent.putExtra("headUrl", headUrl);
        startActivityForResult(intent,1);
    }


    public void getDataFromWeb(){
        TRequestParams params = new TRequestParams(URLEnum.Users.getUrl());
        x.http().request(HttpMethod.GET,params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    Log.d("me success",result.getMessage() + result.getData().toString());
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    Long userId = jsonObject.getLong("userId");
                    SharedPreferences sharedPreferences;
                    sharedPreferences = TApplication.getContext().getSharedPreferences(SysConsts.SP_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SPEnum.UserId.getKey(), userId);
                    editor.apply();
                    nickname = jsonObject.getString("nickname");
                    phone = jsonObject.getString("phone");
                    signature = jsonObject.getString("signature");
                    headUrl = jsonObject.getString("headImgUrl");
                    String fans = jsonObject.getString("fansCount");
                    String follow = jsonObject.getString("followCount");
                    Log.d("我改变的头像", headUrl);
                    tag = jsonObject.getString("tag");
                    if (jsonObject.get("sex") != null && Integer.parseInt(jsonObject.get("sex").toString()) == User.SEX_MALE)
                        sex = "男";
                    else
                        sex = "女";
                    ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build(); //圆形图片
                    if (!headUrl.isEmpty())
                        x.image().bind(img_head, headUrl, imageOptions);
                    username.setText(nickname);
                    tv_signature.setText(signature);
                    tv_tag.setText(tag);
                    if (fans != null)
                        tv_fensi.setText(fans);
                    else
                        tv_fensi.setText("0");
                    if (follow != null)
                        tv_guanzhu.setText(follow);
                    else
                        tv_guanzhu.setText("0");
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    username.setText(data.getStringExtra("nickname"));
                    tv_signature.setText(data.getStringExtra("sig"));
                    tv_tag.setText(data.getStringExtra("tag"));
                }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        myFollowWorkFragment.getDynamicUnRead();
        getDataFromWeb();
        refreshWork();
    }

    public void refreshWork() {
        myFollowWorkFragment.refreshWork();
        myFavWorkFragment.refreshWork();
        myWorkFragment.refreshWork();
    }


}
