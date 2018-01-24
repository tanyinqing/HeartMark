package com.yikang.heartmark.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.heartmark.common.business.other.DataHandler;
import com.yikang.heartmark.model.chat.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Created by lepu on 2014/12/18.
 */
public class AtozAdapterPatient extends BaseAdapter {
    private static Logger logger = LoggerFactory.getLogger(AtozAdapterPatient.class);
    List<? extends User> list;
    Context context;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    //String url;
    private DataHandler selectCallback;

    public AtozAdapterPatient(Context context, List<? extends User> list, ImageLoader imageLoader, DisplayImageOptions options) {
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
        this.options = options;
        //this.url = url;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        viewHolder holder = null;
//        UserInfo userInfo = list.get(i).getUserInfo();
//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.item_simple_user_patient, null);
//            holder = new viewHolder();
////            holder.ageTxt = (TextView) view.findViewById(R.id.age);
//            holder.imgBtnSelect = (ImageView) view.findViewById(R.id.imgBtn_select);
//            holder.nameTxt = (TextView) view.findViewById(R.id.txt_name);
//            holder.headImgView = (ImageView) view.findViewById(R.id.imgView_head);
//            holder.departmentTxt = (TextView) view.findViewById(R.id.txt_department);
//            holder.alphaTxt = (TextView) view.findViewById(R.id.txt_alpha);
//            holder.tubiao = (ImageView) view.findViewById(R.id.tubiao);
//            holder.tags = (TextView) view.findViewById(R.id.txt_tag);
//            view.setTag(holder);
//        } else {
//            holder = (viewHolder) view.getTag();
//        }
//        if (i == 0) {
//            holder.alphaTxt.setVisibility(View.VISIBLE);
//            holder.alphaTxt.setText(userInfo.getAlpha().toUpperCase());
//        } else {
//            if (list.get(i - 1).getUserInfo().getAlpha().equals(userInfo.getAlpha())) {
//                holder.alphaTxt.setVisibility(View.GONE);
//            } else {
//                holder.alphaTxt.setVisibility(View.VISIBLE);
//                holder.alphaTxt.setText(userInfo.getAlpha().toUpperCase());
//            }
//
//        }
//        Doctor doctor = (Doctor) list.get(i);
//        holder.departmentTxt.setText(doctor.getDepartmentName());
//
//        String tagsStr = doctor.getHospitalName();
//        holder.tags.setText(tagsStr);
//
//        holder.nameTxt.setText(userInfo.getNickName());
//
//        ImageLoader.getInstance().displayImage(AppContext.getAppContext().getDownloadUrl(doctor.getProduct_icon_id()), holder.tubiao
//                , new DisplayImageOptions.Builder().
//                considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
//                imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
//                build());
//
//        ImageLoader.getInstance().displayImage(AppContext.getAppContext().getDownloadUrl(userInfo.getProfilePictureThumbnailId()), holder.headImgView, getOption());
//
//        if (selectCallback != null) {
//            handleSelectView(holder, userInfo, i);
//        }
        return null;
    }
//
//    private void handleSelectView(final viewHolder holder, final UserInfo item, final int i) {
//        holder.imgBtnSelect.setVisibility(selectCallback != null ? View.VISIBLE : View.GONE);
//        holder.imgBtnSelect.setImageResource(item.isSelected ? R.drawable.icon_data_select : R.drawable.icon_data_unselect);
//        holder.imgBtnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.isSelected) {
//                    item.isSelected = false;
//                    holder.imgBtnSelect.setImageResource(R.drawable.icon_data_unselect);
//                    selectCallback.onData(Constants.Selected.ISNOTSELECTED, "", i);
////                    selectedImageList.remove(item);
//                } else if (!item.isSelected) {
//                    item.isSelected = true;
//                    holder.imgBtnSelect.setImageResource(R.drawable.icon_data_select);
//                    selectCallback.onData(Constants.Selected.ISSELECTED, "", i);
////                    selectedImageList.add(item);
//                }
//
//            }
//        });
//    }
//
//    class viewHolder {
//        TextView departmentTxt, nameTxt, alphaTxt, tags;
//        ImageView headImgView, tubiao;
//        ImageView imgBtnSelect;
//    }
//
//
//    private String parseToTags(Patient patient) {
//        if (null != patient) {
//            return "";
//        }
//        if (null == patient.getJsonTags() || patient.getJsonTags().isEmpty())
//            return "";
//
//        List<Tag> tags = new ArrayList<Tag>();
//        StringBuffer sb = new StringBuffer();
//        try {
//            JSONArray array = new JSONArray(patient.getJsonTags());
//            if (array == null || array.length() == 0 || patient.getJsonTags().equals("")) {
//                return "";
//            }
//
//            for (int i = 0; i < array.length(); i++) {
//                sb.append(array.getString(i) + " | ");
//            }
//
//        } catch (JSONException e) {
//            logger.error(e.toString());
//            return "";
//        }
//
//        return sb.toString().substring(0, sb.length() - 2);
//    }

//    private DisplayImageOptions getOption() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.icon_header_default_patient)
//                .showImageForEmptyUri(R.drawable.icon_header_default_patient)
//                .showImageOnFail(R.drawable.icon_header_default_patient)       // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)
//                .build();
//        return options;
//    }
}
