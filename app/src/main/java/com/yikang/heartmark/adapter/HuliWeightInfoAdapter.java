package com.yikang.heartmark.adapter;

import java.math.BigDecimal;
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
import com.yikang.heartmark.model.HuLiWeight;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class HuliWeightInfoAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public List<HuLiWeight> arrayList = null;
	private int day;

	public HuliWeightInfoAdapter(Context c, List<HuLiWeight> dataAddList, int day) {
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
			convertView = layoutInflater.inflate(R.layout.huli_weight_info_item, null);

			holder = new ViewHolder();
			holder.localTextView0 = (TextView) convertView.findViewById(R.id.huli_life_info_date);
			holder.localTextView1 = (TextView) convertView.findViewById(R.id.huli_life_info_base);
			holder.localTextView2 = (TextView) convertView.findViewById(R.id.huli_life_info_this);
			holder.localTextView3 = (TextView) convertView.findViewById(R.id.huli_life_info_diff);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		if (arrayList != null || arrayList.size() != 0) {
			
			HuLiWeight item;
			for (int i = 0; i < arrayList.size(); i++) {
				item = arrayList.get(i);
				if (Integer.valueOf(item.day) == position + 1) {
					int baseWeight = item.baseWeight;
					double thisWeight = Double.valueOf((item.thisWeight/10)+"."+(item.thisWeight%10));
					double diff = Math.abs(thisWeight-baseWeight);
					BigDecimal b = new BigDecimal(diff);
					diff =b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();  
					holder.localTextView1.setText(String.valueOf(baseWeight));
					holder.localTextView2.setText(String.valueOf(thisWeight));
					holder.localTextView3.setText(String.valueOf(diff));
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
	}

	static class ViewHolder {
		LinearLayout bloodDataItem;
		TextView localTextView0;
		TextView localTextView1;
		TextView localTextView2;
		TextView localTextView3;
	}
}
