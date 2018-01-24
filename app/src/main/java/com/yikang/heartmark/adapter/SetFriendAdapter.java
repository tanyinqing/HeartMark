package com.yikang.heartmark.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.model.Friend;
import com.yikang.heartmark.util.MyToast;
import com.yuzhi.framework.util.ConnectionManager;

@SuppressLint("InflateParams")
public class SetFriendAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<Friend> arrayList = null;

	public SetFriendAdapter(Context c, ArrayList<Friend> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (arrayList == null || arrayList.size() == 0)
			return null;
		final ViewHolder holder;

		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.set_friend_item, null);

			holder = new ViewHolder();
			ImageView friendHead = (ImageView) convertView
					.findViewById(R.id.friend_head);
			TextView friendName = (TextView) convertView
					.findViewById(R.id.friend_name);
			TextView friendPhone = (TextView) convertView
					.findViewById(R.id.friend_phone);
			CheckBox friendCheck = (CheckBox) convertView
					.findViewById(R.id.friend_checkBox);

			holder.friendHead = friendHead;
			holder.friendName = friendName;
			holder.friendPhone = friendPhone;
			holder.friendCheck = friendCheck;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Friend item = arrayList.get(position);

		holder.friendName.setText(item.name);
		holder.friendPhone.setText(item.phone);
		
		if("0001".equals(item.sex)){
			holder.friendHead.setBackgroundResource(R.drawable.friend_head_man);
		}else if("0002".equals(item.sex)){
			holder.friendHead.setBackgroundResource(R.drawable.friend_head_men);
		}
		
		if (item.check == 0) {
			holder.friendCheck.setChecked(false);
		} else {
			holder.friendCheck.setChecked(true);
		}

		holder.friendCheck
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							updateCheck(arrayList.get(position).uid, "1");
						} else {
							updateCheck(arrayList.get(position).uid, "0");
						}
					}
				});
		return convertView;
	}

	static class ViewHolder {
		ImageView friendHead;
		TextView friendName;
		TextView friendPhone;
		CheckBox friendCheck;
	}

	private void updateCheck(String friendId, String check){
		Map<String,String> params = new HashMap<String,String>();
		params.put("friendId",  friendId);
		params.put("isSendMessage",  check);
		ConnectionManager.getInstance().send("FN06080WD00", "modifyDefaultReceivePerson", params, 
				"updateCheckSucessCallBack", SetFriendAdapter.this);
	}
	
	@SuppressWarnings("rawtypes")
	public void updateCheckSucessCallBack(Object data){
		Map resultMap = (Map)data;
		String result = (String) resultMap.get("head");
		String hint = (String) resultMap.get("body");
		if (result.equals("success")) {
			MyToast.show(context, hint,Toast.LENGTH_LONG);
		} else if (result.equals("02")) {
			MyToast.show(context, hint,Toast.LENGTH_LONG);
		}
	}
	
}
