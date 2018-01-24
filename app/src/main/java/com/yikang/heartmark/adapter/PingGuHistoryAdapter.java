package com.yikang.heartmark.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heartmark.R;

@SuppressLint("InflateParams")
public class PingGuHistoryAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<Map<String, String>> arrayList = null;

	public PingGuHistoryAdapter(Context c, ArrayList<Map<String, String>> list) {
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
			convertView = layoutInflater.inflate(R.layout.pinggu_history_item,
					null);

			holder = new ViewHolder();
			TextView historyCount = (TextView) convertView
					.findViewById(R.id.pinggu_history_item_count);
			TextView historyTime = (TextView) convertView
					.findViewById(R.id.pinggu_history_item_time);
			TextView historyType = (TextView) convertView
					.findViewById(R.id.pinggu_history_item_type);

			holder.historyCount = historyCount;
			holder.historyTime = historyTime;
			holder.historyType = historyType;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.historyCount.setText(String.format(context.getResources()
				.getString(R.string.pinggu_history_count), getCount()-position));
		String time = (String) arrayList.get(position).get("CREATE_TIME");
		holder.historyTime.setText(time);
		String type = (String) arrayList.get(position).get("ASSESSMENT_TYPE");
		if(type.equals("0001")){
			holder.historyType.setText("即时评估");
		}else if(type.equals("0002")){
			holder.historyType.setText("阶段评估");
		}
		return convertView;
	}

	static class ViewHolder {
		TextView historyCount;
		TextView historyTime;
		TextView historyType;
	}
}
