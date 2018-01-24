package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.SetRemindYaoActivity;
import com.yikang.heartmark.activity.ZiXunDetailActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.database.YaoDB;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.model.Yao;
import com.yikang.heartmark.model.ZiXun;

public class SetRemindYaoAdapter extends BaseExpandableListAdapter{
	private ArrayList<String> titleList;
	private ArrayList<ArrayList<String>> dataList;
	private Context context;
	private LayoutInflater layoutInflater = null;
	
    public SetRemindYaoAdapter(ArrayList<String> titleList, ArrayList<ArrayList<String>> dataList, Context context){
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
			convertView = layoutInflater.inflate(R.layout.set_remind_yao_title_item, null);
			holder = new ViewHolder();
			
			TextView mateType = (TextView) convertView.findViewById(R.id.mate_type);
			TextView mateCount = (TextView) convertView.findViewById(R.id.mate_count);
			ImageView mateImg = (ImageView) convertView.findViewById(R.id.mate_title_image);
			
			holder.mateType = mateType;
			holder.mateCount = mateCount;
			holder.mateImg = mateImg;
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
	
		holder.mateType.setText(titleList.get(groupPosition));
		holder.mateCount.setText(String.valueOf(dataList.get(groupPosition).size()));
		
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
			convertView = layoutInflater.inflate(R.layout.set_remind_yao_data_item, null);
			holder = new ViewHolderData();
			
			TextView textName = (TextView) convertView.findViewById(R.id.set_remind_yao_data_name);
			TextView textExplain = (TextView) convertView.findViewById(R.id.set_remind_yao_data_explain);
			LinearLayout layoutTime = (LinearLayout) convertView.findViewById(R.id.set_remind_yao_data_time);
			LinearLayout textDelete = (LinearLayout) convertView.findViewById(R.id.set_remind_yao_data_delete);
			
			holder.textName = textName;
			holder.textExplain = textExplain;
			holder.layoutTime = layoutTime;
			holder.layoutDelete = textDelete;
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolderData) convertView.getTag();
		}
		
		holder.textName.setText(dataList.get(groupPosition).get(childPosition));
		ArrayList<Alarm> alarmList= new AlarmDB(context).getAlarmListByYaoTypeName(Alarm.TYPE_YAO,
				titleList.get(groupPosition), dataList.get(groupPosition).get(childPosition));
		ArrayList<String> timeList = new ArrayList<String>();
		holder.layoutTime.removeAllViews();
		//过滤出时间
		for(int i=0; i<alarmList.size(); i++){
			boolean isHave = false;
			LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(  
			        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if(i == 0){
				timeList.add(alarmList.get(i).time);
				TextView textView =new TextView(context);
				textView.setText(alarmList.get(i).time);
				holder.layoutTime.addView(textView,mLayoutParams);
			}
			
			for(int k=0; k<timeList.size(); k++){
				if(alarmList.get(i).time.equals(timeList.get(k))){
					isHave = true;
				}
			}
			
			if(!isHave){
				timeList.add(alarmList.get(i).time);
				TextView textView =new TextView(context);
				textView.setText(alarmList.get(i).time);
				holder.layoutTime.addView(textView,mLayoutParams);
			}
		}
		
        Yao yao = new YaoDB(context).getYaoListDetail
	              (titleList.get(groupPosition), dataList.get(groupPosition).get(childPosition));
		holder.textExplain.setOnClickListener(new MyViewOnclicklistener(yao.yaoId));
		holder.layoutDelete.setOnClickListener(new MyViewOnclicklistener(titleList.get(groupPosition),
				dataList.get(groupPosition).get(childPosition),null));
		return convertView;
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		String yaoId; 
		String yaoType;
		String yaoName;
		public MyViewOnclicklistener(String yaoId) {
			this.yaoId = yaoId;
		}
		//第三个参数无意义,仅用于区分构造方法
		public MyViewOnclicklistener(String yaoType, String yaoName, String distinguishVoid) {
			this.yaoType = yaoType;
			this.yaoName = yaoName;
		}
		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.set_remind_yao_data_explain:
				ZiXun zixun = new ZiXun();
				zixun.newId = yaoId;
				zixun.type = ZiXun.TYPE_EXPLAIN;
				Intent detailIntent = new Intent(context, ZiXunDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newItem", zixun);
				detailIntent.putExtras(bundle);
				context.startActivity(detailIntent);
				break;
			case R.id.set_remind_yao_data_delete:
				AlarmDB alarmDB =new AlarmDB(context);
				AlarmManager alarmManager = (AlarmManager) SetRemindYaoActivity.instance.getSystemService(Context.ALARM_SERVICE);
				//关闭闹钟
				ArrayList<Alarm>  alarmList = alarmDB.getAlarmListByYaoTypeName(Alarm.TYPE_YAO,yaoType, yaoName);
				for(int i=0; i<alarmList.size(); i++){
					Alarm.closeRemind(alarmManager, context, alarmList.get(i).alarmId);
				}
				//删除数据库数据
				alarmDB.deleteByYaoTypeName(Alarm.TYPE_YAO ,yaoType, yaoName);
				//更新界面
				SetRemindYaoActivity.instance.refreshListView();
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
			TextView mateCount;
			ImageView mateImg;
	 }
	static class ViewHolderData{
		   TextView textName;
		   TextView textExplain; //说明
		   LinearLayout layoutTime;  //time View
		   LinearLayout layoutDelete;
	}
	 
}
