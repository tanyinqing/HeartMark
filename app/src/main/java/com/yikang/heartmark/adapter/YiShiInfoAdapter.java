package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.YiShiChatActivity;
import com.yikang.heartmark.model.YiShiInfo;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.SharedPreferenceUtil;

@SuppressLint("InflateParams")
public class YiShiInfoAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<YiShiInfo> arrayList = null;
	public String sex = "";

	public YiShiInfoAdapter(Context c, ArrayList<YiShiInfo> list) {
		context = c;
		layoutInflater = LayoutInflater.from(c);
		arrayList = list;
		sex = SharedPreferenceUtil.getString(c, ConstantUtil.USER_SEX);
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
			convertView = layoutInflater.inflate(R.layout.yishi_info_item, null);

			holder = new ViewHolder();
			LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.yishi_info_item_layout);
			ImageView infoImage = (ImageView) convertView.findViewById(R.id.yishi_info_item_image);
			TextView infoName = (TextView) convertView.findViewById(R.id.yishi_info_item_name);
			TextView infoTime = (TextView) convertView.findViewById(R.id.yishi_info_item_time);
			TextView infoTitle = (TextView) convertView.findViewById(R.id.yishi_info_item_title);
			Button infoReply = (Button) convertView.findViewById(R.id.yishi_info_item_reply);
			TextView infoChatImage = (TextView) convertView.findViewById(R.id.yishi_info_item_chat_image);
			TextView infoChatReply = (TextView) convertView.findViewById(R.id.yishi_info_item_chat_reply);
			TextView infoChatAdd = (TextView) convertView.findViewById(R.id.yishi_info_item_chat_add);
			

			holder.linearLayout = linearLayout;
			holder.infoImage = infoImage;
			holder.infoName = infoName;
			holder.infoTime = infoTime;
			holder.infoTitle = infoTitle;
			holder.infoReply = infoReply;
			holder.infoChatImage = infoChatImage;
			holder.infoChatReply = infoChatReply;
			holder.infoChatAdd = infoChatAdd;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		YiShiInfo item = arrayList.get(position);

		if(sex.equals("") ||sex.equals("0001")){
			holder.infoImage.setImageResource(R.drawable.friend_head_man);
		}else if(sex.equals("0002")){
			holder.infoImage.setImageResource(R.drawable.friend_head_men);
		}
		holder.infoName.setText(item.name);
		holder.infoTime.setText(item.time);
		holder.infoTitle.setText(item.title);
		holder.infoChatImage.setText("图片" + item.chatImage);
		holder.infoChatReply.setText("回复" + item.chatReply);
		holder.infoChatAdd.setText("追问" + item.chatAdd);
		
		if(item.reply){
			holder.infoReply.setBackgroundResource(R.drawable.yishi_reply_yes);
		}else{
			holder.infoReply.setBackgroundResource(R.drawable.yishi_reply_no);
		}
		
		holder.linearLayout.setOnClickListener(new MyViewOnclicklistener(position));
		
		return convertView;
	}

	class MyViewOnclicklistener implements View.OnClickListener {
		int position;
		public MyViewOnclicklistener(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			int rid = v.getId();
			switch (rid) {
			case R.id.yishi_info_item_layout:
				Intent mIntent = new Intent(context,YiShiChatActivity.class);
				mIntent.putExtra("chatType", "reply");
				mIntent.putExtra("uuid", arrayList.get(position).uuid);
				context.startActivity(mIntent);
				break;
			}
		}
	}
	
	
	static class ViewHolder {
		LinearLayout linearLayout;
		ImageView infoImage;
		TextView infoName;
		TextView infoTime;
		TextView infoTitle;
		Button infoReply;
		TextView infoChatImage;
		TextView infoChatReply;
		TextView infoChatAdd;
	}

}
