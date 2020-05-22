package cn.tianyu_studio.musicpartnerapp.entity;

import java.io.Serializable;

/**
 * @author 汪继友
 * @since 2018-10-30
 */

public class Comment implements Serializable {

    public static final int LIKE = 1;
    public static final int UNLIKE = 0;
    private static final long serialVersionUID = 1L;
    private Long commentId;

    private Long workId;

    private Long publisherId;

    private Long favCount;

    private String content;

    /**
     * 被回复的评论id
     */
    private Long replyCommentId;

    private String gmtCreate;

    private User publisher;

    private Comment replyComment;

    private Integer isFav;


    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getFavCount() {
        return favCount;
    }

    public void setFavCount(Long favCount) {
        this.favCount = favCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public Comment getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(Comment replyComment) {
        this.replyComment = replyComment;
    }

    public Integer getIsFav() {
        return isFav;
    }

    public void setIsFav(Integer isFav) {
        this.isFav = isFav;
    }
}
