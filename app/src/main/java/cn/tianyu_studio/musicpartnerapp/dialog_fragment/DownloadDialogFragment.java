package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.download.DownloadService;
import cn.tianyu_studio.musicpartnerapp.download.NetWorkConnect;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;

@ContentView(R.layout.dialog_play_download)
public class DownloadDialogFragment extends BaseDialogFragment {

    public static final String KEY_MAP = "map";
    public static final String KEY_AUTHOR_HEAD_IMG = "authorHeadImg";
    public static final String KEY_AUTHOR_NAME = "authorName";
    public static final String KEY_WORK_NAME = "workName";

    public static final String MV = "MV";
    public static final String MUSIC = "音乐";
    public static final String PIC = "图片";

    @ViewInject(R.id.play_dialog_download_recycle)
    private RecyclerView recyclerView;
    @ViewInject(R.id.play_dialog_download_iv_authorHeadImg)
    private ImageView iv_authorHeadImg;
    @ViewInject(R.id.play_dialog_download_tv_workName)
    private TextView tv_workName;
    @ViewInject(R.id.play_dialog_download_tv_authorName)
    private TextView tv_authorName;
    private int type;
    private BaseQuickAdapter<DownloadItem, BaseViewHolder> adapter;
    private List<DownloadItem> selectItems = new ArrayList<>();
    private DownloadService.DownloadBinder downloadBinder;
    private String downUrl, downName;
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        HashMap<String, String> map = (HashMap<String, String>) bundle.getSerializable(KEY_MAP);
        downName = bundle.getString("name");
        x.image().bind(iv_authorHeadImg, bundle.getString(KEY_AUTHOR_HEAD_IMG));
        tv_workName.setText(bundle.getString(KEY_WORK_NAME));
        tv_authorName.setText(bundle.getString(KEY_AUTHOR_NAME));

        Intent intent = new Intent(getActivity(),DownloadService.class);
        getActivity().startService(intent); // 启动服务
        boolean a = getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE); // 绑定服务
        initAdapter();
        initRecyclerView(map);
    }

    private void initRecyclerView(HashMap<String, String> map) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        // 从bundle中初始化adapter数据


        for (Map.Entry<String, String> entry : map.entrySet()) {
            DownloadItem downloadItem = new DownloadItem();
            downloadItem.name = entry.getKey();
            if (!entry.getValue().isEmpty())
                downUrl = entry.getValue();
            downloadItem.downloadUrl = entry.getValue();
            switch (entry.getKey()) {
                case MV:
                    downloadItem.imgResId = R.drawable.mv;
                    type = 0;
                    break;
                case MUSIC:
                    type = 1;
                    downloadItem.imgResId = R.drawable.music;
                    break;
                case PIC:
                    type = 2;
                    downloadItem.imgResId = R.drawable.pic;
                    break;
            }
            adapter.addData(downloadItem);
        }
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<DownloadItem, BaseViewHolder>(R.layout.item_play_dialog_download, null) {
            @Override
            protected void convert(BaseViewHolder helper, DownloadItem item) {
                helper.setImageResource(R.id.item_play_dialog_download_iv, item.imgResId)
                        .setText(R.id.item_play_dialog_download_tv, item.name);

                CheckBox checkBox = helper.getView(R.id.item_play_dialog_download_cb);
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked)
                        selectItems.add(item);
                    else
                        selectItems.remove(item);
                });
            }
        };
    }

    @Event(R.id.play_dialog_download_tv_startDownload)
    private void startDownload(View view) {
        //Toast.makeText(getActivity(), "开始下载：" + selectItems.size(), Toast.LENGTH_SHORT).show();
        //采用给定url测试  http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else if (!NetWorkConnect.isNetworkConnected(getActivity()))
            Toast.makeText(getActivity(), "网络出现故障，下载失败", Toast.LENGTH_SHORT).show();

        else{
            Log.d("start","dow");
            if (selectItems.isEmpty())
                Toast.makeText(getActivity(), "请选择要下载的内容 ", Toast.LENGTH_SHORT).show();
            else
                downloadBinder.startDownload(downUrl, type, downName);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "暂无sd卡读写权限，下载失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("我结束了", "ddf");
        getActivity().unbindService(connection);
    }


    private class DownloadItem {
        int imgResId;
        String name;
        String downloadUrl;
    }
}
