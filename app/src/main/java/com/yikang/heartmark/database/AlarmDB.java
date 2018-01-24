package com.yikang.heartmark.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.Alarm;

public class AlarmDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public AlarmDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	//插入一个对象
	public void insertAlarm(Alarm alarm) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", alarm.type);
		values.put("alarmId", alarm.alarmId);
		values.put("alarmTime", alarm.alarmTime);
		values.put("time", alarm.time);
		values.put("week", alarm.week);
		values.put("yaoId", alarm.yaoId);
		values.put("yaoName", alarm.yaoName);
		values.put("yaoType", alarm.yaoType);
		values.put("setTime", alarm.setTime);
		db.insert("alarm", null, values);
		db.close();
	}

	//得到所有对象的集合
	public ArrayList<Alarm> getAlarmList() {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Alarm> alarmArray = new ArrayList<Alarm>();
		Cursor cursor = db.query("alarm", new String[] { "id", "alarmId","yaoId"}, null, null, null, null, null);
		//将cursor对应的值放入链表集合中
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}

	//查询使用某种药的人群
	public ArrayList<Alarm> getAlarmListByYao(int yaoId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Alarm> alarmArray = new ArrayList<Alarm>();
		Cursor cursor = db.query("alarm", new String[] { "id", "alarmId","yaoId"}, "yaoId=?", 
				new String[] {String.valueOf(yaoId)}, null, null, null);
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}
	
	//查询使用某类型药的人群
	public ArrayList<Alarm> getAlarmListByType(String type) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Alarm> alarmArray = new ArrayList<Alarm>();
		Cursor cursor = db.query("alarm", new String[] { "id", "type","alarmId","alarmTime","time","week","yaoId","yaoName","yaoType","setTime"}, 
				"type=?", new String[] {String.valueOf(type)}, null, null, null);
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}
	
	public ArrayList<Alarm> getAlarmListByYaoType(String type, String yaoType) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Alarm> alarmArray = new ArrayList<Alarm>();
		Cursor cursor = db.query("alarm", new String[] { "id", "type","alarmId","alarmTime","time","week","yaoId","yaoName","yaoType","setTime"}, 
				"type=? and yaoType=?", new String[] {type, yaoType}, null, null, null);
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}
	
	public ArrayList<Alarm> getAlarmListByYaoTypeName(String type, String yaoType, String yaoName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Alarm> alarmArray = new ArrayList<Alarm>();
		Cursor cursor = db.query("alarm", new String[] { "id", "type","alarmId","alarmTime","time","week","yaoId","yaoName","yaoType","setTime"}, 
				"type=? and yaoType=? and yaoName=?", new String[] {type, yaoType, yaoName}, null, null, null);
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}
	
	public ArrayList<Alarm> getAlarmListByTime(String type, String time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Alarm> alarmArray = new ArrayList<Alarm>();
		Cursor cursor = db.query("alarm", new String[] { "id", "type","alarmId","alarmTime","time","week","yaoId","yaoName","yaoType","setTime"}, 
				"type=? and time=?", new String[] {type, time}, null, null, null);
		cursorMethod(alarmArray, cursor);
		cursor.close();
		db.close();
		return alarmArray;
	}
	
	//将cursor对应的值放入链表集合中
	private void cursorMethod(ArrayList<Alarm> alarmArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			Alarm alarm = new Alarm();
			alarm.id = cursor.getInt(cursor.getColumnIndex("id"));
			alarm.type = cursor.getString(cursor.getColumnIndex("type"));
			alarm.alarmId = cursor.getInt(cursor.getColumnIndex("alarmId"));
			alarm.alarmTime = cursor.getLong(cursor.getColumnIndex("alarmTime"));
			alarm.setTime = cursor.getLong(cursor.getColumnIndex("setTime"));
			alarm.time = cursor.getString(cursor.getColumnIndex("time"));
			alarm.week = cursor.getString(cursor.getColumnIndex("week"));
			alarm.yaoId = cursor.getInt(cursor.getColumnIndex("yaoId"));
			alarm.yaoName = cursor.getString(cursor.getColumnIndex("yaoName"));
			alarm.yaoType = cursor.getString(cursor.getColumnIndex("yaoType"));
			alarmArray.add(alarm);
		}
	}
	
	//删除某种定时
	public void delete(int yaoId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("alarm", "yaoId=?",new String[] {String.valueOf(yaoId)});
		db.close();
	}
	
	public void deleteByYaoTypeName(String type, String yaoType, String yaoName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("alarm", "type=? and yaoType=? and yaoName=?",new String[] {type, yaoType, yaoName});
		db.close();
	}
	
	public void deleteByTypeName(String type, String time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("alarm", "type=? and time=?",new String[] {type, time});
		db.close();
	}
}