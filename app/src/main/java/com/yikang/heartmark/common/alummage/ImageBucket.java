package com.yikang.heartmark.common.alummage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket implements Serializable {
    public int count;
    public String bucketName;
    public List<ImageItem> imageList= new ArrayList();

}
