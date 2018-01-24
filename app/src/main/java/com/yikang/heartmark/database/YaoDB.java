package com.yikang.heartmark.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikang.heartmark.model.Yao;

public class YaoDB {
	
	@SuppressWarnings("unused")
	private Context context;
	private DBHelper helper;

	public YaoDB(Context context) {
		helper = new DBHelper(context, "HeartMark.db", null, DBHelper.DATABASE_VERSION);
		this.context = context;
	}
	
	/**
	 * 添加数据
	 */
	public void insertYao(Yao yao) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", yao.uid);
		values.put("yaoId", yao.yaoId);
		values.put("name", yao.name);
		values.put("type", yao.type);
		values.put("typeName",  yao.typeName);
		values.put("firm", yao.firm);
		values.put("content", yao.content);
		values.put("contentText", yao.contentText);
		db.insert("yao", null, values);
		db.close();
	}

	/**
	 * 查询数据,uid
	 */
	public ArrayList<Yao> getYaoList(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Yao> yaoArray = new ArrayList<Yao>();
		Cursor cursor = db.query("yao", new String[] { "id", "uid", "yaoId", "name", "type",
				"typeName", "firm", "content", "contentText"}, "uid=?", new String[] {uid}, null, 
				null, null);
		cursorMethod(yaoArray, cursor);
		cursor.close();
		db.close();
		return yaoArray;
	}
	
	/**
	 * 查询数据   只查要的类型  并且类型不能重复  set的数据不重复 类似分组
	 HashSet类，是存在于java.util包中的类。同时也被称为集合，该容器中只能存储不重复的对象
	 */
	public ArrayList<String> getTypeList() {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Yao> yaoArray = new ArrayList<Yao>();
		Cursor cursor = db.query("yao", new String[] { "id", "uid","yaoId","name", "type",
				"typeName", "firm", "content", "contentText"}, null, null, null, null, null);
		cursorMethod(yaoArray, cursor);
		cursor.close();
		db.close();
		Set<String> hashSet = new HashSet<String>(); 
		ArrayList<String> arrayList;
		for(int i=0; i<yaoArray.size(); i++){
			hashSet.add(yaoArray.get(i).typeName);
		}
		arrayList = new ArrayList<String>(hashSet);
		return arrayList;
	}
	
	//通过type 查 药的name   查询所有某一类型药的名字
	public ArrayList<String> getNameListByType(String type){
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Yao> yaoArray = new ArrayList<Yao>();
		Cursor cursor = db.query("yao", new String[] { "id", "uid","yaoId","name", "type",
				"typeName", "firm", "content", "contentText"}, "typeName=?", new String[] {type}, 
				null, null, null);
		cursorMethod(yaoArray, cursor);
		cursor.close();
		db.close();
		ArrayList<String> arrayList = new ArrayList<String>();
		for(int i=0; i<yaoArray.size(); i++){
			arrayList.add(yaoArray.get(i).name);
		}
		return arrayList;
	}
	
	
	/**
	 * 查询数据,type
	 */
	public ArrayList<Yao> getYaoListByType(String type) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Yao> yaoArray = new ArrayList<Yao>();
		Cursor cursor = db.query("yao", new String[] { "id", "uid","yaoId","name", "type",
				"typeName", "firm", "content", "contentText"}, "typeName=?", new String[] {type}, 
				null, null, null);
		cursorMethod(yaoArray, cursor);
		cursor.close();
		db.close();
		return yaoArray;
	}
	
	/**
	 * 查询数据,type and name  查询某类型的某种药
	 */
	public Yao getYaoListDetail(String type, String name) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Yao> yaoArray = new ArrayList<Yao>();
		Yao yao = new Yao();
		Cursor cursor = db.query("yao", new String[] { "id", "uid","yaoId","name", "type",
				"typeName", "firm", "content", "contentText"}, "typeName=? and name=?", new 
				String[] {type, name}, null, null, null);
		cursorMethod(yaoArray, cursor);
		if(yaoArray.size() >0 && yaoArray != null){
			yao = yaoArray.get(0);
		}
		cursor.close();
		db.close();
		return yao;
	}
	
	/**
	 * 对cursor查询结果方法抽取
	 */
	private void cursorMethod(ArrayList<Yao> yaoArray, Cursor cursor) {
		while (cursor.moveToNext()) {
			Yao yao = new Yao();
			yao.id = cursor.getInt(cursor.getColumnIndex("id"));
			yao.uid = cursor.getString(cursor.getColumnIndex("uid"));
			yao.yaoId = cursor.getString(cursor.getColumnIndex("yaoId"));
			yao.name = cursor.getString(cursor.getColumnIndex("name"));
			yao.type = cursor.getString(cursor.getColumnIndex("type"));
			yao.typeName = cursor.getString(cursor.getColumnIndex("typeName"));
			yao.firm = cursor.getString(cursor.getColumnIndex("firm"));
			yao.content = cursor.getString(cursor.getColumnIndex("content"));
			yao.contentText = cursor.getString(cursor.getColumnIndex("contentText"));
			yaoArray.add(yao);
		}
	}
	
	/**
	 * 删除数据  uid
	 */
	public void delete(String uid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("yao", "uid=?",new String[] {uid});
		db.close();
	}
	
	/**
	 * 删除数据  id
	 */
	public void deleteById(String id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("yao", "id=?",new String[] {id});
		db.close();
	}
	
	/**
	 * 删除数据
	 */
	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("yao", null,null);
		db.close();
	}
}