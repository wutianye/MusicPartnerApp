package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.tianyu_studio.musicpartnerapp.R;

/**
 * Created by admin on 2018/11/27.
 */

public class LoadingDialogFragment extends DialogFragment {

    /**
     * 加载框提示信息 设置默认
     */
    private String hintMsg = "正在加载...";

    /**
     * 设置加载框提示信息
     */
    public void setHintMsg(String hintMsg) {
        if (!hintMsg.isEmpty()) {
            this.hintMsg = hintMsg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置背景透明
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View loadingView = inflater.inflate(R.layout.dialog_loading, container);
        TextView hintTextView = loadingView.findViewById(R.id.tv_loading_dialog_hint);
        hintTextView.setText(hintMsg);
        return loadingView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 让宽度和屏幕宽度保持一致
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}