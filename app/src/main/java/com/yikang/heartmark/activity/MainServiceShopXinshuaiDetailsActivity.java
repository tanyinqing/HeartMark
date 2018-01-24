package com.yikang.heartmark.activity;

import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopXinshuaiDetailsActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	
	private int intExtra = -1;
	private String url = "file:///android_asset/";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_xinshuai_details);
		intExtra  = getIntent().getIntExtra("main_service_shop_xinshuai", -1);
		
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopXinshuaiDetailsTopBar);
		topbar.setTopbarTitle(R.string.service_shop_details);
		topbar.setOnTopbarLeftButtonListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		WebView webView = (WebView) findViewById(R.id.main_service_shop_xinshuai_details);
		//支持内容重新布局  
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		 //将图片调整到适合
		webView.getSettings().setUseWideViewPort(true); 
		// 缩放至屏幕的大小
		webView.getSettings().setLoadWithOverviewMode(true); 		
		switch (intExtra) {
		case 1:		
			webView.loadUrl(url+"main_service_shop_xinshuai_img01.png");
			break;
		case 2:
			webView.loadUrl(url+"main_service_shop_xinshuai_img02.png");
			break;
		case 3:
			webView.loadUrl(url+"main_service_shop_xinshuai_img03.png");
			break;
		case 4:
			webView.loadUrl(url+"main_service_shop_xinshuai_img04.png");
			break;
		case 5:
			webView.loadUrl(url+"main_service_shop_xinshuai_img05.png");
			break;
		case 6:
			webView.loadUrl(url+"main_service_shop_xinshuai_img06.png");
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
