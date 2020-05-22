package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    public int T = 30; //倒计时时长
    @ViewInject(R.id.reg_et_username)
    TextView username;
    @ViewInject(R.id.reg_et_pwd)
    TextView pwd;
    @ViewInject(R.id.reg_et_verifyCode)
    TextView verifyCode;
    @ViewInject(R.id.reg_btn_getVerifyCode)
    Button timeBtn;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));

    }

    @Event(R.id.reg_btn_next)
    private void nextStep(View view) {
        String name;
        String passwd;
        String code;
        name = username.getText().toString();
        passwd = pwd.getText().toString();
        code = verifyCode.getText().toString();

        if (name.isEmpty() || passwd.isEmpty() || code.isEmpty())
            Toast.makeText(this, "请填写所有信息", Toast.LENGTH_LONG).show();

        else if (passwd.length() < 6)
            Toast.makeText(this, "密码需要至少6位", Toast.LENGTH_LONG).show();
        else {
            Intent intent = new Intent(this, CompleteInfoActivity.class);
            intent.putExtra("username", name);
            intent.putExtra("passwd", passwd);
            intent.putExtra("verifyCode", code);
            startActivity(intent);
        }
    }

    @Event(R.id.reg_btn_getVerifyCode)
    private void code(View view) {
        String phone = username.getText().toString();
        if (phone.isEmpty() || phone.length() < 11) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams(URLEnum.Code.getUrl());
        params.addBodyParameter("phone", username.getText().toString());
        x.http().request(HttpMethod.POST, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    new Thread(new MyCountDownTimer()).start();//开始执行
                }
            }
        });
    }

    @Event(R.id.reg_tv_cancel)
    private void cancel(View view) {
        finish();
    }

    /**
     * 自定义倒计时类，实现Runnable接口
     */
    class MyCountDownTimer implements Runnable {

        @Override
        public void run() {

            //倒计时开始，循环
            while (T > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeBtn.setClickable(false);
                        timeBtn.setText(T + "秒后重新开始");
                    }
                });
                try {
                    Thread.sleep(1000); //强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                T--;
            }

            //倒计时结束，也就是循环结束
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    timeBtn.setClickable(true);
                    timeBtn.setText("获取验证码");
                }
            });
            T = 30; //最后再恢复倒计时时长
        }
    }


}
