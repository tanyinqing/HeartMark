package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.HuLiLife;

public class HuLiLifeDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public HuLiLifeDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	/**
	 * 添加数据
	 */
	public void insertHuliLifeData(HuLiLife huliLife) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", huliLife.uid);
		values.put("sync", huliLife.sync);
		values.put("date", huliLife.date);
		values.put("dateMonth", huliLife.dateMonth);
		values.put("day", huliLife.day);
		values.put("time", huliLife.time);
		values.put("salt", huliLife.salt);
		values.put("water", huliLife.water);
		values.put("weight", huliLife.weight);
		values.put("food", huliLife.food);
		values.put("sport", huliLife.sport);
		db.insert("huliLife", null, values);
		db.close();
	}

	/**
	 * 查询数据
	 */
	public ArrayList<HuLiLife> getHuliListByDateMonth(String dateMonth, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HuLiLife> huliLifeArray = new ArrayList<HuLiLife>();
		Cursor cursor = db.query("huliLife", new String[] { "id", "uid","sync", "date",
				"dateMonth", "day", "time", "salt","water", "weight", "food", "sport" }, "dateMonth=? and uid=?", new String[] { dateMonth, uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	/**
	 * 对cursor查询结果方法抽取
	 */
	private void cursorMethod(ArrayList<HuLiLife> huliLifeArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			HuLiLife huliLife = new HuLiLife();
			huliLife.id = cursor.getInt(cursor.getColumnIndex("id"));
			huliLife.uid = cursor.getString(cursor.getColumnIndex("uid"));
			huliLife.sync = cursor.getInt(cursor.getColumnIndex("sync"));
			huliLife.date = cursor.getString(cursor.getColumnIndex("date"));
			huliLife.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
			huliLife.day = cursor.getString(cursor.getColumnIndex("day"));
			huliLife.time = cursor.getString(cursor.getColumnIndex("time"));
			huliLife.salt = cursor.getString(cursor.getColumnIndex("salt"));
			huliLife.water = cursor.getString(cursor.getColumnIndex("water"));
			huliLife.weight = cursor.getString(cursor.getColumnIndex("weight"));
			huliLife.food = cursor.getString(cursor.getColumnIndex("food"));
			huliLife.sport = cursor.getString(cursor.getColumnIndex("sport"));
			huliLifeArray.add(huliLife);
		}
	}

	/**
	 * 修改数据
	 */
//	public int updataBloodData(String uid) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("sync", CeLingData.SYNC_YES);
//		int result = db.update("celingData", values, "sync=? and uid=?",new String[] { String.valueOf( CeLingData.SYNC_NO), uid});
//		db.close();
//		return result;
//	}
	
	/**
	 * 删除数据
	 */
//	public void delete(int sync, String uid) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		db.delete("celingData", "sync=? and uid=?",new String[] { String.valueOf(sync), uid});
//		db.close();
//	}
	
	/**
	 * 删除数据
	 */
//	public void delete() {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		db.delete("celingData", null,null);
//		db.close();
//	}
}