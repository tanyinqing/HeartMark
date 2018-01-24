//package com.yikang.heartmark.controller.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.lepu.pasm.R;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import java.util.Date;
//import java.util.List;
//
//import model.Notice;
//import util.DateUtil;
//
//
///**
// * Created by lepu on 2014/12/25.
// */
//public class NoticesAdapter extends BaseAdapter {
//    Context context;
//    List<Notice> list;
//    ImageLoader imageLoader;
//
//    public NoticesAdapter(Context context, List<Notice> list, ImageLoader imageLoader) {
//        this.context = context;
//        this.list = list;
//        this.imageLoader = imageLoader;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        ViewHodler hodler = null;
//        if (view == null) {
//            hodler = new ViewHodler();
//            view = LayoutInflater.from(context).inflate(R.layout.item_notice_patient, null);
//            hodler.headerImg = (ImageView) view.findViewById(R.id.imgView_header);
//            hodler.photo = (ImageView) view.findViewById(R.id.img_photo);
//            hodler.txtDoctorName = (TextView) view.findViewById(R.id.txt_doctorName);
//            hodler.time = (TextView) view.findViewById(R.id.time);
//            hodler.content = (TextView) view.findViewById(R.id.content);
//            view.setTag(hodler);
//        } else {
//            hodler = (ViewHodler) view.getTag();
//        }
//        Notice notice = list.get(i);
//        Date data = notice.getCreatedTime();
//        hodler.time.setText(DateUtil.formatNoticeDateTime(data));
//        hodler.content.setText(notice.getContent());
////        if (notice.getDoctor() != null) {
////            hodler.txtDoctorName.setText(notice.getDoctor().getUserInfo().getNickName());
////        }
////        String url = null;
////
////
////            if (notice.getDoctor() != null && !notice.getDoctor().getUserInfo().getProfilePictureThumbnailId().equals("")) {
////                url = AppCommonService.getInstance().getObjectDownloadUrl(notice.getDoctor().getUserInfo().getProfilePictureThumbnailId());
////                ImageLoaderUtil.getInstance().loadImage(imageLoader, url, hodler.headerImg);
////            }
//
//
//        return view;
//    }
//
//    class ViewHodler {
//        ImageView headerImg, photo;
//        TextView time, content, txtDoctorName;
//    }
//}
