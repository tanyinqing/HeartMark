package com.yikang.heartmark.common.business.chat;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Created by Chang on 12/22/2014.
 */
public class MediaPacket extends Packet {
    PacketExtension extension;
    Message message;

    public  MediaPacket() {
    }
    public  MediaPacket(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public PacketExtension getExtension() {
        return extension;
    }

    public void setExtension(PacketExtension extension) {
        this.extension = extension;
    }

    @Override
    public CharSequence toXML() {
        if (extension==null){
            return message.toXML();
        }else {
            String pid = this.message.getPacketID();
            String toId = this.message.getTo();
            String fromId = this.message.getFrom();
            this.setTo(toId);
            this.setFrom(fromId);
            //String fromId = this.getFrom();
            String xml =  "<message id=\"" + pid + "\" to=\"" + toId +  "\" type=\"chat\">" +
                    extension.toXML() + "<request xmlns=\"urn:xmpp:receipts\"/></message>";

            CharSequence charSequence = new StringBuffer(xml);
            return charSequence;

        }
    }
}
