package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.HelpHeartDB;
import com.yikang.heartmark.database.HelpWaterDB;
import com.yikang.heartmark.database.HuLiWeightDB;
import com.yikang.heartmark.model.HelpHeart;
import com.yikang.heartmark.model.HelpWater;
import com.yikang.heartmark.model.HuLiWeight;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class LoginActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener {

	private EditText editPhone;
	private EditText editPassword;
	private Button buttonLogin;
	private Button buttonForget;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.loginTopBar);
		topbar.setTopbarTitle(R.string.login);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightText("注册");
		topbar.setOnTopbarRightButtonListener(this);

		editPhone = (EditText) findViewById(R.id.login_phone);
		editPassword = (EditText) findViewById(R.id.login_password);
		buttonLogin = (Button) findViewById(R.id.login_login);
		buttonForget = (Button) findViewById(R.id.login_forget);
		
		buttonLogin.setOnClickListener(new MyViewOnclicklistener());
		buttonForget.setOnClickListener(new MyViewOnclicklistener());
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.login_login:
				doLogin();
				break;
			case R.id.login_forget:
				Intent forgetIntent = new Intent(LoginActivity.this, ForgetPassword.class);
				startActivity(forgetIntent);
				break;
			default:
				break;
			}
		}
	}

	private String infoNameString;
	private String infoPswString;
	private void doLogin() {
		infoNameString = editPhone.getText().toString().trim();
		infoPswString = editPassword.getText().toString().trim();
		if (TextUtils.isEmpty(infoNameString)) {
			MyToast.show(LoginActivity.this, "用户名不能为空，请输入用户名", Toast.LENGTH_SHORT);
			return;
		} else if (infoNameString.length() != 11) {
			MyToast.show(LoginActivity.this, "请输入11位的手机号",Toast.LENGTH_SHORT);
			return;
		}else if (TextUtils.isEmpty(infoPswString)) {
			MyToast.show(LoginActivity.this, "密码不能为空，请输入密码",Toast.LENGTH_SHORT);
			return;
		} else if (infoPswString.length() < 6
				|| infoPswString.length() > 16) {
			MyToast.show(LoginActivity.this, "请输入6-16位密码",Toast.LENGTH_SHORT);
			return;
		}else{
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", infoNameString);
			params.put("password", infoPswString);
			if (ConnectUtil.isConnect(AppContext.context)) {//如果网络是连接状态
				ConnectionManager.getInstance().send("FN01060WL00", "onMobileLogin", params,
								"loginSucessCallBackHandler", this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context, R.string.check_network);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void loginSucessCallBackHandler(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(LoginActivity.this,
					R.string.check_network_timeout);
		} else {
			String resultCode = (String) resultMap.get("resultCode");
			String retValue = HandMethodsValue(resultCode);
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//MyToast.show(LoginActivity.this, retValue,Toast.LENGTH_SHORT);
				String tokenId = (String) resultMap.get("tokenId");
				Map userInfo = (Map) resultMap.get("userInfo");
				Map memberInfo = (Map) resultMap.get("memberInfo");
				// 保存用户名
				String userName = (String) userInfo.get("USERNAME");
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_NAME, userName);
				// 保存uid
				String userUid = (String) userInfo.get("UUID");
				SharedPreferenceUtil.setString(LoginActivity.this, ConstantUtil.USER_UID, userUid);
				// tokenId(系统识别是否登录)
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_TOKEN, tokenId);
				
			    /**
			     * 个人和中心使用
			     */
				String USERNAMEinfo = (String) memberInfo.get("USERNAME");//昵称
				String SEX_VALUE = (String) memberInfo.get("SEX");//性别
				String AGE = String.valueOf(Double.valueOf(memberInfo.get("AGE").toString()).intValue());//年龄
				String WEIGHT = (String) memberInfo.get("WEIGHT");//体重
				String DOCTOR_NO = (String) resultMap.get("DOCTOR_NO");//医号
				String flag = (String) resultMap.get("flag");//医号
				
				
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_NAME_info, USERNAMEinfo);
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_SEX, SEX_VALUE);
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_AGE, AGE);
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_WEIGHT, WEIGHT);
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_NUMBER, DOCTOR_NO);
				SharedPreferenceUtil.setString(LoginActivity.this,ConstantUtil.USER_NUMBER_CODE, flag);
				
				// 保存登录标志
				SharedPreferenceUtil.setBoolean(LoginActivity.this,ConstantUtil.LOGIN, true);
				// 上传推送id
				saveJpushId();
				
				if(SEX_VALUE.equals("") || SEX_VALUE == null ||Integer.valueOf(AGE) == 0 
						|| WEIGHT.equals("") || WEIGHT == null){
					Intent infoIntent = new Intent(LoginActivity.this, CenterInfoActivity.class);
					infoIntent.putExtra("login_center", "login_center");
					startActivity(infoIntent);
					finish();
					return;
				}
				
				// 下载用户的其他数据到客户端
				downloadData();	
				finish();
			} else if (result.equals("fail")) {
				MyToast.show(LoginActivity.this, retValue,Toast.LENGTH_SHORT);
			}
		}
	}

	//用户状态判断
	private String HandMethodsValue(String resultCode) {
		String retValue = "";
		if("01001".equals(resultCode)){
			retValue = "成功";
		}else if("02002".equals(resultCode)){
			retValue = "用户名不存在";
		}else if("02003".equals(resultCode)){
			retValue = "用戶名或密码错误";
		}
		return retValue;
	}
	
	private void saveJpushId() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceType", "0001");
		params.put("clientId", SharedPreferenceUtil.getString(
				LoginActivity.this, ConstantUtil.JPUSH_ID));
		ConnectionManager.getInstance().send("FN06090WD00",
				"saveMessagePushInfo", params);
	}
	
	/****************** 把心率 体重 补水的信息下载下来  保存到数据库中******************************/    
	public void downloadData() {
		//TODO 登录，对数据进行下载处理
		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(LoginActivity.this)) {
			ConnectionManager.getInstance().send("FN11060WD00",
					"queryNurseInfo", params, "getDataCallBack",
					LoginActivity.this);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
	private HuLiWeightDB weightDB;
	private HelpWaterDB waterDB;
	private HelpHeartDB heartDB;
	private ArrayList<HelpHeart> helpHeart = new ArrayList<HelpHeart>();
	private ArrayList<HuLiWeight> huLiWeight = new ArrayList<HuLiWeight>();
	private ArrayList<HelpWater> helpWater = new ArrayList<HelpWater>();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getDataCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(LoginActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				String uid = SharedPreferenceUtil.getString(LoginActivity.this, ConstantUtil.USER_UID);
				int baseweight = Integer.valueOf(SharedPreferenceUtil.getString
						(LoginActivity.this, ConstantUtil.USER_WEIGHT));
				int sync = ConstantUtil.SYNC_YES;		
				heartDB = new HelpHeartDB(LoginActivity.this);
				weightDB = new HuLiWeightDB(LoginActivity.this);
				waterDB = new HelpWaterDB(LoginActivity.this);
				heartDB.delete();weightDB.delete();waterDB.delete();
				List<Map> list = (List) resultMap.get("nurseInfoList");
				for (int i = 0; i < list.size(); i++) {
//					list.get(i).get("TRAN_ID");
//					list.get(i).get("OUT_WATER");
//					list.get(i).get("CREATE_TIME");
//					list.get(i).get("WEIGHT");
//					list.get(i).get("CREATE_USER");
//					list.get(i).get("NURSE_DATE");
//					list.get(i).get("HEART_RATE");
//					list.get(i).get("UUID");
//					list.get(i).get("IN_WATER");
					
					String celingTime = (String) list.get(i).get("NURSE_DATE");
					if(list.get(i).get("HEART_RATE") != null){
						HelpHeart heart = new HelpHeart();
						heart.uid = uid;
						heart.sync = sync;
						heart.date = celingTime.substring(0, 10);
						heart.dateMonth = celingTime.substring(0, 7);
						heart.day = celingTime.substring(8, 10);
						heart.timeMill = DateUtil.getLongOfDayTime(celingTime);
						heart.heart = Double.valueOf(list.get(i).get("HEART_RATE").toString()).intValue();
						helpHeart.add(heart);
					}
					
					if(list.get(i).get("WEIGHT") != null){
						HuLiWeight weight = new HuLiWeight();
						weight.uid = uid;
						weight.sync = sync;
						weight.date = celingTime.substring(0, 10);
						weight.dateMonth = celingTime.substring(0, 7);
						weight.day = celingTime.substring(8, 10);
						weight.timeMill = DateUtil.getLongOfDayTime(celingTime);
						double value = Double.valueOf(list.get(i).get("WEIGHT").toString());
						int thisWeight = Double.valueOf(value*10).intValue();
						weight.thisWeight = thisWeight;
						weight.baseWeight = baseweight;
						weight.diff = Math.abs(weight.thisWeight - weight.baseWeight);
						huLiWeight.add(weight);
					}
					
					if(list.get(i).get("IN_WATER") != null || list.get(i).get("OUT_WATER") != null){
						HelpWater water = new HelpWater();
						water.uid = uid;
						water.sync = sync;
						water.date = celingTime.substring(0, 10);
						water.dateMonth = celingTime.substring(0, 7);
						water.day = celingTime.substring(8, 10);
						water.timeMill = DateUtil.getLongOfDayTime(celingTime);
						if(list.get(i).get("IN_WATER") == null){
							water.inWater = 0;
						}else{
							water.inWater = Double.valueOf(list.get(i).get("IN_WATER").toString()).intValue();
						}
						if(list.get(i).get("OUT_WATER") == null){
							water.outWater = 0;
						}else{
							water.outWater = Double.valueOf(list.get(i).get("OUT_WATER").toString()).intValue();
						}
						helpWater.add(water);
					}
				}
				heartDB.insertList(helpHeart);
				weightDB.insertList(huLiWeight);
				waterDB.insertList(helpWater);
			}
		}
	}
	/*************************************************************/

	@Override
	public void onTopbarRightButtonSelected() {
		Intent registIntent = new Intent(LoginActivity.this,
				RegistActivity.class);
		startActivity(registIntent);
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
