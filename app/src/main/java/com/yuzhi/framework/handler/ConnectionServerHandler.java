package com.yuzhi.framework.handler;

import java.lang.reflect.Method;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yuzhi.framework.constant.MessageConstant;

public class ConnectionServerHandler extends Handler {
	
	private Method sucessCallBackHandler;
	
	private Object callBackObject;
	
	public ConnectionServerHandler(){
	}
	
	public ConnectionServerHandler(String sucessCallBackHandler,Object obj){
		try {
			this.sucessCallBackHandler = obj.getClass().getMethod(sucessCallBackHandler, Object.class);
			callBackObject = obj;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void handleMessage(Message msg) {
		if(null != sucessCallBackHandler){
			try {
				sucessCallBackHandler.invoke(callBackObject, msg.obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Log.e("connection", MessageConstant.MSG_ERROR_CALLBACK_METHOD);
		}
	}
}
