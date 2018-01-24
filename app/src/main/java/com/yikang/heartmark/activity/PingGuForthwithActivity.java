package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.sharesdk.ceshi.PromptManager;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class PingGuForthwithActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinggu_forthwith_result);
		init();
		//PromptManager.showToastTest(this,"这个是有评估完成后  跳转过来的结果页面PingGuForthwithActivity");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.pingguForthwithResultTopBar);
		topbar.setTopbarTitle(R.string.evaluate);
		topbar.setOnTopbarLeftButtonListener(this);
		
		TextView resultText = (TextView) findViewById(R.id.pingguForthwithResult);
		
		Intent intent = getIntent();
		resultText.setText(intent.getStringExtra("pingguResult"));
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
