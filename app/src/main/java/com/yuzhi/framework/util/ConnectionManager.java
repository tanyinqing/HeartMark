package com.yuzhi.framework.util;

/**
 * Copyright (c) 上海域至信息科技有限公司. All rights are reserved. LICENSE INFORMATION
 * 
 * 连接server端 使用工具类
 * 
 * @author ZhangChuanfu
 * @version Ver 1.0 2014-07-12 创建
 * 
 */

import java.util.Map;

import com.yuzhi.framework.thread.ConnectionServerThread;


public class ConnectionManager {

	/**
	 * ConnectionManager类的实例
	 */
	private static ConnectionManager instance;

	/**
	 * 读取服务的数据使用子线程
	 */
	private ConnectionServerThread connectionServerThread;
	/**
	 * 获得ConnectionManager的实例
	 * 
	 * @return ConnectionManager
	 */
	public static ConnectionManager getInstance() {
		instance = new ConnectionManager();
		return instance;
	}
	/**
	 * 向服务器端发送请求,无回调方法
	 * @param <T>
	 * @param moduleId :action名称
	 * @param method	:方法名称
	 * @param param		:参数信息，参数应为Map类型
	 * @return
	 */
	public <T> void send(String moduleId, String method, Map<String,String> params) {
		//整理请求server端路径
		/****************** 获取连接的URL******************************/       
		ServiceURL serviceURL = new ServiceURL(moduleId,method);
		String url = serviceURL.getURL();
		 /*************************************************************/
		
		connectionServerThread = new ConnectionServerThread(url,params);
		connectionServerThread.start();
	}
	
	//moduleId 模块名 方法是属于那个模块的   method  方法名  params 参数名   sucessCallBackHandler 回调方法名
	public  void send(String moduleId, String method, Map<String,String> params,String sucessCallBackHandler,Object obj){
		//整理请求server端路径
		ServiceURL serviceURL = new ServiceURL(moduleId,method);
		String url = serviceURL.getURL();
	    connectionServerThread = new ConnectionServerThread(url,params,sucessCallBackHandler,obj);
		connectionServerThread.start();
		
	}
	/**
	 * 向服务器端发送请求
	 * @param <T>
	 * @param moduleId	: action 名称
	 * @param method	: 方法名称
	 * @param params	: 参数
	 * @param sucessCallBackHandler	: 回调方法名称
	 * @param obj	: 回调方法所在对象
	 */
	public <T> void send(String moduleId, String method, Map<String,String> params,String sucessCallBackHandler,Object obj,Class<T> objClass){
		//整理请求server端路径
		ServiceURL serviceURL = new ServiceURL(moduleId,method);
		String url = serviceURL.getURL();
		
		connectionServerThread = new ConnectionServerThread(url,params,sucessCallBackHandler,obj,objClass);
		connectionServerThread.start();
	}

}
