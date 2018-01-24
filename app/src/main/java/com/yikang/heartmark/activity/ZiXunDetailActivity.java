package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.database.GoodDB;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.PlaySoundUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yuzhi.framework.util.ConnectionManager;

public class ZiXunDetailActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnTopbarRightButtonListener,
		OnClickListener {

	private Button buttonYuYin;
	private Button buttonGood;
	private Button buttonHouse;
	private WebView webView;

	private TopBarView topbar;
	private PlaySoundUtil playSound;
	private ZiXun zixun = null;
	private GoodDB goodDB;//对点赞的判断

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zixun_detail);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		playSound.stopSound();
		zixun = null;
	}

	private void init() {
		topbar = (TopBarView) findViewById(R.id.zixunDetailTopBar);
		topbar.setTopbarTitle(R.string.zixun_detail);
		topbar.setOnTopbarLeftButtonListener(this);
		//topbar.setRightButton(R.drawable.share_img);
		//topbar.setOnTopbarRightButtonListener(this);

		goodDB = new GoodDB(ZiXunDetailActivity.this);
		playSound = new PlaySoundUtil(ZiXunDetailActivity.this);
		buttonYuYin = (Button) findViewById(R.id.detail_yuyin);
		buttonGood = (Button) findViewById(R.id.detail_good);
		buttonHouse = (Button) findViewById(R.id.detail_house);
		buttonYuYin.setOnClickListener(this);
		buttonGood.setOnClickListener(this);
		buttonHouse.setOnClickListener(this);

		webView = (WebView) findViewById(R.id.zixun_webview);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLoadWithOverviewMode(true);

		Intent intent = this.getIntent();
		zixun = (ZiXun) intent.getSerializableExtra("newItem");

		refreseData(topbar);

		
		if (zixun.type.equals(ZiXun.TYPE_ZIXUN)) {
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			Map<String, String> params = new HashMap<String, String>();
			params.put("newsId", zixun.newId);
			ConnectionManager.getInstance().send("FN11050WL00",
					"queryNewsDetailById", params, "detailCallBackHandler",this);
		} else if (zixun.type.equals(ZiXun.TYPE_EXPLAIN)) {
			handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			Map<String, String> params = new HashMap<String, String>();
			params.put("drugId", zixun.newId);
			ConnectionManager.getInstance().send("CM01090WD00",
					"queryDrugInfoById", params, "yaoDetailCallBack", this);
			// String body = zixun.content;
			// String showBody = null;
			// if(body != null && !body.equals("")){
			// showBody = body.replace("upload",
			// ConnectUtil.HOST_URL+"/upload");
			// }else{
			// showBody = body;
			// }
			//
			// webView.loadDataWithBaseURL(null, showBody, "text/html",
			// "utf-8",null);
		}

	}

	// 咨询页的详情
	@SuppressWarnings("rawtypes")
	public void detailCallBackHandler(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ZiXunDetailActivity.this,R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				String body = (String) resultMap.get("CONTENT");
				String showBody = null;
				if (body != null && !body.equals("")) {
					showBody = body.replace("upload", ConnectUtil.HOST_URL+ "/upload");
				} else {
					showBody = body;
				}
				webView.loadDataWithBaseURL(null, showBody, "text/html","utf-8", null);
				zixun.contentRead = (String) resultMap.get("CONTENT_TEMP");
			} else if (result.equals("fail")) {

			}
		}
	}

	// 药品详情
	@SuppressWarnings("rawtypes")
	public void yaoDetailCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ZiXunDetailActivity.this,
					R.string.check_network_timeout);
		} else {
			String body = (String) resultMap.get("CONTENT");
			String showBody = null;
			if (body != null && !body.equals("")) {
				//用一个新的字符串替代老的字符串
				showBody = body.replace("upload", ConnectUtil.HOST_URL+ "/upload");
			} else {
				showBody = body;
			}
			//用webView显示一个字符串
			webView.loadDataWithBaseURL(null, showBody, "text/html", "utf-8",null);
			zixun.contentRead = (String) resultMap.get("CONTENT_TEMP");
		}
	}

	private void refreseData(TopBarView topbar) {
		//如果状态是咨询
		if (zixun.type.equals(ZiXun.TYPE_ZIXUN)) {
			// 如果数据库里已经点赞，在点赞按钮不能点击
			if (goodDB.isHave(zixun.newId)) {
				buttonGood.setSelected(true);
				buttonGood.setClickable(false);
			} else {
				buttonGood.setSelected(false);
				buttonGood.setClickable(true);
			}

			// 查看数据库的状态是否收藏
			if (zixun.house.equals("1")) {
				buttonHouse.setSelected(true);
			} else {
				buttonHouse.setSelected(false);
			}
		} else if (zixun.type.equals(ZiXun.TYPE_EXPLAIN)) {
			topbar.setRightButtonVisible(false);
			buttonGood.setVisibility(View.GONE);
			buttonHouse.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		showShare("心衰管理", zixun.newId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail_yuyin:
			playSound(buttonYuYin);
			break;
		case R.id.detail_good:
			doGood();
			msgIsTrue();
			break;
		case R.id.detail_house:
			if (zixun.house.equals("1")) {
				loseHouse();
			} else if (zixun.house.equals("0")) {
				doHouse();
			}
			msgIsTrue();
			break;
		}
	}

	public void msgIsTrue() {
		Message msg = new Message();
		msg.what = 1;
		MainZiXunActivity.getInstance().mHandler.sendMessage(msg);
	}

	/**
	 * 语音播报
	 * 
	 * @param context
	 * @param buttonYuYin2
	 */
	private void playSound(Button buttonYuYin) {
		playSound.playSound(zixun.contentRead, buttonYuYin);
	}

	/**
	 * 点赞
	 */
	private void doGood() {
		//判断网络是否连接
		if (!ConnectUtil.isConnect(ZiXunDetailActivity.this)) {
			ShowUtil.showToast(ZiXunDetailActivity.this, R.string.check_network);
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("newsId", zixun.newId);
		ConnectionManager.getInstance().send("FN11050WL00", "updateNewsPraise",
				params, "goodCallBackHandler", this);
	}

	@SuppressWarnings("rawtypes")
	public void goodCallBackHandler(Object data) {
		Map resultMap = (Map) data;
		// 网络连接超时
		if (resultMap == null) {
			ShowUtil.showToast(ZiXunDetailActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				ShowUtil.showToast(ZiXunDetailActivity.this,R.string.good_success);
				buttonGood.setSelected(true);
				buttonGood.setClickable(false);
				//如果点赞成功则点赞表中加入一个id
				new GoodDB(ZiXunDetailActivity.this).insertGood(zixun.newId);
			} else if (result.equals("fail")) {
				ShowUtil.showToast(ZiXunDetailActivity.this, R.string.good_fail);
			}
		}
	}

	/**
	 * 咨询收藏
	 */
	private void doHouse() {
		if (!ConnectUtil.isLogin(ZiXunDetailActivity.this)) {
			ShowUtil.showToast(ZiXunDetailActivity.this, R.string.login_please);
			return;
		}
		if (!ConnectUtil.isConnect(ZiXunDetailActivity.this)) {
			ShowUtil.showToast(ZiXunDetailActivity.this, R.string.check_network);
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("newsId", zixun.newId);
		ConnectionManager.getInstance().send("FN11050WL00", "saveNewsCollect",
				params, "houseCallBackHandler", this);
	}

	@SuppressWarnings("rawtypes")
	public void houseCallBackHandler(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ZiXunDetailActivity.this,R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				zixun.house = 1 + "";
				ShowUtil.showToast(ZiXunDetailActivity.this,R.string.house_success);
				refreseData(topbar);
			} else if (result.equals("fail")) {
				ShowUtil.showToast(ZiXunDetailActivity.this,R.string.house_fail);
			}
		}
	}

	private void loseHouse() {
		if (!ConnectUtil.isLogin(ZiXunDetailActivity.this)) {
			ShowUtil.showToast(ZiXunDetailActivity.this, R.string.login_please);
			return;
		}
		if (!ConnectUtil.isConnect(ZiXunDetailActivity.this)) {
			ShowUtil.showToast(ZiXunDetailActivity.this, R.string.check_network);
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("newsId", zixun.newId);
		ConnectionManager.getInstance().send("FN11050WL00",
				"deleteNewsCollect", params, "loseHouseCallBackHandler", this);
	}

	@SuppressWarnings("rawtypes")
	public void loseHouseCallBackHandler(Object data) {
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(ZiXunDetailActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				zixun.house = 0 + "";
				ShowUtil.showToast(ZiXunDetailActivity.this,R.string.house_lose);
				refreseData(topbar);
			} else if (result.equals("fail")) {
				ShowUtil.showToast(ZiXunDetailActivity.this,R.string.house_fail);
			}
		}
	}

	/**
	 * 分享客户端
	 * 
	 * @param contextString
	 * @param newId
	 */
	private void showShare(String contextString, String newId) {
		ShareSDK.initSDK(this);

		String url = ConnectUtil.HOST_URL
				+ "FI01080WD00P!querySbtNewsDetailPages.htm?uuid=" + newId;
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
		oks.setText(contextString + url);
		// oks.setImagePath(MainActivity.TEST_IMAGE);//
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
		// 微信的两个平台、Linked-In支持此字段
		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// appPath是待分享应用程序的本地路劲，仅在微信中使用
		// oks.setAppPath(MainActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment(getContext().getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(this.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);
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
}
