package com.yuzhi.framework.constant;

import com.yikang.heartmark.util.ConnectUtil;

public class ConnectionConstant {

//	public static final String SERVER_URL = "http://10.10.14.116:9898/";   //公司内网
//	public static final String SERVER_URL = "http://124.205.59.76/";   //外网
	public static final String SERVER_URL = ConnectUtil.HOST_URL;
	
	public static final String SERVER_NAME = "BnpHome";

	public static final String SERVICE_SUFFIX = "J!";

	public static final String URL_SUFFIX = ".htm"; //尾标

	public static final String CONNECT_MOTHED = "HttpClient";  //connect_mothed

	public static final String HTTP_CLIENT = "HttpClient";

	public static final String HTTP_URL_CONNECTION = "HttpURLConnection";

	public static final String SERVER_ENCODING = "utf-8";

}
