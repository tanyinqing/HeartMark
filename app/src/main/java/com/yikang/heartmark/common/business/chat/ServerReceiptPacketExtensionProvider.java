package com.yikang.heartmark.common.business.chat;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Chang on 12/22/2014.
 */
public class ServerReceiptPacketExtensionProvider implements PacketExtensionProvider {
    //private static Logger logger = LoggerFactory.getLogger(ServerReceiptPacketExtensionProvider.class);

    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {

        ServerReceiptExtension extension = new ServerReceiptExtension();
        String tagName = "";
        String tagValue = "";

        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.getName();
                //extension.setMessageId(parser.getAttributeValue(null, "id"));
                //break;
            } else if (eventType == XmlPullParser.TEXT) {
                if (tagName.equals("id"))
                    extension.setMessageId(parser.getText());
                else if (tagName.equals("type"))
                    extension.setContent(parser.getText());
//                extension.setContent(parser.getText());
                //break;
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(ServerReceiptExtension.ELEMENT_NAME)) {
                    break;
                }
            }
        }

        return extension;
    }


}
