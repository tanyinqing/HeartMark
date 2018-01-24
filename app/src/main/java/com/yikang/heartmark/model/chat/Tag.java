package com.yikang.heartmark.model.chat;


import com.yikang.heartmark.common.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guolchen on 2014/12/26.
 */
public class Tag extends TagIcon implements Serializable {
    long tagId;
    long creatorId;
    //    int drawable;
//    int color;
    Date createdTime = new Date(TimeUtil.getAdjustDateTimeVal(TimeUtil.toUTC(new Date(System.currentTimeMillis())).getTime()));
    int patientCount = 0;
//    String tagText = "";
//    boolean iDeleted = false;
//    boolean isSelected = false;

    public boolean isUnClick = false;  //个数为0  不能点击

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public boolean isiDeleted() {
        return iDeleted;
    }

    public void setiDeleted(boolean iDeleted) {
        this.iDeleted = iDeleted;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", creatorId=" + creatorId +
                ", drawable=" + drawable +
                ", color=" + color +
                ", createdTime=" + createdTime +
                ", patientCount=" + patientCount +
                ", tagText='" + tagText + '\'' +
                ", iDeleted=" + iDeleted +
                '}';
    }
}
