package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.CeLingDB;
import com.yikang.heartmark.model.CeLingData;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;

@SuppressWarnings("unused")
public class CeLingResultActivity extends BaseActivity {
	private TextView timeText;
	private TextView typeText;
	private TextView resultText;
	private TextView riskText;
	private Button saveButton;

	private String time;
	private String timeHHmmss;
	private String type;
	private String result;
	private String risk;
	private String diagMySelf;
	private String diag;
	private CheckBox[] checkBoxs = new CheckBox[6];
	private String[] states = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_celing_result);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		timeText = (TextView) findViewById(R.id.celing_result_time);
		typeText = (TextView) findViewById(R.id.celing_result_type);
		resultText = (TextView) findViewById(R.id.celing_result_result);
		riskText = (TextView) findViewById(R.id.celing_result_risk);
		checkBoxs[0] = (CheckBox) findViewById(R.id.celing_result_duohan);
		checkBoxs[1] = (CheckBox) findViewById(R.id.celing_result_xinji);
		checkBoxs[2] = (CheckBox) findViewById(R.id.celing_result_xiongteng);
		checkBoxs[3] = (CheckBox) findViewById(R.id.celing_result_jicu);
		checkBoxs[4] = (CheckBox) findViewById(R.id.celing_result_fanzao);
		checkBoxs[5] = (CheckBox) findViewById(R.id.celing_result_kesou);
		saveButton = (Button) findViewById(R.id.celing_result_save);
		states = getResources().getStringArray(R.array.celing_state);
		saveButton.setOnClickListener(new MyViewOnclicklistener());

		LinearLayout layout = (LinearLayout) findViewById(R.id.celing_result_layout);
		layout.setOnClickListener(new MyViewOnclicklistener());

		Intent intent = getIntent();
		time = DateUtil.getNowDate(DateUtil.YEAR_MONTH_DAY);
		timeHHmmss = DateUtil.getNowDate(DateUtil.DATE_HOUR_MINUTE_SEC);
		
		type = "NT-proBNP";
		result = intent.getStringExtra("celingResult");
		int resultInt = Double.valueOf(result.replaceAll("[a-zA-Z/<>]", "")).intValue();

		int age = Integer.valueOf(SharedPreferenceUtil.getString(CeLingResultActivity.this,ConstantUtil.USER_AGE));
		if (age > 75) {
			if (resultInt < 300) {
				risk = "低";
				diagMySelf = "风险较低，请注意保持身体健康";
				diag = "您的好友心衰风险较低，请注意保持身体健康";
			} else if (resultInt > 300 && resultInt < 1800) {
				risk = "较高";
				diagMySelf = "心衰风险较高，请及时就医时刻注意您健康的状况";
				diag = "您的好友心衰风险较高，请及时就医时刻注意健康的状况";
			} else {
				risk = "危险";
				diagMySelf = "您有心衰的危险，请您立刻就医";
				diag = "您的好友有心衰的危险，请立刻就医";
			}
		} else if (age < 75 && age > 50) {
			if (resultInt < 300) {
				risk = "低";
				diagMySelf = "风险较低，请注意保持身体健康";
				diag = "您的好友心衰风险较低，请注意保持身体健康";
			} else if (resultInt > 300 && resultInt < 900) {
				risk = "较高";
				diagMySelf = "心衰风险较高，请及时就医时刻注意您健康的状况";
				diag = "您的好友心衰风险较高，请及时就医时刻注意健康的状况";
			} else {
				risk = "危险";
				diagMySelf = "您有心衰的危险，请您立刻就医";
				diag = "您的好友有心衰的危险，请立刻就医";
			}
		} else if (age < 50) {
			if (resultInt < 300) {
				risk = "低";
				diagMySelf = "风险较低，请注意保持身体健康";
				diag = "您的好友心衰风险较低，请注意保持身体健康";
			} else if (resultInt > 300 && resultInt < 450) {
				risk = "较高";
				diagMySelf = "心衰风险较高，请及时就医时刻注意您健康的状况";
				diag = "您的好友心衰风险较高，请及时就医时刻注意健康的状况";
			} else {
				risk = "危险";
				diagMySelf = "您有心衰的危险，请您立刻就医";
				diag = "您的好友有心衰的危险，请立刻就医";
			}
		}

		timeText.setText(time);
		typeText.setText(type);
		resultText.setText(result);
		riskText.setText(risk);
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.celing_result_layout:
				break;
			case R.id.celing_result_save:
			  //showSendDialog();
				doUpload();
			  //getFriend();
				break;
			default:
				break;
			}
		}
	}

	// 上传请求
	private void doUpload() {
		
//		monitorType：测量类型(接收Code)
//		monitorResult：测量结果
//		diagnois：诊断结果
//		monitorStatus：监测状态
//		monitorStatus:[{"CODE":"0002"},{"CODE":"0003"},{"CODE":"0004"},{"CODE":"0050","VALUE":"其它症状"}]
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("monitorType", "0001");
		params.put("monitorResult", result);
		params.put("diagnois", diagMySelf);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < 6; i++) {
			JSONObject json = new JSONObject();
			if (checkBoxs[i].isChecked()) {
				try {
					json.put("CODE", "000" + (i + 1));
					jsonArray.put(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		params.put("monitorStatus", jsonArray.toString());
		params.put("createTime", time+" "+timeHHmmss);
		ConnectionManager.getInstance().send("FN06060WD00", "saveMonitorData",
				params, "uploadSucessCallBack", this);
	}

	// 上传返回
	@SuppressWarnings("rawtypes")
	public void uploadSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			insertToDb(ConstantUtil.SYNC_NO);
			finishActivity();
		} else {
			String result = (String) resultMap.get("head");
			String body = (String) resultMap.get("body");
			if (result.equals("success")) {
				//
				Map ret = (Map) resultMap.get("INFOMAP");
				uuid = (String) ret.get("UUID");
				insertToDb(ConstantUtil.SYNC_YES);
				if(SharedPreferenceUtil.getBooleanDefaultTrue(CeLingResultActivity.this, ConstantUtil.FRIEND_CHECK)){
					getFriend();
				}else{
					finishActivity();
				}
				
			} else if (result.equals("fail")) {
				//
				insertToDb(ConstantUtil.SYNC_NO);
				finishActivity();
			}
		}
	}

	
	//获取是否有好友，有则提示推送
	private void getFriend(){
		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN06090WD00","queryFriendCount", 
					params, "getFriendCallBack",this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		}else{
			finishActivity();
		} 
	}
	
	// 获取好友返回
	@SuppressWarnings("rawtypes")
	public void getFriendCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(CeLingResultActivity.this,
					R.string.check_network_timeout);
			finishActivity();
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				int intValue = Double.valueOf(resultMap.get("body").toString()).intValue();
				if(intValue==0){
					finishActivity();
				}else{
					showSendDialog();
				}
			} else if (result.equals("fail")) {
				finishActivity();
			}
		}
	}
	
	// 推送
	private String uuid = "";
	private void showSendDialog() {
		MyDialog dialog = new MyDialog(CeLingResultActivity.this);
		dialog.setTitle(R.string.push_title);
		dialog.setMessage(R.string.push_msg);
		dialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("message", diag);
				params.put("monitorId", uuid);
				if (ConnectUtil.isConnect(AppContext.context)) {
					ConnectionManager.getInstance().send("FN06090WD00",
							"sendMessageToFriend", params,
							"sendSucessCallBack", CeLingResultActivity.this);
				} else {
					ShowUtil.showToast(AppContext.context,
							R.string.check_network);
				}
				finishActivity();
			}
		});
		dialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
		dialog.create(null);
		dialog.showMyDialog();
	}

	@SuppressWarnings("rawtypes")
	public void sendSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		String result = (String) resultMap.get("head");
		if (result.equals("success")) {
			ShowUtil.showToast(CeLingResultActivity.this, R.string.push_success);
		} else if (result.equals("fail") || resultMap == null) {
			ShowUtil.showToast(CeLingResultActivity.this, R.string.push_fail);
		}
	}

	private void insertToDb(int sync) {
		CeLingData celingData = new CeLingData();
		celingData.uid = ConstantUtil.getUid(CeLingResultActivity.this);
		celingData.sync = sync;
		celingData.date = time;
		celingData.dateMonth = time.substring(0, 7);
		celingData.day = time.substring(8, 10);
		celingData.time = timeHHmmss.substring(0, 5);
		celingData.type = type;
		celingData.result = result;
		String state = "";
		for (int i = 0; i < 6; i++) {
			if (checkBoxs[i].isChecked()) {
				state += states[i] + ",";
			}
		}
		if (state != null && state.length() > 1) {
			state = state.substring(0, state.length() - 1);
		}
		celingData.state = state;
		celingData.diag = diagMySelf;
		new CeLingDB(CeLingResultActivity.this).insertCeLingData(celingData);
		MyToast.show(CeLingResultActivity.this, "保存成功", Toast.LENGTH_LONG);
	}
	
	private void finishActivity() {
/*		CeLingResultActivity.this.finish();
		if(MainCeLingActivity.instance != null){
			MainCeLingActivity.instance.finish();
		}*/
		Intent homeIntent = new Intent(this, MyMainActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(homeIntent);
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
