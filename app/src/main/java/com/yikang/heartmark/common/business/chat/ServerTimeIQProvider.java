package com.yikang.heartmark.common.business.chat;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Chang on 9/29/2014.
 */
public class ServerTimeIQProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        final StringBuffer buffer = new StringBuffer();

        // skip the <query> tag by calling parser.next()
        while (true) {
            switch (parser.next()) {
                case XmlPullParser.TEXT:
                    // We need to escape characters like & and <
                    buffer.append(parser.getText());
                    break;

                case XmlPullParser.START_TAG:
                    buffer.append('<' + parser.getName() );
                    for( int i=0,count=  parser.getAttributeCount();i< count;i++){
                        buffer.append( " "+ parser.getAttributeName(i)+"='"+parser.getAttributeValue(i)+"'" );
                    }
                    buffer.append('>');
                    break;

                case XmlPullParser.END_TAG:

                    if ("query".equals(parser.getName())) {
                        return new ServerTimeIQ("<query xmlns='http://kanebay.com/protocol/server#time'>"+buffer.toString().trim()+"</query>");
                    }else {
                        buffer.append("</" + parser.getName() + '>');
                    }
                    break;
                default:
            }
        }
    }

}
