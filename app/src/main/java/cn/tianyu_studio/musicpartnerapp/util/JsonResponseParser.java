package cn.tianyu_studio.musicpartnerapp.util;

import com.alibaba.fastjson.JSON;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

import cn.tianyu_studio.musicpartnerapp.entity.TResult;

/**
 * Created by 天宇 on 2018/1/2.
 */

public class JsonResponseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (resultClass.equals(TResult.class)) {
            return JSON.parseObject(result, TResult.class);
        } else
            return null;
    }
}
