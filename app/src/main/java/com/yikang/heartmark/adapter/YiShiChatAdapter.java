package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.lidroid.xutils.BitmapUtils;
import com.yikang.heartmark.model.YiShiChat;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.DpPxUtils;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class YiShiChatAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<YiShiChat> arrayList = null;
	public Bitmap bitmap = null;

	public YiShiChatAdapter(Context c, ArrayList<YiShiChat> list,
			Bitmap userBitmap) {
		context = c;
		layoutInflater = LayoutInflater.from(c);
		arrayList = list;
		bitmap = userBitmap;
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
		 ViewHolder holder = null;

		if (arrayList.get(position).chatFrom == 0) {
			convertView = layoutInflater.inflate(R.layout.yishi_chat_item_yishi, null);
			final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.yishi_chat_yishi_layout);
			TextView textView = (TextView) convertView.findViewById(R.id.yishi_chat_text_yishi);
			YiShiChat item = arrayList.get(position);
			if(item.chatText != null && !item.chatText.equals("")){
				textView.setText(item.chatText);
			}else if(item.chatImage != null && !item.chatImage.equals("")){
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				bitmapUtils.display(linearLayout,ConnectUtil.HOST_URL + item.chatImage);
			}
		} else if (arrayList.get(position).chatFrom == 1) {
			convertView = layoutInflater.inflate(R.layout.yishi_chat_item_user,
					null);
			final RelativeLayout linearLayout = (RelativeLayout) convertView.findViewById(R.id.yishi_chat_user_layout);
			final TextView textView = (TextView) convertView.findViewById(R.id.yishi_chat_text_user);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.yishi_chat_item_user_heart);
			imageView.setBackgroundColor(color.transparent);
			imageView.setImageBitmap(bitmap);
			
			YiShiChat item = arrayList.get(position);
			if(item.chatText != null && !item.chatText.equals("")){
				textView.setText(item.chatText);
			}else if(item.chatImage != null && !item.chatImage.equals("")){
				//按照比例设置轮播图的宽高
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				@SuppressWarnings("deprecation")
				int width = wm.getDefaultDisplay().getWidth() - DpPxUtils.dip2px(context, 236);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.width = width;
				if(!TextUtils.isEmpty(item.chatImageHigh) && !TextUtils.isEmpty(item.chatImageWide)){
					params.height = width * Integer.valueOf(item.chatImageHigh) / Integer.valueOf(item.chatImageWide);
				}else{
					params.height = width-100;
				}
				textView.setLayoutParams(params);
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				bitmapUtils.display(textView,ConnectUtil.HOST_URL + item.chatImage);
			}
		}
		return convertView;
	}

	static class ViewHolder {
		LinearLayout linearLayout;
		ImageView imageView;
		TextView textView;
	}
}
