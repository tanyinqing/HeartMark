package com.yikang.heartmark.common.util;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yikang.heartmark.application.AppContext;

import java.io.File;

/**
 * Created by chuanyhu on 2015/1/24.
 */
public class LocalImageLoader extends ImageLoader {

    static class AlbumImageLoaderHolder {
        static LocalImageLoader instance = new LocalImageLoader();
    }

    public static LocalImageLoader getInstance(){
        return AlbumImageLoaderHolder.instance;
    }

    private LocalImageLoader(){
        super();

        Application appContext = AppContext.getAppContext();
        File cacheDir = StorageUtils.getOwnCacheDirectory(appContext, "AlbumImageLoader/Cache");
        // Create configuration for ImageLoader (all options are optional, use only those you really want to customize)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(appContext)
                //.memoryCacheExtraOptions(150, 150) // max width, max height
                .threadPoolSize(2)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(appContext,10 * 1000, 60 * 1000)) // connectTimeout (5 s), readTimeout (20 s)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();
        // Initialize ImageLoader with created configuration. Do it once on Application start.
        super.init(config);
    }
}
