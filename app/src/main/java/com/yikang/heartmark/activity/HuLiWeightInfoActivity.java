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
import com.yikang.heartmark.adapter.HuliWeightInfoAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.HuLiWeightDB;
import com.yikang.heartmark.model.HuLiWeight;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class HuLiWeightInfoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener , OnTopbarRightButtonListener{
	private ListView listView;
	private TextView textViewMonth;
	private ImageView imageViewLastMonth;
	private ImageView imageViewNextMonth;
	private HuLiWeightDB weightDB;
	private String uid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huli_weight_info);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.huliWeightInfoTopBar);
		topbar.setTopbarTitle(R.string.huli_weight_info);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightButton(R.drawable.refresh_white_img);
		topbar.setOnTopbarRightButtonListener(this);

		weightDB = new HuLiWeightDB(HuLiWeightInfoActivity.this);
		uid = ConstantUtil.getUid(HuLiWeightInfoActivity.this);
		listView = (ListView) findViewById(R.id.huli_weight_info_listview);
		textViewMonth = (TextView) findViewById(R.id.tv_month);
		imageViewLastMonth = (ImageView) findViewById(R.id.iv_lastmonth);
		imageViewNextMonth = (ImageView) findViewById(R.id.iv_nextmonth);
		imageViewLastMonth.setOnClickListener(new MyViewOnclicklistener());
		imageViewNextMonth.setOnClickListener(new MyViewOnclicklistener());

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
		String date = textViewMonth.getText().toString().trim();
		String[] strs = date.split("-");
		int daysOfMonth = DateUtil.getDaysOfMonth(Integer.parseInt(strs[0]),
				Integer.parseInt(strs[1]));
		List<HuLiWeight> dataAddList = new ArrayList<HuLiWeight>();
		dataAddList = weightDB.getHuliListByMonth(date,uid);
		HuliWeightInfoAdapter myAdapter = new HuliWeightInfoAdapter(
				HuLiWeightInfoActivity.this, dataAddList, daysOfMonth);
		listView.setAdapter(myAdapter);
		listView.setFooterDividersEnabled(false);
	}

	private void lastMonth() {
		String date = textViewMonth.getText().toString().trim();
		String[] strs = date.split("-");
		int year = Integer.parseInt(strs[0]);
		int month = Integer.parseInt(strs[1]);
		if (month == 1) {
			year -= 1;
			month = 12;
		} else {
			month -= 1;
		}
		if (month < 10) {
			date = year + "-" + "0" + month;
		} else {
			date = year + "-" + month;
		}
		textViewMonth.setText(date);
		refreshDayListView();
	}

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
			ShowUtil.showToast(HuLiWeightInfoActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				weightDB.deleteBySync(uid);
				List<Map> list = (List) resultMap.get("nurseInfoList");
				for (int i = 0; i < list.size(); i++) {
					//没有数据，跳出本次循环
					if(list.get(i).get("WEIGHT") == null){
						continue;
					}
					
					HuLiWeight weight = new HuLiWeight();
					weight.uid = uid;
					weight.sync = ConstantUtil.SYNC_YES;
					String celingTime = (String) list.get(i).get("NURSE_DATE");
					weight.date = celingTime.substring(0, 10);
					weight.dateMonth = celingTime.substring(0, 7);
					weight.day = celingTime.substring(8, 10);
					weight.timeMill = DateUtil.getLongOfDayTime(celingTime);
				//	weight.time = celingTime.substring(11, 16);
					double value = Double.valueOf(list.get(i).get("WEIGHT").toString());
					int thisWeight = Double.valueOf(value*10).intValue();
					weight.thisWeight = thisWeight;
                    weight.baseWeight = Integer.valueOf(SharedPreferenceUtil.getString
                    		(HuLiWeightInfoActivity.this, ConstantUtil.USER_WEIGHT));
                    weight.diff = Math.abs(weight.thisWeight - weight.baseWeight);
					weightDB.insert(weight);
				}
				
				refreshDayListView();
				ShowUtil.showToast(HuLiWeightInfoActivity.this,
						R.string.sync_success);
			} else if (result.equals("fail")) {
				ShowUtil.showToast(HuLiWeightInfoActivity.this,
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
		if (ConnectUtil.isConnect(HuLiWeightInfoActivity.this)) {
			ConnectionManager.getInstance().send("FN11060WD00",
					"queryNurseInfo", params, "getDataCallBack",
					HuLiWeightInfoActivity.this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}		
	}
}
