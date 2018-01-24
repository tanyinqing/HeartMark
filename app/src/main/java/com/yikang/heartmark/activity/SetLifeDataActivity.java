package com.yikang.heartmark.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.CeLingDB;
import com.yikang.heartmark.database.HelpHeartDB;
import com.yikang.heartmark.database.HelpWaterDB;
import com.yikang.heartmark.database.HuLiWeightDB;
import com.yikang.heartmark.model.CeLingData;
import com.yikang.heartmark.model.HelpHeart;
import com.yikang.heartmark.model.HelpWater;
import com.yikang.heartmark.model.HuLiWeight;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.CalendarView;
import com.yikang.heartmark.view.CalendarView.OnItemClickListener;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.wheel.WheelView;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;
/**
* <p>Title: SetLifeDataActivity</p>
* <p>Description:生活日志 </p>
* @author lizhengjun
* @Email: lizhengjunkijojo@163.com
* @date   2015-3-6
 */
@SuppressLint("SimpleDateFormat")
public class SetLifeDataActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener {
	
	private SimpleDateFormat format;
	private String calendarDate; //选中的date
	@ViewInject(R.id.calendarLeft)
	private ImageButton calendarLeft;
	@ViewInject(R.id.calendarCenter)
	private TextView calendarCenter;
	@ViewInject(R.id.calendarRight)
	private ImageButton calendarRight;
	@ViewInject(R.id.calendar)
	private CalendarView calendar;
	
	@ViewInject(R.id.set_life_weight_data)
	private TextView weightData;
	@ViewInject(R.id.set_life_heart_data)
	private TextView heartData;
	@ViewInject(R.id.set_life_water_in_data)
	private TextView waterInData;
	@ViewInject(R.id.set_life_water_out_data)
	private TextView waterOutData;
	@ViewInject(R.id.set_life_bnp_data)
	private TextView bnpData;
	private int DrawableString = R.drawable.set_other;
	private Date thisDownDate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_life_data);
		init();
	}
	private String uid;
	private HuLiWeightDB weightDB;
	private HelpHeartDB heartDB;
	private HelpWaterDB waterDB;
	private CeLingDB bnpDB;
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setLifeBarView);
		topbar.setTopbarTitle(R.string.set_life_data);
		topbar.setRightText("详情");
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setOnTopbarRightButtonListener(this);
		ViewUtils.inject(this);
		
		uid = ConstantUtil.getUid(this);
		weightDB = new HuLiWeightDB(SetLifeDataActivity.this);
		heartDB = new HelpHeartDB(SetLifeDataActivity.this);
		waterDB = new HelpWaterDB(SetLifeDataActivity.this);
		bnpDB = new CeLingDB(SetLifeDataActivity.this);
		
		format = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setSelectMore(false); // 单选
		calendarDate = format.format(new Date());
		showData(calendarDate);
		try {
			// 设置日历日期
			Date date = format.parse(DateUtil.getNowDate(DateUtil.YEAR_MONTH_DAY));
			calendar.setCalendarData(date);
			thisDownDate = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 获取日历中年月 ya[0]为年，ya[1]为月
		String[] ya = calendar.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		
		//设置监听
		calendarLeft.setOnClickListener(new MyViewOnclicklistener());
		calendarRight.setOnClickListener(new MyViewOnclicklistener());
		calendar.setOnItemClickListener(itemListener);
		
		weightData.setOnClickListener(new MyViewOnclicklistener());
		heartData.setOnClickListener(new MyViewOnclicklistener());
		waterInData.setOnClickListener(new MyViewOnclicklistener());
		waterOutData.setOnClickListener(new MyViewOnclicklistener());
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
			case R.id.set_life_weight_data:
				setLifeWeightValue("请选择体重",weightData);
				break;
			case R.id.set_life_heart_data:
				setLifeHeartValue("请选择心率",heartData);
				break;
			case R.id.set_life_water_in_data:
				setLifeWaterInValue("请选择入水总量",waterInData);
				break;
			case R.id.set_life_water_out_data:
				setLifeWaterOutValue("请选择出水总量",waterOutData);
				break;
			default:
				break;
			}
		}
	}
	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void OnItemClick(Date selectedStartDate, Date selectedEndDate,Date downDate) {
			if(calendar.isSelectMore()){
				MyToast.show(SetLifeDataActivity.this,123+"", Toast.LENGTH_SHORT);
			}else{
				calendarDate = format.format(downDate);
				//通过string yyyy-mm-dd 时间获取时间码
				long calendarTime = DateUtil.getLongOfDayTime(calendarDate);
				//设置时间不能晚于当前时间
				if(calendarTime > DateUtil.getNowTimeInMillis()){
					ShowUtil.showToast(SetLifeDataActivity.this, R.string.time_early);
					calendar.setCalendarData(thisDownDate);
					return;
				}
				thisDownDate = downDate;
				showData(calendarDate);
			}
		}
	};
	
	/**
	 * 获取数据显示
	* <p>Title: showData</p>
	* <p>Description: </p>
	* @param calendarDate
	 */
	private void showData(String calendarDate) {
		if(!TextUtils.isEmpty(calendarDate)){
			ArrayList<HuLiWeight> weightList = weightDB.getHuliListByDate(calendarDate, uid);
			ArrayList<HelpHeart> heartList = heartDB.getListByDate(calendarDate, uid);
			ArrayList<HelpWater> waterList = waterDB.getHuliListByDate(calendarDate, uid);
			ArrayList<CeLingData> bnpList = bnpDB.getListByDate(calendarDate, uid);
			if (weightList.size() != 0) {
				weightData.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				int weightValue = weightList.get(0).thisWeight;
				weightData.setText((weightValue/10)+"."+(weightValue%10)+" kg");
			}else{
				weightData.setText("");
				weightData.setBackgroundResource(DrawableString);
			}
			if (heartList.size() != 0) {
				heartData.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				heartData.setText(heartList.get(0).heart+" bpm");
			} else {
				heartData.setText("");
				heartData.setBackgroundResource(DrawableString);
			}
			if (waterList.size() != 0) {
				if(waterList.get(0).inWater != 0){
					waterInData.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
					waterInData.setText(waterList.get(0).inWater+" ml");
				}else{
					waterInData.setText("");
					waterInData.setBackgroundResource(DrawableString);
				}
				if(waterList.get(0).outWater!=0){
					waterOutData.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
					waterOutData.setText(waterList.get(0).outWater+" ml");
				}else{
					waterOutData.setBackgroundResource(DrawableString);
					waterOutData.setText("");
				}
			} else {
				waterInData.setText("");
				waterOutData.setText("");
				waterInData.setBackgroundResource(DrawableString);
				waterOutData.setBackgroundResource(DrawableString);
			}
			if (bnpList.size() != 0) {
				bnpData.setText(bnpList.get(0).result);
				bnpData.setTextColor(getResources().getColor(R.color.typeface));
			} else {
				bnpData.setText("当日无BNP值");
				bnpData.setTextColor(Color.rgb(214, 214, 214));
			}
		}
	}
/*	
	private int weightValue = 0;
	private HuLiWeight weight = null;
	private void setLifeWeightValue(String title, final TextView tv) {
		String uSER_WEIGHT = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_WEIGHT);
		int indexWeight = Integer.valueOf(uSER_WEIGHT);
		MyDialog myDialog = new MyDialog(this);
		myDialog.setSimpleSelectPicker(10, indexWeight - 10, indexWeight + 10, "  kg");
		myDialog.setTitle(title);
		myDialog.setNegativeButton(R.string.cancel,null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v.findViewById(R.id.simpleSelectPicker);
				weightValue = wv_content.getCurrentItem();
				tv.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				tv.setText(weightValue+" kg");
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("nurseDate", calendarDate);
				params.put("weight", String.valueOf(weightValue));
				ConnectionManager.getInstance().send("FN11060WD00","saveNurseInfo", params,"setLifeWeight",
						SetLifeDataActivity.this);
				
			    weight = new HuLiWeight();
				weight.uid = uid;
				weight.sync = 0;
				weight.date = calendarDate;
				weight.dateMonth = calendarDate.substring(0, 7);
				weight.day = calendarDate.substring(8, 10);
				weight.timeMill = DateUtil.getLongOfDayTime(calendarDate+"");
				Integer baseWeight = Integer.valueOf(SharedPreferenceUtil.getString(SetLifeDataActivity.this, ConstantUtil.USER_WEIGHT));
				weight.baseWeight = baseWeight;
				weight.thisWeight = weightValue;
				weight.diff = Math.abs((weightValue) - baseWeight);
				ArrayList<HuLiWeight> weightList = weightDB.getHuliListByDate(calendarDate, uid);
				if (weightList.size() != 0) {
					weightDB.updata(weight, uid);
				} else {
					weightDB.insert(weight);
				}
			}
		});
		myDialog.create(null).show();
	}*/
	private int weightValue = 0;
	private HuLiWeight weight = null;
	private void setLifeWeightValue(String title, final TextView tv) {
		String uSER_WEIGHT = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_WEIGHT);
		final int indexWeight = Integer.valueOf(uSER_WEIGHT);
		MyDialog myDialog = new MyDialog(this);
		myDialog.setTwoWheel(null,indexWeight - 10, indexWeight + 10,1,10,"  .",null,0,9,1,0,"  kg");
		myDialog.setTitle(title);
		myDialog.setNegativeButton(R.string.cancel,null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WheelView wv_content01 = (WheelView) v.findViewById(R.id.oneWheel);
				int weightValue01 = wv_content01.getCurrentItem();
				WheelView wv_content02 = (WheelView) v.findViewById(R.id.twoWheel);
				int weightValue02 = wv_content02.getCurrentItem();
				weightValue = (indexWeight-10+weightValue01)*10+weightValue02;
				tv.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				tv.setText((weightValue/10)+"."+(weightValue%10)+" kg");
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("nurseDate", calendarDate);
				params.put("weight", String.valueOf((weightValue/10)+"."+(weightValue%10)));
				ConnectionManager.getInstance().send("FN11060WD00","saveNurseInfo", params,"setLifeWeight",
						SetLifeDataActivity.this);
				
			    weight = new HuLiWeight();
				weight.uid = uid;
				weight.sync = 0;
				weight.date = calendarDate;
				weight.dateMonth = calendarDate.substring(0, 7);
				weight.day = calendarDate.substring(8, 10);
				weight.timeMill = DateUtil.getLongOfDayTime(calendarDate+"");
				Integer baseWeight = Integer.valueOf(SharedPreferenceUtil.getString(SetLifeDataActivity.this, ConstantUtil.USER_WEIGHT));
				weight.baseWeight = baseWeight;
				weight.thisWeight = weightValue;
				weight.diff = Math.abs((weightValue) - baseWeight);
				ArrayList<HuLiWeight> weightList = weightDB.getHuliListByDate(calendarDate, uid);
				if (weightList.size() != 0) {
					weightDB.updata(weight, uid);
				} else {
					weightDB.insert(weight);
				}
			}
		});
		myDialog.create(null).show();
	}
	@SuppressWarnings({ "rawtypes" })
	public void setLifeWeight(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(SetLifeDataActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				weight.sync = 1;
				weightDB.updata(weight, uid);
			}
		}
	}
	
	private int heartValue = 0;
	private HelpHeart heart = null;
	private void setLifeHeartValue(String title, final TextView tv) {
		MyDialog myDialog = new MyDialog(this);
		String weightDefault = "79bpm";
		if (weightDefault.length() > 0) {
			heartValue = Integer.valueOf(weightDefault.substring(0,weightDefault.length() - 3));
		}
		myDialog.setSimpleSelectPicker(heartValue, 1, 200, "bpm");
		myDialog.setTitle(title);
		myDialog.setNegativeButton(R.string.cancel,null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v.findViewById(R.id.simpleSelectPicker);
				heartValue = wv_content.getCurrentItem() + 1;
				tv.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				tv.setText(heartValue+" bpm");
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("nurseDate", calendarDate);
				params.put("heartRate", String.valueOf(heartValue));
				ConnectionManager.getInstance().send("FN11060WD00","saveNurseInfo", params,"setLifeHeart",
						SetLifeDataActivity.this);
				
			    heart = new HelpHeart();
				heart.uid = uid;
				heart.sync = 0;
				heart.date = calendarDate;
				heart.dateMonth = calendarDate.substring(0, 7);
				heart.day = calendarDate.substring(8, 10);
				heart.timeMill = DateUtil.getLongOfDayTime(calendarDate+"");
				heart.heart = heartValue;
				ArrayList<HelpHeart> heartList = heartDB.getListByDate(calendarDate, uid);
				if (heartList.size() != 0) {
					heartDB.updata(heart ,uid);
				} else {
					heartDB.insert(heart);
				}
			}
		});
		myDialog.create(null).show();
	}
	@SuppressWarnings({ "rawtypes" })
	public void setLifeHeart(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(SetLifeDataActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				heart.sync = 1;
				heartDB.updata(heart ,uid);
			}
		}
	}
	
	private int waterinValue = 0;
	private HelpWater waterIn = null;
	private void setLifeWaterInValue(String title, final TextView tv) {
		MyDialog myDialog = new MyDialog(this);
		myDialog.setSimpleSelectPickerData(0, 30, 100, 10, "ml");
		myDialog.setTitle(title);
		myDialog.setNegativeButton(R.string.cancel,null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v.findViewById(R.id.simpleSelectPicker);
				waterinValue = wv_content.getCurrentItem() * 100;
				tv.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				tv.setText(waterinValue+" ml");
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("nurseDate", calendarDate);
				params.put("inWater", String.valueOf(waterinValue));
				ConnectionManager.getInstance().send("FN11060WD00","saveNurseInfo", params,"setLifeWaterIn",SetLifeDataActivity.this);
				
				waterIn = new HelpWater();
				waterIn.uid = uid;
				waterIn.sync = 0;
				waterIn.date = calendarDate;
				waterIn.dateMonth = calendarDate.substring(0, 7);
				waterIn.day = calendarDate.substring(8, 10);
				waterIn.timeMill = DateUtil.getLongOfDayTime(calendarDate+"");
				waterIn.inWater = waterinValue;
				ArrayList<HelpWater> waterinList = waterDB.getHuliListByDate(calendarDate, uid);
				if (waterinList.size()!=0) {
					waterIn = waterinList.get(0);
					waterIn.inWater = waterinValue;
					waterDB.updata(waterIn, uid);
				} else {
					waterIn.outWater = 0;
					waterDB.insert(waterIn);
				}
			}
		});
		myDialog.create(null).show();
	}
	@SuppressWarnings({ "rawtypes" })
	public void setLifeWaterIn(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(SetLifeDataActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				waterIn.sync = 1;
				waterDB.updata(waterIn, uid);
			}
		}
	}
	
	private int wateroutValue = 0;
	private HelpWater waterOut = null;
	private void setLifeWaterOutValue(String title, final TextView tv) {
		MyDialog myDialog = new MyDialog(this);
		myDialog.setSimpleSelectPickerData(0, 30, 100, 10, "ml");
		myDialog.setTitle(title);
		myDialog.setNegativeButton(R.string.cancel,null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v.findViewById(R.id.simpleSelectPicker);
				wateroutValue = wv_content.getCurrentItem() * 100;
				tv.setBackgroundColor(SetLifeDataActivity.this.getResources().getColor(R.color.new_gray));
				tv.setText(wateroutValue+" ml");
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("nurseDate", calendarDate);
				params.put("outWater", String.valueOf(wateroutValue));
				ConnectionManager.getInstance().send("FN11060WD00","saveNurseInfo", params,"setLifeWaterOut",SetLifeDataActivity.this);
				
				waterOut = new HelpWater();
				waterOut.uid = uid;
				waterOut.sync = 0;
				waterOut.date = calendarDate;
				waterOut.dateMonth = calendarDate.substring(0, 7);
				waterOut.day = calendarDate.substring(8, 10);
				waterOut.timeMill = DateUtil.getLongOfDayTime(calendarDate+"");
				waterOut.outWater = wateroutValue;
				ArrayList<HelpWater> wateroutList = waterDB.getHuliListByDate(calendarDate, uid);
				if (wateroutList.size()!=0) {
					waterOut = wateroutList.get(0);
					waterOut.outWater = wateroutValue;
					waterDB.updata(waterOut, uid);
				} else {
					waterOut.inWater = 0;
					waterDB.insert(waterOut);
				}
			}
		});
		myDialog.create(null).show();
	}
	@SuppressWarnings({ "rawtypes" })
	public void setLifeWaterOut(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(SetLifeDataActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				waterOut.sync = 1;
				waterDB.updata(waterOut, uid);
			}
		}
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
	@Override
	public void onTopbarRightButtonSelected() {
		if(ConnectUtil.isLogin(this)){
			Intent helperIntent = new Intent(this, MainHelpActivity.class);
			startActivity(helperIntent);
		}else{
			Intent helperIntent = new Intent(this, LoginActivity.class);
			startActivity(helperIntent);
		}
	}
}
