package cn.tianyu_studio.musicpartnerapp.util;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by tianyu on 17-7-5.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TMessage {
    public static int CODE_SUCCESS = 1;
    public static int CODE_FAILURE = -1;
    private int code;
    private String info;
    private Object data;

    public TMessage() {

    }

    public TMessage(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public TMessage(int code, String info, Object data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
