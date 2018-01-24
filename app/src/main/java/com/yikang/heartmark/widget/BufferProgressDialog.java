package com.yikang.heartmark.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.heartmark.R;

public class BufferProgressDialog {
	private Dialog progress;

	@SuppressLint("InflateParams")
	public BufferProgressDialog(final Context context) {

		final Dialog dialog = new Dialog(context, R.style.BufferDialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.buffer_dialog, null);

		dialog.setContentView(v);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		progress = dialog;
	}

	public void destroyProgressDialog() {
		progress.cancel();
		
		//取消网络请求
	}

	public Dialog get_progress() {
		return progress;
	}
}
