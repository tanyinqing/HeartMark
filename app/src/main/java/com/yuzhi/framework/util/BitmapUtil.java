package com.yuzhi.framework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class BitmapUtil {
	public static Bitmap decodeFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(2000);
			connection.connect();
			bitmap = BitmapFactory.decodeStream(connection.getInputStream());
		} catch (Exception localException) {   
		}

		return bitmap;
	}

	private static final String TAG = "BtimapUtil";

	public static Bitmap getBitmap(String url, Context context) {
		Log.e(TAG, "------url=" + url);
		String imageName = url
				.substring(url.lastIndexOf("/") + 1, url.length());
		File file = new File(getPath(context), imageName);
		if (file.exists()) {
			Log.e(TAG, "getBitmap from Local");
			return BitmapFactory.decodeFile(file.getPath());
		}
		return getNetBitmap(url, file, context);
	}

	public static List<Bitmap> getBitmap(List<String> list, Context context) {
		List<Bitmap> result = new ArrayList<Bitmap>();
		for (String strUrl : list) {
			Bitmap bitmap = getBitmap(strUrl, context);
			if (bitmap != null) {
				result.add(bitmap);
			}
		}
		return result;
	}

	private static String getPath(Context context) {
		String path = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		String packageName = context.getPackageName() + "/cach/images/";
		if (hasSDCard) {
			path = "/sdcard/apps_images/" + packageName;
		} else {
			path = "/data/data/" + packageName;
		}
		File file = new File(path);
		boolean isExist = file.exists();
		if (!isExist) {

			file.mkdirs();

		}
		return file.getPath();
	}

	private static Bitmap getNetBitmap(String strUrl, File file, Context context) {
		Log.e(TAG, "getBitmap from net");
		Bitmap bitmap = null;
		if (NetUtil.isConnnected(context)) {
			try {
				URL url = new URL(strUrl);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream in = con.getInputStream();
				bitmap = BitmapFactory.decodeStream(in);
				FileOutputStream out = new FileOutputStream(file.getPath());
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				in.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

			}
		}
		return bitmap;
	}

}