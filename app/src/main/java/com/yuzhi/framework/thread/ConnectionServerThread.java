package com.yuzhi.framework.thread;

import java.util.Map;

import android.os.Message;

import com.yuzhi.framework.constant.ConnectionConstant;
import com.yuzhi.framework.handler.ConnectionServerHandler;
import com.yuzhi.framework.util.HttpClientUtil;
import com.yuzhi.framework.util.HttpUtil;

public class ConnectionServerThread<T> extends Thread {
	
	protected HttpUtil httpUtil;
	
	private String url;
	
	private ConnectionServerHandler handler;
	
	private Map<String,String> params;
	
	private Class<T> object;
	
	 /****************** 线程构造器******************************/
	public ConnectionServerThread(String url,Map<String,String> params){
		this.url = url;
		this.params = params; 
		this.handler = new ConnectionServerHandler(); 
	}
	public ConnectionServerThread(String url,Map<String,String> params,String sucessCallBackHandler,Object obj){
		this.url = url;
		this.params = params; 
		this.handler = new ConnectionServerHandler(sucessCallBackHandler,obj); 
	}

	public ConnectionServerThread(String url,Map<String,String> params,Class<T> object){
		this.url = url;
		this.params = params; 
		this.handler = new ConnectionServerHandler(); 
		this.object = object;
	}

	public ConnectionServerThread(String url,Map<String,String> params,String sucessCallBackHandler,Object obj,Class<T> object){
		this.url = url;
		this.params = params; 
		this.handler = new ConnectionServerHandler(sucessCallBackHandler,obj); 
		this.object = object;
	}
	
    /*************************************************************/
	

	private void connectServer(){
		if(ConnectionConstant.CONNECT_MOTHED.equals(ConnectionConstant.HTTP_CLIENT)){
			httpUtil = new HttpClientUtil();
		}
		Object data;
		if(object!=null){
			data = httpUtil.sendHttpRequest(url, params,object);
		}else{
			data = httpUtil.sendHttpRequest(url, params);
		}
		Message msg = new Message();
		msg.obj = data;
		handler.sendMessage(msg);
	}
	
	@Override
	public void run() {
		connectServer();
	}
}
