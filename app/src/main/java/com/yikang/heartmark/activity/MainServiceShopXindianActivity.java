package com.yikang.heartmark.activity;

import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopXindianActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	
	private String url = "file:///android_asset/main_service_shop_xindian_img.png";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_xindian);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopXindianTopBar);
		topbar.setTopbarTitle(R.string.service_shop_xindian);
		topbar.setOnTopbarLeftButtonListener(this);
		
		WebView webView = (WebView) findViewById(R.id.main_service_shop_xindian_imgLL);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setUseWideViewPort(true); 
		webView.getSettings().setLoadWithOverviewMode(true); 
		webView.loadUrl(url);
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
