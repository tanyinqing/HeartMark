package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.ceshi.PromptManager;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;

public class MainPingGuActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnTopbarRightButtonListener{
	private TextView textViewCount;
	public static final String INFO_STAGE ="info_stage";  //阶段评估
	public static final String INFO_FORTHWITH ="info_FORTHWITH";   //即时评估
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pinggu);			
		init();
		//PromptManager.showToastTest(this,"这个是有主页的 病情评估 跳转过来的页面MainPingGuActivity");
	
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.pingguTopBar);
		topbar.setTopbarTitle(R.string.pinggu);
		topbar.setOnTopbarLeftButtonListener(this);
		//topbar.setRightText("测试结果");
		//topbar.setOnTopbarRightButtonListener(this);
		
		textViewCount = (TextView) findViewById(R.id.pinggu_count);
		Button buttonStart = (Button) findViewById(R.id.pinggu_start_stage);
		Button buttonForthwith = (Button) findViewById(R.id.pinggu_start_forthwith);
		RelativeLayout layoutHistory = (RelativeLayout) findViewById(R.id.pinggu_history_layout);
		buttonStart.setOnClickListener(new MyViewOnclicklistener());
		buttonForthwith.setOnClickListener(new MyViewOnclicklistener());
		layoutHistory.setOnClickListener(new MyViewOnclicklistener());
		//获取有多少人参加了评估
		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN11070WD00",
					"countAssessmentNumber", params, "getCountCallBack", this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
	
	//从服务端取得所有评估次数  
	@SuppressWarnings({ "rawtypes"})
	public void getCountCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MainPingGuActivity.this,R.string.check_network_timeout);
		} else {
			Map countMap = (Map) resultMap.get("body");
//			intValue()是把Integer对象类型变成int的基础数据类型；
			int count = Double.valueOf(countMap.get("COUNT").toString()).intValue();
			// 已有%d人进行评估  按照这个格式 格式字符串
			textViewCount.setText(String.format(getResources().getString(R.string.pinggu_count),count));
		}
	}
	
	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			//阶段评估
			case R.id.pinggu_start_stage:
				Map<String, String> params = new HashMap<String, String>();
				if (ConnectUtil.isConnect(AppContext.context)) {
					ConnectionManager.getInstance().send("FN11070WD00",
							"assessmentConditions", params, "getZiGeCallBack", MainPingGuActivity.this);
					handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
				} else {
					ShowUtil.showToast(AppContext.context, R.string.check_network);
				}
				break;
				//即使评估
			case R.id.pinggu_start_forthwith:
				if (ConnectUtil.isConnect(AppContext.context)) {
					Intent startIntent = new Intent(MainPingGuActivity.this, PingGuInfoActivity.class);
					startIntent.putExtra("infoFrom", INFO_FORTHWITH);
					startActivity(startIntent);
				} else {
					ShowUtil.showToast(AppContext.context, R.string.check_network);
				}
				break;
				//历史评估
			case R.id.pinggu_history_layout:
				if (ConnectUtil.isConnect(AppContext.context)) {
					Intent historyIntent = new Intent(MainPingGuActivity.this, PingGuHistoryActivity.class);
					startActivity(historyIntent);
				} else {
					ShowUtil.showToast(AppContext.context, R.string.check_network);
				}
				break;
			default:
				break;
			}
		}
	}
	
	//获取是否有资格进行评估  阶段评估时显示的内容
	@SuppressWarnings({ "rawtypes"})
	public void getZiGeCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MainPingGuActivity.this,R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if(result.equals("success")){
				Intent startIntent = new Intent(MainPingGuActivity.this, PingGuInfoActivity.class);
				//意思是这是阶段评估跳转过去的
				startIntent.putExtra("infoFrom", INFO_STAGE);
				startActivity(startIntent);
			}else if(result.equals("fail")){
				//ShowUtil.showToast(MainPingGuActivity.this, (String) resultMap.get("body"));
				showPingGuDialog();
			}
		}
	}
	
	private void showPingGuDialog() {
		MyDialog dialog = new MyDialog(MainPingGuActivity.this)
				.setTitle(R.string.pinggu_title)
				.setMessage(R.string.pinggu_msg)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent celingIntent = new Intent(MainPingGuActivity.this, MainCeLingActivity.class);
						celingIntent.putExtra(ConstantUtil.CELING_FROM, "main");
						startActivity(celingIntent);
						finish();
					}
				})
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent startIntent = new Intent(MainPingGuActivity.this, PingGuInfoActivity.class);
						startIntent.putExtra("infoFrom", INFO_STAGE);
						startActivity(startIntent);
					}
				});

		dialog.create(null);
		dialog.showMyDialog();
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		Intent resultIntent = new Intent(MainPingGuActivity.this, PingGuStageActivity.class);
		startActivity(resultIntent);
	}
}
