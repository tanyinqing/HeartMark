package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.HelpWater;
import com.yikang.heartmark.util.ConstantUtil;

public class HelpWaterDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public HelpWaterDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	// 添加数据
	public void insert(HelpWater helpWater) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", helpWater.uid);
		values.put("sync", helpWater.sync);
		values.put("date", helpWater.date);
		values.put("dateMonth", helpWater.dateMonth);
		values.put("day", helpWater.day);
		values.put("time", helpWater.time);
		values.put("timeMill", helpWater.timeMill);
		values.put("inWater", helpWater.inWater);
		values.put("outWater", helpWater.outWater);
		values.put("diff", helpWater.diff);
		db.insert("water", null, values);
		db.close();
	}
	public void insertList(ArrayList<HelpWater> list) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for(int i = 0 ; i < list.size() ;i++){
			ContentValues values = new ContentValues();
			HelpWater helpWater = list.get(i);
			values.put("uid", helpWater.uid);
			values.put("sync", helpWater.sync);
			values.put("date", helpWater.date);
			values.put("dateMonth", helpWater.dateMonth);
			values.put("day", helpWater.day);
			values.put("time", helpWater.time);
			values.put("timeMill", helpWater.timeMill);
			values.put("inWater", helpWater.inWater);
			values.put("outWater", helpWater.outWater);
			values.put("diff", helpWater.diff);
			db.insert("water", null, values);
		}
		db.close();
	}

	 // 查询数据  日期
	public ArrayList<HelpWater> getHuliListByDate(String date, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpWater> huliLifeArray = new ArrayList<HelpWater>();
		Cursor cursor = db.query("water", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "inWater","outWater", "diff"}, "date=? and uid=?", 
				new String[] { date, uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	//查询数据  月份
	public ArrayList<HelpWater> getWaterListByMonth(String month, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpWater> huliLifeArray = new ArrayList<HelpWater>();
		Cursor cursor = db.query("water", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "inWater","outWater", "diff"}, "dateMonth=? and uid=?", 
				new String[] { month, uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	//未同步查询
	public ArrayList<HelpWater> getListByNoSync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpWater> huliLifeArray = new ArrayList<HelpWater>();
		Cursor cursor = db.query("water", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "inWater","outWater", "diff"}, "sync=? and uid=?", 
				new String[] { String.valueOf(ConstantUtil.SYNC_NO), uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	//获取是否有未同步的数据
	public boolean haveNoSync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		boolean isHave = false;
		Cursor cursor = db.query("water", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "inWater","outWater", "diff"}, "date=? and uid=?", 
				new String[] { String.valueOf(ConstantUtil.SYNC_NO), uid}, null, null, null);
		if(cursor.moveToFirst() == false){
			isHave = false;
		}else{
			isHave = true;
		}
		cursor.close();
		db.close();
		return isHave;
	}
	
	//当日是否有数据
	public boolean haveDataByDate(String date, String uid){
		boolean isHave = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("water", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "inWater","outWater", "diff"}, "date=? and uid=?", 
				 new String[] { date, uid}, null, null, null);
		if(cursor.moveToFirst() == false){
			isHave = false;
		}else{
			isHave = true;
		}
		cursor.close();
		db.close();
		return isHave;
	}
	
	
	 //对cursor查询结果方法抽取
	private void cursorMethod(ArrayList<HelpWater> huliLifeArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			HelpWater helpWater = new HelpWater();
			helpWater.id = cursor.getInt(cursor.getColumnIndex("id"));
			helpWater.uid = cursor.getString(cursor.getColumnIndex("uid"));
			helpWater.sync = cursor.getInt(cursor.getColumnIndex("sync"));
			helpWater.date = cursor.getString(cursor.getColumnIndex("date"));
			helpWater.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
			helpWater.day = cursor.getString(cursor.getColumnIndex("day"));
			helpWater.time = cursor.getString(cursor.getColumnIndex("time"));
			helpWater.timeMill = cursor.getLong(cursor.getColumnIndex("timeMill"));
			helpWater.inWater = cursor.getInt(cursor.getColumnIndex("inWater"));
			helpWater.outWater = cursor.getInt(cursor.getColumnIndex("outWater"));
			helpWater.diff = cursor.getInt(cursor.getColumnIndex("diff"));
			huliLifeArray.add(helpWater);
		}
	}


	 //修改数据
	public int updata(HelpWater huliWeight, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", huliWeight.uid);
		values.put("sync", huliWeight.sync);
		values.put("date", huliWeight.date);
		values.put("dateMonth", huliWeight.dateMonth);
		values.put("day", huliWeight.day);
		values.put("time", huliWeight.time);
		values.put("timeMill", huliWeight.timeMill);
		values.put("inWater", huliWeight.inWater);
		values.put("outWater", huliWeight.outWater);
		values.put("diff", huliWeight.diff);
		int result = db.update("water", values, "date=? and uid=?",
				new String[] { String.valueOf(huliWeight.date), uid});
		db.close();
		return result;
	}
	
	//将未同步的标志改为同步
	public int updataSyncNoToYes(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sync", ConstantUtil.SYNC_YES);
		int result = db.update("water", values, "sync=? and uid=?",new String[] { String.valueOf( ConstantUtil.SYNC_NO), uid});
		db.close();
		return result;
	}
	
	//删除已经同步的
	public void deleteBySync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("water", "sync=? and uid=?",new String[] { String.valueOf(ConstantUtil.SYNC_YES), uid});
		db.close();
	}
	

//	public void delete(int sync, String uid) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		db.delete("celingData", "sync=? and uid=?",new String[] { String.valueOf(sync), uid});
//		db.close();
//	}
	
	/**
	 * 删除数据
	 */
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("water",null,null);
		db.close();
	}
}