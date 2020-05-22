package cn.tianyu_studio.musicpartnerapp.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.tianyu_studio.musicpartnerapp.adapter.NotificationMultipleItemQuickAdapter;

public class Notification implements MultiItemEntity {
    private Long notificationId;

    private String content;

    private String gmtCreate;

    private Integer isRead;


    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public int getItemType() {
        return NotificationMultipleItemQuickAdapter.ITEM_TYPE_NOTIFICATION;
    }
}
