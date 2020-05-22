package cn.tianyu_studio.musicpartnerapp.entity;

import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 汪继友
 * @since 2018-11-01
 */

public class Feedback {

    private static final long serialVersionUID = 1L;


    private Long feedbackId;

    private String content;

    private Long publisherId;

    private Date gmtCreate;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
