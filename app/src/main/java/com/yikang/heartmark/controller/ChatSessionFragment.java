//package com.yikang.heartmark.controller;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Vibrator;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.example.heartmark.R;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.yikang.heartmark.controller.adapter.ChatSessionAdapter;
//import com.yikang.heartmark.controller.base.AbsBaseFragment;
//import com.yikang.heartmark.model.chat.ChatSession;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by changliu on 2014/12/25.
// */
//public class ChatSessionFragment extends AbsBaseFragment {
//    private View view;
//    private ListView chatSessionListView;
//    private List<ChatSession> chatSessionList = new ArrayList<ChatSession>();
//    private ChatSessionAdapter chatSessionAdapter;
//    ImageLoader imageLoader;
//    View headerView;
//    TextView txtNewNoticeCount;
//    private Vibrator vibrator;
//    private AudioManager audioManager;
//    private TextView txtTip;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        audioManager = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
//        vibrator = (Vibrator) getActivity()
//                .getSystemService(Context.VIBRATOR_SERVICE);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_chat_session_patient, container, false);
//        headerView = inflater.inflate(R.layout.item_chat_session_list_patient, null);
//       // txtTip = (TextView) getActivity().findViewById(R.id.tab_tip);
//        init();
//        return view;
//    }
//
//    void init() {
//        chatSessionListView = (ListView) view.findViewById(R.id.lstView_chat_session);
//        txtNewNoticeCount = (TextView) headerView.findViewById(R.id.txt_chat_new_msg_number);
//        chatSessionListView.addHeaderView(headerView);
//        setMessageHandle();
//        setNewNoticeCountHandle();
//    }
//
//    void setChatSessionAdapter() {
//        if (null == chatSessionAdapter) {
//            chatSessionAdapter = new ChatSessionAdapter(getActivity(), chatSessionList, imageLoader);
//            chatSessionListView.setAdapter(chatSessionAdapter);
//        } else {
//            chatSessionAdapter.notifyDataSetChanged();
//        }
//
//        chatSessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                  //todo 跳转
////                Bundle bundle = new Bundle();
////                bundle.putSerializable(Constants.Chat.CHAT_SESSION, chatSessionList.get(i - 1));
////                //performGoAction("gotoChatting", bundle);
//            }
//        });
//        chatSessionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i > 0) {
//                    popupDelMenu(view, i - 1);
//                }
//                return true;
//            }
//        });
//    }
//
//    //删除
//    void popupDelMenu(View view, final int i) {
//        //todo
////        CommonPopUpWindow.setLongclickDeletePopView(getActivity(), view, new DataHandler() {
////            @Override
////            public void onData(int code, String str, Object obj) {
////                if (code == Constants.DELETE_CLICK) {
////                    long sessionId = chatSessionList.get(i).getSessionId();
////                    ChatDAO.getInstance().clearChatHistoryBySession(sessionId);
////                    chatSessionList.remove(i);
////                    chatSessionAdapter.notifyDataSetChanged();
////                }
////            }
////        });
//    }
//
//    private void setMessageHandle() {
//        Handler h = new Handler() {
//            public void handleMessage(Message msg) {
//
//
//                if (msg == null) {
//                    logger.error("handleMessage: msg is null");
//                    return;
//                }
//                Bundle data = msg.getData();
//                if (data == null) {
//                    logger.error("handleMessage: data is null");
//                    return;
//                }
//                ChatSession session = (ChatSession) data.getSerializable(Constants.Chat.CHAT_SESSION);
//                if (session == null) {
//                    logger.error("handleMessage: session is null for msg " + msg.what);
//                    return;
//                }
//                switch (msg.what) {
//                    case Constants.Chat.CHAT_MESSAGE_RECEIVED:
//                        //播放消息提示音
//                        // getRingerMode()——
//                        // 返回当前的铃声模式。如
//                        // RINGER_MODE_NORMAL（普通）2、RINGER_MODE_SILENT（静音）、RINGER_MODE_VIBRATE（震动）
//                        //setRingType();
//                        logger.debug("Session List:CHAT_MESSAGE_RECEIVED");
//                        boolean isNew = true;
//                        for (int i = 0; i < chatSessionList.size(); i++) {
//                            if (chatSessionList.get(i).getSessionId() == session.getSessionId()) {
//                                chatSessionList.set(i, session);
//                                isNew = false;
//                                break;
//                            }
//                        }
//                        if (isNew) {
//                            chatSessionList.add(session);
//                        }
//                        handleNewMessageCount(0);
//                        chatSessionAdapter.notifyDataSetChanged();
//                        break;
//                    case Constants.Chat.CHAT_MESSAGE_CHANGED:
//                        logger.debug("Chatting Room:CHAT_MESSAGE_CHANGED");
//                        for (int i = 0; i < chatSessionList.size(); i++) {
//                            if (chatSessionList.get(i).getSessionId() == session.getSessionId()) {
//                                chatSessionList.set(i, session);
//                                isNew = false;
//                                break;
//                            }
//                        }
//                        handleNewMessageCount(0);
//                        if (chatSessionAdapter != null) {
//                            chatSessionAdapter.notifyDataSetChanged();
//                        }
//                        break;
//                }
//            }
//        };
//
//        AppContext.getInstance().setChatSessionHandler(h);
//    }
//
//    //todo 铃声通知
////    private void setRingType() {
////        int modeType = audioManager.getRingerMode();
////        if (modeType == 2) { //普通模式
////            MediaPlayer mp = new MediaPlayer();
////            try {
////                mp.setDataSource(AppContext.getAppContext().getApplicationContext(), RingtoneManager
////                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
////                mp.prepare();
////                mp.start();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        } else if (null != vibrator) {
////            vibrator.vibrate(new long[]{100, 500}, -1);
////        }
////    }
//
//    private void setNewNoticeCountHandle() {
//        Handler h = new Handler() {
//            public void handleMessage(Message msg) {
//                Bundle data = msg.getData();
//                switch (msg.what) {
//                    case Constants.Counts.MSG_NEW_NOTICE_PATIENT:
//                        int newNoticeCount = data.getInt(Constants.Counts.NEW_NOTICE_COUNT);
//                        if (newNoticeCount > 0) {
//                            txtNewNoticeCount.setText(String.valueOf(newNoticeCount));
//                            txtNewNoticeCount.setVisibility(View.VISIBLE);
//                            TextView content = (TextView) headerView.findViewById(R.id.txt_content);
//                            content.setText(getResources().getString(R.string.new_notice_tips));
//                            content.setTextColor(getResources().getColor(R.color.notice));
//                        }
//                        handleNewMessageCount(newNoticeCount);
//                        break;
//                    case Constants.Counts.MSG_INIT_NOTICE_COUNT_PATIENT:
//                        TextView content = (TextView) headerView.findViewById(R.id.txt_content);
//                        txtNewNoticeCount.setVisibility(View.GONE);
////                        content.setTextColor(getResources().getColor(R.color.bg_title));
//                        content.setTextColor(getResources().getColor(R.color.txt_patient));
//                        handleNewMessageCount(0);
//                        break;
//                    case Constants.Counts.MSG_NEW_VIP_PATIENT:
//                        setChatSession();
//                        break;
//                }
//            }
//        };
//        //todo 需要修改
//        AppContext.getInstance().setNewNoticeHandler(h);
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setNoticeView();
//        setChatSession();
//        handleNewMessageCount(0);
//    }
//
//    private void handleNewMessageCount(int noticeCount) {
//        int count = noticeCount;
//        for (int i = 0; i < chatSessionList.size(); i++) {
//            if (chatSessionList.get(i).getLastMsgContent().equals(Constants.Counts.NEW_PATIENT_FLAG)) {
//                count = count + 1;
//            } else {
//                count = count + chatSessionList.get(i).getNewMsgCount();
//            }
//        }
//    //todo message notice
////        if (count > 0) {
////            txtTip.setVisibility(View.VISIBLE);
////            txtTip.setText(count + "");
////        } else {
////            txtTip.setVisibility(View.GONE);
////        }
//    }
//
//    void setChatSession() {
////        List<ChatSession> sessionList = ChatManager.getInstance().getChatSessions();
//        List<ChatSession> sessionList = ChatManager.getInstance().getChatSessionsWithProductIcon();
//        if (sessionList != null) {
//            chatSessionList.clear();
//            chatSessionList.addAll(sessionList);
//            setChatSessionAdapter();
//        }
//    }
//
//    void setNoticeView() {
//        Notice notice = NoticeManager.getInstance().getFirstNotice();
//
//        ImageView header = (ImageView) headerView.findViewById(R.id.imgView_user_head);
//        TextView name = (TextView) headerView.findViewById(R.id.txt_user_name);
//        TextView content = (TextView) headerView.findViewById(R.id.txt_content);
//        TextView date = (TextView) headerView.findViewById(R.id.txt_time);
//        name.setText(getString(R.string.doctor_notice));
//        header.setImageResource(R.drawable.icon_notice);
//
//        if (null == notice) {
//            content.setText("");
//            date.setText("");
//        } else {
//            content.setText(notice.getContent());
//            date.setText(DateUtil.formatTime(notice.getCreatedTime()));
//        }
//
//        //todo 头像点击
////        headerView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Bundle bundle = new Bundle();
////                performGoAction("gotoNoticeList", null);
////            }
////        });
//    }
//}