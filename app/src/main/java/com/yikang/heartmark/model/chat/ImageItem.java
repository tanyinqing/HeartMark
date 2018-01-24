package com.yikang.heartmark.model.chat;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
    public String width;
    public String height;
    public int  size;
	public boolean isSelected = false;
}
