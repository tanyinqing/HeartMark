package com.yikang.heartmark.common.business.chat;


import com.yikang.heartmark.constant.Constants;

import org.jivesoftware.smack.packet.PacketExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chang on 12/22/2014.
 */
public class MediaPacketExtension implements PacketExtension {
    private static Logger logger = LoggerFactory.getLogger(MediaPacketExtension.class);

    public static final String NAMESPACE = "jabber:kanebay:extbody:oob";
    public static final String ELEMENT_NAME = "body-extension";
    public static final String SUB_ELEMENT_NAME = "attach";

    public static final String TAG_TYPE = "type";
    public static final String TAG_OBJECT_ID = "object-id";
    public static final String TAG_OBJECT_SIZE = "object-size";
    public static final String TAG_IMG_STYLE = "img-style";
    public static final String TAG_THUMBNAIL_IMG_ID = "thumbnail-img-id";
    public static final String TAG_LARGE_IMG_ID = "large-img-id";
    public static final String TAG_DURATION = "duration";

    private String elementName = "";	//body-extension
    private String subElementName = "";	//attach
    private String namespace = "";	//jabber:kanebay:extbody:oob

    private int messageType = 0;	//1=text,2=image,3=audio,4=vedio
    private long objectSize = 0;
    private int duration = 0;
    private String objectId = "";

    private int imgStyle = 0;
    private String thumbnailImgId = "";
    private String largeImgId = "";

    public MediaPacketExtension(String elementName, String namespace, String subElementName) {
        this.elementName = elementName;
        this.namespace = namespace;
        this.subElementName = subElementName;
    }

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public CharSequence toXML() {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append("<").append(elementName).append(" xmlns='").append(namespace).append("'>");
            String httpMsgType = Constants.Http.HTTP_MESSAGE_TYPE_TEXT;
            if (Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE == messageType)
                httpMsgType = Constants.Http.HTTP_MESSAGE_TYPE_IMAGE;
            else if (Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO == messageType)
                httpMsgType = Constants.Http.HTTP_MESSAGE_TYPE_AUDIO;
            else if (Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO == messageType)
                httpMsgType = Constants.Http.HTTP_MESSAGE_TYPE_VIDEO;

            buf.append("<").append(TAG_TYPE).append(">").append(httpMsgType).append("</").append(TAG_TYPE).append(">");
            buf.append("<").append(subElementName).append(">");
            buf.append("<").append(TAG_OBJECT_ID).append(">").append(objectId).append("</").append(TAG_OBJECT_ID).append(">");
            buf.append("<").append(TAG_OBJECT_SIZE).append(">").append(objectSize).append("</").append(TAG_OBJECT_SIZE).append(">");

            if (Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE == messageType) {
                String httpImageStyle = imgStyle == Constants.Chat.CHAT_IMAGE_STYLE_LARGE ? Constants.Http.HTTP_IMAGE_STYLE_LARGE : Constants.Http.HTTP_IMAGE_STYLE_RAW;
                buf.append("<").append(TAG_IMG_STYLE).append(">").append(httpImageStyle).append("</").append(TAG_IMG_STYLE).append(">");
                buf.append("<").append(TAG_THUMBNAIL_IMG_ID).append(">").append(thumbnailImgId).append("</").append(TAG_THUMBNAIL_IMG_ID).append(">");
                buf.append("<").append(TAG_LARGE_IMG_ID).append(">").append(largeImgId).append("</").append(TAG_LARGE_IMG_ID).append(">");

            } else if (Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO == messageType) {

                buf.append("<").append(TAG_DURATION).append(">").append(duration).append("</").append(TAG_DURATION).append(">");
            } else if (Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO == messageType) {

                buf.append("<").append(TAG_THUMBNAIL_IMG_ID).append(">").append(thumbnailImgId).append("</").append(TAG_THUMBNAIL_IMG_ID).append(">");
                buf.append("<").append(TAG_DURATION).append(">").append(duration).append("</").append(TAG_DURATION).append(">");
            } else {
                logger.error("Invalid message type:" + messageType);
                return null;
            }

            buf.append("</").append(subElementName).append(">");
            buf.append("</").append(elementName).append(">");
            return new StringBuffer(buf.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

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

    public String getThumbnailImgId() {
        return thumbnailImgId;
    }

    public void setThumbnailImgId(String thumbnailImgId) {
        this.thumbnailImgId = thumbnailImgId;
    }

    public String getLargeImgId() {
        return largeImgId;
    }

    public void setLargeImgId(String largeImgId) {
        this.largeImgId = largeImgId;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public void setObjectSize(long objectSize) {
        this.objectSize = objectSize;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getImgStyle() {
        return imgStyle;
    }

    public void setImgStyle(int imgStyle) {
        this.imgStyle = imgStyle;
    }

}
