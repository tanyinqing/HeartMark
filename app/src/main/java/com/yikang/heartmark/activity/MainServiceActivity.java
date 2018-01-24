package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.example.heartmark.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnClickListener{
	
	@ViewInject(R.id.main_service_shop)
	private RelativeLayout serviceShop;
	@ViewInject(R.id.main_service_other)
	private RelativeLayout serviceOther;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceTopBar);
		topbar.setTopbarTitle(R.string.service);
		topbar.setOnTopbarLeftButtonListener(this);
		ViewUtils.inject(this);
		
		serviceShop.setOnClickListener(this);
		serviceOther.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_service_shop:   //健康商城
			if(ConnectUtil.isLogin(MainServiceActivity.this)){
				Intent helperIntent = new Intent(MainServiceActivity.this, MainServiceShopActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_other:  //更多应用
			if(ConnectUtil.isLogin(MainServiceActivity.this)){
				Intent helperIntent = new Intent(MainServiceActivity.this, MainServiceOtherActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
