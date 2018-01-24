package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.YiShiInfoAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.YiShiInfo;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class MainYiShiActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private ListView infoListView;
	private ImageView infoNoDataImg;
	private YiShiInfoAdapter infoAdapter;
	private ArrayList<YiShiInfo> infoList = new ArrayList<YiShiInfo>();
	private Button infoButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_yishi);
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		getInfo();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.yishiTopBar);
		topbar.setTopbarTitle(R.string.yishi);
		topbar.setOnTopbarLeftButtonListener(this);

		infoListView = (ListView) findViewById(R.id.yishi_info_listview);
		infoNoDataImg = (ImageView) findViewById(R.id.yishi_info_nodata_img);
		infoButton = (Button) findViewById(R.id.yishi_info_button);
		infoButton.setOnClickListener(buttonListener);
	}

	private void getInfo() {
		Map<String, String> params = new HashMap<String, String>();

		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN06070WD00",
					"queryConsultationInfo", params, "getInfoSucessCallBack",
					this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	@SuppressWarnings("rawtypes")
	public void getInfoSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MainYiShiActivity.this,
					R.string.check_network_timeout);
		} else { 
			List list = (List) resultMap.get("resultSet");
			if(list == null || list.size() == 0){
				infoListView.setVisibility(View.GONE);
				infoNoDataImg.setVisibility(View.VISIBLE);
				return;
			}else{
				infoListView.setVisibility(View.VISIBLE);
				infoNoDataImg.setVisibility(View.GONE);
			}
			infoList.clear();
			for (int i = 0; i < list.size(); i++) {
				YiShiInfo yishiInfo = new YiShiInfo();
				Map temp = new HashMap();
				temp = (Map) list.get(i);
				yishiInfo.uuid = (String) temp.get("UUID");
				yishiInfo.name = (String) temp.get("USERNAME");
				yishiInfo.time = (String) temp.get("CREATE_TIME");
				yishiInfo.title = (String) temp.get("CONTENT");
				yishiInfo.chatImage = String.valueOf(Double.valueOf(
						temp.get("IMAGE_COUNT").toString()).intValue());
				yishiInfo.chatReply = String.valueOf(Double.valueOf(
						temp.get("REPLY_COUNT").toString()).intValue());
				yishiInfo.chatAdd = String.valueOf(Double.valueOf(
						temp.get("ASK_COUNT").toString()).intValue());
				yishiInfo.isReply= String.valueOf(Double.valueOf(
						temp.get("RP_FLAG").toString()).intValue());
				if (!yishiInfo.isReply.equals("0")
						&& yishiInfo.isReply != null) {
					yishiInfo.reply = true;
				} else {
					yishiInfo.reply = false;
				}
				infoList.add(yishiInfo);
			}

			infoAdapter = new YiShiInfoAdapter(MainYiShiActivity.this, infoList);
			infoListView.setAdapter(infoAdapter);
		}
	}

	View.OnClickListener buttonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent chatIntent = new Intent(MainYiShiActivity.this,
					YiShiChatActivity.class);
			chatIntent.putExtra("chatType", "ask");
			startActivity(chatIntent);

		}
	};

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
