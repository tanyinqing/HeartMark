package com.yikang.heartmark.common.business.chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.yikang.heartmark.common.business.other.MediaManager;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.Media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chang on 12/24/2014.
 */
public class MediaService extends IntentService {
    private static Logger logger = LoggerFactory.getLogger(MediaService.class);

    public static final String ACTION_UPLOAD_MEDIA = "media_upload";
    public static final String ACTION_DOWNLOAD_MEDIA = "media_download";

    public static final String LOCAL_BROADCASTING = "localBroadcast";

    public MediaService() {
        super("MediaService()");
        // in case we’re shut down unexpectedly
        setIntentRedelivery( false);
    }

    public MediaService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        logger.debug("intent action = " + intent.getAction());

        Bundle data = intent.getExtras();
        if (data == null) {
            logger.error("onHandleIntent data is null");
            return;
        }

        Media media = (Media) data.getSerializable(Constants.Chat.CHAT_MEDIA);
        if (media == null) {
            logger.error("onHandleIntent media is null");
            return;
        }

        if (action.equals(ACTION_UPLOAD_MEDIA)) {
            doUpload(media);
        }else if (action.equals(ACTION_DOWNLOAD_MEDIA)){
            doDownload(media);
        }

//        Intent resultsIntent = new Intent(LOCAL_BROADCASTING);
//        Bundle result = new Bundle();
//        result.putSerializable("media", media);
//        resultsIntent.putExtras(result);
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        localBroadcastManager.sendBroadcast(resultsIntent);
    }


    void doUpload(Media media){
        if (media == null) {
            logger.error("onHandleIntent media is null");
            return;
        }

        if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE)
            MediaManager.getInstance().uploadImage(media);
        else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO)
            MediaManager.getInstance().uploadAudio(media);
        else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO)
            MediaManager.getInstance().uploadVideo(media);
    }

    void doDownload(Media media){
        if (media == null) {
            logger.error("onHandleIntent media is null");
            return;
        }

        if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE)
            MediaManager.getInstance().downloadImage(media);
        else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_AUDIO)
            MediaManager.getInstance().downloadAudio(media);
        else if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_VIDEO)
            MediaManager.getInstance().downloadVideo(media);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("MediaService is destoried!");

    }
}
