package com.yikang.heartmark.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.SetRemindCelingAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.CalendarView;
import com.yikang.heartmark.view.CalendarView.OnItemClickListener;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.wheel.WheelView;
import com.yikang.heartmark.widget.MyDialog;

public class SetRemindCelingActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private CalendarView calendar;   //日历的控件
	
	private ImageButton calendarLeft;
	private TextView calendarCenter;   //显示年月的控件
	private ImageButton calendarRight;
	 private ListView listView;	
	private ImageView imageview;  
	
	private String calendarDate; //选中的date
	private SimpleDateFormat format;   
    private AlarmDB alarmDB;
    private AlarmManager alarmManager;
    public static SetRemindCelingActivity instance;  //本文的单例模式
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_remind_celing);
		instance = this;
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	@SuppressLint("SimpleDateFormat")
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setRemindCelingTopBar);
		topbar.setTopbarTitle(R.string.remind_celing);
		topbar.setOnTopbarLeftButtonListener(this);

		alarmDB = new AlarmDB(SetRemindCelingActivity.this);
		alarmManager = (AlarmManager) SetRemindCelingActivity.this.getSystemService(Context.ALARM_SERVICE);
		format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取日历控件对象
		calendar = (CalendarView) findViewById(R.id.calendar);
		calendar.setSelectMore(false); // 单选

		calendarLeft = (ImageButton) findViewById(R.id.calendarLeft);
		calendarCenter = (TextView) findViewById(R.id.calendarCenter);
		calendarRight = (ImageButton) findViewById(R.id.calendarRight);
		listView = (ListView) findViewById(R.id.setRemindCelingListView);
		imageview = (ImageView) findViewById(R.id.setRemindCelingimageview);

		try {
			// 设置日历日期
			Date date = format.parse(DateUtil.getNowDate(DateUtil.YEAR_MONTH_DAY));
			calendar.setCalendarData(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 获取日历中年月 ya[0]为年，ya[1]为月
		String[] ya = calendar.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		
		//注册监听器
		calendarLeft.setOnClickListener(new MyViewOnclicklistener());
		calendarRight.setOnClickListener(new MyViewOnclicklistener());
		calendar.setOnItemClickListener(itemListener);
		
		refreshView();
	}
	
	// 左右月 监听
	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.calendarLeft:
				// 点击上一月 同样返回年月
				String leftYearAndmonth = calendar.clickLeftMonth();
				String[] top = leftYearAndmonth.split("-");
				calendarCenter.setText(top[0] + "年" + top[1] + "月");
				break;
			case R.id.calendarRight:
				// 点击下一月
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] bottom = rightYearAndmonth.split("-");
				calendarCenter.setText(bottom[0] + "年" + bottom[1] + "月");
				break;

			default:
				break;
			}
		}
	}

	//对点击事件的监听器
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void OnItemClick(Date selectedStartDate, Date selectedEndDate,
				Date downDate) {
			if(calendar.isSelectMore()){
				//选择时间段
				//MyToast.show(getApplicationContext(), format.format(selectedStartDate)+"到"+format.format(selectedEndDate), Toast.LENGTH_SHORT);
			}else{
				//MyToast.show(getApplicationContext(), format.format(downDate), Toast.LENGTH_SHORT);
				calendarDate = format.format(downDate);
				showCheckTime();
			}
		}

	};

	//选择提醒的时间
	public void showCheckTime(){
		String	dateStr = DateUtil.getNowDate(DateUtil.DATE_HOUR_MINUTE);
		Date date = DateUtil.dateFromString(dateStr, DateUtil.DATE_HOUR_MINUTE);
		MyDialog dialog = new MyDialog(SetRemindCelingActivity.this).setTitle("请选择时间")
				.setTimePicker(date).setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						WheelView wv_hour = (WheelView) v
								.findViewById(R.id.hour);
						WheelView wv_minute = (WheelView) v
								.findViewById(R.id.minute);
						String parten = "00";
						DecimalFormat decimal = new DecimalFormat(parten);
						String text = decimal.format(wv_hour.getCurrentItem())
								+ ":"
								+ decimal.format(wv_minute.getCurrentItem());
						
						Alarm alarm = new Alarm();
						alarm.type = Alarm.TYPE_CELING;
						long alarmTime = DateUtil.getLongOfDayTimeAll(calendarDate, text);
						if(alarmTime < DateUtil.getNowTimeInMillis()){
							ShowUtil.showToast(SetRemindCelingActivity.this, R.string.time_late);
							return;
						}else{
							alarm.alarmTime = alarmTime;
						}
						alarm.time = calendarDate + "  "+text;
						//查询是否已经设置当前时间
						ArrayList<Alarm> alarmList = alarmDB.getAlarmListByTime(Alarm.TYPE_CELING, alarm.time);
						if(alarmList.size() > 0){
							ShowUtil.showToast(SetRemindCelingActivity.this, R.string.alarm_repeat);
							return;
						}
						alarm.setTime = DateUtil.getNowTimeInMillis();
						alarm.isRepeat = false;
						Alarm.setRemind(alarmDB, alarm, alarmManager, SetRemindCelingActivity.this);
						
						refreshView();
					}
				});
		dialog.create(null).show();
	}
	
	public void refreshView(){
		ArrayList<String> stringList = new ArrayList<String>();
		ArrayList<Alarm> alarmList = alarmDB.getAlarmListByType(Alarm.TYPE_CELING);
		if(alarmList.size()<=0){
			listView.setVisibility(View.GONE);
			imageview.setVisibility(View.VISIBLE);
		}else{
			imageview.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
		
		for(int i=0; i<alarmList.size(); i++){
			stringList.add(alarmList.get(i).time);
		}
		SetRemindCelingAdapter adapter = new SetRemindCelingAdapter(SetRemindCelingActivity.this, stringList);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
