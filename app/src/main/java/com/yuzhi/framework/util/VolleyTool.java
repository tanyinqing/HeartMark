package com.yuzhi.framework.util;
import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Environment;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;


public class VolleyTool {
	private static VolleyTool mInstance = null;
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	private HttpStack stack;
	private static BitmapCache bitmapCache = null;

	private VolleyTool(Context context) {
		File sdDir = Environment.getExternalStorageDirectory();
		File file = new File(sdDir, "图片缓存目录");
		if (!file.exists()) {
			file.mkdirs();
		}
		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {

		}

		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
			}
		}
		Network network = new BasicNetwork(stack);

		mRequestQueue = new RequestQueue(new DiskBasedCache(file), network);
		mRequestQueue.start();

		mImageLoader = new ImageLoader(mRequestQueue, getInstance());
	}

	public static VolleyTool getInstance(Context context) {
		if (mInstance == null || mImageLoader == null || mRequestQueue == null) {
			mInstance = new VolleyTool(context);
		}
		return mInstance;
	}

	public static BitmapCache getInstance() {
		if (bitmapCache == null) {
			bitmapCache = new BitmapCache();
		}
		return bitmapCache;
	}

	public RequestQueue getmRequestQueue() {
		return mRequestQueue;
	}

	public ImageLoader getmImageLoader() {
		return mImageLoader;
	}

	public void release() {
		VolleyTool.mInstance = null;
		VolleyTool.mImageLoader = null;
		VolleyTool.mRequestQueue = null;
	}
}
