package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.heartmark.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnClickListener{
	
	@ViewInject(R.id.main_service_shop_consultation)
	private LinearLayout shopConsultation;
	@ViewInject(R.id.main_service_shop_xinshuai)
	private LinearLayout shopXinshuai;
	@ViewInject(R.id.main_service_shop_xuetang)
	private LinearLayout shopXuetang;
	@ViewInject(R.id.main_service_shop_xindian)
	private LinearLayout shopXindian;
	@ViewInject(R.id.main_service_shop_zhinenghujiao)
	private LinearLayout shopZhinenghujiao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopTopBar);
		topbar.setTopbarTitle(R.string.service_shop);
		topbar.setOnTopbarLeftButtonListener(this);
		ViewUtils.inject(this);
		shopConsultation.setOnClickListener(this);
		shopXinshuai.setOnClickListener(this);
		shopXuetang.setOnClickListener(this);
		shopXindian.setOnClickListener(this);
		shopZhinenghujiao.setOnClickListener(this);
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_service_shop_consultation:
			if(ConnectUtil.isLogin(MainServiceShopActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopActivity.this, MainServiceShopHuizhenActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xinshuai:
			if(ConnectUtil.isLogin(MainServiceShopActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopActivity.this, MainServiceShopXinshuaiActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xuetang:
			if(ConnectUtil.isLogin(MainServiceShopActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopActivity.this, MainServiceShopXuetangActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_xindian:
			if(ConnectUtil.isLogin(MainServiceShopActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopActivity.this, MainServiceShopXindianActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		case R.id.main_service_shop_zhinenghujiao:
			if(ConnectUtil.isLogin(MainServiceShopActivity.this)){
				Intent helperIntent = new Intent(MainServiceShopActivity.this, MainServiceShopZhinengActivity.class);
				startActivity(helperIntent);
			}else{
				Intent helperIntent = new Intent(MainServiceShopActivity.this, LoginActivity.class);
				startActivity(helperIntent);
			}
			break;
		default:
			break;
		}
	}
}
