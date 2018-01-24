package com.yikang.heartmark.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BimpUtils {

    private static Logger logger = LoggerFactory.getLogger(BimpUtils.class);

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static byte[] resizeImage2Common(String imgPath) {
        Bitmap newBitmap = resize2CommonImage(imgPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        newBitmap.recycle();
        return baos.toByteArray();
    }

    public static byte[] compressImage2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return baos.toByteArray();
    }

    public static Bitmap  resizeImageWithSize(String imgPath,float destWidth,float destHeight) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, newOpts);//此时返回bm为空
        int outWidth = newOpts.outWidth;
        int outHeight = newOpts.outHeight;


        if( outHeight >outWidth ){
            if( destHeight > destWidth ) {
            }else {
                float  tmp = destWidth;
                destWidth = destHeight;
                destWidth=tmp;
            }
        }else if( outWidth>outHeight){//横
            if( destWidth >destHeight ) {
            }else {
                float  tmp = destWidth;
                destWidth = destHeight;
                destWidth=tmp;
            }
        }

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (outWidth > outHeight && outWidth > destWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / destWidth);
        } else if (outWidth < outHeight && outHeight > destHeight) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / destHeight);
        }
        if (be <= 0)
            be = 1;

        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        Matrix m = new Matrix();
        float scale =  destWidth /bitmap.getWidth()  < destHeight  /bitmap.getHeight() ? destWidth /bitmap.getWidth() :
                destHeight  /bitmap.getHeight();

        m.setScale(scale,scale);
        int digree = getRotateDegree(imgPath);
        if (digree != 0) {
            m.postRotate(digree);
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), m, true);
        if(newBitmap !=bitmap )
            bitmap.recycle();

        return newBitmap;
    }

    public static Bitmap  resize2CommonImage(String imgPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, newOpts);//此时返回bm为空
        int outWidth = newOpts.outWidth;
        int outHeight = newOpts.outHeight;
        //判断图片是竖
        float destWidth= 1080;
        float destHeight=1920;

        if( outHeight >outWidth ){
            destWidth=1080;
            destHeight=1920;
        }else if( outWidth>outHeight){//横
            destWidth=1920;
            destHeight=1080;
        }

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (outWidth > outHeight && outWidth > destWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / destWidth);
        } else if (outWidth < outHeight && outHeight > destHeight) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / destHeight);
        }
        if (be <= 0)
            be = 1;

        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        Matrix m = new Matrix();
        float scale =  destWidth /bitmap.getWidth()  < destHeight  /bitmap.getHeight() ? destWidth /bitmap.getWidth() :
                destHeight  /bitmap.getHeight();

        m.setScale(scale,scale);
        int digree = getRotateDegree(imgPath);
        if (digree != 0) {
            m.postRotate(digree);
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), m, true);
        if(newBitmap !=bitmap )
            bitmap.recycle();

        return newBitmap;
    }

    public static int getRotateDegree(String imgPath){
        int digree =0;
        try {
            ExifInterface exif = new ExifInterface(imgPath);
            //读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度

            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
            return digree;
        } catch (IOException e) {
            logger.error(e.toString(),e);
            return digree;
        }
    }


    public static ByteArrayInputStream compressImageStream(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return isBm;
    }


    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap getSubBitmapAndResize(byte[] imageBytes ,Rect subRect, int destWidth,int destHeight  ){

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        int w = subRect.width();
        int h = subRect.height();
        float hh = destHeight;
        float ww = destWidth;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        float be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (h / hh);
        }
        if (be <= 0)
            be = 1;
        Matrix m = new Matrix();
        m.setScale(be,be);

        Bitmap subBitmap  = Bitmap.createBitmap(bitmap, subRect.left,subRect.top,subRect.width(),subRect.height(),m,false);
        bitmap.recycle();
        return compressImageOnce(subBitmap);//压缩好比例大小后再进行质量压缩
    }


    private static Bitmap compressImageOnce(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.recycle();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }



    public static Bitmap resizeImage(String srcPath,int width,int height) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = height;//这里设置高度为800f
        float ww = width;//这里设置宽度为480f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


    public static  Map<String,Double> getImageLocation(String fileName){

        Map<String, Double> locationMap = new HashMap();
        locationMap.put("latitude", Double.parseDouble("1000.0"));
        locationMap.put("longitude", Double.parseDouble("1000.0"));
        //经度 纬度
        try {
            ExifInterface exif = new ExifInterface(fileName);
            if (exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null && exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null) {
                String s_lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String s_lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

                Double s1_la1 = Double.parseDouble(s_lat.split(",")[0].trim().split("/")[0]);
                Double s1_lo1 = Double.parseDouble(s_lat.split(",")[1].trim().split("/")[0]);
                Double s1_lo2 = Double.parseDouble(s_lat.split(",")[1].trim().split("/")[1]);

                Double s2_la1 = Double.parseDouble(s_lon.split(",")[0].trim().split("/")[0]);
                Double s2_lo1 = Double.parseDouble(s_lon.split(",")[1].trim().split("/")[0]);
                Double s2_lo2 = Double.parseDouble(s_lon.split(",")[1].trim().split("/")[1]);
                double latitude = s1_la1 + (s1_lo1 / s1_lo2 / 60);
                double longitude = s2_la1 + (s2_lo1 / s2_lo2 / 60);
                locationMap.put("latitude", latitude);
                locationMap.put("longitude", longitude);
            }
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
        }
        return locationMap;
    }


    public static Bitmap getImage(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }


}
