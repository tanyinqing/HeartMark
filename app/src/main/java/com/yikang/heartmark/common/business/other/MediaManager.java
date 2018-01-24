package com.yikang.heartmark.common.business.other;

import android.graphics.Bitmap;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.business.chat.ChatManager;
import com.yikang.heartmark.common.business.chat.MessageBuilder;
import com.yikang.heartmark.common.util.BimpUtils;
import com.yikang.heartmark.common.util.HttpRequestUtil;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ChatError;
import com.yikang.heartmark.model.chat.Media;
import com.yikang.heartmark.model.chat.ResultData;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by guolchen on 2014/12/17.
 */
public class MediaManager {

    private static Logger logger = LoggerFactory.getLogger(MediaManager.class);
    private MediaListener mediaListener;

    public interface MediaListener {
        void onUpload(Media media);

        void onDownload(Media media);

        void onMediaError(long sessionId, String messageId, Media media, ChatError chatError);
    }

    static class MediaManagerHolder {
        static MediaManager mediaManager = new MediaManager();
    }

    public static MediaManager getInstance() {
        return MediaManagerHolder.mediaManager;
    }

    private MediaManager() {
        this.mediaListener = ChatManager.getInstance();
    }

    public void uploadImage(Media media) {
        Bitmap bitmap = BimpUtils.resize2CommonImage(media.getFullName());
        try {
            media.setContent(BimpUtils.compressImage2Bytes(bitmap));
            ResultData map = uploadFile(media);

            if (map.getCode() == Constants.SERVER_SUCCESS) {
                media.setObjectId(map.getJsonObject().getString("object_id"));
                media.setThumbnailImageId(map.getJsonObject().getString("thumbnail_img_id"));
                media.setLargeImageId(map.getJsonObject().getString("large_img_id"));
                String imageStyle = map.getJsonObject().getString("image_style");
                if (imageStyle.equals(Constants.Http.HTTP_IMAGE_STYLE_LARGE)) {
                    media.setImageStyle(Constants.Chat.CHAT_IMAGE_STYLE_LARGE);
                } else if (imageStyle.equals(Constants.Http.HTTP_IMAGE_STYLE_RAW)) {
                    media.setImageStyle(Constants.Chat.CHAT_IMAGE_STYLE_RAW);
                }
                //media.setMimeType(map.getJsonObject().getString("mime_type"));
                mediaListener.onUpload(media);
            } else {
                mediaListener.onMediaError(media.getSessionId(), media.getMessageId(), media, MessageBuilder.buildError(Constants.Error.MEDIA_UPLOAD_ERROR, map.getCode() + map.getReason()));
            }

        } catch (Exception e) {
            logger.error(e.toString(), e);
            mediaListener.onMediaError(media.getSessionId(), media.getMessageId(), media, MessageBuilder.buildError(Constants.Error.MEDIA_UPLOAD_ERROR, e));
        }
    }

    public void uploadAudio(Media media) {
        try {
            //byte[] content = readFileSdcardFile("/storage/sdcard0/record/recorder/录音03.amr");
            byte[] content = readFile(media.getFullName());
            if (null != content) {
                media.setContent(content);
                ResultData map = uploadFile(media);

                if (map.getCode() == Constants.SERVER_SUCCESS) {
                    media.setObjectId(map.getJsonObject().getString("object_id"));
                    mediaListener.onUpload(media);
                } else {
                    mediaListener.onMediaError(media.getSessionId(), media.getMessageId(), media, MessageBuilder.buildError(Constants.Error.MEDIA_UPLOAD_ERROR, map.getCode() + map.getReason()));
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void uploadVideo(Media media) {
    }

    public void downloadImage(Media media) {

    }

    public void downloadAudio(Media media) {
        try {
            ResultData map = new ResultData();
            downloadFile(media.getObjectId(), "", map);
            if (map.getCode() == Constants.SERVER_SUCCESS) {
                media.setContent(map.getData());
                if (saveFile(media)) {
                    mediaListener.onDownload(media);
                }
            } else {
                mediaListener.onMediaError(media.getSessionId(), media.getMessageId(), media, MessageBuilder.buildError(Constants.Error.MEDIA_DOWNLOAD_ERROR, map.getCode() + map.getReason()));
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            mediaListener.onMediaError(media.getSessionId(), media.getMessageId(), media, MessageBuilder.buildError(Constants.Error.MEDIA_DOWNLOAD_ERROR, e.getMessage()));
        }
    }

    public void downloadVideo(Media media) {

    }

    public ResultData uploadFile(Media media) throws Exception {
        RequestParams params = new RequestParams();
        params.add("uploader", media.getUploader());
        params.add("user_id", String.valueOf(media.getUserId()));
        //params.add("file_name", media.getFileName());

        params.add("latitude", String.valueOf(media.getLatitude()));
        params.add("longitude", String.valueOf(media.getLongitude()));
        params.add("duration", String.valueOf(media.getDuration()));

        params.add("mime_type", media.getMimeType());

        params.add("object_time", String.valueOf(media.getObjectTime()));
        params.put("location", media.getJsonLocation());

        if (media.getMessageType() == Constants.Chat.CHAT_MESSAGE_TYPE_IMAGE) {
            if (media.getImageStyle() == Constants.Chat.CHAT_IMAGE_STYLE_LARGE)
                params.add("image_style", Constants.Http.HTTP_IMAGE_STYLE_LARGE); // 1 -large.2-raw
            else
                params.add("image_style", Constants.Http.HTTP_IMAGE_STYLE_RAW); // 1 -large.2-raw
        } else {
            params.add("image_style", "");
        }

        //处理压缩数据上传
        InputStream inputStream = new ByteArrayInputStream(media.getContent());
        params.put("files", inputStream, media.getFileName(), media.getMimeType());
        ResultData map = new ResultData();
        //todo  url
//        final String url = AppContext.getAppContext().getServiceAddress(Constants.UPLOAD_ADDRESS_NAME) + "/v1/objects/upload_one";
        final String url = "";

        try {
            final SyncHttpClient client = new SyncHttpClient();
            client.setTimeout(Constants.Chat.CHAT_UPLOAD_TIMEOUT);
            HttpRequestUtil.addRequestHeader(client);
            client.post(url, params, HttpRequestUtil.getSyncResponseHandler(map));
            return map;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            map.setLocalError(e.getMessage());
            return null;
        }
    }

    public void downloadFile(String objectId, String userId, final ResultData map) {
        String url = AppContext.getAppContext().getServiceAddress(Constants.DOWNLOAD_ADDRESS_NAME) + "/objects/download?object_id=" + objectId + "&user_id=" + userId;

        final SyncHttpClient client = new SyncHttpClient();
        client.setTimeout(Constants.Chat.CHAT_UPLOAD_TIMEOUT);
        HttpRequestUtil.addRequestHeader(client);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    map.setData(bytes);
                    map.setCodeReason(Constants.SERVER_SUCCESS, "success");
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                    map.setLocalError(e.getMessage());
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                map.setCodeReason(i, "error");
                map.setLocalError(throwable.getMessage());
            }
        });
    }

    private byte[] readFile(String fileName) throws IOException {
        byte[] buffer = null;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return buffer;
    }

    //TODO Chang need to save download file to local storage
    private boolean saveFile(Media media) {
        //TODO Chang hardcoded file name
        String newFile = StorageManager.getInstance().getNewAudioFileName(media.getObjectId(), "amr");
        media.setFullName(StorageManager.getInstance().getFullFileName(newFile, media.getSessionId()));

        return StorageManager.getInstance().SaveAudio(media.getFullName(), media.getContent());
    }

    private String getFileName(String filePath) {
        int start = filePath.lastIndexOf("/");
        int end = filePath.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return filePath.substring(start + 1, end);
        } else {
            return null;
        }
    }

}

