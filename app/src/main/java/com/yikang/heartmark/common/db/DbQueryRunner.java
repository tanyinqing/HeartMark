package com.yikang.heartmark.common.db;

/**
 * Created by chuanyhu on 2014/8/19.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * sqlite的dao操作帮助类
 */
public class DbQueryRunner<T> {

    private   static Logger logger = LoggerFactory.getLogger(DbQueryRunner.class);


    private SQLiteDatabase database;
    private RowHandler mapHandler = new MapRowHandler();

    static class DbHelperHolder
    {
        static DbQueryRunner dbHelper = new DbQueryRunner();
    }

    public static DbQueryRunner getInstance()
    {
        return DbHelperHolder.dbHelper;
    }
    private  DbQueryRunner() {
        checkDatabase();
    }
    private void checkDatabase()
    {
        File dbfile = AppContext.getAppContext().getDatabasePath("askdrdb.sqlite");
        if(! dbfile.exists()){
            dbfile.getParentFile().mkdirs();
            copyDbFile(R.raw.askdrdb,dbfile.getAbsolutePath());
        }

        database = new DbOpenHelper(AppContext.getAppContext(),dbfile.getAbsolutePath()).getWritableDatabase();
    }
    private void copyDbFile(int rawid, String file){
        try {
            InputStream is = AppContext.getAppContext().getResources().openRawResource(rawid);

            FileOutputStream fos = new FileOutputStream(file, false);
            byte[] buf = new byte[4096];
            for (int len =-1; true; ) {
                len = is.read(buf);
                if (len == -1)
                    break;
                fos.write(buf, 0, len);
            }
            fos.close();
            is.close();
        }catch (IOException ex){
            logger.error(ex.toString(),ex);
        }
    }

    public List query(String sql)
    {
        return this.query(sql,mapHandler,null);
    }

    public List query(String sql,String[] param)
    {
        return this.query(sql,mapHandler,param);
    }
    public List query(String sql,RowHandler<T> handler)
    {
        return this.query(sql,handler,null);
    }
    public List query(String sql,RowHandler<T> handler ,String[] param){
        Cursor cursor=null;
        try{
            List<T> list = new ArrayList();
            cursor = database.rawQuery(sql,param);
            while (cursor.moveToNext()) {
                list.add(handler.handle(cursor));
            }
            return list;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            throw new RuntimeException(e);
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
    }

    public int update(String sql) {
        try {
            database.execSQL(sql);
            return 1;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            throw new RuntimeException(e);
        }
    }

    public int update(String sql ,Object[] param) {
        try {
            database.execSQL(sql,param);
            return 1;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            throw new RuntimeException(e);
        }
    }

    public void beginTransaction() {
        database.beginTransaction();
    }
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }
    public void endTransaction() {
        database.endTransaction();
    }

    /*更新补丁*/
    public int batchUpdate(String[] arrsql ,Object[][] arrparam) {
        try {
            database.beginTransaction();
            for(int i=0; i< arrsql.length;i++) {
                database.execSQL(arrsql[i], arrparam[i]);
            }
            database.setTransactionSuccessful();
            return arrsql.length;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            throw new RuntimeException(e);
        }finally {
            database.endTransaction();
        }
    }
    //行处理接口
    public interface RowHandler<T> {
        T handle(Cursor c);
    }
    //将每行处理成Map<String,String>结构
    static public class MapRowHandler implements RowHandler<Map<String,String>> {

        @Override
        public Map<String,String> handle(Cursor c) {
            Map<String,String> map = new CaseInsensitiveMap();

            String[] columns = c.getColumnNames();

            for (String col : columns) {
                map.put(col, c.getString(c.getColumnIndex(col)));
            }

            return map;
        }

    }
}