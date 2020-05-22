package cn.tianyu_studio.musicpartnerapp.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.bilibili.boxing.BoxingMediaLoader;
import com.blankj.utilcode.util.Utils;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.xutils.x;

import java.util.ArrayList;

import cn.tianyu_studio.musicpartnerapp.loader.XUtilsMediaLoader;

/**
 * 全局Application
 * Created by 汪继友 on 2017/7/27.
 */

public class TApplication extends Application {
    private static ArrayList<Activity> activities;
    private static Context context;

    public static void addActivity(Activity activity) {
        activities.add(activity);

    }

    public static void finishApp() {
        try {
            for (Activity activity : activities) {
                if (activity != null)
                    activity.finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.exit(0);
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        activities = new ArrayList<>();
        //init xutils3
        x.Ext.init(this);
        x.Ext.setDebug(true);

        // init UtilCode
        Utils.init(this);

        // init bilibili MediaLoger
        BoxingMediaLoader.getInstance().init(new XUtilsMediaLoader());

        // 解决android 7.0拍照问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    Log.d("FFmpeg", "fail");
                }

                @Override
                public void onSuccess() {
                    Log.d("FFmpeg", "succcess");
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }
}