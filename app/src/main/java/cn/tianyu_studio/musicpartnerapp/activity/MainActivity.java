package cn.tianyu_studio.musicpartnerapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;

import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.fragment.HomeFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.MeFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.MessageFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.me.MyFollowWorkFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    //读写权限和拍照权限
    private static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    SharedPreferences sharedPreferences;
    MeFragment meFragment;
    MediaScannerConnection mediaScannerConnection;
    @ViewInject(R.id.main_tab)
    private CommonTabLayout tab;
    private int preSelectTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackToFinish(true);
        sharedPreferences = getSharedPreferences(SysConsts.SP_NAME, MODE_PRIVATE);
        initView();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 防止app崩溃重启后，fragment重叠
        // super.onSaveInstanceState(outState);
    }

    private void initView() {

        // 如果没有权限，则请求权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE);
        }
        initTab();

    }

    private void initTab() {
        String[] titles = getResources().getStringArray(R.array.home_titles);
        int[] selectIcons = {R.drawable.home, R.drawable.news, R.drawable.upload, R.drawable.me};
        int[] unSelectIcons = {R.drawable.home_unselect, R.drawable.news_unselect, R.drawable.upload_unselect, R.drawable.me_unselect};
        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            final String title = titles[i];
            final int selectIcon = selectIcons[i];
            final int unSelectIcon = unSelectIcons[i];
            tabEntities.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return title;
                }

                @Override
                public int getTabSelectedIcon() {
                    return selectIcon;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return unSelectIcon;
                }
            });
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MessageFragment());
        fragments.add(new Fragment());
        meFragment = new MeFragment();
        fragments.add(meFragment);
        tab.setTabData(tabEntities, this, R.id.main_frameLayout, fragments);
        tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 2) {
                    startActivity(new Intent(MainActivity.this, UploadWorkActivity.class));
                    tab.setCurrentTab(preSelectTabIndex);
                    return;
                }
                if (position == 3)
                    meFragment.refreshWork();
                preSelectTabIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        checkToken();
        getUnReadNotifiction();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    LogUtil.w("权限被拒");
                    Toast.makeText(this, "程序可能因为权限不足而运行异常", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageFragment.cancelLunxun();
        MyFollowWorkFragment.cancelLunxun();
    }

    public void getUnReadNotifiction() {
        TRequestParams params = new TRequestParams(URLEnum.NotifictionUnread.getUrl());
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                // loadingDialogFragment.dismiss();
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    String num = result.getData().toString();
                    Log.d("unread", num);
                    if (Integer.parseInt(num) > 0) {
                        MsgView view = tab.getMsgView(1);
                        view.setVisibility(View.VISIBLE);
                        tab.showMsg(1, Integer.parseInt(num));
                    } else {
                        MsgView view = tab.getMsgView(1);
                        view.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    /**
     * 检查token是否存在或过期
     */
    private void checkToken() {
        String token = sharedPreferences.getString(SPEnum.Token.getKey(), "");
        if (StringUtils.isTrimEmpty(token)) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            RequestParams params = new RequestParams(URLEnum.CheckToken.getUrl());
            params.addQueryStringParameter("token", sharedPreferences.getString(SPEnum.Token.getKey(), ""));
            x.http().get(params, new BaseCallback<TResult>() {
                @Override
                public void onSuccess(TResult tMessage) {
                    if (tMessage.getCode().equals(TResultCode.SUCCESS.getCode())) {
                        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                        updateMediaStore(directory);
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("token");
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            });
        }
    }

    private void updateMediaStore(String path) {
        MediaScannerConnection.MediaScannerConnectionClient client = new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onScanCompleted(String path, Uri uri) {  //当client和MediaScaner扫描完成后  进行关闭我们的连接
                // TODO Auto-generated method stub
                mediaScannerConnection.disconnect();
            }

            @Override
            public void onMediaScannerConnected() {   //当client和MediaScanner完成链接后，就开始进行扫描。
                // TODO Auto-generated method stub
                File file = new File(path);
                if (file.isFile())
                    mediaScannerConnection.scanFile(path, null);
                else {
                    File[] files = file.listFiles();
                    if (files == null)
                        return;
                    for (File f : files)
                        mediaScannerConnection.scanFile(f.getAbsolutePath(), null);
                }
                //mediaScannerConnection.scanFile(path+fileName, null);
            }
        };

        mediaScannerConnection = new MediaScannerConnection(MainActivity.this, client);
        mediaScannerConnection.connect();
    }

    @Override
    protected void onResume() {
        Log.d("main的resume","resume");
        super.onResume();
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        updateMediaStore(directory);
    }

}
