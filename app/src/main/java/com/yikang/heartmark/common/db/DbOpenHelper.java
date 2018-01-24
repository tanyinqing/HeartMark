package com.yikang.heartmark.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chuanyhu on 2014/8/19.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    /*
     * 1context 确定数据库的存储位置
     * 2.数据库名字
     * 3用来创建cursor（结果集）的工厂默认串null就可以
     * 4.版本，后期用来更新数据库
     */
    public DbOpenHelper(Context context,String dbFile){
        super(context,dbFile,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
