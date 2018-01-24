package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.ViewPagerAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.HuLiLifeView;
import com.yikang.heartmark.view.HuLiPatientView;
import com.yikang.heartmark.view.HuLiWeightTotalView;
import com.yikang.heartmark.view.MenuBarView;
import com.yikang.heartmark.view.MenuBarView.OnMenuBarListener;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;

public class MainHuLiActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnTopbarRightButtonListener,OnMenuBarListener{
	private MenuBarView huliMenuBarView = null;
	private ViewPager huliViewPager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_huli);
		
		init();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init(){
		TopBarView topbar = (TopBarView) findViewById(R.id.huliTopBar);
		topbar.setTopbarTitle(R.string.huli);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightText("评估");
		topbar.setOnTopbarRightButtonListener(this);
		
		huliViewPager = (ViewPager) findViewById(R.id.huliViewPager);
		ArrayList<View> views = new ArrayList<View>();
		views.add(new HuLiLifeView(this));
		views.add(new HuLiWeightTotalView(this));
		views.add(new HuLiPatientView(this));
		huliViewPager.setAdapter(new ViewPagerAdapter(views));
		huliViewPager.setOnPageChangeListener(new MyPageChangeListener());

		huliMenuBarView = (MenuBarView) findViewById(R.id.huliMenuBar);
		huliMenuBarView.setOnMenuBarListener(this);
		huliMenuBarView.setSelectedIndex(0);
	}
	

	// viewPager监听
	class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {

			}
			huliMenuBarView.setSelectedIndex(arg0);
		}
	}
	

	@Override
	public void onMenuBarItemSelected(int menuIndex) {
		huliViewPager.setCurrentItem(menuIndex);
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		Intent intent = new Intent(MainHuLiActivity.this, EvaluateActivity.class);
		startActivity(intent);
	}
}
