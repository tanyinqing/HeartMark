package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.ZiXunNewAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class SetCollectionActivity extends BaseActivity implements
		OnTopbarLeftButtonListener{
	private ListView zixunListView;
	private ZiXunNewAdapter zixunAdapter;
	private ArrayList<ZiXun> infoList = new ArrayList<ZiXun>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_collection);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setCollectionTopBar);
		topbar.setTopbarTitle(R.string.set_collect);
		topbar.setOnTopbarLeftButtonListener(this);

		zixunListView = (ListView) findViewById(R.id.zixun_info_listview);
		zixunListView.setOnItemClickListener(itemClickListener);

		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN11050WL00",
					"queryNewsCollect", params, "newSucessCallBack",this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ZiXun zixun = infoList.get(position);
			Intent detailIntent = new Intent(SetCollectionActivity.this,
					ZiXunDetailGetActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("newItem", zixun);
			detailIntent.putExtras(bundle);
			startActivity(detailIntent);
		}
	};

	// 首次请求或者刷新回调
	@SuppressWarnings("rawtypes")
	public void newSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(SetCollectionActivity.this,
					R.string.check_network_timeout);
		} else {
			List list = (List) resultMap.get("body");
			infoList.clear();
			for (int i = 0; i < list.size(); i++) {
				ZiXun zixunInfo = new ZiXun();
				Map temp = new HashMap();
				temp = (Map) list.get(i);
				zixunInfo.newId = (String) temp.get("NEWS_ID");
				zixunInfo.title = (String) temp.get("TITLE");
				zixunInfo.image = (String) temp.get("ICON_URL");
				zixunInfo.time = (String) temp.get("CREATE_TIME");
				zixunInfo.contentRead = (String) temp.get("CONTENT");
				zixunInfo.good = String.valueOf(Double.valueOf(
						temp.get("PRAISE_COUNT").toString()).intValue());
				zixunInfo.type = ZiXun.TYPE_ZIXUN;
				infoList.add(zixunInfo);
			}
			zixunAdapter = new ZiXunNewAdapter(SetCollectionActivity.this,
					infoList);
			zixunListView.setAdapter(zixunAdapter);
		}
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
