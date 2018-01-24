package com.yikang.heartmark.model.chat;

import com.yikang.heartmark.constant.Constants;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by guangdye on 2014/8/28.
 */
public class Media implements Serializable {
    private long userId = 0;     //用户id
    private String uploader = ""; // 用户名
    private String fileName = "";
    private String fullName = "";
    private double latitude = 0.0;    // gps坐标 经度
    private double longitude = 0.0;   // gps坐标纬度
    private String mimeType = "";   // 文件类型:  图片image/jpeg 

    private String objectId;//下载的表示下载的id
    private long objectTime = 0; //文件时间
    private long objectSize;
    private String thumbnailImageId = "";
    private String largeImageId = "";
    private int duration = 0;//时长
    private int imageStyle = Constants.Chat.CHAT_IMAGE_STYLE_LARGE;//图片类型//large, raw

    private String jsonLocation = "";    // 终端本地获取地址json格式 { “country”:”国家”,“province”:”省”,“city:””市”,“district”:”区”,“street”:”街道”}
    private transient byte[] content = null;

    private int messageType = Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE;
    private int transType = Constants.Chat.CHAT_FILE_UPLOAD;
    private String contentText;
    private long sessionId;
    private String messageId;
    private String fileExtension;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(int imageStyle) {
        this.imageStyle = imageStyle;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getObjectTime() {
        return objectTime;
    }

    public void setObjectTime(long objectTime) {
        this.objectTime = objectTime;
    }

    public String getJsonLocation() {
        return jsonLocation;
    }

    public void setJsonLocation(String jsonLocation) {
        this.jsonLocation = jsonLocation;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public void setObjectSize(long objectSize) {
        this.objectSize = objectSize;
    }

    public String getThumbnailImageId() {
        return thumbnailImageId;
    }

    public void setThumbnailImageId(String thumbnailImageId) {
        this.thumbnailImageId = thumbnailImageId;
    }

    public String getLargeImageId() {
        return largeImageId;
    }

    public void setLargeImageId(String largeImageId) {
        this.largeImageId = largeImageId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getLocalThumbnailFileName() {
        return thumbnailImageId + File.separator + fileExtension;
    }

    public String getLocalLargeImageFileName() {
        return largeImageId + File.separator + fileExtension;
    }

    public String getLocalObjectFileName() {
        return objectId + File.separator + fileExtension;
    }

    @Override
    public String toString() {
        return "Media{" +
                "userId=" + userId +
                ", uploader='" + uploader + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", duration=" + duration +
                ", imageStyle='" + imageStyle + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", objectTime=" + objectTime +
                ", jsonLocation='" + jsonLocation + '\'' +
                ", content=" + Arrays.toString(content) +
                ", messageType=" + messageType +
                ", objectId='" + objectId + '\'' +
                ", transType=" + transType +
                ", objectSize=" + objectSize +
                '}';
    }
}