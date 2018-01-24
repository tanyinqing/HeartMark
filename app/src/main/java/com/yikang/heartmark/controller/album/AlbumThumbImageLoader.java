package com.yikang.heartmark.controller.album;

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
public class AlbumThumbImageLoader extends ImageLoader {

    static class AlbumImageLoaderHolder {
        static AlbumThumbImageLoader instance = new AlbumThumbImageLoader();
    }

    public static AlbumThumbImageLoader getInstance() {
        return AlbumImageLoaderHolder.instance;
    }

    private AlbumThumbImageLoader() {
        super();

        AppContext appContext = AppContext.getAppContext();
        File cacheDir = StorageUtils.getOwnCacheDirectory(appContext, "AlbumThumbImageLoader/Cache");
        // Create configuration for ImageLoader (all options are optional, use only those you really want to customize)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(appContext)
                //.memoryCacheExtraOptions(150, 150) // max width, max height
                .threadPoolSize(2)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                        //.denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation
                .memoryCacheExtraOptions(360, 360)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheExtraOptions(360, 360, null)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(appContext, 10 * 1000, 60 * 1000)) // connectTimeout (5 s), readTimeout (20 s)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();
        // Initialize ImageLoader with created configuration. Do it once on Application start.
        super.init(config);
    }
}
