//package com.yikang.heartmark.activity.chat;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.heartmark.R;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.yikang.heartmark.activity.chat.adapter.ChattingAdapter;
//import com.yikang.heartmark.application.AppContext;
//import com.yikang.heartmark.common.base.AbsBaseFragment;
//import com.yikang.heartmark.common.base.ReturnResult;
//import com.yikang.heartmark.common.business.chat.ChatDAO;
//import com.yikang.heartmark.common.business.chat.ChatManager;
//import com.yikang.heartmark.common.business.chat.MessageBuilder;
//import com.yikang.heartmark.common.business.other.StorageManager;
//import com.yikang.heartmark.common.util.DateUtil;
//import com.yikang.heartmark.common.util.ImageLoaderUtil;
//import com.yikang.heartmark.common.util.KeyBoardUtil;
//import com.yikang.heartmark.constant.Constants;
//import com.yikang.heartmark.model.chat.ChatMessage;
//import com.yikang.heartmark.model.chat.ChatSession;
//import com.yikang.heartmark.model.chat.ImageItem;
//import com.yikang.heartmark.model.chat.Media;
//import com.yikang.heartmark.model.chat.UserInfo;
//
//import org.jivesoftware.smack.util.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//
///**
// * Created by changliu on 2015/01/27.
// */
//public class BaseChattingFragment extends AbsBaseFragment implements MediaRecorder.OnErrorListener {
//    private static Logger logger = LoggerFactory.getLogger(BaseChattingFragment.class);
//
//    protected View view;
//    protected ListView chatListView;
//    protected LinearLayout uploadPhotoLineLayout;
//    protected EditText chatInput;
//    protected Button pressToTalkBtn;
//    protected ImageView backImg;
//
//    protected ImageView chatAdd, upLoadNativePhotoImgView, takePhotoImgView;
//    protected ImageButton chatVoice;
//    protected TextView titleTxt;
//    protected ChattingAdapter chattingAdapter;
//    protected boolean isShowUploadPhoto = false, isPressToTalk = false;
//    protected UserInfo userInfo;
//    protected Button sendButton;
//    protected MediaRecorder recorder;
//    protected ChatSession chatSession;
//    protected boolean sessionValid = false;
//    protected Handler handler;
//    protected ImageLoader imageLoader = ImageLoader.getInstance();
//    protected List<ChatMessage> chatMessages;
//    protected boolean canUploadVoice = false;
//    //    protected boolean hasRecordingPermission = false;
//    protected long recordStart;
//    protected int duration;
//    protected TextView noteTxt;
//    protected String newAudioFile;
//    protected CountDownTimer countDownTimer;
//    protected RelativeLayout recordingTipLayout;
//    protected ImageView imgVoiceIcon;
//    protected ImageView imgVoiceVolume;
//    protected TextView txtCountDown;
//    protected TextView txtVoiceTip;
//    protected VoiceThread voiceThread;//线程
//    protected boolean isInCountDown = false;
//
//    protected boolean isPlaying = false;
//    ImageView voiceIconView;
//    //    private VoicePlayClickListener currentPlayListener = null;
//    ChatMessage currentMessage = null;
//
//    protected static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
//    protected String IMAGE_FILE_NAME = "askdrCaptureImage%s.jpg";
//    protected String IMAGE_FOLDER = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
//    protected String newImageFilePath;
//
//    protected final static int NA = -1;
//    protected ImageView imgViewStar;
//
//    private MediaPlayer mp;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflateView(inflater, container);
//
//        Bundle bundle = getArguments();
//        if (!setupChatSession(getArguments()))
//            return view;
//
//        init();
//        return view;
//    }
//
//    protected void initExtra() {
//    }
//
//    void init() {
//        imgViewStar = (ImageView) view.findViewById(R.id.imgView_star);
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
//        handler = new Handler();
//        backImg = (ImageView) view.findViewById(R.id.imgView_title_back);
//        titleTxt = (TextView) view.findViewById(R.id.txt_title);
//        uploadPhotoLineLayout = (LinearLayout) view.findViewById(R.id.lineLayout_upload_photo);
//        chatInput = (EditText) view.findViewById(R.id.edt_chat_input);
//        pressToTalkBtn = (Button) view.findViewById(R.id.btn_press_to_talk);
//        chatListView = (ListView) view.findViewById(R.id.lstView_chat);
//        chatVoice = (ImageButton) view.findViewById(R.id.imgBtn_chat_voice);
//        chatAdd = (ImageView) view.findViewById(R.id.imgView_add);
//        recordingTipLayout = (RelativeLayout) view.findViewById(R.id.relLayout_voice_tip);
//
//        imgVoiceIcon = (ImageView) recordingTipLayout.findViewById(R.id.imgView_voice_icon);
//        imgVoiceVolume = (ImageView) recordingTipLayout.findViewById(R.id.imgView_voice_volume);
//        txtCountDown = (TextView) recordingTipLayout.findViewById(R.id.txt_countdown);
//        txtVoiceTip = (TextView) recordingTipLayout.findViewById(R.id.txt_recording_tip);
//
//        sendButton = (Button) view.findViewById(R.id.btn_send_message);
//        sendButton.setOnClickListener(viewClickListener);
//        chatVoice.setOnClickListener(viewClickListener);
//        chatAdd.setOnClickListener(viewClickListener);
//        backImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                KeyBoardUtil.hide(getActivity());
//                goBack2ChatSessions();
//            }
//        });
//        pressToTalkBtn.setOnTouchListener(touchListener);
//        upLoadNativePhotoImgView = (ImageView) view.findViewById(R.id.imgView_upload_native_photo);
//        takePhotoImgView = (ImageView) view.findViewById(R.id.imgView_take_photo);
//        upLoadNativePhotoImgView.setOnClickListener(viewClickListener);
//        takePhotoImgView.setOnClickListener(viewClickListener);
//        titleTxt.setText(chatSession.getUserNickName());
//
//        ImageView iconFee = (ImageView) view.findViewById(R.id.ic_level_patient);
//        if (iconFee != null) {
//            ImageLoaderUtil.getInstance().loadImage(imageLoader, AppContext.getAppContext().getDownloadUrl(chatSession.productIconId), iconFee);
//        }
//
//        initExtra();
//
//        chatInput.setOnTouchListener(inputTouchListener);
//        chatInput.addTextChangedListener(textChangedListener);
//
////        chatInput.setOnKeyListener(new View.OnKeyListener() {
////            @Override
////            public boolean onKey(View view, int i, KeyEvent keyEvent) {
////
////                if (keyEvent != null) {
////                    sendMessage();
////                    return true;
////                }
////
////                return false;
////            }
////        });
//        setMessageHandle();
//        initChatMessages();
//
//        chattingAdapter = setupChattingAdapter();
//        setupChatListView();
//
//        chatListView.setOnTouchListener(chatListViewOnTouchListener);
//        setupCountDownTimer();
//        StorageManager.getInstance().setupSessionFolder(chatSession.getSessionId());
//    }
//
//    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
//        return null;
//    }
//
//    protected void gotoDoctorProfile() {
//    }
//
//    protected void gotoPatientProfile() {
//    }
//
//    protected View.OnClickListener viewClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            processViewOnClick(view);
//        }
//    };
//
//    TextWatcher textChangedListener = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//            sendButton.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//            if (charSequence.toString().equals("")) {
//                sendButton.setVisibility(View.INVISIBLE);
//            } else {
//                sendButton.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//        }
//    };
//
//    View.OnTouchListener touchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            return proceeViewOnTouch(view, motionEvent);
//        }
//    };
//
//    View.OnTouchListener inputTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                uploadPhotoLineLayout.setVisibility(View.GONE);
//            }
//            return false;
//        }
//    };
//
//    View.OnTouchListener chatListViewOnTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                KeyBoardUtil.hideSoftKeyBoard(getActivity(), view);
//            }
//            return false;
//        }
//    };
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUESTCODE_TAKE:// 调用相机拍照
//                    try {
//                        if (newImageFilePath == null || newImageFilePath.isEmpty())
//                            return;
//                        sendPhoto(newImageFilePath);
//                    } catch (Exception e) {
//                        logger.error(e.toString(), e);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//
//    private void goBack2ChatSessions() {
//        chatSession.setNewMsgCount(0);
//        ChatManager.getInstance().resetNewMsgCount(chatSession.getSessionId());
//        Intent intent = new Intent(Constants.FILTER_ACTION_TO_CHAT_SESSIION);
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //todo
//                //performBackToMainRoot();
//            }
//        }, 20);
//
//    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            goBack2ChatSessions();
//            return true;
//        }
//        return false;
//    }
//
//    void sendMessage() {
//        if (!StringUtils.isEmpty(chatInput.getText().toString())) {
//            ChatMessage chatMessage = MessageBuilder.buildNewTextMessage(chatSession, chatInput.getText().toString());
//            ChatManager.getInstance().sendMessage(chatSession, chatMessage);
//            chatInput.setText("");
//            KeyBoardUtil.hideSoftKeyBoard(getActivity(), view);
//            chatListView.setSelection(chatMessages.size() - 1);
//
//        }
//    }
//
//    public boolean isHaveRelation() {
////        long toUserId = chatSession.getUserId();
////        if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
////            Patient patient = DoctorManager.getInstance().getPatientfromDB(toUserId);
////            if (patient != null) {
////                return !(patient.getStatus() == Constants.Relation.RELATION_CODE_RELEASE || patient.getStatus() == Constants.Relation.RELATION_CODE_RELEASED);
////            }
////        } else {
////            Doctor doctor = PatientManager.getInstance().getDoctorfromDB(toUserId);
////            if (doctor != null) {
////                return !(doctor.getStatus() == Constants.Relation.RELATION_CODE_RELEASE || doctor.getStatus() == Constants.Relation.RELATION_CODE_RELEASED);
////            }
////        }
//
//        return false;
//    }
//
//    void showRelieveRelationShipDialog() {
////        final CommonPopUpWindow hasReleaseTip = new CommonPopUpWindow(getActivity(), R.layout.dialog_has_release_tip);
////        final boolean isDoctor = AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR ? true : false;
////        hasReleaseTip.registerDismissClick(R.id.btn_left);
////        TextView txtContent = (TextView) hasReleaseTip.getPoPView().findViewById(R.id.txt_tip_content);
////        final Button btn_deletePatient = (Button) hasReleaseTip.getPoPView().findViewById(R.id.btn_deletePatient);
////        if (!isDoctor) {
////            btn_deletePatient.setText(R.string.del_doctor);
////        } else {
////            btn_deletePatient.setText(R.string.delete_patient);
////        }
////        txtContent.setText(getString(R.string.relieve_relation_part_one) + chatSession.getUserNickName() +
////                getString(R.string.relieve_relation_part));
////        hasReleaseTip.registerClick(R.id.btn_deletePatient, new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (isDoctor) {
////                    //删除患者
////                    DoctorManager.getInstance().updatePatientRelation(
////                            chatSession.getHostUserId(),
////                            chatSession.getUserId(), Constants.Relation.RELATION_CODE_RELEASE);
////                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
////                    Message msg = new Message();
////                    msg.what = Constants.Counts.MSG_NEW_RELATION_DOCTOR;
////                    if (null != AppContext.getAppContext().getNewCountDoctorHandle()) {
////                        AppContext.getAppContext().getNewCountDoctorHandle().sendMessage(msg);
////                    }
////                } else {
////                    PatientManager.getInstance().updateDoctorRelation(
////                            chatSession.getHostUserId(),
////                            chatSession.getUserId(), Constants.Relation.RELATION_CODE_RELEASE);
////                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
////                    Message msg = new Message();
////                    msg.what = Constants.Counts.MSG_NEW_VIP_PATIENT;
////                    if (null != AppContext.getAppContext().getNewNoticeHandler()) {
////                        AppContext.getAppContext().getNewNoticeHandler().sendMessage(msg);
////                    }
////                }
////
////                performBackToMainRoot();
////
////                hasReleaseTip.dismiss();
////            }
////        });
//    }
//
//    void reSendMessage(int id) {
//        ChatMessage message = chatMessages.get(id);
//        chatMessages.remove(id);
//        if (message.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_TEXT) {
//            String sendStr = message.getContent();
//            ChatMessage chatMessage = MessageBuilder.buildResendTextMessage(chatSession, sendStr, message.getMessageId());
//            ChatManager.getInstance().sendMessage(chatSession, chatMessage);
//            chatInput.setText("");
//            KeyBoardUtil.hideSoftKeyBoard(getActivity(), view);
////            chatListView.setSelection(chatMessages.size() - 1);
//        } else if (message.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO) {
//            Media media = message.getMedia();
//            if (media == null) {
//                return;
//            }
//            ChatMessage chatMessage = MessageBuilder.buildResendAudioMessage(message.getMessageId(), chatSession, media.getFullName(), media.getDuration(), media.getObjectSize(), Constants.Chat.CHAT_MIME_TYPE_AMR);
//            ChatManager.getInstance().prepareSendMediaMessage(chatSession, chatMessage);
//        }
//    }
//
//    void sendPhoto(String filePath) {
//        File file = new File(filePath);
//        if (file.exists()) {
//            //TODO Chang , later need to decide LARGE or RAW by user
//            ChatMessage chatMessage = MessageBuilder.buildNewImageMessage(chatSession, filePath, Constants.Chat.CHAT_IMAGE_STYLE_LARGE, (int) file.length(), false);
//            ChatManager.getInstance().prepareSendMediaMessage(chatSession, chatMessage);
//        }
//    }
//
//    void resendPhoto(String filePath, ChatMessage message) {
//        File file = new File(filePath);
//        if (file.exists()) {
//            //TODO Chang , later need to decide LARGE or RAW by user
//            ChatMessage chatMessage = MessageBuilder.buildResendImageMessage(message.getMessageId(), chatSession, filePath, Constants.Chat.CHAT_IMAGE_STYLE_LARGE, (int) file.length(), true);
//            ChatManager.getInstance().prepareSendMediaMessage(chatSession, chatMessage);
////            ChatManager.getInstance().startAsyncMediaOperation(MediaService.ACTION_UPLOAD_MEDIA, message.getMedia());
//        }
//    }
//
//    void sendAudio() {
//        if (newAudioFile == null || newAudioFile.isEmpty()) {
//            logger.error("no audio file found to send");
//            setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.VISIBLE, getString(R.string.recording_error_try_again), 0, View.VISIBLE);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                    setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.GONE, null, 0, View.GONE);
//                }
//            }, 1000);
//            return;
//        }
//
//        int vDuration = duration;
//        long size = 0;
//
//        try {
//            File file = new File(newAudioFile);
//            size = file.length();
//            MediaPlayer mp = MediaPlayer.create(getActivity(), Uri.parse(newAudioFile));
//            if (mp == null) { //xiaomi phone no try/catch the Exception
//                handler.removeCallbacks(voiceThread);
//                Toast.makeText(getActivity(), R.string.no_audio_permission, Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                vDuration = mp.getDuration();//即为时长 是ms
//                mp.release();
//            }
//        } catch (Exception e) {
//            //Toast.makeText(getActivity(), R.string.no_audio_permission, Toast.LENGTH_SHORT).show();
//            logger.error(e.toString(), e);
//            //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
////            setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.VISIBLE, getString(R.string.recording_error_try_again), 0, View.VISIBLE);
////            handler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
////                    setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.GONE, null, 0, View.GONE);
////                }
////            }, 1000);
//        }
//        logger.debug("[recording] size=" + size + " duration=" + vDuration);
//        if (vDuration < 900) { //09.s
//            logger.error("can't send audio file with zero or 1s duration");
//            handler.removeCallbacks(voiceThread);
//            setupRecordingUI(View.VISIBLE, R.drawable.vocie_icon_warning, View.GONE, NA, View.GONE, null, View.VISIBLE, getString(R.string.time_too_short), 0, View.VISIBLE);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                    setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.GONE, null, 0, View.GONE);
//                }
//            }, 2000);
//            return;
//        } else {
//            setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.GONE, null, 0, View.GONE);
//        }
//        vDuration = vDuration + 500;
//        ChatMessage chatMessage = MessageBuilder.buildNewAudioMessage(chatSession, newAudioFile, vDuration, (int) size, Constants.Chat.CHAT_MIME_TYPE_AMR, false);
//        ChatManager.getInstance().prepareSendMediaMessage(chatSession, chatMessage);
//    }
//
//    public void reSendPhoto(final int position) {
//        //todo
////        final ChatMessage chatMessage = chatMessages.get(position);
////        final CommonPopUpWindow tipPopWindow = new CommonPopUpWindow(getActivity(), R.layout.dialog_common);
////        tipPopWindow.registerDismissClick(R.id.layout_bg);
////        tipPopWindow.registerDismissClick(R.id.btn_left);
////        tipPopWindow.setTextContent(R.id.txt_title, getString(R.string.chat_is_resend_photo));
////        tipPopWindow.registerClick(R.id.btn_right, new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                chatMessages.remove(position);
////                resendPhoto(chatMessage.getMedia().getFullName(), chatMessage);
////                tipPopWindow.dismiss();
////            }
////        });
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mp != null) {
//            mp.release();
//        }
//    }
//
//    void playAudio(int id, final ChattingAdapter.OnVoicePlayEnd onVoicePlayEnd) {
//        if (isPlaying) {
//            mp.release();
//        }
//
//        try {
//            ChatMessage chatMessage = chatMessages.get(id);
//            String audioFile = chatMessage.getMedia().getFullName();
//            mp = MediaPlayer.create(getActivity(), Uri.parse(audioFile));
//            final Timer timer = new Timer();
//            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.release();
//                    timer.cancel();
//                    //播放完毕回调
//                    onVoicePlayEnd.OnPlayEnd();
//                    isPlaying = false;
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                            setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.GONE, null, 0, View.GONE);
//                        }
//                    }, 200);
//                }
//            });
//
//            final AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
//            mp.setLooping(false);
//            mp.start();
//            isPlaying = true;
//            //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//            //setupRecordingUI(View.VISIBLE, R.drawable.voice_recording, View.VISIBLE,R.drawable.voice_volume_1, View.GONE,null, View.GONE,null,0,View.VISIBLE);
//
////            timer.schedule(new TimerTask() {
////                @Override
////                public void run() {
////                    int volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
////                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
////                    final float ampLevel = (float) volumeLevel / maxVolume;
////                    logger.debug("[playing] volumeLevel=" + volumeLevel + " maxVolume=" + maxVolume + " volume=" + ampLevel);
////
////                    getActivity().runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            try {
////                                if (ampLevel < 0.2)
////                                    imgVoiceVolume.setImageResource(R.drawable.voice_volume_1);
////                                else if (ampLevel < 0.4)
////                                    imgVoiceVolume.setImageResource(R.drawable.voice_volume_2);
////                                else if (ampLevel < 0.6)
////                                    imgVoiceVolume.setImageResource(R.drawable.voice_volume_3);
////                                else if (ampLevel < 0.8)
////                                    imgVoiceVolume.setImageResource(R.drawable.voice_volume_4);
////                                else
////                                    imgVoiceVolume.setImageResource(R.drawable.voice_volume_5);
////                            } catch (Exception e) {
////                                logger.error(e.toString(), e);
////                            }
////                        }
////                    });
////                }
////            }, 500, 500);
//
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
//
//    public void showUploadPhoto(int position) {
//        Bundle bundle = new Bundle();
//        if (chatMessages.get(position).getDirection() == Constants.Chat.CHAT_DIRECTION_RECEIVE) {
//            String objectId = chatMessages.get(position).getMedia().getObjectId();
//            String largeImageId = chatMessages.get(position).getMedia().getLargeImageId();
//            if (objectId.equals(largeImageId)) {
//                bundle.putString(Constants.Chat.CHAT_IMAGE_OBJECT_ID, largeImageId);
//            } else {
//                bundle.putString(Constants.Chat.CHAT_IMAGE_OBJECT_ID, objectId);
//            }
//        } else {
//            bundle.putString(Constants.Chat.CHAT_IMAGE_ATTACHFILE, chatMessages.get(position).getMedia().getFullName());
//        }
//        bundle.putString(Constants.Chat.CHAT_MESSAGE, "MEDIA_MESSAGE");
//        bundle.putString(Constants.Chat.CHAT_SESSION, String.valueOf(chatSession.getSessionId()));
//        performGoAction("gotoPhotoDisplay", bundle);
//    }
//
//    void showUploadPhotoView() {
//        if (isShowUploadPhoto) {
//            uploadPhotoLineLayout.setVisibility(View.GONE);
//        } else {
//            uploadPhotoLineLayout.setVisibility(View.VISIBLE);
//        }
//        isShowUploadPhoto = !isShowUploadPhoto;
//        KeyBoardUtil.hideSoftKeyBoard(getActivity(), chatInput);
//        chatListView.setSelection(chatMessages.size() - 1);
//    }
//
//    /**
//     * 录音
//     */
//    void showPressToTalk() {
//        uploadPhotoLineLayout.setVisibility(View.GONE);
//        sendButton.setVisibility(View.INVISIBLE);
//        if (!isPressToTalk) {
//            chatVoice.setImageResource(R.drawable.chat_icon_keybord_sel);
//            pressToTalkBtn.setVisibility(View.VISIBLE);
//            chatInput.setVisibility(View.INVISIBLE);
//            if (imgViewStar != null) {
//                imgViewStar.setVisibility(View.GONE);
//            }
//        } else {
//            chatVoice.setImageResource(R.drawable.chat_icon_voice_selector);
//            pressToTalkBtn.setVisibility(View.GONE);
//            chatInput.setVisibility(View.VISIBLE);
//            if (imgViewStar != null) {
//                imgViewStar.setVisibility(View.VISIBLE);
//            }
//
//            sendButton.setVisibility(TextUtils.isEmpty(chatInput.getText()) ? View.INVISIBLE : View.VISIBLE);
//        }
//        isPressToTalk = !isPressToTalk;
//        //KeyBoardUtil.hideSoftKeyBoard(getActivity(), chatInput);
//    }
//
//    protected void addImageNote(View view, int id) {
//    }
//
//    private void startRecording() {
//
//        String newFile = StorageManager.getInstance().getNewAudioFileName("amr");
//        newAudioFile = StorageManager.getInstance().createFile(newFile, chatSession.getSessionId());
//        if (newAudioFile == null || newAudioFile.isEmpty()) {
//            logger.error("can't create new file " + newAudioFile);
//            return;
//        }
//
//        recorder = new MediaRecorder();// new出MediaRecorder对象
//        if (recorder == null) {
//            return;
//        }
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        recorder.setOutputFile(newAudioFile);
//        recorder.setMaxDuration(60000);
//        duration = 0;
//        recordStart = System.currentTimeMillis();
//        try {
//            recorder.prepare();// 准备录制
//            recorder.start();// 开始录制
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), R.string.no_audio_permission, Toast.LENGTH_SHORT).show();
//            logger.error(e.toString(), e);
//            return;
//        }
//        countDownTimer.start();
////            MediaPlayer mp = MediaPlayer.create(getActivity(), Uri.parse(newAudioFile));
////            if (mp == null) {
////                final CommonPopUpWindow noPermissionPop = new CommonPopUpWindow(getActivity(), R.layout.dialog_common_tip);
////                View contentView = noPermissionPop.getPoPView();
////                TextView txtContent = (TextView) contentView.findViewById(R.id.txt_content);
////                txtContent.setText(getString(R.string.recording_tip));
////                noPermissionPop.registerDismissClick(R.id.layout_bg);
////                noPermissionPop.registerClick(R.id.btn_yes, new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        startActivity(new Intent(Settings.ACTION_SETTINGS));
////                        noPermissionPop.dismiss();
////                    }
////                });
////
////                hasRecordingPermission = false;
////                return;
////            } else {
////                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
////                hasRecordingPermission = true;
////                mp.release();
////            }
//
//        isInCountDown = false;
////        catch (IllegalStateException e) {
////            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
////            logger.error(e.toString(), e);
////        } catch (IOException e) {
////            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
////            logger.error(e.toString(), e);
////        }
//        voiceThread = new VoiceThread();
//        handler.postDelayed(voiceThread, 1000);
//
//    }
//
//    private void stopRecording() {
//        logger.debug("[recording] stopRecording");
//
//        try {
//            countDownTimer.cancel();
//            duration = (int) (System.currentTimeMillis() - recordStart);
//
//            if (recorder == null) {
//                logger.debug("[recording] stopRecording recorder=null");
//                return;
//            }
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        logger.debug("[recording] postDelayed to turnoff UI");
//                        //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                        setupRecordingUI(View.GONE, NA, View.GONE, NA, View.GONE, null, View.GONE, null, NA, View.GONE);
//                    } catch (Exception e) {
//                        logger.error(e.toString(), e);
//                    }
//                }
//            }, 1000);
//        } catch (Exception e) {
//            logger.debug("[recording] stopRecording error=" + e.getMessage());
//            logger.error(e.toString(), e);
//        }
//    }
//
//    protected void setAdd2NotePopMenu(View view, final int id) {
//    }
//
//    private void setMessageHandle() {
//        Handler h = new Handler() {
//            public void handleMessage(Message msg) {
//                Bundle data = msg.getData();
//                ChatMessage message = (ChatMessage) data.getSerializable(Constants.Chat.CHAT_MESSAGE);
//
//                if (null == message) {
//                    return;
//                }
//                long sessionId = chatSession.getSessionId();
//                if (sessionId != message.getSessionId()) {
//                    return;
//                }
//                boolean isNew = false;
//                switch (msg.what) {
//                    case Constants.Chat.CHAT_MESSAGE_SENT:
//                        logger.debug("Chatting Room:CHAT_MESSAGE_SENT");
//
////                        for (int i = 0; i < chatMessages.size(); i++) {
////                            if (chatMessages.get(i).getMessageId().equals(message.getMessageId())) {
////                                chatMessages.set(i, message);
////                                isNew = false;
////                                break;
////                            }
////                        }
//
//                        chatMessages.add(message);
//
//                        chattingAdapter.notifyDataSetChanged();
//                        chatListView.setSelection(chatListView.getCount() - 1);
//                        break;
//                    case Constants.Chat.CHAT_MESSAGE_RECEIVED:
//                        logger.debug("Chatting Room:CHAT_MESSAGE_RECEIVED");
//                        isNew = true;
//                        for (int i = 0; i < chatMessages.size(); i++) {
//                            if (chatMessages.get(i).getMessageId().equals(message.getMessageId())) {
//                                chatMessages.set(i, message);
//                                isNew = false;
//                                break;
//                            }
//                        }
//
//                        if (isNew) {
//                            chatMessages.add(message);
//                        }
//                        chattingAdapter.notifyDataSetChanged();
//                        chatListView.setSelection(chatListView.getCount() - 1);
//
//                        break;
//                    case Constants.Chat.CHAT_MESSAGE_CHANGED:
//                        logger.debug("Chatting Room:CHAT_MESSAGE_CHANGED");
//                        for (int i = 0; i < chatMessages.size(); i++) {
//                            if (chatMessages.get(i).getMessageId().equals(message.getMessageId())) {
//                                chatMessages.set(i, message);
//                                break;
//                            }
//                        }
//
//                        chattingAdapter.notifyDataSetChanged();
//                        chatListView.setSelection(chatListView.getCount() - 1);
//                        break;
//                    case Constants.Chat.CHAT_MESSAGE_REPLACE:
//                        logger.debug("Chatting Room:CHAT_MESSAGE_REPLACE");
//                        message = (ChatMessage) data.getSerializable("message");
//                        for (int i = 0; i < chatMessages.size(); i++) {
//                            if (chatMessages.get(i).getMessageId().equals(message.getMessageId())) {
//                                chatMessages.remove(i);
//                                break;
//                            }
//                        }
//                        chatMessages.add(message);
//                        chattingAdapter.notifyDataSetChanged();
//                        chatListView.setSelection(chatListView.getCount() - 1);
//                        break;
//
//                    case Constants.Chat.CHAT_MESSAGE_DOWNLOAD_MEDIA:
//                        //NO need to implement
//                        //Media media = (Media) data.getSerializable("media");
//                        //MediaManager.getInstance().loadImage(chatSession,media, Constants.Http.HTTP_DOWNLOAD_IMAGE_THUMBNAIL,new ImageLoader(), new ImageView());
//                        break;
//                }
//            }
//        };
//
//        AppContext.getAppContext().setChatMessageHandler(h);
//    }
//
//    @Override
//    public void onError(MediaRecorder mr, int what, int extra) {
//        logger.error(what + extra + "");
//    }
//
//    private void processViewOnClick(View view) {
//        KeyBoardUtil.hide(getActivity());
//        if (!isHaveRelation()) {
//            showRelieveRelationShipDialog();
//            return;
//        }
//        switch (view.getId()) {
//            case R.id.imgBtn_chat_voice:
//                showPressToTalk();
//                break;
//            case R.id.imgView_add:
//                showUploadPhotoView();
//                break;
//            case R.id.imgView_take_photo:
//                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                String imageName = String.format(IMAGE_FILE_NAME, DateUtil.formatyyyyMMddHHMMss(new Date()));
//                File file = new File(IMAGE_FOLDER, imageName);
//                newImageFilePath = file.getPath();
//                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
//                break;
//            case R.id.btn_send_message:
//                sendMessage();
//                break;
//            case R.id.imgView_upload_native_photo:
//                Bundle bundle = new Bundle();
//                bundle.putInt(Constants.PHOTO_SELECTED_MAX_ALLOW_SELECT_NUMBER, 9);
//                performGoAction("gotoAlbum", bundle, new ReturnResult() {
//                    @Override
//                    public void onResult(int resultCode, Bundle resultData) {
//                        uploadPhotoLineLayout.setVisibility(View.GONE);
//                        ArrayList<ImageItem> thisSelectedImages = (ArrayList) resultData.getSerializable(Constants.PHOTO_SELECTED_SELECTED_IMAGES);
//                        for (int i = 0; i < thisSelectedImages.size(); i++) {
//                            sendPhoto(thisSelectedImages.get(i).imagePath);
//                        }
//                    }
//                });
//
//                break;
//
//            case R.id.txt_title_note:
//                Bundle newBundle = new Bundle();
//                //todo
////                Patient patient = DoctorManager.getInstance().getPatientfromDB(chatSession.getUserId());
////                newBundle.putSerializable(Constants.PATIENT, patient);
////                performGoAction("gotoNoteList", newBundle);
//                break;
//        }
//    }
//
//
//    private boolean proceeViewOnTouch(View view, MotionEvent motionEvent) {
////        if (!CommonUtil.isHavePermission("android.permission.RECORD_AUDIO")) {
////            Toast.makeText(getActivity(), "没权限", Toast.LENGTH_SHORT).show();
////            return false;
////        }
//        float y1 = 0, y2 = 0, dy = 0, disY = 0;
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                y1 = motionEvent.getY();
//                startRecording();
//                canUploadVoice = true;
//                break;
//            case MotionEvent.ACTION_MOVE:
////                if (!hasRecordingPermission) break;
//                if (isInCountDown)
//                    break;
//
//                y2 = motionEvent.getY();
//                dy = y1 - y2;
//                y1 = y2;
//                disY = disY + dy;
//                if (disY > 50) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            canUploadVoice = false;
//                            //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                            setupRecordingUI(NA, R.drawable.voice_release, NA, NA, NA, null, NA, getString(R.string.release_to_cancel), R.drawable.voice_release_text_bg, NA);
//                            logger.debug("[recording] 松开手指，取消发送 canUploadVoice=false");
//
//                        }
//                    }, 200);
//                } else {
//                    canUploadVoice = true;
//                    //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                    setupRecordingUI(NA, R.drawable.voice_recording, NA, NA, NA, null, NA, getString(R.string.move_to_cancel), 0, NA);
//                    logger.debug("[recording] 手指上滑，取消发送 canUploadVoice=true");
//                }
//                break;
//            case MotionEvent.ACTION_UP:
////                if (!hasRecordingPermission) break;
//                stopRecording();
//                if (canUploadVoice) {
//                    sendAudio();
//                }
//                break;
//            default:
//                break;
//        }
//        return false;
//
//    }
//
//    private void setupCountDownTimer() {
//        countDownTimer = new CountDownTimer(30000, 100) {
//
//            public void onTick(long millisUntilFinished) {
//
//                long remindingSeconds = millisUntilFinished / 1000;
//                logger.debug("[recording] countdown remindingSeconds=" + remindingSeconds);
//
//                if (remindingSeconds > 11) {
//                    int amplitude = recorder.getMaxAmplitude();// todo null
//                    double ampLevel = amplitude / 32768.00;
//                    if (ampLevel < 0.2)
//                        imgVoiceVolume.setImageResource(R.drawable.voice_volume_1);
//                    else if (ampLevel < 0.4)
//                        imgVoiceVolume.setImageResource(R.drawable.voice_volume_2);
//                    else if (ampLevel < 0.6)
//                        imgVoiceVolume.setImageResource(R.drawable.voice_volume_3);
//                    else if (ampLevel < 0.8)
//                        imgVoiceVolume.setImageResource(R.drawable.voice_volume_4);
//                    else
//                        imgVoiceVolume.setImageResource(R.drawable.voice_volume_5);
//                } else {
//                    isInCountDown = true;
//                    long actSec = remindingSeconds - 1;
//                    if (actSec < 1) {
//                        logger.debug("[recording] countdown to last second " + actSec);
//
//                        //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                        setupRecordingUI(
//                                View.VISIBLE, R.drawable.vocie_icon_warning,
//                                NA, NA,
//                                View.GONE, null,
//                                NA, getString(R.string.time_too_long), NA,
//                                NA);
//
//                    } else {
//                        logger.debug("[recording] countdown in progress " + actSec);
//
//                        //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//                        setupRecordingUI(
//                                View.GONE, R.drawable.vocie_icon_warning,
//                                View.GONE, NA,
//                                View.VISIBLE, "" + actSec,
//                                NA, getString(R.string.move_to_cancel), NA,
//                                NA);
//                    }
//                }
//            }
//
//            public void onFinish() {
//                logger.debug("[recording] countdown complete");
//                stopRecording();
//            }
//        };
//    }
//
//    private void setupRecordingUI(int iconView, int iconResId, int volumeView, int volumeResId, int countDownView, String countDownText,
//                                  int voiceTipView, String tipText, int voiceTipBgResId, int layoutView) {
//        if (voiceTipBgResId >= 0) txtVoiceTip.setBackgroundResource(voiceTipBgResId);
//        if (tipText != null) txtVoiceTip.setText(tipText);
//        if (voiceTipView >= 0) txtVoiceTip.setVisibility(voiceTipView);
//        if (iconResId >= 0) imgVoiceIcon.setImageResource(iconResId);
//        if (iconView >= 0) imgVoiceIcon.setVisibility(iconView);
//        if (volumeResId >= 0) imgVoiceVolume.setImageResource(volumeResId);
//        if (volumeView >= 0) imgVoiceVolume.setVisibility(volumeView);
//        if (countDownText != null) txtCountDown.setText(countDownText);
//        if (countDownView >= 0) txtCountDown.setVisibility(countDownView);
//        if (layoutView >= 0) recordingTipLayout.setVisibility(layoutView);
//
//    }
//
//    private boolean setupChatSession(Bundle bundle) {
//        chatSession = (ChatSession) bundle.getSerializable(Constants.Chat.CHAT_SESSION);
//        if (chatSession == null) {
//            userInfo = (UserInfo) bundle.getSerializable(Constants.USER_INFO);
//            if (userInfo == null) {
//                logger.error("onCreateView userInfo is null!");
//                return false;
//            }
//            chatSession = ChatManager.getInstance().getOrCreateChatSession(AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId(), userInfo);
//            if (chatSession == null) {
//                logger.error("onCreateView chatsession is null!");
//                return false;
//            }
//        } else {
//            if (chatSession.getLastMsgContent().equals(Constants.Counts.NEW_PATIENT_FLAG)) {
////                if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//                chatSession.setLastMsgContent(AppContext.getAppContext().getString(R.string.chat_message_new_relation));
////                } else {
////                    chatSession.setLastMsgContent(AppContext.getAppContext().getString(R.string.chat_message_new_relation_patient));
////                }
//
//                ChatDAO.getInstance().updateChatSession(chatSession);
//            }
//        }
//
//
//        return true;
//    }
//
//    private ChattingAdapter setupChattingAdapter() {
//        ChattingAdapter chattingAdapter = new ChattingAdapter(getActivity(), chatMessages, imageLoader, new FragmentCallback() {
//            @Override
//            public void requestResendPhoto(int id) {
//                reSendPhoto(id);
//            }
//
//            @Override
//            public void showLargePhoto(int id) {
//                showUploadPhoto(id);
//            }
//
//            @Override
//            public void addTextToNote(View view, int id) {
//                setAdd2NotePopMenu(view, id);
//            }
//
//            @Override
//            public void reSendContent(int id) {
//                reSendMessage(id);
//            }
//
//            @Override
//            public void addImageToNote(View view, int id) {
//                addImageNote(view, id);
//            }
//
//            @Override
//            public void playVoice(int id, ChattingAdapter.OnVoicePlayEnd onVoicePlayEnd) {
//                playAudio(id, onVoicePlayEnd);
//            }
//
//            @Override
//            public void stopVoid(int id) {
//                stopVoice(id);
//            }
//
//            @Override
//            public void gotoProfile(String direction) {
//                logger.debug("gotoProfile - " + direction);
//                if (direction.equals(Constants.DOCTOR))
//                    gotoDoctorProfile();
//                else if (direction.equals(Constants.PATIENT))
//                    gotoPatientProfile();
//                else
//                    logger.warn("gotoProfile invalid direction=" + direction);
//            }
//
//        });
//
//        return chattingAdapter;
//    }
//
//    /**
//     * 关闭当前
//     *
//     * @param id
//     */
//    private void stopVoice(int id) {
//        if (mp != null) {
//            if (isPlaying) {
//                try {
//                    mp.stop();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                mp.release();
//            }
//        }
////        if (id > 0) {
////            playAudio(id, onVoicePlayEnd);
////        }
//    }
//
//    private void initChatMessages() {
//        chatMessages = ChatManager.getInstance().getChatMessages(chatSession.getSessionId());
//        if (null == chatMessages) {
//            chatMessages = new ArrayList<ChatMessage>();
//        } else {
//            for (int i = 0; i < chatMessages.size(); i++) {
//                chatMessages.get(i).setGuestProfilePicId(chatSession.getUserProfilePictureId());
//            }
//        }
//    }
//
//    private void setupChatListView() {
//        chatListView.setAdapter(chattingAdapter);
//        if (chatMessages.size() >= 1) {
//            chatListView.setSelection(chatMessages.size() - 1);
//        }
//    }
//
//    private class VoiceThread implements Runnable {
//        @Override
//        public void run() {
//            logger.debug("[recording] VoiceThread run. UI Visible");
//
//            //int iconView, int iconResId, int volumeView, int volumeResId, int countDownView,String countDownText, int voiceTipView, String tipText, int voiceTipBgResId, int layoutView
//            setupRecordingUI(
//                    View.VISIBLE, R.drawable.voice_recording,
//                    View.VISIBLE, R.drawable.voice_volume_1,
//                    View.INVISIBLE, null,
//                    View.VISIBLE, getString(R.string.move_to_cancel), 0,
//                    View.VISIBLE);
//
//        }
//    }
//
//}