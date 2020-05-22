package cn.tianyu_studio.musicpartnerapp.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.tianyu_studio.musicpartnerapp.adapter.NotificationMultipleItemQuickAdapter;

/**
 * 动态
 *
 * @author 汪继友
 * @since 2018-11-08
 */

public class Dynamic implements MultiItemEntity {

    /**
     * 评论了作品
     */
    public static final int TYPE_COMMENT = 0;
    /**
     * 点赞作品
     */
    public static final int TYPE_FAV_WORK = 1;
    /**
     * 点赞评论
     */
    public static final int TYPE_FAV_COMMENT = 2;
    /**
     * 评论时艾特某人
     */
    public static final int TYPE_REPLY = 3;

    /**
     * 动态id
     */

    private Long dynamicId;

    /**
     * 消息产生者
     */
    private Long senderId;

    private String senderNickname;

    private String senderHeadImgUrl;


    /**
     * 消息接收者
     */
    private Long receiverId;

    /**
     * 关联的作品id
     */
    private Long workId;

    private String workName;

    /**
     * 通知内容，类型是赞时为空
     */
    private String content;

    /**
     * 通知类型：评论、@、赞
     */
    private Integer type;

    /**
     * 是否已读
     */
    private Integer isRead;

    private String gmtCreate;


    public String getSenderHeadImgUrl() {
        return senderHeadImgUrl;
    }

    public void setSenderHeadImgUrl(String senderHeadImgUrl) {
        this.senderHeadImgUrl = senderHeadImgUrl;
    }

    public Long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public int getItemType() {
        return NotificationMultipleItemQuickAdapter.ITEM_TYPE_COMMENT;
    }
}
