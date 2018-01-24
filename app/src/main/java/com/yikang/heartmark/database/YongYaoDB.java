package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.YongYao;

public class YongYaoDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public YongYaoDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	/**
	 * 添加数据
	 */
	public void insertYongYao(YongYao yongyao) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", yongyao.uid);
		values.put("time", yongyao.time);
		values.put("timeShow", yongyao.timeShow);
		values.put("name",  yongyao.name);
		values.put("usage", yongyao.usage);
		values.put("sideEffect", yongyao.remind);
		db.insert("yongyaoRemind", null, values);
		db.close();
	}

	/**
	 * 查询数据
	 */
	public ArrayList<YongYao> getCeLingList(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<YongYao> yongyaoArray = new ArrayList<YongYao>();
		Cursor cursor = db.query("yongyaoRemind", new String[] { "id", "uid","time", "timeShow",
				"name", "usage", "sideEffect"}, "uid=?", new String[] {uid}, null, null, null);
		cursorMethod(yongyaoArray, cursor);
		cursor.close();
		db.close();
		return yongyaoArray;
	}
	
	//最后一个添加的数据的id
	public int getLastId(){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("yongyaoRemind", new String[] { "id", "uid","time", "timeShow",
				"name", "usage", "sideEffect"}, null, null, null, null, null);
		cursor.moveToLast();
		int lastId = cursor.getInt(cursor.getColumnIndex("id"));
		cursor.close();
		db.close();
		return lastId;
	
	}
	
	/**
	 * 对cursor查询结果方法抽取
	 */
	private void cursorMethod(ArrayList<YongYao> yongyaoArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			YongYao yongyao = new YongYao();
			yongyao.id = cursor.getInt(cursor.getColumnIndex("id"));
			yongyao.uid = cursor.getString(cursor.getColumnIndex("uid"));
			yongyao.time = cursor.getString(cursor.getColumnIndex("time"));
			yongyao.timeShow = cursor.getString(cursor.getColumnIndex("timeShow"));
			yongyao.name = cursor.getString(cursor.getColumnIndex("name"));
			yongyao.usage = cursor.getString(cursor.getColumnIndex("usage"));
			yongyao.remind = cursor.getString(cursor.getColumnIndex("sideEffect"));
			yongyaoArray.add(yongyao);
		}
	}
	
	/**
	 * 修改数据
	 */
	public int updataBloodData(String timeShow, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("timeShow", timeShow);
		int result = db.update("yongyaoRemind", values, "timeShow=? and uid=?",new String[] 
		{timeShow, uid});
		db.close();
		return result;
	}
	
	/**
	 * 删除数据
	 */
	public void delete(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("yongyaoRemind", "uid=?",new String[] {uid});
		db.close();
	}
	
	/**
	 * 删除数据
	 */
	public void deleteById(String id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("yongyaoRemind", "id=?",new String[] {id});
		db.close();
	}
	
	/**
	 * 删除数据
	 */
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("yongyaoRemind", null,null);
		db.close();
	}
}