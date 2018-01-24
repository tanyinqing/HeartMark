package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.ceshi.PromptManager;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.PingGuResult;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.PicChartUtil;
import com.yikang.heartmark.view.PieChart01View;
import com.yikang.heartmark.view.ProgressDoubleView;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.widget.MyDialog;
@SuppressWarnings("unused")
public class PingGuStageActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	private TextView result;
	private TextView hint;
	private TextView per;
	private TextView risk;
	private LinearLayout cakeLayout;
	private LinearLayout resultLayout;
	private ProgressDoubleView progressView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinggu_stage_result);
		init();
		//PromptManager.showToastTest(this,"这个是从评估历史 跳转过来的评估状态说明的页面PingGuStageActivity");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.pingguResultTopBar);
		topbar.setTopbarTitle(R.string.evaluate);
		topbar.setOnTopbarLeftButtonListener(this);
		
		result = (TextView) findViewById(R.id.pinggu_result_result);
		hint = (TextView) findViewById(R.id.pinggu_result_hint);
		per = (TextView) findViewById(R.id.pinggu_result_per);
		risk = (TextView) findViewById(R.id.pinggu_result_risk);
		cakeLayout = (LinearLayout) findViewById(R.id.pinggu_result_cake_layout);
		resultLayout = (LinearLayout) findViewById(R.id.pinggu_result_resultLL);
		progressView = (ProgressDoubleView) findViewById(R.id.pingguResultProgress);
		
		Intent intent = this.getIntent(); 
		PingGuResult resultObject=(PingGuResult)intent.getSerializableExtra("pingguResult");
		
		if(TextUtils.isEmpty(resultObject.result)){
			if(resultObject.from.equals(PingGuResult.INFO)){
				showPingGuDialog();
			}
		}else{
			//result.setText(resultObject.result);
		}
		
		
		result.setText(resultObject.result);
		hint.setText(resultObject.hint);
		risk.setText(resultObject.risk);
		
		if(resultObject.per != null){
			int percentInt = 0;
			Double percent = Double.valueOf(resultObject.per) * 100;
			percentInt = percent.intValue();
			if(percentInt > 90){
				percentInt = 90;
			}
			
			per.setText(percentInt+"%");
			
			progressView.setProgress(percentInt);
			
			//饼形图
			//View pieChart = new PicChartUtil().execute(this,100-percent.intValue(),percent.intValue());
			PieChart01View pieChart = new PieChart01View(PingGuStageActivity.this);
			pieChart.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			pieChart.initView(percentInt);
			cakeLayout.addView(pieChart);
		}
		
	}

	private void showPingGuDialog() {
		MyDialog dialog = new MyDialog(PingGuStageActivity.this)
				.setTitle(R.string.pinggu_title)
				.setMessage(R.string.pinggu_result_msg)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent celingIntent = new Intent(PingGuStageActivity.this, MainCeLingActivity.class);
						celingIntent.putExtra(ConstantUtil.CELING_FROM, "main");
						startActivity(celingIntent);
						finish();
					}
				})
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
					}
				});

		dialog.create(null);
		dialog.showMyDialog();
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
