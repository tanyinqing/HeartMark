package com.yikang.heartmark.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.YongYaoInfoAdapter;
import com.yikang.heartmark.database.YaoDB;

public class YongYaoInfoView extends LinearLayout {
	private Context context;
	private ExpandableListView yongYaoListView;
	private YongYaoInfoAdapter yongYaoInfoAdapter;
	private List<String> titleList;
	private List<List<String>> dataList;
	private YaoDB yaoDB;

	public YongYaoInfoView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.yongyao_info_view, this,
				true);
		init();
	}

	public YongYaoInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.yongyao_info_view, this,
				true);
		init();
	}

	private void init() {
		yaoDB = new YaoDB(context);
		yongYaoListView = (ExpandableListView) findViewById(R.id.yongyao_info_listview);
		
		//title
		titleList = yaoDB.getTypeList();
		dataList = new ArrayList<List<String>>();
		
		List<String> tempArray;
		for (int index = 0; index < titleList.size(); ++index) {
			tempArray = new ArrayList<String>();
			dataList.add(tempArray);
		}
		//data
		List<String> nameArray;
	    for(int k=0; k<titleList.size(); k++){
	        nameArray= yaoDB.getNameListByType(titleList.get(k));
	        for(int i=0; i<nameArray.size(); i++){
	        	dataList.get(k).add(nameArray.get(i));
	        }
		}
		
		yongYaoInfoAdapter = new YongYaoInfoAdapter(titleList, dataList,context);
		yongYaoListView.setAdapter(yongYaoInfoAdapter);
	}
	
}
