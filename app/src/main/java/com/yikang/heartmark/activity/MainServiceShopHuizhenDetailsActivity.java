package com.yikang.heartmark.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.lidroid.xutils.BitmapUtils;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.Expert;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;

public class MainServiceShopHuizhenDetailsActivity extends BaseActivity implements
    OnTopbarLeftButtonListener{
	
	private Expert item = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_shop_huizhen_details);
		item  = (Expert) getIntent().getBundleExtra("huizhen").getSerializable("huizhen_item");
		init();
	}
	
	private void init(){
    	TopBarView topbar = (TopBarView) findViewById(R.id.mainServiceShopHuizhenDetailsTopBar);
		topbar.setTopbarTitle(R.string.service_shop_details_huizhen);
		topbar.setOnTopbarLeftButtonListener(this);

		ImageView huizhen_item01_img = (ImageView) findViewById(R.id.huizhen_item01_img);
		TextView huizhen_item01_name = (TextView) findViewById(R.id.huizhen_item01_name);
		TextView huizhen_item01_position = (TextView) findViewById(R.id.huizhen_item01_position);
		TextView huizhen_item01_cando = (TextView) findViewById(R.id.huizhen_item01_cando);
		TextView huizhen_item01_context = (TextView) findViewById(R.id.huizhen_item01_context);
		
		BitmapUtils bitmapUtils = new BitmapUtils(MainServiceShopHuizhenDetailsActivity.this);
		bitmapUtils.display(huizhen_item01_img, ConnectUtil.HOST_URL + item.EXPERT_IMG_URL);
		huizhen_item01_name.setText(item.EXPERT_NAME+ "  " +item.POSTION);
		huizhen_item01_position.setText(item.WORK_UNIT);
		huizhen_item01_cando.setText("擅长："+item.BE_GOOD_AT);
		huizhen_item01_context.setText(item.DETAILED_INFO);
		
		findViewById(R.id.huizhen_details_tel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+item.RESERVATION_TEL));
                startActivity(intent);
			}
		});
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}
}
