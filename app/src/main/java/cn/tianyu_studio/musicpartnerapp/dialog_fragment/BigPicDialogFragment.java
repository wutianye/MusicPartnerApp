package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.download.DownloadService;

public class BigPicDialogFragment extends DialogFragment {

    ImageView img;
    ImageView down;
    RelativeLayout relativeLayout;
    String u = "";
    private int t;
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("binding", "bind");
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

    };

    public void setUrl(String url, int type) {
        this.u = url;
        this.t = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置背景black
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.black);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        Intent intent = new Intent(getActivity(), DownloadService.class);
        getActivity().startService(intent); // 启动服务
        boolean a = getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE); // 绑定服务

        View view = inflater.inflate(R.layout.dialog_bigpic, container);
        img = view.findViewById(R.id.big_pic_src);
        down = view.findViewById(R.id.big_pic_download);
        relativeLayout = view.findViewById(R.id.big_pic_rl);
        down.setOnClickListener(v -> {
            downloadBinder.startDownload(u, t, "");
        });
        relativeLayout.setOnClickListener(v -> {
            dismiss();
        });
        Glide.with(getActivity()).load(u).into(img);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 让宽度和屏幕宽度保持一致
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("我结束了", "ddf");
        getActivity().unbindService(connection);
    }
}
