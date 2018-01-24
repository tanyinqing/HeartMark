package com.yikang.heartmark.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import cn.sharesdk.ceshi.PromptManager;

import com.example.heartmark.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yikang.heartmark.adapter.NetViewPagerAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.controller.XmppActivity;
import com.yikang.heartmark.controller.album.AlbumHelper;
import com.yikang.heartmark.database.CeLingDB;
import com.yikang.heartmark.database.HelpHeartDB;
import com.yikang.heartmark.database.HelpWaterDB;
import com.yikang.heartmark.database.HuLiWeightDB;
import com.yikang.heartmark.database.ImageDB;
import com.yikang.heartmark.database.YaoDB;
import com.yikang.heartmark.model.CeLingData;
import com.yikang.heartmark.model.HelpHeart;
import com.yikang.heartmark.model.HelpWater;
import com.yikang.heartmark.model.HuLiWeight;
import com.yikang.heartmark.model.Yao;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.MyPoint;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;
import com.yuzhi.framework.util.JsonUtil;

@SuppressLint({ "NewApi", "HandlerLeak" })
@SuppressWarnings("unused")

public class MyMainActivity extends BaseActivity implements 
    OnTopbarRightButtonListener{
	private ArrayList<ZiXun> pagerList;
	private ViewPager mainViewPager;
	private LinearLayout pointLayout;
	private ImageView point;
	private NetViewPagerAdapter mainViewPagerAdapter;
	private FrameLayout pagerLayout;
	private BluetoothAdapter bluetoothAdapter;

	private GestureDetector mGestureDetector; // 手势识别
	private boolean isThreadState = true;
	public boolean isThreadRun = true;
	public ArrayList<MyPoint> pointList;
	private int currentnum = 0;
	
	private String uid;
	private YaoDB yaoDB;
	private CeLingDB ceLingDB;//提醒的数据库表
	private HuLiWeightDB weightDB;
	private HelpWaterDB waterDB;
	private HelpHeartDB heartDB;
	private ImageDB imageDB;
	private String[] states = null;
	
	//版本更新
	private String version;
	private String content;
	private String versionUrl;
	private final int MSG_SHOWUPGRADE = 11;
	private final int MSG_DOAWNLOAD = 12;
	 
    public MyMainActivity instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymain);
		instance = this;
		// PromptManager.showDialogTest(this,"MyMainActivity这个是主页的MyViewOnclicklistener方法");
		//PromptManager.showDialogTest(this,"MyMainActivity这个是主页的MyViewOnclicklistener方法");
		init();
	}

	@Override
	public void onResume() {
		isThreadRun = true;
		super.onResume();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public void onPause() {
		isThreadState = true;
		isThreadRun = false;
		instance = null;
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		TimerStop();
		//如果蓝牙开启,退出时关闭蓝牙
		if(bluetoothAdapter.isEnabled()){
			bluetoothAdapter.disable();
		}
	}

	@SuppressWarnings("deprecation")
	private void init() {
        // todo 开始加载相册服务
        AlbumHelper.getHelper();

		TopBarView topbar = (TopBarView) findViewById(R.id.main_TopBar);
		topbar.setTopbarTitle(R.string.main_title);
		topbar.setLeftButtonGone();
		topbar.setRightButton(R.drawable.remind_white_img);
		topbar.setOnTopbarRightButtonListener(this);
		
		uid = ConstantUtil.getUid(MyMainActivity.this);
		yaoDB = new YaoDB(MyMainActivity.this);
		ceLingDB = new CeLingDB(MyMainActivity.this);
		weightDB = new HuLiWeightDB(MyMainActivity.this);
		waterDB = new HelpWaterDB(MyMainActivity.this);
		heartDB = new HelpHeartDB(MyMainActivity.this);
		imageDB = new ImageDB(MyMainActivity.this);
		
		//pagerLayout = (FrameLayout) findViewById(R.id.main_pagerlayout);
		
		mainViewPager = (ViewPager) findViewById(R.id.main_viewpager);
		mainViewPager.setOnPageChangeListener(new MyPageChangeListener());
		//监听到viewpager的点击事件，停止滑动。(需求点击跳转详情，这里在adaper中设置监听)
		//mainViewPager.setOnTouchListener(onTouchListener);

		pointLayout = (LinearLayout) findViewById(R.id.main_pointlayout);
		pagerList = new ArrayList<ZiXun>();
		//mGestureDetector = new GestureDetector(this, new MyGestureDetector());
		states = getResources().getStringArray(R.array.celing_state);
		
		LinearLayout celingLayout = (LinearLayout) findViewById(R.id.main_celing);
		LinearLayout pingguLayout = (LinearLayout) findViewById(R.id.main_pinggu);
		LinearLayout zixunLayout = (LinearLayout) findViewById(R.id.main_zixun);
		LinearLayout phoneLayout = (LinearLayout) findViewById(R.id.main_phone);
		LinearLayout baikeLayout = (LinearLayout) findViewById(R.id.main_baike);
		LinearLayout helperLayout = (LinearLayout) findViewById(R.id.main_helper);
		LinearLayout centerLayout = (LinearLayout) findViewById(R.id.main_center);
		
		celingLayout.setOnClickListener(new MyViewOnclicklistener());
		pingguLayout.setOnClickListener(new MyViewOnclicklistener());
		zixunLayout.setOnClickListener(new MyViewOnclicklistener());
		phoneLayout.setOnClickListener(new MyViewOnclicklistener());
		baikeLayout.setOnClickListener(new MyViewOnclicklistener());
		helperLayout.setOnClickListener(new MyViewOnclicklistener());
		centerLayout.setOnClickListener(new MyViewOnclicklistener());

		//按照比例设置轮播图的宽高
		WindowManager wm = (WindowManager) MyMainActivity.this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = width;
		params.height = width * 19 / 32;
		mainViewPager.setLayoutParams(params);
		
		if(SharedPreferenceUtil.getBoolean(MyMainActivity.this, ConstantUtil.SHOW_UPDATE)){
			sync();
		}else{
			pagerList = imageDB.getImageList();
			mainViewPagerAdapter = new NetViewPagerAdapter(MyMainActivity.this,pagerList);
			mainViewPager.setAdapter(mainViewPagerAdapter);
			setPoint(0);
			TimerStart();
		}
		
	}

	private void sync(){
		//更新版本   ---0号请求---
		Map<String, String> param = new HashMap<String, String>();
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("CM01080WD00",
					"queryAndroidLastVersion", param,
					"getVersionSucessCallBack", this);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
		
		//是否登录
		ConnectionManager.getInstance().send("FN01060WL00", "validateUserLoginStatus", param, "userLoginSucessCallBack", this);
		
		//轮播图请求    ---1号请求---
		ConnectionManager.getInstance().send("FN06040WD00","queryAdvertisementInfo", param, "pagerSucessCallBack",MyMainActivity.this);
		
		//药品详情   ---2号请求 ---
		ConnectionManager.getInstance().send("CM01090WD00", "queryDrugInfo", param, "yaoSucessCallBack", this);
		
		//同步测量数据  ---3号请求 ---
		ArrayList<CeLingData>  celingList = ceLingDB.getCeLingListByNoSync(uid);
		if(celingList.size() > 0){
			List<Map<String, String>> celingUploadList = new ArrayList<Map<String, String>>();
			for(int i=0; i<celingList.size(); i++){
				Map<String,String> celingParam = new HashMap<String,String>();
				celingParam.put("MONITOR_RESULT", celingList.get(i).result);
				celingParam.put("MONITOR_TYPE_NAME", celingList.get(i).type);
				celingParam.put("CREATE_TIME", celingList.get(i).date+" "+celingList.get(i).time);
				celingParam.put("DIAGNOIS", celingList.get(i).diag);
				celingParam.put("MONITOR_TYPE", "0001");
				
				JSONArray jsonArray = new JSONArray();
				String state[] = celingList.get(i).state.split(",");
				for(int k=0; k<state.length; k++){
					JSONObject json = new JSONObject();
					for(int j=0; j<states.length; j++){
						if(state[k].equals(states[j])){
							try {
								json.put("CODE", "000" + (j + 1));
								jsonArray.put(json);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				celingParam.put("MONITOR_STATUS", jsonArray.toString());
				celingUploadList.add(celingParam);
			}
			
			Map<String, String> celingParams = new HashMap<String, String>();
			String uploadString = JsonUtil.convertObjectToJson(celingUploadList);
			celingParams.put("monitorDataList", uploadString);
			ConnectionManager.getInstance().send("FN06060WD00", "saveMonitorDataList", celingParams, "celingSucessCallBack", this);
		}
		
		//同步体重  ---4号请求---
		ArrayList<HuLiWeight>  weightList = weightDB.getListByNoSync(uid);
		if(weightList.size() > 0){
			List<Map<String, String>> weightUploadList = new ArrayList<Map<String, String>>();
			for(int i=0; i<weightList.size(); i++){
				Map<String,String> weightParam = new HashMap<String,String>();
				weightParam.put("WEIGHT", String.valueOf((weightList.get(i).thisWeight/10)+"."+(weightList.get(i).thisWeight%10)));
				weightParam.put("NURSE_DATE", weightList.get(i).date+ " " +weightList.get(i).time);
				weightUploadList.add(weightParam);
			}
			Map<String, String> weightParams = new HashMap<String, String>();
			String uploadString = JsonUtil.convertObjectToJson(weightUploadList);
			weightParams.put("nurseData", uploadString);
			ConnectionManager.getInstance().send("FN11060WD00", "saveNurseInfoByList", weightParams, "weightSucessCallBack", this);
			
		}
		
		//同步水量  ---5号请求---
		ArrayList<HelpWater>  waterList = waterDB.getListByNoSync(uid);
		if(waterList.size() > 0){
			List<Map<String, String>> waterUploadList = new ArrayList<Map<String, String>>();
			for(int i=0; i<waterList.size(); i++){
				Map<String,String> weightParam = new HashMap<String,String>();
				if(waterList.get(i).inWater!=0){
					weightParam.put("IN_WATER", String.valueOf(waterList.get(i).inWater));
				}
				if(waterList.get(i).outWater!=0){
					weightParam.put("OUT_WATER", String.valueOf(waterList.get(i).outWater));
				}
				weightParam.put("NURSE_DATE", waterList.get(i).date+ " " +waterList.get(i).time);
				waterUploadList.add(weightParam);
			}
			Map<String, String> weightParams = new HashMap<String, String>();
			String uploadString = JsonUtil.convertObjectToJson(waterUploadList);
			weightParams.put("nurseData", uploadString);
			ConnectionManager.getInstance().send("FN11060WD00", "saveNurseInfoByList", weightParams, "waterSucessCallBack", this);
		}
		
		//同步心率  ---6号请求---
		//从数据库查询到的没有同步的数据
		ArrayList<HelpHeart>  heartList = heartDB.getListByNoSync(uid);
		if(heartList.size() > 0){
			//把数据整理成键值对，放到链表中
			List<Map<String, String>> heartUploadList = new ArrayList<Map<String, String>>();
			for(int i=0; i<heartList.size(); i++){
				Map<String,String> heartParam = new HashMap<String,String>();
				heartParam.put("HEART_RATE", String.valueOf(heartList.get(i).heart));
				heartParam.put("NURSE_DATE", heartList.get(i).date+ " " +heartList.get(i).time);
				heartUploadList.add(heartParam);
			}
			// 这个是上传携带的参数
			Map<String, String> weightParams = new HashMap<String, String>();
			String uploadString = JsonUtil.convertObjectToJson(heartUploadList);
			weightParams.put("nurseData", uploadString);
			// heartSucessCallBack 这个是回调方法
			ConnectionManager.getInstance().send("FN11060WD00", "saveNurseInfoByList", weightParams, "heartSucessCallBack", this);
		}
	}
	
	// Version  ---0号---回调
		@SuppressWarnings("rawtypes")
		public void getVersionSucessCallBack(Object data) {
			Map resultMap = (Map) data;
			if (resultMap == null) {
				ShowUtil.showToast(MyMainActivity.this,R.string.check_network_timeout);
			} else {
				version = (String) resultMap.get("VERSION_NO");
				content = (String) resultMap.get("CONTENT");
				content = content.replace(";", ";\n");
				versionUrl = (String) resultMap.get("DOWN_URL");
				// 无需更新
				String v = getVersionName();
				if (version.equals(v)) {
					//ShowUtil.showToast(MyMainActivity.this, R.string.version_new);
				} else {
					Message message = Message.obtain();
					message.what = MSG_SHOWUPGRADE;
					handler.sendMessage(message);
				}
			}

		}
		
		// 是否登录---回调
		@SuppressWarnings("rawtypes")
		public void userLoginSucessCallBack(Object data) {
			Map resultMap = (Map) data;
			if (resultMap == null) {
				ShowUtil.showToast(MyMainActivity.this,R.string.check_network_timeout);
			} else {
				String result = (String) resultMap.get("resultCode");
				if (!result.equals("01001")) {
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_NAME, "");
					SharedPreferenceUtil.setString(MyMainActivity.this, ConstantUtil.USER_UID, "");
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_TOKEN, "");
					
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_NAME_info, "");
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_SEX, "");
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_AGE, "0");
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_WEIGHT, "");
					SharedPreferenceUtil.setString(MyMainActivity.this,ConstantUtil.USER_NUMBER, "");
					
					// 保存登录标志
					SharedPreferenceUtil.setBoolean(MyMainActivity.this,ConstantUtil.LOGIN, false);
				} 
			}

		}
	
	// 轮播图   ---1号回调--- 回调的数据是键值对
	@SuppressWarnings("rawtypes")
	public void pagerSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MyMainActivity.this,R.string.check_network_timeout);
			pagerList = imageDB.getImageList();
		} else {
			List list = (List) resultMap.get("advertisementInfoList");
			//new FileUtils(this).deleteFile();
			imageDB.delete();
			for (int i = 0; i < list.size(); i++) {
				ZiXun pager = new ZiXun();
				Map temp = new HashMap();
				temp = (Map) list.get(i);
				pager.content = (String) temp.get("TYPE_NAME");
				pager.image = (String) temp.get("IMAGE_URL");
				pager.newId = (String) temp.get("DETAILS_ID");
				pagerList.add(pager);
			}
			imageDB.insertImage(pagerList);
		}
		mainViewPagerAdapter = new NetViewPagerAdapter(MyMainActivity.this,pagerList);
		mainViewPager.setAdapter(mainViewPagerAdapter);
		setPoint(0);
		TimerStart();
	}
	
	//---2号回调 ---
	@SuppressWarnings("rawtypes")
	public void yaoSucessCallBack(Object data){
		ArrayList<Yao> yaoList = new ArrayList<Yao>();
		List resultMap = (ArrayList) data;
		if (resultMap == null || resultMap.size() == 0) {
		   //ShowUtil.showToast(MyMainActivity.this,R.string.check_network_timeout);
		} else {
		for(int i =0;i<resultMap.size();i++){
			Yao yao =new Yao();
			Map temp = new HashMap();
			temp = (Map) resultMap.get(i);
			yao.uid = uid;
			yao.yaoId = (String) temp.get("UUID");
			yao.name = (String) temp.get("DRUG_NAME");
			yao.type = (String) temp.get("DRUG_TYPE");
		    yao.typeName = (String) temp.get("DRUG_TYPE_NAME");
		    yao.firm = (String) temp.get("DRUG_COMPANY");
		    //yao.content = (String) temp.get("CONTENT");
		    //yao.contentText = (String) temp.get("CONTENT_TEMP");
		    yaoList.add(yao);
		 }
		//清理数据表
		yaoDB.delete();
		//保存到数据库
		for(int i=0; i<yaoList.size(); i++){
			yaoDB.insertYao(yaoList.get(i));
		  }
    	}
	}
	
	//---3号回调 ---
	@SuppressWarnings("rawtypes")
	public void celingSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
//			ShowUtil.showToast(CeLingResultActivity.this,
//					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//
				ceLingDB.updataSyncNoToYes(uid);
			} else if (result.equals("fail")) {
				//
			}
		}
	}
	
	//---4号回调 ---
	@SuppressWarnings("rawtypes")
	public void weightSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			//ShowUtil.showToast(MyMainActivity.this,"体重空");
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//ShowUtil.showToast(MyMainActivity.this,"体重yes");
				
				weightDB.updataSyncNoToYes(uid);
			} else if (result.equals("fail")) {
				//ShowUtil.showToast(MyMainActivity.this,"体重no");
				
			}
		}
	}
	
	//---5号回调 ---
	@SuppressWarnings("rawtypes")
	public void waterSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			//ShowUtil.showToast(MyMainActivity.this,"体重空");
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//ShowUtil.showToast(MyMainActivity.this,"体重yes");
				
				waterDB.updataSyncNoToYes(uid);
			} else if (result.equals("fail")) {
				//ShowUtil.showToast(MyMainActivity.this,"体重no");
				
			}
		}
	}
	
	//---6号回调 ---
	@SuppressWarnings("rawtypes")
	public void heartSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			//ShowUtil.showToast(MyMainActivity.this,"体重空");
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//ShowUtil.showToast(MyMainActivity.this,"体重yes");
				
				heartDB.updataSyncNoToYes(uid);
			} else if (result.equals("fail")) {
				//ShowUtil.showToast(MyMainActivity.this,"体重no");
				
			}
		}
	}
	
	//监听
	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.main_celing:
				//if(!ConnectUtil.isLogin(MyMainActivity.this)){
				if(true){
					Intent celingIntent = new Intent(MyMainActivity.this, MainCeLingActivity.class);
					celingIntent.putExtra(ConstantUtil.CELING_FROM, "main");
					startActivity(celingIntent);
				}else{
					Intent celingIntent = new Intent(MyMainActivity.this, LoginActivity.class);
					startActivity(celingIntent);
				}
				break;
			case R.id.main_pinggu:
				//if(!ConnectUtil.isLogin(MyMainActivity.this)){
				if(true){
					Intent pingguIntent = new Intent(MyMainActivity.this, MainPingGuActivity.class);
					startActivity(pingguIntent);
				}else{
					Intent pingguIntent = new Intent(MyMainActivity.this, LoginActivity.class);
					startActivity(pingguIntent);
				}
				
				break;
			case R.id.main_zixun:
//				if(!ConnectUtil.isLogin(MyMainActivity.this)){
					Intent zixunIntent = new Intent(MyMainActivity.this, MainYiShiActivity.class);
			//	Intent zixunIntent = new Intent(MyMainActivity.this, XmppActivity.class);
					startActivity(zixunIntent);
//				}else{
//					Intent zixunIntent = new Intent(MyMainActivity.this, LoginActivity.class);
//					startActivity(zixunIntent);
//				}
				break;
			case R.id.main_phone:
				//if(!ConnectUtil.isLogin(MyMainActivity.this)){
				if(true){
					Intent helperIntent = new Intent(MyMainActivity.this, MainPhoneActivity.class);
					startActivity(helperIntent);
				}else{
					Intent helperIntent = new Intent(MyMainActivity.this, LoginActivity.class);
					startActivity(helperIntent);
				}
				break;
			case R.id.main_baike:
			    Intent baikeIntent = new Intent(MyMainActivity.this, MainZiXunActivity.class);
				startActivity(baikeIntent);
				break;
			case R.id.main_helper:
				//if(!ConnectUtil.isLogin(MyMainActivity.this)){
					if(true){
					Intent helperIntent = new Intent(MyMainActivity.this, MainServiceActivity.class);
					startActivity(helperIntent);
				}else{
					Intent helperIntent = new Intent(MyMainActivity.this, LoginActivity.class);
					startActivity(helperIntent);
				}
				
				break;
			case R.id.main_center:
				Intent centerIntent = new Intent(MyMainActivity.this, MainSetActivity.class);
				startActivity(centerIntent);
				break;
			default:
				break;
			}
		}
	}
	//开启事件传递机制
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (mainViewPager.getCurrentItem() == (pagerList.size() - 1)) {
					currentnum = 0;
				} else {
					currentnum = mainViewPager.getCurrentItem() + 1;
				}
				mainViewPager.setCurrentItem(currentnum);
				break;
			case MSG_SHOWUPGRADE:
				showUpgradeDialog();
				SharedPreferenceUtil.setBoolean(MyMainActivity.this, ConstantUtil.SHOW_UPDATE, false);
				break;
				// 进行下载
			case MSG_DOAWNLOAD:
				doawnload();
				break;
			default:
				break;
			}
		}
	};
	
	// 获取版本号
	public String getVersionName() {
		PackageManager manager = getPackageManager();// packageManager
		try {
			// 获取哪个应用程序的清单文件，getPackageName()指获取当前程序的包名
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
					0);
			return packageInfo.versionName;// 获取清单文件中的版本信息
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// 升级
	private void showUpgradeDialog() {
		MyDialog dialog = new MyDialog(MyMainActivity.this)
				.setTitle(R.string.upgrade_title)
				.setMessage(ConnectUtil.versionNameString + version + "\n更新内容有:\n" + content)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Message message = Message.obtain();
						message.what = MSG_DOAWNLOAD;
						handler.sendMessage(message);
					}
				})
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
					}
				});

		dialog.create(null);
		dialog.showMyDialog();
	}

	// 下载
	MyDialog m_pDialog;
	public void doawnload() {
		HttpUtils utils = new HttpUtils();
		m_pDialog = new MyDialog(MyMainActivity.this);
		m_pDialog.setTitle("提示");
		m_pDialog.setMessage("心衰版本更新中...");
		//m_pDialog.setIcon(R.drawable.heart_mark_icon);
		//m_pDialog.setIndeterminate(false);
		//m_pDialog.setCancelable(false);
		m_pDialog.setProgressbar();
		m_pDialog.create(null);
		m_pDialog.showMyDialog();
		/**
		 * 判断SD的状态：如果SD不存在就进行友好提示
		 */
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyToast.show(getApplicationContext(), "手机SD卡不存在",
					Toast.LENGTH_SHORT);
			return;
		}
		/*
		 * 参数： 1 下载url地址 2 下载到哪个位置 3 回调方法
		 */
		utils.download(versionUrl, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/HeartMark" + version + ".apk",
				new RequestCallBack<File>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {// 下载失败
						ShowUtil.showToast(MyMainActivity.this,
								R.string.download_fail);
						m_pDialog.dismiss();
					}

					// 下载成功
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						m_pDialog.dismiss();
						// 下载成功，调用系统安装程序进行程序的安装
						installAPk();
					}

					@Override
					public void onLoading(final long total, final long current,
							boolean isUploading) {// 下载进度
						super.onLoading(total, current, isUploading);
						int progress = (int) (current * 100 / total);
						m_pDialog.setProgress(progress);
					}
				});
	}
	
	// 安装
	public void installAPk() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/HeartMark" + version + ".apk")),
				"application/vnd.android.package-archive");
		/**
		 * 安装后进行函数的回调
		 */
		this.startActivityForResult(intent, 100);
	}
	
	private Timer timer; 
	public void TimerStart() {
		TimerTask task = new TimerTask() {
			public void run() {
				Message message = handler.obtainMessage();
				message.what = 1;
				handler.sendMessage(message);
			}
		};
		timer = new Timer(true);
		timer.schedule(task, 0, 4000);
	}
	public void TimerStop() {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	// viewPager监听器
	class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			setPoint(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	//初始化轮播图的指示点
	public void setPoint(int page) {
		pointLayout.removeAllViews();
		page = page % pagerList.size();
		for (int i = 0; i < pagerList.size(); i++) {
			point = new ImageView(this);
			LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 10;
			point.setLayoutParams(params);
			point.setBackgroundResource(R.drawable.point_yes);
			if (page != i) {
				point.setBackgroundResource(R.drawable.point_no);
			}
			pointLayout.addView(point);
		}
	}

	@Override
	public void onTopbarRightButtonSelected() {
		
        Intent remindIntent = new Intent(MyMainActivity.this, SetRemindActivity.class);
        startActivity(remindIntent);
       
	}
}
