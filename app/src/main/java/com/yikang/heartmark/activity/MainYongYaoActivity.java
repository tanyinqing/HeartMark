package com.yikang.heartmark.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.ViewPagerAdapter;
import com.yikang.heartmark.adapter.YongYaoAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.YaoDB;
import com.yikang.heartmark.database.YongYaoDB;
import com.yikang.heartmark.model.Yao;
import com.yikang.heartmark.model.YongYao;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.MenuBarView;
import com.yikang.heartmark.view.MenuBarView.OnMenuBarListener;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.view.YongYaoInfoView;
import com.yikang.heartmark.view.YongYaoRemindView;
import com.yuzhi.framework.util.ConnectionManager;

public class MainYongYaoActivity extends BaseActivity implements OnMenuBarListener,
		OnTopbarLeftButtonListener,OnTopbarRightButtonListener {
	private ListView yongYaoListView;
	private YongYaoAdapter yongYaoAdapter;
	private ArrayList<YongYao> yongYaoList = new ArrayList<YongYao>();
	private YaoDB yaoDB;
	public static MainYongYaoActivity instance;
	
	private MenuBarView yaoMenuBarView = null;
	private ViewPager yaoViewPager;
	private String uid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_yongyao);
		instance = this;
		init();
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		refreshListView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.yongyaoTopBar);
		topbar.setTopbarTitle(R.string.yongyao);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightButton(R.drawable.yongyao_add);
		topbar.setOnTopbarRightButtonListener(this);
		
		uid = ConstantUtil.getUid(MainYongYaoActivity.this);
		yaoDB = new YaoDB(MainYongYaoActivity.this);
		
		yaoViewPager = (ViewPager) findViewById(R.id.yaoViewPager);
		ArrayList<View> views = new ArrayList<View>();
		views.add(new YongYaoRemindView(this));
		views.add(new YongYaoInfoView(this));
		yaoViewPager.setAdapter(new ViewPagerAdapter(views));
		yaoViewPager.setOnPageChangeListener(new MyPageChangeListener());
		
		yaoMenuBarView = (MenuBarView) findViewById(R.id.yaoMenuBar);
		yaoMenuBarView.setOnMenuBarListener(this);
		yaoMenuBarView.setSelectedIndex(0);
		
		Map<String,String> params = new HashMap<String,String>();
		ConnectionManager.getInstance().send("CM01090WD00", "queryDrugInfo", params, "yaoSucessCallBack", this);
	}

	@SuppressWarnings("rawtypes")
	public void yaoSucessCallBack(Object data){
		ArrayList<Yao> yaoList = new ArrayList<Yao>();
		List resultMap = (ArrayList) data;
		if (resultMap == null || resultMap.size() == 0) {
			ShowUtil.showToast(MainYongYaoActivity.this,R.string.check_network_timeout);
		} else {
		for(int i =0;i<resultMap.size();i++){
			Yao yao =new Yao();
			Map temp = new HashMap();
			temp = (Map) resultMap.get(i);
			yao.uid = uid;
			yao.name = (String) temp.get("DRUG_NAME");
			yao.type = (String) temp.get("DRUG_TYPE");
		    yao.typeName = (String) temp.get("DRUG_TYPE_NAME");
		    yao.firm = (String) temp.get("DRUG_COMPANY");
		    yao.content = (String) temp.get("CONTENT");
		    yao.contentText = (String) temp.get("CONTENT_TEMP");
		    yaoList.add(yao);
		 }
		yaoDB.delete();
		//保存到数据库
		for(int i=0; i<yaoList.size(); i++){
			yaoDB.insertYao(yaoList.get(i));
		  }
    	}
	}
	
	public void refreshListView(){
		yongYaoList = new YongYaoDB(MainYongYaoActivity.this).getCeLingList(uid);
		yongYaoAdapter = new YongYaoAdapter(MainYongYaoActivity.this, yongYaoList);
		yongYaoListView.setAdapter(yongYaoAdapter);
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		Intent addIntent = new Intent(MainYongYaoActivity.this, YongYaoAddActivity.class);
		startActivity(addIntent);
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
				yaoMenuBarView.setSelectedIndex(arg0);
			}
		}

	@Override
	public void onMenuBarItemSelected(int menuIndex) {
		yaoViewPager.setCurrentItem(menuIndex);
	}
}
