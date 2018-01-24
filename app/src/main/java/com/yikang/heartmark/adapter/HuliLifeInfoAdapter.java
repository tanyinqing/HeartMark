package com.yikang.heartmark.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.model.HuLiLife;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class HuliLifeInfoAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public List<HuLiLife> arrayList = null;
	private int day;

	public HuliLifeInfoAdapter(Context c, List<HuLiLife> dataAddList, int day) {
		this.context = c;
		this.layoutInflater = LayoutInflater.from(c);
		this.arrayList =  dataAddList;
		this.day = day;
	}

	@Override
	public int getCount() {
		return day;
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
		final ViewHolder holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.huli_life_info_item, null);

			holder = new ViewHolder();
			holder.localTextView0 = (TextView) convertView.findViewById(R.id.huli_life_info_date);
			holder.localTextView1 = (TextView) convertView.findViewById(R.id.huli_life_info_salt);
			holder.localTextView2 = (TextView) convertView.findViewById(R.id.huli_life_info_water);
			holder.localTextView3 = (TextView) convertView.findViewById(R.id.huli_life_info_weight);
			holder.localTextView4 = (TextView) convertView.findViewById(R.id.huli_life_info_food);
			holder.localTextView5 = (TextView) convertView.findViewById(R.id.huli_life_info_sport);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		if (arrayList != null || arrayList.size() != 0) {
			
			HuLiLife item;
			for (int i = 0; i < arrayList.size(); i++) {
				item = arrayList.get(i);
				if (Integer.valueOf(item.day) == position + 1) {
						holder.localTextView1.setText(item.salt);
						holder.localTextView2.setText(item.water);
						holder.localTextView3.setText(item.weight);
						holder.localTextView4.setText(item.food);
						holder.localTextView5.setText(item.sport);
					}
				}
			}
		holder.localTextView0.setText(String.valueOf(position + 1));
		return convertView;
	}

	protected void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.localTextView0.setText(null);
		viewHolder.localTextView1.setText(null);
		viewHolder.localTextView2.setText(null);
		viewHolder.localTextView3.setText(null);
		viewHolder.localTextView4.setText(null);
		viewHolder.localTextView5.setText(null);
	}

	static class ViewHolder {
		LinearLayout bloodDataItem;
		TextView localTextView0;
		TextView localTextView1;
		TextView localTextView2;
		TextView localTextView3;
		TextView localTextView4;
		TextView localTextView5;
	}
}
