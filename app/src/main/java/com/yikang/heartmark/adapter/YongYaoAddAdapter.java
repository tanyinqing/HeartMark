package com.yikang.heartmark.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.YongYaoAddActivity;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.wheel.WheelView;
import com.yikang.heartmark.widget.MyDialog;

@SuppressLint("InflateParams")
public class YongYaoAddAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<String> arrayList = null;

	public YongYaoAddAdapter(Context c, ArrayList<String> list) {
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
			convertView = layoutInflater.inflate(R.layout.yongyao_add_item,
					null);

			holder = new ViewHolder();
			RelativeLayout addLayout = (RelativeLayout) convertView
					.findViewById(R.id.yongyao_add_item_layout);
			TextView addTime = (TextView) convertView
					.findViewById(R.id.yongyao_add_item_text);

			holder.addLayout = addLayout;
			holder.addTime = addTime;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String item = arrayList.get(position);
		holder.addTime.setText(item);
		holder.addLayout.setOnClickListener(new MyViewOnclicklistener(holder.addTime, position));

		return convertView;
	}

	class MyViewOnclicklistener implements View.OnClickListener {
		private TextView textView;
		private int position;

		public MyViewOnclicklistener(TextView textView, int position) {
			this.textView = textView;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.yongyao_add_item_layout:

				String dateStr = textView.getText().toString().trim();
				if (dateStr.equals("")) {
					dateStr = DateUtil.getNowDate(DateUtil.DATE_HOUR_MINUTE);
				}
				Date date = DateUtil.dateFromString(dateStr, DateUtil.DATE_HOUR_MINUTE);
				MyDialog dialog = new MyDialog(context).setTitle("请选择时间")
						.setTimePicker(date).setNegativeButton(R.string.cancel, null)
						.setPositiveButton(R.string.ok, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								WheelView wv_hour = (WheelView) v
										.findViewById(R.id.hour);
								WheelView wv_minute = (WheelView) v
										.findViewById(R.id.minute);
								String parten = "00";
								DecimalFormat decimal = new DecimalFormat(parten);
								String text = decimal.format(wv_hour.getCurrentItem())
										+ ":"
										+ decimal.format(wv_minute.getCurrentItem());
								textView.setText(text);
								YongYaoAddActivity.instance.timeList.set(position, text);
								YongYaoAddActivity.instance.haveTime = true;
							}
						});
				dialog.create(null).show();
			
				break;
			default:
				break;
			}
		}
	}

	static class ViewHolder {
		RelativeLayout addLayout;
		TextView addTime;
	}

}
