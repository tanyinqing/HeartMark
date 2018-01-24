package com.yikang.heartmark.common.business.chat;


import com.yikang.heartmark.constant.Constants;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Chang on 12/22/2014.
 */
public class MediaPacketExtensionProvider implements PacketExtensionProvider {
    private static Logger logger = LoggerFactory.getLogger(MediaPacketExtensionProvider.class);

    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {

        MediaPacketExtension extension = new MediaPacketExtension(MediaPacketExtension.ELEMENT_NAME, MediaPacketExtension.NAMESPACE, MediaPacketExtension.SUB_ELEMENT_NAME);
        String tagName = "";
        String tagValue = "";

        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.getName();
            } else if (eventType == XmlPullParser.TEXT) {
                tagValue = parser.getText();
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(MediaPacketExtension.ELEMENT_NAME)) {
                    break;
                } else {
                    if (tagName.equals(MediaPacketExtension.TAG_TYPE)) {
                        if (tagValue.equals(Constants.Http.HTTP_MESSAGE_TYPE_IMAGE)) {
                            extension.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE);
                        } else if (tagValue.equals(Constants.Http.HTTP_MESSAGE_TYPE_AUDIO)) {
                            extension.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO);
                        } else if (tagValue.equals(Constants.Http.HTTP_MESSAGE_TYPE_VIDEO)) {
                            extension.setMessageType(Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO);
                        } else {
                            logger.error("invaid message type from received message");
                            return null;
                        }
                    } else if (tagName.equals(MediaPacketExtension.TAG_OBJECT_ID)) {
                        extension.setObjectId(tagValue);
                    } else if (tagName.equals(MediaPacketExtension.TAG_OBJECT_SIZE)) {
                        extension.setObjectSize(Long.valueOf(tagValue));
                    } else if (tagName.equals(MediaPacketExtension.TAG_DURATION)) {
                        extension.setDuration(Integer.valueOf(tagValue));
                    } else if (tagName.equals(MediaPacketExtension.TAG_THUMBNAIL_IMG_ID)) {
                        extension.setThumbnailImgId(tagValue);
                    } else if (tagName.equals(MediaPacketExtension.TAG_LARGE_IMG_ID)) {
                        extension.setLargeImgId(tagValue);
                    } else if (tagName.equals(MediaPacketExtension.TAG_IMG_STYLE)) {
                        if (tagValue.equals(Constants.Http.HTTP_IMAGE_STYLE_RAW))
                            extension.setImgStyle(Constants.Chat.CHAT_IMAGE_STYLE_RAW);
                        else
                            extension.setImgStyle(Constants.Chat.CHAT_IMAGE_STYLE_LARGE);
                    }
                }
            }
        }

        return extension;
    }
}
