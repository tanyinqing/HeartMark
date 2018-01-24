package com.yikang.heartmark.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GoodDB {
	//点赞数据表
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public GoodDB(Context context) {
		// 数据库的初始化
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}

	//向数据库里加入点赞的数目
	public void insertGood(String goodId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("goodId", goodId);
		db.insert("good", null, values); 
		db.close();
	}

	/*判断是否有点赞的条目*/
	public boolean isHave(String goodId) {
		boolean isHave = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("good", new String[] { "id", "goodId"}, "goodId=?", 
				new String[] {goodId}, null, null, null);
		if(cursor.moveToFirst() == false){
			isHave = false;
		}else{
			isHave = true;
		}
		cursor.close();
		db.close();
		return isHave;
	}

	public void delete(String goodId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("good", "goodId=?",new String[] {goodId});
		db.close();
	}
	
}