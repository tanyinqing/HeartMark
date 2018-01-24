package com.yikang.heartmark.model.chat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Chang on 9/17/2014.
 */
public class ChatSession implements Serializable {
    private long sessionId = 0;
    private String userProfilePictureId = "";
    private long hostUserId = 0;
    private long userId = 0;
    private String userNickName = "";
    private int gender = 0;
    private int newMsgCount = 0;
    private String lastMsgContent = "";
    private Date lastMsgTime = new Date();
    public String productIconId = "";  //product level
    public int product_leavel;

    public ChatSession() {
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserProfilePictureId() {
        return userProfilePictureId;
    }

    public void setUserProfilePictureId(String userProfilePictureId) {
        this.userProfilePictureId = userProfilePictureId;
    }

    public long getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(long hostUserId) {
        this.hostUserId = hostUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getNewMsgCount() {
        return newMsgCount;
    }

    public void setNewMsgCount(int newMsgCount) {
        this.newMsgCount = newMsgCount;
    }

    public String getLastMsgContent() {
        return lastMsgContent;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public Date getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(Date lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    @Override
    public String toString() {
        return "ChatSession{" +
                "sessionId=" + sessionId +
                ", userProfilePictureId='" + userProfilePictureId + '\'' +
                ", hostUserId=" + hostUserId +
                ", userId=" + userId +
                ", userNickName='" + userNickName + '\'' +
                ", gender=" + gender +
                ", newMsgCount=" + newMsgCount +
                ", lastMsgContent='" + lastMsgContent + '\'' +
                ", lastMsgTime=" + lastMsgTime +
                '}';
    }
}
