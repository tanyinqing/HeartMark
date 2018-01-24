package com.yikang.heartmark.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heartmark.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.SharedPreferenceUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yikang.heartmark.view.TopBarView.OnTopbarRightButtonListener;
import com.yikang.heartmark.wheel.WheelView;
import com.yikang.heartmark.widget.MyDialog;
import com.yuzhi.framework.util.ConnectionManager;

public class CenterInfoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener, OnClickListener,
		OnTopbarRightButtonListener {

	@ViewInject(R.id.center_info_username)
	private EditText username;
	@ViewInject(R.id.center_info_sex)
	private TextView sex;
	@ViewInject(R.id.center_info_age)
	private TextView age;
	@ViewInject(R.id.center_info_weight)
	private TextView weight;
/*	@ViewInject(R.id.center_info_history)
	private TextView history;
    @ViewInject(R.id.center_info_number)
	private EditText number;*/

	@ViewInject(R.id.center_info_sexLL)
	private LinearLayout sexLL;
	@ViewInject(R.id.center_info_ageLL)
	private LinearLayout ageLL;
	@ViewInject(R.id.center_info_weightLL)
	private LinearLayout weightLL;
/*	@ViewInject(R.id.center_info_historyLL)
	private LinearLayout historyLL;
 	@ViewInject(R.id.center_info_numberImg)
	private ImageView numberImg;*/
	private String login_center;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_info);
		login_center = getIntent().getStringExtra("login_center");
		init();
	}

	private int indexSex = 0;
	private int indexAge = 40;
	private int indexWeight = 60;
	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.centerInfoBar);
		topbar.setTopbarTitle(R.string.centerinfo);
		topbar.setOnTopbarLeftButtonListener(this);
		topbar.setRightText("保存");
		topbar.setOnTopbarRightButtonListener(this);
		ViewUtils.inject(this);

		sexLL.setOnClickListener(this);
		ageLL.setOnClickListener(this);
		weightLL.setOnClickListener(this);
/*		historyLL.setOnClickListener(this);
		historyLL.setVisibility(View.GONE);
		numberImg.setOnClickListener(this);*/

		String USER_NAME_info = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_NAME_info);
		if (!TextUtils.isEmpty(USER_NAME_info)) {
			username.setText(USER_NAME_info);
			if(USER_NAME_info.length()<16){
				username.setSelection(USER_NAME_info.length());
			}
		}
		String uSER_SEX = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_SEX);
		if (!TextUtils.isEmpty(uSER_SEX) && "0001".equals(uSER_SEX)) {
			sex.setText("男");
			indexSex = 0;
		} else if (!TextUtils.isEmpty(uSER_SEX) && "0002".equals(uSER_SEX)) {
			sex.setText("女");
			indexSex = 1;
		}
		String uSER_AGE = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_AGE);
		if (!TextUtils.isEmpty(uSER_AGE) && Integer.valueOf(uSER_AGE) != 0) {
			age.setText(uSER_AGE);
			indexAge = Integer.valueOf(uSER_AGE);
		}else if(!TextUtils.isEmpty(uSER_AGE) && Integer.valueOf(uSER_AGE) == 0){
			age.setText("");
		}
		String uSER_WEIGHT = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_WEIGHT);
		if (!TextUtils.isEmpty(uSER_WEIGHT)) {
			weight.setText(uSER_WEIGHT);
			indexWeight = Integer.valueOf(uSER_WEIGHT);
		}
/*		String USER_NUMBER = SharedPreferenceUtil.getString(this,
				ConstantUtil.USER_NUMBER);
		if (!TextUtils.isEmpty(USER_NUMBER)) {
			number.setText(USER_NUMBER);
		}*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_info_sexLL:
			showSex(sex,indexSex);
			break;
		case R.id.center_info_ageLL:
			showDialogAge("   岁", 0, 120, age, indexAge);
			break;
		case R.id.center_info_weightLL:
			showDialogWeight("   kg", 0, 160, weight , indexWeight);
			break;
/*		case R.id.center_info_historyLL:
			break;
		case R.id.center_info_numberImg:
			try {
				Intent intent = new Intent(CenterInfoActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 0);
			} catch (Exception e) {
			}
			break;*/
		}
	}

	/**
	 * 性别
	 */
	private int sexIndex;
	private void showSex(final TextView sex2, int defaultValue) {
		final String[] sexArray = { "男", "女" };
		sexIndex = defaultValue;
		MyDialog dialog = new MyDialog(CenterInfoActivity.this);
		dialog.setTitle("请选择性别");
		dialog.setSingleChoiceItems(sexArray, defaultValue,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						sexIndex = which;
						indexSex = which;
					}
				});
		dialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sex2.setText(sexArray[sexIndex]);
			}
		});
		dialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		dialog.create(null).show();
	}

	/**
	 * 年龄，体重
	 * @param  
	 */
	public void showDialogAge(String unit, int A, int B, final TextView age2, int defaultValue) {
		MyDialog myDialog = new MyDialog(CenterInfoActivity.this);
		myDialog.setSimpleSelectPicker(defaultValue, A, B, unit);
		myDialog.setTitle(R.string.regist);
		myDialog.setNegativeButton(R.string.cancel, null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v
						.findViewById(R.id.simpleSelectPicker);
				age2.setText(wv_content.getCurrentItem()+ "");
				indexAge = wv_content.getCurrentItem();
			}
		});
		myDialog.create(null).show();
	}
	public void showDialogWeight(String unit, int A, int B, final TextView age2, int defaultValue) {
		MyDialog myDialog = new MyDialog(CenterInfoActivity.this);
		myDialog.setSimpleSelectPicker(defaultValue, A, B, unit);
		myDialog.setTitle(R.string.regist);
		myDialog.setNegativeButton(R.string.cancel, null);
		myDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WheelView wv_content = (WheelView) v
						.findViewById(R.id.simpleSelectPicker);
				age2.setText(wv_content.getCurrentItem()+ "");
				indexWeight = wv_content.getCurrentItem();
			}
		});
		myDialog.create(null).show();
	}

/*	//处理扫描结果（在界面上显示）
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			if(scanResult.length() > 20){
				scanResult = scanResult.substring(0, 20);
			}
			number.setText(scanResult);
		}
	}

	//病史 
	private String[] historyList;
	private boolean[] checkedItems;
	public void editYaoRemindMethod(final TextView tv) {
		AlertDialog.Builder builder = new Builder(CenterInfoActivity.this);
		historyList = new String[] { "病史一", "病史二", "病史三", "病史四", "病史五", "病史六" };
		checkedItems = new boolean[] { false, false, false, false, false,
				false, false };
		builder.setMultiChoiceItems(historyList, checkedItems,
				new OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						if (isChecked) {
							checkedItems[which] = true;
						}
					}
				});
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					String daysOfWeekStr = "";

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (checkedItems.length > 0) {
							for (int i = 0; i < checkedItems.length; i++) {
								if (checkedItems[i] == true) {

								} else {

								}
							}
							daysOfWeekStr = "";
						}
						tv.setText(daysOfWeekStr);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
*/
	
	@Override
	public void onTopbarLeftButtonSelected() {
		if(!TextUtils.isEmpty(login_center)&&"login_center".equals(login_center)){
			save();
		}else{
			finish();
		}
	}

	@Override
	public void onTopbarRightButtonSelected() {
		save();
	}

	private String infoNameString;
	private String infoAgeString;
	private String infoSexString;
	private String infoWeightString;
	//private String infoNumberString;
	private void save() {

		infoNameString = username.getText().toString().trim();
		infoSexString = sex.getText().toString().trim();
		infoAgeString = age.getText().toString().trim();
		infoWeightString = weight.getText().toString().trim();
//		infoNumberString = number.getText().toString().trim();

		if (TextUtils.isEmpty(infoNameString)) {
			ShowUtil.showToast(this, "请输入昵称");
			return;
		} else if (TextUtils.isEmpty(infoSexString)) {
			ShowUtil.showToast(this, "请选择性别");
			return;
		} else if (TextUtils.isEmpty(infoAgeString)) {
			ShowUtil.showToast(this, "请选择年龄");
			return;
		} else if (TextUtils.isEmpty(infoWeightString)) {
			ShowUtil.showToast(this, "请选择体重");
			return;
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("USERNAME", infoNameString);
			if (sexIndex == 0) {
				params.put("SEX", "0001");
			} else if (sexIndex == 1) {
				params.put("SEX", "0002");
			}
			params.put("AGE", infoAgeString);
			params.put("WEIGHT", infoWeightString);
//			params.put("DOCTOR_NO", infoNumberString);

			if (ConnectUtil.isConnect(AppContext.context)) {
				ConnectionManager.getInstance().send("FN01080WL00",
						"updateMemberInformationForMobile", params,
						"saveInfoCallBackHandler", this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context,
						R.string.check_network);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void saveInfoCallBackHandler(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(CenterInfoActivity.this,
					R.string.check_network_timeout);
		} else {
			String result = (String) resultMap.get("head");
			if (result.equals("success")) {
				SharedPreferenceUtil.setString(CenterInfoActivity.this,
						ConstantUtil.USER_NAME_info, infoNameString);
				if (sexIndex == 0) {
					SharedPreferenceUtil.setString(CenterInfoActivity.this,
							ConstantUtil.USER_SEX, "0001");
				} else if (sexIndex == 1) {
					SharedPreferenceUtil.setString(CenterInfoActivity.this,
							ConstantUtil.USER_SEX, "0002");
				}
				SharedPreferenceUtil.setString(CenterInfoActivity.this,
						ConstantUtil.USER_AGE, infoAgeString);
				SharedPreferenceUtil.setString(CenterInfoActivity.this,
						ConstantUtil.USER_WEIGHT, infoWeightString);
//				SharedPreferenceUtil.setString(CenterInfoActivity.this,
//						ConstantUtil.USER_NUMBER, infoNumberString);
				SharedPreferenceUtil.setString(CenterInfoActivity.this,
						ConstantUtil.USER_NAME, infoNameString);
				finish();
			} else if (result.equals("fail")) {
				ShowUtil.showToast(CenterInfoActivity.this, "个人信息修改失败");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!TextUtils.isEmpty(login_center)&&"login_center".equals(login_center)){
				save();
			}else{
				finish();
			}
		}
		return false;
	}
}
