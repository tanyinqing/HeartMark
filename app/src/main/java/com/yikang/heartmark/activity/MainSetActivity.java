package com.yikang.heartmark.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.myzxing.activity.CaptureActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ImageTools;
import com.yikang.heartmark.util.ImageUtil;
import com.yikang.heartmark.util.ImageUtilBase;
import com.yikang.heartmark.util.MyToast;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;
import com.yuzhi.framework.util.FileUploadUtil;

@SuppressLint({ "HandlerLeak", "SdCardPath" })
public class MainSetActivity extends BaseActivity implements
		OnTopbarLeftButtonListener , OnTopbarRightButtonListener{
	
	private final int SCAN_IMG = 22;
	private final int ACTION_CROP = 21;
	private final int CROP_PICTURE = 3;//图片
	private final int CROP_PHOTO = 4; //拍照
	public final int UPLOAD_SUCCESS = 11;
	public final int UPLOAD_FAIL = 10;
	public final String FOLDER_PATH = "/mnt/sdcard/lepu/image/case"; // 文件夹位置
	public final String IMAGE_PATH = "/mnt/sdcard/lepu/image/case/caseimage.jpg"; // 图片位置
	
	
	private TextView registText;
	private TextView trialText;
	private TextView loginText;
	private ImageView headIcon;
	private TopBarView topbar;
	private TextView scan_Txt;
	private RelativeLayout setScan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_set);
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshView();
	}

	private void init() {
		topbar = (TopBarView) findViewById(R.id.setTopBar);
		topbar.setTopbarTitle(R.string.set);
		topbar.setOnTopbarLeftButtonListener(this);

		registText = (TextView) findViewById(R.id.set_regist);
		trialText = (TextView) findViewById(R.id.set_trial);
		loginText = (TextView) findViewById(R.id.set_login);
		headIcon = (ImageView) findViewById(R.id.set_heat_icon);

		
		RelativeLayout setLifeData = (RelativeLayout) findViewById(R.id.set_life_data);
		RelativeLayout setCelingDate = (RelativeLayout) findViewById(R.id.set_celing_data);
		RelativeLayout setRemind = (RelativeLayout) findViewById(R.id.set_remind_data);
		RelativeLayout setOther = (RelativeLayout) findViewById(R.id.set_other);
		RelativeLayout setImage = (RelativeLayout) findViewById(R.id.set_image);
		setScan = (RelativeLayout) findViewById(R.id.set_scan);
		scan_Txt = (TextView) findViewById(R.id.scan_Txt);
		
		registText.setOnClickListener(new MyViewOnclicklistener());
		loginText.setOnClickListener(new MyViewOnclicklistener());
		headIcon.setOnClickListener(new MyViewOnclicklistener());
		setLifeData.setOnClickListener(new MyViewOnclicklistener());
		setCelingDate.setOnClickListener(new MyViewOnclicklistener());
		setRemind.setOnClickListener(new MyViewOnclicklistener());
		setOther.setOnClickListener(new MyViewOnclicklistener());
		setImage.setOnClickListener(new MyViewOnclicklistener());
		setScan.setOnClickListener(new MyViewOnclicklistener());
	}
	

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.set_heat_icon:
				//showChooseIconDialog();
				break;
			case R.id.set_regist:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
					Intent collectIntent = new Intent(MainSetActivity.this, CenterInfoActivity.class);
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetActivity.this, RegistActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_login:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
				Intent collectIntent = new Intent(MainSetActivity.this, CenterInfoActivity.class);
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_life_data:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
				Intent collectIntent = new Intent(MainSetActivity.this, SetLifeDataActivity.class);
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_celing_data:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
				Intent dataIntent = new Intent(MainSetActivity.this, CeLingDataActivity.class);
					startActivity(dataIntent);
				}else{
					Intent dataIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(dataIntent);
				}
				break;
			case R.id.set_remind_data:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
				Intent remindIntent = new Intent(MainSetActivity.this, SetRemindActivity.class);
					startActivity(remindIntent);
				}else{
					Intent remindIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(remindIntent);
				}
				break;																																																								
			case R.id.set_other:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
				Intent collectIntent = new Intent(MainSetActivity.this, MainSetAboutActivity.class);
					startActivity(collectIntent);
				}else{
					Intent collectIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_scan:
//				if(ConnectUtil.isLogin(MainSetActivity.this)){
				if(true){
				try {
						Intent intent = new Intent(MainSetActivity.this,
								CaptureActivity.class);
						startActivityForResult(intent, SCAN_IMG);
					} catch (Exception e) {
					}
				}else{
					Intent collectIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			case R.id.set_image:
				if(ConnectUtil.isLogin(MainSetActivity.this)){
				//if(true){
				showChooseIconDialog();
				}else{
					Intent collectIntent = new Intent(MainSetActivity.this, LoginActivity.class);
					startActivity(collectIntent);
				}
				break;
			default:
				break;
			}
		}
	}


	// 更新登录显示
	private void refreshView() {
		if (ConnectUtil.isLogin(MainSetActivity.this)) {
			trialText.setVisibility(View.GONE);
			loginText.setVisibility(View.GONE);
			registText.setText(SharedPreferenceUtil.getString(
					MainSetActivity.this, ConstantUtil.USER_NAME));
			String uSER_SEX = SharedPreferenceUtil.getString(this,
					ConstantUtil.USER_SEX);
			if (!TextUtils.isEmpty(uSER_SEX) && "0001".equals(uSER_SEX)) {
				headIcon.setBackgroundResource(R.drawable.friend_head_man);
			} else if (!TextUtils.isEmpty(uSER_SEX) && "0002".equals(uSER_SEX)) {
				headIcon.setBackgroundResource(R.drawable.friend_head_men);
			}
			topbar.setRightText("注销");
			topbar.setOnTopbarRightButtonListener(this);
			//setInfo.setVisibility(View.VISIBLE);
			//setFriend.setVisibility(View.VISIBLE);
		} else {
			trialText.setVisibility(View.VISIBLE);
			loginText.setVisibility(View.VISIBLE);
			registText.setText(getResources().getString(R.string.regist));
			loginText.setText(getResources().getString(R.string.login_please));
			headIcon.setBackgroundResource(R.drawable.default_head);
			topbar.setRightTextGone();
			//setInfo.setVisibility(View.GONE);
			//setFriend.setVisibility(View.GONE);
		}
		String flag = SharedPreferenceUtil.getString(this,ConstantUtil.USER_NUMBER_CODE);
		String scanNum = SharedPreferenceUtil.getString(this,ConstantUtil.USER_NUMBER);
		if("true".equals(flag)){
			scan_Txt.setText(scanNum);
			setScan.setClickable(false);
			setScan.setFocusable(false);
		}else{
			scan_Txt.setText(scanNum);
			setScan.setClickable(true);
			setScan.setFocusable(true);
		}
	}

	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	@Override
	public void onTopbarRightButtonSelected() {
		showEscDialog();
	}
	// 注销
	private void showEscDialog() {
		MyDialog dialog = new MyDialog(MainSetActivity.this)
				.setTitle(R.string.useresc_title)
				.setMessage(R.string.useresc_msg)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_NAME, "");
						SharedPreferenceUtil.setString(MainSetActivity.this, ConstantUtil.USER_UID, "");
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_TOKEN, "");
						
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_NAME_info, "");
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_SEX, "");
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_AGE, "0");
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_WEIGHT, "");
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_NUMBER, "");
						SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_NUMBER_CODE, "");
						
						// 保存登录标志
						SharedPreferenceUtil.setBoolean(MainSetActivity.this,ConstantUtil.LOGIN, false);
						refreshView();
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

	
	/**
	 * 处理扫描结果（在界面上显示）
	 */
	@SuppressWarnings("rawtypes")
	public void saveScanMessageInfo(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(MainSetActivity.this,
					R.string.check_network_timeout);
		}else{
			String result = (String) resultMap.get("head");
			String message = (String) resultMap.get("message");
			String DOCTOR_NO = (String) resultMap.get("DOCTOR_NO");
			String flag = (String) resultMap.get("flag");
			if (result.equals("success")) {
				if("01003".equals(message)&&"true".equals(flag)){
					setScan.setClickable(false);
					setScan.setFocusable(false);
					scan_Txt.setText(DOCTOR_NO);
					MyToast.show(MainSetActivity.this,"绑定成功",Toast.LENGTH_LONG);
				}else if("01001".equals(message)){
					setScan.setClickable(false);
					setScan.setFocusable(false);
					scan_Txt.setText(DOCTOR_NO);
					MyToast.show(MainSetActivity.this,"已经绑定过",Toast.LENGTH_LONG);
				}else if("01002".equals(message)){
					MyToast.show(MainSetActivity.this,"医生编号不存在",Toast.LENGTH_LONG);
				}
				SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_NUMBER, DOCTOR_NO);
				SharedPreferenceUtil.setString(MainSetActivity.this,ConstantUtil.USER_NUMBER_CODE, flag);
			}
		}
	}
	
	//拍照上传
	protected void showChooseIconDialog() {
		AlertDialog.Builder builer = new Builder(MainSetActivity.this);
		builer.setTitle("提示");
		builer.setMessage("选择上传图片");
		builer.setPositiveButton("拍照",
				new android.content.DialogInterface.OnClickListener() {
					@SuppressLint("WorldWriteableFiles")
					public void onClick(DialogInterface dialog, int which) {
//
//						Uri imageUri = null;
//						String fileName = null;
//						Intent openCameraIntent = new Intent(
//								MediaStore.ACTION_IMAGE_CAPTURE);
//						// 删除上一次截图的临时文件
//						@SuppressWarnings("deprecation")
//						SharedPreferences sharedPreferences = MainSetActivity.this
//								.getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
//						ImageTools.deletePhotoAtPathAndName(Environment
//								.getExternalStorageDirectory()
//								.getAbsolutePath(), sharedPreferences
//								.getString("tempName", ""));
//
//						// 保存本次截图临时文件名字
//						fileName = String.valueOf(System.currentTimeMillis())+ ".jpg";
//						Editor editor = sharedPreferences.edit();
//						editor.putString("tempName", fileName);
//						editor.commit();
//
//						imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
						// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
						//openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
						//startActivityForResult(openCameraIntent, CROP_PHOTO);
						Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(camera,CROP_PHOTO);
					}
				});
		builer.setNegativeButton("图片库",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//						openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//						startActivityForResult(openAlbumIntent, ACTION_CROP);
//						
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, CROP_PICTURE);
					}
				});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SCAN_IMG:
				Bundle bundle = data.getExtras();
				//这个是扫描到的字符串
				String scanResult = bundle.getString("result");
				Map<String, String> params = new HashMap<String, String>();
				params.put("doctorNo",scanResult);
				ConnectionManager.getInstance().send("FN01070WD00",
						"bindingDoctor", params,"saveScanMessageInfo",this);
				break;
			case ACTION_CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					@SuppressWarnings("deprecation")
					String fileName = MainSetActivity.this
							.getSharedPreferences("temp",
									Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
					uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				}
				//对图片进行剪切
				cropImage(uri, 200, 200, CROP_PICTURE);
				break;
			case CROP_PICTURE://图片库
//				Bitmap photo = null;
//				Uri photoUri = data.getData();
//				if (photoUri != null) {
//					photo = BitmapFactory.decodeFile(photoUri.getPath());
//				}
//				if (photo == null) {
//					Bundle extra = data.getExtras();
//					if (extra != null) {
//						photo = (Bitmap) extra.get("data");
//						ByteArrayOutputStream stream = new ByteArrayOutputStream();
//						photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//					}
//				}
				Bitmap picture = BitmapFactory.decodeFile(getImagePath(data));
			    
				picture = ImageUtil.dowithBigImage(picture);
				
				File excelFiles = new File(FOLDER_PATH);
				if (!excelFiles.exists()) {
					excelFiles.mkdirs();
				}
				File excelFile = new File(IMAGE_PATH);
				if (excelFile.exists()) {
					excelFile.mkdir();
				}
				if(picture != null){
					ImageUtilBase.SaveImage(picture, IMAGE_PATH);
				}

				File file = new File(IMAGE_PATH);
				if (file != null) {
					new Thread() {
						@SuppressWarnings("rawtypes")
						public void run() {
							Map resultMap = (Map) FileUploadUtil.putImg("FN06070WD00S!uploadPatientImage.action",IMAGE_PATH);
							String result = (String) resultMap.get("head");
							Message msg = new Message();
							if (result.equals("success")) {
								msg.what = UPLOAD_SUCCESS;
								Bundle b=new Bundle();
								b.putString("url", (String) resultMap.get("url"));
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
			case CROP_PHOTO://拍照
				Bundle getBundle = data.getExtras();

				Bitmap bitmap = (Bitmap) getBundle.get("data");
				//处理大图片 成小点的图片，在把图片放大
				bitmap = ImageUtil.dowithBigImage(bitmap);
				
				File folderFile = new File(FOLDER_PATH);
				if (!folderFile.exists()) {
					folderFile.mkdirs();
				}
				File imageFile = new File(IMAGE_PATH);
				if (imageFile.exists()) {
					imageFile.mkdir();
				}
				ImageUtilBase.SaveImage(bitmap, IMAGE_PATH);

				File files = new File(IMAGE_PATH);
				if (files != null) {
					new Thread() {
						@SuppressWarnings("rawtypes")
						public void run() {
							Map resultMap = (Map) FileUploadUtil.putImg("FN06070WD00S!uploadPatientImage.action",IMAGE_PATH);
							String result = (String) resultMap.get("head");
							Message msg = new Message();
							if (result.equals("success")) {
								msg.what = UPLOAD_SUCCESS;
								Bundle b=new Bundle();
								b.putString("url", (String) resultMap.get("url"));
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
	//返回图片的路径
	private String getImagePath(Intent data) {
		Uri uri = data.getData();
		if (uri == null)
			return "";

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);

		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();

		return actualimagecursor.getString(actual_image_column_index);

	}
	
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_SUCCESS:
				String userUid = SharedPreferenceUtil.getString(MainSetActivity.this, ConstantUtil.USER_UID);
				String getImageUrl = msg.getData().getString("url");
				Map<String, String> params = new HashMap<String, String>();
				params.put("userID", userUid);
				params.put("url", getImageUrl);
				ConnectionManager.getInstance().send("FN06070WD00",
						"saveImgInfo", params,
						"saveImgInfo", MainSetActivity.this);
				break;
			case UPLOAD_FAIL:
				MyToast.show(MainSetActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG);
				break;
			default:
				break;
			}
		}
	};
	@SuppressWarnings("rawtypes")
	public void saveImgInfo(Object data) {
		Map resultMap = (Map) data;
		String result = (String) resultMap.get("head");
		if (result.equals("success")) {
			MyToast.show(MainSetActivity.this,"病历照片上传成功",Toast.LENGTH_LONG);
		}
	}
}
