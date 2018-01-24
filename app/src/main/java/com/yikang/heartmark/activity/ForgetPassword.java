package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ForgetPassword extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private LinearLayout layout1; // 获取验证码
	private LinearLayout layout2; // 输入密码
	private EditText editPhone; // 电话
	private EditText editCode; // 验证码
	private EditText editPass; // 密码
	private TextView textPhone;
	private Button buttonCode;
	private Button buttonNext;
	private Button buttonFinish;
	private String getCode = null;
	private MyCount myCount; // 验证码倒计时

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpassword);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.forgetTopBar);
		topbar.setTopbarTitle(R.string.forget_pass);
		topbar.setOnTopbarLeftButtonListener(this);

		layout1 = (LinearLayout) findViewById(R.id.forget_layout1);
		layout2 = (LinearLayout) findViewById(R.id.forget_layout2);
		editPhone = (EditText) findViewById(R.id.forget_phone);
		editCode = (EditText) findViewById(R.id.forget_code);
		editPass = (EditText) findViewById(R.id.forget_pass);
		textPhone = (TextView) findViewById(R.id.forget_show_phone);
		buttonCode = (Button) findViewById(R.id.forget_code_get);
		buttonNext = (Button) findViewById(R.id.forget_next);
		buttonFinish = (Button) findViewById(R.id.forget_finish);

		buttonCode.setOnClickListener(new MyViewOnclicklistener());
		buttonNext.setOnClickListener(new MyViewOnclicklistener());
		buttonFinish.setOnClickListener(new MyViewOnclicklistener());

		myCount = new MyCount(60000, 1000);
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.forget_code_get:
				getCode();
				break;
			case R.id.forget_next:
				doNext();
				break;
			case R.id.forget_finish:
				doFinish();
				break;
			default:
				break;
			}
		}
	}

	// next请求
	private void doNext() {
		if (editPhone.getText().toString().equals("")) {
			ShowUtil.showToast(ForgetPassword.this, R.string.phone_hint);
			return;
		} else if (editPhone.getText().toString().trim().length() != 11) {
			ShowUtil.showToast(ForgetPassword.this, R.string.phone_format_hint);
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", editPhone.getText().toString().trim());
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN01090WL00",
					"getMobileCode", params, "getMobileCodeCodeSucessCallBack",this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}

	}

	// next返回
	@SuppressWarnings("rawtypes")
	public void getMobileCodeCodeSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ForgetPassword.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				getCode = (String) resultMap.get("CODE");
				layout1.setVisibility(View.GONE);
				layout2.setVisibility(View.VISIBLE);
				buttonCode.setClickable(false);
				myCount.start();
				textPhone.setText(String.format(
						getResources().getString(R.string.code_send),
						editPhone.getText().toString()));
			} else if (result.equals("fail")) {
				ShowUtil.showToast(ForgetPassword.this,
						R.string.phone_notregist);
			}

		}
	}

	// code请求
	private void getCode() {
		if (editPhone.getText().toString().trim().length() != 11) {
			ShowUtil.showToast(AppContext.context, R.string.phone_error);
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", editPhone.getText().toString().trim());
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN01090WL00",
					"getMobileVerCode", params, "getCodeSucessCallBack", this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	// code返回
	@SuppressWarnings("rawtypes")
	public void getCodeSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ForgetPassword.this,
					R.string.check_network_timeout);
		} else {
			getCode = (String) resultMap.get("CODE");
			buttonCode.setClickable(false);
			myCount.start();
		}
	}

	// finish请求
	private void doFinish() {
		if (getCode == null) {
			ShowUtil.showToast(ForgetPassword.this, R.string.code_no);
			return;
		} else if (!editCode.getText().toString().equals(getCode)) {
			ShowUtil.showToast(ForgetPassword.this, R.string.code_error);
			return;
		} else if (editPass.getText().toString().equals("")
				|| editPass.getText().toString() == null) {
			ShowUtil.showToast(ForgetPassword.this, R.string.password_hint);
			return;
		}else if (editPass.getText().toString().trim().length() < 6
				|| editPass.getText().toString().trim().length() > 16) {
			MyToast.show(ForgetPassword.this, "请输入6-16位密码",
					Toast.LENGTH_SHORT);
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", editPhone.getText().toString().trim());
		params.put("newPassword", editPass.getText().toString().trim());
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN01090WL00",
					"resetPassword", params, "finishSucessCallBack", this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	// finish返回
	@SuppressWarnings("rawtypes")
	public void finishSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ForgetPassword.this,R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				ShowUtil.showToast(ForgetPassword.this,R.string.forget_success);
				finish();
			} else if (result.equals("fail")) {
				ShowUtil.showToast(ForgetPassword.this,R.string.phone_notregist);
			}
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
