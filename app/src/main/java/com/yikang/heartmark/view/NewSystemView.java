package com.yikang.heartmark.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.MainNewsActivity;
import com.yikang.heartmark.activity.ZiXunDetailActivity;
import com.yikang.heartmark.adapter.NewSystemAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.model.NewSystem;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.DynamicListView.DynamicListViewListener;
import com.yuzhi.framework.util.ConnectionManager;

public class NewSystemView extends LinearLayout implements
		DynamicListViewListener {
	private Context context;
	private DynamicListView infoListView;
	private NewSystemAdapter infoAdapter;
	private ArrayList<NewSystem> infoList = new ArrayList<NewSystem>();
	private int page = 1;

	public NewSystemView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.new_system_view, this,true);
		init();
	}

	public NewSystemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.new_system_view, this,true);
		init();
	}

	private void init() {
		infoListView = (DynamicListView) findViewById(R.id.new_system_listview);
		infoListView.setOnRefreshListener(this);
		infoListView.setOnMoreListener(this);
		infoListView.setDoMoreWhenBottom(false);  // 滚动到低端的时候是否自动加载更多
		infoListView.setOnItemClickListener(itemClickListener);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("pageNo", String.valueOf(page));
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN06090WD00", "querySystemMessageList", params, "newPublishSucessCallBack", this);
			((MainNewsActivity)context).handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
		
		
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent detailIntent = new Intent(context, ZiXunDetailActivity.class);
			detailIntent.putExtra("webxml", infoList.get(position-1).content);
			context.startActivity(detailIntent);
		}
	};

	//首次请求或者刷新回调
	@SuppressWarnings("rawtypes")
	public void newPublishSucessCallBack(Object data){
		((MainNewsActivity)context).handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		infoListView.doneRefresh();
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(context,R.string.check_network_timeout);
		} else {
		List list = (List)resultMap.get("resultSet");
		infoList.clear();
		for(int i =0;i<list.size();i++){
			NewSystem newFriend =new NewSystem();
			Map temp = new HashMap();
			temp = (Map)list.get(i);
			newFriend.image = (String) temp.get("ICON_URL");
			newFriend.info = (String) temp.get("TITLE");
			newFriend.author = (String) temp.get("USERNAME");
		    newFriend.time = (String) temp.get("CREATE_TIME");
		    newFriend.content = (String) temp.get("CONTENT");
		    infoList.add(newFriend);
		}
		infoAdapter = new NewSystemAdapter(context, infoList);
		infoListView.setAdapter(infoAdapter);
	}
	}
	
	//上拉加载的时候的返回
	@SuppressWarnings("rawtypes")
	public void getMoreSucessCallBack(Object data){
		infoListView.doneMore();
		Map resultMap = (Map)data;
		if(resultMap == null){
			//失败处理
			return;
		}
		List list = (List)resultMap.get("resultSet");
		for(int i =0;i<list.size();i++){
			NewSystem newFriend =new NewSystem();
			Map temp = new HashMap();
			temp = (Map)list.get(i);
			newFriend.image = (String) temp.get("ICON_URL");
			newFriend.info = (String) temp.get("TITLE");
			newFriend.author = (String) temp.get("USERNAME");
			newFriend.time = (String) temp.get("CREATE_TIME");
			newFriend.content = (String) temp.get("CONTENT");
		    
		    infoList.add(newFriend);
		}
		if(infoAdapter != null){
			infoAdapter.notifyDataSetChanged();
			page ++;
		}
	}
	
	@Override
	public boolean onRefreshOrMore(DynamicListView dynamicListView,
			boolean isRefresh) {
		if(isRefresh){
			//刷新请求第一页
			page = 1;
			Map<String,String> params = new HashMap<String,String>();
			params.put("pageNo", String.valueOf(page));
			ConnectionManager.getInstance().send("FN06090WD00", "querySystemMessageList", params, "newPublishSucessCallBack", this);
		}else{
			if(page == 1){
				page ++;
			}
			Map<String,String> params = new HashMap<String,String>();
			params.put("pageNo", String.valueOf(page));
			ConnectionManager.getInstance().send("FN06090WD00", "querySystemMessageList", params, "getMoreSucessCallBack", this);
		}
		return false;
	}
}
