package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.http.HttpMethod;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.adapter.MyPageAdapter;
import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.DownloadDialogFragment;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.LoadingDialogFragment;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.ShareDialogFragment;
import cn.tianyu_studio.musicpartnerapp.entity.Comment;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.Work;
import cn.tianyu_studio.musicpartnerapp.fragment.HomeFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.music.GeciFragment;
import cn.tianyu_studio.musicpartnerapp.fragment.music.MusicFragment;
import cn.tianyu_studio.musicpartnerapp.global.TApplication;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;
import cn.tianyu_studio.musicpartnerapp.widget.ExpandableTextView;

/**
 * 播放界面
 */
@ContentView(R.layout.activity_play)
public class PlayActivity extends BaseActivity {

    @ViewInject(R.id.play_tv_usertag)
    TextView tv_usertag;
    @ViewInject(R.id.play_viewpager)
    ViewPager viewPager;
    @ViewInject(R.id.play_tv_worktag)
    TextView tv_tag;
    @ViewInject(R.id.play_tv_commentNum)
    TextView commentNum;
    InputMethodManager imm;  //唤醒软键盘
    MusicFragment musicFragment;
    GeciFragment geciFragment;
    @ViewInject(R.id.play_recycle_comment)
    private RecyclerView recycle_comment;
    @ViewInject(R.id.play_expTv)
    private ExpandableTextView expTv_introduction;
    @ViewInject(R.id.play_iv_like)
    private ImageView iv_like;
    @ViewInject(R.id.play_tv_like)
    private TextView tv_like;
    @ViewInject(R.id.play_ll_lyric)
    private LinearLayout ll_lyric;
    @ViewInject(R.id.play_videoPlayer)
    private JZVideoPlayerStandard videoPlayerStandard;
    @ViewInject(R.id.play_btn_follow)
    private Button btn_follow;
    @ViewInject(R.id.play_tv_username)
    private TextView tv_username;
    @ViewInject(R.id.play_iv_authorHeadImg)
    private ImageView iv_authorHeadImg;
    @ViewInject(R.id.play_et_comment)
    private EditText et_comment;
    /*
     * 作品信息
     * 包括歌手名、封面、歌曲名
     */
    @ViewInject(R.id.play_iv_head)
    private TextView tv_workName;
    private LoadingDialogFragment loadingDialogFragment;
    private Work work;
    private BaseQuickAdapter<Comment, BaseViewHolder> commentAdapter;
    private long replyCommentId = 0;
    private int current = 1;
    private int totalCurrent = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(getFragmentManager(), "playLoading");
        initWork(HomeFragment.selectWorkId);
    }

    private void updateFollowButton() {
        if (Work.FOLLOW == work.getAuthor().getIsFollow()) {
            btn_follow.setText(getString(R.string.play_follow));
        } else {
            btn_follow.setText(getString(R.string.play_unfollow));
        }
    }



    @Event(R.id.play_btn_follow)
    private void clickFollowButton(View view) {
        work.getAuthor().setIsFollow((work.getAuthor().getIsFollow() + 1) % 2);
        TRequestParams params = new TRequestParams(URLEnum.SetFollow.getUrl());
        params.addParameter("followUserId", work.getAuthorId());
        params.addParameter("followStatus", work.getAuthor().getIsFollow());
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(PlayActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == TResultCode.SUCCESS.getCode()) {
                    updateFollowButton();
                }
            }
        });
    }

    private void initVideo() {
        videoPlayerStandard.setUp(work.getVideoUrl()
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
        loadingDialogFragment.dismiss();
    }

    private void initMusicPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (!StringUtils.isTrimEmpty(work.getAudioUrl())) {
            musicFragment = new MusicFragment();
            musicFragment.setUrl(work.getAudioUrl(), work.getCoverUrl());
            fragmentList.add(musicFragment);
        } else {
            geciFragment = new GeciFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", work.getLyricUrl());
            geciFragment.setArguments(bundle);
            fragmentList.add(geciFragment);
            loadingDialogFragment.dismiss();
        }
        MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (work != null && !work.getVideoUrl().isEmpty() && JZVideoPlayer.backPress()) {
            return;
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (work == null)
            return;
        if (!work.getVideoUrl().isEmpty()) {
            JZVideoPlayer.releaseAllVideos();
        } else if (!work.getAudioUrl().isEmpty()) {
            musicFragment.close();
        }
    }

    private void initCommentAdapter(List<Comment> list) {
        // commentAdapter.setEnableLoadMore(false);
        commentAdapter = new BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.item_play_comment, list) {
            @Override
            protected void convert(BaseViewHolder helper, Comment item) {
                helper.setText(R.id.item_play_comment_tv_userName, item.getPublisher().getNickname())
                        .setText(R.id.item_play_comment_tv_likeCount, item.getFavCount() + "")
                        .setText(R.id.item_play_comment_tv_content, item.getContent())
                        .addOnClickListener(R.id.item_play_comment_iv_like)
                        .addOnClickListener(R.id.item_play_comment_tv_likeCount)
                        .addOnClickListener(R.id.item_play_comment_iv_reply);
                //后台传过来的时间中间会带个T
                helper.setText(R.id.item_play_comment_tv_time, item.getGmtCreate().replace('T', ' '));

                // 更新评论点赞图标
                if (item.getIsFav() == Comment.LIKE) {
                    helper.setImageResource(R.id.item_play_comment_iv_like, R.drawable.like_red);
                    helper.setTextColor(R.id.item_play_comment_tv_likeCount, getResources().getColor(R.color.app_color));
                } else {
                    helper.setImageResource(R.id.item_play_comment_iv_like, R.drawable.like_black);
                    helper.setTextColor(R.id.item_play_comment_tv_likeCount, getResources().getColor(R.color.black));
                }

                ImageView iv_userHeadImg = helper.getView(R.id.item_play_comment_iv_userHeadImg);
                x.image().bind(iv_userHeadImg, item.getPublisher().getHeadImgUrl());
                TextView tv_replay = helper.getView(R.id.item_play_comment_tv_replay);
                if (item.getReplyComment() == null) {
                    tv_replay.setVisibility(View.GONE);
                    return;
                }

                //构造被回复内容
                SpannableString userName = new SpannableString("@" + item.getReplyComment().getPublisher().getNickname());
                userName.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getApplication().getResources().getColor(R.color.clickable_text));
                        ds.setUnderlineText(false);
                    }
                }, 0, userName.length(), Spanned.SPAN_MARK_MARK);

                tv_replay.setText(userName);
                tv_replay.append(":" + item.getReplyComment().getContent());
                tv_replay.setMovementMethod(LinkMovementMethod.getInstance());
            }
        };
        // commentAdapter.bindToRecyclerView(recycle_comment);
        commentAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Comment comment = commentAdapter.getItem(position);
            switch (view.getId()) {
                case R.id.item_play_comment_iv_like:
                case R.id.item_play_comment_tv_likeCount:
                    TextView tv_likeCount = (TextView) adapter.getViewByPosition(position, R.id.item_play_comment_tv_likeCount);
                    ImageView iv_like = (ImageView) adapter.getViewByPosition(position, R.id.item_play_comment_iv_like);

                    if (comment.getIsFav() == Comment.LIKE) {
                        comment.setFavCount(comment.getFavCount() - 1);
                        iv_like.setImageResource(R.drawable.like_black);
                        tv_likeCount.setTextColor(getResources().getColor(R.color.black));
                    } else {
                        comment.setFavCount(comment.getFavCount() + 1);
                        iv_like.setImageResource(R.drawable.like_red);
                        tv_likeCount.setTextColor(getResources().getColor(R.color.app_color));
                    }
                    comment.setIsFav((comment.getIsFav() + 1) % 2);
                    updateCommentFav(comment.getIsFav(), comment.getCommentId());
                    tv_likeCount.setText(String.valueOf(comment.getFavCount()));
                    break;

                case R.id.item_play_comment_iv_reply:
                    TextView tv_publisherNickname = (TextView) adapter.getViewByPosition(recycle_comment, position, R.id.item_play_comment_tv_userName);
                    replyCommentId = comment.getCommentId();
                    et_comment.setHint("@" + tv_publisherNickname.getText() + " :");
                    et_comment.setFocusable(true);
                    et_comment.requestFocus();
                    //强制弹出软键盘
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    break;
            }
        });

        // 设置尾布局
//        TextView tv_footer = new TextView(this);
//        tv_footer.setText("没有更多");
//        tv_footer.setTextSize(12);
//        tv_footer.setTextColor(getResources().getColor(R.color.gray_cc));
//        tv_footer.setGravity(Gravity.CENTER_HORIZONTAL);
//        tv_footer.setPadding(0, 0, 0, 40);
//        commentAdapter.addFooterView(tv_footer);
        recycle_comment.setHasFixedSize(true);
        recycle_comment.setNestedScrollingEnabled(false);
        recycle_comment.setLayoutManager(new LinearLayoutManager(this));
        recycle_comment.setAdapter(commentAdapter);

        commentAdapter.setOnLoadMoreListener(() -> recycle_comment.postDelayed(() -> {
            ++current;
            if (current >= totalCurrent) {
                initCommentData();
                commentAdapter.loadMoreEnd();
            } else {
                initCommentData();
                commentAdapter.loadMoreComplete();
            }
        }, 3000), recycle_comment);
        commentAdapter.disableLoadMoreIfNotFullPage();
    }

    /**
     * 根据当前work是否被点赞，更新点赞的图标
     */
    private void updateLikeView() {
        tv_like.setText(String.valueOf(work.getFavCount()));
        if (work.getIsFav() == Work.LIKE) {
            iv_like.setImageResource(R.drawable.like_red);
            tv_like.setTextColor(getResources().getColor(R.color.app_color));
        } else {
            iv_like.setImageResource(R.drawable.like_black);
            tv_like.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * 考虑两种情况 一种是发表评论  一种是回复评论
     * 此处先做简单的回复测试。
     * 已完成测试
     */
    @Event(R.id.play_publish_comment)
    private void publish(View view) {
        imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
        String text = et_comment.getText().toString();
        et_comment.setText("");
        et_comment.clearFocus();
        if (!StringUtils.isTrimEmpty(text)) {
            TRequestParams params = new TRequestParams(URLEnum.Comment.getUrl());
            params.addBodyParameter("content", text);
            params.addParameter("workId", work.getWorkId());
            if (replyCommentId > 0) {
                params.addParameter("replyCommentId", replyCommentId);
            }
            x.http().request(HttpMethod.POST, params, new BaseCallback<TResult>() {
                @Override
                public void onSuccess(TResult result) {
                    initCommentData();
                    Toast.makeText(PlayActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Event(value = {R.id.play_iv_like, R.id.play_tv_like})
    private void clickLike(View view) {
        if (work.getIsFav() == Work.LIKE) {
            work.setFavCount(work.getFavCount() - 1);
        } else {
            work.setFavCount(work.getFavCount() + 1);
        }
        work.setIsFav((work.getIsFav() + 1) % 2);
        updateLikeView();
        TRequestParams params = new TRequestParams(URLEnum.SetFav.getUrl());
        params.addParameter("workId", work.getWorkId());
        params.addParameter("favStatus", work.getIsFav());
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(PlayActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示下载框，并将可以下载的项和下载地址以map的方式传递过去
     */
    @Event(value = {R.id.play_tv_download, R.id.play_iv_download})
    private void showDownloadDialogFragment(View view) {
        DownloadDialogFragment downloadDialogFragment = new DownloadDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(DownloadDialogFragment.KEY_AUTHOR_HEAD_IMG, work.getAuthor().getHeadImgUrl());
        bundle.putString(DownloadDialogFragment.KEY_AUTHOR_NAME, tv_username.getText().toString());
        bundle.putString(DownloadDialogFragment.KEY_WORK_NAME, tv_workName.getText().toString());
        HashMap<String, String> map = new HashMap<>();
        if (!StringUtils.isTrimEmpty(work.getVideoUrl())) {
            map.put(DownloadDialogFragment.MV, work.getVideoUrl());
        } else if (!StringUtils.isTrimEmpty(work.getAudioUrl())) {
            map.put(DownloadDialogFragment.MUSIC, work.getAudioUrl());
        } else {
            map.put(DownloadDialogFragment.PIC, work.getLyricUrl());
        }
        bundle.putSerializable(DownloadDialogFragment.KEY_MAP, map);
        bundle.putString("name", work.getName());
        downloadDialogFragment.setArguments(bundle);

        downloadDialogFragment.show(getFragmentManager(), "downloadFragment");
    }

    @Event(R.id.play_iv_share)
    private void showEditDialogFragment(View view) {
        SharedPreferences sharedPreferences;
        sharedPreferences = TApplication.getContext().getSharedPreferences(SysConsts.SP_NAME, Context.MODE_PRIVATE);
        Long id = sharedPreferences.getLong(SPEnum.UserId.getKey(), 0L);
        ShareDialogFragment shareDialogFragment = new ShareDialogFragment();
        Bundle bundle = new Bundle();
        if (id != work.getAuthorId())
            bundle.putBoolean("permission", false);
        else
            bundle.putBoolean("permission", true);
        bundle.putLong("workId", work.getWorkId());
        shareDialogFragment.setArguments(bundle);
        shareDialogFragment.show(getFragmentManager(), "shareDialogFragment");
    }

    @Event(R.id.play_iv_back)
    private void back(View view) {
        finish();
    }

    /**
     * 判断点击按下是否在EditText上
     * 我们这里做一下微调，即作用区域不包括提交按钮，这样会截断他的监听事件
     * 我们把x轴的比较去掉，只保留Y轴的比较
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            // int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            //int right = left + v.getWidth();
            return !(event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 解决软键盘下拉后，edittext的焦点改变 bug
     * 此方法针对 点击屏幕外  触发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (imm.isActive() && isShouldHideInput(v, ev)) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    //Log.d("click","点击了屏幕外");
                    et_comment.setHint("");
                    et_comment.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private void initWork(long workid) {
        videoPlayerStandard.setVisibility(View.GONE);
        ll_lyric.setVisibility(View.INVISIBLE);
        TRequestParams tRequestParams = new TRequestParams(URLEnum.Work.getUrl() + "/" + workid);
        x.http().request(HttpMethod.GET, tRequestParams, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    work = JSON.parseObject(result.getData().toString(), Work.class);
                    initView();
                } else {
                    Toast.makeText(PlayActivity.this, "该作品已删除或不存在", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PlayActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void initView() {
        tv_workName.setText(work.getName());
        tv_username.setText(work.getAuthor().getNickname());
        tv_usertag.setText(work.getAuthor().getTag());
        StringBuilder tagBuilder = new StringBuilder();
        for (int t = 0; t < work.getWorkTags().size(); t++) {
            if (t > 0)
                tagBuilder.append("/").append(work.getWorkTags().get(t).getTagName());
            else
                tagBuilder.append(work.getWorkTags().get(t).getTagName());
        }
        tv_tag.setText(tagBuilder.toString());
        ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build(); //圆形图片
        x.image().bind(iv_authorHeadImg, work.getAuthor().getHeadImgUrl(), imageOptions);
        iv_authorHeadImg.setOnClickListener(v -> {
            Intent i = new Intent(this, UserPageActivity.class);
            i.putExtra("id", work.getAuthorId());
            startActivity(i);
        });
        if (!StringUtils.isTrimEmpty(work.getVideoUrl())) {
            ll_lyric.setVisibility(View.GONE);
            videoPlayerStandard.setVisibility(View.VISIBLE);
            initVideo();
        } else {
            videoPlayerStandard.setVisibility(View.GONE);
            ll_lyric.setVisibility(View.VISIBLE);
            initMusicPager();
        }
        updateLikeView();
        updateFollowButton();
        expTv_introduction.setText(work.getIntroduction());
        initCommentData();
    }

    private void initCommentData() {
        TRequestParams requestParams = new TRequestParams(URLEnum.Comment.getUrl());
        requestParams.addQueryParameter("workId", work.getWorkId());
        requestParams.addQueryParameter("current", current);
        x.http().request(HttpMethod.GET, requestParams, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                commentNum.setText(jsonObject.getString("total"));
                totalCurrent = jsonObject.getInteger("pages");
                if (current > 1)
                    commentAdapter.addData(JSON.parseArray(jsonObject.getString("records"), Comment.class));
                else
                    initCommentAdapter(JSON.parseArray(jsonObject.getString("records"), Comment.class));
            }
        });
    }

    private void updateCommentFav(int status, Long id) {
        TRequestParams params = new TRequestParams(URLEnum.SetCommentFav.getUrl());
        params.addParameter("commentId", id);
        params.addParameter("favStatus", status);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(PlayActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void closeLoading() {
        loadingDialogFragment.dismiss();
    }


}
