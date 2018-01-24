package com.yikang.heartmark.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.YongYaoAdapter;
import com.yikang.heartmark.database.YongYaoDB;
import com.yikang.heartmark.model.YongYao;
import com.yikang.heartmark.util.ConstantUtil;

public class YongYaoRemindView extends LinearLayout {
	private Context context;
	private String uid;
	private ListView yongYaoListView;
	private YongYaoAdapter yongYaoAdapter;
	private ArrayList<YongYao> yongYaoList = new ArrayList<YongYao>();
	public static YongYaoRemindView instance;

	public YongYaoRemindView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.yongyao_remind_view, this,
				true);
		init();
	}

	public YongYaoRemindView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.yongyao_remind_view, this,
				true);
		init();
	}

	private void init() {
		instance = this;
		uid = ConstantUtil.getUid(context);
		yongYaoListView = (ListView) findViewById(R.id.yongyao_listview);
		
		yongYaoList = new YongYaoDB(context).getCeLingList(uid);
		if(yongYaoList.size() != 0 && yongYaoList != null){
			yongYaoAdapter = new YongYaoAdapter(context, yongYaoList);
			yongYaoListView.setAdapter(yongYaoAdapter);
		}
	}
	
	public void refreshListView(){
		yongYaoList = new YongYaoDB(context).getCeLingList(uid);
		yongYaoAdapter = new YongYaoAdapter(context, yongYaoList);
		yongYaoListView.setAdapter(yongYaoAdapter);
	}
	
}
