package cn.tianyu_studio.musicpartnerapp.entity;


import java.util.List;

/**
 * @author 汪继友
 * @since 2018-10-30
 */

public class Work {
    public static final int LIKE = 1;
    public static final int UNLIKE = 0;
    public static final int FOLLOW = 1;
    public static final int UNFOLLOW = 0;
    public static final int TYPE_PERSONAL = 0;
    public static final int TYPE_TEAM = 1;
    private Long workId;

    /**
     * 发布者id
     */
    private Long authorId;

    private String coverUrl;

    private String name;

    private String introduction;

    /**
     * 作品类型，团队作品、个人作品
     */
    private Integer type;

    private String audioUrl = "";

    private String lyricUrl = "";

    private String videoUrl = "";

    /**
     * 收藏数量
     */
    private Long favCount;

    /**
     * 被浏览量
     */
    private Long browseCount;

    private String gmtCreate;

    private List<WorkTag> workTags;

    private Integer isFav;

    private User author;


    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getFavCount() {
        return favCount;
    }

    public void setFavCount(Long favCount) {
        this.favCount = favCount;
    }

    public Long getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Long browseCount) {
        this.browseCount = browseCount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public List<WorkTag> getWorkTags() {
        return workTags;
    }

    public void setWorkTags(List<WorkTag> workTags) {
        this.workTags = workTags;
    }

    public Integer getIsFav() {
        return isFav;
    }

    public void setIsFav(Integer isFav) {
        this.isFav = isFav;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
