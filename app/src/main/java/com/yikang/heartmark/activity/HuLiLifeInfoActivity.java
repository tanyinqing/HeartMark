package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.HuliLifeInfoAdapter;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.HuLiLifeDB;
import com.yikang.heartmark.model.HuLiLife;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class HuLiLifeInfoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private ListView listView;
	private TextView textViewMonth;
	private ImageView imageViewLastMonth;
	private ImageView imageViewNextMonth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huli_life_info);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.huliLifeInfoTopBar);
		topbar.setTopbarTitle(R.string.huli_life);
		topbar.setOnTopbarLeftButtonListener(this);

		listView = (ListView) findViewById(R.id.huli_life_info_listview);
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
		List<HuLiLife> dataAddList = new ArrayList<HuLiLife>();
		dataAddList = new HuLiLifeDB(HuLiLifeInfoActivity.this)
				.getHuliListByDateMonth(date, ConstantUtil.getUid(HuLiLifeInfoActivity.this));
		HuliLifeInfoAdapter myAdapter = new HuliLifeInfoAdapter(
				HuLiLifeInfoActivity.this, dataAddList, daysOfMonth);
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

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
