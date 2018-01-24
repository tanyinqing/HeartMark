package com.yikang.heartmark.common.business.chat;


import com.yikang.heartmark.model.chat.ChatError;
import com.yikang.heartmark.model.chat.ChatMessage;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by Chang on 9/19/2014.
 */
public interface IClientChat {

    boolean isConnectionClosed();
    boolean isConnectionValid();
    void setConnectionClosed(boolean value);

    XMPPConnection getConnection();

    void login(final String host, final int port, final long userId, final String password);
    void disconnect();
    void listenIncomingMessages();
    void sendServerTimeIQ();
    void sendMessageIQ(long fromId, long toId, String messageId);

    void setXmppListener(XmppListener callback);

    void sendMessage(ChatMessage chatMessage);

    public interface XmppListener {
        void onLogin();
        void onServerTime(String tm);
        void onIQMessage(MessageStateIQ messageState);
        void onReceiveMessage(ChatMessage chatMessage);
        void onReceiveReceipt(ChatMessage chatMessage);
        void onXmppError(long sessionId, String messageId, ChatError chatError);
    }
}


