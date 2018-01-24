package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class RegistActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	
	private String usernameString = "";
	private EditText editPhone;
	private EditText editCode;
	private EditText editPassWord;
	private Button buttonCode;
	private Button buttonNext;
	private String getCode = null;
	private MyCount mc;
	public static RegistActivity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		instance = this;
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.registTopBar);
		topbar.setTopbarTitle(R.string.regist);
		topbar.setOnTopbarLeftButtonListener(this);

		editPhone = (EditText) findViewById(R.id.regist_phone);
		editCode = (EditText) findViewById(R.id.regist_code);
		editPassWord = (EditText) findViewById(R.id.regist_password);
		buttonCode = (Button) findViewById(R.id.regist_code_get);
		buttonNext = (Button) findViewById(R.id.regist_next);

		buttonCode.setOnClickListener(new MyViewOnclicklistener());
		buttonNext.setOnClickListener(new MyViewOnclicklistener());
		
		mc = new MyCount(60000, 1000);
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.regist_code_get:
				checkPhone();
				//getCode();
				break;
			case R.id.regist_next:
				doRegist();
/*				Intent nextIntent = new Intent(RegistActivity.this,
						RegistInfoActivity.class);
				startActivity(nextIntent);*/
				break;
			default:
				break;
			}
		}
	}

	public void checkPhone() {
		usernameString = editPhone.getText().toString().trim();
		if(usernameString.length() != 11){
			ShowUtil.showToast(AppContext.context, R.string.phone_error);
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile",usernameString);
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN01070WD00",
					"registerUserInfoByMobileValidate", params,
					"SucessCallBack", this);
			;
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
	@SuppressWarnings("rawtypes")
	public void SucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(RegistActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				getCode();
			} else if (result.equals("fail")) {
				MyToast.show(RegistActivity.this,"手机号已被注册", Toast.LENGTH_LONG);
			}
		}
	}
	
	private void getCode() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile",usernameString);
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN01070WD00","getMobileVerCode", params, "getCodeSucessCallBack", this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
	@SuppressWarnings("rawtypes")
	public void getCodeSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(RegistActivity.this,R.string.check_network_timeout);
		} else {
		    getCode = (String) resultMap.get("CODE");
		    buttonCode.setClickable(false);
		    mc.start();
		}
	}

	private void doRegist() {
		if (editPhone.getText().toString().equals("")) {
			MyToast.show(RegistActivity.this,  "请输入手机号", Toast.LENGTH_LONG);
			return;
		} else if (editPhone.getText().toString().trim().length() != 11) {
			MyToast.show(RegistActivity.this, "请输入11位的手机号", Toast.LENGTH_LONG);
			return;
		} else if (editCode.getText().toString().equals("")) {
			MyToast.show(RegistActivity.this, "请输入短信验证码", Toast.LENGTH_LONG);
			return;
		} else if (getCode == null) {
			MyToast.show(RegistActivity.this, "请获取短信验证码", Toast.LENGTH_LONG);
		} else if (!editCode.getText().toString().equals(getCode)) {
			MyToast.show(RegistActivity.this, "验证码有误", Toast.LENGTH_LONG);
		} else if (editPassWord.getText().toString().equals("")) {
			MyToast.show(RegistActivity.this, "请输入密码", Toast.LENGTH_LONG);
			return;
		} else if (editPassWord.getText().toString().trim().length() < 6
				|| editPassWord.getText().toString().trim().length() > 16) {
			MyToast.show(RegistActivity.this, "请输入6-16位密码", Toast.LENGTH_LONG);
			return;
		} else {
			if(editPhone.getText().toString().trim().equals(usernameString)){
				connectRegist();
			}else{
				MyToast.show(RegistActivity.this, "手机号与验证码不匹配", Toast.LENGTH_LONG);
				return;
			}
		}
	}

	private void connectRegist() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", editPhone.getText().toString().trim());
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN01070WD00",
					"registerUserInfoByMobileValidate", params,
					"registSucessCallBack", this);
			;
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	@SuppressWarnings("rawtypes")
	public void registSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(RegistActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//MyToast.show(RegistActivity.this, "验证成功", Toast.LENGTH_LONG);
				Intent nextIntent = new Intent(RegistActivity.this,
						RegistInfoActivity.class);
				nextIntent.putExtra("phone", editPhone.getText().toString()
						.trim());
				nextIntent.putExtra("password", editPassWord.getText()
						.toString().trim());
				startActivity(nextIntent);
			} else if (result.equals("fail")) {
				MyToast.show(RegistActivity.this,"手机号已被注册", Toast.LENGTH_LONG);
			}
		}
	}

	public void finishActivity() {
		if (instance != null) {
			instance.finish();
		}
	}
	
    class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {     
        	buttonCode.setText("发送验证码");
        	buttonCode.setClickable(true);
        }     
        @Override     
        public void onTick(long millisUntilFinished) {     
        	buttonCode.setText("请等待60秒(" + millisUntilFinished / 1000 + ")");     
        }    
    } 

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

}
