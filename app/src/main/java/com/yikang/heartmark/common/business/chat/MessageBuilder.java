package com.yikang.heartmark.common.business.chat;


import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.util.Base64Util;
import com.yikang.heartmark.common.util.BytesUtil;
import com.yikang.heartmark.common.util.TimeUtil;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ChatError;
import com.yikang.heartmark.model.chat.ChatMessage;
import com.yikang.heartmark.model.chat.ChatSession;
import com.yikang.heartmark.model.chat.Media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Chang on 12/30/2014.
 */
public class MessageBuilder {
    private static Logger logger = LoggerFactory.getLogger(MessageBuilder.class);


    //for message from receipt
    public static ChatMessage buildMessageFromServerReceipt(long toId, ServerReceiptExtension serverReceiptExtension, String recepitsId) {
        ChatMessage message = buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_TEXT, 0, toId, null, serverReceiptExtension.getMessageId(),
                Constants.Chat.CHAT_DIRECTION_SEND, false,
                Constants.Chat.CHAT_SEND_STATUS_OFFLINE, 0, null);

        ChatMessage.MessageReceipt receipt = message.createMessageReceipt();
        receipt.setReceiptsId(recepitsId);
        receipt.setReason(serverReceiptExtension.getContent());

        message.setReceipt(receipt);
        message.setFromUserId(Constants.Chat.CHAT_FROM_SERVER_ID);
        //dont need to set receivedTime, bc msg is still in offline storage
        return message;
    }

    //for reatime and offline receipt,
    public static ChatMessage buildMessageFromReceipt(long toId, long fromId, String messageId, String recepitsId) {
        ChatMessage message = buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_TEXT, 0, toId, null, messageId, Constants.Chat.CHAT_DIRECTION_SEND, false,
                Constants.Chat.CHAT_SEND_STATUS_RECEIVED, 0, "");

        ChatMessage.MessageReceipt receipt = message.createMessageReceipt();
        receipt.setReceiptsId(recepitsId);

        message.setReceipt(receipt);
        message.setFromUserId(fromId);
        message.setReceiveTime(getLastMsgTime(System.currentTimeMillis()));
        return message;
    }


    //for received message
    public static ChatMessage buildReceivedMessage(int messageType, long toId, long fromId, String messageId, String content, long sendTime) {
        ChatMessage chatMessageX = buildMessage(messageType, 0, toId, content, messageId, Constants.Chat.CHAT_DIRECTION_RECEIVE, false,
                Constants.Chat.CHAT_SEND_STATUS_RECEIVED, sendTime, "");
        chatMessageX.setFromUserId(fromId);
        return chatMessageX;
    }

    //for new text message
    public static ChatMessage buildNewTextMessage(ChatSession session, String content) {
        return buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_TEXT, session.getSessionId(), session.getUserId(), content, getPacketId(), Constants.Chat.CHAT_DIRECTION_SEND, false,
                Constants.Chat.CHAT_SEND_STATUS_SENT, System.currentTimeMillis(), session.getUserProfilePictureId());
    }

    //for resend text message
    public static ChatMessage buildResendTextMessage(ChatSession session, String content, String messageId) {
        return buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_TEXT, session.getSessionId(), session.getUserId(), content, messageId, Constants.Chat.CHAT_DIRECTION_SEND, true,
                Constants.Chat.CHAT_SEND_STATUS_SENT, System.currentTimeMillis(), session.getUserProfilePictureId());
    }

    //for new image message
    public static ChatMessage buildNewImageMessage(ChatSession session, String filePath, int imageStyle, long objectSize, boolean isResend) {
        ChatMessage message = buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE, session.getSessionId(), session.getUserId(), Constants.Chat.CHAT_LAST_MSG_IMAGE_TEXT, getPacketId(), Constants.Chat.CHAT_DIRECTION_SEND, isResend,
                Constants.Chat.CHAT_SEND_STATUS_SENT, System.currentTimeMillis(), session.getUserProfilePictureId());

        Media media = MessageBuilder.buildImageMedia(filePath, imageStyle, objectSize);
        media.setSessionId(session.getSessionId());
        media.setMessageId(message.getMessageId());
        message.setMedia(media);
        return message;
    }

    public static ChatMessage buildResendImageMessage(String messageId, ChatSession session, String filePath, int imageStyle, long objectSize, boolean isResend) {
        ChatMessage message = buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE, session.getSessionId(), session.getUserId(), Constants.Chat.CHAT_LAST_MSG_IMAGE_TEXT, messageId, Constants.Chat.CHAT_DIRECTION_SEND, isResend,
                Constants.Chat.CHAT_SEND_STATUS_SENT, System.currentTimeMillis(), session.getUserProfilePictureId());

        Media media = MessageBuilder.buildImageMedia(filePath, imageStyle, objectSize);
        media.setSessionId(session.getSessionId());
        media.setMessageId(message.getMessageId());
        message.setMedia(media);
        return message;
    }

    public static ChatMessage buildNewAudioMessage(ChatSession session, String filePath, int duration, long objectSize, String mineType, boolean isResend) {
        ChatMessage message = null;
        if (!isResend) {
            message = buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO, session.getSessionId(), session.getUserId(), Constants.Chat.CHAT_LAST_MSG_AUDIO_TEXT, getPacketId(), Constants.Chat.CHAT_DIRECTION_SEND, isResend,
                    Constants.Chat.CHAT_SEND_STATUS_SENT, System.currentTimeMillis(), session.getUserProfilePictureId());
        }

        Media media = MessageBuilder.buildAudioMedia(filePath, duration, objectSize, mineType);
        media.setSessionId(session.getSessionId());
        media.setMessageId(message.getMessageId());
        media.setDuration(duration);
        message.setMedia(media);
        return message;
    }

    public static ChatMessage buildResendAudioMessage(String messageId, ChatSession session, String filePath, int duration, long objectSize, String mineType) {
        ChatMessage message = buildMessage(Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO, session.getSessionId(), session.getUserId(), Constants.Chat.CHAT_LAST_MSG_AUDIO_TEXT, messageId, Constants.Chat.CHAT_DIRECTION_SEND, true,
                Constants.Chat.CHAT_SEND_STATUS_SENT, System.currentTimeMillis(), session.getUserProfilePictureId());

        Media media = MessageBuilder.buildAudioMedia(filePath, duration, objectSize, mineType);
        media.setSessionId(session.getSessionId());
        media.setMessageId(message.getMessageId());
        media.setDuration(duration);
        message.setMedia(media);
        return message;
    }

    private static ChatMessage buildMessage(int messageType, long sessionId, long toId, String content, String messageId, int direction,
                                            boolean isResend, int status, long sendTimestamp, String userProfilePictureId) {

        ChatMessage message = new ChatMessage(messageId, toId, content);
        message.setDirection(direction);
        message.setResend(isResend);
        message.setSessionId(sessionId);
        message.setHostUserId(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId());
        message.setMessageType(messageType);
        message.setSendStatus(status);

        //in case we don't want to set time
        if (sendTimestamp > 0) message.setSendTime(getLastMsgTime(sendTimestamp));

        String picId = AppContext.getAppContext().getCurrentUser().getUserInfo().getProfilePictureThumbnailId();

        if (picId.equals("")) {
            message.setHostProfilePicId(String.valueOf(AppContext.getAppContext().getCurrentUser().getUserInfo().getGender()));
        } else {
            message.setHostProfilePicId(AppContext.getAppContext().getCurrentUser().getUserInfo().getProfilePictureThumbnailId());
        }
        message.setGuestProfilePicId(userProfilePictureId);

        return message;
    }

    private static String getPacketId() {
        UUID uuid = UUID.randomUUID();
        byte[] buf = BytesUtil.uuid2BytesNew(uuid);
        String b64 = Base64Util.encode(buf);
        if (b64.length() == 25) {
            b64 = b64.substring(0, 24);
        }
        return b64;
    }

    private static Date getLastMsgTime() throws ParseException {
        long adjustServerTime = TimeUtil.getAdjustDateTimeVal(TimeUtil.toUTC(new Date(System.currentTimeMillis())).getTime());
        return new Date(adjustServerTime);
    }

    private static Date getLastMsgTime(long timeStamp) {
        try {
            long adjustServerTime = TimeUtil.getAdjustDateTimeVal(TimeUtil.toUTC(new Date(timeStamp)).getTime());
            return new Date(adjustServerTime);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return new Date();
        }
    }

    public static String getFormatedJID(long userId, String host, String deviceType) {
        return userId + "@" + host; // + "/" + deviceType; //AppContext.getAppContext().getDeviceInfo().getDeviceType();
    }

    public static ChatError buildError(int errorType, String errorMsg) {
        return buildError(errorType, new RuntimeException(errorMsg));
    }

    public static ChatError buildError(int errorType, Exception ex) {
        ChatError error = new ChatError(errorType);
        error.setException(ex);
        return error;
    }

    public static MediaPacket buildMediaPacket(Media media) {

        if (media == null)
            return null;

        MediaPacketExtension extension = new MediaPacketExtension(MediaPacketExtension.ELEMENT_NAME, MediaPacketExtension.NAMESPACE, MediaPacketExtension.SUB_ELEMENT_NAME);
        extension.setObjectId(media.getObjectId());
        extension.setObjectSize(media.getObjectSize());

        if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE) {
            extension.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE);
            extension.setImgStyle(media.getImageStyle());
            extension.setThumbnailImgId(media.getThumbnailImageId());
            extension.setLargeImgId(media.getLargeImageId());
        } else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO) {
            extension.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO);
            extension.setDuration(media.getDuration());
        } else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO) {
            extension.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO);
            extension.setDuration(media.getDuration());
            extension.setThumbnailImgId(media.getThumbnailImageId());
        }

        MediaPacket mediaPacket = new MediaPacket();
        mediaPacket.setExtension(extension);
        return mediaPacket;
    }

    public static Media buildImageMedia(String filePath, int imageStyle, long objectSize) {

        Media media = new Media();
        media.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE);
        media.setContentText(Constants.Chat.CHAT_LAST_MSG_IMAGE_TEXT);
        media.setTransType(Constants.Chat.CHAT_FILE_UPLOAD);
        media.setMimeType(Constants.Chat.CHAT_MIME_TYPE_IMAGE);
        media.setUploader(AppContext.getAppContext().getCurrentUser().getUserInfo().getNickName());
        media.setUserId(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId());
        media.setFullName(filePath);
        media.setImageStyle(imageStyle);
        media.setObjectSize(objectSize);
        media.setObjectTime(System.currentTimeMillis());
        media.setFileName(getFileName(filePath));
        media.setFileExtension(getFileExtension(media.getFileName()));
        return media;
    }

    public static Media buildAudioMedia(String filePath, int duration, long objectSize, String mineType) {

        Media media = new Media();
        media.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO);
        media.setContentText(Constants.Chat.CHAT_LAST_MSG_AUDIO_TEXT);
        media.setTransType(Constants.Chat.CHAT_FILE_UPLOAD);
        media.setMimeType(mineType);
        media.setUploader(AppContext.getAppContext().getCurrentUser().getUserInfo().getNickName());
        media.setUserId(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId());
        media.setFullName(filePath);
        media.setDuration(duration);
        media.setObjectSize(objectSize);
        media.setObjectTime(System.currentTimeMillis());
        media.setFileName(getFileName(filePath));
        media.setFileExtension(getFileExtension(media.getFileName()));

        return media;
    }

    public static Media buildVideoMedia(String filePath, int duration, long objectSize) {

        Media media = new Media();
        media.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO);
        media.setContentText(Constants.Chat.CHAT_LAST_MSG_VIDEO_TEXT);
        media.setTransType(Constants.Chat.CHAT_FILE_UPLOAD);
        media.setMimeType(Constants.Chat.CHAT_MIME_TYPE_MP4);
        media.setUploader(AppContext.getAppContext().getCurrentUser().getUserInfo().getNickName());
        media.setUserId(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId());
        media.setFullName(filePath);
        media.setDuration(duration);
        media.setObjectSize(objectSize);
        media.setObjectTime(System.currentTimeMillis());
        media.setFileName(getFileName(filePath));
        media.setFileExtension(getFileExtension(media.getFileName()));

        return media;
    }

    public static Media buildMedia(MediaPacketExtension mediaExtension) {

        if (mediaExtension != null) {
            Media media = new Media();
            media.setMessageType(mediaExtension.getMessageType());
            media.setObjectId(mediaExtension.getObjectId());
            media.setObjectSize((int) mediaExtension.getObjectSize());

            if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE) {
                media.setImageStyle(mediaExtension.getImgStyle());
                media.setThumbnailImageId(mediaExtension.getThumbnailImgId());
                media.setLargeImageId(mediaExtension.getLargeImgId());
                media.setContentText(Constants.Chat.CHAT_LAST_MSG_IMAGE_TEXT);
            } else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO) {
                media.setDuration(mediaExtension.getDuration());
                media.setContentText(Constants.Chat.CHAT_LAST_MSG_AUDIO_TEXT);
            } else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO) {
                media.setDuration(mediaExtension.getDuration());
                media.setThumbnailImageId(mediaExtension.getThumbnailImgId());
                media.setContentText(Constants.Chat.CHAT_LAST_MSG_VIDEO_TEXT);
            }
            return media;
        }

        return null;
    }

    private static String getFileName(String fullName) {
        if (fullName == null || fullName.isEmpty())
            return "";

        int pos = fullName.lastIndexOf(File.separator);
        if (pos > 0)
            return fullName.substring(pos + 1);
        else
            return fullName;
    }

    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty())
            return "";

        int pos = fileName.lastIndexOf(".");
        if (pos > 0)
            return fileName.substring(pos + 1);
        else
            return "";
    }
}
