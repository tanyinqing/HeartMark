package com.yikang.heartmark.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;

@SuppressLint("HandlerLeak")
public class MainSetAboutActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private String version;
	private String content;
	private String versionUrl;
	private final int MSG_SHOWUPGRADE = 11;
	private final int MSG_DOAWNLOAD = 12;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_set_about);
		init();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.setOtherTopBar);
		topbar.setTopbarTitle(R.string.set_other);
		topbar.setRightTextGone();
		topbar.setOnTopbarLeftButtonListener(this);
		
		TextView updateVersionNum = (TextView) findViewById(R.id.updateVersionNum);
		updateVersionNum.setTypeface(Typeface.DEFAULT_BOLD, Typeface.ITALIC);
		updateVersionNum.setText("版本:" + ConnectUtil.versionName + getVersionName());
		RelativeLayout setFriend = (RelativeLayout) findViewById(R.id.set_friend);
		RelativeLayout setFriendData = (RelativeLayout) findViewById(R.id.set_friend_data);
		RelativeLayout setInfo = (RelativeLayout) findViewById(R.id.set_info);
		RelativeLayout setCollection = (RelativeLayout) findViewById(R.id.set_collection);
		//RelativeLayout setCache = (RelativeLayout) findViewById(R.id.set_cache);
		RelativeLayout setUpdate = (RelativeLayout) findViewById(R.id.set_update);
		//RelativeLayout setFeel = (RelativeLayout) findViewById(R.id.set_feel);
		RelativeLayout setAbout = (RelativeLayout) findViewById(R.id.set_about);
		RelativeLayout setLimits = (RelativeLayout) findViewById(R.id.set_limits);

		setFriend.setOnClickListener(new MyViewOnclicklistener());
		setFriendData.setOnClickListener(new MyViewOnclicklistener());
		setInfo.setOnClickListener(new MyViewOnclicklistener());
		setCollection.setOnClickListener(new MyViewOnclicklistener());
		//setCache.setOnClickListener(new MyViewOnclicklistener());
		setUpdate.setOnClickListener(new MyViewOnclicklistener());
		//setFeel.setOnClickListener(new MyViewOnclicklistener());
		setAbout.setOnClickListener(new MyViewOnclicklistener());
		setLimits.setOnClickListener(new MyViewOnclicklistener());
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.set_friend:
				if(ConnectUtil.isLogin(MainSetAboutActivity.this)){
					Intent intentFriend = new Intent(MainSetAboutActivity.this, SetFriendActivity.class);
					startActivity(intentFriend);
				}else{
					Intent intentFriend = new Intent(MainSetAboutActivity.this, LoginActivity.class);
					startActivity(intentFriend);
				}
				break;
			case R.id.set_friend_data:
				if(ConnectUtil.isLogin(MainSetAboutActivity.this)){
					Intent intentFriend = new Intent(MainSetAboutActivity.this, MainNewsActivity.class);
					startActivity(intentFriend);
				}else{
					Intent intentFriend = new Intent(MainSetAboutActivity.this, LoginActivity.class);
					startActivity(intentFriend);
				}
				break;
			case R.id.set_info:
				if(ConnectUtil.isLogin(MainSetAboutActivity.this)){
					Intent collectIntent = new Intent(MainSetAboutActivity.this, CenterInfoActivity.class);
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetAboutActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_collection:
				if(ConnectUtil.isLogin(MainSetAboutActivity.this)){
					Intent collectIntent = new Intent(MainSetAboutActivity.this, SetCollectionActivity.class);
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetAboutActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_update:
				doUpdate();
				break;
			case R.id.set_about:
				if(ConnectUtil.isLogin(MainSetAboutActivity.this)){
					Intent collectIntent = new Intent(MainSetAboutActivity.this, SetAboutActivity.class);
					collectIntent.putExtra("set", "about");
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetAboutActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_limits:
				if(ConnectUtil.isLogin(MainSetAboutActivity.this)){
					Intent collectIntent = new Intent(MainSetAboutActivity.this, SetAboutActivity.class);
					collectIntent.putExtra("set", "limit");
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetAboutActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			}
		}
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 弹出升级框
			case MSG_SHOWUPGRADE:
				showUpgradeDialog();
				break;
			// 进行下载
			case MSG_DOAWNLOAD:
				doawnload();
				break;
			}
		};
	};

	// 升级
	private void showUpgradeDialog() {
		MyDialog dialog = new MyDialog(MainSetAboutActivity.this)
				.setTitle(R.string.upgrade_title)
				.setMessage(ConnectUtil.versionNameString + version + "\n更新内容有:\n" + content)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Message message = Message.obtain();
						message.what = MSG_DOAWNLOAD;
						handler.sendMessage(message);
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

	// Version请求
	private void doUpdate() {
		Map<String, String> params = new HashMap<String, String>();
		if (ConnectUtil.isConnect(AppContext.context)) {
			ConnectionManager.getInstance().send("CM01080WD00",
					"queryAndroidLastVersion", params,
					"getVersionSucessCallBack", this);
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		} else {
			ShowUtil.showToast(AppContext.context, R.string.check_network);
		}
	}

	// Version返回
	@SuppressWarnings("rawtypes")
	public void getVersionSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MainSetAboutActivity.this,
					R.string.check_network_timeout);
		} else {
			version = (String) resultMap.get("VERSION_NO");
			content = (String) resultMap.get("CONTENT");
			content = content.replace(";", ";\n");
			versionUrl = (String) resultMap.get("DOWN_URL");
			// 无需更新
			String v = getVersionName();
			if (version.equals(v)) {
				ShowUtil.showToast(MainSetAboutActivity.this, R.string.version_new);
			} else {
				Message message = Message.obtain();
				message.what = MSG_SHOWUPGRADE;
				handler.sendMessage(message);
			}
		}

	}

	// 下载
	MyDialog m_pDialog;
	public void doawnload() {
		HttpUtils utils = new HttpUtils();
		m_pDialog = new MyDialog(MainSetAboutActivity.this);
		m_pDialog.setTitle("提示");
		m_pDialog.setMessage("心衰版本更新中...");
		//m_pDialog.setIcon(R.drawable.heart_mark_icon);
		//m_pDialog.setIndeterminate(false);
		//m_pDialog.setCancelable(false);
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
		utils.download(versionUrl, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/HeartMark" + version + ".apk",
				new RequestCallBack<File>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {// 下载失败
						ShowUtil.showToast(MainSetAboutActivity.this,
								R.string.download_fail);
						m_pDialog.dismiss();
					}

					// 下载成功
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						m_pDialog.dismiss();
						// 下载成功，调用系统安装程序进行程序的安装
						installAPk();
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
	public void installAPk() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/HeartMark" + version + ".apk")),
				"application/vnd.android.package-archive");
		/**
		 * 安装后进行函数的回调
		 */
		this.startActivityForResult(intent, 100);
	}

	// 获取版本号
	public String getVersionName() {
		PackageManager manager = getPackageManager();// packageManager
		try {
			// 获取哪个应用程序的清单文件，getPackageName()指获取当前程序的包名
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
					0);
			return packageInfo.versionName;// 获取清单文件中的版本信息
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}

	}
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
