package com.yikang.heartmark.model.chat;


import com.yikang.heartmark.common.util.TimeUtil;
import com.yikang.heartmark.constant.Constants;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Chang on 9/19/2014.
 */
public class ChatMessage implements Serializable {

    private long autoId;
    private long sessionId = 0;
    private String messageId = "";
    private long hostUserId = 0;
    private long toUserId = 0;
    private long fromUserId = 0;
    private String content = "";
    private Date sendTime = new Date(TimeUtil.getAdjustDateTimeVal(TimeUtil.toUTC(new Date(System.currentTimeMillis())).getTime()));
    private Date receiveTime = new Date(TimeUtil.getAdjustDateTimeVal(TimeUtil.toUTC(new Date(System.currentTimeMillis())).getTime()));
    private boolean isRead = false;
    private Integer sendStatus = Constants.Chat.CHAT_SEND_STATUS_SENT;
    private int direction = Constants.Chat.CHAT_DIRECTION_SEND;
    private int messageType = Constants.Chat.CHAT_MESSAGE_TYPE_TEXT;

    //Chang just a handy storage for image Id
    private String hostProfilePicId = "";
    private String guestProfilePicId = "";
    private String key = "";
    private int queryTryCount = 0;
    private long createdTimeStamp = System.currentTimeMillis();
    private String hostGender = "1";//加上性别
    private String guestGender = "1";//加上性别

    private Date dispatchTime = new Date(TimeUtil.getAdjustDateTimeVal(TimeUtil.toUTC(new Date(System.currentTimeMillis())).getTime()));
    private boolean resend;
    private Media media = null;//new Media();

    private MessageReceipt receipt = null;

    public long getElapsed() {
        return System.currentTimeMillis() - createdTimeStamp;
    }

    public class MessageReceipt {
        private String receiptsId;
        private String reason;

        public String getReceiptsId() {
            return receiptsId;
        }

        public void setReceiptsId(String receiptsId) {
            this.receiptsId = receiptsId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public ChatMessage() {
    }

    public ChatMessage(String messageId, long toId) {
        this.messageId = messageId;
        this.toUserId = toId;
    }

    public ChatMessage(String messageId, long toId, String content) {
        this.messageId = messageId;
        this.toUserId = toId;
        this.content = content;
    }

    public MessageReceipt createMessageReceipt() {
        return new MessageReceipt();
    }

    public void setHostGender(String hostGender) {
        this.hostGender = hostGender;
    }

    public void setGuestGender(String guestGender) {
        this.guestGender = guestGender;
    }

    public String getHostGender() {
        return hostGender;
    }

    public String getGuestGender() {
        return guestGender;
    }

    public long getAutoId() {
        return autoId;
    }

    public void setAutoId(long autoId) {
        this.autoId = autoId;
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

    public long getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(long hostUserId) {
        this.hostUserId = hostUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getHostProfilePicId() {
        return hostProfilePicId;
    }

    public void setHostProfilePicId(String hostProfilePicId) {
        this.hostProfilePicId = hostProfilePicId;
    }

    public String getGuestProfilePicId() {
        return guestProfilePicId;
    }

    public void setGuestProfilePicId(String guestProfilePicId) {
        this.guestProfilePicId = guestProfilePicId;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public int getQueryTryCount() {
        return queryTryCount;
    }

    public void setQueryTryCount(int queryTryCount) {
        this.queryTryCount = queryTryCount;
    }

    public boolean isResend() {
        return resend;
    }

    public void setResend(boolean resend) {
        this.resend = resend;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public MessageReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(MessageReceipt receipt) {
        this.receipt = receipt;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "autoId=" + autoId +
                ", sessionId=" + sessionId +
                ", messageId='" + messageId + '\'' +
                ", hostUserId=" + hostUserId +
                ", toUserId=" + toUserId +
                ", fromUserId=" + fromUserId +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", receiveTime=" + receiveTime +
                ", isRead=" + isRead +
                ", sendStatus=" + sendStatus +
                ", direction=" + direction +
                ", messageType=" + messageType +
                ", hostProfilePicId='" + hostProfilePicId + '\'' +
                ", guestProfilePicId='" + guestProfilePicId + '\'' +
                ", key='" + key + '\'' +
                ", queryTryCount=" + queryTryCount +
                ", hostGender='" + hostGender + '\'' +
                ", guestGender='" + guestGender + '\'' +
                ", dispatchTime=" + dispatchTime +
                '}';
    }
}
