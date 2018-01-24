package com.yikang.heartmark.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.heartmark.R;
import com.yikang.heartmark.activity.ZiXunDetailGetActivity;
import com.yikang.heartmark.model.ZiXun;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ImageDownLoader;
import com.yikang.heartmark.util.ImageDownLoader.onImageLoaderListener;

public class NetViewPagerAdapter extends PagerAdapter{
	private LayoutInflater layoutInflater = null;
	private ArrayList<ZiXun> pagers;
	private Context context;
	private ImageDownLoader mImageDownLoader;
	
	public NetViewPagerAdapter(Context context, ArrayList<ZiXun> views) {
		this.pagers = views;
		this.context = context;
		mImageDownLoader = new ImageDownLoader(context);
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return pagers.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = layoutInflater.inflate(R.layout.item_pager_image, view, false);
		final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		ZiXun item = pagers.get(position);
		Bitmap bitmap = null;
		bitmap = mImageDownLoader.downloadImage(ConnectUtil.HOST_URL + item.image, new onImageLoaderListener() {
			@Override
			public void onImageLoader(Bitmap bitmap, String url) {
				if(imageView != null && bitmap != null){
					imageView.setImageBitmap(bitmap);
				}
			}
		});
		
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
		}
	/*	else{
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.pinggu_show));
		}*/
		/*BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(imageView,ConnectUtil.HOST_URL + item.image);*/
		
		view.addView(imageLayout, 0);
		imageView.setOnClickListener(new MyViewOnclicklistener(pagers.get(position)));
		return imageLayout;
	}
	
	class MyViewOnclicklistener implements View.OnClickListener {
        private ZiXun zixun;
		public MyViewOnclicklistener(ZiXun zixun){
			this.zixun = zixun;
		}
		@Override
		public void onClick(View v) {
			final int rid = v.getId();
			switch (rid) {
			case R.id.image:
				Intent detailIntent = new Intent(context,ZiXunDetailGetActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newItem", zixun);
				detailIntent.putExtras(bundle);
				context.startActivity(detailIntent);
				break;

			default:
				break;
			}
		}
	}
}
