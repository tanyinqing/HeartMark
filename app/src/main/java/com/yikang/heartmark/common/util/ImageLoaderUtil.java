package com.yikang.heartmark.common.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.heartmark.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yikang.heartmark.common.business.other.StorageManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chang on 12/25/2014.
 */
public class ImageLoaderUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageLoaderUtil.class);
    //private String url = AppContext.getAppContext().getServiceAddress(Constants.DOWNLOAD_ADDRESS_NAME) + "/objects/download/?object_id=";

    static class ImageLoaderUtilHolder {
        static ImageLoaderUtil instance = new ImageLoaderUtil();
    }

    public static ImageLoaderUtil getInstance() {
        return ImageLoaderUtilHolder.instance;
    }

    public void loadImage(ImageLoader imageLoader, String url, String localFileName, ImageView imageView, ProgressBar progressBar) {
        logger.debug("loadImage=" + url);
        if (url==null || url.isEmpty()) return;
        imageView.setVisibility(View.INVISIBLE);
        DisplayImageOptions displayImgOptions = new DisplayImageOptions.Builder().
                considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
                imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
                showImageOnLoading(null).
                build();
        imageLoader.displayImage(url, imageView, displayImgOptions,
                new ImageLoadingListener(progressBar, localFileName),
                new SimpleImageLoadingProgressListener(progressBar));
        imageView.setVisibility(View.VISIBLE);
    }

    public void loadImage(ImageLoader imageLoader, String url, ImageView imageView, ProgressBar progressBar) {
        logger.debug("loadImage=" + url);
        if (url==null || url.isEmpty()) return;
        imageView.setVisibility(View.INVISIBLE);
        DisplayImageOptions displayImgOptions = new DisplayImageOptions.Builder().
                considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
                imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
                showImageOnLoading(null).
                build();
        imageLoader.displayImage(url, imageView, displayImgOptions,
                new ImageLoadingListener(progressBar, null),
                new SimpleImageLoadingProgressListener(progressBar));
        imageView.setVisibility(View.VISIBLE);
    }

    public void loadImage(ImageLoader imageLoader, String url, ImageView imageView,DisplayImageOptions options,
                          SimpleImageLoadingListener loadingListeneristener, ImageLoadingProgressListener progressListener) {
        logger.debug("loadImage=" + url);
        if (url==null || url.isEmpty()) return;
        imageView.setVisibility(View.INVISIBLE);
        imageLoader.displayImage(url, imageView, options,loadingListeneristener,progressListener);
        imageView.setVisibility(View.VISIBLE);
    }

    public void loadImage(ImageLoader imageLoader, String url, ImageView imageView, DisplayImageOptions options) {
        logger.debug("loadImage=" + url);
        if (url==null || url.isEmpty()) return;
        imageView.setVisibility(View.INVISIBLE);
        imageLoader.displayImage(url, imageView, options);
        imageView.setVisibility(View.VISIBLE);
    }

    public void loadImage(ImageLoader imageLoader, String url, ImageView imageView) {
        logger.debug("loadImage=" + url);
        if (url==null || url.isEmpty()) return;
        imageView.setVisibility(View.INVISIBLE);
        DisplayImageOptions displayImgOptions = new DisplayImageOptions.Builder().
                considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
                imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
                showImageOnLoading(R.drawable.default_head).
                build();
        imageLoader.displayImage(url, imageView, displayImgOptions);
        imageView.setVisibility(View.VISIBLE);
    }

    public class ImageLoadingListener extends SimpleImageLoadingListener {
        private ProgressBar spinner;
        private String localFile;

        ImageLoadingListener(final ProgressBar spinner, String localFile) {
            this.spinner = spinner;
            this.localFile = localFile;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            String message = null;
            spinner.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            spinner.setVisibility(View.GONE);
            if (localFile!=null && !localFile.isEmpty())
                StorageManager.getInstance().SaveImage(localFile, loadedImage);
        }
    }

    public class SimpleImageLoadingProgressListener implements ImageLoadingProgressListener {
        private ProgressBar spinner;
        private boolean firstDo = true;

        SimpleImageLoadingProgressListener(final ProgressBar spinner) {
            this.spinner = spinner;
        }

        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {

        }
    }

}
