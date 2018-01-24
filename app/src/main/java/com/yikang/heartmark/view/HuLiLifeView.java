package com.yikang.heartmark.view;

import java.text.DecimalFormat;
import java.util.Date;

import android.R.color;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.HuLiLifeInfoActivity;
import com.yikang.heartmark.database.HuLiLifeDB;
import com.yikang.heartmark.model.HuLiLife;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateType;
import com.yikang.heartmark.util.DateUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.wheel.WheelView;
import com.yikang.heartmark.widget.MyDialog;

public class HuLiLifeView extends LinearLayout {
	private Context context;
	private TextView timeText;
	private TextView saltText;
	private TextView waterText;
	private TextView weightText;
	private TextView foodText;
	private TextView sportText;
	public static int selectCount = 0;

	public HuLiLifeView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.huli_guid_view, this,
				true);
		init();
	}

	public HuLiLifeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.huli_guid_view, this,
				true);
		init();
	}

	private void init() {

		timeText = (TextView) findViewById(R.id.huli_time_text);
		saltText = (TextView) findViewById(R.id.huli_salt_text);
		waterText = (TextView) findViewById(R.id.huli_water_text);
		weightText = (TextView) findViewById(R.id.huli_weight_text);
		foodText = (TextView) findViewById(R.id.huli_food_text);
		sportText = (TextView) findViewById(R.id.huli_sport_text);
		RelativeLayout timeLayout = (RelativeLayout) findViewById(R.id.huli_time);
		RelativeLayout saltLayout = (RelativeLayout) findViewById(R.id.huli_salt);
		RelativeLayout waterLayout = (RelativeLayout) findViewById(R.id.huli_water);
		RelativeLayout weightLayout = (RelativeLayout) findViewById(R.id.huli_weight);
		RelativeLayout foodLayout = (RelativeLayout) findViewById(R.id.huli_food);
		RelativeLayout sportLayout = (RelativeLayout) findViewById(R.id.huli_sport);
		Button savaButton = (Button) findViewById(R.id.huli_save);

		timeLayout.setOnClickListener(new MyViewOnclicklistener());
		saltLayout.setOnClickListener(new MyViewOnclicklistener());
		waterLayout.setOnClickListener(new MyViewOnclicklistener());
		weightLayout.setOnClickListener(new MyViewOnclicklistener());
		foodLayout.setOnClickListener(new MyViewOnclicklistener());
		sportLayout.setOnClickListener(new MyViewOnclicklistener());
		savaButton.setOnClickListener(new MyViewOnclicklistener());
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.huli_time:
				showDateTimePicker();
				break;
			case R.id.huli_salt:
				String[] saltList = new String[] { "小于2g", "2g-3g", "大于3g" };
				showChoose(context, saltList, "食盐量", saltText);
				break;
			case R.id.huli_water:
				String[] waterList = new String[] { "小于2L", "大于2L" };
				showChoose(context, waterList, "饮水量", waterText);
				break;
			case R.id.huli_weight:
				String[] weightList = new String[] { "已记录", "未记录" };
				showChoose(context, weightList, "体重记录", weightText);
				break;
			case R.id.huli_food:
				String[] foodList = new String[] { "三餐", "五餐", "七餐" };
				showChoose(context, foodList, "少量多餐", foodText);
				break;
			case R.id.huli_sport:
				String[] sportList = new String[] { "10分钟", "20分钟", "30分钟" };
				showChoose(context, sportList, "合理运动", sportText);
				break;
			case R.id.huli_save:
				saveToDb();
				MyToast.show(context, "保存成功", Toast.LENGTH_LONG);
				Intent intent = new Intent(context, HuLiLifeInfoActivity.class);
				context.startActivity(intent);
				break;
			default:
				break;
			}
		}
	}

	public static void showChoose(Context context, final String[] stringList,
			String title, final TextView textView) {
		selectCount = 0;
		MyDialog dialog = new MyDialog(context);
		dialog.setTitle(title);
		dialog.setSingleChoiceItems(stringList, selectCount,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectCount = which;
					}
				});
		dialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		dialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String sex = stringList[selectCount];
				textView.setBackgroundColor(color.transparent);
				textView.setText(sex);
			}
		});
		dialog.create(null).show();

	}

	// 弹出日期选择器 yyyy-mm-dd
	private void showDateTimePicker() {

		String dateStr = "";
		dateStr = timeText.getText().toString().trim();
		if (dateStr.equals("")) {
			dateStr = "1960-01-01";
		}

		Date date = DateUtil.dateFromString(dateStr, DateUtil.YEAR_MONTH_DAY);
		MyDialog dialog = new MyDialog(context).setTitle(R.string.check_time)
				.setDateTimePicker(date, DateType.YEAR_MONTH_DAY)
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				}).setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 如果是个数,则显示为"02"的样式
						WheelView wv_year = (WheelView) v
								.findViewById(R.id.year);
						WheelView wv_month = (WheelView) v
								.findViewById(R.id.month);
						WheelView wv_day = (WheelView) v.findViewById(R.id.day);

						String parten = "00";
						DecimalFormat decimal = new DecimalFormat(parten);
						// 设置日期的显示
						String text = (wv_year.getCurrentItem() + MyDialog.START_YEAR)
								+ "-"
								+ decimal.format((wv_month.getCurrentItem() + 1))
								+ "-"
								+ decimal.format((wv_day.getCurrentItem() + 1));
						timeText.setBackgroundColor(color.transparent);
						timeText.setText(text);
					}
				});

		dialog.create(null).show();
	}

	private void saveToDb() {
		HuLiLife huliLife = new HuLiLife();
		huliLife.uid = ConstantUtil.getUid(context);
		huliLife.sync = 0;
		String time = timeText.getText().toString().trim();
		if(time != null && time.length() >0){
			huliLife.date = time;
			huliLife.dateMonth = time.substring(0, 7);
			huliLife.day = time.substring(8, 10);
		}

		huliLife.time = DateUtil.stringFromDate(new Date(),DateUtil.DATE_HOUR_MINUTE);
		huliLife.salt = saltText.getText().toString();
		huliLife.water = waterText.getText().toString();
		huliLife.weight = weightText.getText().toString();
		huliLife.food = foodText.getText().toString();
		huliLife.sport = sportText.getText().toString();
		HuLiLifeDB huliLifeDb = new HuLiLifeDB(context);
		huliLifeDb.insertHuliLifeData(huliLife);
	}

}
