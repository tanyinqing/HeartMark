//package com.yikang.heartmark.activity.chat.adapter;
//
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.heartmark.R;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.yikang.heartmark.common.base.AbsBaseFragment;
//import com.yikang.heartmark.common.business.other.AppCommonService;
//import com.yikang.heartmark.common.business.other.CommonManager;
//import com.yikang.heartmark.common.business.other.StorageManager;
//import com.yikang.heartmark.common.util.DateUtil;
//import com.yikang.heartmark.common.util.ImageLoaderUtil;
//import com.yikang.heartmark.constant.Constants;
//import com.yikang.heartmark.model.chat.ChatMessage;
//import com.yikang.heartmark.model.chat.Media;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
///**
// * Created by lepu on 2014/12/19.
// */
//public class ChattingAdapter extends BaseAdapter {
//    private static Logger logger = LoggerFactory.getLogger(ChattingAdapter.class);
//    private int lstPositonCache = -1;
//    private boolean isCurrentRunning = false;
//
//    List<ChatMessage> list;
//    Context context;
//    ImageLoader imageLoader;
//    DisplayImageOptions displayImgOptions;
//
//    private AbsBaseFragment.FragmentCallback callback;
//    private AnimationDrawable lstDrable; //旧动画
//    private AnimationDrawable curDrable;//当前动画
//    private ImageView imageViewCache;
//
//    public ChattingAdapter(Context context, List<ChatMessage> list, ImageLoader imageLoader, AbsBaseFragment.FragmentCallback callback) {
//        this.list = list;
//        this.context = context;
//        this.imageLoader = imageLoader;
//        this.callback = callback;
//        displayImgOptions = new DisplayImageOptions.Builder().
//                considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
//                imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
//                showImageOnLoading(R.drawable.chat_bg_default).
//                build();
//
//        lstDrable = new AnimationDrawable();
//        curDrable = new AnimationDrawable();
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
//        ViewHolder holder = null;
//        String url = "";
////        final boolean isFromPatient = AppContext.getAppContext().getApplicationType() == Constants.AppTypes.PATIENT;
//        //todo
//        final boolean isFromPatient = false;
//
//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.item_chatting_list, null);
//            holder = new ViewHolder();
//
//            holder.laout_sender_chatting_content = (RelativeLayout) view.findViewById(R.id.laout_sender_chatting_content);
//            holder.layout_receive_chatting_content = (RelativeLayout) view.findViewById(R.id.layout_receive_chatting_content);
//            holder.sendVoiceTime = (TextView) view.findViewById(R.id.send_voice_time);
//            holder.receiveVoiceTime = (TextView) view.findViewById(R.id.receive_voice_time);
//
//            holder.senderHeader = (ImageView) view.findViewById(R.id.imgView_sender_header);
//            holder.receiveHeader = (ImageView) view.findViewById(R.id.imgView_receive_header);
//            holder.senderTxt = (TextView) view.findViewById(R.id.txt_sender_chatting_content);
//            holder.receiveTxt = (TextView) view.findViewById(R.id.txt_receive_chatting_content);
//            holder.receivePhoto = (ImageView) view.findViewById(R.id.imgView_receive_photo);
//            holder.senderPhoto = (ImageView) view.findViewById(R.id.imgView_sender_photo);
//
//            holder.receiveMediaProgress = (ProgressBar) view.findViewById(R.id.pb_receive_media_progress);
//            holder.senderMediaProgress = (ProgressBar) view.findViewById(R.id.pb_sender_media_progress);
//            holder.receiveMediaResetImg = (ImageView) view.findViewById(R.id.receive_media_reset);
//            holder.senderMediaResetImg = (ImageView) view.findViewById(R.id.sender_media_reset);
//            holder.senderTextResendImg = (ImageView) view.findViewById(R.id.imgView_sender_text_resend);
//            holder.senderTextProgress = (ProgressBar) view.findViewById(R.id.pb_sender_text_resend);
//            holder.sendVoice = (ImageView) view.findViewById(R.id.imgView_sender_voice);
//            //todo
//            holder.receiveVoice = (ImageView) view.findViewById(R.id.imgView_receive_voice);
//            holder.time = (TextView) view.findViewById(R.id.chat_time);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//        //滑动时候
//        curDrable.stop();
//        lstDrable.stop();
//        lstPositonCache = -1;
//        isCurrentRunning = false;
//        holder.sendVoice.setBackgroundResource(R.drawable.icon_speech3_send);
//        holder.sendVoice.setImageResource(0);
//        holder.receiveVoice.setBackgroundResource(R.drawable.kb_audio_receive_3);
//        holder.receiveVoice.setImageResource(0);
//        imageViewCache = null;
//        //////////////////////////////
//
//        setVisibleGone(holder);
////        if (isFromPatient) {
////            holder.laout_sender_chatting_content.setBackgroundResource(R.drawable.chat_bg_patient_send);
////        } else {
//        holder.laout_sender_chatting_content.setBackgroundResource(R.drawable.chat_bg_doctor);
////        }
//        final ViewHolder finalHolder = holder;
//        holder.laout_sender_chatting_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View p) {
//                setVoiceClick(i, finalHolder);
//            }
//        });
//        holder.layout_receive_chatting_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setLeftVoiceClick(i, finalHolder);
//            }
//        });
//        holder.senderHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callback.gotoProfile(isFromPatient ? Constants.PATIENT : Constants.DOCTOR);
//            }
//        });
//
//        holder.receiveHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callback.gotoProfile(!isFromPatient ? Constants.PATIENT : Constants.DOCTOR);
//            }
//        });
//
//        final ChatMessage chatMessage = list.get(i);
//        if (i == 0) {
//            holder.time.setVisibility(View.VISIBLE);
//            holder.time.setText(DateUtil.formatChatDateTime(chatMessage.getSendTime()));
//        }
//        if (i > 0) {
//            if (DateUtil.isCloseEnough(chatMessage.getSendTime(), list.get(i - 1).getSendTime())) {
//                holder.time.setVisibility(View.GONE);
//            } else {
//                holder.time.setText(DateUtil.formatChatDateTime(chatMessage.getSendTime()));
//                holder.time.setVisibility(View.VISIBLE);
//            }
//        }
//
//        holder.receiveVoiceTime.setText("");
//        holder.sendVoiceTime.setText("");
//        holder.senderTxt.setText("");
//        holder.receiveTxt.setText("");
//        holder.senderTxt.setVisibility(View.GONE);
//        holder.receiveTxt.setVisibility(View.GONE);
//        holder.sendVoice.setVisibility(View.GONE);
//        holder.receiveVoice.setVisibility(View.GONE);
//
//        Media media = list.get(i).getMedia();
//        if (list.get(i).getDirection() == Constants.Chat.CHAT_DIRECTION_RECEIVE) { //接收到的
//            holder.receiveHeader.setVisibility(View.VISIBLE);
//            holder.layout_receive_chatting_content.setVisibility(View.VISIBLE);
//            //todo
////            Boolean isPatientApp = AppContext.getAppContext().getApplicationType() == Constants.AppTypes.PATIENT;
////            holder.receiveHeader.setImageResource(isPatientApp ? R.drawable.icon_header_default_patient : R.drawable.icon_header_default);
//            if (!list.get(i).getGuestProfilePicId().isEmpty()) {
//                url = AppCommonService.getInstance().getObjectDownloadUrl(list.get(i).getGuestProfilePicId());
//                ImageLoaderUtil.getInstance().loadImage(imageLoader, url, holder.receiveHeader);
//            }
//
//            int messageType = list.get(i).getMessageType();
//
//            switch (messageType) {
//                case 1://text
//                    holder.receiveTxt.setVisibility(View.VISIBLE);
//                    holder.receiveTxt.setText(list.get(i).getContent());
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.receiveTxt.getLayoutParams();
//                    params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                    holder.receiveTxt.setLayoutParams(params);
//                    if (isDoctorType()) {
//                        addTextNoteListener(list.get(i).getSendStatus(), holder.receiveTxt, i);
//                    }
//                    break;
//                case 2://image
//                    holder.receivePhoto.setVisibility(View.VISIBLE);
//                    url = CommonManager.getInstance().getMediaObjectFile(media, Constants.MediaObjectTypes.THUMBNAIL);
//                    if (url != null)
//                        ImageLoaderUtil.getInstance().loadImage(imageLoader, url, holder.receivePhoto, displayImgOptions);
//
//                    holder.receivePhoto.setVisibility(View.VISIBLE);
//                    holder.receivePhoto.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            callback.showLargePhoto(i);
//                        }
//                    });
//                    if (isDoctorType()) {
//                        addPhotoNoteListener(holder.receivePhoto, i);
//                    }
//                    break;
//                case 3://voice
//                    holder.receiveVoice.setVisibility(View.VISIBLE);
//                    holder.receiveTxt.setVisibility(View.VISIBLE);
////                    holder.receiveTxt.setText("");
//                    int duration = list.get(i).getMedia().getDuration();
//                    String time = String.valueOf(duration / 1000 == 0 ? 1 : duration / 1000) + "''";
//                    holder.receiveVoiceTime.setText(time);
//                    RelativeLayout.LayoutParams paramReceive = (RelativeLayout.LayoutParams) holder.receiveTxt.getLayoutParams();
//                    handleVoiceTextWidth(paramReceive, time);
//                    holder.receiveTxt.setLayoutParams(paramReceive);
//                    break;
//                case 4://voido
//                    break;
//            }
//
//        } else {//发送出来的
//            holder.laout_sender_chatting_content.setVisibility(View.VISIBLE);
//            holder.senderTxt.setVisibility(View.GONE);
//            holder.receiveTxt.setVisibility(View.GONE);
//            holder.sendVoice.setVisibility(View.GONE);
//            holder.receiveVoice.setVisibility(View.GONE);
//
//            holder.senderHeader.setVisibility(View.VISIBLE);
//            //Boolean isPatientApp = AppContext.getAppContext().getApplicationType() == Constants.AppTypes.PATIENT;
//            holder.receiveHeader.setImageResource(R.drawable.icon_header_default);
//            if (!list.get(i).getHostProfilePicId().isEmpty()) {
//                url = AppCommonService.getInstance().getObjectDownloadUrl(list.get(i).getHostProfilePicId());
//                ImageLoader.getInstance().displayImage(url, holder.senderHeader, new DisplayImageOptions.Builder().
//                        considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
//                        imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
//                        build());
////                ImageLoaderUtil.getInstance().loadImage(imageLoader, url, holder.senderHeader);
//            }
//
//            int messageID = list.get(i).getMessageType();
//            switch (messageID) {
//                case 1://text
//                    holder.senderTxt.setVisibility(View.VISIBLE);
//                    holder.senderTxt.setText(list.get(i).getContent());
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.senderTxt.getLayoutParams();
//                    params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                    holder.senderTxt.setLayoutParams(params);
//                    //holder.senderTxt.setWidth(200);
//
//                    if (isDoctorType()) {
//                        addTextNoteListener(list.get(i).getSendStatus(), holder.senderTxt, i);
//                    }
//                    addReSendContentListener(list.get(i).getSendStatus(), holder.senderTextResendImg, i);
//                    showTextProgress(list.get(i).getSendStatus(), holder.senderTextProgress);
//                    break;
//                case 2://image
//                    holder.senderPhoto.setVisibility(View.VISIBLE);
//                    if (!media.getFullName().isEmpty())
//                        ImageLoaderUtil.getInstance().loadImage(imageLoader, StorageManager.getInstance().getFormatFileName(media.getFullName()), holder.senderPhoto, displayImgOptions);
//
//                    holder.senderPhoto.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            callback.showLargePhoto(i);
//                        }
//                    });
//                    if (list.get(i).getSendStatus() == Constants.Chat.CHAT_SEND_STATUS_SENT) {
//
//                        holder.senderMediaProgress.setVisibility(View.VISIBLE);
//                    } else if (list.get(i).getSendStatus() == Constants.Chat.CHAT_SEND_STATUS_LOST ||
//                            list.get(i).getSendStatus() == Constants.Chat.CHAT_SEND_STATUS_UPLOAD_FAILED) {
//                        holder.senderMediaProgress.setVisibility(View.GONE);
//                        holder.senderMediaResetImg.setVisibility(View.VISIBLE);
//                        //holder.senderPhoto.setImageResource(R.drawable.chat_send_photo_fail);
//                        holder.senderMediaResetImg.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                callback.requestResendPhoto(i);
//                            }
//                        });
//                    } else {
//                        holder.senderPhoto.setVisibility(View.VISIBLE);
//                        holder.senderMediaProgress.setVisibility(View.GONE);
//                        holder.senderMediaResetImg.setVisibility(View.GONE);
//
//                        if (isDoctorType()) {
//                            addPhotoNoteListener(holder.senderPhoto, i);
//                        }
//                    }
//                    break;
//                case 3://voice
//                    if (list.get(i).getSendStatus() == Constants.Chat.CHAT_SEND_STATUS_SENT) {
//                        holder.senderMediaProgress.setVisibility(View.VISIBLE);
//                        holder.senderTextResendImg.setVisibility(View.GONE);
//                    } else if (list.get(i).getSendStatus() == Constants.Chat.CHAT_SEND_STATUS_LOST ||
//                            list.get(i).getSendStatus() == Constants.Chat.CHAT_SEND_STATUS_UPLOAD_FAILED) {
//                        holder.senderTextResendImg.setVisibility(View.VISIBLE);
//                        holder.senderMediaProgress.setVisibility(View.GONE);
//                        holder.senderTextResendImg.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                callback.reSendContent(i);
//                            }
//                        });
//                    } else {
//                        holder.senderMediaProgress.setVisibility(View.GONE);
//                        holder.senderTextResendImg.setVisibility(View.GONE);
//                    }
//
//                    holder.sendVoice.setVisibility(View.VISIBLE);
//                    holder.senderTxt.setVisibility(View.VISIBLE);
//                    int duration = list.get(i).getMedia().getDuration();
//                    String time = String.valueOf(duration / 1000 == 0 ? 1 : duration / 1000) + "''";
//                    holder.sendVoiceTime.setText(time);
//                    RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) holder.senderTxt.getLayoutParams();
//                    handleVoiceTextWidth(param, time);
//                    holder.senderTxt.setLayoutParams(param);
////                    showVoice(holder.sendVoice, i);
//                    //showVoice(holder.senderTxt, i);
//                    break;
//                case 4://vedio
//
//                    break;
//            }
//        }
//
//        return view;
//    }
//
//    /**
//     * 发送动画效果
//     *
//     * @param i
//     * @param finalHolder
//     */
//    private void setVoiceClick(int i, final ViewHolder finalHolder) {
//        final AnimationDrawable[] animationDrawable = {null};
//        if (lstPositonCache == i) { //点击同一个
//            if (isCurrentRunning) {
//                callback.stopVoid(-1);
//                curDrable.stop();
//                isCurrentRunning = false;
//                finalHolder.sendVoice.setBackgroundResource(R.drawable.icon_speech3_send);
//                finalHolder.sendVoice.setImageResource(0);
//            } else {
//                finalHolder.sendVoice.setBackgroundResource(0);
//                finalHolder.sendVoice.setImageResource(R.drawable.vido_animation);
//                curDrable = (AnimationDrawable) finalHolder.sendVoice.getDrawable();
//                curDrable.start();
//                isCurrentRunning = true;
//                callback.playVoice(i, new OnVoicePlayEnd() {
//                    @Override
//                    public void OnPlayEnd() {
//                        finalHolder.sendVoice.setBackgroundResource(R.drawable.icon_speech3_send);
//                        finalHolder.sendVoice.setImageResource(0);
//                    }
//                });
//            }
//        } else { //不是同一个
//            if (null != imageViewCache) {
//                if (imageViewCache.getId() == finalHolder.sendVoice.getId()) { //同一侧
//                    imageViewCache.setImageResource(R.drawable.icon_speech3_send);
//                    imageViewCache.setBackgroundResource(0);
//                } else {
//                    imageViewCache.setImageResource(R.drawable.kb_audio_receive_3);
//                    imageViewCache.setBackgroundResource(0);
//                }
//            }
//            callback.stopVoid(-1);
//            callback.playVoice(i, new OnVoicePlayEnd() {
//                @Override
//                public void OnPlayEnd() {
//                    finalHolder.sendVoice.setBackgroundResource(R.drawable.icon_speech3_send);
//                    finalHolder.sendVoice.setImageResource(0);
//                }
//            });
//            curDrable.stop();
//            finalHolder.sendVoice.setBackgroundResource(R.drawable.icon_speech3_send);
//            finalHolder.sendVoice.setImageResource(0);
//            //上面是旧的 下面是新的对象
//            finalHolder.sendVoice.setBackgroundResource(0);
//            finalHolder.sendVoice.setImageResource(R.drawable.vido_animation);
//            curDrable = (AnimationDrawable) finalHolder.sendVoice.getDrawable();
//            curDrable.start();
//            isCurrentRunning = true;
//        }
//        lstPositonCache = i;
//        imageViewCache = finalHolder.sendVoice;
//
//    }
//
//
//    /**
//     * 接收动画效果
//     *
//     * @param i
//     * @param finalHolder
//     */
//    private void setLeftVoiceClick(int i, final ViewHolder finalHolder) {
//        final AnimationDrawable[] animationDrawable = {null};
//        if (lstPositonCache == i) { //点击同一个
//            if (isCurrentRunning) {
//                callback.stopVoid(-1);
//                curDrable.stop();
//                isCurrentRunning = false;
//                finalHolder.receiveVoice.setBackgroundResource(R.drawable.kb_audio_receive_3);
//                finalHolder.receiveVoice.setImageResource(0);
//            } else {
//                finalHolder.receiveVoice.setBackgroundResource(0);
//                finalHolder.receiveVoice.setImageResource(R.drawable.vido_animation_left);
//                curDrable = (AnimationDrawable) finalHolder.receiveVoice.getDrawable();
//                curDrable.start();
//                isCurrentRunning = true;
//                callback.playVoice(i, new OnVoicePlayEnd() {
//                    @Override
//                    public void OnPlayEnd() {
//                        finalHolder.receiveVoice.setBackgroundResource(R.drawable.kb_audio_receive_3);
//                        finalHolder.receiveVoice.setImageResource(0);
//                    }
//                });
//            }
//        } else { //不是同一个
//            setDefaultVoid(finalHolder);
//            callback.stopVoid(-1);
//            callback.playVoice(i, new OnVoicePlayEnd() {
//                @Override
//                public void OnPlayEnd() {
//                    finalHolder.receiveVoice.setBackgroundResource(R.drawable.kb_audio_receive_3);
//                    finalHolder.receiveVoice.setImageResource(0);
//
//
//                }
//            });
//            curDrable.stop();
//            finalHolder.receiveVoice.setBackgroundResource(R.drawable.kb_audio_receive_3);
//            finalHolder.receiveVoice.setImageResource(0);
//            //上面是旧的 下面是新的对象
//            finalHolder.receiveVoice.setBackgroundResource(0);
//            finalHolder.receiveVoice.setImageResource(R.drawable.vido_animation_left);
//            curDrable = (AnimationDrawable) finalHolder.receiveVoice.getDrawable();
//            curDrable.start();
//            isCurrentRunning = true;
//        }
//        lstPositonCache = i;
//        imageViewCache = finalHolder.receiveVoice;
//    }
//
//    /**
//     * 设置默认
//     *
//     * @param finalHolder
//     */
//    private void setDefaultVoid(ViewHolder finalHolder) {
//        if (null != imageViewCache) {
//            if (imageViewCache.getId() == finalHolder.receiveVoice.getId()) { //同一侧
//                imageViewCache.setImageResource(R.drawable.kb_audio_receive_3);
//                imageViewCache.setBackgroundResource(0);
//            } else {
//                imageViewCache.setImageResource(R.drawable.icon_speech3_send);
//                imageViewCache.setBackgroundResource(0);
//            }
//        }
//    }
//
//    private void handleVoiceTextWidth(RelativeLayout.LayoutParams params, String time) {
//        int duration = Integer.valueOf(time.split("''")[0]);
//
//        if ((time.split("''")[0]).contains("'")) {
//            params.width = 350;
//            return;
//        }
//        if (duration < 3) {
//            params.width = 130;
//        } else if (duration == 3) {
//            params.width = 140;
//        } else if (duration == 4) {
//            params.width = 150;
//        } else if (duration == 5) {
//            params.width = 160;
//        } else if (duration == 6) {
//            params.width = 170;
//        } else if (duration == 7) {
//            params.width = 180;
//        } else if (duration == 8) {
//            params.width = 190;
//        } else if (duration == 9) {
//            params.width = 200;
//        } else if (duration > 9 && duration < 20) {
//            params.width = 230;
//        } else if (duration > 19 && duration < 30) {
//            params.width = 250;
//        } else if (duration > 29 && duration < 40) {
//            params.width = 270;
//        } else if (duration > 39 && duration < 50) {
//            params.width = 290;
//        } else if (duration > 49 && duration < 60) {
//            params.width = 310;
//        } else if (duration == 60000) {
//            params.width = 350;
//        }
//    }
//
//    static class ViewHolder {
//        ImageView receiveHeader, senderHeader, receivePhoto, senderPhoto, receiveMediaResetImg,
//                senderMediaResetImg, senderTextResendImg;
//        ImageView sendVoice, receiveVoice;
//        TextView receiveTxt, senderTxt, time;
//        ProgressBar receiveMediaProgress, senderMediaProgress, senderTextProgress;
//        TextView sendVoiceTime, receiveVoiceTime;
//        RelativeLayout layout_receive_chatting_content, laout_sender_chatting_content;
//    }
//
//    void setVisibleGone(ViewHolder holder) {
//        holder.layout_receive_chatting_content.setVisibility(View.GONE);
//        holder.laout_sender_chatting_content.setVisibility(View.GONE);
//        holder.receiveHeader.setVisibility(View.GONE);
//        holder.senderHeader.setVisibility(View.GONE);
//        holder.receivePhoto.setVisibility(View.GONE);
//        holder.senderPhoto.setVisibility(View.GONE);
//        holder.receiveMediaResetImg.setVisibility(View.GONE);
//        holder.senderMediaResetImg.setVisibility(View.GONE);
//        holder.senderTextResendImg.setVisibility(View.GONE);
//        holder.receiveTxt.setVisibility(View.GONE);
//        holder.senderTxt.setVisibility(View.GONE);
//        holder.receiveMediaProgress.setVisibility(View.GONE);
//        holder.senderMediaProgress.setVisibility(View.GONE);
//        holder.time.setVisibility(View.GONE);
//        holder.senderTextProgress.setVisibility(View.GONE);
//        holder.sendVoice.setVisibility(View.GONE);
//        holder.receiveVoice.setVisibility(View.GONE);
//    }
//
//
//    void addTextNoteListener(int status, View view, final int position) {
//        switch (status) {
//            case Constants.Chat.CHAT_SEND_STATUS_OFFLINE:
//            case Constants.Chat.CHAT_SEND_STATUS_RECEIVED:
//                view.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        callback.addTextToNote(view, position);
//                        return true;
//                    }
//                });
//                break;
//        }
//    }
//
//    void addPhotoNoteListener(View view, final int position) {
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                callback.addImageToNote(view, position);
//                return true;
//            }
//        });
//    }
//
//    void addReSendContentListener(int status, View view, final int id) {
//        switch (status) {
//            case Constants.Chat.CHAT_SEND_STATUS_LOST:
//                view.setVisibility(View.VISIBLE);
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callback.reSendContent(id);
//                    }
//                });
//                break;
//            case Constants.Chat.CHAT_SEND_STATUS_OFFLINE:
//                break;
//
//        }
//    }
//
//    void showTextProgress(int status, ProgressBar progressBar) {
//        if (status == Constants.Chat.CHAT_SEND_STATUS_SENT) {
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }
//    }
//
//
//    void showVoice(View view, final int id) {
////        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
////        lp.width = 300;
////        view.setLayoutParams(lp);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                view.setBackground(AppContext.getAppContext().getDrawable(R.anim.voice_play_animation));
//                callback.playVoice(id, new OnVoicePlayEnd() {
//                    @Override
//                    public void OnPlayEnd() {
//                    }
//                });
//            }
//        });
//    }
//
////    class VoicePlayClickListener implements View.OnClickListener {
////        private AnimationDrawable voiceAnimation = null;
////
////        private ChatMessage message;
////
////
////        private boolean isPlaying = false;
////
////        ImageView voiceIconView;
////        private VoicePlayClickListener currentPlayListener = null;
////        ChatMessage currentMessage = null;
////
////        MediaPlayer mediaPlayer = null;
////
////        public VoicePlayClickListener(ChatMessage message, ImageView voiceIcon) {
////            voiceIconView = voiceIcon;
////        }
////
////        @Override
////        public void onClick(View view) {
////
////        }
////
////        public void stopPlayVoice() {
////            voiceAnimation.stop();
////            if (message.getDirection() == Constants.Chat.CHAT_DIRECTION_RECEIVE) {
////                voiceIconView.setImageResource(R.drawable.voice_icon_receive_nor);
////            } else {
////                voiceIconView.setImageResource(R.drawable.voice_icon_sender_nor);
////            }
////            // stop play voice
////            if (mediaPlayer != null) {
////                mediaPlayer.stop();
////                mediaPlayer.release();
////            }
////            isPlaying = false;
////        }
////
////        private void showAnimation() {
////            // play voice, and start animation
////            if (message.getDirection() == Constants.Chat.CHAT_DIRECTION_RECEIVE) {
////                voiceIconView.setImageResource(R.anim.voice_play_animation);
////            } else {
////                voiceIconView.setImageResource(R.anim.voice_play_animation);
////            }
////            voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
////            voiceAnimation.start();
////        }
////
////
////    }
//
//    boolean isDoctorType() {
////        if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
////            return true;
////        }
////        return false;
//
//        //todo
//        return true;
//    }
//
//    public interface OnVoicePlayEnd {
//        void OnPlayEnd();
//    }
//
//}
//
