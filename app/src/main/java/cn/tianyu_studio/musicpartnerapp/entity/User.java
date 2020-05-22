package cn.tianyu_studio.musicpartnerapp.entity;


import java.util.Date;

/**
 * @author 汪继友
 * @since 2018-10-30
 */
public class User {

    public static final int SEX_MALE = 0;
    public static final int SEX_FEMALE = 1;
    private static final long serialVersionUID = 1L;
    private Long userId;

    private String phone = "";

    private String pwd;

    private String headImgUrl = "";

    private String nickname = "";

    private Date birthday;

    private String sex;

    /**
     * 个性签名
     */
    private String signature = "";

    private String tag = "";

    /**
     * 接受作品推送通知
     */
    private Integer receiveWorkPush;
    /**
     * 作品允许评论
     */
    private Integer allowComment;

    private String gmtCreate;

    private Integer isFollow;

    private long followCount = 0L;

    private long fansCount = 0L;


    public long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(long followCount) {
        this.followCount = followCount;
    }

    public long getFansCount() {
        return fansCount;
    }

    public void setFansCount(long fansCount) {
        this.fansCount = fansCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getReceiveWorkPush() {
        return receiveWorkPush;
    }

    public void setReceiveWorkPush(Integer receiveWorkPush) {
        this.receiveWorkPush = receiveWorkPush;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }
}
