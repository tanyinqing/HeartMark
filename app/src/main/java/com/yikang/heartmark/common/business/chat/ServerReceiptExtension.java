package com.yikang.heartmark.common.business.chat;

import org.jivesoftware.smack.packet.PacketExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chang on 12/22/2014.
 */
public class ServerReceiptExtension implements PacketExtension {
    private static Logger logger = LoggerFactory.getLogger(ServerReceiptExtension.class);

    public static final String NAMESPACE = "urn:kanebay:receipts";
    public static final String ELEMENT_NAME = "received-extension";

    private String elementName = "";
    private String namespace = "";

    private String messageId = "";
    private String content = "";

    public ServerReceiptExtension() {
        this.elementName = ELEMENT_NAME;
        this.namespace = NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public CharSequence toXML() {
        return "<received-extension xmlns='urn:kanebay:receipts'><id>" + messageId + "</id><type>" + content + "</type></received-extension>";
    }

//    public static class Provider extends org.jivesoftware.smack.provider.EmbeddedExtensionProvider {
//        public Provider() { /* compiled code */ }
//
//        protected org.jivesoftware.smack.packet.PacketExtension createReturnExtension(java.lang.String s, java.lang.String s1, java.util.Map<java.lang.String,java.lang.String> map, java.util.List<? extends org.jivesoftware.smack.packet.PacketExtension> list) { /* compiled code */ }
//    }


}
