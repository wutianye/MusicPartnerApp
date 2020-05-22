package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.CleanCacheDialogFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.setting_tv_cache)
    private TextView tv_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(R.id.setting_rl_cleanCache)
    private void showCleanCacheDialog(View view) {
        CleanCacheDialogFragment cleanCacheDialogFragment = new CleanCacheDialogFragment();
        cleanCacheDialogFragment.show(getFragmentManager(), "cleanCache");
    }

    @Event(R.id.setting_tv_back)
    private void back(View view) {
        finish();
    }

    public void cleanCache() {
        Toast.makeText(this, "清理完成", Toast.LENGTH_SHORT).show();
        tv_cache.setText("0M");
    }

    @Event(R.id.setting_rl_feedback)
    private void goToFeedback(View view) {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

}
