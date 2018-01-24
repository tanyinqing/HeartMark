package com.yikang.heartmark.model.chat;

import java.io.Serializable;

/**
 * Created by Chang on 12/19/2014.
 */
public class UserInfo implements Serializable {
    long userId = 0;//原始ID
    String nickName = "";
    String profilePictureThumbnailId = "";
    int age = 0;
    int gender = 0;
    long mobile = 0;
    String locationCode = "";
    String alpha = "";
    //    String hospitialName;
//    String deptmentName;
//    String jobTitle;
//    String teachingTitle;
    String updateTime = "";

    //just to format a xmpp JID format from userId@host
    String jid = "";
    public Boolean isSelected = false;
    public Boolean isConfirm = false;
    public Boolean isUnableSelect = false;
    public Boolean isDelete = false;
    public String productIconId = "";  //product level

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfilePictureThumbnailId() {
        return profilePictureThumbnailId;
    }

    public void setProfilePictureThumbnailId(String profilePictureThumbnailId) {
        this.profilePictureThumbnailId = profilePictureThumbnailId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", profilePictureThumbnailId='" + profilePictureThumbnailId + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", mobile=" + mobile +
                ", locationCode='" + locationCode + '\'' +
                ", alpha='" + alpha + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
