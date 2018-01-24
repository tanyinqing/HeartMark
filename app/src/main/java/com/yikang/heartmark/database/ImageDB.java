package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.ZiXun;

public class ImageDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public ImageDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	public void insertImage(ArrayList<ZiXun> pagerList) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for(int i = 0; i <pagerList.size() ; i++){
			values.put("content", pagerList.get(i).content);
			values.put("image", pagerList.get(i).image);
			values.put("newId", pagerList.get(i).newId);
			db.insert("image", null, values);
		}
		db.close();
	}


	//查询对象的列表
	public ArrayList<ZiXun> getImageList() {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<ZiXun> alarmArray = new ArrayList<ZiXun>();
		Cursor cursor = db.query("image", new String[] { "content", "image","newId"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			ZiXun ziXun = new ZiXun();
			ziXun.content = cursor.getString(cursor.getColumnIndex("content"));
			ziXun.image = cursor.getString(cursor.getColumnIndex("image"));
			ziXun.newId = cursor.getString(cursor.getColumnIndex("newId"));
			alarmArray.add(ziXun);
		}
		cursor.close();
		db.close();
		return alarmArray;
	}

	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("image",null,null);
		db.close();
	}
}