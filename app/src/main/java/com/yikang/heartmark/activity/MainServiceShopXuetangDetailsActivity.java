package com.yikang.heartmark.activity;

import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopXuetangDetailsActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	
	private int intExtra = -1;
	private String url = "file:///android_asset/";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_xuetang_detail);
		intExtra  = getIntent().getIntExtra("main_service_shop_xuetang", -1);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopXuetangDetailsTopBar);
    	topbar.setTopbarTitle(R.string.service_shop_details);
		topbar.setOnTopbarLeftButtonListener(this);
		
		WebView webView = (WebView) findViewById(R.id.main_service_shop_xuetang_details);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setUseWideViewPort(true); 
		webView.getSettings().setLoadWithOverviewMode(true); 
		
		switch (intExtra) {
		case 1:
			webView.loadUrl(url+"main_service_shop_xuetang_img01.png");
			break;
		case 2:
			webView.loadUrl(url+"main_service_shop_xuetang_img02.png");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		intExtra = -1;
	}
}
