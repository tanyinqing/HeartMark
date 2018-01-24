package com.yikang.heartmark.controller.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.alummage.ImageBucket;
import com.yikang.heartmark.common.alummage.ImageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 专辑帮助类
 */
public class AlbumHelper {
    private ContentResolver contentResolver;
    private List<ImageBucket> imageBuckets = new ArrayList<ImageBucket>();
    private HashMap<String, ImageBucket> bucketMap = new HashMap<String, ImageBucket>();

    private LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();

    private List<AlbumChangeListener> changeListeners = new ArrayList<AlbumChangeListener>();

    static final String TAG = AlbumHelper.class.getSimpleName();

    static class AlbumHelperHolder {
        private static AlbumHelper instance = new AlbumHelper();
    }

    public static AlbumHelper getHelper() {
        return AlbumHelperHolder.instance;
    }

    Handler handler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    final String chageType = (String) blockingQueue.poll(10000, TimeUnit.MICROSECONDS);
                    if (chageType == null)
                        continue;

                    blockingQueue.clear();
                    buildImagesBucketList();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (AlbumChangeListener listener : changeListeners) {
                                listener.onAlbumChange(chageType);
                            }
                        }
                    });

                } catch (Exception ex) {
                    Log.e(TAG, ex.toString(), ex);
                }
            }
        }
    };

    private AlbumHelper() {
        Context context = AppContext.getAppContext();
        contentResolver = context.getContentResolver();

        handler = new Handler(context.getMainLooper());

        blockingQueue.offer("init-query");

        Thread th = new Thread(runnable);
        th.setDaemon(true);
        th.start();

        contentResolver.registerContentObserver(Media.EXTERNAL_CONTENT_URI, false, new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                blockingQueue.offer("update-query");
            }
        });
    }


    void buildImagesBucketList() {
        long startTime = System.currentTimeMillis();

        List<ImageBucket> tmpimageBuckets = new ArrayList<ImageBucket>();
        HashMap<String, ImageBucket> tmpbucketMap = new HashMap<String, ImageBucket>();

        // 构造相册索引
        String columns[] = new String[]{Media._ID, Media.BUCKET_ID,
                Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.SIZE, Media.BUCKET_DISPLAY_NAME, Media.WIDTH, Media.HEIGHT};
        // 得到一个游标
        Cursor cur = contentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                Media._ID + " desc");

        if (cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            int photoWidthIndex = cur.getColumnIndexOrThrow(Media.WIDTH);
            int photoHeightIndex = cur.getColumnIndexOrThrow(Media.HEIGHT);
            // 获取图片总数
            int totalNum = cur.getCount();
            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);
                String width = cur.getString(photoWidthIndex);
                String height = cur.getString(photoHeightIndex);

                ImageBucket bucket = tmpbucketMap.get(bucketName);

                if (bucket == null) {
                    bucket = new ImageBucket();
                    tmpimageBuckets.add(bucket);
                    tmpbucketMap.put(bucketName, bucket);

                    bucket.imageList = new ArrayList<ImageItem>();
                    bucket.bucketName = bucketName;
                }

                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.width = width;
                imageItem.height = height;
                imageItem.thumbnailPath = "";

                bucket.imageList.add(imageItem);

            } while (cur.moveToNext());
        }
        cur.close();

        //调整Camera的位置
//        ImageBucket tmpBucket = tmpbucketMap.get("Camera");
//        if (tmpBucket != null) {
//            tmpimageBuckets.remove(tmpBucket);
//            tmpimageBuckets.add(0, tmpBucket);
//            Log.i("AlbumHelper", "tmpBucket.bucketName:" + tmpBucket.bucketName);
//        }
        Collections.sort(tmpimageBuckets, new Comparator<ImageBucket>() {
            @Override
            public int compare(ImageBucket lhs, ImageBucket rhs) {
                return lhs.bucketName.compareToIgnoreCase(rhs.bucketName);
            }
        });

        this.bucketMap = tmpbucketMap;
        this.imageBuckets = tmpimageBuckets;

        long endTime = System.currentTimeMillis();
        Log.i("AlbumHelper", "load Bucket using time:" + (endTime - startTime));
    }

    /**
     * 得到相机图片集
     *
     * @param refresh
     */
    public ImageBucket getDCIMBucket(boolean refresh) {
        ImageBucket cameraBucket = this.bucketMap.get("Camera");
        if (cameraBucket == null && imageBuckets.size() > 0) {
            cameraBucket = imageBuckets.get(0);
        }
        return cameraBucket;
    }

    /**
     * 得到图片集
     */
    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        return new ArrayList(this.imageBuckets);
    }

    public void registerAlbumChangeListener(AlbumChangeListener listener) {
        changeListeners.add(listener);
    }

    public void unregisterAlbumChangeListener(AlbumChangeListener listener) {
        changeListeners.remove(listener);
    }


    public interface AlbumChangeListener {
        void onAlbumChange(String chageType);
    }
}
