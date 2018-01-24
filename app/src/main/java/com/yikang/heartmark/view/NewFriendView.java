package com.yikang.heartmark.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.NewFriendAdapter;
import com.yikang.heartmark.model.NewFriend;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.DynamicListView.DynamicListViewListener;
import com.yuzhi.framework.util.ConnectionManager;

public class NewFriendView extends LinearLayout implements
		DynamicListViewListener {
	private Context context;
	private DynamicListView infoListView;
	private NewFriendAdapter infoAdapter;
	private ArrayList<NewFriend> infoList = new ArrayList<NewFriend>();
	private int page = 1;

	public NewFriendView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.new_friend_view, this,
				true);
		init();
	}

	public NewFriendView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.new_friend_view, this,
				true);
		init();
	}

	private void init() {
		infoListView = (DynamicListView) findViewById(R.id.new_friend_listview);
		infoListView.setOnRefreshListener(this);
		infoListView.setOnMoreListener(this);
		infoListView.setDoMoreWhenBottom(false); // 滚动到低端的时候是否自动加载更多

		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", String.valueOf(page));
		ConnectionManager.getInstance().send("FN06090WD00",
				"queryFriendMessageList", params, "newFriendSucessCallBack",this);

	}

	// 首次请求或者刷新回调
	@SuppressWarnings("rawtypes")
	public void newFriendSucessCallBack(Object data) {
		infoListView.doneRefresh();
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(context,R.string.check_network_timeout);
		} else {
			List list = (List) resultMap.get("body");
			infoList.clear();
			for (int i = 0; i < list.size(); i++) {
				NewFriend newFriend = new NewFriend();
				Map temp = new HashMap();
				temp = (Map) list.get(i);
				newFriend.image = (String) temp.get("FRIEND_SEX");
				newFriend.name = (String) temp.get("FRIEND_NAME");
				newFriend.time = (String) temp.get("CREATE_TIME");
				newFriend.result = (String) temp.get("MONITOR_RESULT");
				newFriend.diag = (String) temp.get("CONTENT");
				infoList.add(newFriend);
			}
			infoAdapter = new NewFriendAdapter(context, infoList);
			infoListView.setAdapter(infoAdapter);
		}
	}

	// 上拉加载的时候的返回
	@SuppressWarnings("rawtypes")
	public void getMoreSucessCallBack(Object data) {
		infoListView.doneMore();
		Map resultMap = (Map) data;
		if(resultMap == null){
			//失败处理
			return;
		}
		List list = (List) resultMap.get("resultSet");
		for (int i = 0; i < list.size(); i++) {
			NewFriend newFriend = new NewFriend();
			Map temp = new HashMap();
			temp = (Map) list.get(i);
			newFriend.image = (String) temp.get("ICON_URL");
			newFriend.name = (String) temp.get("USERNAME");
			newFriend.time = (String) temp.get("USERNAME");
			newFriend.result = (String) temp.get("CONTENT");

			infoList.add(newFriend);
		}
		if (infoAdapter != null) {
			infoAdapter.notifyDataSetChanged();
			page ++;
		}
	}

	@Override
	public boolean onRefreshOrMore(DynamicListView dynamicListView,
			boolean isRefresh) {
		if (isRefresh) {
			// 刷新请求第一页
			page = 1;
			Map<String, String> params = new HashMap<String, String>();
			params.put("pageNo", String.valueOf(page));
			ConnectionManager.getInstance().send("FN06090WD00",
					"queryFriendMessageList", params,
					"newFriendSucessCallBack", this);
		} else {
			if(page == 1){
				page ++;
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("pageNo", String.valueOf(page));
			ConnectionManager.getInstance().send("FN06090WD00",
					"queryFriendMessageList", params, "getMoreSucessCallBack",
					this);
		}
		return false;
	}
}
