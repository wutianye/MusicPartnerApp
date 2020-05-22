package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

/**
 * Created by admin on 2018/11/1.
 */

@ContentView(R.layout.dialog_reply)
public class ReplyDialogFragment extends BaseDialogFragment {

    InputMethodManager inputManager;
    long WorkId;
    long select_id;
    @ViewInject(R.id.dialog_reply_ed_comment)
    private TextView tv_reply;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        WorkId = bundle.getLong("workId");
        select_id = bundle.getLong("select_id");
        tv_reply.setFocusable(true);
        tv_reply.requestFocus();
     //调用系统输入法

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                inputManager = (InputMethodManager) tv_reply.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(tv_reply, 0);
            }
        },200);



    }

    @Event(R.id.dialog_reply_publish_comment)
    private void publish(View view){

        String content = tv_reply.getText().toString();
        inputManager.hideSoftInputFromWindow(tv_reply.getWindowToken(), 0);
        dismiss();
        ReplyComment(content);

    }

    @Override
    public void onStart(){
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp= window.getAttributes();
        lp.dimAmount =0f;
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }

    private void ReplyComment(String content) {
        TRequestParams params = new TRequestParams(URLEnum.Comment.getUrl());
        params.addBodyParameter("content", content);
        params.addParameter("workId", WorkId);
        params.addParameter("replyCommentId", select_id);

        x.http().request(HttpMethod.POST, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    Toast.makeText(getActivity(), "回复成功", Toast.LENGTH_SHORT).show();
                    setRead(WorkId);
                }
            }
        });
    }

    private void setRead(long id) {
        TRequestParams params = new TRequestParams(URLEnum.Dynamic.getUrl() + "/" + id);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode() == TResultCode.SUCCESS.getCode()) {
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());

                }
            }
        });
    }

}
