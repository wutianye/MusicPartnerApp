package cn.tianyu_studio.musicpartnerapp.util;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

/**
 * Created by 天宇 on 2018/1/2.
 */

public abstract class BaseCallback<T> implements Callback.CommonCallback<T> {

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtil.e("错误！！xutils callback error" + ex.getLocalizedMessage());
    }
}
