package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.SetRemindYaoAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;

public class SetRemindYaoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener {
	private AlarmDB alarmDB;
	private ArrayList<String> titleList;
	private ArrayList<ArrayList<String>> dataList;
	
	private ExpandableListView listView;
	private ImageView imageview;
	private SetRemindYaoAdapter adapter;
	public static SetRemindYaoActivity instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_remind_yao);
		instance = this;
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshListView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setRemindYaoTopBar);
		topbar.setTopbarTitle(R.string.remind_yao);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightButton(R.drawable.yongyao_add);
		topbar.setOnTopbarRightButtonListener(this);
		
		alarmDB = new AlarmDB(SetRemindYaoActivity.this);
		titleList = new ArrayList<String>();
		dataList = new ArrayList<ArrayList<String>>();
		
		listView = (ExpandableListView) findViewById(R.id.set_remind_yao_listview);
		imageview = (ImageView) findViewById(R.id.setRemindYaoimageview);
	}

	public void refreshListView(){
		titleList.clear();//标题数据
		dataList.clear();//内容数据
		ArrayList<Alarm> alarmList = null;
		//获得提醒数据的类别
		alarmList= alarmDB.getAlarmListByType(Alarm.TYPE_YAO);
		for(int i=0; i<alarmList.size(); i++){
			boolean isHave = false;//标记变量 每一次循环 都要初始化
			if(i == 0){//第一个标题 要加上
				titleList.add(alarmList.get(i).yaoType);
			}
			
			for(int k=0; k<titleList.size(); k++){
				if(alarmList.get(i).yaoType.equals(titleList.get(k))){
					isHave = true;
				}
			}
			
			if(!isHave){//如果标题不重复 就把这个标题加入链表中
				titleList.add(alarmList.get(i).yaoType);
			}
		}
		
		if(titleList.size()<=0){
			listView.setVisibility(View.GONE);
			imageview.setVisibility(View.VISIBLE);
		}else{
			imageview.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
		
		ArrayList<String> tempArray;
		for (int index = 0; index < titleList.size(); ++index) {
			tempArray = new ArrayList<String>();
			dataList.add(tempArray);
		}
		
		for(int k=0; k<titleList.size(); k++){
			alarmList.clear();
			//获得字列表的数据
			alarmList = alarmDB.getAlarmListByYaoType(Alarm.TYPE_YAO, titleList.get(k));
			// alarmList是提醒的个数
			for(int j=0; j<alarmList.size(); j++){
				boolean isHave = false;
				if(j == 0){//第一个直接加入药名
					dataList.get(k).add(alarmList.get(j).yaoName);
				}
				for(int y=0; y<dataList.get(k).size(); y++){
					if(alarmList.get(j).yaoName.equals(dataList.get(k).get(y))){
						isHave = true;
					}
				}
				if(!isHave){//分类下药名的数量
				dataList.get(k).add(alarmList.get(j).yaoName);
				}
			}
		}
		
		adapter = new SetRemindYaoAdapter(titleList, dataList,SetRemindYaoActivity.this);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		Intent addIntent = new Intent(SetRemindYaoActivity.this,YongYaoAddActivity.class);
		startActivity(addIntent);
	}
}
