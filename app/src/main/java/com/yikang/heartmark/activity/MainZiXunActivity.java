package com.yikang.heartmark.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.ViewPagerAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.PlaySoundUtil;
import com.yikang.heartmark.view.MenuBarView;
import com.yikang.heartmark.view.MenuBarView.OnMenuBarListener;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.ZiXunExpertView;
import com.yikang.heartmark.view.ZiXunInfoView;
import com.yikang.heartmark.view.ZiXunSportView;

@SuppressLint("HandlerLeak")
public class MainZiXunActivity extends BaseActivity implements OnMenuBarListener,
		OnTopbarLeftButtonListener {
	
	private MenuBarView zixunMenuBarView = null;
	private ViewPager zixunViewPager;
	private static MainZiXunActivity instance;
	public PlaySoundUtil playSound;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_zixun);
		instance = this;
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
		playSound.stopSound();
	}
	
	public static MainZiXunActivity getInstance(){
		return instance;
	}
	
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.zixunTopBar);
		topbar.setTopbarTitle(R.string.zixun);
		topbar.setOnTopbarLeftButtonListener(this);
		
		playSound = new PlaySoundUtil(MainZiXunActivity.this);
		
		initData();
	}

	private void initData() {
		zixunViewPager = (ViewPager) findViewById(R.id.zixunViewPager);
		
		ArrayList<View> views = new ArrayList<View>();
		views.add(new ZiXunInfoView(this));
		views.add(new ZiXunExpertView(this));
		views.add(new ZiXunSportView(this));
		
		//views.add(new YongYaoInfoView(this));
		zixunViewPager.setAdapter(new ViewPagerAdapter(views));
		zixunViewPager.setOnPageChangeListener(new MyPageChangeListener());

		zixunMenuBarView = (MenuBarView) findViewById(R.id.zixunMenuBar);
		zixunMenuBarView.setOnMenuBarListener(this);
		zixunMenuBarView.setSelectedIndex(0);
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
			zixunMenuBarView.setSelectedIndex(arg0);
		}
	}

	@Override
	public void onMenuBarItemSelected(int menuIndex) {
		zixunViewPager.setCurrentItem(menuIndex);
	}

	@Override
	public void onTopbarLeftButtonSelected() {
        finish();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(isTrue){
			initData();
			isTrue = false;
		}
	}
	private boolean isTrue = false;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				isTrue = true;
			}
		}	
	};
}
