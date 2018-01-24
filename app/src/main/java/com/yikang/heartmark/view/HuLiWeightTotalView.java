package com.yikang.heartmark.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.HuLiWeightInfoActivity;
import com.yikang.heartmark.view.MenuBarView.OnMenuBarListener;

public class HuLiWeightTotalView extends RelativeLayout implements
    OnMenuBarListener {
	private MenuBarView menuBarView;
	private ViewFlipper flipperView;
	private Context context;
	private HuLiWeightWeekView weekView;
	private HuLiWeightView weightView;
	
	public HuLiWeightTotalView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.huli_weight_total_view,this, true);
		init();
	}
	
	public HuLiWeightTotalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.huli_weight_total_view, this, true);
		init();
	}
	
	
	private void init(){
		flipperView = (ViewFlipper) findViewById(R.id.total_viewFlipper);
		weekView = (HuLiWeightWeekView) findViewById(R.id.weekView);
		weightView = (HuLiWeightView) findViewById(R.id.weightView);
		
		menuBarView = (MenuBarView) findViewById(R.id.total_menuBarView);
		menuBarView.setOnMenuBarListener(this);
		menuBarView.setSelectedIndex(0);
		
		Button detail = (Button) findViewById(R.id.total_detail);
		detail.setOnClickListener(listener);
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, HuLiWeightInfoActivity.class);
			context.startActivity(intent);
		}
	};

	@Override
	public void onMenuBarItemSelected(int menuIndex) {
		flipperView.removeAllViews();
		switch (menuIndex) {
		case 0:
			flipperView.addView(weekView);
			break;
		case 1:
			flipperView.addView(weightView);
			break;
		}
	}
}
