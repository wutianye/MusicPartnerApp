package cn.tianyu_studio.musicpartnerapp.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;

import org.xutils.http.HttpMethod;
import org.xutils.x;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.constants.URLEnum;
import cn.tianyu_studio.musicpartnerapp.entity.TResult;
import cn.tianyu_studio.musicpartnerapp.entity.TResultCode;
import cn.tianyu_studio.musicpartnerapp.entity.Work;

/**
 * Created by 天宇 on 2018/1/4.
 */

public class BaseFragment extends Fragment {
    LinearLayout ll_like, ll_follow;
    CustomPopWindow popWindow;
    private boolean injected = false;
    private ImageView iv_like, iv_follow;
    private TextView tv_like, tv_follow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;

        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    /**
     * 显示小菜单，点赞和关注
     */
    protected void showMenu(View view, Work work) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_menu, null);
        popWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
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
            //updateMenuView(work);
            updateFav(work);
        });

        ll_follow.setOnClickListener(v -> {
            work.getAuthor().setIsFollow((work.getAuthor().getIsFollow() + 1) % 2);
            //updateMenuView(work);
            updateFollow(work);
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

    public void updateFav(Work work) {
        TRequestParams params = new TRequestParams(URLEnum.SetFav.getUrl());
        params.addParameter("workId", work.getWorkId());
        params.addParameter("favStatus", work.getIsFav());
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == TResultCode.SUCCESS.getCode()) {
                    updateMenuView(work);
                }
            }
        });
    }

    public void updateFollow(Work work) {
        TRequestParams params = new TRequestParams(URLEnum.SetFollow.getUrl());
        params.addParameter("followUserId", work.getAuthorId());
        params.addParameter("followStatus", work.getAuthor().getIsFollow());
        x.http().request(HttpMethod.PUT, params, new BaseCallback<TResult>() {
            @Override
            public void onSuccess(TResult result) {
                Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == TResultCode.SUCCESS.getCode()) {
                    updateMenuView(work);
                }
            }
        });
    }

    public void popDismiss() {
        popWindow.dissmiss();
    }

}
