package cn.tianyu_studio.musicpartnerapp.constants;

/**
 * Created by 天宇 on 2018/2/3.
 */

public enum URLEnum {

    Code("users/sendCode"),
    Tag("tags"),
    Qiniu("qiniu/token"),
    Notifiction("notifications"),
    NotifictionUnread("notifications/unread-count"),
    Dynamic("dynamics"),
    Users("users"),
    ChangePasswd("resetPwdByCode"),
    LogOut("users/logout"),
    Login("users/login"),
    Register("users/register"),
    Work("works"),
    SetFollow("user-follows"),
    SetFav("user-favs"),
    SetCommentFav("user-fav-comments"),
    MyWork("works/mine"),
    UserWork("works/listByAuthor"),
    MyFollow("works/my-follow"),
    CheckToken("tickets/check"), //检测token是否有效
    Feed("feedbacks"),
    Comment("comments");

    private String url;

    URLEnum(String url) {
        this.url = SysConsts.SERVER_URL + url;
    }

    public String getUrl() {
        return url;
    }
}