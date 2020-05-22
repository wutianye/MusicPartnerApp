package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.http.HttpMethod;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.MainActivity;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

/**
 * Created by admin on 2018/12/5.
 */

public class ConfirmDialogFragment extends BaseDialogFragment {
    TextView tv_cancel;
    TextView tv_confirm;
    Long id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        id = bundle.getLong("workId");
        // 设置背景透明
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.dialog_confirm, container);
        tv_cancel = view.findViewById(R.id.confirm_cancel);
        tv_confirm = view.findViewById(R.id.confirm_ok);
        initButton();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 让宽度和屏幕宽度保持一致
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void initButton() {
        tv_cancel.setOnClickListener(v -> {
            dismiss();
        });

        tv_confirm.setOnClickListener(v -> {

            TRequestParams params = new TRequestParams(URLEnum.Work.getUrl());
            params.addQueryParameter("workId", id);
            x.http().request(HttpMethod.DELETE, params, new BaseCallback<TResult>() {
                @Override
                public void onSuccess(TResult tMessage) {
                    dismiss();
                    Log.d("confirm", "id" + id + "    " + tMessage.getMessage());
                    if (tMessage.getCode().equals(TResultCode.SUCCESS.getCode())) {
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }
            });
        });
    }
}
