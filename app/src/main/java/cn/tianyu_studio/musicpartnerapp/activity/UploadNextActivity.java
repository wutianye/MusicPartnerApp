package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.zhouwei.library.CustomPopWindow;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.compress.VideoCompress;
import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.ProgressDialogFragment;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.Tag;
import cn.tianyu_studio.musicpartnerapp.entity.WorkTag;
import cn.tianyu_studio.musicpartnerapp.global.TApplication;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;


@ContentView(R.layout.activity_upload_next)
public class UploadNextActivity extends BaseActivity {
    public volatile boolean isCancelled = false;
    CustomPopWindow headerPopWindow;
    RelativeLayout filterLayout;
    @ViewInject(R.id.uploadNext_fenlei)
    LinearLayout linearLayout;
    @ViewInject(R.id.uploadNext_tv_allchoose)
    TextView choose_tv;
    @ViewInject(R.id.uploadNext_radio)
    RadioGroup radioGroup;
    @ViewInject(R.id.uploadNext_name)
    EditText ed;
    @ViewInject(R.id.uploadNext_et_content)
    EditText ed_content;
    int type = 2;
    List<Tag> tagList;
    ArrayList<String> list;
    ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
    private int[] select_btn;
    private int select_num = 0;
    private String select_content = "";
    private String cover;
    private String audioPath = "";
    //    private String yasuopath = "";
    private String videoPath = "";
    //上传四人组
    private String coverUrl = "";
    private String audioUrl = "";
    private String lyricUrl = "";
    private String videoUrl = "";
    private UploadManager uploadManager;
    private String qiniuToken;
    private String[] ly;
    private int count = 0;
    private myHandler handler = new myHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        cover = i.getStringExtra("cover");
        list = i.getStringArrayListExtra("photo");
        audioPath = i.getStringExtra("music");
        videoPath = i.getStringExtra("video");
        Log.d("path查看", "audio" + audioPath + "video" + videoPath);
//        SharedPreferences sharedPreferences;
//        sharedPreferences = TApplication.getContext().getSharedPreferences(SysConsts.SP_NAME, Context.MODE_PRIVATE);
//        userId = sharedPreferences.getLong(SPEnum.UserId.getKey(), 0L);
        getQiniuToken();
        initTags();
        monitoringRadioGrop();
    }

    @Event(R.id.uploadNext_fenlei)
    private void fenlei(View view) {
        headerPopWindow.showAsDropDown(linearLayout);
    }

    @Event(R.id.uploadNext_tv_close)
    private void exit(View view) {
        isCancelled = true;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Event(R.id.uploadNext_choose_fengmian)
    private void exit2(View view) {
        finish();
    }


    @Event(R.id.uploadNext_publish)
    private void publish(View view) {
        qiniuUp();
    }

    private void initTags() {
        TRequestParams params = new TRequestParams(URLEnum.Tag.getUrl());
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    Log.d("我拿到tag了", result.getData().toString());
                    tagList = JSON.parseArray(result.getData().toString(), Tag.class);
                    select_btn = new int[tagList.size()];
                    for (int m = 0; m < select_btn.length; m++)
                        select_btn[m] = 0;
                    initFilterLayout();
                    initHeaderPopWindow();
                }
            }
        });
    }


    private BaseQuickAdapter<Tag, BaseViewHolder> initFilterAdapter() {

        return new BaseQuickAdapter<Tag, BaseViewHolder>(R.layout.item_home_filter, tagList) {
            @Override
            protected void convert(BaseViewHolder helper, Tag item) {
                helper.setText(R.id.item_home_head_filter_btn, item.getName());
                Button btn = helper.getView(R.id.item_home_head_filter_btn);
                helper.getView(R.id.item_home_head_filter_btn).setOnClickListener(v -> {

                    if (select_btn[helper.getAdapterPosition()] == 0) {
                        if (select_num < 3) {
                            select_num++;
                            btn.setBackgroundResource(R.drawable.mybutton_selected);
                            //btn.setTextColor(getResources().getColor(R.color.app_color));
                            select_btn[helper.getAdapterPosition()] = 1;
                            select_content += item.getName() + "、";
                            choose_tv.setText(select_content);
                        } else
                            Toast.makeText(UploadNextActivity.this, "最多选择三项", Toast.LENGTH_SHORT).show();
                    } else {
                        select_num--;
                        select_content = "";
                        btn.setBackgroundResource(R.drawable.home_filter_item);
                        //btn.setTextColor(Color.parseColor("#000000"));
                        select_btn[helper.getAdapterPosition()] = 0;
                        for (int i1 = 0; i1 < 16; i1++) {
                            if (select_btn[i1] == 1)
                                select_content += tagList.get(i1).getName() + "、";
                        }
                        choose_tv.setText(select_content);
                    }
                });
            }
        };
    }

    private void initFilterLayout() {
        filterLayout = new RelativeLayout(UploadNextActivity.this);
        filterLayout.setBackgroundColor(getResources().getColor(R.color.white));
        filterLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DensityUtil.dip2px(5), 0, DensityUtil.dip2px(20));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        RecyclerView recycle_filter = new RecyclerView(UploadNextActivity.this);
        recycle_filter.setLayoutManager(new GridLayoutManager(UploadNextActivity.this, 4));    //网格布局，一行显示4个item
        recycle_filter.setAdapter(initFilterAdapter());
        recycle_filter.setLayoutParams(params);

        recycle_filter.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                //headerPopWindow.dissmiss();

            }
        });

        filterLayout.addView(recycle_filter);
    }

    private void initHeaderPopWindow() {
        headerPopWindow = new CustomPopWindow.PopupWindowBuilder(UploadNextActivity.this)
                .setView(filterLayout)    //显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)  //是否PopupWindow 以外触摸dissmiss
                .create();                    //创建PopupWindow
    }

    private void monitoringRadioGrop() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.uploadNext_radio_geren:
                            linearLayout.setVisibility(View.VISIBLE);
                            type = 0;
                            break;
                        case R.id.uploadNext_radio_tuandui:
                            linearLayout.setVisibility(View.GONE);
                            type = 1;
                            break;
                    }
                }
        );
    }

    private void getQiniuToken() {
        TRequestParams params = new TRequestParams(URLEnum.Qiniu.getUrl());
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Log.d("success", "成功");
                // 登录成功
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    qiniuToken = result.getData().toString();
                }
            }
        });
    }

    private void qiniuUp() {

        String name = ed.getText().toString();
        String introduction = ed_content.getText().toString();

        if (type == 2) {
            Toast.makeText(UploadNextActivity.this, "请选择你的作品类型为个人或团队", Toast.LENGTH_LONG).show();
            return;
        }

        if (type == 0 && select_content.isEmpty()) {
            Toast.makeText(UploadNextActivity.this, "请完整填写作品标签", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() && introduction.isEmpty()) {
            Toast.makeText(UploadNextActivity.this, "请完整填写基本内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (audioPath.isEmpty() && videoPath.isEmpty() && list.isEmpty()) {
            Toast.makeText(UploadNextActivity.this, "您并没有选择要上传的文件", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UploadNextActivity.this, UploadWorkActivity.class);
            startActivity(intent);
            finish();
        } else {
            uploadManager = new UploadManager();
            dialogFragment.attach(t -> {
                Log.e("cancel", "cancel");
                isCancelled = t;
            });
            dialogFragment.show(getFragmentManager(), "progress");
            coverUp();
        }

    }

    private void coverUp() {
        uploadManager.put(cover, null, qiniuToken, (key, rinfo, response) -> {
            if (!isCancelled) {
                try {
                    Log.d("response", response.toString());
                    coverUrl = SysConsts.QINIU_RESOURCES_PREFIX + java.net.URLEncoder.encode(response.getString("key"), "UTF-8");
                    if (!videoPath.isEmpty()) {
                        // yasuo(videoPath,yasuopath);
                        //FFmpeg();
                        //CompressC();
                        EpMediaTask();
                        //videoUp();

                    } else if (!audioPath.isEmpty())
                        musicUp();
                    else
                        photoUp();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(UploadNextActivity.this, "取消上传", Toast.LENGTH_SHORT).show();
            }
        }, new UploadOptions(null, "test-type", true, (key, percent) -> {
            int progress = (int) (percent * 100);
            dialogFragment.setTitle("正在上传封面");
            Log.d("qiniu", progress + "");
            dialogFragment.setProgress(progress);
        }, () -> isCancelled));
    }

    //TODO 所有的key使用null
    //TODO 后续优化时，所有文件用原来的文件名（图片、视频、音频）
    private void videoUp(String compressPath) {
        uploadManager.put(compressPath, null, qiniuToken, (key, info, response) -> {
            dialogFragment.dismiss();
            if (!isCancelled) {
                Toast.makeText(UploadNextActivity.this, "上传完成，正在进行后台处理，暂勿离开当前页面", Toast.LENGTH_SHORT).show();
                try {
                    videoUrl = SysConsts.QINIU_RESOURCES_PREFIX + java.net.URLEncoder.encode(response.getString("key"), "UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                workUp();
            } else {
                Toast.makeText(UploadNextActivity.this, "取消上传", Toast.LENGTH_SHORT).show();
            }
        }, new UploadOptions(null, "test-type", true, (key, percent) -> {
            int progress = (int) (percent * 100);
            dialogFragment.setTitle("正在上传视频");
            Log.d("qiniu", progress + "");
            dialogFragment.setProgress(progress);
        }, () -> isCancelled));
    }

    private void musicUp() {
        uploadManager.put(audioPath, null, qiniuToken, (key, info, response) -> {
            if (!isCancelled) {
                dialogFragment.dismiss();
                try {
                    audioUrl = SysConsts.QINIU_RESOURCES_PREFIX + java.net.URLEncoder.encode(response.getString("key"), "UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //photoUp();
                workUp();
            } else {
                Toast.makeText(UploadNextActivity.this, "取消上传", Toast.LENGTH_SHORT).show();
            }
        }, new UploadOptions(null, "test-type", true, (key, percent) -> {
            dialogFragment.setTitle("正在上传音乐");
            int progress = (int) (percent * 100);
            Log.d("qiniu", progress + "");
            dialogFragment.setProgress(progress);
        }, () -> isCancelled));
    }

    private void photoUp() {
        if (!list.isEmpty()) {
            ly = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                //  dialogFragment.setProgress(0);
                final int finalI = i;
                Log.d("我是图片", list.get(finalI));
                uploadManager.put(list.get(finalI), null, qiniuToken, (key, info, response) -> {
                    if (isCancelled) {
                        Toast.makeText(UploadNextActivity.this, "取消上传", Toast.LENGTH_SHORT).show();
                    } else {
                        count++;
                        try {
                            Log.d("任务完成", "" + finalI + "号");
                            ly[finalI] = SysConsts.QINIU_RESOURCES_PREFIX + java.net.URLEncoder.encode(response.getString("key"), "UTF-8");
                            if (count == list.size()) {
                                dialogFragment.dismiss();
                                Toast.makeText(UploadNextActivity.this, "上传完成，正在进行后台处理，暂勿离开当前页面", Toast.LENGTH_SHORT).show();
                                Log.d("我完成的图片上传任务有", count + "个");
                                workUp();
                            } else
                                dialogFragment.setProgress(0);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new UploadOptions(null, "test-type", true, (key, percent) -> {
                    dialogFragment.setTitle("正在上传歌词图片" + finalI);
                    int progress = (int) (percent * 100);
                    Log.d("qiniu", progress + "");
                    dialogFragment.setProgress(progress);
                }, () -> isCancelled));

            }
        } else {
            dialogFragment.dismiss();
            Toast.makeText(UploadNextActivity.this, "上传完成，正在进行后台处理，暂勿离开当前页面", Toast.LENGTH_SHORT).show();
            workUp();
        }

    }

    private void workUp() {

        String name = ed.getText().toString();
        String introduction = ed_content.getText().toString();
        List<WorkTag> workTagList = new ArrayList<>();

        for (int k = 0; k < 16; k++) {
            if (select_btn[k] == 1) {
                WorkTag tag = new WorkTag();
                tag.setTagId(tagList.get(k).getTagId());
                tag.setTagName(tagList.get(k).getName());
                //Log.d("tagname",tagList.get(k).getName()+tagList.get(k).getTagId());
                workTagList.add(tag);
            }
        }

        if (!list.isEmpty()) {
            for (int j = 0; j < ly.length; j++) {

                if (j != 0)
                    lyricUrl += "," + ly[j];
                else
                    lyricUrl += ly[j];
            }
        }
        Log.d("lyurl", lyricUrl);
        TRequestParams params = new TRequestParams(URLEnum.Work.getUrl());
        params.addBodyParameter("name", name);
        params.addBodyParameter("introduction", introduction);
        params.addParameter("type", type);
        params.addBodyParameter("coverUrl", coverUrl);
        params.addBodyParameter("audioUrl", audioUrl);
        params.addBodyParameter("videoUrl", videoUrl);
        if (!list.isEmpty())
            params.addBodyParameter("lyricUrl", lyricUrl);
        String json = JSON.toJSONString(workTagList);
        params.addParameter("workTags", json);
        Log.d("workjson", json);
        x.http().request(HttpMethod.POST, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {

                // 登录成功
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    Log.d("success", "上传成功");
                    Toast.makeText(UploadNextActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadNextActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UploadNextActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void CompressC() {
        File file = new File(videoPath);
        if(file.length()>0)
        {
            videoUp(videoPath);
            return;
        }
        int index = videoPath.lastIndexOf(".");
        Log.d("last path", "" + videoPath.substring(index + 1));
        String fileType = videoPath.substring(index + 1);
        String fileName = file.getName();
        String appCacheDir = getExternalFilesDir(android.os.Environment.DIRECTORY_MOVIES).getAbsolutePath();
        String destPath = appCacheDir + "/" + fileName + "_compress" + "." + fileType;
        if(new File(destPath).isFile()){
            videoUp(videoPath);
            return;
        }
        VideoCompress.VideoCompressTask task = VideoCompress.compressVideoLow(videoPath,
                destPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                dialogFragment.setTitle("正在压缩视频");
            }

            @Override
            public void onSuccess() {
                //Finish successfully
                dialogFragment.setProgress(100);
                if (!isCancelled)
                    videoUp(destPath);
            }

            @Override
            public void onFail() {
                //Failed
            }

            @Override
            public void onProgress(float percent) {
                //Progress
                Log.d("compress precent", percent + "");
                dialogFragment.setProgress((int) percent);
            }
        });

    }

    public void EpMediaTask(){
        int index = new File(videoPath).getName().lastIndexOf(".");
        String fileName = new File(videoPath).getName().substring(0,index);
        String appCacheDir = getExternalFilesDir(android.os.Environment.DIRECTORY_MOVIES).getAbsolutePath();
        String destPath = appCacheDir + "/" + fileName + "_ep.mp4" ;
        EpVideo epVideo = new EpVideo(videoPath);
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(destPath);
//        outputOption.setHeight(576);
//        outputOption.setWidth(1024);
        outputOption.frameRate = 25;//输出视频帧率,默认30
        outputOption.bitRate = 2;//输出视频码率,默认10
        dialogFragment.setTitle("正在进行转码");
        dialogFragment.setProgress(0);
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message = new Message();
                message.what = 0;
                message.arg1 = 100;
                handler.sendMessage(message);
                if (!isCancelled)
                    videoUp(destPath);
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onProgress(float progress) {
                //这里获取处理进度
                Log.d("compress precent", progress + "");
                Message message = new Message();
                message.what = 0;
                int p = (int)(progress * 100);
                if(p>=1) {
                    message.arg1 = p;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class myHandler extends Handler{
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: dialogFragment.setProgress(msg.arg1);
            }
            super.handleMessage(msg);
        }
    }

}
