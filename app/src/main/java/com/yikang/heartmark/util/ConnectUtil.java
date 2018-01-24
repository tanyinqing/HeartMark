package com.yikang.heartmark.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectUtil {

	public static String versionName = "Beta";
	public static String versionNameString = "当前最新版本" + versionName;
    // public static String HOST_URL ="http://10.10.14.116:9898/BnpHome/";//亚斌服务器
	// public static String HOST_URL = "http://61.50.132.151/BnpHome/";//外网
	 public static String HOST_URL = "http://61.50.132.150/BnpHome/";//外网(新)
	// public static String HOST_URL = "http://192.168.21.18:8080/BnpHome/";//西二旗服务器
	// public static String HOST_URL = "http://192.168.22.224:8080/BnpHome/";//西二旗服务器
	// public static String HOST_URL = "http://10.10.14.107:8088/BnpHome/";//凤翔
    // public static String HOST_URL = "http://10.10.14.117:8080/BnpHome/";//凤翔
    // public static String HOST_URL = "http://10.10.14.116:9090/BnpHome/";// 公司内部服务器

	// 是否登录
	public static boolean isLogin(Context context) {
		//return SharedPreferenceUtil.getBoolean(context, ConstantUtil.LOGIN);
		return true;  //测试的时候，每个时候都是登录的
	}

	// 判断当前设备网络是否已经连接 
	public static boolean isConnect(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	
	private static long lastClickTime;
	// 防止连续点击
	public static boolean isFastDoubleClick(int ms) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < ms) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
