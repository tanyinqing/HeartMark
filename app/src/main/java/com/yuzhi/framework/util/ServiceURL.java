package com.yuzhi.framework.util;


import com.yuzhi.framework.constant.ConnectionConstant;

public class ServiceURL {


	protected String moduleId;
	
	protected String method;  //方法名字

	
	public ServiceURL(String moduleId, String method) {
		this.moduleId = moduleId;
		this.method = method;
	}

	protected String populateUrl() {
		String url = ConnectionConstant.SERVER_URL
				+ moduleId
				+ ConnectionConstant.SERVICE_SUFFIX + method
				+ ConnectionConstant.URL_SUFFIX;
		return url;
	}

	public String getURL() {
		return populateUrl();
	}

}
