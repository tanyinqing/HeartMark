package com.yikang.heartmark.model.chat;

import com.yikang.heartmark.constant.Constants;

import java.util.Date;

/**
 * Created by Chang on 12/17/2014.
 */
public class Patient extends User {
    private String cursor;
    public static final String PATIENT_ID = "patient_id";
    private String lastNoticeTime = "";
    private String lastRelationChangeTime = "";
    private String patientId = ""; //混淆后的user_id
    private String jsonTags = "";//医生给患者打的标签
    private Date createdRealtionTime;
    private int status = Constants.Relation.RELATION_CODE_NORMAL; //1表示有关系，2表示主动解除关系，3表示对方解除关系

    //private ProductInfo productInfo;
    public int product_level;    //1深度咨询 2家庭医生
    public String product_name;
    public String product_icon_id = "";

//    public void setProductInfo(ProductInfo productInfo) {
//        this.productInfo = productInfo;
//    }
//
//    public ProductInfo getProductInfo() {
//        return productInfo;
//    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getCursor() {
        return cursor;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getLastNoticeTime() {
        return lastNoticeTime;
    }

    public void setLastNoticeTime(String lastNoticeTime) {
        this.lastNoticeTime = lastNoticeTime;
    }

    public String getLastRelationChangeTime() {
        return lastRelationChangeTime;
    }

    public void setLastRelationChangeTime(String lastRelationChangeTime) {
        this.lastRelationChangeTime = lastRelationChangeTime;
    }

    public String getJsonTags() {
        return jsonTags;
    }

    public void setJsonTags(String jsonTags) {
        this.jsonTags = jsonTags;
    }

    public Date getCreatedRealtionTime() {
        return createdRealtionTime;
    }

    public void setCreatedRealtionTime(Date createdRealtionTime) {
        this.createdRealtionTime = createdRealtionTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "lastNoticeTime='" + lastNoticeTime + '\'' +
                ", lastRelationChangeTime='" + lastRelationChangeTime + '\'' +
                ", patientId='" + patientId + '\'' +
                ", jsonTags='" + jsonTags + '\'' +
                ", createdRealtionTime=" + createdRealtionTime +
                ", status=" + status +
                '}';
    }
}
