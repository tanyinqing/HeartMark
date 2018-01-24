package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class SetFriendAddActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	private EditText editText;
	private Button button;
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_friend_add);
		init();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void init(){
		TopBarView topbar = (TopBarView) findViewById(R.id.setFriendAddTopBar);
		topbar.setTopbarTitle(R.string.set_friend_add);
		topbar.setOnTopbarLeftButtonListener(this);
		
		editText = (EditText) findViewById(R.id.friend_add_name);
		button = (Button) findViewById(R.id.friend_add_button);
		button.setOnClickListener(buttonListener);
	}

	View.OnClickListener buttonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(TextUtils.isEmpty(editText.getText().toString().trim())){
				MyToast.show(SetFriendAddActivity.this, "请输入要添加用户信息", Toast.LENGTH_SHORT);
				return;
			}else{
				Map<String,String> params = new HashMap<String,String>();
				params.put("friendName",  editText.getText().toString().trim());
				ConnectionManager.getInstance().send("FN06080WD00", "saveFriendInfo", params, 
						"addFriendSucessCallBack", SetFriendAddActivity.this);
				
			}
		}
	};
	
	@SuppressWarnings("rawtypes")
	public void addFriendSucessCallBack(Object data){
		Map resultMap = (Map)data;
		String result = (String) resultMap.get("head");
		String resultCode = (String) resultMap.get("resultCode");
		String retValue = HandMethodsValue(resultCode);
		if (result.equals("success")) {
			MyToast.show(SetFriendAddActivity.this, retValue,Toast.LENGTH_LONG);
			finish();
		} else if (result.equals("fail")) {
			MyToast.show(SetFriendAddActivity.this, retValue,Toast.LENGTH_LONG);
		}
	}
	
	private String HandMethodsValue(String resultCode) {
		String retValue = "";
		if("01001".equals(resultCode)){
			retValue = "信息保存成功";
		}else if("02001".equals(resultCode)){
			retValue = "添加用户信息不存在";
		}else if("02002".equals(resultCode)){
			retValue = "该用户已是您的好友";
		}else if("02003".equals(resultCode)){
			retValue = "请登录后再操作";
		}
		return retValue;
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
