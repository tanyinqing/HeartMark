package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.SetRemindCelingActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;

@SuppressLint("InflateParams")
public class SetRemindCelingAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<String> arrayList = null;

	public SetRemindCelingAdapter(Context c, ArrayList<String> list) {
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
			convertView = layoutInflater.inflate(R.layout.set_remind_celing_item,
					null);

			holder = new ViewHolder();
			TextView remindTime = (TextView) convertView
					.findViewById(R.id.set_remind_celing_item_time);
			ImageView remindDelete = (ImageView) convertView
					.findViewById(R.id.set_remind_celing_item_delete);

			holder.remindTime = remindTime;
			holder.remindDelete = remindDelete;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.remindTime.setText(arrayList.get(position));
		holder.remindDelete.setOnClickListener(new MyViewOnclicklistener(arrayList.get(position)));
		return convertView;
	}
	
	class MyViewOnclicklistener implements View.OnClickListener {

		String time;
		public MyViewOnclicklistener( String time) {
			this.time = time;
		}
		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.set_remind_celing_item_delete:
				AlarmDB alarmDB =new AlarmDB(context);
				AlarmManager alarmManager = (AlarmManager) SetRemindCelingActivity.instance.getSystemService(Context.ALARM_SERVICE);
				//关闭闹钟
				ArrayList<Alarm>  alarmList = alarmDB.getAlarmListByTime(Alarm.TYPE_CELING,time);
				for(int i=0; i<alarmList.size(); i++){
					Alarm.closeRemind(alarmManager, context, alarmList.get(i).alarmId);
				}
				//删除数据库数据
				alarmDB.deleteByTypeName(Alarm.TYPE_CELING ,time);
				//更新界面
				SetRemindCelingActivity.instance.refreshView();
				break;
			default:
				break;
			}
		}
	}

	static class ViewHolder {
		TextView remindTime;
		ImageView remindDelete;
	}

}
