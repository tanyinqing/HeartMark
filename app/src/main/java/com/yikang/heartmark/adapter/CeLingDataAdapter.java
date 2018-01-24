package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.model.CeLingData;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class CeLingDataAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<CeLingData> arrayList = null;

	public CeLingDataAdapter(Context c, ArrayList<CeLingData> list) {
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
			convertView = layoutInflater.inflate(R.layout.celing_data_item, null);

			holder = new ViewHolder();
			LinearLayout dataMonthLayout = (LinearLayout) convertView.findViewById(R.id.celing_data_month_layout);
			TextView dataMonth = (TextView) convertView.findViewById(R.id.celing_data_month);
			TextView dataDay = (TextView) convertView.findViewById(R.id.celing_data_day);
			TextView dataTime = (TextView) convertView.findViewById(R.id.celing_data_time);
			TextView dataType = (TextView) convertView.findViewById(R.id.celing_data_type);
			TextView dataResult = (TextView) convertView.findViewById(R.id.celing_data_result);
			TextView dataState = (TextView) convertView.findViewById(R.id.celing_data_state);
			TextView dataDiag = (TextView) convertView.findViewById(R.id.celing_data_diag);

			holder.dataMonthLayout = dataMonthLayout;
			holder.dataMonth = dataMonth;
			holder.dataDay = dataDay;
			holder.dataTime = dataTime;
			holder.dataType = dataType;
			holder.dataResult = dataResult;
			holder.dataState = dataState;
			holder.dataDiag = dataDiag;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CeLingData item = arrayList.get(position);

		holder.dataMonth.setText(item.dateMonth);
		holder.dataDay.setText(item.day);
		holder.dataTime.setText(item.time);
		holder.dataType.setText(item.type);
		holder.dataResult.setText(item.result);
		holder.dataState.setText(item.state);
		holder.dataDiag.setText(item.diag);
		
		if(position == 0){
			holder.dataMonthLayout.setVisibility(View.VISIBLE);
		}else{
			if(item.dateMonth.equals(arrayList.get(position-1).dateMonth)){
				holder.dataMonthLayout.setVisibility(View.GONE);
			}else{
				holder.dataMonthLayout.setVisibility(View.VISIBLE);
			}
			
		}
		// holder.goodItem.setOnClickListener(new MyViewOnclicklistener(item));
		return convertView;
	}

	static class ViewHolder {
		LinearLayout dataMonthLayout;
		TextView dataMonth;
		TextView dataDay;
		TextView dataTime;
		TextView dataType;
		TextView dataResult;
		TextView dataState;
		TextView dataDiag;
	}
}
