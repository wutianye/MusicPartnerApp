package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.tianyu_studio.musicpartnerapp.R;

/**
 * Created by admin on 2018/11/18.
 */

public class ProgressDialogFragment extends DialogFragment {

    Button btn;
    Cancel c;
    private View view;
    private TextView textView;
    private ProgressBar progressBar;
    private TextView tv_title;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_progress, container, false);
        // 让弹出框在底部
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity =  Gravity.CENTER;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        // 这句必须加，否则会有间距
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        initView();
        return view;
    }

    private void initView(){
        tv_title = view.findViewById(R.id.up11_progress_tv_title);
       textView = view.findViewById(R.id.up_progress_tv_n);
       progressBar = view.findViewById(R.id.up_progress_pb);
        btn = view.findViewById(R.id.up_progress_btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.fun(true);
                dismiss();
            }
        });
    }

    public  void setProgress(int progress){
        progressBar.setProgress(progress);
        textView.setText(""+progress+"%");
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public void setMax(int max){
        progressBar.setMax(max);
    }

    public void attach(Cancel cancel){
        this.c = cancel;
    }

    public interface Cancel {
        void fun(Boolean t);
    }


}
