package cn.tianyu_studio.musicpartnerapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.LoadingDialogFragment;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.User;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;

import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

@ContentView(R.layout.activity_complete_info)
public class CompleteInfoActivity extends BaseActivity implements OnDateSetListener {
    public static String headUrlPath = "";
    //user_web 参数
    private String phone;
    private String pwd;
    private String birthday = "";
    private String sex = String.valueOf(User.SEX_MALE);
    private String code;
    @ViewInject(R.id.completeInfo_iv_headImg)
    private ImageView iv_headImg;
    @ViewInject(R.id.completeInfo_tv_male)
    private TextView male;
    @ViewInject(R.id.completeInfo_tv_female)
    private TextView female;
    @ViewInject(R.id.completeInfo_et_birthday)
    private EditText tv_birthday;
    @ViewInject(R.id.completeInfo_et_nick)
    private TextView tv_nick;
    private LoadingDialogFragment loadingDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));

        Intent intent = getIntent();
        phone = intent.getStringExtra("username");
        pwd = intent.getStringExtra("passwd");
        code = intent.getStringExtra("verifyCode");
        // Log.d("phone  com",phone);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        birthday = format1.format(new Date());
    }

    /**
     * 选择头像
     */
    @Event(R.id.completeInfo_iv_headImg)
    private void chooseHeadImg(View view) {
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 注册完成
     */
    @Event(R.id.completeInfo_btn_complete)
    private void completeRegister(View view) {
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(getFragmentManager(), "playLoading");
        String nickname = tv_nick.getText().toString();
        RequestParams params = new RequestParams(URLEnum.Register.getUrl());
        if (!headUrlPath.isEmpty())
            params.addBodyParameter(" headImgFile", new File(headUrlPath), "image/jpg");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("pwd", pwd);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("sex", sex);
        params.addParameter("birthday", birthday);
        params.addBodyParameter("code", code);
        x.http().post(params, new BaseCallback<TResult>() {

            @Override
            public void onSuccess(TResult result) {
                Log.d("result",result.getCode()+result.getMessage());
                Toast.makeText(CompleteInfoActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == 1) {
                    Log.d("dasa", "注册成功");
                    Toast.makeText(CompleteInfoActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CompleteInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFinished() {
                loadingDialogFragment.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("错误！！xutils callback error" + ex.getLocalizedMessage());
                Toast.makeText(CompleteInfoActivity.this, "xutils callback error" + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Event(value = {R.id.completeInfo_tv_back, R.id.completeInfo_tv_cancel})
    private void back(View view) {
        finish();
    }

    /**
     * 生日 点击事件
     */
    @Event(R.id.completeInfo_et_birthday)
    private void date(View view) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1970);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DATE, 1);
        tv_nick.clearFocus();
        TimePickerDialog mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setTitleStringId("日期选择")
                .setMinMillseconds(calendar.getTime().getTime())
                .setMaxMillseconds(new Date().getTime())
                .setThemeColor(Color.parseColor("#666666"))
                .setCallBack(this)
                .build();

        mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month_day");
    }

    /**
     * 性别 点击事件
     */
    @Event(R.id.completeInfo_tv_female)
    private void clickFemale(View view) {
        female.setTextColor(getResources().getColor(R.color.white));
        male.setTextColor(getResources().getColor(R.color.gray_cc));
        sex = String.valueOf(User.SEX_FEMALE);
    }

    @Event(R.id.completeInfo_tv_male)
    private void clickMale(View view) {
        male.setTextColor(getResources().getColor(R.color.white));
        female.setTextColor(getResources().getColor(R.color.gray_cc));
        sex = String.valueOf(User.SEX_MALE);
    }

    /**
     * 申请权限 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_PICK_IMAGE:
                    ArrayList<ImageFile> imageList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    if (imageList.size() <= 0) {
                        return;
                    }
                    String coverPath = imageList.get(0).getPath();
                    headUrlPath = coverPath;
                    x.image().bind(iv_headImg, coverPath);
                    break;
            }
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        //String text = formatters.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(millseconds), ZoneId.of("Asia/Shanghai")));
        //birthday = LocalDate.of(Integer.parseInt(text.substring(0, 4)), Integer.parseInt(text.substring(5, 7)), Integer.parseInt(text.substring(8, 10)));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        birthday = format1.format(new Date(millseconds));
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        String t = format2.format(new Date(millseconds));

        Handler handler = new Handler();
        handler.postDelayed(() -> tv_birthday.setText(t), 300);

    }


}
