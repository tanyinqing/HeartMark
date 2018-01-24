package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.R.color;
import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.YongYaoAddAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.database.YaoDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.model.Yao;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.widget.MyDialog;

public class YongYaoAddActivity extends BaseActivity implements 
    OnTopbarLeftButtonListener, OnTopbarRightButtonListener{
	private RelativeLayout addTypeLayout;
	public  TextView addType;
	private RelativeLayout addNameLayout;
	private TextView addName;
	private Button addAddTime;
	private Button addMinTime;
	private Button addAsk;
	private ListView addListView;
	
	private YongYaoAddAdapter addAdapter;
	public  ArrayList<String> timeList = new ArrayList<String>();
	public  boolean haveTime = false;
	private AlarmManager alarmManager;
	private YaoDB yaoDB;
	private AlarmDB alarmDB;
	public ArrayList<String> typeList = new ArrayList<String>();
	public ArrayList<String> nameList = new ArrayList<String>();
	public String yaoDetail = null;
	public String yaoRead = null;
	public String yaoId = null;
	public static YongYaoAddActivity  instance;
	public static int selectCount = 0;
	public ArrayList<CheckBox> checkList ;
	public int week;
	private String[] weekList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yongyao_add);
		instance = this;
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	private void init(){
		TopBarView topbar = (TopBarView) findViewById(R.id.yongyaoAddTopBar);
		topbar.setTopbarTitle(R.string.yongyao_add);
		topbar.setRightText("说明书");
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setOnTopbarRightButtonListener(this);
		
		week = DateUtil.getThisWeek();
		//返回0,说明今天为周日
		if(week == 0){
			week = 7;
		}
		weekList = getResources().getStringArray(R.array.week_list);
		yaoDB = new YaoDB(YongYaoAddActivity.this);
		alarmDB = new AlarmDB(YongYaoAddActivity.this);
		checkList = new ArrayList<CheckBox>();
		
		addTypeLayout = (RelativeLayout) findViewById(R.id.yongyao_add_type_layout);
		addType = (TextView) findViewById(R.id.yongyao_add_name);
		addNameLayout = (RelativeLayout) findViewById(R.id.yongyao_add_name_layout);
		addName = (TextView) findViewById(R.id.yongyao_add_usage);
		addAddTime = (Button) findViewById(R.id.yongyao_add_time_add);
		addMinTime = (Button) findViewById(R.id.yongyao_add_time_min);
		addAsk = (Button) findViewById(R.id.yongyao_add_ask);
		
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check1));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check2));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check3));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check4));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check5));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check6));
		checkList.add((CheckBox)findViewById(R.id.set_remind_yao_check7));
		
		addListView = (ListView) findViewById(R.id.yongyao_add_time);
		timeList.clear();
		timeList.add("");
		timeList.add("");
		timeList.add("");
		addAdapter = new YongYaoAddAdapter(YongYaoAddActivity.this, timeList);
		addListView.setAdapter(addAdapter);
		
		addTypeLayout.setOnClickListener(new MyViewOnclicklistener());
		addNameLayout.setOnClickListener(new MyViewOnclicklistener());
		addAddTime.setOnClickListener(new MyViewOnclicklistener());
		addMinTime.setOnClickListener(new MyViewOnclicklistener());
		addAsk.setOnClickListener(new MyViewOnclicklistener());
		
		alarmManager  = (AlarmManager) YongYaoAddActivity.this.getSystemService(Context.ALARM_SERVICE);
		
		typeList = yaoDB.getTypeList();
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
			final int rid = v.getId();
			switch (rid) {
			case R.id.yongyao_add_type_layout:
				if(typeList == null || typeList.size() == 0){
					ShowUtil.showToast(YongYaoAddActivity.this, R.string.no_data);
				}else{
					showTypeChoose(YongYaoAddActivity.this, typeList, "选择类型", addType);
				}
				break;
			case R.id.yongyao_add_name_layout:
				if(addType.getText().toString().equals("") ||
						addType.getText().toString() == null){
					ShowUtil.showToast(YongYaoAddActivity.this, R.string.choose_type);
				}else{
					showNameChoose(YongYaoAddActivity.this, nameList, "选择名字", addName);
				}
				break;
			case R.id.yongyao_add_time_add:
				if(timeList.size() < 5){
					timeList.add("");
					addAdapter.notifyDataSetChanged();
				}else{
					
				}
				
				break;
			case R.id.yongyao_add_time_min:
				if(timeList.size() > 1){
					timeList.remove(timeList.size() - 1);
					addAdapter.notifyDataSetChanged();
				}else{
					
				}
				
				break;
			case R.id.yongyao_add_ask:
				doSave();
				break;
			default:
				break;
			}
		}
	}
	
	//类型选择 弹出dialog
	public static void showTypeChoose(Context context, ArrayList<String> stringList,
			String title, final TextView textView) {
		final String[] stringArray = (String[])stringList.toArray(new String[stringList.size()]);
		selectCount = 0;
		MyDialog dialog = new MyDialog(context);
		dialog.setTitle(title);
		dialog.setSingleChoiceItems(stringArray, selectCount,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectCount = which;
					}
				});
		dialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		dialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String type = stringArray[selectCount];
				textView.setBackgroundColor(color.transparent);
				textView.setText(type);
				instance.addName.setText("");
				instance.nameList.clear();
				instance.nameList = instance.yaoDB.getNameListByType(type);
			}
		});
		dialog.create(null).show();

	}
	  
	//姓名选择   弹出dialog
	public static void showNameChoose(Context context, ArrayList<String> stringList,
			String title, final TextView textView) {
		final String[] stringArray = (String[])stringList.toArray(new String[stringList.size()]);
		selectCount = 0;
		MyDialog dialog = new MyDialog(context);
		dialog.setTitle(title);
		dialog.setSingleChoiceItems(stringArray, selectCount,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectCount = which;
					}
				});
		dialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		dialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = stringArray[selectCount];
				textView.setBackgroundColor(color.transparent);
				textView.setText(name);
				
				Yao yao = instance.yaoDB.getYaoListDetail(instance.addType.getText().toString(),name);
				instance.yaoDetail = yao.content;
				instance.yaoRead = yao.contentText;
				instance.yaoId = yao.yaoId;
			}
		});
		dialog.create(null).show();

	}
	
	public void doSave(){
		if(addType.getText().toString() == null || addType.getText().toString().equals("")){
			ShowUtil.showToast(instance, R.string.choose_type);
			return;
		}
		
		if(addName.getText().toString() == null || addName.getText().toString().equals("")){
			ShowUtil.showToast(instance, R.string.choose_name);
			return;
		}
		
		if( !haveTime){
			ShowUtil.showToast(instance, R.string.choose_time);
			return;
		}
		
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
		
		Alarm alarm = new Alarm();
		for(int i=0; i<checkList.size(); i++){
			if(checkList.get(i).isChecked()){//选中了星期几
				for(int k=0; k<timeList.size(); k++){//设置了时间的信息
					if(timeList.get(k) != null && !timeList.get(k).equals("")){
						int day = 0;//这个是时间的差值
						//获取现在的时间信息
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
						
						alarm.type = Alarm.TYPE_YAO;
						// 闹钟响起的时间的毫秒值
						alarm.alarmTime = DateUtil.getLongOfDayTimeAll(yearMonthDay,timeList.get(k)) + day*DateUtil.DAY_MILLIS;
						alarm.setTime = DateUtil.getNowTimeInMillis();
						alarm.time = timeList.get(k);
						alarm.week = weekList[i];
						alarm.isRepeat = true;
						alarm.repeatTime = DateUtil.WEEK_MILLIS;
						alarm.yaoName = addName.getText().toString();
						alarm.yaoType = addType.getText().toString();
						Alarm.setRemind(alarmDB, alarm, alarmManager, instance);
					}
				}
			}
		}
		
		ShowUtil.showToast(instance, R.string.save_success);
		finish();
	}
	
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		if(addType.getText().toString() == null || addType.getText().toString().equals("")){
			ShowUtil.showToast(instance, R.string.choose_type);
			return;
		}
		
		if(addName.getText().toString() == null || addName.getText().toString().equals("")){
			ShowUtil.showToast(instance, R.string.choose_name);
			return;
		}
		ZiXun zixun = new ZiXun();
		zixun.newId = yaoId;
		zixun.type = ZiXun.TYPE_EXPLAIN;
		Intent detailIntent = new Intent(YongYaoAddActivity.this, ZiXunDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("newItem", zixun);
		detailIntent.putExtras(bundle);
		startActivity(detailIntent);
	}
}
