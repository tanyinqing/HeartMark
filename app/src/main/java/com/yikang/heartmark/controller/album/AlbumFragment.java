package com.yikang.heartmark.controller.album;

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
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.common.alummage.ImageBucket;
import com.yikang.heartmark.common.alummage.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanyhu on 2014/8/2.
 */
public class AlbumFragment extends BaseActivity {
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


        //tv_size = (TextView)findViewById(R.id.tv_size);
        //Button bt_view = (Button) albumview.findViewById(R.id.bt_view);
        TextView tv_otherPics = (TextView) findViewById(R.id.tv_otherPics);
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

                // todo 统计代码 选中图片进入下一页	505
                finishPhotoSelect();
                //滤镜 贴纸
//                gotoPS();
            }
        });

        //左下角  预览
//        bt_view.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                if (isNull()) return;
//                gotoPreviewPhoto(selectedImageList, selectedImageList, 0);
//            }
//        });

        if (imageBucket == null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    asyncLoadPhoto();
                }
            });
        }

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View albumview = inflater.inflate(R.layout.fragment_image_bucket, null);
//
//        tv_size = (TextView) albumview.findViewById(R.id.tv_size);
//        //Button bt_view = (Button) albumview.findViewById(R.id.bt_view);
//        TextView tv_otherPics = (TextView) albumview.findViewById(R.id.tv_otherPics);
//        txtViewCameraTitle = (TextView) albumview.findViewById(R.id.txtview_cameratitle);
//        gridView = (GridView) albumview.findViewById(R.id.gridview);
//        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//
//        adapter = new ImageGridAdapter(imageBucket == null ? new ArrayList<ImageItem>() : imageBucket.imageList, maxAllowSelectNumber);
//        gridView.setAdapter(adapter);
//
//        //其他相册
//        tv_otherPics.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                gotoAlbumList();
//            }
//        });
//        //取消
//        albumview.findViewById(R.id.tv_cancelpic).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                for (ImageItem i : selectedImageList) {
//                    i.isSelected = false;
//                }
//
//                //todo 退出
////                boolean imageSelectFlag = new PollPubManager().isImageSelect(getActivity());
////                int fragmentCount = ViewUtil.getFragmentCount(getFragmentManager());
////                if (imageSelectFlag && fragmentCount == 1) {
////                    getActivity().finish();
////                } else {
////                    AlbumFragment.this.popBackStack();
////                }
//            }
//        });
//
//        Button btnFinish = (Button) albumview.findViewById(R.id.btn_finish);
//        //使用
//        btnFinish.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                // todo 统计代码 选中图片进入下一页	505
//                finishPhotoSelect();
//                //滤镜 贴纸
////                gotoPS();
//            }
//        });
//
//        //左下角  预览
////        bt_view.setOnClickListener(new View.OnClickListener() {
////
////            public void onClick(View v) {
////                if (isNull()) return;
////                gotoPreviewPhoto(selectedImageList, selectedImageList, 0);
////            }
////        });
//
//        if (imageBucket == null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    asyncLoadPhoto();
//                }
//            });
//        }
//        // Log.e("AlbumFragment", "==============================onCreateView " + imageBucket);
//        return albumview;
//    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//
//    }

    private void asyncLoadPhoto() {
        // Log.e("AlbumFragment", "==============================asyncLoadPhoto");
        //如果是第一次加载,进行异步刷新
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                AlbumHelper helper = AlbumHelper.getHelper();
                imageBucket = helper.getDCIMBucket(false);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                adapter.setImageList(imageBucket == null ? new ArrayList() : imageBucket.imageList);
                adapter.notifyDataSetChanged();
                //Log.e("AlbumFragment", "==============================onPostExecute" + (imageBucket == null ? 0 : imageBucket.imageList.size()));
            }
        };
        task.execute();

    }


    /**
     * 结束图片选择
     */
    private void finishPhotoSelect() {
        if (isNull()) return;
        Bundle param = new Bundle();
        //todo 返回进行发送
//        param.putSerializable(Constants.PhotoSelected_SelectedImages, selectedImageList);
//        performGoAction("gotoEmbellish", param);
    }

    /**
     * 判断是否选择图片，进行 （预览--使用）
     *
     * @return
     */
    private boolean isNull() {
        if (selectedImageList == null || selectedImageList.size() == 0) {
            Toast.makeText(AlbumFragment.this, "没有选择图片", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void gotoAlbumList() {
        AlbumFragment.this.startActivityForResult(new Intent(AlbumFragment.this, AlbumListActivity.class), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //todo 进行刷新
//        imageBucket = (ImageBucket) resultData.getSerializable(Constants.ImageBucket_ParamName);
//        txtViewCameraTitle.setText(imageBucket.bucketName);
//        adapter.setImageList(imageBucket == null ? new ArrayList() : imageBucket.imageList);
//        adapter.notifyDataSetChanged();
    }

    //预览
//    private void gotoPreviewPhoto(List allImages, List selectedImages, int position) {
//        Bundle param = new Bundle();
//        param.putSerializable(Constants.PhotoSelected_AllImages, (ArrayList) allImages);
//        param.putSerializable(Constants.PhotoSelected_SelectedImages, (ArrayList) selectedImages);
//        param.putInt(Constants.PhotoSelected_CurrentPosition, position);
//        param.putInt(Constants.PhotoSelected_MaxAllowSelectNumber, maxAllowSelectNumber);
//
//        //以下对象不做任何使用，只确保AlbumListFragment混淆成功
//        albumListFragment = new AlbumListFragment();
//        productPreviewFragment = new ProductPreviewFragment();
//
//        AlbumFragment.this.performGoAction("gotoPhotoView", param, new ReturnResult() {
//            @Override
//            public void onResult(int resultCode, Bundle resultData) {
//                System.out.println(" refresh the imgViewSelectedFlag images");
//            }
//        });
//    }

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
                convertView = View.inflate(AlbumFragment.this, R.layout.item_image_grid, null);
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
//            holder.imgView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    gotoPreviewPhoto(dataList, selectedImageList, position);
//                }
//            });
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
                    Toast.makeText(AlbumFragment.this, "最多选3张", Toast.LENGTH_SHORT).show();
                    return;
                }
                holder.imgViewSelectedFlag.setImageResource(R.drawable.icon_data_select);
                selectedImageList.add(imgItem);
            }

            if (selectedImageList.size() > 0) {
                tv_size.setVisibility(View.VISIBLE);
                tv_size.setText("" + selectedImageList.size());
            } else {
                tv_size.setVisibility(View.GONE);
            }
        }
    }
}
