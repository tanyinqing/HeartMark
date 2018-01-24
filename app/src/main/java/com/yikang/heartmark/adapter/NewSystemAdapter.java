package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.model.NewSystem;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.DownImage;
import com.yikang.heartmark.util.DownImage.ImageCallBack;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class NewSystemAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<NewSystem> arrayList = null;

	public NewSystemAdapter(Context c, ArrayList<NewSystem> list) {
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
			convertView = layoutInflater.inflate(R.layout.new_system_item, null);

			holder = new ViewHolder();
			ImageView friendImage = (ImageView) convertView.findViewById(R.id.new_system_image);
			TextView friendInfo = (TextView) convertView.findViewById(R.id.new_system_info);
			TextView friendAuthor = (TextView) convertView.findViewById(R.id.new_system_author);
			TextView friendTime = (TextView) convertView.findViewById(R.id.new_system_time);

			holder.friendImage = friendImage;
			holder.friendInfo = friendInfo;
			holder.friendAuthor = friendAuthor;
			holder.friendTime = friendTime;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewSystem item = arrayList.get(position);

		holder.friendImage.setImageResource(R.drawable.head_icon);
		holder.friendInfo.setText(item.info);
		holder.friendAuthor.setText(item.author);
		holder.friendTime.setText(item.time);
		DownImage downImage = new DownImage(ConnectUtil.HOST_URL + item.image);
		downImage.loadImage(new ImageCallBack() {
			@Override
			public void getDrawable(Drawable drawable) {
				holder.friendImage.setImageDrawable(drawable);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView friendImage;
		TextView friendInfo;
		TextView friendAuthor;
		TextView friendTime;
	}

}
