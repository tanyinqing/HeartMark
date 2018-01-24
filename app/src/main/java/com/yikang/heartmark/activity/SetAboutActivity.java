package com.yikang.heartmark.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
/**
 * 
* <p>Title: SetAboutActivity</p>
* <p>Description: </p>
* @author lizhengjun
* @Email: lizhengjunkijojo@163.com
* @date   2015-3-10
 */
@SuppressLint("SimpleDateFormat")
public class SetAboutActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private String stringExtra;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_about);
		stringExtra = getIntent().getStringExtra("set");
		init();
	}
	private void init() {
		try {
			TopBarView topbar = (TopBarView) findViewById(R.id.setAboutBarView);
			topbar.setOnTopbarLeftButtonListener(this);
			WebView webView = (WebView) findViewById(R.id.set_about_context);
			if("about".equals(stringExtra)){
				webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				webView.getSettings().setUseWideViewPort(true); 
				webView.getSettings().setLoadWithOverviewMode(true); 				
				topbar.setTopbarTitle(R.string.set_about);
				webView.loadUrl("file:///android_asset/about.png"); 
			}else if("limit".equals(stringExtra)){
				//如果是免责声明 就读取asset文件夹中的html文本，图片也在里面
				webView.getSettings().setJavaScriptEnabled(false);
				webView.getSettings().setSupportZoom(true);
				webView.getSettings().setBuiltInZoomControls(true);
				topbar.setTopbarTitle(R.string.set_limit);
				webView.loadUrl("file:///android_asset/html01.html"); 
			}
		} catch (Exception e) {
		}
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
