package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.SetRemindHuliAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class SetRemindHuliActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	
	private ListView listView;
	private ImageView imageview;
	private Button buttonAdd;
	private AlarmDB alarmDB;
	public static SetRemindHuliActivity instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_remind_huli);
		instance = this;
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setRemindHuliTopBar);
		topbar.setTopbarTitle(R.string.remind_huli);
		topbar.setOnTopbarLeftButtonListener(this);
		
		alarmDB = new AlarmDB(SetRemindHuliActivity.this);
		
		listView = (ListView) findViewById(R.id.set_remind_huli_listview);
		buttonAdd = (Button) findViewById(R.id.set_remind_huli_add);
		imageview = (ImageView) findViewById(R.id.setRemindhuliimageview);
		
		buttonAdd.setOnClickListener(listener);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentAdd = new Intent(SetRemindHuliActivity.this, SetRemindHuliAddActivity.class);
			startActivity(intentAdd);
		}
	};
	
	public void refreshView(){
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<String> weekList = new ArrayList<String>();
		ArrayList<Alarm> alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_HULI);
		
		//过滤时间
		for(int i=0; i<alarmList.size(); i++){
			boolean isHave = false;
			if(i == 0){
				timeList.add(alarmList.get(i).time);
			}
			
			for(int k=0; k<timeList.size(); k++){
				if(alarmList.get(i).time.equals(timeList.get(k))){
					isHave = true;
				}
			}
			//重复的数据不在添加
			if(!isHave){
				timeList.add(alarmList.get(i).time);
			}
		}
	 
		if(timeList.size()<=0){
			listView.setVisibility(View.GONE);
			imageview.setVisibility(View.VISIBLE);
		}else{
			imageview.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
		
		//过滤重复周期
		for(int i=0; i<timeList.size(); i++){
			ArrayList<Alarm> alarms = alarmDB.getAlarmListByTime(Alarm.TYPE_HULI, timeList.get(i));
			String weeks = "";
			for(int k=0; k<alarms.size(); k++){
				weeks += alarms.get(k).week+",";
			}
			//把最后一个逗号去掉
			weekList.add(weeks.substring(0, weeks.length() - 1));
		}
		
		SetRemindHuliAdapter adapter = new SetRemindHuliAdapter(SetRemindHuliActivity.this, timeList, weekList);
		listView.setAdapter(adapter);
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
