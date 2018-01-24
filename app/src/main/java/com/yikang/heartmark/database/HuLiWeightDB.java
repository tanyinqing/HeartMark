package com.yikang.heartmark.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.HuLiWeight;
import com.yikang.heartmark.util.ConstantUtil;

@SuppressLint("SimpleDateFormat")
public class HuLiWeightDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public HuLiWeightDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	public HuLiWeightDB(Context context,String calendarDate, int weightValue) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * 添加数据
	 */
	public void insert(HuLiWeight huliWeight) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", huliWeight.uid);
		values.put("sync", huliWeight.sync);
		values.put("date", huliWeight.date);
		values.put("dateMonth", huliWeight.dateMonth);
		values.put("day", huliWeight.day);
		values.put("time", huliWeight.time);
		values.put("timeMill", huliWeight.timeMill);
		values.put("baseWeight", huliWeight.baseWeight);
		values.put("thisWeight", huliWeight.thisWeight);
		values.put("diff", huliWeight.diff);
		db.insert("weight", null, values);
		db.close();
	}
	
	public void insertList(ArrayList<HuLiWeight> list) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for(int i=0;i<list.size();i++){
			ContentValues values = new ContentValues();
			HuLiWeight huliWeight = list.get(i);
			values.put("uid", huliWeight.uid);
			values.put("sync", huliWeight.sync);
			values.put("date", huliWeight.date);
			values.put("dateMonth", huliWeight.dateMonth);
			values.put("day", huliWeight.day);
			values.put("time", huliWeight.time);
			values.put("timeMill", huliWeight.timeMill);
			values.put("baseWeight", huliWeight.baseWeight);
			values.put("thisWeight", huliWeight.thisWeight);
			values.put("diff", huliWeight.diff);
			db.insert("weight", null, values);
		}
		db.close();
	}
	
    //日期查询
	public ArrayList<HuLiWeight> getHuliListByDate(String date, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HuLiWeight> huliLifeArray = new ArrayList<HuLiWeight>();
		Cursor cursor = db.query("weight", new String[] { "id", "uid", "sync", "date",
				"dateMonth", "day", "time", "timeMill", "baseWeight","thisWeight", "diff"}, "date=? and uid=?", 
				new String[] { date, uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	
	//时间段查询
	public ArrayList<HuLiWeight> getHuLiListByTime(Date date, String uid, long startTime, long endTime){
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HuLiWeight> huliLifeArray = new ArrayList<HuLiWeight>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM:dd");  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if(1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);  
		for(int i = 0;i<=6;i++){
			if(i!=0){
				cal.add(Calendar.DATE, 1);  
			}
			String imptimeEnd = sdf.format(cal.getTime());  
			String dateMonth = imptimeEnd.substring(0, imptimeEnd.indexOf(":"));
			String dayString = imptimeEnd.substring(imptimeEnd.lastIndexOf(":")+1,imptimeEnd.length());
			Cursor cursor = db.query("weight", new String[] { "id", "uid", "sync", "date",
					"dateMonth", "day", "time", "timeMill", "baseWeight","thisWeight", "diff"}, 
					"uid=? and dateMonth=? and day= ?", new String[] {uid, dateMonth, 
					dayString}, null, null,null);
			if (cursor.moveToNext()) {
				HuLiWeight huliWeight = new HuLiWeight();
				huliWeight.id = cursor.getInt(cursor.getColumnIndex("id"));
				huliWeight.uid = cursor.getString(cursor.getColumnIndex("uid"));
				huliWeight.sync = cursor.getInt(cursor.getColumnIndex("sync"));
				huliWeight.date = cursor.getString(cursor.getColumnIndex("date"));
				huliWeight.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
				huliWeight.day = cursor.getString(cursor.getColumnIndex("day"));
				huliWeight.time = cursor.getString(cursor.getColumnIndex("time"));
				huliWeight.timeMill = cursor.getLong(cursor.getColumnIndex("timeMill"));
				huliWeight.baseWeight = cursor.getInt(cursor.getColumnIndex("baseWeight"));
				huliWeight.thisWeight = cursor.getInt(cursor.getColumnIndex("thisWeight"));
				huliWeight.diff = cursor.getInt(cursor.getColumnIndex("diff"));
				huliLifeArray.add(huliWeight);
			}
			cursor.close();
		}
		db.close();
		return huliLifeArray;
	}
	
	//时间段查询
	public ArrayList<HuLiWeight> getHuLiListByTime(String uid, long startTime, long endTime){
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HuLiWeight> huliLifeArray = new ArrayList<HuLiWeight>();
		Cursor cursor = db.query("weight", new String[] { "id", "uid", "sync", "date",
				"dateMonth", "day", "time", "timeMill", "baseWeight","thisWeight", "diff"}, 
				"uid=? and timeMill>? and timeMill<?", new String[] {uid, String.valueOf(startTime), 
				String.valueOf(endTime)}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	
	//月份查询
	public ArrayList<HuLiWeight> getHuliListByMonth(String month, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HuLiWeight> huliLifeArray = new ArrayList<HuLiWeight>();
		Cursor cursor = db.query("weight", new String[] { "id", "uid", "sync", "date",
				"dateMonth", "day", "time", "timeMill", "baseWeight","thisWeight", "diff"}, "dateMonth=? and uid=?", 
				new String[] { month, uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	//未同步查询
	public ArrayList<HuLiWeight> getListByNoSync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HuLiWeight> huliLifeArray = new ArrayList<HuLiWeight>();
		Cursor cursor = db.query("weight", new String[] { "id", "uid", "sync", "date",
				"dateMonth", "day", "time", "timeMill", "baseWeight","thisWeight", "diff"}, "sync=? and uid=?", 
				new String[] { String.valueOf(ConstantUtil.SYNC_NO), uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	
	//获取是否有未同步的数据
	public boolean haveNoSync (String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		boolean isHave = false;
		Cursor cursor = db.query("weight", new String[] { "id", "uid", "sync", "date",
				"dateMonth", "day", "time", "timeMill", "baseWeight","thisWeight", "diff"}, "sync=? and uid=?", 
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
	
	 //对cursor查询结果方法抽取
	private void cursorMethod(ArrayList<HuLiWeight> huliLifeArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			HuLiWeight huliWeight = new HuLiWeight();
			huliWeight.id = cursor.getInt(cursor.getColumnIndex("id"));
			huliWeight.uid = cursor.getString(cursor.getColumnIndex("uid"));
			huliWeight.sync = cursor.getInt(cursor.getColumnIndex("sync"));
			huliWeight.date = cursor.getString(cursor.getColumnIndex("date"));
			huliWeight.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
			huliWeight.day = cursor.getString(cursor.getColumnIndex("day"));
			huliWeight.time = cursor.getString(cursor.getColumnIndex("time"));
			huliWeight.timeMill = cursor.getLong(cursor.getColumnIndex("timeMill"));
			huliWeight.baseWeight = cursor.getInt(cursor.getColumnIndex("baseWeight"));
			huliWeight.thisWeight = cursor.getInt(cursor.getColumnIndex("thisWeight"));
			huliWeight.diff = cursor.getInt(cursor.getColumnIndex("diff"));
			huliLifeArray.add(huliWeight);
		}
	}

	//修改单条数据
	public int updata(HuLiWeight huliWeight, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", huliWeight.uid);
		values.put("sync", huliWeight.sync);
		values.put("date", huliWeight.date);
		values.put("dateMonth", huliWeight.dateMonth);
		values.put("day", huliWeight.day);
		values.put("time", huliWeight.time);
		values.put("timeMill", huliWeight.timeMill);
		values.put("baseWeight", huliWeight.baseWeight);
		values.put("thisWeight", huliWeight.thisWeight);
		values.put("diff", huliWeight.diff);
		int result = db.update("weight", values, "date=? and uid=?",
				new String[] { String.valueOf(huliWeight.date), uid});
		db.close();
		return result;
	}
	
	
	//将未同步的标志改为同步
	public int updataSyncNoToYes(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sync", ConstantUtil.SYNC_YES);
		int result = db.update("weight", values, "sync=? and uid=?",new String[] { String.valueOf( ConstantUtil.SYNC_NO), uid});
		db.close();
		return result;
	}
	
	public void deleteBySync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("weight", "sync=? and uid=?",new String[] { String.valueOf(ConstantUtil.SYNC_YES), uid});
		db.close();
	}
	
	/**
	 * 删除数据
	 */
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("weight",null,null);
		db.close();
	}
}