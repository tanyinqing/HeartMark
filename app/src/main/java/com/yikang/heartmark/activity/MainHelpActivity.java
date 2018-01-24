package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainHelpActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainHelpTopBar);
		topbar.setTopbarTitle(R.string.help);
		topbar.setOnTopbarLeftButtonListener(this);
		
		RelativeLayout weightLayout = (RelativeLayout) findViewById(R.id.help_weight);
		RelativeLayout waterLayout = (RelativeLayout) findViewById(R.id.help_water);
		RelativeLayout heartLayout = (RelativeLayout) findViewById(R.id.help_heart);
		
		weightLayout.setOnClickListener(new MyViewOnclicklistener());
		waterLayout.setOnClickListener(new MyViewOnclicklistener());
		heartLayout.setOnClickListener(new MyViewOnclicklistener());
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.help_weight:
				Intent weightInten = new Intent(MainHelpActivity.this, HelpWeightActivity.class);
				startActivity(weightInten);
				break;
			case R.id.help_water:
				Intent waterInten = new Intent(MainHelpActivity.this, HelpWaterActivity.class);
				startActivity(waterInten);
				break;
			case R.id.help_heart:
				Intent heartInten = new Intent(MainHelpActivity.this, HelpHeartActivity.class);
				startActivity(heartInten);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
