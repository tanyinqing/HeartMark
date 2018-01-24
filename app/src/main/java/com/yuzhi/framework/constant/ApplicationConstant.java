package com.yuzhi.framework.constant;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConstant{
	
	private static Map<String,Object> session = new HashMap<String,Object>();
	
	private static String APPLICATION_SESSION_USER = "sxtUser";
	private static String APPLICATION_SESSION_COMPANY = "sxtCompany";
	
	public static Map<String, Object> getSession() {
		return session;
	}
	
	public static void setSessionAttribute(String key, Object value) {
		if(value == null){
			getSession().remove(key);
		}else{
			getSession().put(key, value);
		}
	}
	
	public static Object getSessionAttribute(String key) {
		if (getSession() != null && getSession().containsKey(key)) {
			return getSession().get(key);
		} else {
			return null;
		}
	}

	public static void removeCurrentSessionAttribute(String key) {
		getSession().remove(key);
	}

	public static void clearSession() {
		session = new HashMap<String,Object>();
	}

	public static String getSessionId() {
		if(session.containsKey("sessionId")){
			return session.get("sessionId").toString();
		}else{
			return null;
		}
	}

	public static void setSessionId(String sessionId) {
		session.put("sessionId", sessionId);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> getCompany() {
		Map<String,String> result = ((Map<String,String>)getSessionAttribute(APPLICATION_SESSION_COMPANY));
		if(result != null){
			return result;
		}else{
			return null;
		}
	}
	
	public static void setCompany(Map<String,String> sxtCompany) {
		setSessionAttribute(APPLICATION_SESSION_COMPANY,sxtCompany);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> getUser() {
		Map<String,String> result = ((Map<String,String>)getSessionAttribute(APPLICATION_SESSION_USER));
		if(result != null){
			return result;
		}else{
			return null;
		}
	}
	
	public static void setUser(Map<String,String> sxtUser) {
		setSessionAttribute(APPLICATION_SESSION_USER,sxtUser);
	}
	
}
