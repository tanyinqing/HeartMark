package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.SetFriendAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.Friend;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class SetFriendActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener {
	private TextView friendTitleText;
	private Button friendPushCheck;
	private ListView friendListView;
	private SetFriendAdapter friendAdapter;
	private ArrayList<Friend> friendList = new ArrayList<Friend>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_friend);
		init();
	}
	@Override
	public void onResume() {
		super.onResume();
		//数据获得
		getFriend();
	}
	@Override
	public void onPause() {
		super.onPause();
		//数据清空
		friendList.clear();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setFriendTopBar);
		topbar.setTopbarTitle(R.string.set_friend);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightButton(R.drawable.set_friend_add);
		topbar.setOnTopbarRightButtonListener(this);

		friendTitleText = (TextView) findViewById(R.id.set_friend_title);
		friendPushCheck = (Button) findViewById(R.id.set_friend_push_check);
		friendListView = (ListView) findViewById(R.id.set_friend_listview);
		
		if(SharedPreferenceUtil.getBooleanDefaultTrue(SetFriendActivity.this, ConstantUtil.FRIEND_CHECK)){
			friendPushCheck.setSelected(true);
		}else{
			friendPushCheck.setSelected(false);
		}
		
		friendPushCheck.setOnClickListener(checkListener);
	}

	View.OnClickListener checkListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(friendPushCheck.isSelected()){
				friendPushCheck.setSelected(false);
				SharedPreferenceUtil.setBoolean(SetFriendActivity.this, ConstantUtil.FRIEND_CHECK, false);
			}else{
				friendPushCheck.setSelected(true);
				SharedPreferenceUtil.setBoolean(SetFriendActivity.this, ConstantUtil.FRIEND_CHECK, true);
			}
			
		}
	};
	
	private void getFriend() {
		Map<String, String> params = new HashMap<String, String>();
		ConnectionManager.getInstance().send("FN06080WD00", "getFriendList",
				params, "getFriendSucessCallBack", SetFriendActivity.this);
	}

	@SuppressWarnings("rawtypes")
	public void getFriendSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		String result = (String) resultMap.get("head");
		String hint = (String) resultMap.get("body");
		if (result.equals("success")) {
			List list = (List) resultMap.get("friendList");
			friendList.clear();
			for (int i = 0; i < list.size(); i++) {
				Friend friend = new Friend();
				Map temp = new HashMap();
				temp = (Map) list.get(i);
				friend.uid = (String) temp.get("FRIEND_ID");
				friend.name = (String) temp.get("FRIEND_NAME");
				friend.phone = (String) temp.get("FRIEND_TEL");
				friend.sex = (String) temp.get("SEX");
				friend.check = Integer.valueOf((String) temp.get("IS_SEND_MESSAGE"));
				friendList.add(friend);
			}
			friendAdapter = new SetFriendAdapter(SetFriendActivity.this, friendList);
			friendListView.setAdapter(friendAdapter);
			friendTitleText.setVisibility(View.VISIBLE);
			friendTitleText.setText("您共有"+friendList.size()+"位好友");
		} else if (result.equals("fail")) {
			MyToast.show(SetFriendActivity.this, hint, Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		Intent addFriendIntent = new Intent(SetFriendActivity.this,
				SetFriendAddActivity.class);
		startActivity(addFriendIntent);
	}
}
