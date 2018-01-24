package com.yikang.heartmark.activity;

import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopZhinengActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	
	private String url = "file:///android_asset/main_service_shop_zhineng_img.png";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_zhineng);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopZhinengTopBar);
		topbar.setTopbarTitle(R.string.service_shop_zhineng);
		topbar.setOnTopbarLeftButtonListener(this);
        
		WebView webView = (WebView) findViewById(R.id.main_service_shop_zhineng_imgLL);
		//支持内容重新布局  
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		 //WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
		webView.getSettings().setUseWideViewPort(true); 
		webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		webView.loadUrl(url);
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
