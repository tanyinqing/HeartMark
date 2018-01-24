package com.yikang.heartmark.common.business.chat;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Chang on 9/20/2014.
 */
public class XMPPConnectionListener implements ConnectionListener {
    private static Logger logger = LoggerFactory.getLogger(XMPPConnectionListener.class);

    private long userId;
    private String password;
    private String host;
    private int port;
    private IClientChat clientChat;

    public XMPPConnectionListener(IClientChat clientChat,  String host,  int port, long userId,  String password){
        this.host = host;
        this.port = port;
        this.userId = userId;
        this.password = password;
        this.clientChat = clientChat;
    }
    @Override
    public void connected(final XMPPConnection connection){
        logger.debug("XMPPConnectionListener connected");
        boolean c = clientChat.getConnection().isConnected();
        clientChat.setConnectionClosed(!c);
//        if(!connection.isAuthenticated())
//            login(connection, userId, password);
    }
    @Override
    public void authenticated(XMPPConnection arg0){}
    @Override
    public void connectionClosed(){
        logger.debug("XMPPConnectionListener connectionClosed");
        boolean c = clientChat.getConnection().isConnected();
        clientChat.setConnectionClosed(!c);

    }
    @Override
    public void connectionClosedOnError(Exception arg0){
        logger.debug("XMPPConnectionListener connectionClosedOnError:" + arg0.getMessage());
        boolean c = clientChat.getConnection().isConnected();
        clientChat.setConnectionClosed(!c);

    }
    @Override
    public void reconnectingIn(int arg0){
        logger.debug("XMPPConnectionListener reconnectingIn:" + arg0);
    }
    @Override
    public void reconnectionFailed(Exception arg0){
        logger.debug("XMPPConnectionListener reconnectionFailed:" + arg0);
    }
    @Override
    public void reconnectionSuccessful(){
        logger.debug("XMPPConnectionListener reconnectionSuccessful");
        boolean c = clientChat.getConnection().isConnected();
        clientChat.setConnectionClosed(!c);

        if(!clientChat.getConnection().isAuthenticated())
            clientChat.login(host,port,userId, password);

    }


    private void login(final XMPPConnection connection, final long userId, final String password) {
        try {
            logger.debug("XMPPConnectionListener login:" + userId);
            connection.login(String.valueOf(userId), password);
        } catch (SmackException.NotConnectedException e) {
            logger.warn("XMPPConnectionListener login NotConnectedException:" + e.getMessage());

            // If is not connected, a timer is schelude and a it will try to reconnect
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        connection.connect();
//                    }catch (IOException e){
//                    }catch (XMPPException e){
//                    }catch (SmackException e) {
//                    }
//                }
//            }, 5 * 1000);
        } catch (SaslException e) {
            logger.warn("XMPPConnectionListener login SaslException:" + e.getMessage());
        } catch (XMPPException e) {
            logger.warn("XMPPConnectionListener login XMPPException:" + e.getMessage());
        } catch (SmackException e) {
            logger.warn("XMPPConnectionListener login SmackException:" + e.getMessage());
        } catch (IOException e) {
            logger.warn("XMPPConnectionListener login IOException:" + e.getMessage());
        }
    }

}

