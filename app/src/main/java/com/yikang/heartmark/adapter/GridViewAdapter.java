package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.heartmark.R;
import com.yikang.heartmark.view.MyPoint;

public class GridViewAdapter extends BaseAdapter{
	private ArrayList<MyPoint> points;
	private Context context;
	public GridViewAdapter(ArrayList<MyPoint> views, Context context) {
		this.points = views;
		this.context = context;
	}

	@Override
	public int getCount() {
		return points.size();
	}

	@Override
	public Object getItem(int arg0) {
		return points.get(arg0).getIV();
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1 = LayoutInflater.from(context).inflate(R.layout.imageview, null);
		ImageView image = (ImageView) arg1.findViewById(R.id.image);
		image.setImageResource(points.get(arg0).getIV());
		return arg1;
	}
}
