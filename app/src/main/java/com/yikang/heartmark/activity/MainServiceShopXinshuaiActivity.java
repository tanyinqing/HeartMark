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

public class MainServiceShopXinshuaiActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_xinshuai);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopXinshuaiTopBar);
		topbar.setTopbarTitle(R.string.service_shop_xinshuai);
		topbar.setOnTopbarLeftButtonListener(this);
		findViewById(R.id.main_service_shop_xinshuai_icon01).setOnClickListener(this);
		findViewById(R.id.main_service_shop_xinshuai_icon02).setOnClickListener(this);
		findViewById(R.id.main_service_shop_xinshuai_icon03).setOnClickListener(this);
		findViewById(R.id.main_service_shop_xinshuai_icon04).setOnClickListener(this);
		findViewById(R.id.main_service_shop_xinshuai_icon05).setOnClickListener(this);
		findViewById(R.id.main_service_shop_xinshuai_icon06).setOnClickListener(this);
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_service_shop_xinshuai_icon01:
			if(ConnectUtil.isLogin(MainServiceShopXinshuaiActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, MainServiceShopXinshuaiDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xinshuai", 1);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xinshuai_icon02:
			if(ConnectUtil.isLogin(MainServiceShopXinshuaiActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, MainServiceShopXinshuaiDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xinshuai", 2);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xinshuai_icon03:
			if(ConnectUtil.isLogin(MainServiceShopXinshuaiActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, MainServiceShopXinshuaiDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xinshuai", 3);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xinshuai_icon04:
			if(ConnectUtil.isLogin(MainServiceShopXinshuaiActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, MainServiceShopXinshuaiDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xinshuai", 4);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xinshuai_icon05:
			if(ConnectUtil.isLogin(MainServiceShopXinshuaiActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, MainServiceShopXinshuaiDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xinshuai", 5);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xinshuai_icon06:
			if(ConnectUtil.isLogin(MainServiceShopXinshuaiActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, MainServiceShopXinshuaiDetailsActivity.class);
				helperIntent.putExtra("main_service_shop_xinshuai", 6);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopXinshuaiActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		default:
			break;
		}
	}
}
