package cn.tianyu_studio.musicpartnerapp.download;

/**
 * Created by admin on 2018/8/8.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}