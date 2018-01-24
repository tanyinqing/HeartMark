package com.yikang.heartmark.common.business.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;

import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.business.other.AppCommonService;
import com.yikang.heartmark.common.business.other.MediaManager;
import com.yikang.heartmark.common.util.TimeUtil;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ChatError;
import com.yikang.heartmark.model.chat.ChatMessage;
import com.yikang.heartmark.model.chat.ChatSession;
import com.yikang.heartmark.model.chat.Media;
import com.yikang.heartmark.model.chat.UserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chang on 12/29/2014.
 * 聊天管理
 */
public class ChatManager implements IClientChat.XmppListener, MediaManager.MediaListener {
    private static Logger logger = LoggerFactory.getLogger(ChatManager.class);

    private String host;
    private int port;
    private long uid;
    private String pwd;
    private int loginState = Constants.Chat.CHAT_LOGIN_STATE_NEW;
    private Vibrator vibrate;

    private Map<String, ChatMessage> newMessages = new ConcurrentHashMap<String, ChatMessage>();
    private List<ChatSession> chatSessions = null;
    private boolean isMonitorStopped = false;

    private IClientChat chatClient = null;

    static class ChatManagerHolder {
        static ChatManager manager = new ChatManager();
//        vibrator = (Vibrator) getActivity()
//        .getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static ChatManager getInstance() {
        return ChatManagerHolder.manager;
    }

    private ChatManager() {
        resetAllSentMessages();
        chatClient = ClientChatXmppImpl.getInstance();
        chatClient.setXmppListener(this);
    }

    //********Public methods**************//

    public int getLoginState() {
        return loginState;
    }

    public void login(String host, int port, long userId, String pwd) {
        loginState = Constants.Chat.CHAT_LOGIN_STATE_STARTED;
        this.host = host;
        this.port = port;
        this.uid = userId;
        this.pwd = pwd;

        chatClient.login(host, port, userId, pwd);
    }

    public void logout() {
        isMonitorStopped = true;
        chatClient.disconnect();
        loginState = Constants.Chat.CHAT_LOGIN_STATE_NEW;
    }

    public void sendMessage(ChatSession session, ChatMessage chatMessage) {
        try {

            if (!verifyOutGoingMessage(session, chatMessage)) {
                logger.error("verifyOutGoingMessage failed");
                return;
            }

            //1. add message to database
            ChatDAO.getInstance().replaceMessage(chatMessage);

            //2.update session content and lastMsgTime to database
            session.setLastMsgContent(chatMessage.getContent());
            session.setLastMsgTime(chatMessage.getSendTime());

            ChatDAO.getInstance().updateChatSession(session);

            //3. put message to Queue
            newMessages.put(chatMessage.getMessageId(), chatMessage);

            //4. broadcast data change to UI
            notifyUIDataChange(session, chatMessage, Constants.Chat.CHAT_MESSAGE_SENT);

            //5. send out message, so receipt should arrive after steps 1 and 2
            chatClient.sendMessage(chatMessage);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void sendMessage(ChatSession session, ChatMessage chatMessage, Boolean isMediaType) {
        try {

            if (!verifyOutGoingMessage(session, chatMessage)) {
                logger.error("verifyOutGoingMessage failed");
                return;
            }

            //1. add message to database
            ChatDAO.getInstance().replaceMessage(chatMessage);

            //2.update session content and lastMsgTime to database
            session.setLastMsgContent(chatMessage.getContent());
            session.setLastMsgTime(chatMessage.getSendTime());

            ChatDAO.getInstance().updateChatSession(session);

            //3. put message to Queue
            newMessages.put(chatMessage.getMessageId(), chatMessage);

            //4. broadcast data change to UI
            // notifyUIDataChange(session, chatMessage, Constants.Chat.CHAT_MESSAGE_SENT);

            //5. send out message, so receipt should arrive after steps 1 and 2
            chatClient.sendMessage(chatMessage);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void prepareSendMediaMessage(ChatSession session, ChatMessage chatMessage) {
        try {

            if (chatMessage.getMedia() == null) {
                logger.warn("no media to upload");
                return;
            }

            //1. add message to database
            ChatDAO.getInstance().replaceMessage(chatMessage);

            //2.update session lastMsgTime lastContent to database
            session.setLastMsgContent(chatMessage.getContent());
            session.setLastMsgTime(chatMessage.getSendTime());
            ChatDAO.getInstance().updateChatSession(session);

            //3. broadcast data change to UI
            notifyUIDataChange(session, chatMessage, Constants.Chat.CHAT_MESSAGE_SENT);

            //4. send out async media message, so receipt should arrive after steps 1 and 2
            startAsyncMediaOperation(MediaService.ACTION_UPLOAD_MEDIA, chatMessage.getMedia());

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public List<ChatSession> getChatSessions() {
        try {
            return ChatDAO.getInstance().getChatSessions();
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public List<ChatSession> getChatSessionsWithProductIcon() {
//        List<ChatSession> sessionList = getChatSessions();
//        HashMap iconMap = AppContext.getAppContext().getCachePatientsMap();
//        Boolean isDoctorType = AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR;
//
//        if (iconMap == null) {
//            if (isDoctorType) {
//                DoctorManager.getInstance().loadAndCachePatients();
//            } else {
//                PatientManager.getInstance().loadAndCacheDoctors();
//            }
//            iconMap = AppContext.getAppContext().getCachePatientsMap();
//        }
//
//        if (iconMap != null) {
//            for (ChatSession item : sessionList) {
//                if (iconMap.get(item.getUserId()) != null) {
//                    if (isDoctorType) {
//                        Patient patient = (Patient) iconMap.get(item.getUserId());
//                        item.productIconId = patient.product_icon_id;
//                    } else {
//                        Doctor doctor = (Doctor) iconMap.get(item.getUserId());
//                        item.productIconId = doctor.getProduct_icon_id();
//                        item.product_leavel = doctor.getProduct_level();
//
//                    }
//                }
//            }
//        }
        return new ArrayList<ChatSession>();
    }

    public ChatSession getOrCreateChatSession(long hostId, UserInfo userInfo, String content, Date sendTime, int messageType) {
        try {
            ChatSession session = ChatDAO.getInstance().getUserChatSession(hostId, userInfo.getUserId());
            if (null == session) {
                session = insertChatSession(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId(),
                        userInfo, messageType, content, sendTime);
                session.productIconId = userInfo.productIconId;
            }

            if (null != session) {
                //加上了头像 昵称 更新
                session.setUserProfilePictureId(userInfo.getProfilePictureThumbnailId());
                session.setUserNickName(userInfo.getNickName());
                session.productIconId = userInfo.productIconId;
                ChatDAO.getInstance().updateChatSession(session);
            }

            return session;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return null;
        }
    }

    public List<ChatMessage> getChatMessages(long sessionId) {
        try {
            return ChatDAO.getInstance().getChatMessages(sessionId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public ChatSession getOrCreateChatSession(long hostId, UserInfo userInfo) {
        try {
            return getOrCreateChatSession(hostId, userInfo, "", null, Constants.Chat.CHAT_MESSAGE_SENT);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return null;
        }
    }

    public void resetNewMsgCount(long sessionId) {
        try {
            ChatDAO.getInstance().reSetNewMsgCount(sessionId);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    //********Xmpp Listener callback methods**************//
    @Override
    public void onLogin() {
        loginState = Constants.Chat.CHAT_LOGIN_STATE_SUCCESS;
        logger.debug("chat login. logined=" + loginState);
        startListenIncomingMessages();
    }

    @Override
    public void onServerTime(String tm) {
        logger.debug("onServerTime:" + tm);
        try {
            Date current = TimeUtil.toUTC(new Date());
            //format=2014-10-03T09:35:07Z
            String serverTime = tm.replace('T', ' ').replace('Z', ' ');
            if (!serverTime.isEmpty()) {
                Date serverUtcDate = TimeUtil.getUtcDateTime(serverTime);
                long localServerTimeDiff = current.getTime() - serverUtcDate.getTime();
                TimeUtil.setLocalServerTimeDiff(localServerTimeDiff);
                logger.debug("chat Time in UTC:" + serverUtcDate + ", local CurrentTime:" + current + ", TimeDiff:" + localServerTimeDiff);
            } else {
                TimeUtil.setLocalServerTimeDiff(0);
                logger.warn("chat ServerTime is empty!");
            }

            startMonitorNewMessages();

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * 收到消息
     *
     * @param chatMessage
     */
    @Override
    public void onReceiveMessage(ChatMessage chatMessage) {
        logger.debug("onReceiveMessage:" + chatMessage.getMessageId());

        //1. get or create session for incoming message
        ChatSession chatSession = null;
        if (chatMessage.getSessionId() == 0) {
            chatSession = ChatDAO.getInstance().getChatSession(chatMessage.getToUserId(), chatMessage.getFromUserId());
            if (null == chatSession) {
                UserInfo userInfo = getUserInfo(chatMessage.getFromUserId());
                if (null != userInfo) {
                    chatSession = getOrCreateChatSession(chatMessage.getHostUserId(),
                            userInfo,
                            chatMessage.getContent(),
                            chatMessage.getSendTime(),
                            Constants.Chat.CHAT_MESSAGE_RECEIVED);
                } else {
                    //TODO Chang need to handle this error?
                    logger.error("get UserInfo Error! Can't create ChatSession");
                    return;
                }
            }
        } else {
            chatSession = ChatDAO.getInstance().getChatSession(chatMessage.getSessionId());
        }

        //TODO what to do here?
        if (chatSession == null) {
            logger.error("ChatSession is null!");
            return;
        }

        //2. insert new message
        chatMessage.setSessionId(chatSession.getSessionId());
        if (null == chatMessage.getGuestProfilePicId() || chatMessage.getGuestProfilePicId().isEmpty()) {
            chatMessage.setGuestProfilePicId(chatSession.getUserProfilePictureId());
        }

//        ChatDAO.getInstance().updateMessage(chatMessage);
        ChatDAO.getInstance().insertMessage(chatMessage);

        //3. update session
        chatSession.setLastMsgTime(chatMessage.getSendTime());
        chatSession.setLastMsgContent(chatMessage.getContent());
        chatSession.setNewMsgCount(chatSession.getNewMsgCount() + 1);
        ChatDAO.getInstance().updateChatSession(chatSession);

        //4. update UI
        notifyUIDataChange(chatSession, chatMessage, Constants.Chat.CHAT_MESSAGE_RECEIVED);

        //5. download media if it is a media message
        if (chatMessage.getMedia() != null) {
            //Download media async, for now, Image is download directly by ImageLoader, so no actual download happen there
            Media media = chatMessage.getMedia();
            media.setSessionId(chatSession.getSessionId());
            media.setMessageId(chatMessage.getMessageId());
            startAsyncMediaOperation(MediaService.ACTION_DOWNLOAD_MEDIA, media);
            //MediaService -> MediaManager for actual file download and save to local, then invoke onDownload callback to notify UI data ready
        }
    }

    @Override
    public void onReceiveReceipt(ChatMessage receivedMessage) {
        logger.debug("onReceiveReceipt:" + receivedMessage.getMessageId());

        //1. if message id in newMessage Queue, so remove it to prevent Offline Query
        ChatMessage localMessage = null;
        if (newMessages.containsKey(receivedMessage.getMessageId())) {
            localMessage = newMessages.get(receivedMessage.getMessageId());
            newMessages.remove(receivedMessage.getMessageId());
        }

        ChatMessage.MessageReceipt receipt = receivedMessage.getReceipt();

        //2. update message in database
        if (localMessage == null)
            localMessage = ChatDAO.getInstance().getChatMessage(receivedMessage.getMessageId());

        if (localMessage == null) {
            logger.error("ChatMessage " + receivedMessage.getMessageId() + " not found");
            return;
        }
        //for remote receipt
        if (receivedMessage.getFromUserId() != Constants.Chat.CHAT_FROM_SERVER_ID) {
            localMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_RECEIVED);
            localMessage.setReceiveTime(TimeUtil.getAdjustDateTime(TimeUtil.getCurrentTimeInUTC()));
        }
        //for server returned offline,don't update receive time until we get real receipt from remote
        else if (receipt.getReason().equals("offline")) {
            localMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_OFFLINE);
        } else {
            localMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_BLOCKED);
        }

        ChatDAO.getInstance().updateMessage(localMessage);

        ChatSession chatSession = ChatDAO.getInstance().getChatSession(localMessage.getSessionId());

        //3. broadcast data chnage to UI
        if (null != chatSession) {
            notifyUIDataChange(chatSession, localMessage, Constants.Chat.CHAT_MESSAGE_CHANGED);
        }

    }

    /**
     * 语音消息
     *
     * @param iq
     */
    @Override
    public void onIQMessage(MessageStateIQ iq) {
        logger.debug("onIQMessage:" + iq);
        String messageId = iq.getPacketId();

        if (newMessages.containsKey(messageId)) {
            int code = iq.getCode();

            logger.debug("Find Message in newMessages Queue:" + messageId);
            ChatMessage chatMessage = newMessages.get(messageId);
            if (chatMessage != null)
                newMessages.remove(messageId);
            else {
                chatMessage = ChatDAO.getInstance().getChatMessage(messageId);
            }

            if (code == Constants.Chat.CHAT_MESSAGE_CODE_OFFLINE) {
                chatMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_OFFLINE);
            } else if (code == Constants.Chat.CHAT_MESSAGE_CODE_NOFOUND) {
                chatMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_LOST);
            } else if (code == Constants.Chat.CHAT_MESSAGE_CODE_BLOCKED) {
                chatMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_BLOCKED);
            } else {
                logger.debug("Result OfflineMessageIQ message:" + messageId + " has INVALID Code:" + code);
                return;
            }

            ChatDAO.getInstance().updateMessage(chatMessage);
            ChatSession chatSession = ChatDAO.getInstance().getChatSession(chatMessage.getSessionId());
            if (null == chatSession) {
                logger.error("Strange Error: session is not exist");
                return;
            }

            notifyUIDataChange(chatSession, chatMessage, Constants.Chat.CHAT_MESSAGE_CHANGED);

        } else {
            logger.warn("IQMessage - Message was not found: " + messageId);
        }
    }

    @Override
    public void onXmppError(long sessionId, String messageId, ChatError chatError) {
        logger.debug("onXmppError sessionId:" + sessionId + " messageId:" + messageId + " error:" + chatError);
        //TODO Chang need to update message status

        if (chatError.getErrorType() == Constants.Error.XMPP_CONNECTION_ERROR) {
            //logout();
            //login(this.host, this.port, this.uid, this.pwd);
        } else if (chatError.getErrorType() == Constants.Error.XMPP_LOGIN_ERROR) {
            loginState = Constants.Chat.CHAT_LOGIN_STATE_ERROR;
        }
    }

    //********MediaListener callback methods**************//
    @Override
    public void onUpload(Media media) {
        logger.debug("onUpload media " + media.getFullName() + " for sessionId:" + media.getSessionId() + ", messageId:" + media.getMessageId());

        if (null != media) {
            ChatSession chatSession = null;
            for (ChatSession session : chatSessions) {
                if (session.getSessionId() == media.getSessionId()) {
                    chatSession = session;
                    break;
                }
            }

            ChatMessage chatMessage = ChatDAO.getInstance().getChatMessage(media.getMessageId());
            chatMessage.setMedia(media);

            chatMessage.setMessageType(media.getMessageType());

            sendMessage(chatSession, chatMessage, true);
        }
    }

    @Override
    public void onDownload(Media media) {
        logger.debug("onDownload media " + media.getObjectId() + " for sessionId:" + media.getSessionId() + ", messageId:" + media.getMessageId());

        if (null != media) {

            ChatSession chatSession = null;
            for (ChatSession session : chatSessions) {
                if (session.getSessionId() == media.getSessionId()) {
                    chatSession = session;
                    break;
                }
            }

            ChatMessage chatMessage = ChatDAO.getInstance().getChatMessage(media.getMessageId());
            chatMessage.setMedia(media);
            chatMessage.setMessageType(media.getMessageType());
            //todo by chengl
            ChatDAO.getInstance().replaceMessage(chatMessage);
            notifyUIDataChange(chatSession, chatMessage, Constants.Chat.CHAT_MESSAGE_CHANGED);
        }
    }

    @Override
    public void onMediaError(long sessionId, String messageId, Media media, ChatError chatError) {
        logger.debug("onXmppError sessionId:" + sessionId + " messageId:" + messageId + " error:" + chatError);
        //TODO Chang need to update message status

        ChatSession chatSession = null;
        for (ChatSession session : chatSessions) {
            if (session.getSessionId() == sessionId) {
                chatSession = session;
                break;
            }
        }
        ChatMessage chatMessage = ChatDAO.getInstance().getChatMessage(messageId);
        chatMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_UPLOAD_FAILED);
        chatMessage.setMedia(media);
        ChatDAO.getInstance().updateMessagesStatus(chatMessage.getMessageId(), Constants.Chat.CHAT_SEND_STATUS_UPLOAD_FAILED);
        notifyUIDataChange(chatSession, chatMessage, Constants.Chat.CHAT_MESSAGE_CHANGED);
    }

    //********Private methods**************//
    void startListenIncomingMessages() {
        chatClient.listenIncomingMessages();
    }

    private void startMonitorNewMessages() {
        Thread monitor = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        if (isMonitorStopped) {
                            logger.debug("Quit from MonitorNewMessages thread!");
                            return;
                        }
                        queryIQProc();
                        Thread.sleep(AppCommonService.getInstance().getOfflineMsgIQQueryTime());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        monitor.setPriority(Thread.MIN_PRIORITY);
        monitor.start();
    }

    private void queryIQProc() {
        for (String key : newMessages.keySet()) {
            ChatMessage chatMessage = newMessages.get(key);
            if (chatMessage.getQueryTryCount() == 0) {
                //First time query need to wait at least 5s
                if (chatMessage.getElapsed() > AppCommonService.getInstance().getOfflineMsgIQQueryTime()) {
                    chatMessage.setQueryTryCount(1);
                    chatClient.sendMessageIQ(chatMessage.getFromUserId(), chatMessage.getToUserId(), chatMessage.getMessageId());
                    logger.debug("queryIQProc #1 try for:" + chatMessage.getMessageId());
                }
            } else if (chatMessage.getQueryTryCount() == 1) {
                chatMessage.setQueryTryCount(2);
                chatClient.sendMessageIQ(chatMessage.getFromUserId(), chatMessage.getToUserId(), chatMessage.getMessageId());
                logger.debug("queryIQProc #2 try for:" + chatMessage.getMessageId());
            } else {
                chatMessage.setSendStatus(Constants.Chat.CHAT_SEND_STATUS_LOST);
                ChatDAO.getInstance().updateMessage(chatMessage);
                newMessages.remove(key);
                logger.debug("queryIQProc After #2 try for:" + chatMessage.getMessageId());
                ChatSession chatSession = ChatDAO.getInstance().getChatSession(chatMessage.getSessionId());
                if (null != chatSession) {
                    notifyUIDataChange(chatSession, chatMessage, Constants.Chat.CHAT_MESSAGE_CHANGED);
                }
            }
        }
    }

    private ChatSession insertChatSession(long hostUserId, UserInfo userInfo, int messageType, String content, Date lastMsgDateTime) {
        try {
            return ChatDAO.getInstance().insertChatSession(hostUserId, userInfo, messageType, content, lastMsgDateTime);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private UserInfo getUserInfo(long fromId) {
        UserInfo userInfo = null;
        //todo
//        if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//            Patient patient = DoctorManager.getInstance().getPatientfromDB(fromId); //getUserIn
//            if (null == patient) {
//                patient = DoctorManager.getInstance().getPatientDetailSync(null,
//                        ((Doctor) AppContext.getAppContext().getCurrentUser()).getDoctorId(), String.valueOf(fromId));
//                if (null == patient) {
//                    logger.error("query patient table and get from server error, no record at orginalId = " + fromId + " , maybe relation has released!");
//                    return null;
//                }
//            }
//            userInfo = patient.getUserInfo();
//        } else {
//            Doctor doctor = PatientManager.getInstance().getDoctorfromDB(fromId); //getUserIn
//            if (null == doctor) {
//                doctor = PatientManager.getInstance().getDoctorDetailSync(null,
//                        ((Patient) AppContext.getAppContext().getCurrentUser()).getPatientId(), String.valueOf(fromId));
//                if (null == doctor) {
//                    logger.error("query doctor table and get from server error, no record at orginalId = " + fromId + ", maybe relation has released!");
//                    return null;
//                }
//            }
//            userInfo = doctor.getUserInfo();
//        }

        return userInfo;
    }

    private void startAsyncMediaOperation(String action, Media media) {
        Intent intent = new Intent(AppContext.getAppContext(), MediaService.class);
        intent.setAction(action);
        Bundle data = new Bundle();
        data.putSerializable(Constants.Chat.CHAT_MEDIA, media);

        intent.putExtras(data);
        AppContext.getAppContext().startService(intent);
    }

    private void notifyUIDataChange(ChatSession session, ChatMessage message, int what) {
        if (chatSessions == null) {
            chatSessions = new ArrayList<ChatSession>();
        }

        boolean isNew = true;

        for (int i = 0; i < chatSessions.size(); i++) {
            if (chatSessions.get(i).getSessionId() == session.getSessionId()) {
                chatSessions.set(i, session);
                isNew = false;
                break;
            }
        }

        if (isNew)
            chatSessions.add(session);

        notifyAppContext(what, session, message);
    }

    private void notifyAppContext(int what, ChatSession session, ChatMessage message) {

        if (AppContext.getAppContext().getChatMessageHandler() != null) {
            Message msg = new Message();
            msg.what = what;
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.Chat.CHAT_MESSAGE, message);
            msg.setData(bundle);

            AppContext.getAppContext().getChatMessageHandler().sendMessage(msg);
        }

        if (AppContext.getAppContext().getChatSessionHandler() != null) {
            Message msg = new Message();
            msg.what = what;
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.Chat.CHAT_SESSION, session);
            msg.setData(bundle);

            AppContext.getAppContext().getChatSessionHandler().sendMessage(msg);
        }

    }

    private void resetAllSentMessages() {
        try {
            ChatDAO.getInstance().updateAllSentMessages();
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    private boolean verifyOutGoingMessage(ChatSession session, ChatMessage chatMessage) {
        boolean isValid = false;

        if (session == null) {
            logger.error("Invalid Outgoing Message: Session is null");
            return isValid;
        }

        if (session.getSessionId() == 0) {
            logger.error("Invalid Outgoing Message: SessionId is zero");
            return isValid;
        }

        if (session.getHostUserId() == session.getUserId()) {
            logger.error("Invalid Outgoing Message: Session HostUserId is same as (to)UserId");
            return isValid;
        }

        if (session.getHostUserId() != AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId()) {
            logger.error("Invalid Outgoing Message: Session HostUserId is not same as CurrentUser Id");
            return isValid;
        }

        if (chatMessage == null) {
            logger.error("Invalid Outgoing Message: chatMessage is null");
            return isValid;
        }

        if (chatMessage.getSessionId() == 0) {
            logger.error("Invalid Outgoing Message: chatMessage sessionId is zero");
            return isValid;
        }

        if (chatMessage.getMessageId() == null || chatMessage.getMessageId().isEmpty()) {
            logger.error("Invalid Outgoing Message: chatMessage messageId is null or empty");
            return isValid;
        }

        if (chatMessage.getSendStatus() != Constants.Chat.CHAT_SEND_STATUS_SENT) {
            logger.error("Invalid Outgoing Message: chatMessage status has an invalid value - " + chatMessage.getSendStatus());
            return isValid;
        }

        return true;
    }
}
