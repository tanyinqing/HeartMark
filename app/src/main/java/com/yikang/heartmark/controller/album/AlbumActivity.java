package com.yikang.heartmark.controller.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmark.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.common.alummage.ImageBucket;
import com.yikang.heartmark.common.alummage.ImageItem;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.controller.base.ReturnResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chuanyhu on 2014/8/2.
 */
public class AlbumActivity extends BaseActivity {
    private GridView gridView;
    private TextView tv_size;

    private int maxAllowSelectNumber = 3;

    private ImageBucket imageBucket;
    private ArrayList<ImageItem> selectedImageList = new ArrayList<ImageItem>();
    private ImageGridAdapter adapter;
    private Handler handler = new Handler();
    private TextView txtViewCameraTitle;

    //以下对象不做任何使用，只确保AlbumListFragment混淆成功
    private AlbumListActivity albumListActivity;
    private DisplayImageOptions displayImgOptions;
    private ReturnResult returnResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_bucket);
//        Bundle args = getArguments();
//        if (args != null)
//            imageBucket = (ImageBucket) args.getSerializable(Constants.ImageBucket_ParamName);
        //todo 传递最多张数
        // maxAllowSelectNumber = args.getInt(Constants.PhotoSelected_MaxAllowSelectNumber);
        // Log.e("AlbumFragment", "===========================maxAllowSelectNumber=" + maxAllowSelectNumber + " imageBucket" + imageBucket);
        //tv_size = (TextView) findViewById(R.id.tv_size);
        Button bt_view = (Button) findViewById(R.id.bt_view);
        TextView tv_otherPics = (TextView) findViewById(R.id.tv_otherPics);
        ImageView navigation_back = (ImageView) findViewById(R.id.navigation_back);
        navigation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txtViewCameraTitle = (TextView) findViewById(R.id.txtview_cameratitle);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        adapter = new ImageGridAdapter(imageBucket == null ? new ArrayList<ImageItem>() : imageBucket.imageList, maxAllowSelectNumber);
        gridView.setAdapter(adapter);

        //其他相册
        tv_otherPics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoAlbumList();
            }
        });
        //取消
        findViewById(R.id.tv_cancelpic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (ImageItem i : selectedImageList) {
                    i.isSelected = false;
                }
                //todo 退出
//                boolean imageSelectFlag = new PollPubManager().isImageSelect(getActivity());
//                int fragmentCount = ViewUtil.getFragmentCount(getFragmentManager());
//                if (imageSelectFlag && fragmentCount == 1) {
//                    getActivity().finish();
//                } else {
//                    AlbumFragment.this.popBackStack();
//                }
            }
        });

        Button btnFinish = (Button) findViewById(R.id.btn_finish);
        //使用
        btnFinish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finishPhotoSelect();
            }
        });

        //左下角  预览
        bt_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isNull()) return;
                //gotoPreviewPhoto(selectedImageList, selectedImageList, 0);
            }
        });

        if (imageBucket == null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    asyncLoadPhoto();
                }
            });
        }
        displayImgOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.top)
                .showImageForEmptyUri(R.drawable.top)
                .showImageOnFail(R.drawable.top)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }


    private void asyncLoadPhoto() {
        // Log.e("AlbumFragment", "==============================asyncLoadPhoto");
        //如果是第一次加载,进行异步刷新
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                AlbumHelper helper = AlbumHelper.getHelper();
                imageBucket = helper.getDCIMBucket(false);
                return imageBucket;
            }

            @Override
            protected void onPostExecute(Object o) {
                imageBucket= (ImageBucket) o;
                adapter.setImageList(imageBucket == null ? new ArrayList() : imageBucket.imageList);
                adapter.notifyDataSetChanged();
            }
        };
        task.execute();
    }

    /**
     * 结束图片选择
     */
    private void finishPhotoSelect() {
        //todo 返回进行发送
        if (isNull()) return;
        Intent intent = new Intent();
        intent.putExtra(Constants.PhotoSelected_SelectedImages, selectedImageList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 判断是否选择图片，进行 （预览--使用）
     *
     * @return
     */
    private boolean isNull() {
        if (selectedImageList == null || selectedImageList.size() == 0) {
            Toast.makeText(AlbumActivity.this, "没有选择图片", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void gotoAlbumList() {
        AlbumActivity.this.startActivityForResult(new Intent(AlbumActivity.this, AlbumListActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //todo 进行刷新
        if (resultCode == 1) {
            ImageBucket imageBucket = (ImageBucket) data.getSerializableExtra(Constants.ImageBucket_ParamName);
            txtViewCameraTitle.setText(imageBucket.bucketName);
            adapter.setImageList(imageBucket == null ? new ArrayList() : imageBucket.imageList);
            adapter.notifyDataSetChanged();
        }
    }

    //todo  预览 执行放大缩小操作
    private void gotoPreviewPhoto(List allImages, List selectedImages, int position) {
//        Bundle param = new Bundle();
//        param.putSerializable(Constants.PhotoSelected_AllImages, (ArrayList) allImages);
//        param.putSerializable(Constants.PhotoSelected_SelectedImages, (ArrayList) selectedImages);
//        param.putInt(Constants.PhotoSelected_CurrentPosition, position);
//        param.putInt(Constants.PhotoSelected_MaxAllowSelectNumber, maxAllowSelectNumber);
//
//        AlbumFragment.this.performGoAction("gotoPhotoView", param, new ReturnResult() {
//            @Override
//            public void onResult(int resultCode, Bundle resultData) {
//                System.out.println(" refresh the imgViewSelectedFlag images");
//            }
//        });
    }

    static class Holder {
        private ImageView imgView;
        private ImageButton imgViewSelectedFlag;
        private TextView text;
    }

    private class ImageGridAdapter extends BaseAdapter {
        List<ImageItem> dataList;
        private int maxAllowSelectNumber;

        public ImageGridAdapter(List<ImageItem> list, int maxAllowSelectNumber) {
            dataList = list;
            this.maxAllowSelectNumber = maxAllowSelectNumber;

            for (ImageItem i : dataList) {
                i.isSelected = false;
            }

            if (selectedImageList.size() != 0) {
                for (ImageItem i : selectedImageList) {
                    i.isSelected = true;
                }
//                tv_size.setVisibility(View.VISIBLE);
//                tv_size.setText(String.valueOf(selectedImageList.size()));
            }
        }

        public void setImageList(List<ImageItem> list) {
            for (ImageItem i : list) {
                i.isSelected = false;
            }
            this.dataList = list;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ImageItem imgItem = dataList.get(position);
            final Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(AlbumActivity.this, R.layout.item_image_grid, null);
                holder.imgView = (ImageView) convertView.findViewById(R.id.imgview_photo);
                holder.imgViewSelectedFlag = (ImageButton) convertView.findViewById(R.id.imgview_selected_flag);
                holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
//          String filePath = imgItem.thumbnailPath !=null || imgItem.thumbnailPath.length() > 0  ? imgItem.thumbnailPath : imgItem.imagePath;
            String filePath = imgItem.imagePath;

            //holder.imgView.setImageBitmap(null);
            AlbumThumbImageLoader imageloader = AlbumThumbImageLoader.getInstance();
            imageloader.cancelDisplayTask(holder.imgView);
            imageloader.displayImage("file://" + filePath, holder.imgView, displayImgOptions);

            Log.e("AlbumFragment", "==============================" + position + "===" + filePath);

            if (imgItem.isSelected) {
                holder.imgViewSelectedFlag.setImageResource(R.drawable.icon_data_select);
            } else {
                holder.imgViewSelectedFlag.setImageResource(R.drawable.icon_data_unselect);
                holder.text.setBackgroundColor(0x00000000);
            }

            //右上角标示
            holder.imgViewSelectedFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRightCorner(imgItem, holder);
                }
            });

            //点击单个详情
            holder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPreviewPhoto(dataList, selectedImageList, position);
                    Log.d("ddd", "ddd");
                }
            });
            return convertView;
        }

        /**
         * 点击右上角标示
         *
         * @param imgItem
         * @param holder
         */
        private void onRightCorner(ImageItem imgItem, Holder holder) {
            if (imgItem.isSelected) {
                imgItem.isSelected = false;
                holder.imgViewSelectedFlag.setImageResource(R.drawable.icon_data_unselect);
                selectedImageList.remove(imgItem);
            } else if (!imgItem.isSelected) {
                imgItem.isSelected = true;
                if (selectedImageList.size() >= maxAllowSelectNumber) {
                    Toast.makeText(AlbumActivity.this, "最多选择3张", Toast.LENGTH_SHORT).show();
                    return;
                }
                holder.imgViewSelectedFlag.setImageResource(R.drawable.icon_data_select);
                selectedImageList.add(imgItem);
            }

//            if (selectedImageList.size() > 0) {
//                tv_size.setVisibility(View.VISIBLE);
//                tv_size.setText("" + selectedImageList.size());
//            } else {
//                tv_size.setVisibility(View.GONE);
//            }
        }
    }
}
