package com.yikang.heartmark.activity;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.widget.MyDialog;

public class MainServiceOtherActivity extends BaseActivity implements
    OnTopbarLeftButtonListener, OnClickListener{
	
	@ViewInject(R.id.main_service_other_dowmload01)
	private TextView dowmload01;
	@ViewInject(R.id.main_service_other_dowmload02)
	private TextView dowmload02;
	@ViewInject(R.id.main_service_other_dowmload03)
	private TextView dowmload03;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_other);
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceOtherTopBar);
		topbar.setTopbarTitle(R.string.service_other);
		topbar.setOnTopbarLeftButtonListener(this);
		ViewUtils.inject(this);
		dowmload01.setOnClickListener(this);
		dowmload02.setOnClickListener(this);
		dowmload03.setOnClickListener(this);
	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_service_other_dowmload01:
			String dowmload01 = "http://www.e-care365.com/app/poctor.apk";
			showUpgradeDialog("普博士手机血糖APP下载中...",dowmload01,"poctor");
			break;
		case R.id.main_service_other_dowmload02:
			MyToast.show(this, "敬请期待爱心脏app", Toast.LENGTH_LONG);
			break;
		case R.id.main_service_other_dowmload03:
			MyToast.show(this, "敬请期待同心管家app", Toast.LENGTH_LONG);
			break;
		default:
			break;
		}
	}
	
	//提示
	MyDialog dialog;
	private void showUpgradeDialog(final String content, final String dowmload012, final String string2) {
		dialog = new MyDialog(MainServiceOtherActivity.this)
				.setTitle("提示")
				.setMessage("是否下载普博士手机血糖app")
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						doawnload(content,dowmload012,string2);
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
	
	// 下载
	MyDialog m_pDialog;
	public void doawnload(String context, String dowmload, final String name) {
		HttpUtils utils = new HttpUtils();
		m_pDialog = new MyDialog(MainServiceOtherActivity.this);
		m_pDialog.setTitle("提示");
		m_pDialog.setMessage(context);
		m_pDialog.setProgressbar();
		m_pDialog.create(null);
		m_pDialog.showMyDialog();
		/**
		 * 判断SD的状态：如果SD不存在就进行友好提示
		 */
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyToast.show(getApplicationContext(), "手机SD卡不存在",
					Toast.LENGTH_SHORT);
			return;
		}
		/*
		 * 参数： 1 下载url地址 2 下载到哪个位置 3 回调方法
		 */
		utils.download(dowmload, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/"+name+".apk",
				new RequestCallBack<File>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {// 下载失败
						ShowUtil.showToast(MainServiceOtherActivity.this,R.string.download_fail);
						m_pDialog.dismiss();
					}

					// 下载成功
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						m_pDialog.dismiss();
						installAPk(name);
					}

					@Override
					public void onLoading(final long total, final long current,
							boolean isUploading) {// 下载进度
						super.onLoading(total, current, isUploading);
						int progress = (int) (current * 100 / total);
						m_pDialog.setProgress(progress);
					}
				});
	}

	// 安装
	public void installAPk(String name) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/" + name + ".apk")),
				"application/vnd.android.package-archive");
		this.startActivityForResult(intent, 100);
	}
}
