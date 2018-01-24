package com.yikang.heartmark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class EvaluateActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private TextView textTitle;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private Button check1;
	private Button check2;
	private Button check3;
	private Button next;

	private String[] textTitleList;
	private String[] textList1;
	private String[] textList2;
	private String[] textList3;

	private int count = 0;
	private int score = 0; // 分数
	private int thisScore = 0;  //这次的分数

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.evaluateTopBar);
		topbar.setTopbarTitle(R.string.evaluate);
		topbar.setOnTopbarLeftButtonListener(this);

		textTitleList = getResources().getStringArray(
				R.array.evaluate_title_list);
		textList1 = getResources().getStringArray(R.array.evaluate_text1_list);
		textList2 = getResources().getStringArray(R.array.evaluate_text2_list);
		textList3 = getResources().getStringArray(R.array.evaluate_text3_list);

		textTitle = (TextView) findViewById(R.id.evaluate_title);
		text1 = (TextView) findViewById(R.id.evaluate_text1);
		text2 = (TextView) findViewById(R.id.evaluate_text2);
		text3 = (TextView) findViewById(R.id.evaluate_text3);
		check1 = (Button) findViewById(R.id.evaluate_check1);
		check2 = (Button) findViewById(R.id.evaluate_check2);
		check3 = (Button) findViewById(R.id.evaluate_check3);
		check1.setOnClickListener(new MyViewOnclicklistener());
		check2.setOnClickListener(new MyViewOnclicklistener());
		check3.setOnClickListener(new MyViewOnclicklistener());
		next = (Button) findViewById(R.id.evaluate_next);

		next.setOnClickListener(listener);
		refreshView(count);
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			check1.setSelected(false);
			check2.setSelected(false);
			check3.setSelected(false);
			count++;
			score += thisScore;
			if(count <= 9){
				if (count == 9) {
					next.setText("完成");
					thisScore = 0;
				}
				refreshView(count);
			}else if(count == 10){
				
				check1.setVisibility(View.GONE);
				check2.setVisibility(View.GONE);
				check3.setVisibility(View.GONE);
				text1.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);
				
				if(score >0 && score <=6){
					textTitle.setText("心衰风险较低，请注意及时检查,保持正常的生活作息时间,坚持合理运动。");
				}else if(score >6 && score <=12){
					textTitle.setText("心衰风险较高,请您注意合理的生活作息,适量运动,不吸烟喝酒,保持良好的心态,定期到医院复查。");
				}else if(score >12 ){
					textTitle.setText("心衰风险程度很高,请您按时测量体重,注意食盐量,按时记录出入水量,若有不适请及时就诊就医。");
				}
			}else{
				finish();
				Intent huliIntent = new Intent(EvaluateActivity.this, MainHuLiActivity.class);
				startActivity(huliIntent);
			}
			
		}
	};

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			if(count < 9){
				switch (rid) {
				case R.id.evaluate_check1:
					check1.setSelected(true);
					check2.setSelected(false);
					check3.setSelected(false);
                    thisScore = 0;
					break;
				case R.id.evaluate_check2:
					check1.setSelected(false);
					check2.setSelected(true);
					check3.setSelected(false);
					thisScore = 1;
					break;
				case R.id.evaluate_check3:
					check1.setSelected(false);
					check2.setSelected(false);
					check3.setSelected(true);
					thisScore = 2;
					break;
				default:
					break;
				}
			}else{
				switch (rid) {
				case R.id.evaluate_check1:
					if(check1.isSelected()){
						check1.setSelected(false);
						thisScore -= 0;
					}else{
						check1.setSelected(true);
						thisScore += 0;
					}
					break;
				case R.id.evaluate_check2:
					if(check2.isSelected()){
						check2.setSelected(false);
						thisScore -= 1;
					}else{
						check2.setSelected(true);
						thisScore += 1;
					}
					break;
				case R.id.evaluate_check3:
					if(check3.isSelected()){
						check3.setSelected(false);
						thisScore -= 1;
					}else{
						check3.setSelected(true);
						thisScore += 1;
					}
					break;
				default:
					break;
				}
			}
		}
	}

	private void refreshView(int count) {
		textTitle.setText(textTitleList[count]);
		text1.setText(textList1[count]);
		text2.setText(textList2[count]);
		text3.setText(textList3[count]);
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
