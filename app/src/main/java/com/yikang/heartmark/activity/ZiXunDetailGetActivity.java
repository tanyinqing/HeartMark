package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.PlaySoundUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class ZiXunDetailGetActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener,
		OnClickListener {

	private Button buttonYuYin;
	private WebView webView;

	private PlaySoundUtil playSound;
	private ZiXun zixun = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zixun_detail_get);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		playSound.stopSound();
		zixun = null;
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.zixunDetailTopBar);
		topbar.setTopbarTitle(R.string.zixun_detail);
		topbar.setOnTopbarLeftButtonListener(this);
		//topbar.setRightButton(R.drawable.share_img);
		//topbar.setOnTopbarRightButtonListener(this);

		playSound = new PlaySoundUtil(ZiXunDetailGetActivity.this);
		buttonYuYin = (Button) findViewById(R.id.detail_yuyin);
		buttonYuYin.setOnClickListener(this);

		webView = (WebView) findViewById(R.id.zixun_webview);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLoadWithOverviewMode(true);

		Intent intent = this.getIntent();
		zixun = (ZiXun) intent.getSerializableExtra("newItem");

		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
		Map<String, String> params = new HashMap<String, String>();
		params.put("newsId", zixun.newId);
		ConnectionManager.getInstance().send("FN11050WL00", "queryNewsDetailById",
				params, "detailCallBackHandler", this);

	}

	@SuppressWarnings("rawtypes")
	public void detailCallBackHandler(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ZiXunDetailGetActivity.this,R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				String body = (String) resultMap.get("CONTENT");
				String showBody = null;
				if(body != null &&  !body.equals("")){
					showBody = body.replace("upload", ConnectUtil.HOST_URL+"/upload");
				}else{
					showBody = body;
				}
				webView.loadDataWithBaseURL(null, showBody, "text/html","utf-8", null);
				zixun.contentRead = (String) resultMap.get("CONTENT_TEMP");
			} else if (result.equals("fail")) {

			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail_yuyin:
			playSound(buttonYuYin);
			break;
		}
	}

	/**
	 * 语音播报
	 * @param buttonYuYin2 
	 */
	private void playSound(Button buttonYuYin) {
		playSound.playSound(zixun.contentRead,buttonYuYin);
	}

	/**
	 * 分享客户端
	 * 
	 * @param contextString
	 */
	private void showShare(String contextString) {
		ShareSDK.initSDK(this);

		OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.heart_mark_icon,
				this.getString(R.string.app_name));
		// oks.setAddress("12345678901");// address是接收人地址，仅在信息和邮件使用
		// oks.setTitle(this.getString(R.string.share)); //
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn"); //
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// text是分享文本，所有平台都需要这个字段
		oks.setText(contextString);
		// oks.setImagePath(MainActivity.TEST_IMAGE);//
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
		// 微信的两个平台、Linked-In支持此字段
		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.baidu.com");
		// appPath是待分享应用程序的本地路劲，仅在微信中使用
		// oks.setAppPath(MainActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment(getContext().getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(this.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.baidu.com");
		// venueName是分享社区名称，仅在Foursquare使用
		// oks.setVenueName("Southeast in China");
		// venueDescription是分享社区描述，仅在Foursquare使用
		// oks.setVenueDescription("This is a beautiful place!");
		// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
		// oks.setLatitude(23.122619f);
		// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
		// oks.setLongitude(113.372338f);
		// 是否直接分享（true则直接分享）
		oks.setSilent(true);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
		// if (platform != null) {
		// oks.setPlatform(platform);
		// }
		// 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
		// oks.setCallback(new OneKeyShareCallback());
		// 通过OneKeyShareCallback来修改不同平台分享的内容
		// oks.setShareContentCustomizeCallback(
		// new ShareContentCustomizeDemo());
		oks.show(this);
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		showShare("心衰管理");
	}
}
