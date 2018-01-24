package com.yikang.heartmark.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.ZiXunDetailActivity;
import com.yikang.heartmark.database.YaoDB;
import com.yikang.heartmark.model.Yao;
import com.yikang.heartmark.model.ZiXun;

public class YongYaoInfoAdapter extends BaseExpandableListAdapter{
	private List<String> titleList;
	private List<List<String>> dataList;
	private Context context;
	private LayoutInflater layoutInflater = null;
	
    public YongYaoInfoAdapter(List<String> titleList, List<List<String>> dataList, Context context){
    	this.titleList = titleList;
    	this.dataList = dataList;
    	this.context = context;
    	layoutInflater = LayoutInflater.from(context);
    }
	@Override
	public int getGroupCount() {
		return titleList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = dataList.get(groupPosition).size();
		return count;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return titleList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return dataList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.yongyao_info_item, null);
			holder = new ViewHolder();
			
			TextView mateType = (TextView) convertView.findViewById(R.id.mate_type);
			ImageView mateImg = (ImageView) convertView.findViewById(R.id.mate_title_image);
			holder.mateType = mateType;
			holder.mateImg = mateImg;
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
	
		holder.mateType.setText(titleList.get(groupPosition));
		if(isExpanded){
			holder.mateImg.setBackgroundResource(R.drawable.arrow_bottom);
		}else{
			holder.mateImg.setBackgroundResource(R.drawable.arrow_right);
		}
		
		return convertView;
	}

	
	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ViewHolderData holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.yongyao_info_data_item, null);
			holder = new ViewHolderData();
			
			RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.yongyao_data_layout);
			TextView textView = (TextView) convertView.findViewById(R.id.yongyao_data_text);
			
			holder.layout = layout;
			holder.textView = textView;
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolderData) convertView.getTag();
		}
		
		holder.textView.setText(dataList.get(groupPosition).get(childPosition));
		
        Yao yao = new YaoDB(context).getYaoListDetail
	              (titleList.get(groupPosition), dataList.get(groupPosition).get(childPosition));
		
		holder.layout.setOnClickListener(new MyViewOnclicklistener(yao.yaoId, yao.content, yao.contentText));
		return convertView;
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		String yaoId;
		String content; 
		String contentText;
		public MyViewOnclicklistener(String yaoId, String content, String contentText) {
			this.content = content;
			this.contentText = contentText;
			this.yaoId = yaoId;
		}
		
		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.yongyao_data_layout:
				ZiXun zixun = new ZiXun();
				zixun.newId = yaoId;
				zixun.type = ZiXun.TYPE_EXPLAIN;
				Intent detailIntent = new Intent(context, ZiXunDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newItem", zixun);
				detailIntent.putExtras(bundle);
				context.startActivity(detailIntent);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
//        String content = null;
//        String contentText = null;
//        for(int i=0; i<yaoList.size(); i++){
//        	if(yaoList.get(i).type.equals(titleList.get(groupPosition))
//        			&& yaoList.get(i).name.equals(dataList.get(groupPosition).get(childPosition))){
//        		content = yaoList.get(i).content;
//        		contentText = yaoList.get(i).contentText;
//        	}
//        }
//        
//		Intent detailIntent = new Intent(context, ZiXunDetailActivity.class);
//		detailIntent.putExtra("webxml", content);
//		detailIntent.putExtra("readtext", contentText);
//		context.startActivity(detailIntent);
		return false;
	}

	
	static class ViewHolder {
			TextView mateType;
			ImageView mateImg;
	 }
	static class ViewHolderData{
		   RelativeLayout layout;
		   TextView textView;
	}
	 
}
