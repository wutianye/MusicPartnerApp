package cn.tianyu_studio.musicpartnerapp.loader;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;

import org.xutils.x;

public class XUtilsMediaLoader implements IBoxingMediaLoader {
    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        x.image().bind(img, absPath);
    }

    @Override
    public void displayRaw(@NonNull ImageView img, @NonNull String absPath, int width, int height, IBoxingCallback callback) {
        x.image().bind(img, absPath);
    }
}
