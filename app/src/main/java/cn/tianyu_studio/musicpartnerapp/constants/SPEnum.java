package cn.tianyu_studio.musicpartnerapp.constants;

/**
 * Created by 天宇 on 2018/2/3.
 */

public enum SPEnum {
    Token("token"),
    UserId("userId"),
    BaseInfoComplete("baseinfo_complete");

    private String key;
    SPEnum(String key) {
        this.key = key;
    }
    public String getKey(){
        return key;
    }
}
