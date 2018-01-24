package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.Scan;

public class ScanDB {

	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public ScanDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * 添加数据
	 */
	public void insertScan(Scan scan) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", scan.id);
		values.put("type", scan.type);
		values.put("scan", scan.scan);
		values.put("scanNumber", scan.scanNumber);
		db.insert("scan", null, values);
		db.close();
	}

	// 查询所有
	public ArrayList<Scan> getScanList() {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Scan> alarmArray = new ArrayList<Scan>();
		Cursor cursor = db.query("scan", new String[] { "id", "type", "scan",
				"scanNumber" }, null, null, null, null, null);
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}

	/**
	 * 查询数据,批号
	 */
	public Scan getScanByNumber(String scanNumber) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Scan scan = new Scan();
		Cursor cursor = db.query("scan", new String[] { "id", "type", "scan",
				"scanNumber" }, "scanNumber=?", new String[] { scanNumber },null, null, null);
		while (cursor.moveToNext()) {
			scan.id = cursor.getInt(cursor.getColumnIndex("id"));
			scan.type = cursor.getString(cursor.getColumnIndex("type"));
			scan.scan = cursor.getString(cursor.getColumnIndex("scan"));
			scan.scanNumber = cursor.getString(cursor.getColumnIndex("scanNumber"));
		}
		cursor.close();
		db.close();
		return scan;
	}

	// 通过type 查 name
	// public ArrayList<String> getNameListByType(String uid, String type){
	// SQLiteDatabase db = helper.getWritableDatabase();
	// ArrayList<Yao> yaoArray = new ArrayList<Yao>();
	// Cursor cursor = db.query("yao", new String[] { "id", "uid","name",
	// "type",
	// "typeName", "firm", "content", "contentText"}, "uid=? and typeName=?",
	// new String[] {uid,type}, null, null, null);
	// cursorMethod(yaoArray, cursor);
	// cursor.close();
	// db.close();
	// ArrayList<String> arrayList = new ArrayList<String>();
	// for(int i=0; i<yaoArray.size(); i++){
	// arrayList.add(yaoArray.get(i).name);
	// }
	// return arrayList;
	// }

	/**
	 * 对cursor查询结果方法抽取
	 */
	private void cursorMethod(ArrayList<Scan> scanArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			Scan scan = new Scan();
			scan.id = cursor.getInt(cursor.getColumnIndex("id"));
			scan.type = cursor.getString(cursor.getColumnIndex("type"));
			scan.scan = cursor.getString(cursor.getColumnIndex("scan"));
			scan.scanNumber = cursor.getString(cursor
					.getColumnIndex("scanNumber"));
			scanArray.add(scan);
		}
	}

	/**
	 * 修改数据
	 */
	public int updataBloodData(String timeShow, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("timeShow", timeShow);
		int result = db.update("yongyaoRemind", values, "timeShow=? and uid=?",
				new String[] { timeShow, uid });
		db.close();
		return result;
	}

	/**
	 * 删除数据
	 */
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("scan", null, null);
		db.close();
	}
}