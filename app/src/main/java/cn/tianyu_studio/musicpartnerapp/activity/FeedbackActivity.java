package cn.tianyu_studio.musicpartnerapp.activity;

import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.http.HttpMethod;
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
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity {

    @ViewInject(R.id.feedback_tv_confirm)
    private TextView tv_confirm;

    @ViewInject(R.id.feedback_et)
    private EditText et_content;
    /**
     * 监听EditText的输入事件，当EditText的内容大于0时，将右上角的“确认”按钮，变为可点击，颜色更改为黑色
     */
    @Event(value = R.id.feedback_et, type = TextWatcher.class, method = "onTextChanged", setter = "addTextChangedListener")
    private void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            tv_confirm.setClickable(true);
            tv_confirm.setTextColor(getResources().getColor(R.color.black));
        } else {
            tv_confirm.setClickable(false);
            tv_confirm.setTextColor(getResources().getColor(R.color.gray_cc));
        }
    }

    @Event(R.id.feedback_tv_confirm)
    private void submitFeedback(View view) {
        TRequestParams tRequestParams = new TRequestParams(URLEnum.Feed.getUrl());
        tRequestParams.addBodyParameter("content", et_content.getText().toString());
        x.http().request(HttpMethod.GET, tRequestParams, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    // 解决mini UI 在toast前面 显示app名称的问题
                    Toast toast = Toast.makeText(FeedbackActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("提交成功！感谢您的反馈！");
                    toast.show();
                }
            }
        });
    }

    @Event(R.id.feedback_tv_back)
    private void back(View view) {
        finish();
    }
}
