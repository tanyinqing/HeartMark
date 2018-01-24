package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.model.YongYaoRemind;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class YongYaoRemindAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<YongYaoRemind> arrayList = null;

	public YongYaoRemindAdapter(Context c, ArrayList<YongYaoRemind> list) {
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
			convertView = layoutInflater.inflate(R.layout.yongyao_remind_item,
					null);

			holder = new ViewHolder();
			TextView remindName = (TextView) convertView
					.findViewById(R.id.yongyao_remind_item_name);
			TextView remindTime = (TextView) convertView
					.findViewById(R.id.yongyao_remind_item_time);
			TextView remindInfo = (TextView) convertView
					.findViewById(R.id.yongyao_remind_item_info);
			TextView remindDelete = (TextView) convertView
					.findViewById(R.id.yongyao_remind_item_delete);

			holder.remindName = remindName;
			holder.remindTime = remindTime;
			holder.remindInfo = remindInfo;
			holder.remindDelete = remindDelete;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		YongYaoRemind item = arrayList.get(position);

		holder.remindName.setText(item.name);
		holder.remindTime.setText(item.time);
		holder.remindInfo.setText(item.info);
		holder.remindDelete.setText(item.delete);
		// holder.goodItem.setOnClickListener(new MyViewOnclicklistener(item));
		return convertView;
	}

	static class ViewHolder {
		TextView remindName;
		TextView remindTime;
		TextView remindInfo;
		TextView remindDelete;
	}

}
