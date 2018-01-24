package com.yikang.heartmark.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 *
 * @author weichyang
 */
public class SharedPreferencesUtils {
    public static final String SP_NAME = "config";
    private static SharedPreferences sp;

    /*保存sp boolean值*/
    public static void saveBoolean(Context ct, String key, boolean value) {
        if (sp == null)
            sp = ct.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ct, String key, boolean defValue) {
        if (sp == null)
            sp = ct.getSharedPreferences(SP_NAME, 0);

        return sp.getBoolean(key, defValue);

    }

    /*保存sp int值*/
    public static int getInt(Context ct, String key, int defValue) {
        if (sp == null)
            sp = ct.getSharedPreferences(SP_NAME, 0);

        return sp.getInt(key, defValue);

    }

    public static void saveInt(Context ct, String key, int value) {
        if (sp == null)
            sp = ct.getSharedPreferences(SP_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }

    /*保存string*/
    public static String getString(Context ct, String key, String defValue) {
        if (sp == null)
            sp = ct.getSharedPreferences(SP_NAME, 0);

        return sp.getString(key, defValue);

    }

    public static void saveString(Context ct, String key, String value) {
        if (sp == null)
            sp = ct.getSharedPreferences(SP_NAME, 0);
        sp.edit().putString(key, value).commit();
    }

    /*保存string*/
    public static String getString(Context ct,String perfKey,String key, String defValue) {
        SharedPreferences sharedPref = ct.getSharedPreferences(perfKey, 0);
        return sharedPref.getString(key, defValue);
    }

}
