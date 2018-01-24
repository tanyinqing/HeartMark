package com.yikang.heartmark.common.business.chat;

import android.os.AsyncTask;
import android.os.Bundle;

import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ChatMessage;
import com.yikang.heartmark.model.chat.Media;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chang on 9/19/2014.
 */
public class ClientChatXmppImpl implements  IClientChat {

    private static Logger logger = LoggerFactory.getLogger(ClientChatXmppImpl.class);

    private String serverAddress;
    private XMPPConnection connection;
    private  boolean connectionClosed;
    private XmppListener xmppListener = null;

    static class ClientChatHolder { static IClientChat clientChat = new ClientChatXmppImpl(); }

    public static IClientChat getInstance()
    {
        return ClientChatHolder.clientChat;
    }

    private ClientChatXmppImpl(){
        ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
        ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, new DeliveryReceiptRequest().getNamespace(), new DeliveryReceiptRequest.Provider());
        ProviderManager.addIQProvider("query", "http://kanebay.com/protocol/server#time", new ServerTimeIQProvider());
        ProviderManager.addIQProvider("query", "http://kanebay.com/protocol/offlinemessage#exist", new MessageStateIQProvider());
        ProviderManager.addExtensionProvider(MediaPacketExtension.ELEMENT_NAME, MediaPacketExtension.NAMESPACE, new MediaPacketExtensionProvider());
        ProviderManager.addExtensionProvider(ServerReceiptExtension.ELEMENT_NAME, ServerReceiptExtension.NAMESPACE, new ServerReceiptPacketExtensionProvider());
    }

    @Override
    public void disconnect() {
        try {
            if (connection == null || !connection.isConnected()) {return;}

            connection.disconnect(); // todo NetworkOnMainThreadException
        } catch (SmackException.NotConnectedException e) {
            logger.error(e.toString(),e);
        }
    }

    @Override
    public void login(final String host, final int port, final long userId, final String password) {
        this.serverAddress = host;

        //setup providers
        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... arg0) {
                boolean isConnected = false;

                try
                {
                    ConnectionConfiguration config = new ConnectionConfiguration(serverAddress, port);
                    config.setReconnectionAllowed(true);
                    //设置成disabled，则不会去验证服务器证书是否有效，默认为enabled
                    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                    //SASLAuthentication.supportSASLMechanism("PLAIN",0);
                    //设置可以调试，默认为false，老版本的写法为XMPPConnection.DEBUG_ENABLED = true;
                    config.setDebuggerEnabled(true);
                    //设置是否在登陆的时候告诉服务器，默认为true
                    //need to set to false to retrieve offline messages
                    config.setSendPresence(true);
                    connection = new XMPPTCPConnection(config);
                    //设置等待时间
                    //connection.setPacketReplyTimeout(5000);
                    //enable autoReceipt
                    DeliveryReceiptManager.getInstanceFor(connection).enableAutoReceipts();
                    DeliveryReceiptManager.getInstanceFor(connection).setAutoReceiptsEnabled(true);
                    //Chang 10-09 好像没有什么特别的用处， 具体回执已经在消息接听代码处理了
                    //DeliveryReceiptManager.getInstanceFor(connection).addReceiptReceivedListener(new SimpleReceiptReceivedListener());

                    connection.addConnectionListener(new XMPPConnectionListener(ClientChatXmppImpl.getInstance(),host ,port, userId, password));
                    connection.connect();
                    connection.login(String.valueOf(userId), password);
                    xmppListener.onLogin();

                    sendServerTimeIQ();
                    isConnected = true;

                    logger.debug("Complete login process host=" + host + ",port=" + port + ", user=" + userId);
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                    xmppListener.onXmppError(0,null,MessageBuilder.buildError(Constants.Error.XMPP_LOGIN_ERROR, e));
                }

                return isConnected;
            }
        };

        connectionThread.execute();
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        AsyncTask<ChatMessage, Void, Void> connectionThread = new AsyncTask<ChatMessage, Void, Void>() {
            @Override
            protected Void doInBackground(ChatMessage... arg0) {
                ChatMessage chatMessage = arg0[0];

                try {
                    if (isConnectionClosed()) {
                        logger.debug("isConnectionClosed = TRUE. Failed to sent out message to " + chatMessage.getToUserId() + ", msg = " + chatMessage.getContent());
                        xmppListener.onXmppError(chatMessage.getSessionId(),chatMessage.getMessageId(), MessageBuilder.buildError(Constants.Error.XMPP_CONNECTION_ERROR, "xmpp connection is closed"));
                        return null;
                    } else if (connection == null || !connection.isConnected()) {
                        logger.debug("Connection is null or closed. Failed to sent out message to " + chatMessage.getToUserId() + ", msg = " + chatMessage.getContent());
                        xmppListener.onXmppError(chatMessage.getSessionId(),chatMessage.getMessageId(),MessageBuilder.buildError(Constants.Error.XMPP_CONNECTION_ERROR, "xmpp connection is null or closed"));
                        return null;
                    }

                    String jid = MessageBuilder.getFormatedJID(chatMessage.getToUserId(), serverAddress, AppContext.getAppContext().getDeviceInfo().getDeviceType());
                    Message message = new Message(jid, Message.Type.chat);
                    message.setPacketID(chatMessage.getMessageId());

                    DeliveryReceiptManager.addDeliveryReceiptRequest(message);
                    Media media = chatMessage.getMedia();
                    if (media == null) {
                        message.setBody(chatMessage.getContent());
                        connection.sendPacket(message);

                        logger.debug("send a text message to " + chatMessage.getToUserId());
                        return null;
                    }

                    MediaPacket mediaPacket = MessageBuilder.buildMediaPacket(media);
                    mediaPacket.setMessage(message);
                    connection.sendPacket(mediaPacket);

                    logger.debug("send a extension message to " + chatMessage.getToUserId());
                    return null;
                } catch (SmackException.NotConnectedException e) {
                    logger.error(e.toString(), e);
                    xmppListener.onXmppError(chatMessage.getSessionId(),chatMessage.getMessageId(),MessageBuilder.buildError(Constants.Error.XMPP_CONNECTION_ERROR, e));
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                    xmppListener.onXmppError(chatMessage.getSessionId(),chatMessage.getMessageId(),MessageBuilder.buildError(Constants.Error.XMPP_CONNECTION_ERROR, e));
                }

                return null;
            }
        };

        connectionThread.execute(chatMessage);
    }

    @Override
    public void listenIncomingMessages() {
        //PacketFilter  filter = new AndFilter(new PacketTypeFilter(Message.class), new PacketTypeFilter(IQ.class));

        PacketListener iqListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                logger.debug("listenIncomingMessages Receive new IQ packet: " + packet);

                if (packet instanceof MessageStateIQ) {
                    MessageStateIQ iq = (MessageStateIQ) packet;
                    logger.debug("Receive OfflineMessage IQ:" + iq);
                    xmppListener.onIQMessage(iq);

                } else if (packet instanceof ServerTimeIQ) {
                    ServerTimeIQ iq = (ServerTimeIQ) packet;
                    xmppListener.onServerTime(iq.getServerTime());
                }

            }
        };

        PacketListener msgListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                logger.debug("listenIncomingMessages packet: " + packet.getPacketID());

                Message message = (Message) packet;
                ChatMessage chatMessage = null;

                //1 test if it is from Server Receipt
                if (message.getFrom().equals(Constants.Chat.CHAT_FROM_SERVER)) {
                    //Server_externsion toId = self, from=Server,so Ids have no value to use
                    ServerReceiptExtension serverReceiptExtension = message.getExtension(ServerReceiptExtension.ELEMENT_NAME, ServerReceiptExtension.NAMESPACE);
                    chatMessage = MessageBuilder.buildMessageFromServerReceipt(0, serverReceiptExtension, serverReceiptExtension.getMessageId());
                    logger.debug("onReceiveReceipt for server receipt:" + chatMessage.getMessageId());
                    xmppListener.onReceiveReceipt(chatMessage);
                    return;
                }

                long toId = Long.valueOf(message.getTo().split("@")[0]);
                long fromId = Long.valueOf(message.getFrom().split("@")[0]);
                //2. test for both RealTime and Offline message Receipt
                //http://xmpp.org/extensions/xep-0184.html
                DeliveryReceipt receipt = message.getExtension("received", "urn:xmpp:receipts");
                if (receipt != null) {
                    chatMessage = MessageBuilder.buildMessageFromReceipt(toId, fromId, receipt.getId(), message.getPacketID());
                    logger.debug("onReceiveReceipt for receive receipt:" + chatMessage.getMessageId());
                    xmppListener.onReceiveReceipt(chatMessage);
                    return;
                }

                long sentTime = System.currentTimeMillis();
                //3. test for receive offline message
                //http://xmpp.org/extensions/xep-0203.html; http://xmpp.org/extensions/xep-0091.html
                PacketExtension delayExtension = message.getExtension("delay", "urn:xmpp:delay");
                if (delayExtension != null) {
                    DelayInformation delayInfo = (DelayInformation) delayExtension;  //DelayInfo
                    sentTime = delayInfo.getStamp().getTime(); //actual send time from remote
                    logger.debug("process offline message sent at - " + delayInfo.getStamp());
                }

                //4. build received message for both realtime or offline
                //4.1.  test for media data
                MediaPacketExtension mediaExtension = message.getExtension(MediaPacketExtension.ELEMENT_NAME, MediaPacketExtension.NAMESPACE);
                if (mediaExtension != null) {
                    Media media = MessageBuilder.buildMedia(mediaExtension);
                    chatMessage = MessageBuilder.buildReceivedMessage(media.getMessageType(),toId, fromId, message.getPacketID(), media.getContentText(), sentTime);
                    chatMessage.setMedia(media);
                    chatMessage.setMessageType(media.getMessageType());
                    logger.debug("parse for media:" + media);
                }else{
                    //4.2.  jsut a text message
                    chatMessage = MessageBuilder.buildReceivedMessage(Constants.Chat.CHAT_MESSAGE_TYPE_TEXT,toId, fromId, message.getPacketID(), message.getBody(), sentTime);
                }

                if (DeliveryReceiptManager.hasDeliveryReceiptRequest(packet)) {/*do nothing*/}

                logger.debug("onReceiveMessage for message:" + chatMessage);
                xmppListener.onReceiveMessage(chatMessage);

            }
        };

        connection.addPacketListener(iqListener, new PacketTypeFilter(IQ.class));
        connection.addPacketListener(msgListener, new PacketTypeFilter(Message.class));
        //connection.addPacketListener(extListener,new PacketExtensionFilter(DeliveryReceipt.ELEMENT));
    }

    @Override
    public void sendServerTimeIQ() {
        Packet packet = new ServerTimeIQ("<query xmlns='http://kanebay.com/protocol/server#time'></query>");
        try {
            connection.sendPacket(packet);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    @Override
    public void sendMessageIQ(long fromId, long toId, String packetId) {
        try {
            MessageStateIQ packet = new MessageStateIQ("<query xmlns='http://kanebay.com/protocol/offlinemessage#exist'>" +
                    "<message from='" + fromId + "' to='" + toId +"' id='" + packetId +"' /></query>");

            if (isConnectionClosed()) {
                logger.debug("isConnectionClosed = TRUE. Failed to send OfflineMsg IQ: fromId=" + fromId + ",toId=" + toId + ",packetId=" + packetId);
                return;
            }else if (connection == null || !connection.isConnected()) {
                logger.debug("connection is null or closed. Failed to send OfflineMsg IQ: fromId=" + fromId + ",toId=" + toId + ",packetId=" + packetId);
                return;
            }

            connection.sendPacket(packet);

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    private  void notifyChatStateChange(int what, Bundle data){
        android.os.Message msg = new android.os.Message();
        if (data!=null) { msg.setData(data); }
        msg.what = what;
        //ChatManager.getInstance().onMessage(msg);
    }

    @Override
    public void setConnectionClosed(boolean value) {
        connectionClosed = value;
    }

    @Override
    public boolean isConnectionClosed() {
        return connectionClosed;
    }

    @Override
    public boolean isConnectionValid() {
        if((connection == null) || (!connection.isConnected()) || isConnectionClosed()) {
            return false;
        }
        return true;
    }

    @Override
    public XMPPConnection getConnection() {
        return connection;
    }

    @Override
    public void setXmppListener(XmppListener callback) {
        this.xmppListener = callback;
    }
}