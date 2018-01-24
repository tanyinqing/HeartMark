package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.model.NewFriend;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class NewFriendAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<NewFriend> arrayList = null;

	public NewFriendAdapter(Context c, ArrayList<NewFriend> list) {
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
			convertView = layoutInflater.inflate(R.layout.new_friend_item, null);

			holder = new ViewHolder();
			ImageView friendImage = (ImageView) convertView.findViewById(R.id.new_friend_image);
			TextView friendName = (TextView) convertView.findViewById(R.id.new_friend_name);
			TextView friendTime = (TextView) convertView.findViewById(R.id.new_friend_time);
			TextView friendResult = (TextView) convertView.findViewById(R.id.new_friend_result);
			TextView friendDiag = (TextView) convertView.findViewById(R.id.new_friend_result_diag);

			holder.friendImage = friendImage;
			holder.friendName = friendName;
			holder.friendTime = friendTime;
			holder.friendResult = friendResult;
			holder.friendDiag = friendDiag;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewFriend item = arrayList.get(position);
		
		holder.friendName.setText(item.name);
		holder.friendTime.setText(item.time);
		holder.friendResult.setText(item.result);
		holder.friendDiag.setText(item.diag);
		if("0001".equals(item.image)){
			holder.friendImage.setImageResource(R.drawable.friend_head_man);
		}else if("0002".equals(item.image)){
			holder.friendImage.setImageResource(R.drawable.friend_head_men);
		}
		return convertView;
	}

	static class ViewHolder {
		ImageView friendImage;
		TextView friendName;
		TextView friendTime;
		TextView friendResult;
		TextView friendDiag;
	}
}
