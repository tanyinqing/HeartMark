package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.CeLingData;
import com.yikang.heartmark.util.ConstantUtil;

public class CeLingDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public CeLingDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	/**
	 * 添加数据
	 */
	public void insertCeLingData(CeLingData celingData) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", celingData.uid);
		values.put("sync", celingData.sync);
		values.put("date", celingData.date);
		values.put("dateMonth", celingData.dateMonth);
		values.put("day", celingData.day);
		values.put("time", celingData.time);
		values.put("type", celingData.type);
		values.put("result", celingData.result);
		values.put("state", celingData.state);
		values.put("diag", celingData.diag);
		db.insert("celingData", null, values);
		db.close();
	}

	//查询  日期
	public ArrayList<CeLingData> getListByDate(String date, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<CeLingData> celingArray = new ArrayList<CeLingData>();
		Cursor cursor = db.query("celingData", new String[] { "id", "uid","sync", "date",
				"dateMonth", "day", "time", "type","result", "state", "diag" }, "date=? and uid=?", new String[] {date, uid}, null, null, "date desc,time desc");
		
		cursorMethod(celingArray, cursor);
		cursor.close();
		db.close();
		return celingArray;
	}
	
	/**
	 * 查询数据, uid
	 */
	public ArrayList<CeLingData> getCeLingListByUid(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<CeLingData> celingArray = new ArrayList<CeLingData>();
		Cursor cursor = db.query("celingData", new String[] { "id", "uid","sync", "date",
				"dateMonth", "day", "time", "type","result", "state", "diag" }, "uid=?", new String[] {uid}, null, null, "date desc,time desc");
		/*Cursor cursor = db.query(
		 * "celingData",   数据的表名
		 *  new String[] { "id", "uid","sync", "date",
				"dateMonth", "day", "time", "type","result", "state", "diag" },   要查询出来的列明
				 "uid=?",   查询的条件子句
				 new String[] {uid},   为子句中的占位符提供参数值
				 null,   用于控制分组
				 null,    用于对分组进行过滤
				 "date desc,time desc"  用于对记录进行排序
				 );*/
		cursorMethod(celingArray, cursor);
		cursor.close();
		db.close();
		return celingArray;
	}
	
	/**
	 * 获取是否有未同步的数据
	 */
	public boolean haveNoSync(String uid) {
		boolean isHave = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("celingData", new String[] { "id", "uid","sync", "date",
				"dateMonth", "day", "time", "type","result", "state", "diag" }, "sync=? and uid=?", new String[] { String.valueOf(ConstantUtil.SYNC_NO), uid}, null, null,null);
		if(cursor.moveToFirst() == false){
			isHave = false;
		}else{
			isHave = true;
		}
		cursor.close();
		db.close();
		return isHave;
	}
	
	//获取没有上传的数据
	public ArrayList<CeLingData> getCeLingListByNoSync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<CeLingData> celingArray = new ArrayList<CeLingData>();
		Cursor cursor = db.query("celingData", new String[] { "id", "uid","sync", "date",
				"dateMonth", "day", "time", "type","result", "state", "diag" }, "sync=? and uid=?", new String[] { String.valueOf(ConstantUtil.SYNC_NO), uid}, null, null, null);
		cursorMethod(celingArray, cursor);
		cursor.close();
		db.close();
		return celingArray;
	}
	
	/**
	 * 对cursor查询结果方法抽取
	 */
	private void cursorMethod(ArrayList<CeLingData> celingArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			CeLingData celingData = new CeLingData();
			celingData.id = cursor.getInt(cursor.getColumnIndex("id"));
			celingData.uid = cursor.getString(cursor.getColumnIndex("uid"));
			celingData.sync = cursor.getInt(cursor.getColumnIndex("sync"));
			celingData.date = cursor.getString(cursor.getColumnIndex("date"));
			celingData.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
			celingData.day = cursor.getString(cursor.getColumnIndex("day"));
			celingData.time = cursor.getString(cursor.getColumnIndex("time"));
			celingData.type = cursor.getString(cursor.getColumnIndex("type"));
			celingData.result = cursor.getString(cursor.getColumnIndex("result"));
			celingData.state = cursor.getString(cursor.getColumnIndex("state"));
			celingData.diag = cursor.getString(cursor.getColumnIndex("diag"));
			celingArray.add(celingData);
		}
	}

	/**
	 * 修改数据
	 */
	public int updataSyncNoToYes(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sync", ConstantUtil.SYNC_YES);
		int result = db.update("celingData", values, "sync=? and uid=?",new String[] { String.valueOf( ConstantUtil.SYNC_NO), uid});
		/*int result = db.update("celingData", 数据表名
		 * values, 代表想跟新的数据
		 * "sync=? and uid=?",  满足条件的子类
		 * new String[] { String.valueOf( ConstantUtil.SYNC_NO), uid});  为子句传入参数
		 * */
		db.close();
		return result;
	}
	
    //删除已经同步的数据
	public void deleteBySync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("celingData", "sync=? and uid=?",new String[] { String.valueOf(ConstantUtil.SYNC_YES), uid});
		db.close();
	}
	
	/**
	 * 删除该表数据
	 */
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("celingData", null,null);
		db.close();
	}
}