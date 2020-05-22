package cn.tianyu_studio.musicpartnerapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhouwei.library.CustomPopWindow;

import org.xutils.http.HttpMethod;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.User;
import cn.tianyu_studio.musicpartnerapp.entity.Work;
import cn.tianyu_studio.musicpartnerapp.fragment.HomeFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseCallback;
import cn.tianyu_studio.musicpartnerapp.util.TRequestParams;

@ContentView(R.layout.activity_user_page)
public class UserPageActivity extends BaseActivity {

    @ViewInject(R.id.page_recycler)
    RecyclerView recyclerView;
    @ViewInject(R.id.page_tv_userName)
    TextView tv_name;
    @ViewInject(R.id.page_tv_qianm)
    TextView tv_signature;
    @ViewInject(R.id.page_tv_tag)
    TextView tv_tag;
    @ViewInject(R.id.page_iv_userHeadImg)
    ImageView img_head;
    @ViewInject(R.id.page_tv_fensi)
    TextView tv_fensi;
    @ViewInject(R.id.page_tv_guanzhu)
    TextView tv_guanzhu;
    BaseQuickAdapter<Work, BaseViewHolder> workAdapter;
    BaseQuickAdapter<String, BaseViewHolder> testAdapter;
    int totalCurrent = 1;
    int current = 1;
    LinearLayout ll_like, ll_follow;
    long id;
    private ImageView iv_like, iv_follow;
    private TextView tv_like, tv_follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        id = i.getLongExtra("id", 0L);
        getUserDataFromWeb();
        getWorkDataFromWeb();
    }

    private void initView() {
        View view = new View(this);
        View.inflate(this, R.layout.layout_userdata, null);
        workAdapter.addHeaderView(view);
    }


    private void getUserDataFromWeb() {
        TRequestParams params = new TRequestParams(URLEnum.Users.getUrl() + "/" + id);
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    Log.d("me success", result.getMessage() + result.getData().toString());
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    tv_name.setText(jsonObject.getString("nickname"));
                    tv_signature.setText(jsonObject.getString("signature"));
                    tv_tag.setText(jsonObject.getString("tag"));
                    String headUrl = jsonObject.getString("headImgUrl");
                    ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build(); //圆形图片
                    if (!headUrl.isEmpty())
                        x.image().bind(img_head, headUrl, imageOptions);
                    String fans = jsonObject.getString("fansCount");
                    String follow = jsonObject.getString("followCount");
                    if (fans != null)
                        tv_fensi.setText(fans);
                    else
                        tv_fensi.setText("0");
                    if (follow != null)
                        tv_guanzhu.setText(follow);
                    else
                        tv_guanzhu.setText("0");
                }
            }
        });
    }

    private void initHomeAdapter(List<Work> list) {
        ImageOptions imageOptions = new ImageOptions.Builder().setCircular(true).build(); //圆形图片
        workAdapter = new BaseQuickAdapter<Work, BaseViewHolder>(R.layout.item_work_list, list) {
            @Override
            protected void convert(BaseViewHolder helper, Work item) {
                StringBuilder workTagText = new StringBuilder();
                for (int i = 0; i < item.getWorkTags().size(); i++) {
                    workTagText.append(item.getWorkTags().get(i).getTagName()).append(" ");
                }
                helper.setText(R.id.item_workList_tv_title, item.getName());
                helper.setText(R.id.item_workList_tv_type, workTagText.toString());
                helper.setText(R.id.item_workList_tv_like_num, item.getFavCount() + "");
                helper.setText(R.id.item_workList_tv_play_num, item.getBrowseCount() + "");
                helper.setText(R.id.item_workList_tv_username, item.getAuthor().getNickname());
                helper.setText(R.id.item_workList_tv_intro, item.getAuthor().getTag());
                RelativeLayout rl_bg = helper.getView(R.id.item_workList_rl);
                Glide.with(UserPageActivity.this).load(item.getCoverUrl())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                rl_bg.setBackground(resource);
                            }
                        });
                ImageView img_head = helper.getView(R.id.item_workList_img_head);
                x.image().bind(img_head, item.getAuthor().getHeadImgUrl(), imageOptions);
                helper.addOnClickListener(R.id.item_workList_btn_menu).addOnClickListener(R.id.item_workList_rl);
            }
        };
        workAdapter.setOnItemChildClickListener((adapter1, view, position) -> {
            switch (view.getId()) {
                case R.id.item_workList_btn_menu:
                    showMenu(view, workAdapter.getItem(position));
                    break;
                case R.id.item_workList_rl:
                    Intent intent = new Intent(this, PlayActivity.class);
                    // TODO 更改为work id
                    HomeFragment.selectWorkId = workAdapter.getItem(position).getWorkId();
                    startActivity(intent);
                    break;
            }
        });
        //initView();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(workAdapter);
        workAdapter.setOnLoadMoreListener(() -> recyclerView.postDelayed(() -> {
            ++current;
            getWorkDataFromWeb();
            if (current >= totalCurrent)
                workAdapter.loadMoreEnd();
            else
                workAdapter.loadMoreComplete();
        }, 3000), recyclerView);
        workAdapter.disableLoadMoreIfNotFullPage();
    }


    private void getWorkDataFromWeb() {
        TRequestParams params = new TRequestParams(URLEnum.UserWork.getUrl());
        params.addQueryParameter("current", current);
        params.addQueryParameter("authorId", id);
        x.http().request(HttpMethod.GET, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                if (result.getCode().equals(TResultCode.SUCCESS.getCode())) {
                    JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                    totalCurrent = jsonObject.getInteger("pages");
                    if (current > 1) {
                        workAdapter.addData(JSON.parseArray(jsonObject.getString("records"), Work.class));
                    } else {
                        initHomeAdapter(JSON.parseArray(jsonObject.getString("records"), Work.class));
                    }
                }
            }
        });
    }

    /**
     * 显示小菜单，点赞和关注
     */
    protected void showMenu(View view, Work work) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_menu, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(SysConsts.MENU_WIDTH, SysConsts.MENU_HEIGHT)
                //是否PopupWindow 以外触摸dissmiss
                .setOutsideTouchable(true)
                .create()
                //显示PopupWindow，x和y偏移，控制popwindow在左边
                .showAsDropDown(view, -SysConsts.MENU_WIDTH - 10, -SysConsts.MENU_HEIGHT / 2 - 25);

        ll_like = contentView.findViewById(R.id.menu_ll_like);
        iv_like = contentView.findViewById(R.id.menu_iv_like);
        tv_like = contentView.findViewById(R.id.menu_tv_like);
        ll_follow = contentView.findViewById(R.id.menu_ll_follow);
        iv_follow = contentView.findViewById(R.id.menu_iv_follow);
        tv_follow = contentView.findViewById(R.id.menu_tv_follow);

        updateMenuView(work);

        ll_like.setOnClickListener(v -> {
            work.setIsFav((work.getIsFav() + 1) % 2);
            updateMenuView(work);
            updateFav(work.getWorkId(), work.getIsFav());
        });

        ll_follow.setOnClickListener(v -> {
            User user = work.getAuthor();
            user.setIsFollow((work.getAuthor().getIsFollow() + 1) % 2);
            updateMenuView(work);
            updateFollow(work.getAuthorId(), work.getAuthor().getIsFollow());
        });
    }

    /**
     * 根据用户是否关注/点赞work，更新menu
     */
    private void updateMenuView(Work work) {
        if (work.getIsFav() == Work.UNLIKE) {
            iv_like.setImageResource(R.drawable.like_white);
            tv_like.setText("喜欢");
        } else {
            iv_like.setImageResource(R.drawable.like_red);
            tv_like.setText("取消");
        }

        if (work.getAuthor().getIsFollow() == Work.UNFOLLOW) {
            iv_follow.setImageResource(R.drawable.follow);
            tv_follow.setText("关注");
        } else {
            iv_follow.setImageResource(R.drawable.follow_red);
            tv_follow.setText("取消");
        }
    }

    private void updateFav(Long workId, int status) {
        TRequestParams params = new TRequestParams(URLEnum.SetFav.getUrl());
        params.addParameter("workId", workId);
        params.addParameter("favStatus", status);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(UserPageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFollow(Long followUserId, int status) {
        TRequestParams params = new TRequestParams(URLEnum.SetFollow.getUrl());
        params.addParameter("followUserId", followUserId);
        params.addParameter("followStatus", status);
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(UserPageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
