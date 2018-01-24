package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.myzxing.activity.CaptureActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.wheel.WheelView;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;

public class RegistInfoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private ImageView imageManSelect;
	private ImageView imageMenSelect;
	private ImageView imageMenScan;
	private EditText editName;
	private EditText editAge;
	private EditText editWeight;
	private EditText editNumber;
	private RelativeLayout layoutAge;
	private RelativeLayout layoutWeight;
	private CheckBox[] checkBoxs = new CheckBox[6];
	private Button buttonFinish;
	private String phone;
	private String password;
	private String sexCode = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registinfo);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.registInfoTopBar);
		topbar.setTopbarTitle(R.string.regist_info);
		topbar.setOnTopbarLeftButtonListener(this);

		imageManSelect = (ImageView) findViewById(R.id.regist_select_man);
		imageMenSelect = (ImageView) findViewById(R.id.regist_select_men);
		imageMenScan = (ImageView) findViewById(R.id.regist_info_scan);
		editName = (EditText) findViewById(R.id.regist_info_name);
		editAge = (EditText) findViewById(R.id.regist_info_age);
		editWeight = (EditText) findViewById(R.id.regist_info_weight);
		editNumber = (EditText) findViewById(R.id.regist_info_number);
		layoutAge = (RelativeLayout) findViewById(R.id.regist_info_age_layout);
		layoutWeight = (RelativeLayout) findViewById(R.id.regist_info_weight_layout);
		checkBoxs[0] = (CheckBox) findViewById(R.id.registinfo_check1);
		checkBoxs[1] = (CheckBox) findViewById(R.id.registinfo_check2);
		checkBoxs[2] = (CheckBox) findViewById(R.id.registinfo_check3);
		checkBoxs[3] = (CheckBox) findViewById(R.id.registinfo_check4);
		checkBoxs[4] = (CheckBox) findViewById(R.id.registinfo_check5);
		checkBoxs[5] = (CheckBox) findViewById(R.id.registinfo_check6);
		buttonFinish = (Button) findViewById(R.id.regist_info_finish);
		Intent getIntent = getIntent();
		phone = getIntent.getStringExtra("phone");
		password = getIntent.getStringExtra("password");

		imageManSelect.setOnClickListener(new MyViewOnclicklistener());
		imageMenSelect.setOnClickListener(new MyViewOnclicklistener());
		imageMenScan.setOnClickListener(new MyViewOnclicklistener());
		layoutAge.setOnClickListener(new MyViewOnclicklistener());
		layoutWeight.setOnClickListener(new MyViewOnclicklistener());
		buttonFinish.setOnClickListener(new MyViewOnclicklistener());
	}

	private int indexAge = 30;
	private int indexWeight = 60;
	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.regist_select_man:
				imageManSelect.setSelected(true);
				imageMenSelect.setSelected(false);
				sexCode = "0001";
				break;
			case R.id.regist_select_men:
				imageManSelect.setSelected(false);
				imageMenSelect.setSelected(true);
				sexCode = "0002";
				break;
			case R.id.regist_info_scan:
				try {
					Intent intent = new Intent(RegistInfoActivity.this,
							CaptureActivity.class);
					startActivityForResult(intent, 0);
				} catch (Exception e) {
				}
				break;
			case R.id.regist_info_age_layout:
				showDialogAge("   岁",0,120, editAge , indexAge);
				break;
			case R.id.regist_info_weight_layout:
				showDialogWeight("   kg",0,160, editWeight, indexWeight);
				break;
			case R.id.regist_info_finish:
				doRegist();
				break;
			default:
				break;
			}
		}
	}

	private String infoNameString;
	private String infoAgeString;
	private String infoSexString;
	private String infoWeightString;
	private String infoNumberString;
	private void doRegist() {
		infoNameString = editName.getText().toString().trim();
		infoSexString = sexCode;
		infoAgeString = editAge.getText().toString().trim();
		infoWeightString = editWeight.getText().toString().trim();
		infoNumberString = editNumber.getText().toString().trim();
		
		if (TextUtils.isEmpty(infoSexString)) {
			ShowUtil.showToast(this, "请选择性别");
			return;
		}else if (TextUtils.isEmpty(infoNameString)) {
			ShowUtil.showToast(this, "请输入昵称");
			return;
		}else if (TextUtils.isEmpty(infoAgeString)) {
			ShowUtil.showToast(this, "请选择年龄");
			return;
		} else if (TextUtils.isEmpty(infoWeightString)) {
			ShowUtil.showToast(this, "请选择体重");
			return;
		}else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", phone);
			params.put("password", password);
			params.put("userName", infoNameString);
			params.put("sex", infoSexString);
			params.put("age", infoAgeString);
			params.put("weight", infoWeightString);
			params.put("doctorNo", infoNumberString);
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < 6; i++) {
				JSONObject json = new JSONObject();
				if (checkBoxs[i].isSelected()) {
					try {
						json.put("CODE", "000" + i + 1);
						jsonArray.put(json);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			params.put("medicalHistory", jsonArray.toString());
			if (ConnectUtil.isConnect(AppContext.context)) {
				ConnectionManager.getInstance().send("FN01070WD00",
						"registerUserInfoByMobileAndLogin", params, "registerSucessCallBack",
						this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context, R.string.check_network);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void registerSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(RegistInfoActivity.this,
					R.string.check_network_timeout);
		}else{
			String resultCode = (String) resultMap.get("resultCode");
			String retValue = HandMethodsValue(resultCode);
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				String tokenId = (String) resultMap.get("tokenId");
				Map userInfo = (Map) resultMap.get("userInfo");
				Map memberInfo = (Map) resultMap.get("memberInfo");
				// 保存用户名
				String userName = (String) userInfo.get("USERNAME");
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_NAME, userName);
				// 保存uid
				String userUid = (String) userInfo.get("UUID");
				SharedPreferenceUtil.setString(RegistInfoActivity.this, ConstantUtil.USER_UID, userUid);
				// tokenId(系统识别是否登录)
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_TOKEN, tokenId);
				
			    /**
			     * 个人和中心使用
			     */
				String USERNAMEinfo = (String) memberInfo.get("USERNAME");//昵称
				String SEX_VALUE = (String) memberInfo.get("SEX");//性别
				String AGE = String.valueOf(Double.valueOf(memberInfo.get("AGE").toString()).intValue());//年龄
				String WEIGHT = (String) memberInfo.get("WEIGHT");//体重
				String DOCTOR_NO = (String) memberInfo.get("DOCTOR_NO");//医号
				String flag = (String) memberInfo.get("flag");//医号
				String message = (String) memberInfo.get("message");//医号
				
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_NAME_info, USERNAMEinfo);
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_SEX, SEX_VALUE);
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_AGE, AGE);
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_WEIGHT, WEIGHT);
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_NUMBER, DOCTOR_NO);
				SharedPreferenceUtil.setString(RegistInfoActivity.this,ConstantUtil.USER_NUMBER_CODE, flag);
/*				if("01003".equals(message)){
					MyToast.show(RegistInfoActivity.this,"绑定成功",Toast.LENGTH_LONG);
				}else if("01001".equals(message)){
					MyToast.show(RegistInfoActivity.this,"已经绑定过",Toast.LENGTH_LONG);
				}else if("01002".equals(message)){
					MyToast.show(RegistInfoActivity.this,"医生编号不存在",Toast.LENGTH_LONG);
				}*/
				
				// 保存登录标志
				SharedPreferenceUtil.setBoolean(RegistInfoActivity.this,ConstantUtil.LOGIN, true);
				// 上传推送id
				saveJpushId();
				
				if(SEX_VALUE.equals("") || SEX_VALUE == null ||Integer.valueOf(AGE) == 0 
						|| WEIGHT.equals("") || WEIGHT == null){
					Intent infoIntent = new Intent(RegistInfoActivity.this, CenterInfoActivity.class);
					startActivity(infoIntent);
					RegistActivity.instance.finish();
					finish();
					return;
				}
				
				Intent logoinIntent = new Intent(RegistInfoActivity.this, MainSetActivity.class);
				logoinIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(logoinIntent);
				RegistActivity.instance.finish();
				finish();
			}else{
				MyToast.show(RegistInfoActivity.this,retValue, Toast.LENGTH_LONG);
			}
		}
	}

	private String HandMethodsValue(String resultCode) {
		String retValue = "";
		if("01001".equals(resultCode)){
			retValue = "成功";
		}else if("02002".equals(resultCode)){
			retValue = "用户名不存在";
		}else if("02003".equals(resultCode)){
			retValue = "用戶名或密码错误";
		}else if("03002".equals(resultCode)){
			retValue = "注册时使用密码不能为空";
		}else if("03003".equals(resultCode)){
			retValue = "注册时使用用户名已经存在";
		}else if("03004".equals(resultCode)){
			retValue = "注册时使用手机号已经存在";
		}else if("03005".equals(resultCode)){
			retValue = "注册时使用电子邮件已经存在";
		}
		return retValue;
	}

	/**
	 * 注册JpushId
	 */
	private void saveJpushId() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceType", "0001");
		params.put("clientId", SharedPreferenceUtil.getString(
				RegistInfoActivity.this, ConstantUtil.JPUSH_ID));
		ConnectionManager.getInstance().send("FN06090WD00",
				"saveMessagePushInfo", params);
	}
	
	public void showDialogAge(String unit,int A,int B, final EditText editText, int index) {
		MyDialog myDialog = new MyDialog(RegistInfoActivity.this);
		myDialog.setSimpleSelectPicker(index, A, B, unit);
		myDialog.setTitle(R.string.regist);
		myDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v
						.findViewById(R.id.simpleSelectPicker);
				editText.setText(wv_content.getCurrentItem() + "");
				indexAge = wv_content.getCurrentItem();
			}
		});
		myDialog.create(null).show();
	}
	
	public void showDialogWeight(String unit,int A,int B, final EditText editText, int index) {
		MyDialog myDialog = new MyDialog(RegistInfoActivity.this);
		myDialog.setSimpleSelectPicker(index, A, B, unit);
		myDialog.setTitle(R.string.regist);
		myDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v
						.findViewById(R.id.simpleSelectPicker);
				editText.setText(wv_content.getCurrentItem() + "");
				indexWeight = wv_content.getCurrentItem();
			}
		});
		myDialog.create(null).show();
	}

	// 处理扫描结果（在界面上显示）
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			if(scanResult.length() > 20){
				scanResult = scanResult.substring(0, 20);
			}
			editNumber.setText(scanResult);
		}
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
