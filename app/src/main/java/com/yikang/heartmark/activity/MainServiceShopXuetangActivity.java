package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopXuetangActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_xuetang);
		init();
		findViewById(R.id.main_service_shop_xuetang_icon01).setOnClickListener(this);
		findViewById(R.id.main_service_shop_xuetang_icon02).setOnClickListener(this);
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopXuetangTopBar);
		topbar.setTopbarTitle(R.string.service_shop_xuetang);
		topbar.setOnTopbarLeftButtonListener(this);
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_service_shop_xuetang_icon01:
			if(ConnectUtil.isLogin(MainServiceShopXuetangActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXuetangActivity.this, MainServiceShopXuetangDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xuetang", 1);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXuetangActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xuetang_icon02:
			if(ConnectUtil.isLogin(MainServiceShopXuetangActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXuetangActivity.this, MainServiceShopXuetangDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xuetang", 2);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXuetangActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		default:
			break;
		}
	}
}
