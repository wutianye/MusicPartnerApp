package cn.tianyu_studio.musicpartnerapp.activity;

import android.os.Bundle;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(R.id.search_tv_close)
    private void close(View view) {
        finish();
    }
}
