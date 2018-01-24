package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.HelpHeartInfoAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.HelpHeartDB;
import com.yikang.heartmark.model.HelpHeart;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class HelpHeartInfoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener {
	
	private ListView listView;
	private TextView textViewMonth;
	private TextView heartHeight;
	private TextView heartNormal;
	private TextView heartLow;
	private ImageView imageViewLastMonth;
	private ImageView imageViewNextMonth;
	private HelpHeartDB heartDB;
	private String uid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_heart_info);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.helpHeartInfoTopBar);
		topbar.setTopbarTitle(R.string.help_heart_info);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightButton(R.drawable.refresh_white_img);
		topbar.setOnTopbarRightButtonListener(this);
		
		heartDB = new HelpHeartDB(HelpHeartInfoActivity.this);
		uid = ConstantUtil.getUid(HelpHeartInfoActivity.this);
		listView = (ListView) findViewById(R.id.huli_heart_info_listview);
		textViewMonth = (TextView) findViewById(R.id.tv_month);
		heartHeight = (TextView) findViewById(R.id.help_heart_info_height);
		heartNormal = (TextView) findViewById(R.id.help_heart_info_normal);
		heartLow = (TextView) findViewById(R.id.help_heart_info_low);
		imageViewLastMonth = (ImageView) findViewById(R.id.iv_lastmonth);
		imageViewNextMonth = (ImageView) findViewById(R.id.iv_nextmonth);
		imageViewLastMonth.setOnClickListener(new MyViewOnclicklistener());
		imageViewNextMonth.setOnClickListener(new MyViewOnclicklistener());

		//这个是获取当月的信息
		textViewMonth.setText(DateUtil.getNowDate(DateUtil.YEAR_MONTH));
		refreshDayListView();
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.iv_lastmonth:
				
				lastMonth();
				break;
			case R.id.iv_nextmonth:
				nextMonth();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 刷新月份天数的表格
	 */
	private void refreshDayListView() {
		int height = 0;
		int normal = 0;
		int low = 0;
		String date = textViewMonth.getText().toString().trim();
		List<HelpHeart> dataAddList = new ArrayList<HelpHeart>();
		dataAddList = new HelpHeartDB(HelpHeartInfoActivity.this)
				.getHeartListByMonth(date, ConstantUtil.getUid(HelpHeartInfoActivity.this));
		HelpHeartInfoAdapter myAdapter = new HelpHeartInfoAdapter(
				HelpHeartInfoActivity.this, dataAddList);
		listView.setAdapter(myAdapter);
		listView.setFooterDividersEnabled(false);
		
		for(int i=0; i<dataAddList.size(); i++){
			if(dataAddList.get(i).heart < 60){
				low ++;
			}else if(dataAddList.get(i).heart >= 60 && dataAddList.get(i).heart <= 100){
				normal ++;
			}else if(dataAddList.get(i).heart >= 100){
				height ++;
			}
		}
		
		heartHeight.setText(String.valueOf(height));
		heartNormal.setText(String.valueOf(normal));
		heartLow.setText(String.valueOf(low));
	}

	//这个是上一个月的信息
	private void lastMonth() {
		String date = textViewMonth.getText().toString().trim();
		String[] strs = date.split("-");
		//获取到年和月的信息
		int year = Integer.parseInt(strs[0]);
		int month = Integer.parseInt(strs[1]);
		if (month == 1) {
			year -= 1;
			month = 12;
		} else {
			month -= 1;
		}
		//这个是判断月份是个位数还是2位数
		if (month < 10) {
			date = year + "-" + "0" + month;
		} else {
			date = year + "-" + month;
		}
		textViewMonth.setText(date);
		refreshDayListView();
	}
	//这个是下一个月的信息
	private void nextMonth() {
		String date1 = textViewMonth.getText().toString().trim();
		String[] strs1 = date1.split("-");
		int year1 = Integer.parseInt(strs1[0]);
		int month1 = Integer.parseInt(strs1[1]);
		if (month1 == 12) {
			year1 += 1;
			month1 = 1;
		} else {
			month1 += 1;
		}
		if (month1 < 10) {
			date1 = year1 + "-" + "0" + month1;
		} else {
			date1 = year1 + "-" + month1;
		}
		textViewMonth.setText(date1);
		refreshDayListView();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getDataCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(HelpHeartInfoActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				//删除已经同步的数据
				heartDB.deleteBySync(uid);
				List<Map> list = (List) resultMap.get("nurseInfoList");
				for (int i = 0; i < list.size(); i++) {
					//没有数据，跳出本次循环
					if(list.get(i).get("HEART_RATE") == null){
						continue;
					}
					
					HelpHeart heart = new HelpHeart();
					heart.uid = uid;
					heart.sync = ConstantUtil.SYNC_YES;
					String celingTime = (String) list.get(i).get("NURSE_DATE");
					heart.date = celingTime.substring(0, 10);
					heart.dateMonth = celingTime.substring(0, 7);
					heart.day = celingTime.substring(8, 10);
					heart.timeMill = DateUtil.getLongOfDayTime(celingTime);
				  //heart.time = celingTime.substring(11, 16);
					heart.heart = Double.valueOf(list.get(i).get("HEART_RATE").toString()).intValue();
					heartDB.insert(heart);
				}
				//刷新列表
				refreshDayListView();
				ShowUtil.showToast(HelpHeartInfoActivity.this,
						R.string.sync_success);
			} else if (result.equals("fail")) {
				ShowUtil.showToast(HelpHeartInfoActivity.this,
						R.string.sync_fail);
			}

		}
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(HelpHeartInfoActivity.this)) {
			ConnectionManager.getInstance().send("FN11060WD00",
					"queryNurseInfo", params, "getDataCallBack",
					HelpHeartInfoActivity.this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
}
