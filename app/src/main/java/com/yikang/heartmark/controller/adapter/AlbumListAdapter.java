package com.yikang.heartmark.controller.adapter;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.yikang.heartmark.common.alummage.ImageBucket;
import com.yikang.heartmark.common.util.LocalImageLoader;

import java.util.ArrayList;
import java.util.List;


public class AlbumListAdapter extends BaseAdapter {
    final String TAG = getClass().getSimpleName();

    Activity context;
    /**
     * 图片集列表
     */
    List<ImageBucket> bucketList = new ArrayList<ImageBucket>();

    public AlbumListAdapter(Activity context, List<ImageBucket> bucketList) {
        this.context = context;
        this.bucketList = bucketList;
    }

    @Override
    public int getCount() {
        return bucketList == null ? 0 : bucketList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.item_image_bucket, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ImageBucket bucket = bucketList.get(position);
        holder.count.setText(String.valueOf(bucket.count));
        holder.name.setText(bucket.bucketName);


        if (bucket.count > 0 && bucket.imageList.size() > 0) {
            String sourcePath = bucket.imageList.get(0).imagePath;
            String imgPath = sourcePath;
            holder.iv.setImageBitmap(null);
            LocalImageLoader imageloader = LocalImageLoader.getInstance();
            imageloader.displayImage("file:///" + imgPath, holder.iv);
        }
        //else {
//            holder.iv.setImageBitmap(null);
//           // Log.e(TAG, "no images in bucket " + bucket.bucketName);
//        }
        return convertView;
    }

    static class Holder {
        private ImageView iv;
        private TextView name;
        private TextView count;
    }


}
