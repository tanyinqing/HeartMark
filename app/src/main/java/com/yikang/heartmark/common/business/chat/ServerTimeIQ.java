package com.yikang.heartmark.common.business.chat;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by Chang on 9/29/2014.
 */
public class ServerTimeIQ  extends IQ {

    private final String xml;
    private  String serverTime;

    public ServerTimeIQ(final String xml) {
        this.xml = xml;
    }

    public String getChildElementXML() {
        return this.xml;
    }

    public String getServerTime() {
        final String serverTagBegin = "<server time='";
        int startPos = this.xml.indexOf(serverTagBegin);
        int endPos = this.xml.indexOf("'></server>");
        if (startPos>0 && endPos>startPos){
            serverTime = this.xml.substring(startPos+ + serverTagBegin.length(),endPos);
            return  serverTime;
        }
        return "";
    }
}
