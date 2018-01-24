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

import com.yikang.heartmark.model.HelpHeart;
import com.yikang.heartmark.util.ConstantUtil;

@SuppressLint("SimpleDateFormat")
public class HelpHeartDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public HelpHeartDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
    //添加数据
	public void insert(HelpHeart helpHeart) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", helpHeart.uid);
		values.put("sync", helpHeart.sync);
		values.put("date", helpHeart.date);
		values.put("dateMonth", helpHeart.dateMonth);
		values.put("day", helpHeart.day);
		values.put("time", helpHeart.time);
		values.put("timeMill", helpHeart.timeMill);
		values.put("heart", helpHeart.heart);
		db.insert("heart", null, values);
		db.close();
	}
	//把一个链表的数据插入数据库
	public void insertList(ArrayList<HelpHeart> list) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for(int i = 0; i< list.size() ; i++){
			ContentValues values = new ContentValues();
			HelpHeart helpHeart = list.get(i);
			values.put("uid", helpHeart.uid);
			values.put("sync", helpHeart.sync);
			values.put("date", helpHeart.date);
			values.put("dateMonth", helpHeart.dateMonth);
			values.put("day", helpHeart.day);
			values.put("time", helpHeart.time);
			values.put("timeMill", helpHeart.timeMill);
			values.put("heart", helpHeart.heart);
			db.insert("heart", null, values);
		}
		db.close();
	}

	//查询  日期
	public ArrayList<HelpHeart> getListByDate(String date, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpHeart> huliLifeArray = new ArrayList<HelpHeart>();
		Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "heart"}, "date=? and uid=?", 
				new String[] { date, uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	/**新该
	 * 查询  当前时间   所在周 的一周内的数据  时间段
	* <p>Title: getHeartListByTime</p>
	* <p>Description: </p>
	* @param date
	* @param uid
	* @param startTime
	* @param endTime
	* @return
	 */
	public ArrayList<HelpHeart> getHeartListByTime(Date date, String uid, long startTime, long endTime){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ArrayList<HelpHeart> helpHeartArray = new ArrayList<HelpHeart>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM:dd");  //格式化时间的类
		
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        //cal.get(Calendar.DAY_OF_WEEK);
        //----------------从星期天开始计算，如果今天星期二，那么返回3  
        // 1代表是星期天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if(1 == dayWeek) {  
        //代表如果是星期天   就返回一天前的日期
        	cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
      //将本周的星期一设为日历的时间  星期第一天是星期一：
		cal.setFirstDayOfWeek(Calendar.MONDAY);  
		//一周中的第几天 就不用减1了
		int day = cal.get(Calendar.DAY_OF_WEEK);  
		//让日历中的天数提前几天
		//getFirstDayOfWeek() 获取一星期的第一天；例如，在美国，这一天是 SUNDAY，而在法国，这一天是 MONDAY。
		// 想前推算到星期一的时间
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);  
		
		//利用循环将现在时间的一周之内的数据抽取出来
		// 通过这个循环取出一周内的数据
		for(int i = 0;i<=6;i++){
			if(i!=0){
				//只有不是第一个数据，日期就不断加1
				cal.add(Calendar.DATE, 1);  
			}
			
			//将月份和日期格式化  yyyy-MM:dd
			String imptimeEnd = sdf.format(cal.getTime());  
			//这个是年和月
			String dateMonth = imptimeEnd.substring(0, imptimeEnd.indexOf(":"));
				//这个是日期
			String dayString = imptimeEnd.substring(imptimeEnd.lastIndexOf(":")+1,imptimeEnd.length());
			
			Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
					"dateMonth", "day", "time", "timeMill", "heart"}, 
					"uid=? and dateMonth=? and day= ?", new String[] {uid, dateMonth, 
					dayString}, null, null,null);
			if (cursor.moveToNext()) {
				HelpHeart helpHeart = new HelpHeart();
				helpHeart.id = cursor.getInt(cursor.getColumnIndex("id"));
				helpHeart.uid = cursor.getString(cursor.getColumnIndex("uid"));
				helpHeart.sync = cursor.getInt(cursor.getColumnIndex("sync"));
				helpHeart.date = cursor.getString(cursor.getColumnIndex("date"));
				helpHeart.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
				helpHeart.day = cursor.getString(cursor.getColumnIndex("day"));
				helpHeart.time = cursor.getString(cursor.getColumnIndex("time"));
				helpHeart.timeMill = cursor.getLong(cursor.getColumnIndex("timeMill"));
				helpHeart.heart = cursor.getInt(cursor.getColumnIndex("heart"));
				//helpHeartArray 这个是全局变量  每增加一个数据，就添加一个值
				helpHeartArray.add(helpHeart);
			}
			cursor.close();
		}
		
		
		db.close();
		return helpHeartArray;
	}
	
	//查询  区间内  时间段  的数据
	public ArrayList<HelpHeart> getHeartListByTime(String uid, long startTime, long endTime){
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpHeart> helpHeartArray = new ArrayList<HelpHeart>();
		Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "heart"}, 
				"uid=? and timeMill>? and timeMill<?", new String[] {uid, String.valueOf(startTime), 
				String.valueOf(endTime)}, null, null, "date asc");
		cursorMethod(helpHeartArray, cursor);
		cursor.close();
		db.close();
		return helpHeartArray;
	}
	
	//查询月份
	public ArrayList<HelpHeart> getHeartListByMonth(String month, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpHeart> huliLifeArray = new ArrayList<HelpHeart>();
		Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "heart"}, "dateMonth=? and uid=?", 
				new String[] { month, uid}, null, null, "date" + " desc");
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}
	
	
	//未同步查询
	public ArrayList<HelpHeart> getListByNoSync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<HelpHeart> huliLifeArray = new ArrayList<HelpHeart>();
		Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "heart"}, "sync=? and uid=?", 
				new String[] { String.valueOf(ConstantUtil.SYNC_NO), uid}, null, null, null);
		cursorMethod(huliLifeArray, cursor);
		cursor.close();
		db.close();
		return huliLifeArray;
	}

	//是否有未同步数据
	public boolean haveNoSync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		boolean isHave = false;
		Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "heart"}, "date=? and uid=?", 
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
	
	//当天是否已经有数据
	public boolean haveDataByDate(String date, String uid){
		boolean isHave = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("heart", new String[] { "id", "uid", "sync","date",
				"dateMonth", "day", "time", "timeMill", "heart"}, "date=? and uid=?", 
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
	
	
	// 对cursor查询结果方法抽取
	private void cursorMethod(ArrayList<HelpHeart> huliLifeArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			HelpHeart helpHeart = new HelpHeart();
			helpHeart.id = cursor.getInt(cursor.getColumnIndex("id"));
			helpHeart.uid = cursor.getString(cursor.getColumnIndex("uid"));
			helpHeart.sync = cursor.getInt(cursor.getColumnIndex("sync"));
			helpHeart.date = cursor.getString(cursor.getColumnIndex("date"));
			helpHeart.dateMonth = cursor.getString(cursor.getColumnIndex("dateMonth"));
			helpHeart.day = cursor.getString(cursor.getColumnIndex("day"));
			helpHeart.time = cursor.getString(cursor.getColumnIndex("time"));
			helpHeart.timeMill = cursor.getLong(cursor.getColumnIndex("timeMill"));
			helpHeart.heart = cursor.getInt(cursor.getColumnIndex("heart"));
			huliLifeArray.add(helpHeart);
		}
	}

    //修改数据
	public int updata(HelpHeart helpHeart, String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", helpHeart.uid);
		values.put("sync", helpHeart.sync);
		values.put("date", helpHeart.date);
		values.put("dateMonth", helpHeart.dateMonth);
		values.put("day", helpHeart.day);
		values.put("time", helpHeart.time);
		values.put("timeMill", helpHeart.timeMill);
		values.put("heart", helpHeart.heart);
		int result = db.update("heart", values, "date=? and uid=?",
				new String[] { String.valueOf(helpHeart.date), uid});
		db.close();
		return result;
	}
	
	//将未同步的标志改为同步
	public int updataSyncNoToYes(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sync", ConstantUtil.SYNC_YES);
		int result = db.update("heart", values, "sync=? and uid=?",new String[] { String.valueOf( ConstantUtil.SYNC_NO), uid});
		db.close();
		return result;
	}
	
	//删除已同步的
	public void deleteBySync(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("heart", "sync=? and uid=?",new String[] { String.valueOf(ConstantUtil.SYNC_YES), uid});
		db.close();
	}
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
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("heart",null,null);
		db.close();
	}
}