package com.yikang.heartmark.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heartmark.R;

//自定义顶部导航栏
public class TopBarView extends RelativeLayout {

	private OnTopbarRightButtonListener onTopbarRightButtonListener = null;
	private OnTopbarLeftButtonListener onTopbarLeftButtonListener = null;

	private RelativeLayout mainItemLayout = null;
	private TextView topTitleTextView = null;
	private RelativeLayout leftButton = null;
	private ImageView leftImage = null;
	private ImageButton rightButton = null;
	private TextView rightText = null;

	public TopBarView(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.customtopbar, this, true);
		init();
	}

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.customtopbar, this, true);
		init();
	}

	private void init() {

		topTitleTextView = (TextView) findViewById(R.id.topbarTitle);
		leftButton = (RelativeLayout) findViewById(R.id.topbarLeftButton);
		leftImage = (ImageView) findViewById(R.id.topbarLeftImage);
		rightButton = (ImageButton) findViewById(R.id.topbarRightButton);
		rightText = (TextView) findViewById(R.id.topbarRightText);
		mainItemLayout = (RelativeLayout) findViewById(R.id.mainItemLayout);

		leftButton.setVisibility(View.VISIBLE);

		leftButton.setOnClickListener(new MyViewOnClickListener());
		leftImage.setOnClickListener(new MyViewOnClickListener());
		rightButton.setOnClickListener(new MyViewOnClickListener());
		rightText.setOnClickListener(new MyViewOnClickListener());
	}

	public void setTopbarTitle(int titleName) {
		if (topTitleTextView != null) {
			topTitleTextView.setText(titleName);
		}
	}

	public void setTopbarTitleColor(int color) {
		if (topTitleTextView != null) {
			topTitleTextView.setTextColor(getResources().getColor(color));
		}
	}

	public void setTopbarBackground(int drawable) {
		if (mainItemLayout != null) {
			mainItemLayout.setBackgroundResource(drawable);
		}
	}

	public void setLeftButton(int resBackgroundId) {
		if (leftButton != null) {
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setBackgroundResource(resBackgroundId);
		}
	}

	public void setRightButton(int resBackgroundId) {
		if (rightButton != null) {
			rightButton.setVisibility(View.VISIBLE);
			rightButton.setBackgroundResource(resBackgroundId);
		}
	}

	public void setRightText(String rightTextInfo) {
		if (rightText != null) {
			rightText.setVisibility(View.VISIBLE);
			rightText.setText(rightTextInfo);
		}
	}
	
	public void setLeftButtonGone() {
		leftButton.setVisibility(View.GONE);
	}

	public void setRightButtonVisible(boolean isVisible) {
		if(isVisible){
			rightButton.setVisibility(View.VISIBLE);
		}else{
			rightButton.setVisibility(View.GONE);
		}
		
	}
	
	public void setRightTextGone() {
		rightText.setVisibility(View.GONE);
	}

	public void setOnTopbarRightButtonListener(
			OnTopbarRightButtonListener listener) {
		onTopbarRightButtonListener = listener;
	}

	public void setOnTopbarLeftButtonListener(
			OnTopbarLeftButtonListener listener) {
		onTopbarLeftButtonListener = listener;
	}

	public interface OnTopbarRightButtonListener {
		public void onTopbarRightButtonSelected();
	}

	public interface OnTopbarLeftButtonListener {
		public void onTopbarLeftButtonSelected();
	}

	class MyViewOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int rid = v.getId();
			if (rid == R.id.topbarLeftButton || rid == R.id.topbarLeftImage) {
				if (onTopbarLeftButtonListener != null) {
					onTopbarLeftButtonListener.onTopbarLeftButtonSelected();
				}
			} else if (rid == R.id.topbarRightButton || rid == R.id.topbarRightText) {
				if (onTopbarRightButtonListener != null) {
					onTopbarRightButtonListener.onTopbarRightButtonSelected();
				}
			}
		}

	}

}