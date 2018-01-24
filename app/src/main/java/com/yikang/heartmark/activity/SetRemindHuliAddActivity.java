package com.yikang.heartmark.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.wheel.NumericWheelAdapter;
import com.yikang.heartmark.wheel.WheelView;

public class SetRemindHuliAddActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	private Button buttonSave;
	public WheelView wv_hour;
	public WheelView wv_minutes;
	public ArrayList<CheckBox> checkList ;
	public int week;
	private AlarmDB alarmDB;
	private AlarmManager alarmManager;
	private String[] weekList;
	
	public static SetRemindHuliAddActivity  instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_remind_huli_add);
		instance = this;
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setRemindHuliAddTopBar);
		topbar.setTopbarTitle(R.string.remind_huli_add);
		topbar.setOnTopbarLeftButtonListener(this);
		
		alarmDB = new AlarmDB(instance);
		checkList = new ArrayList<CheckBox>();
		alarmManager  = (AlarmManager) SetRemindHuliAddActivity.this.getSystemService(Context.ALARM_SERVICE);
		week = DateUtil.getThisWeek();
		weekList = getResources().getStringArray(R.array.week_list);
		String dateStr = DateUtil.getNowDate(DateUtil.DATE_HOUR_MINUTE);
		setTimePicker(DateUtil.dateFromString(dateStr, DateUtil.DATE_HOUR_MINUTE));
		
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check1));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check2));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check3));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check4));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check5));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check6));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check7));
		buttonSave = (Button) findViewById(R.id.set_remind_huli_add_save);
		buttonSave.setOnClickListener(listener);
	}

	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//检查是否全没有选择
			boolean isChooseWeek = false;
			for(int i=0; i < checkList.size(); i++){
				if(checkList.get(i).isChecked()){
					isChooseWeek = true;
				}
			}
			
			if(!isChooseWeek){
				ShowUtil.showToast(instance, R.string.choose_week);
				return;
			}
			
			
			
			String parten = "00";
			DecimalFormat decimal = new DecimalFormat(parten);
			String text = decimal.format(wv_hour.getCurrentItem())
					+ ":"
					+ decimal.format(wv_minutes.getCurrentItem());
			//查询是否已经设置当前时间
			ArrayList<Alarm> alarmList = alarmDB.getAlarmListByTime(Alarm.TYPE_HULI, text);
			if(alarmList.size() > 0){
				ShowUtil.showToast(SetRemindHuliAddActivity.this, R.string.alarm_repeat);
				return;
			}
			
			Alarm alarm = new Alarm();
			for(int i=0; i<checkList.size(); i++){
				if(checkList.get(i).isChecked()){
							int day = 0;   //跟今日对比,多少天后开始提醒
							String yearMonthDay = DateUtil.getNowDate(DateUtil.YEAR_MONTH_DAY);
							if(i+1 < week){
								day = 7-(week-(i+1));
							}
							else if(i+1 == week){
								day =0;
							}
							else if(i+1 > week){
								day = i+1 - week;
							}
							
							alarm.type = Alarm.TYPE_HULI;
							alarm.alarmTime = DateUtil.getLongOfDayTimeAll(yearMonthDay, text) + day*DateUtil.DAY_MILLIS;
							alarm.setTime = DateUtil.getNowTimeInMillis();
							alarm.time = text;
							alarm.week = weekList[i];
							alarm.isRepeat = true;
							alarm.repeatTime = DateUtil.WEEK_MILLIS;
							Alarm.setRemind(alarmDB, alarm, alarmManager, instance);
				}
			}
			
			ShowUtil.showToast(instance, R.string.save_success);
			finish();
		}
	};
	
	public void setTimePicker(Date date) {
		int hour = 0;
		int minutes = 0;

		Calendar calendar = Calendar.getInstance();

		if (date != null)
			calendar.setTime(date);

		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minutes = calendar.get(Calendar.MINUTE);

		// 时
		wv_hour = (WheelView) findViewById(R.id.set_remind_huli_add_hour);
		wv_hour.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hour.setCyclic(true);// 可循环滚动
		wv_hour.setLabel("时");// 添加文字
		wv_hour.setCurrentItem(hour);// 初始化时显示的数据

		// 分
		wv_minutes = (WheelView)findViewById(R.id.set_remind_huli_add_minute);
		wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));
		wv_minutes.setCyclic(true);
		wv_minutes.setLabel("分");
		wv_minutes.setCurrentItem(minutes);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 40;

		wv_hour.TEXT_SIZE = textSize;
		wv_minutes.TEXT_SIZE = textSize;

	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
