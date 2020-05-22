package cn.tianyu_studio.musicpartnerapp.constants;

/**
 * Created by 天宇 on 2018/3/14.
 */

public interface SysConsts {
    String SERVER_URL = "http://212.129.139.147:8080/music-partner-api/";
    String SP_NAME = "music_partner";
    String SECRET = "music_happy123";

    String QINIU_RESOURCES_PREFIX = "http://cdn.tianyu-studio.cn/";

    int MENU_WIDTH = 350;
    int MENU_HEIGHT = 100;

    /**
     * 上传作品时，图片最大上传数量
     */
    int MAX_IMAGE_UPLOAD_SIZE = 4;
}
