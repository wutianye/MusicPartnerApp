package cn.tianyu_studio.musicpartnerapp.fragment.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.PlayActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;



@ContentView(R.layout.fragment_music)
public class MusicFragment extends BaseFragment implements MediaPlayer.OnPreparedListener {


    public String url = "";
    MediaPlayer mediaPlayer;
    String coverUrl = "";
    @ViewInject(R.id.play_mpv)
    co.mobiwise.library.MusicPlayerView mpv;
    boolean isPrepare = false;
    Thread thread;
    PlayActivity playActivity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mpv.setProgress(msg.what);

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playActivity = (PlayActivity) getActivity();
        mpv.setAutoProgress(false);
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
           // mediaPlayer.setDataSource("http://phjlt9qya.bkt.clouddn.com/work%23d7ec986d092348dba091f9047d4bd2fc.mp3");
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Event(R.id.play_mpv)
    private void mppv(View view){


        if (mpv.isRotating()) {
            Log.d("stop", "让我们暂停它");
            mpv.stop();
            mediaPlayer.pause();
        } else {
            if (!isPrepare) {
                Toast.makeText(getActivity(), "网络较慢，音乐尚未加载完毕", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("stop", "让我们开启它");
            mpv.start();
            mediaPlayer.start();

        }

    }

    public void setUrl(String url1,String url2){
        url = url1;
        coverUrl = url2;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("音乐", "准备好");
        mpv.setMax(trans(mediaPlayer.getDuration()));
        isPrepare = true;
        mpv.setProgress(0);
        playActivity.closeLoading();
        mpv.setCoverURL(coverUrl);
        // mpv.start();
        thread = new Thread(new SeekBarThread());
        thread.start();
        if (mpv.isRotating())
            mediaPlayer.start();
    }

    public int trans(int d) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String hh = formatter.format(d);
        String[] strArray = hh.split(":");
        return Integer.parseInt(strArray[0]) * 60 + Integer.parseInt(strArray[1]);
    }

    public void pause() {
        if (mediaPlayer != null) {
            mpv.stop();
            mediaPlayer.pause();
        }
    }

    public void start() {

        if (mediaPlayer != null) {
            mpv.start();
            mediaPlayer.start();
        }
    }

    public void close() {
        if (mediaPlayer != null) {
            isPrepare = false;
            mpv.stop();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (mediaPlayer != null) {
                // 将SeekBar位置设置到当前播放位置
                if (mediaPlayer != null && mpv.isRotating())
                    handler.sendEmptyMessage(trans(mediaPlayer.getCurrentPosition()));
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
