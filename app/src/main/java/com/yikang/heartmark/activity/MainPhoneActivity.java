package com.yikang.heartmark.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainPhoneActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainPhoneTopBar);
		topbar.setTopbarTitle(R.string.phone);
		topbar.setOnTopbarLeftButtonListener(this);
		
		findViewById(R.id.main_phone_img).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.phone_number_ofzixun)));
                startActivity(intent);
			}
		});
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
