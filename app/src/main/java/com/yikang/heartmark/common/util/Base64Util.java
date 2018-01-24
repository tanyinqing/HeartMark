package com.yikang.heartmark.common.util;

import android.util.Base64;


public class Base64Util {


    public static String getBASE64(String s) {

        if (s == null) return null;
        return Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
    }

    public static String encode(byte[] bs) {

        if (bs == null) return null;

        byte[] data = Base64.encode(bs, Base64.DEFAULT);

        return new String(data);
    }

    public static byte[] decode(String s) {

        if (s == null) return null;

        try {
            //b = new BASE64Decoder().decodeBuffer(s);
            byte[] data = Base64.decode(s, Base64.DEFAULT);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
