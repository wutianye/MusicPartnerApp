package cn.tianyu_studio.musicpartnerapp.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.global.TApplication;

public class BaseActivity extends AppCompatActivity {

    private boolean backToFinish = false;

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        TApplication.addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void setBackToFinish(boolean backToFinish) {
        this.backToFinish = backToFinish;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backToFinish) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                TApplication.finishApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
