package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class SetRemindActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private AlarmDB alarmDB;
	 private AlarmManager alarmManager;
	 private ArrayList<Alarm> alarmList;
	 public int week;
	 private String[] weekList;
	 private String yearMonthDay; 
	 private int weekAlarm = 0;
	 private int day = 0;   //跟今日对比,多少天后开始提醒
	 
	 private Button celingCheck;
	 private Button yaoCheck;
	 private Button huliCheck;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_remind);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setRemindTopBar);
		topbar.setTopbarTitle(R.string.remind);
		topbar.setOnTopbarLeftButtonListener(this);

		alarmDB = new AlarmDB(SetRemindActivity.this);
		//AlarmManager(全局定时器/闹钟）指定时长或以周期形式执行某项操作
		alarmManager = (AlarmManager) SetRemindActivity.this.getSystemService(Context.ALARM_SERVICE);
		//周一到周五的列表
		weekList = getResources().getStringArray(R.array.week_list);
		week = DateUtil.getThisWeek();
	//获取当前日期   用指定的格式来显示
		yearMonthDay = DateUtil.getNowDate(DateUtil.YEAR_MONTH_DAY);
		
		RelativeLayout celingLayout = (RelativeLayout) findViewById(R.id.set_remind_celing_layout);
		RelativeLayout yaoLayout = (RelativeLayout) findViewById(R.id.set_remind_yao_layout);
		RelativeLayout huliLayout = (RelativeLayout) findViewById(R.id.set_remind_huli_layout);
		celingCheck = (Button) findViewById(R.id.set_remind_celing_check);
		yaoCheck = (Button) findViewById(R.id.set_remind_yao_check);
		huliCheck = (Button) findViewById(R.id.set_remind_huli_check);

		//图层和按钮都加入了点击监听
		celingLayout.setOnClickListener(new MyViewOnclicklistener());
		yaoLayout.setOnClickListener(new MyViewOnclicklistener());
		huliLayout.setOnClickListener(new MyViewOnclicklistener());
		celingCheck.setOnClickListener(new MyViewOnclicklistener());
		yaoCheck.setOnClickListener(new MyViewOnclicklistener());
		huliCheck.setOnClickListener(new MyViewOnclicklistener());
		
		if(SharedPreferenceUtil.getBooleanDefaultTrue(SetRemindActivity.this, ConstantUtil.ALARM_CELING)){
			celingCheck.setSelected(true);
		}else{
			celingCheck.setSelected(false);
		}
		
		if(SharedPreferenceUtil.getBooleanDefaultTrue(SetRemindActivity.this, ConstantUtil.ALARM_YAO)){
			yaoCheck.setSelected(true);
		}else{
			yaoCheck.setSelected(false);
		}
		
		if(SharedPreferenceUtil.getBooleanDefaultTrue(SetRemindActivity.this, ConstantUtil.ALARM_HULI)){
			huliCheck.setSelected(true);
		}else{
			huliCheck.setSelected(false);
		}
	}

	
	//layout 监听
	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.set_remind_celing_layout:
				Intent celingIntent = new Intent(SetRemindActivity.this,
						SetRemindCelingActivity.class);
				startActivity(celingIntent);
				break;
			case R.id.set_remind_yao_layout:
				Intent yaoIntent = new Intent(SetRemindActivity.this,
						SetRemindYaoActivity.class);
				startActivity(yaoIntent);
				break;
			case R.id.set_remind_huli_layout:
				Intent huliIntent = new Intent(SetRemindActivity.this,
						SetRemindHuliActivity.class);
				startActivity(huliIntent);
				break;
				//这个是测量提醒
			case R.id.set_remind_celing_check:
                alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_CELING);
				
                //点击的时候，如果被选中
				if (celingCheck.isSelected()) {
					celingCheck.setSelected(false);

					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_CELING, false);
					for(int i=0; i<alarmList.size(); i++){
						//把这些闹钟逐个关闭
	                	  Alarm.closeRemind(alarmManager, SetRemindActivity.this, alarmList.get(i).alarmId);
	                  }
				
				} else {
					celingCheck.setSelected(true);
					  SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_CELING, true);
	                  for(int i=0; i<alarmList.size(); i++){
	                	  alarmList.get(i).isRepeat = false;
	                	  Alarm.setRemindNoDB(alarmList.get(i), alarmManager, SetRemindActivity.this);
	                  }
				}
				break;
				//这个是用药提醒
			case R.id.set_remind_yao_check:
				alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_YAO);

				if (yaoCheck.isSelected()) {
					yaoCheck.setSelected(false);
					
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_YAO, false);
					for(int i=0; i<alarmList.size(); i++){
	                	  Alarm.closeRemind(alarmManager, SetRemindActivity.this, alarmList.get(i).alarmId);
	                  }
					
				} else {
					yaoCheck.setSelected(true);

					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_YAO, true);
					for(int i=0; i<alarmList.size(); i++){
						  for(int k=0; k<weekList.length; k++){
							  if(alarmList.get(i).week.equals(weekList[k])){
								  weekAlarm = k+1;
							  }
						  }
						  
							if(weekAlarm < week){
								day = 7-(week-weekAlarm);
							}
							else if(weekAlarm == week){
								day =0;
							}
							else if(weekAlarm > week){
								day = weekAlarm - week;
							}
						  //日期+时间+天数的毫秒值
						  alarmList.get(i).alarmTime = DateUtil.getLongOfDayTimeAll(yearMonthDay, alarmList.get(i).time) + day*DateUtil.DAY_MILLIS;
	                	  alarmList.get(i).isRepeat = true;
	                	  alarmList.get(i).repeatTime = DateUtil.WEEK_MILLIS;
	                	  Alarm.setRemindNoDB(alarmList.get(i), alarmManager, SetRemindActivity.this);
	                  }
				}
				break;
			case R.id.set_remind_huli_check:
                alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_HULI);
				
				if (huliCheck.isSelected()) {
					huliCheck.setSelected(false);
					
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_HULI, false);
					for(int i=0; i<alarmList.size(); i++){
	                	  Alarm.closeRemind(alarmManager, SetRemindActivity.this, alarmList.get(i).alarmId);
	                  }
					
				} else {
					huliCheck.setSelected(true);
					
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_HULI, true);
					for(int i=0; i<alarmList.size(); i++){
						  for(int k=0; k<weekList.length; k++){
							  if(alarmList.get(i).week.equals(weekList[k])){
								  weekAlarm = k+1;
							  }
						  }
						  
							if(weekAlarm < week){
								day = 7-(week-weekAlarm);
							}
							else if(weekAlarm == week){
								day =0;
							}
							else if(weekAlarm > week){
								day = weekAlarm - week;
							}
						  //响铃的时间是  //日期+时间+天数的毫秒值
						  alarmList.get(i).alarmTime = DateUtil.getLongOfDayTimeAll(yearMonthDay, alarmList.get(i).time) + day*DateUtil.DAY_MILLIS;
	                	  alarmList.get(i).isRepeat = true;//这是个重复闹钟
	                	  alarmList.get(i).repeatTime = DateUtil.WEEK_MILLIS;//重复的时间是一周
	                	  // alarmList.get(i) 得到的是提醒这个对象
	                	  Alarm.setRemindNoDB(alarmList.get(i), alarmManager, SetRemindActivity.this);
	                  }
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	//checkBox监听
	class MyCheckListener implements CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.set_remind_celing_check:
				alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_CELING);
				
				if (isChecked) {
				  SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_CELING, true);
                  for(int i=0; i<alarmList.size(); i++){
                	  alarmList.get(i).isRepeat = false;
                	  Alarm.setRemindNoDB(alarmList.get(i), alarmManager, SetRemindActivity.this);
                  }
				} else {
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_CELING, false);
					for(int i=0; i<alarmList.size(); i++){
	                	  Alarm.closeRemind(alarmManager, SetRemindActivity.this, alarmList.get(i).alarmId);
	                  }
				}
				break;
			case R.id.set_remind_yao_check:
				alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_YAO);

				if (isChecked) {
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_YAO, true);
					for(int i=0; i<alarmList.size(); i++){
						  for(int k=0; k<weekList.length; k++){
							  if(alarmList.get(i).week.equals(weekList[k])){
								  weekAlarm = k+1;
							  }
						  }
						  
							if(weekAlarm < week){
								day = 7-(week-weekAlarm);
							}
							else if(weekAlarm == week){
								day =0;
							}
							else if(weekAlarm > week){
								day = weekAlarm - week;
							}
						  
						  alarmList.get(i).alarmTime = DateUtil.getLongOfDayTimeAll(yearMonthDay, alarmList.get(i).time) + day*DateUtil.DAY_MILLIS;
	                	  alarmList.get(i).isRepeat = true;
	                	  alarmList.get(i).repeatTime = DateUtil.WEEK_MILLIS;
	                	  Alarm.setRemindNoDB(alarmList.get(i), alarmManager, SetRemindActivity.this);
	                  }
				} else {
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_YAO, false);
					for(int i=0; i<alarmList.size(); i++){
	                	  Alarm.closeRemind(alarmManager, SetRemindActivity.this, alarmList.get(i).alarmId);
	                  }
				}
				break;
			case R.id.set_remind_huli_check:
                alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_HULI);
				
				if (isChecked) {
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_HULI, true);
					for(int i=0; i<alarmList.size(); i++){
						  for(int k=0; k<weekList.length; k++){
							  if(alarmList.get(i).week.equals(weekList[k])){
								  weekAlarm = k+1;
							  }
						  }
						  
							if(weekAlarm < week){
								day = 7-(week-weekAlarm);
							}
							else if(weekAlarm == week){
								day =0;
							}
							else if(weekAlarm > week){
								day = weekAlarm - week;
							}
						  
						  alarmList.get(i).alarmTime = DateUtil.getLongOfDayTimeAll(yearMonthDay, alarmList.get(i).time) + day*DateUtil.DAY_MILLIS;
	                	  alarmList.get(i).isRepeat = true;
	                	  alarmList.get(i).repeatTime = DateUtil.WEEK_MILLIS;
	                	  Alarm.setRemindNoDB(alarmList.get(i), alarmManager, SetRemindActivity.this);
	                  }
				} else {
					SharedPreferenceUtil.setBoolean(SetRemindActivity.this, ConstantUtil.ALARM_HULI, false);
					for(int i=0; i<alarmList.size(); i++){
	                	  Alarm.closeRemind(alarmManager, SetRemindActivity.this, alarmList.get(i).alarmId);
	                  }
				}
				break;
			default:
				break;
			}

		}

	}
	*/

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
