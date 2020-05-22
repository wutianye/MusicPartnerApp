package cn.tianyu_studio.musicpartnerapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.xutils.http.RequestParams;

import cn.tianyu_studio.musicpartnerapp.constants.SPEnum;
import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;
import cn.tianyu_studio.musicpartnerapp.global.TApplication;

/**
 * Created by 天宇 on 2018/2/3.
 */

public class TRequestParams extends RequestParams {

    public TRequestParams() {
        this(null);
    }

    /**
     * 添加token，timestamp，sign
     *
     * @param uri 访问路径
     */
    public TRequestParams(String uri) {
        super(uri);
        SharedPreferences sharedPreferences = TApplication.getContext().getSharedPreferences(SysConsts.SP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(SPEnum.Token.getKey(), "");
        super.addHeader("token", token);
    }

    public void addQueryParameter(String name, Object value) {
        super.addQueryStringParameter(name, value.toString());
    }
}
