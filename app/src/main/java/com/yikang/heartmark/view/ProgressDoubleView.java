package com.yikang.heartmark.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.heartmark.R;

public class ProgressDoubleView extends LinearLayout {
	private SeekBar seekBarTop;
	private SeekBar seekBarBottom;

	public ProgressDoubleView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.progress_double_layout, this, true);
		init();
	}

	public ProgressDoubleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.progress_double_layout, this, true);
		init();
	}

	private void init() {
		seekBarTop = (SeekBar) findViewById(R.id.seekTop);
		seekBarBottom = (SeekBar) findViewById(R.id.seekBottom);
		seekBarTop.setEnabled(false);
		seekBarBottom.setEnabled(false);
	}

	public void setProgress(int progress) {
		seekBarTop.setProgress(progress);
		seekBarBottom.setProgress(progress);
	}

}
