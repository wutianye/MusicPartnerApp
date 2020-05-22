package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.SettingActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;

@ContentView(R.layout.dialog_setting_clean_cache)
public class CleanCacheDialogFragment extends BaseDialogFragment {

    @Event(R.id.dialog_setting_cleanCache_btn_yes)
    private void cleanCache(View view) {
        ((SettingActivity) getActivity()).cleanCache();
        this.dismiss();
    }

    @Event(R.id.dialog_setting_cleanCache_btn_no)
    private void dismiss(View view) {
        this.dismiss();
    }
}
