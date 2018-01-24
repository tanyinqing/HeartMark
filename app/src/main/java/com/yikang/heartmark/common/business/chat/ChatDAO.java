package com.yikang.heartmark.common.business.chat;

import android.database.Cursor;

import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.business.other.StorageManager;
import com.yikang.heartmark.common.db.DbQueryRunner;
import com.yikang.heartmark.common.util.TimeUtil;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ChatMessage;
import com.yikang.heartmark.model.chat.ChatSession;
import com.yikang.heartmark.model.chat.Media;
import com.yikang.heartmark.model.chat.Patient;
import com.yikang.heartmark.model.chat.UserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by guolchen on 2014/12/17.
 */
public class ChatDAO {
    private static Logger logger = LoggerFactory.getLogger(ChatDAO.class);

    static class ChatDAOHolder {
        static ChatDAO chatDAO = new ChatDAO();
    }

    public static ChatDAO getInstance() {
        return ChatDAOHolder.chatDAO;
    }

    public List<ChatMessage> getChatMessages(long sessionId) {

        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT * FROM chat_Message WHERE SessionId=? ";
            String[] params = new String[]{String.valueOf(sessionId)};

            List<ChatMessage> chatMessagegs = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    ChatMessage chatMsg = new ChatMessage();

                    chatMsg.setSessionId(c.getInt(c.getColumnIndex("SessionId")));
                    chatMsg.setMessageId(c.getString(c.getColumnIndex("MessageId")));
                    chatMsg.setContent(c.getString(c.getColumnIndex("Content")));
                    chatMsg.setHostUserId(c.getLong(c.getColumnIndex("HostUserId")));
                    chatMsg.setFromUserId(c.getLong(c.getColumnIndex("FromUserId")));
                    chatMsg.setToUserId(c.getLong(c.getColumnIndex("ToUserId")));
                    chatMsg.setDirection(c.getInt(c.getColumnIndex("Direction")));
                    chatMsg.setMessageType(c.getInt(c.getColumnIndex("MsgType")));

                    String objectId = c.getString(c.getColumnIndex("ObjectId"));
                    //todo by chengl 图片发送失败没有objectId,需要有Media
                    //if (objectId!=null && !objectId.isEmpty()) {
                    if (chatMsg.getMessageType() != Constants.Chat.CHAT_MESSAGE_TYPE_TEXT) {
                        Media media = new Media();
                        media.setObjectId(c.getString(c.getColumnIndex("ObjectId")));
                        media.setThumbnailImageId(c.getString(c.getColumnIndex("ThumbnailImageId")));
                        media.setLargeImageId(c.getString(c.getColumnIndex("LargeImageId")));
                        media.setDuration(c.getInt(c.getColumnIndex("Duration")));
                        media.setObjectSize(c.getInt(c.getColumnIndex("ObjectSize")));
                        media.setFullName(c.getString(c.getColumnIndex("AttachFile")));

                        chatMsg.setMedia(media);
                    }

                    Date sendDateTime = TimeUtil.getUtcDateTime(c.getString(c.getColumnIndex("SendTime")));
                    chatMsg.setSendTime(sendDateTime);
                    Date recDateTime = TimeUtil.getUtcDateTime(c.getString(c.getColumnIndex("ReceiveTime")));
                    chatMsg.setReceiveTime(recDateTime);

                    chatMsg.setRead(Boolean.valueOf(c.getString(c.getColumnIndex("IsRead"))));
                    logger.debug("SendStatus=" + c.getInt(c.getColumnIndex("SendStatus")));
                    if (!c.isNull(c.getColumnIndex("SendStatus"))) {
                        chatMsg.setSendStatus(c.getInt(c.getColumnIndex("SendStatus")));
                    }
                    if (AppContext.getAppContext().getCurrentUser().getUserInfo().getProfilePictureThumbnailId().equals("")) {
                        chatMsg.setHostProfilePicId(String.valueOf(AppContext.getAppContext().getCurrentUser().getUserInfo().getGender()));
                    } else {
                        chatMsg.setHostProfilePicId(AppContext.getAppContext().getCurrentUser().getUserInfo().getProfilePictureThumbnailId());
                    }

                    return chatMsg;

                }
            }, params);

            return chatMessagegs;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public ChatMessage getChatMessage(String messageId) {

        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT * FROM chat_Message WHERE MessageId=? ";
            String[] params = new String[]{String.valueOf(messageId)};

            List<ChatMessage> chatMessagegs = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    ChatMessage chatMsg = new ChatMessage();

                    chatMsg.setSessionId(c.getInt(c.getColumnIndex("SessionId")));
                    chatMsg.setMessageId(c.getString(c.getColumnIndex("MessageId")));
                    chatMsg.setContent(c.getString(c.getColumnIndex("Content")));
                    chatMsg.setHostUserId(c.getLong(c.getColumnIndex("HostUserId")));
                    chatMsg.setFromUserId(c.getLong(c.getColumnIndex("FromUserId")));
                    chatMsg.setToUserId(c.getLong(c.getColumnIndex("ToUserId")));
                    chatMsg.setDirection(c.getInt(c.getColumnIndex("Direction")));
                    chatMsg.setMessageType(c.getInt(c.getColumnIndex("MsgType")));

                    String objectId = c.getString(c.getColumnIndex("ObjectId"));
                    if (objectId != null && !objectId.isEmpty()) {
                        Media media = new Media();
                        media.setObjectId(c.getString(c.getColumnIndex("ObjectId")));
                        media.setThumbnailImageId(c.getString(c.getColumnIndex("ThumbnailImageId")));
                        media.setLargeImageId(c.getString(c.getColumnIndex("LargeImageId")));
                        media.setDuration(c.getInt(c.getColumnIndex("Duration")));
                        media.setObjectSize(c.getInt(c.getColumnIndex("ObjectSize")));
                        media.setFullName(c.getString(c.getColumnIndex("AttachFile")));

                        chatMsg.setMedia(media);
                    }

                    Date sendDateTime = TimeUtil.getUtcDateTime(c.getString(c.getColumnIndex("SendTime")));
                    chatMsg.setSendTime(sendDateTime);
                    Date recDateTime = TimeUtil.getUtcDateTime(c.getString(c.getColumnIndex("ReceiveTime")));
                    chatMsg.setReceiveTime(recDateTime);

                    chatMsg.setRead(Boolean.valueOf(c.getString(c.getColumnIndex("IsRead"))));
                    logger.debug("SendStatus=" + c.getInt(c.getColumnIndex("SendStatus")));
                    if (!c.isNull(c.getColumnIndex("SendStatus"))) {
                        chatMsg.setSendStatus(c.getInt(c.getColumnIndex("SendStatus")));
                    }
                    if (AppContext.getAppContext().getCurrentUser().getUserInfo().getProfilePictureThumbnailId().equals("")) {
                        chatMsg.setHostProfilePicId(String.valueOf(AppContext.getAppContext().getCurrentUser().getUserInfo().getGender()));
                    } else {
                        chatMsg.setHostProfilePicId(AppContext.getAppContext().getCurrentUser().getUserInfo().getProfilePictureThumbnailId());
                    }

                    return chatMsg;

                }
            }, params);
            if (chatMessagegs.size() > 0) {
                return chatMessagegs.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public boolean isDuplicateMessage(long hostUserId, long fromId, long toId, String messageId) {

        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT Count(*) FROM chat_Message WHERE HostUserId=? AND FromUserId=? AND ToUserId=? AND MessageId=?";

            String[] params = new String[]{String.valueOf(hostUserId), String.valueOf(fromId), String.valueOf(toId), messageId};

            List<Integer> records = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    int r = c.getInt(0);
                    return r;
                }
            }, params);

            return records.get(0) > 0;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return false; //if any error, return false, so may let duplicate record in, better than loss it!
    }

    /**
     * 通过userId和hostId查找sessionId,sessionId是数据库自增字段，一般在新建一条session后，马上调用
     * 此函数得到sessionId,没有记录返回0
     */
    public int getSessionId(long hostId, long userId) {
        try {

            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT sessionId from chat_Session Where hostUserId=? AND userId=?";
            String[] params = new String[]{String.valueOf(hostId), String.valueOf(userId)};

            List<Integer> ids = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    return c.getInt(c.getColumnIndex("SessionId"));
                }
            }, params);

            if (ids.isEmpty()) {
                return 0;
            } else {
                return ids.get(0);
            }

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return 0;
    }

    /**
     * 指定sessionId的session新消息数清零
     */
    public void reSetNewMsgCount(long sessionId) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "UPDATE chat_Session SET NewMsgCount=? WHERE SessionId=?";

            int msgCount = 0;
            Object[] params = new Object[]{msgCount, sessionId};

            queryRunner.update(sql, params);

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public List<ChatSession> getChatSessions() {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            /*String sql = "SELECT sessionId, userProfilePictureId,hostUserId, userId,userNickName,gender,\n" +
                    "newMsgCount,lastMsgContent,lastMsgTime from chat_Session WHERE hostUserId=?";*/
            String sql = "";
            //todo  ApplicationType
//            if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//                sql = "SELECT cs.* from chat_Session cs, Patient p where cs.userId = p.OriginalId and cs.hostUserId=? and (p.Status = ? or p.Status = ?) order by cs.LastMsgTime DESC";
//            } else {
            sql = "SELECT cs.* from chat_Session cs, Doctor d where cs.userId = d.OriginalId and cs.hostUserId=? and (d.Status = ? or d.Status = ?) order by cs.LastMsgTime DESC";
//            }

            String[] params = new String[]{String.valueOf(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId()),
                    Constants.Relation.RELATION_CODE_NORMAL_TEXT,
                    Constants.Relation.RELATION_CODE_RELEASED_TEXT};
            List<ChatSession> sessions = getSessionsBySql(sql, params);

            if (sessions != null) {
                logger.debug("getChatSessions return " + sessions.size() + " records");
            } else {
                logger.debug("getChatSessions return null!");
            }

            return sessions;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public ChatSession getChatSession(long sessionId) {

        try {

            String sql = "";
            //todo
//            if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//                //sql = "SELECT cs.* from chat_Session cs, Patient p where cs.userId = p.OriginalId and cs.hostUserId=? and (p.Status = ? or p.Status = ?)";
//                sql = "SELECT cs.* from chat_Session cs, Patient p Where cs.sessionId=? and cs.UserId = p.OriginalId And (p.Status = ? or p.Status = ?)";
//            } else {
            sql = "SELECT cs.* from chat_Session cs, Doctor d Where cs.sessionId=? and cs.UserId = d.OriginalId And (d.Status = ? or d.Status = ?)";
//            }

            DbQueryRunner queryRunner = DbQueryRunner.getInstance();


            String[] params = new String[]{String.valueOf(sessionId),
                    Constants.Relation.RELATION_CODE_NORMAL_TEXT,
                    Constants.Relation.RELATION_CODE_RELEASED_TEXT};

            List<ChatSession> ids = getSessionsBySql(sql, params);

            if (ids.isEmpty()) {
                return null;
            } else {
                return ids.get(0);
            }

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    /**
     * 通过userId和hostId查找chat session
     */
    public ChatSession getChatSession(long hostId, long userId) {
        //todo
//        if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//            Patient patient = DoctorManager.getInstance().getPatientfromDB(userId);
//            if (null == patient) {
//                return null;
//            }
//        } else {
//            Doctor doctor = PatientManager.getInstance().getDoctorfromDB(userId);
//            if (null == doctor) {
//                return null;
//            }
//        }
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT * from chat_Session Where hostUserId=? AND userId=?";
            String[] params = new String[]{String.valueOf(hostId), String.valueOf(userId)};
            List<ChatSession> sessions = getSessionsBySql(sql, params);

            if (sessions == null) {
                logger.debug("getChatSessions return null.");
                return null;
            } else if (sessions.size() == 0) {
                logger.debug("getChatSessions return empty.");
                return null;
            } else if (sessions.size() > 1) {
                logger.debug("getChatSessions return more than one sessions.");
                return null;
            } else {
                return sessions.get(0);
            }

        } catch (Exception e) {
            logger.error(e.toString(), e);
            return null;
        }
    }

    public ChatSession getUserChatSession(long hostId, long userId) {

        try {

            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT sessionId, userProfilePictureId,hostUserId, userId,userNickName,gender,\n" +
                    "newMsgCount,lastMsgContent,lastMsgTime from chat_Session Where hostUserId=? AND userId=?";
            String[] params = new String[]{String.valueOf(hostId), String.valueOf(userId)};

            List<ChatSession> ids = getSessionsBySql(sql, params);

            if (ids.isEmpty()) {
                return null;
            } else {
                return ids.get(0);
            }

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public List<ChatSession> getSessionsBySql(String sql, String[] params) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();

            List<ChatSession> sessions = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    ChatSession chat = new ChatSession();

                    chat.setSessionId(c.getInt(c.getColumnIndex("SessionId")));
                    chat.setGender(c.getInt(c.getColumnIndex("Gender")));
                    chat.setNewMsgCount(c.getInt(c.getColumnIndex("NewMsgCount")));

                    chat.setHostUserId(c.getLong(c.getColumnIndex("HostUserId")));
                    chat.setLastMsgContent(c.getString(c.getColumnIndex("LastMsgContent")));
                    chat.setUserId(c.getLong(c.getColumnIndex("UserId")));
                    chat.setUserNickName(c.getString(c.getColumnIndex("UserNickName")));
                    chat.setUserProfilePictureId(c.getString(c.getColumnIndex("UserProfilePictureId")));

                    if (!c.isNull(c.getColumnIndex("LastMsgTime"))) {
                        Date dateTime = TimeUtil.getUtcDateTime(c.getString(c.getColumnIndex("LastMsgTime")));
                        chat.setLastMsgTime(dateTime);
                    }


                    return chat;
                }
            }, params);

            if (sessions != null) {
                logger.debug("getSessionsBySql return " + sessions.size() + " records");
            } else {
                logger.debug("getSessionsBySql return null!");
            }

            return sessions;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

//    //todo add test data
//    public void insertTestChatSession() {
//        for (int i = 0; i < 10; i++) {
//            ChatSession session = new ChatSession();
//            session.setHostUserId(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId());
//            session.setUserId(33 + i);
//            session.setUserNickName("xx" + i);
//            session.setUserProfilePictureId("");
//            session.setGender(i % 2 + 1);
//            session.setNewMsgCount(i);
//            session.setLastMsgContent("你好" + i);
//            session.setLastMsgTime(TimeUtil.getCurrentTimeInUTC());
//            insertChatSession(session);
//        }
//    }

    public ChatSession insertChatSession(long hostUserId, UserInfo userInfo, int messageType, String content, Date lastMsgDateTime) {
        ChatSession session = new ChatSession();
        session.setUserId(userInfo.getUserId());
        session.setLastMsgContent(content);
        session.setHostUserId(hostUserId);
        session.setLastMsgTime(lastMsgDateTime);
        if (messageType == Constants.Chat.CHAT_NEW_MESSAGE
                || messageType == Constants.Chat.CHAT_OFFLINE_MESSAGE) {
            session.setNewMsgCount(0);
        }
        session.setUserNickName(userInfo.getNickName());
        session.setUserProfilePictureId(userInfo.getProfilePictureThumbnailId());
        session.setGender(userInfo.getGender());

        if (ChatDAO.getInstance().insertChatSession(session)) {
            int sessionId = ChatDAO.getInstance().getSessionId(hostUserId, userInfo.getUserId());
            session.setSessionId(sessionId);
            return session;
        } else {
            return null;
        }
    }

    public boolean insertChatSession(ChatSession session) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "INSERT INTO chat_Session (HostUserId, UserId, UserNickName, UserProfilePictureId, Gender," +
                    "NewMsgCount,LastMsgContent,LastMsgTime) " +
                    " values(?,?,?,?,?,?,?,?)";

            Object[] params = new Object[]{session.getHostUserId(), session.getUserId(), session.getUserNickName(), session.getUserProfilePictureId(),
                    session.getGender(), session.getNewMsgCount(), session.getLastMsgContent(), TimeUtil.formatToUtcDateTime(session.getLastMsgTime())};

            queryRunner.update(sql, params);
            //logger.debug(session.toString());
            return true;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

    public void updateChatSession(ChatSession session) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "UPDATE chat_Session SET NewMsgCount=?,LastMsgContent=?,LastMsgTime=?,UserProfilePictureId=?,UserNickName=? WHERE HostUserId=? AND UserId=?";

            Object[] params = new Object[]{session.getNewMsgCount(), session.getLastMsgContent(),
                    TimeUtil.formatToUtcDateTime(session.getLastMsgTime()), session.getUserProfilePictureId(), session.getUserNickName(), session.getHostUserId(), session.getUserId()};

            queryRunner.update(sql, params);

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }


    public void replaceMessage(ChatMessage msg) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "Replace INTO chat_Message (MessageId, HostUserId, ToUserId,FromUserId, Content,SendTime,ReceiveTime," +
                    "IsRead,SendStatus,Direction,MsgType,SessionId, DispatchTime, ObjectId, ObjectMimeType, " +
                    "ThumbnailImageId,LargeImageId,Duration, ObjectSize, ImageStyle, attachFile) " +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Media media = msg.getMedia();
            if (media == null) media = new Media();

            Object[] params = new Object[]{msg.getMessageId(), msg.getHostUserId(), msg.getToUserId(), msg.getFromUserId(), msg.getContent(),
                    TimeUtil.formatToUtcDateTime(msg.getSendTime()), msg.getReceiveTime() == null ? null : TimeUtil.formatToUtcDateTime(msg.getReceiveTime()), msg.isRead(), msg.getSendStatus(),
                    msg.getDirection(), msg.getMessageType(), msg.getSessionId(), msg.getDispatchTime(), media.getObjectId(), media.getMimeType(), media.getThumbnailImageId(),
                    media.getLargeImageId(), media.getDuration(), media.getObjectSize(), media.getImageStyle(), media.getFullName()};


            queryRunner.update(sql, params);
            //getChatMessages(msg.getSessionId());
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    //todo 有图片消息，文本消息 语音消息
    public void insertMessage(ChatMessage msg) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "INSERT INTO chat_Message (MessageId, HostUserId, ToUserId,FromUserId, Content,SendTime,ReceiveTime," +
                    "IsRead,SendStatus,Direction,MsgType,SessionId, DispatchTime, ObjectId, ObjectMimeType, " +
                    "ThumbnailImageId,LargeImageId,Duration, ObjectSize, ImageStyle, attachFile) " +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Media media = msg.getMedia();
            if (media == null) media = new Media();

            Object[] params = new Object[]{msg.getMessageId(), msg.getHostUserId(), msg.getToUserId(), msg.getFromUserId(), msg.getContent(),
                    TimeUtil.formatToUtcDateTime(msg.getSendTime()), msg.getReceiveTime() == null ? null : TimeUtil.formatToUtcDateTime(msg.getReceiveTime()), msg.isRead(), msg.getSendStatus(),
                    msg.getDirection(), msg.getMessageType(), msg.getSessionId(), msg.getDispatchTime(), media.getObjectId(), media.getMimeType(), media.getThumbnailImageId(),
                    media.getLargeImageId(), media.getDuration(), media.getObjectSize(), media.getImageStyle(), media.getFullName()};


            queryRunner.update(sql, params);
            //getChatMessages(msg.getSessionId());
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void updateMessage(ChatMessage msg) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            //String sql = "UPDATE chat_Message SET IsRead=?, ReceiveTime=?, SendStatus=? WHERE MessageId=?";
            String sql = "UPDATE chat_Message SET HostUserId=?, ToUserId=?,FromUserId=?, Content=?," +
                    "SendTime=?,ReceiveTime=?, IsRead=?, SendStatus=?,Direction=?,MsgType=?,SessionId=?, " +
                    "DispatchTime=?, ObjectId=?, ObjectMimeType=?,ThumbnailImageId=?,LargeImageId=?,Duration=?, ObjectSize=?," +
                    "ImageStyle=?, attachFile=? where MessageId=?";

            Media media = msg.getMedia();
            if (media == null) media = new Media();

            Object[] params = new Object[]{msg.getHostUserId(), msg.getToUserId(), msg.getFromUserId(), msg.getContent(),
                    msg.getSendTime() == null ? null : TimeUtil.formatToUtcDateTime(msg.getSendTime()), msg.getReceiveTime() == null ? null : TimeUtil.formatToUtcDateTime(msg.getReceiveTime()),
                    msg.isRead(), msg.getSendStatus(), msg.getDirection(), msg.getMessageType(), msg.getSessionId(),
                    msg.getDispatchTime() == null ? null : TimeUtil.formatToUtcDateTime(msg.getDispatchTime()),
                    media.getObjectId(), media.getMimeType(), media.getThumbnailImageId(), media.getLargeImageId(),
                    media.getDuration(), media.getObjectSize(), media.getImageStyle(), media.getFullName(), msg.getMessageId()};
//            Object[] params = new Object[]{msg.isRead(), msg.getReceiveTime() == null ?
//                    null : TimeUtil.formatToUtcDateTime(msg.getReceiveTime()), msg.getSendStatus(), msg.getMessageId()};

            queryRunner.update(sql, params);

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void updateResendMessage(ChatMessage msg) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "UPDATE chat_Message SET SendTime=? WHERE MessageId=?";

            Object[] params = new Object[]{TimeUtil.formatToUtcDateTime(msg.getSendTime()), msg.getMessageId()};

            queryRunner.update(sql, params);

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void updateAllSentMessages() {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "UPDATE chat_Message SET SendStatus = ? WHERE  SendStatus in ( 1,5,6) ";
            Object[] params = new Object[]{Constants.Chat.CHAT_SEND_STATUS_LOST};// , Constants.Chat.CHAT_SEND_STATUS_SENT
            queryRunner.update(sql, params);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void updateMessagesStatus(String messageid, int status) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "UPDATE chat_Message SET SendStatus=? WHERE MessageId=?";
            Object[] params = new Object[]{status, messageid};
            queryRunner.update(sql, params);

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public boolean clearAllChatHistory() {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            queryRunner.update("DELETE FROM chat_Message", new Object[]{});
            queryRunner.update("DELETE FROM chat_Session", new Object[]{});
            logger.debug("clearAllChatHistory");
            return true;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

    public List<Media> getChatHistoryStorageFileList(int sessionId) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            String sql = "SELECT MsgType, ObjectId, ThumbnailImageId, LargeImageId, ObjectMimeType  FROM chat_Message WHERE SessionId=? And MsgType <> ? ";
            String[] params = new String[]{String.valueOf(sessionId), String.valueOf(Constants.Chat.CHAT_MESSAGE_TYPE_TEXT)};

            List<Media> medias = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    Media media = new Media();

                    media.setMessageType(c.getInt(c.getColumnIndex("MsgType")));
                    media.setObjectId(c.getString(c.getColumnIndex("ObjectId")));
                    media.setThumbnailImageId(c.getString(c.getColumnIndex("ThumbnailImageId")));
                    media.setLargeImageId(c.getString(c.getColumnIndex("LargeImageId")));
                    media.setMimeType(c.getString(c.getColumnIndex("ObjectMimeType")));
                    return media;

                }
            }, params);
            if (null != medias) {
                return medias;
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }

        return null;
    }

    public boolean clearChatHistoryBySession(long sessionId) {
        try {
            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
            queryRunner.update("DELETE FROM chat_Message WHERE SessionId=" + sessionId, new Object[]{});
            queryRunner.update("DELETE FROM chat_Session WHERE SessionId=" + sessionId, new Object[]{});

            StorageManager.getInstance().clear(sessionId);
            logger.debug("clearChatHistoryBySession");
            return true;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

    public void updatePatientProfilePicId(List<Patient> patients) {
        if (null == patients || patients.size() < 1)
            return;
        List<ChatSession> chatSessions = getChatSessions();
        if (null == chatSessions || chatSessions.size() < 1) {
            return;
        }
        if (chatSessions.size() > 0) {
            for (int i = 0; i < chatSessions.size(); i++) {
                for (int j = 0; j < patients.size(); j++) {
                    if (chatSessions.get(i).getUserId() == patients.get(j).getUserInfo().getUserId()) {
                        chatSessions.get(i).setUserProfilePictureId(patients.get(j).getUserInfo().getProfilePictureThumbnailId());
                        updateChatSession(chatSessions.get(i));
                    }
                }
            }
        }
    }

//    public void updateDoctorProfilePicId(List<Doctor> doctors){
//        if(null == doctors || doctors.size() < 1)
//            return;
//        List<ChatSession> chatSessions = getChatSessions();
//        if(null == chatSessions || chatSessions.size() < 1){
//            return;
//        }
//        if(chatSessions.size() > 0){
//            for(int i = 0; i < chatSessions.size(); i++){
//                for(int j = 0; j < doctors.size(); j++){
//                    if(chatSessions.get(i).getUserId() == doctors.get(j).getUserInfo().getUserId()){
//                        chatSessions.get(i).setUserProfilePictureId(doctors.get(j).getUserInfo().getProfilePictureThumbnailId());
//                        updateChatSession(chatSessions.get(i));
//                    }
//                }
//            }
//        }
//    }

}
