package com.yikang.heartmark.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.sharesdk.framework.ShareSDK;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.DBHelper;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.SharedPreferenceUtil;

//首页
public class WelcomeActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ShareSDK.initSDK(this);
		init();
	}
    @Override
	public void onResume() {
    	super.onResume();
    	//JPushInterface.onResume(WelcomeActivity.this);
    }
    @Override
	public void onPause() {
    	super.onPause();
    	//JPushInterface.onPause(WelcomeActivity.this);
    }
	@Override
	public void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	private void init() {
		SharedPreferenceUtil.setBoolean(WelcomeActivity.this, ConstantUtil.SHOW_UPDATE, true);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				boolean appFirstUse = SharedPreferenceUtil
						.getFirstUse(WelcomeActivity.this);

				Intent intent = new Intent();
				if (appFirstUse) {
					intent.setClass(WelcomeActivity.this, GuideActivity.class);
				} else {
					DBHelper.copyDB(WelcomeActivity.this);
					intent.setClass(WelcomeActivity.this, MyMainActivity.class);
				}
				startActivity(intent);

				finish();
			}
		}, 1000);
	}
}
