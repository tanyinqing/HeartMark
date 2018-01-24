package com.yikang.heartmark.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ZiXun implements Serializable {
	public int id;
	public String newId;  //new 获取id
	public String title;
	public String image;//图片的地址连接
	public String time;
	public String videoUrl;  //视频链接
	public String content;  //点击详细信息xml
	public String contentRead;
	public String good;  //点赞次数
	public String house; //收藏  0为未收藏, 1为收藏
	public String type;  //用于详细页判断, 咨询跟说明书
	
	public static final String TYPE_ZIXUN ="zixun";
	public static final String TYPE_EXPLAIN ="explain";
}
