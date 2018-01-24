package com.yikang.heartmark.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sharesdk.ceshi.PromptManager;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.PingGuHistoryAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.PingGuResult;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class PingGuHistoryActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private ListView listView;
	private ArrayList<PingGuResult> pingGuList;//评估结果对象的集合

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinggu_history);
		init();
		//PromptManager.showToastTest(this,"这个是从主页的 病情评估 跳转过来的历史评估的页面PingGuHistoryActivity");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.pingguHistoryTopBar);
		topbar.setTopbarTitle(R.string.pinggu_history);
		topbar.setOnTopbarLeftButtonListener(this);

		listView = (ListView) findViewById(R.id.pinggu_history_listView);
		pingGuList = new ArrayList<PingGuResult>();
		
		listView.setOnItemClickListener(itemClickListener);

		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN11070WD00",
					"queryAssessmentResultInfo", params, "getHistoryCallBack",
					this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	// 取得所有评估次数
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getHistoryCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(PingGuHistoryActivity.this,R.string.check_network_timeout);
		} else {
			ArrayList<Map<String, String>> resultList = (ArrayList<Map<String, String>>) resultMap.get("body");
			PingGuHistoryAdapter historyAdapter = new PingGuHistoryAdapter(PingGuHistoryActivity.this, resultList);
			listView.setAdapter(historyAdapter);

			for (int i = 0; i < resultList.size(); i++) {
				PingGuResult pingguResult = new PingGuResult();
				String type = (String) resultList.get(i).get("ASSESSMENT_TYPE");
				pingguResult.type = (String) resultList.get(i).get("ASSESSMENT_TYPE");
				if(type.equals("0001")){
					pingguResult.risk = (String) resultList.get(i).get("RESULT_IM");
				}else if(type.equals("0002")){
					//对数字的格式化
					DecimalFormat df = new DecimalFormat("#0.00");
					pingguResult.result = (String) resultList.get(i).get("LAST_MONITOR_DATA");
					pingguResult.hint = (String) resultList.get(i).get("RESULT_TITLE");
					Double proValue = Double.valueOf(resultList.get(i).get("RESULT_PRO_VALUE").toString());
					Double bnpValue = 0.0;
					if(resultList.get(i).get("RESULT_BNP_VALUE") != null && !resultList.get(i).get("RESULT_BNP_VALUE").equals("")){
						bnpValue = Double.valueOf(resultList.get(i).get("RESULT_BNP_VALUE").toString());
					}
					Double value = (bnpValue*0.6+proValue/0.6*0.4);
					pingguResult.per = df.format(value);
					pingguResult.risk = (String) resultList.get(i).get("RESULT_BNP");
					pingguResult.stable = (String) resultList.get(i).get("RESULT_STABLE");
					pingguResult.huli = (String) resultList.get(i).get("RESULT_NURSE");
					pingguResult.from = PingGuResult.HISTORY;
				}
				//这个的目的是建立一个数据链表  为单击做准备
				pingGuList.add(pingguResult);
			}
		}
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String type = pingGuList.get(position).type;
			if(type.equals("0001")){
				Intent detailIntent = new Intent(PingGuHistoryActivity.this, PingGuForthwithActivity.class);
				detailIntent.putExtra("pingguResult",  pingGuList.get(position).risk);
				startActivity(detailIntent);
			}else if(type.equals("0002")){
				Intent detailIntent = new Intent(PingGuHistoryActivity.this, PingGuStageActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("pingguResult", pingGuList.get(position));
				detailIntent.putExtras(bundle);
				startActivity(detailIntent);
			}
		}
	};
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
