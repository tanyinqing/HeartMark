package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.ViewPagerAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.NewFriendView;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainNewsActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
/*	private MenuBarView newsMenuBarView = null;*/
	private ViewPager newsViewPager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.newsTopBar);
		topbar.setTopbarTitle(R.string.news_friend);
		topbar.setOnTopbarLeftButtonListener(this);

		newsViewPager = (ViewPager) findViewById(R.id.newsViewPager);
		ArrayList<View> views = new ArrayList<View>();
		//views.add(new NewSystemView(this));
		views.add(new NewFriendView(this));
		newsViewPager.setAdapter(new ViewPagerAdapter(views));
/*		newsViewPager.setOnPageChangeListener(new MyPageChangeListener());

		newsMenuBarView = (MenuBarView) findViewById(R.id.newsMenuBar);
		newsMenuBarView.setOnMenuBarListener(this);
		newsMenuBarView.setSelectedIndex(0);*/
	}
/*
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
			newsMenuBarView.setSelectedIndex(arg0);
		}
	}

	@Override
	public void onMenuBarItemSelected(int menuIndex) {
		newsViewPager.setCurrentItem(menuIndex);
	}*/
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

}
