package com.yikang.heartmark.common.business.chat;


import org.jivesoftware.smack.packet.IQ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chang on 9/29/2014.
 */
public class MessageStateIQ extends IQ {

    private final String xml;
    private String fromId;
    private String toId;
    private String packetId;
    private  int code;

    static final String matchPattern = "<message from='(\\w+)' to='(\\w+)' id='([=\\w\\+\\- /]+)' code='(\\d+)'></message></query>";
    static Pattern pattern = Pattern.compile(matchPattern);

    public MessageStateIQ(final String xml) {
        this.xml = xml;

        Matcher matcher = pattern.matcher(xml);
        while(matcher.find())
        {
            String s= matcher.group() ;
            int c = matcher.groupCount();
            if (matcher.groupCount()==4){
                fromId = matcher.group(1);
                toId = matcher.group(2);
                packetId = matcher.group(3);
                code = Integer.valueOf(matcher.group(4));
            }
        }

    }

    public String getChildElementXML() {
        return this.xml;
    }

    public String getFromId() {
        return this.fromId;
    }

    public String getToId() {
        return this.toId;
    }

    public String getPacketId() {
        return this.packetId;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "<OfflineMessageIQ>from=" + fromId +" to=" +toId +" packetId=" + packetId + " code=" + code +"</OfflineMessageIQ>";
    }
}
