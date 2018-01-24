package com.yikang.heartmark.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.ZiXunDetailActivity;
import com.yikang.heartmark.adapter.ZiXunNewAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.DynamicListView.DynamicListViewListener;
import com.yuzhi.framework.util.ConnectionManager;

public class ZiXunExpertView extends LinearLayout implements
    DynamicListViewListener{
	private Context context;
	private DynamicListView zixunListView;
	private ZiXunNewAdapter zixunAdapter;
	private ArrayList<ZiXun> infoList = new ArrayList<ZiXun>();
	
	public ZiXunExpertView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.zixun_expert_view, this,
				true);
		init();
	}

	public ZiXunExpertView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.zixun_expert_view, this,
				true);
		init();
	}

	private void init() {
		zixunListView = (DynamicListView) findViewById(R.id.zixun_expert_listview);
		//设置下拉刷新
		//zixunListView.setOnRefreshListener(this);
		zixunListView.setOnMoreListener(this);
		zixunListView.setDoMoreWhenBottom(false);  // 滚动到低端的时候是否自动加载更多
		zixunListView.setOnItemClickListener(itemClickListener);
		
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", String.valueOf(1));
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("FN11050WL00", "queryForumNewsList", params, "newSucessCallBack", this);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//视频链接  infoList.get(position).videoUrl
			if(!infoList.get(position).videoUrl.equals("") && infoList.get(position).videoUrl != null){
				  // 把字符串转化为视频链接的地址
				Uri uri = Uri.parse(infoList.get(position).videoUrl);  
	               Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	               context.startActivity(intent); 
			}else{
				Intent detailIntent = new Intent(context, ZiXunDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newItem", (ZiXun)infoList.get(position));
				detailIntent.putExtras(bundle);
				context.startActivity(detailIntent);
			}

		}
	};
	
	private int page = 1;
	private int totalPage = 0;
	//首次请求或者刷新回调
	@SuppressWarnings("rawtypes")
	public void newSucessCallBack(Object data){
		zixunListView.doneRefresh();
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(context,R.string.check_network_timeout);
		} else {
		page=Double.valueOf(resultMap.get("pageNo").toString()).intValue();
		totalPage=Double.valueOf(resultMap.get("totalPage").toString()).intValue();
		List list = (List)resultMap.get("resultSet");
		infoList.clear();
		for(int i =0;i<list.size();i++){
			ZiXun zixunInfo =new ZiXun();
			Map temp = new HashMap();
			temp = (Map)list.get(i);
			zixunInfo.newId = (String) temp.get("UUID");
			zixunInfo.title = (String) temp.get("TITLE");
			zixunInfo.contentRead = (String) temp.get("CONTENT_TEMP");
		    zixunInfo.image = (String) temp.get("ICON_URL");
		    zixunInfo.time = (String) temp.get("CREATE_TIME");
		    zixunInfo.videoUrl = (String) temp.get("VIDEO_URL");
		    //zixunInfo.content = (String) temp.get("CONTENT");
		    zixunInfo.good = String.valueOf(Double.valueOf(
					temp.get("PRAISE_COUNT").toString()).intValue());
		    zixunInfo.house = String.valueOf(Double.valueOf(
					temp.get("COLLECT_FLAG").toString()).intValue());
		    zixunInfo.type = ZiXun.TYPE_ZIXUN;
		    infoList.add(zixunInfo);
		}
		zixunAdapter = new ZiXunNewAdapter(context, infoList);
		zixunListView.setAdapter(zixunAdapter);
	}
	}
	
	//上拉加载的时候的返回
	@SuppressWarnings("rawtypes")
	public void getMoreSucessCallBack(Object data){
		zixunListView.doneMore();
		Map resultMap = (Map)data;
		if(resultMap == null){
			//失败处理
			return;
		}
		
		List list = (List)resultMap.get("resultSet");
		for(int i =0;i<list.size();i++){
			ZiXun zixunInfo =new ZiXun();
			Map temp = new HashMap();
			temp = (Map)list.get(i);
			zixunInfo.title = (String) temp.get("TITLE");
			zixunInfo.contentRead = (String) temp.get("CONTENT_TEMP");
		    zixunInfo.image = (String) temp.get("ICON_URL");
		    infoList.add(zixunInfo);
		}
		if(zixunAdapter != null){
			zixunAdapter.notifyDataSetChanged();
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
			ConnectionManager.getInstance().send("FN11050WL00", "queryForumNewsList", params, "newSucessCallBack", this);
		}else{
			if(page != totalPage){
				if(page == 1){
					page ++;
				}
				Map<String,String> params = new HashMap<String,String>();
				params.put("pageNo", String.valueOf(page));
				ConnectionManager.getInstance().send("FN11050WL00", "queryForumNewsList", params, "getMoreSucessCallBack", this);
			}else{
				ShowUtil.showToast(AppContext.context,"没有更多数据");
				zixunListView.doneMore();
			}
		}
		return false;
	}
}
