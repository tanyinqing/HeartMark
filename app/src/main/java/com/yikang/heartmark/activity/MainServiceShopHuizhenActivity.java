package com.yikang.heartmark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.lidroid.xutils.BitmapUtils;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.Expert;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class MainServiceShopHuizhenActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnClickListener{
	
	private List<Expert> experts = new ArrayList<Expert>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_huizhen);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopHuizhenTopBar);
		topbar.setTopbarTitle(R.string.service_shop_huizhen);
		topbar.setOnTopbarLeftButtonListener(this);
		
		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(MainServiceShopHuizhenActivity.this)) {
			ConnectionManager.getInstance().send("FN11090WD00",
					"queryExpertsInfo", params, "getDataCallBack",
					MainServiceShopHuizhenActivity.this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.huizhen001:
		
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getDataCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MainServiceShopHuizhenActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				List<Map> list = (List) resultMap.get("body");
				//获得每一个专家的信息
				for (int i = 0; i < list.size(); i++) {
					Expert expert = new Expert();
					expert.BE_GOOD_AT = (String) list.get(i).get("BE_GOOD_AT");
					expert.CREATE_TIME = (String) list.get(i).get("CREATE_TIME");
					expert.DETAILED_INFO = (String) list.get(i).get("DETAILED_INFO");
					expert.EXPERT_IMG_URL = (String) list.get(i).get("EXPERT_IMG_URL");
					expert.EXPERT_NAME = (String) list.get(i).get("EXPERT_NAME");
					expert.POSTION = (String) list.get(i).get("POSTION");
					expert.RESERVATION_TEL = (String) list.get(i).get("RESERVATION_TEL");
					expert.UUID = (String) list.get(i).get("UUID");
					expert.WORK_UNIT = (String) list.get(i).get("WORK_UNIT");
					experts.add(expert);
				}
			}
			ListView huizhen = (ListView) findViewById(R.id.huizhen001);
			ExpertAdapter adapter = new ExpertAdapter(this,experts);
			huizhen.setAdapter(adapter);
		}
	}
	
	class ExpertAdapter extends BaseAdapter{
		
		public List<Expert> arrayList = null;
		private LayoutInflater layoutInflater = null;
		public ExpertAdapter(Context c, List<Expert> experts) {
			layoutInflater = LayoutInflater.from(c);
			arrayList = experts;
		}

		@Override
		public int getCount() {
			int size = 0;
			if (arrayList != null && arrayList.size() > 0)
				size = arrayList.size();
			return size;
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (arrayList == null || arrayList.size() == 0)
				return null;
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.activity_service_shop_huizhen_item, null);

				holder = new ViewHolder();
				holder.huizhen_item = (LinearLayout) convertView.findViewById(R.id.huizhen001);
				holder.huizhen_item_img = (ImageView) convertView.findViewById(R.id.huizhen_item_img);
				holder.huizhen_item_name =  (TextView) convertView.findViewById(R.id.huizhen_item_name);
				holder.huizhen_item_position = (TextView) convertView.findViewById(R.id.huizhen_item_position);
				holder.huizhen_item_cando = (TextView) convertView.findViewById(R.id.huizhen_item_cando);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Expert item = arrayList.get(position);
			BitmapUtils bitmapUtils = new BitmapUtils(MainServiceShopHuizhenActivity.this);
			bitmapUtils.display(holder.huizhen_item_img, ConnectUtil.HOST_URL + item.EXPERT_IMG_URL);
			holder.huizhen_item_name.setText(item.EXPERT_NAME+ "  " +item.POSTION);
			holder.huizhen_item_position.setText(item.WORK_UNIT);
			holder.huizhen_item_cando.setText("擅长："+item.BE_GOOD_AT);
			
			holder.huizhen_item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(ConnectUtil.isLogin(MainServiceShopHuizhenActivity.this)){
						Intent helperIntent = new Intent(MainServiceShopHuizhenActivity.this, MainServiceShopHuizhenDetailsActivity.class);
						Bundle value = new Bundle();
						value.putSerializable("huizhen_item", item);
						helperIntent.putExtra("huizhen", value);
						startActivity(helperIntent);
					}else{
						Intent helperIntent = new Intent(MainServiceShopHuizhenActivity.this, LoginActivity.class);
						startActivity(helperIntent);
					}
				}
			});
			return convertView;
		}
	}
	static class ViewHolder {
		LinearLayout huizhen_item;
		ImageView huizhen_item_img;
		TextView huizhen_item_name;
		TextView huizhen_item_position;
		TextView huizhen_item_cando;
	}
}
