package com.yikang.heartmark.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ImageTools;
import com.yikang.heartmark.util.ImageUtilBase;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.widget.MyDialog;

@SuppressLint("SdCardPath")
public class MainActivity extends BaseActivity {
	private ImageButton headIcon;
	private TextView loginText;
	private TextView trialText;

	public static final int ACTION_CROP = 21;
	public static final int CROP_PICTURE = 3;
	public static final String FOLDER_PATH = "/mnt/sdcard/lepu/image/"; // 文件夹位置
	public static final String IMAGE_PATH = "/mnt/sdcard/lepu/image/icon_image.jpg"; // 图片位置

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshLogin();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {

		headIcon = (ImageButton) findViewById(R.id.main_head_icon);
		loginText = (TextView) findViewById(R.id.main_login);
		trialText = (TextView) findViewById(R.id.main_trial);
		ImageButton setText = (ImageButton) findViewById(R.id.main_set);
		ImageButton newText = (ImageButton) findViewById(R.id.main_new);
		ImageButton zixunLayout = (ImageButton) findViewById(R.id.main_zixun);
		ImageButton celingLayout = (ImageButton) findViewById(R.id.main_celing);
		ImageButton yongyaoLayout = (ImageButton) findViewById(R.id.main_yongyao);
		ImageButton huliLayout = (ImageButton) findViewById(R.id.main_huli);
		ImageButton yishiLayout = (ImageButton) findViewById(R.id.main_yishi);

		headIcon.setOnClickListener(new MyViewOnclicklistener());
		loginText.setOnClickListener(new MyViewOnclicklistener());
		setText.setOnClickListener(new MyViewOnclicklistener());
		newText.setOnClickListener(new MyViewOnclicklistener());
		zixunLayout.setOnClickListener(new MyViewOnclicklistener());
		celingLayout.setOnClickListener(new MyViewOnclicklistener());
		yongyaoLayout.setOnClickListener(new MyViewOnclicklistener());
		huliLayout.setOnClickListener(new MyViewOnclicklistener());
		yishiLayout.setOnClickListener(new MyViewOnclicklistener());
	}

	class MyViewOnclicklistener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.main_head_icon:
				showChooseIconDialog();
				break;
			case R.id.main_login:
				Intent loginIntent = new Intent(MainActivity.this,
						MainSetActivity.class);
				startActivity(loginIntent);
				break;
			case R.id.main_set:
				Intent setIntent = new Intent(MainActivity.this,
						MainSetActivity.class);
				startActivity(setIntent);
				break;
			case R.id.main_new:
				Intent newIntent = null;
				if (ConnectUtil.isLogin(MainActivity.this)) {
					newIntent = new Intent(MainActivity.this,
							MainNewsActivity.class);
				} else {
					newIntent = new Intent(MainActivity.this,
							LoginActivity.class);
				}
				startActivity(newIntent);
				break;
			case R.id.main_zixun:
				Intent zixunIntent = new Intent(MainActivity.this,
						MainZiXunActivity.class);
				startActivity(zixunIntent);
				break;
			case R.id.main_celing:
				Intent celingIntent = null;
				if (ConnectUtil.isLogin(MainActivity.this)) {
					celingIntent = new Intent(MainActivity.this,
							MainCeLingActivity.class);
				} else {
					celingIntent = new Intent(MainActivity.this,
							LoginActivity.class);
				}
				startActivity(celingIntent);
				break;
			case R.id.main_yongyao:
				Intent yongyaoIntent = null;
				if (ConnectUtil.isLogin(MainActivity.this)) {
					yongyaoIntent = new Intent(MainActivity.this,
							MainYongYaoActivity.class);
				} else {
					yongyaoIntent = new Intent(MainActivity.this,
							LoginActivity.class);
				}
				startActivity(yongyaoIntent);
				break;
			case R.id.main_huli:
				Intent huliIntent = null;
				if (ConnectUtil.isLogin(MainActivity.this)) {
					if (SharedPreferenceUtil.getBoolean(MainActivity.this,
							ConstantUtil.Evaluate)) {
						huliIntent = new Intent(MainActivity.this,
								MainHuLiActivity.class);
					} else {
						SharedPreferenceUtil.setBoolean(MainActivity.this,
								ConstantUtil.Evaluate, true);
						huliIntent = new Intent(MainActivity.this,
								EvaluateActivity.class);
					}
				} else {
					huliIntent = new Intent(MainActivity.this,
							LoginActivity.class);
				}
				startActivity(huliIntent);
				break;
			case R.id.main_yishi:
				Intent yishiIntent = null;
				if (ConnectUtil.isLogin(MainActivity.this)) {
					yishiIntent = new Intent(MainActivity.this,
							MainYiShiActivity.class);
				} else {
					yishiIntent = new Intent(MainActivity.this,
							LoginActivity.class);
				}
				startActivity(yishiIntent);
				break;
			default:
				break;
			}
		}
	}

	protected void showChooseIconDialog() {
		MyDialog dialog = new MyDialog(MainActivity.this)
				.setTitle(R.string.phone_title)
				.setMessage(R.string.phone_msg)
				.setPositiveButton(R.string.phone_graph,
						new View.OnClickListener() {

							@SuppressLint("WorldWriteableFiles")
							@Override
							public void onClick(View arg0) {

								// Intent intent = new
								// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								// startActivityForResult(intent,ACTION_CAMERA);

								Uri imageUri = null;
								String fileName = null;
								Intent openCameraIntent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								// 删除上一次截图的临时文件
								@SuppressWarnings("deprecation")
								SharedPreferences sharedPreferences = MainActivity.this
										.getSharedPreferences("temp",
												Context.MODE_WORLD_WRITEABLE);
								ImageTools.deletePhotoAtPathAndName(Environment
										.getExternalStorageDirectory()
										.getAbsolutePath(), sharedPreferences
										.getString("tempName", ""));

								// 保存本次截图临时文件名字
								fileName = String.valueOf(System
										.currentTimeMillis()) + ".jpg";
								Editor editor = sharedPreferences.edit();
								editor.putString("tempName", fileName);
								editor.commit();

								imageUri = Uri.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										fileName));
								// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
								openCameraIntent.putExtra(
										MediaStore.EXTRA_OUTPUT, imageUri);
								startActivityForResult(openCameraIntent,
										ACTION_CROP);

							}
						})
				.setNegativeButton(R.string.phone_album,
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								Intent openAlbumIntent = new Intent(
										Intent.ACTION_GET_CONTENT);
								openAlbumIntent
										.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												"image/*");
								startActivityForResult(openAlbumIntent,
										ACTION_CROP);

							}
						});

		dialog.create(null);
		dialog.showMyDialog();

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
					System.out.println("Data");
				} else {
					System.out.println("File");
					@SuppressWarnings("deprecation")
					String fileName = MainActivity.this.getSharedPreferences(
							"temp", Context.MODE_WORLD_WRITEABLE).getString(
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
				headIcon.setBackgroundColor(color.transparent);
				headIcon.setImageBitmap(ImageUtilBase.toRound(photo, 5000));
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
	
	private void refreshLogin(){
		if(ConnectUtil.isLogin(MainActivity.this)){
			trialText.setVisibility(View.GONE);
			loginText.setText(SharedPreferenceUtil.getString(MainActivity.this, ConstantUtil.USER_NAME));
		}else{
			trialText.setVisibility(View.VISIBLE);
			loginText.setText(getResources().getString(R.string.login_please));
		}
	}
	
}
