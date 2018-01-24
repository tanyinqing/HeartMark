package com.yuzhi.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yuzhi.framework.constant.ApplicationConstant;
import com.yuzhi.framework.constant.ConnectionConstant;


public class HttpClientUtil<T> implements HttpUtil<T> {

	@Override
	public Object sendHttpRequest(String url, Map<String,String> data) {
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Cookie","JSESSIONID=" + ApplicationConstant.getSessionId());
		
		String token = SharedPreferenceUtil.getString(AppContext.context, ConstantUtil.USER_TOKEN);
		if(null == data){
			data = new HashMap<String,String>();	
			data.put("tokenId", token);
		}else{
			data.put("tokenId", token);
		}
		
		List<NameValuePair> nameValuePair = getNameValuePairList(data);

		UrlEncodedFormEntity entity;
		
		String resultJson = "";
		Object resultObject = new Object();
		try {
			entity = new UrlEncodedFormEntity(nameValuePair, ConnectionConstant.SERVER_ENCODING);

			httpPost.setEntity(entity);
			
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			List<Cookie> cookie = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
			configSession(cookie);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				resultJson = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resultJson);
		resultObject = JsonUtil.convertJsonToObject(resultJson);
		return resultObject;
	}
	
	@Override
	public Object sendHttpRequest(String url, Map<String,String> data,Class<T> object) {
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Cookie","JSESSIONID=" + ApplicationConstant.getSessionId());
		
		String token = SharedPreferenceUtil.getString(AppContext.context, ConstantUtil.USER_TOKEN);
		if(null == data){
			data = new HashMap<String,String>();	
			data.put("tokenId", token);
		}else{
			data.put("tokenId", token);
		}
		List<NameValuePair> nameValuePair = getNameValuePairList(data);

		UrlEncodedFormEntity entity;
		
		String resultJson = "";
		Object resultObject = new Object();
		try {
			entity = new UrlEncodedFormEntity(nameValuePair, ConnectionConstant.SERVER_ENCODING);

			httpPost.setEntity(entity);
			
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			List<Cookie> cookie = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
			configSession(cookie);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				resultJson = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resultJson);
		resultObject = new JsonUtil().convertJsonToObject(resultJson,object);
		return resultObject;
	}
	
	@SuppressWarnings("rawtypes")
	private List<NameValuePair> getNameValuePairList(Map data){
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		if(null == data){
			return nameValuePair;
		}
		Iterator iterator = data.entrySet().iterator();
		BasicNameValuePair basicNameValuePair;
		while(iterator.hasNext()){
			Entry entry = (Entry) iterator.next();
			if(null == entry.getValue()){
				basicNameValuePair = new BasicNameValuePair(entry.getKey().toString(), "");
			}else{
				basicNameValuePair = new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString());
			}
			nameValuePair.add(basicNameValuePair);
		}
		return nameValuePair;
	}
	
	private void configSession(List<Cookie> cookie){
		if(ApplicationConstant.getSessionId() == null){
			for(int i=0;i<cookie.size();i++){
				if(cookie.get(i).getName().equals("JSESSIONID")){
					ApplicationConstant.setSessionId(cookie.get(i).getValue());	
				}
			}
		}
	}

}
