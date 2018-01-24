package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.CeLingDataAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.CeLingDB;
import com.yikang.heartmark.model.CeLingData;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class CeLingDataActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener {

	private ListView dataListView;
	private CeLingDataAdapter dataAdapter;
	private ArrayList<CeLingData> dataList = new ArrayList<CeLingData>();
	private String[] states = null;
	private CeLingDB ceLingDB;
	private String uid = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_celing_data);

		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.celingDataTopBar);
		topbar.setTopbarTitle(R.string.celing_data);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightButton(R.drawable.refresh_white_img);
		topbar.setOnTopbarRightButtonListener(this);

		ceLingDB = new CeLingDB(CeLingDataActivity.this);
		states = getResources().getStringArray(R.array.celing_state);
		uid = ConstantUtil.getUid(CeLingDataActivity.this);
		dataListView = (ListView) findViewById(R.id.celing_data_listview);
		
		refreshView();
	}

	private void refreshView() {
		dataList = ceLingDB.getCeLingListByUid(ConstantUtil.getUid(CeLingDataActivity.this));
		dataAdapter = new CeLingDataAdapter(CeLingDataActivity.this, dataList);
		dataListView.setAdapter(dataAdapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getDataCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(CeLingDataActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				ceLingDB.deleteBySync(uid);
				List<Map> list = (List) resultMap.get("monitorDataList");
				for (int i = 0; i < list.size(); i++) {
					CeLingData ceLing = new CeLingData();
					ceLing.uid = uid;
					ceLing.sync = ConstantUtil.SYNC_YES;
					String celingTime = (String) list.get(i).get("CREATE_TIME");
					ceLing.date = celingTime.substring(0, 10);
					ceLing.dateMonth = celingTime.substring(0, 7);
					ceLing.day = celingTime.substring(8, 11);
					ceLing.time = celingTime.substring(11, 16);
					ceLing.type = (String) list.get(i).get("MONITOR_TYPE_NAME");
					ceLing.result = (String) list.get(i).get("MONITOR_RESULT");

					List<Map> stateList = (List) list.get(i).get(
							"monitorStatusDataList");
					
					String state = "";
					if(stateList!=null&&stateList.size()>0){
						for (int k = 0; k < stateList.size(); k++) {
							int count = Integer.valueOf((String) stateList.get(k)
									.get("STATUS_CODE")) - 1;
							state += states[count] + ",";
						}
						if (state != null && state.length() > 1) {
							state = state.substring(0, state.length() - 1);
						}
					}
					
					ceLing.state = state;
					ceLing.diag = (String) list.get(i).get("DIAGNOIS");
					ceLingDB.insertCeLingData(ceLing);
				}
				
				refreshView();
				//ShowUtil.showToast(CeLingDataActivity.this,R.string.sync_success);
			} else if (result.equals("fail")) {
				ShowUtil.showToast(CeLingDataActivity.this,
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
		if (ConnectUtil.isConnect(CeLingDataActivity.this)) {
			ConnectionManager.getInstance().send("FN06060WD00",
					"queryMonitorDataList", params, "getDataCallBack",
					CeLingDataActivity.this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
}
