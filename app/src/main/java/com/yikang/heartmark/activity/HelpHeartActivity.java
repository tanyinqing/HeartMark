package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.HelpHeartMonthView;
import com.yikang.heartmark.view.HelpHeartWeekView;
import com.yikang.heartmark.view.MenuBarView;
import com.yikang.heartmark.view.MenuBarView.OnMenuBarListener;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class HelpHeartActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnMenuBarListener {
	private MenuBarView menuBarView;
	private ViewFlipper flipperView;
	private HelpHeartWeekView weekView;
	private HelpHeartMonthView monthView;
	private TextView weekText;
	private TextView monthText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_heart);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.helpHeartTopBar);
		topbar.setTopbarTitle(R.string.help_heart);
		topbar.setOnTopbarLeftButtonListener(this);
		
		weekText = (TextView) findViewById(R.id.help_week_text);
		monthText = (TextView) findViewById(R.id.help_month_text);

		flipperView = (ViewFlipper) findViewById(R.id.help_weight_plipper);
		weekView = (HelpHeartWeekView) findViewById(R.id.weekView);
		monthView = (HelpHeartMonthView) findViewById(R.id.monthView);
		menuBarView = (MenuBarView) findViewById(R.id.help_weight_menubar);
		//把Activity注册进接口
		menuBarView.setOnMenuBarListener(this);
		//默认选中第一个控件
		menuBarView.setSelectedIndex(0);  

		Button detail = (Button) findViewById(R.id.help_weight_detail);
		detail.setOnClickListener(listener);
	}

	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HelpHeartActivity.this,
					HelpHeartInfoActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override  //flipperView 这个是viewpage
	public void onMenuBarItemSelected(int menuIndex) {
		flipperView.removeAllViews();
		switch (menuIndex) {
		case 0:
			
			flipperView.addView(weekView);
			weekView.showCurve();
			weekText.setTextColor(getResources().getColor(R.color.white));
			monthText.setTextColor(getResources().getColor(R.color.app_color));
			break;
		case 1:
			flipperView.addView(monthView);
			monthView.showCurve();
			weekText.setTextColor(getResources().getColor(R.color.app_color));
			monthText.setTextColor(getResources().getColor(R.color.white));
			break;
		}

	}
}
