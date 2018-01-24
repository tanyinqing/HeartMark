package com.yikang.heartmark.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.model.HelpHeart;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class HelpHeartInfoAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public List<HelpHeart> arrayList = null;

	public HelpHeartInfoAdapter(Context c, List<HelpHeart> dataAddList) {
		this.context = c;
		this.layoutInflater = LayoutInflater.from(c);
		this.arrayList =  dataAddList;
	}

	@Override
	public int getCount() {
		return arrayList.size();
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
			convertView = layoutInflater.inflate(R.layout.help_heart_info_item, null);

			holder = new ViewHolder();
			holder.localTextView1 = (TextView) convertView.findViewById(R.id.help_heart_item_date);
			holder.localTextView2 = (TextView) convertView.findViewById(R.id.help_heart_item_heart);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

			HelpHeart item;
			item = arrayList.get(position);
			holder.localTextView1.setText(String.valueOf(item.date));
			holder.localTextView2.setText(String.valueOf(item.heart));
		return convertView;
	}

	protected void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.localTextView1.setText(null);
		viewHolder.localTextView2.setText(null);
	}

	static class ViewHolder {
		TextView localTextView1;
		TextView localTextView2;
	}
}
