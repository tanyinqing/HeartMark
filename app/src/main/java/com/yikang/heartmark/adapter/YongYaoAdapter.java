package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.ZiXunDetailActivity;
import com.yikang.heartmark.database.YaoDB;
import com.yikang.heartmark.database.YongYaoDB;
import com.yikang.heartmark.model.Yao;
import com.yikang.heartmark.model.YongYao;
import com.yikang.heartmark.view.YongYaoRemindView;

@SuppressLint("InflateParams")
public class YongYaoAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<YongYao> arrayList = null;

	public YongYaoAdapter(Context c, ArrayList<YongYao> list) {
		context = c;
		layoutInflater = LayoutInflater.from(c);
		arrayList = list;
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
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (arrayList == null || arrayList.size() == 0)
			return null;
		final ViewHolder holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.yongyao_item, null);

			holder = new ViewHolder();
			RelativeLayout yongyaoLayout = (RelativeLayout) convertView.findViewById(R.id.yongyao_item_layout);
			TextView yongyaoName = (TextView) convertView.findViewById(R.id.yongyao_item_name);
			TextView yongyaoTime = (TextView) convertView.findViewById(R.id.yongyao_item_time);
			ImageView yongyaoArrow =  (ImageView) convertView.findViewById(R.id.yongyao_item_arrow);
			LinearLayout yongyaoInfoLayout = (LinearLayout) convertView.findViewById(R.id.yongyao_item_info_layout);
			TextView yongyaoInfoName = (TextView) convertView.findViewById(R.id.yongyao_item_info_name);
			TextView yongyaoInfoUsage = (TextView) convertView.findViewById(R.id.yongyao_item_info_usage);
			TextView yongyaoInfoRemind = (TextView) convertView.findViewById(R.id.yongyao_item_info_remind);
			TextView yongyaoInfoDetail = (TextView) convertView.findViewById(R.id.yongyao_item_info_detail);
			TextView yongyaoInfoDelete = (TextView) convertView.findViewById(R.id.yongyao_item_info_delete);

			holder.yongyaoLayout = yongyaoLayout;
			holder.yongyaoName = yongyaoName;
			holder.yongyaoTime = yongyaoTime;
			holder.yongyaoArrow = yongyaoArrow;
			holder.yongyaoInfoLayout = yongyaoInfoLayout;
			holder.yongyaoInfoName = yongyaoInfoName;
			holder.yongyaoInfoUsage = yongyaoInfoUsage;
			holder.yongyaoInfoRemind = yongyaoInfoRemind;
			holder.yongyaoInfoDetail = yongyaoInfoDetail;
			holder.yongyaoInfoDelete = yongyaoInfoDelete;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		YongYao item = arrayList.get(position);

		holder.yongyaoName.setText(item.name);
		holder.yongyaoTime.setText(item.timeShow);
		holder.yongyaoInfoName.setText(item.name);
		holder.yongyaoInfoUsage.setText(item.usage);
		holder.yongyaoInfoRemind.setText(item.remind);
		
		holder.yongyaoLayout.setOnClickListener(new MyViewOnclicklistener(position,holder));
		holder.yongyaoInfoDetail.setOnClickListener(new MyViewOnclicklistener(position,holder));
		holder.yongyaoInfoDelete.setOnClickListener(new MyViewOnclicklistener(position,holder));
		return convertView;
	}
	
	class MyViewOnclicklistener implements View.OnClickListener {
		int position; 
		ViewHolder viewHolder;
		public MyViewOnclicklistener(int position, ViewHolder viewHolder) {
			this.position = position;
			this.viewHolder = viewHolder;
		}
		@Override
		public void onClick(View v) {
			int rid = v.getId();
			switch (rid) {
			case R.id.yongyao_item_layout:
                if(arrayList.get(position).isLookInfo){
                	viewHolder.yongyaoArrow.setBackgroundResource(R.drawable.arrow_right);
                	viewHolder.yongyaoInfoLayout.setVisibility(View.GONE);
                	arrayList.get(position).isLookInfo = false;
                }else{
                	viewHolder.yongyaoArrow.setBackgroundResource(R.drawable.arrow_bottom);
                	viewHolder.yongyaoInfoLayout.setVisibility(View.VISIBLE);
                	arrayList.get(position).isLookInfo = true;
                }
				break;
			case R.id.yongyao_item_info_detail:
				Yao yao = new YaoDB(context).getYaoListDetail
				              (arrayList.get(position).name, arrayList.get(position).usage);
				Intent detailIntent = new Intent(context, ZiXunDetailActivity.class);
				detailIntent.putExtra("webxml", yao.content);
				detailIntent.putExtra("readtext", yao.contentText);
				context.startActivity(detailIntent);
				break;
			case R.id.yongyao_item_info_delete:
				new YongYaoDB(context).deleteById(String.valueOf(arrayList.get(position).id));
				YongYaoRemindView.instance.refreshListView();
				break;
			}
		}
	}

	static class ViewHolder {
		RelativeLayout yongyaoLayout;
		TextView yongyaoName;
		TextView yongyaoTime;
		ImageView yongyaoArrow;
		LinearLayout yongyaoInfoLayout;
		TextView yongyaoInfoName;
		TextView yongyaoInfoUsage;
		TextView yongyaoInfoRemind;
		TextView yongyaoInfoDetail;
		TextView yongyaoInfoDelete;
	}

}
