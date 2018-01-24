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
import com.lidroid.xutils.BitmapUtils;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;

@SuppressLint("InflateParams")
public class ZiXunNewAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<ZiXun> arrayList = null;

	public ZiXunNewAdapter(Context c, ArrayList<ZiXun> list) {
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
			convertView = layoutInflater.inflate(R.layout.zixun_info_item, null);

			holder = new ViewHolder();
			ImageView infoImage = (ImageView) convertView.findViewById(R.id.zixun_info_item_image);
			TextView infoTitle = (TextView) convertView.findViewById(R.id.zixun_info_item_title);
			TextView infoText = (TextView) convertView.findViewById(R.id.zixun_info_item_text);
			TextView infoTime = (TextView) convertView.findViewById(R.id.zixun_info_item_time);
			TextView infoGood = (TextView) convertView.findViewById(R.id.zixun_info_item_good);
			ImageView infoGoodImg = (ImageView) convertView.findViewById(R.id.zixun_info_item_good_img);

			holder.infoImage = infoImage;
			holder.infoTitle = infoTitle;
			holder.infoText = infoText;
			holder.infoTime = infoTime;
			holder.infoGood = infoGood;
			holder.infoGoodImg = infoGoodImg;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ZiXun item = arrayList.get(position);

		holder.infoTitle.setText(item.title);
		holder.infoText.setText(item.contentRead);
		holder.infoTime.setText(item.time);
		if(item.videoUrl == null || item.videoUrl.equals("")){
			holder.infoGood.setText(item.good);
		}else{
			holder.infoGood.setVisibility(View.GONE);
			holder.infoGoodImg.setVisibility(View.GONE);
		}
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(holder.infoImage,ConnectUtil.HOST_URL + item.image);
		return convertView;
	}

	static class ViewHolder {
		ImageView infoImage;
		TextView infoTitle;
		TextView infoText;
		TextView infoTime;
		TextView infoGood;
		ImageView infoGoodImg;
	}
}
