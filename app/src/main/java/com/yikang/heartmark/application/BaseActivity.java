package com.yikang.heartmark.application;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.UmengHelper;
import com.yikang.heartmark.widget.BufferProgressDialog;

public class BaseActivity extends Activity {

	public BufferProgressDialog profressDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// umeng
        UmengHelper.onError(this);
        UmengHelper.updateOnlineConfig(this);
	}
	 
	@Override
	public void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		UmengHelper.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		
		UmengHelper.onPause(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@SuppressLint("HandlerLeak")
	public Handler handlerBase = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantUtil.DIALOG_SHOW:
				profressDialog = new BufferProgressDialog(
						BaseActivity.this);
				break;
			case ConstantUtil.DIALOG_HINT:
				if (profressDialog != null) {
					profressDialog.destroyProgressDialog();
					profressDialog = null;
				}
				break;
			default:
				break;
			}
		};
	};
}
