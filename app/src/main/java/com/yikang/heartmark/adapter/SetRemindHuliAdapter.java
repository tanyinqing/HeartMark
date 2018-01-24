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
import com.yikang.heartmark.activity.SetRemindHuliActivity;
import com.yikang.heartmark.database.AlarmDB;
import com.yikang.heartmark.model.Alarm;

@SuppressLint("InflateParams")
public class SetRemindHuliAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private Context context = null;
	public ArrayList<String> timeList = null;
	public ArrayList<String> weekList = null;

	public SetRemindHuliAdapter(Context c, ArrayList<String> timeList,
			ArrayList<String> weekList) {
		this.context = c;
		this.layoutInflater = LayoutInflater.from(c);
		this.timeList = timeList;
		this.weekList = weekList;
	}

	@Override
	public int getCount() {
		int size = 0;
		if (timeList != null && timeList.size() > 0)
			size = timeList.size();
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
		if (timeList == null || timeList.size() == 0)
			return null;
		final ViewHolder holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.set_remind_huli_item,
					null);

			holder = new ViewHolder();
			TextView remindTime = (TextView) convertView
					.findViewById(R.id.set_remind_huli_item_time);
			TextView remindWeek = (TextView) convertView
					.findViewById(R.id.set_remind_huli_item_week);
			ImageView remindDelete = (ImageView) convertView
					.findViewById(R.id.set_remind_huli_item_delete);

			holder.remindTime = remindTime;
			holder.remindWeek = remindWeek;
			holder.remindDelete = remindDelete;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.remindTime.setText(timeList.get(position));
		holder.remindWeek.setText(weekList.get(position));
		holder.remindDelete.setOnClickListener(new MyViewOnclicklistener(timeList.get(position)));
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
			case R.id.set_remind_huli_item_delete:
				AlarmDB alarmDB =new AlarmDB(context);
				AlarmManager alarmManager = (AlarmManager) SetRemindHuliActivity.instance.getSystemService(Context.ALARM_SERVICE);
				//关闭闹钟
				ArrayList<Alarm>  alarmList = alarmDB.getAlarmListByTime(Alarm.TYPE_HULI,time);
				for(int i=0; i<alarmList.size(); i++){
					Alarm.closeRemind(alarmManager, context, alarmList.get(i).alarmId);
				}
				//删除数据库数据
				alarmDB.deleteByTypeName(Alarm.TYPE_HULI ,time);
				//更新界面
				SetRemindHuliActivity.instance.refreshView();
				break;
			default:
				break;
			}
		}
	}

	static class ViewHolder {
		TextView remindTime;
		TextView remindWeek;
		ImageView remindDelete;
	}

}
