package com.yikang.heartmark.model.chat;


import java.io.Serializable;

/**
 * Created by guolchen on 2014/12/26.
 */
public class TagIcon implements Serializable {

    public boolean isPatient = false;
    public boolean isEdit = false;

    int drawable;
    int color;

    String tagText = "";

    boolean iDeleted = false;
    boolean isSelected = false;

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
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

}
