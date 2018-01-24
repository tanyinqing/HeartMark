package com.yikang.heartmark.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.adapter.YiShiChatAdapter;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.YiShiChat;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ImageTools;
import com.yikang.heartmark.util.ImageUtilBase;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;
import com.yuzhi.framework.util.FileUploadUtil;

@SuppressLint("SdCardPath")
public class YiShiChatActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private ListView chatListView;
	private YiShiChatAdapter chatAdapter;
	private ArrayList<YiShiChat> chatList = new ArrayList<YiShiChat>();
	private Bitmap userBitmap = null;
	private EditText sendEditText;
	private Button yuyinButton;
	private Button imageButton;
	private Button sendButton;
	private String chatType; // ask or reply
	private String chatId; // 用于提问之后的chat
	private String userId; // 

	private final int ACTION_CROP = 21;
	private final int CROP_PICTURE = 3;
	public final int UPLOAD_SUCCESS = 11;
	public final int UPLOAD_FAIL = 10;
	public final String FOLDER_PATH = "/mnt/sdcard/lepu/image/chat"; // 文件夹位置
	public final String IMAGE_PATH = "/mnt/sdcard/lepu/image/chat/upload_image.jpg"; // 图片位置

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yishi_chat);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.yishiChatTopBar);
		topbar.setTopbarTitle(R.string.yishi_chat);
		topbar.setOnTopbarLeftButtonListener(this);

		chatListView = (ListView) findViewById(R.id.yishi_chat_list);
		File imageFile = new File(MainActivity.IMAGE_PATH);
		if (imageFile.exists()) {
			userBitmap = ImageUtilBase.toRound(
					BitmapFactory.decodeFile(MainActivity.IMAGE_PATH), 5000);
		} else {
			String sex = SharedPreferenceUtil.getString(YiShiChatActivity.this, ConstantUtil.USER_SEX);
			if(sex.equals("") || sex.equals("0001")){
				userBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.friend_head_man);
			}else if(sex.equals("0002")){
				userBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.friend_head_men);
			}

		}

		chatType = getIntent().getStringExtra("chatType");
		YiShiChat chat = new YiShiChat();
		chat.chatFrom = 0;
		chat.chatText = "专业医生建议，汇集国内外顶级专业团队，对您的心脏进行一个全方位的监测。";
		chatList.add(chat);
		if (chatType.equals("ask")) {
			chatAdapter = new YiShiChatAdapter(YiShiChatActivity.this,
					chatList, userBitmap);
			chatListView.setAdapter(chatAdapter);
		} else if (chatType.equals("reply")) {
			chatId = getIntent().getStringExtra("uuid");
			Map<String, String> params = new HashMap<String, String>();
			params.put("consultationId", chatId);
			ConnectionManager.getInstance().send("FN06070WD00",
					"queryConsultationInfoDetail", params,
					"getDetailSucessCallBack", this);
		}

		sendEditText = (EditText) findViewById(R.id.yishi_chat_edittext);
		yuyinButton = (Button) findViewById(R.id.yishi_chat_sound);
		imageButton = (Button) findViewById(R.id.yishi_chat_img);
		sendButton = (Button) findViewById(R.id.yishi_chat_send);
		yuyinButton.setOnClickListener(new MyViewOnclicklistener());
		imageButton.setOnClickListener(new MyViewOnclicklistener());
		sendButton.setOnClickListener(new MyViewOnclicklistener());

		/**  editText变化监听,有输入时右边按钮为发送,没输入时为语音按钮
		sendEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!sendEditText.getText().toString().equals("")) {
					yuyinButton.setVisibility(View.GONE);
					sendButton.setVisibility(View.VISIBLE);
				} else {
					yuyinButton.setVisibility(View.VISIBLE);
					sendButton.setVisibility(View.GONE);
				}
			}
		});
		*/
	}

	@SuppressWarnings("rawtypes")
	public void getDetailSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		Map askMap = (Map) resultMap.get("consultationDetail");
		YiShiChat yishiChat = new YiShiChat();
		yishiChat.chatText = (String) askMap.get("CONTENT");
		yishiChat.chatFrom = 1;
		userId = (String) askMap.get("USER_ID");
		chatList.add(yishiChat);

		List list = (List) resultMap.get("consultationRelpyList");
		for (int i = 0; i < list.size(); i++) {
			YiShiChat yishiInfo = new YiShiChat();
			Map temp = new HashMap();
			temp = (Map) list.get(i);

			yishiInfo.chatText = (String) temp.get("CONTENT"); 
			yishiInfo.chatImage = (String) temp.get("IMAGE_URL");
			if(temp.get("IMAGE_HEIGHT") != null && !temp.get("IMAGE_HEIGHT").equals("")){
				yishiInfo.chatImageHigh =String.valueOf(Double.valueOf(temp.get("IMAGE_HEIGHT").toString()).intValue());
			}
			if(temp.get("IMAGE_WIDTH") != null && !temp.get("IMAGE_WIDTH").equals("")){
				yishiInfo.chatImageWide =String.valueOf(Double.valueOf(temp.get("IMAGE_WIDTH").toString()).intValue());
			}
			
			String uuid = (String) temp.get("CREATE_USER");
			if (uuid.equals(userId)) {
				yishiInfo.chatFrom = 1;
			} else {
				yishiInfo.chatFrom = 0;
			}
			chatList.add(yishiInfo);
		}

		chatAdapter = new YiShiChatAdapter(YiShiChatActivity.this, chatList,
				userBitmap);
		chatListView.setAdapter(chatAdapter);
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.yishi_chat_sound:

				break;
			case R.id.yishi_chat_img:

				if (chatType.equals("ask")) {
					MyToast.show(YiShiChatActivity.this, "请先提问",
							Toast.LENGTH_LONG);
				} else {
					showChooseIconDialog();
				}
				break;
			case R.id.yishi_chat_send:
				doChat(sendEditText.getText().toString());
				break;
			default:
				break;
			}
		}
	}

	private void doChat(String sendData) {
		if(sendData == null || sendData.equals("") || sendData.length() == 0){
			ShowUtil.showToast(YiShiChatActivity.this, R.string.no_data_send);
			return;
		}
		if (chatType.equals("ask")) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("content", sendData);
			if (ConnectUtil.isConnect(AppContext.context)) {
				ConnectionManager.getInstance().send("FN06070WD00",
						"saveConsultationInfo", params, "saveSucessCallBack",
						this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context,
						R.string.check_network);
			}

		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("content", sendData);
			params.put("consultationId", chatId);
			if (ConnectUtil.isConnect(AppContext.context)) {
				ConnectionManager.getInstance().send("FN06070WD00",
						"consultationReply", params, "replySucessCallBack",
						this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context,
						R.string.check_network);
			}

		}

	}

	@SuppressWarnings("rawtypes")
	public void saveSucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(YiShiChatActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				chatType = "reply";
				//chatId = "uuid";
				chatId = (String)resultMap.get("uuid");
				YiShiChat chat = new YiShiChat();
				chat.chatFrom = 1;
				chat.chatText = sendEditText.getText().toString();
				chatList.add(chat);
				chatAdapter.notifyDataSetChanged();
				chatListView.setSelection(chatList.size() - 1);
				sendEditText.setText("");

			} else if (result.equals("fail")) {
				MyToast.show(YiShiChatActivity.this, "请登录后再次操作",
						Toast.LENGTH_LONG);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void replySucessCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(YiShiChatActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				YiShiChat chat = new YiShiChat();
				chat.chatFrom = 1;
				chat.chatText = sendEditText.getText().toString();
				chatList.add(chat);
				chatAdapter.notifyDataSetChanged();
				chatListView.setSelection(chatList.size() - 1);
				sendEditText.setText("");

			} else if (result.equals("fail")) {
				MyToast.show(YiShiChatActivity.this, "请重试",
						Toast.LENGTH_LONG);
			}
		}
	}

	protected void showChooseIconDialog() {
		AlertDialog.Builder builer = new Builder(YiShiChatActivity.this);
		builer.setTitle("提示");
		builer.setMessage("选择上传图片");
		builer.setPositiveButton("拍照",
				new android.content.DialogInterface.OnClickListener() {
					@SuppressLint("WorldWriteableFiles")
					public void onClick(DialogInterface dialog, int which) {
						// Intent intent = new
						// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// startActivityForResult(intent,ACTION_CAMERA);

						Uri imageUri = null;
						String fileName = null;
						Intent openCameraIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 删除上一次截图的临时文件
						@SuppressWarnings("deprecation")
						SharedPreferences sharedPreferences = YiShiChatActivity.this
								.getSharedPreferences("temp",
										Context.MODE_WORLD_WRITEABLE);
						ImageTools.deletePhotoAtPathAndName(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath(), sharedPreferences
								.getString("tempName", ""));

						// 保存本次截图临时文件名字
						fileName = String.valueOf(System.currentTimeMillis())
								+ ".jpg";
						Editor editor = sharedPreferences.edit();
						editor.putString("tempName", fileName);
						editor.commit();

						imageUri = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), fileName));
						// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								imageUri);
						startActivityForResult(openCameraIntent, ACTION_CROP);
					}
				});
		builer.setNegativeButton("图片库",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//						Intent openAlbumIntent = new Intent(
//								Intent.ACTION_GET_CONTENT);
//						openAlbumIntent.setDataAndType(
//								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//								"image/*");
//						startActivityForResult(openAlbumIntent, ACTION_CROP);
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, ACTION_CROP);
					}
				});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	@SuppressLint("WorldWriteableFiles")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ACTION_CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					@SuppressWarnings("deprecation")
					String fileName = YiShiChatActivity.this
							.getSharedPreferences("temp",
									Context.MODE_WORLD_WRITEABLE).getString(
									"tempName", "");
					uri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), fileName));
				}
				cropImage(uri, 200, 200, CROP_PICTURE);
				break;
			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					}
				}
				// if(photo.getWidth()<100 || photo.getHeight()<100){
				// photo = ImageUtilBase.resizeImage(photo, 200, 200);
				// }
				// headIcon.setImageBitmap(ImageUtilBase.toRound(photo,5000));
				// 保存到sd卡
				File excelFiles = new File(FOLDER_PATH);
				if (!excelFiles.exists()) {
					excelFiles.mkdirs();
				}
				File excelFile = new File(IMAGE_PATH);
				if (excelFile.exists()) {
					excelFile.mkdir();
				}
				ImageUtilBase.SaveImage(photo, IMAGE_PATH);

				File file = new File(IMAGE_PATH);
				if (file != null) {
					new Thread() {
						@SuppressWarnings("rawtypes")
						public void run() {
							Map resultMap = (Map) FileUploadUtil.upload("FN06070WD00U!uploadConsultationImage.action",
											IMAGE_PATH);
							String result = (String) resultMap.get("head");
							Message msg = new Message();
							if (result.equals("success")) {
								msg.what = UPLOAD_SUCCESS;
								Bundle b=new Bundle();
								b.putString("url", (String) resultMap.get("url"));
								b.putString("IMAGE_WIDTH", String.valueOf(Double.valueOf(resultMap.get("IMAGE_WIDTH").toString()).intValue()));
								b.putString("IMAGE_HEIGHT",String.valueOf(Double.valueOf(resultMap.get("IMAGE_HEIGHT").toString()).intValue()));
								msg.setData(b);
							} else if (result.equals("fail")) {
								msg.what = UPLOAD_FAIL;
								msg.obj = (String) resultMap.get("body");
							}
							handler.sendMessage(msg);
						}
					}.start();
				}
				break;
			default:
				break;
			}
		}

	}

	private void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	public String getImageUrl = null;
	public String getImageH = null;
	public String getImageW = null;
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_SUCCESS:
				Bundle data = msg.getData();
				getImageUrl = data.getString("url");
				getImageH = data.getString("IMAGE_HEIGHT");
				getImageW = data.getString("IMAGE_WIDTH");
				MyToast.show(YiShiChatActivity.this, "上传成功",
						Toast.LENGTH_LONG);
				YiShiChat chat = new YiShiChat();
				chat.chatFrom = 1;
				chat.chatImage = getImageUrl;
				chat.chatImageHigh = getImageH;
				chat.chatImageWide = getImageW;
				chatList.add(chat);
				chatAdapter.notifyDataSetChanged();
				chatListView.setSelection(chatList.size() - 1);
				sendEditText.setText("");
				Map<String, String> params = new HashMap<String, String>();
				params.put("consultationId", chatId);
				params.put("imageUrl", getImageUrl);
				params.put("IMAGE_HEIGHT", getImageH);
				params.put("IMAGE_WIDTH", getImageW);
				ConnectionManager.getInstance().send("FN06070WD00",
						"consultationReply", params,
						"replyImageSucessCallBack", YiShiChatActivity.this);
				break;
			case UPLOAD_FAIL:
				MyToast.show(YiShiChatActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG);
				break;
			default:
				break;
			}
		}
	};

	@SuppressWarnings("rawtypes")
	public void replyImageSucessCallBack(Object data) {
		Map resultMap = (Map) data;
		String result = (String) resultMap.get("head");
		if (result.equals("success")) {

		} else if (result.equals("fail")) {
			MyToast.show(YiShiChatActivity.this, "请重试", Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

}
