package com.yuzhi.framework.util;

import java.util.Map;

public interface HttpUtil<T> {
	
	public Object sendHttpRequest(String url, Map<String,String> data);

	public Object sendHttpRequest(String url, Map<String, String> data,
			Class<T> object);
}
