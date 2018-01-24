package com.yikang.heartmark.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heartmark.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.util.DateUtil;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ChatSession;

import java.util.List;


/**
 * Created by lepu on 2014/12/20.
 */
public class ChatSessionAdapter extends BaseAdapter {
    Context context;
    List<ChatSession> list;
    ImageLoader imageLoader;

    public ChatSessionAdapter(Context context, List<ChatSession> list, ImageLoader imageLoader) {
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
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
        viewHolder holder = null;
        if (view == null) {
            //todo
//            view = LayoutInflater.from(context).inflate(R.layout.item_chat_session_list_patient, null);
            holder = new viewHolder();
            holder.header = (ImageView) view.findViewById(R.id.imgView_user_head);
            holder.name = (TextView) view.findViewById(R.id.txt_user_name);
            holder.content = (TextView) view.findViewById(R.id.txt_content);
            holder.date = (TextView) view.findViewById(R.id.txt_time);
            holder.newMsg = (TextView) view.findViewById(R.id.txt_chat_new_msg_number);
            holder.ic_level = (ImageView) view.findViewById(R.id.ic_level);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }
        ChatSession chatSession = list.get(i);
        holder.name.setText(chatSession.getUserNickName());
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        //holder.date.setText(simpleDateFormat.format(chatSession.getLastMsgTime()));
        holder.date.setText(DateUtil.formatChatDateTime(chatSession.getLastMsgTime()));
        if (chatSession.getLastMsgContent().equals(Constants.Counts.NEW_PATIENT_FLAG)) {
            holder.content.setText(AppContext.getAppContext().getString(R.string.chat_message_new_relation_patient));
            holder.newMsg.setVisibility(View.VISIBLE);
            holder.newMsg.setText("1");
        } else {
            holder.content.setText(chatSession.getLastMsgContent());
            if (chatSession.getNewMsgCount() != 0) {
                holder.newMsg.setVisibility(View.VISIBLE);
                holder.newMsg.setText(chatSession.getNewMsgCount() + "");
            } else {
                holder.newMsg.setVisibility(View.GONE);
            }
        }

//        holder.content.setText(chatSession.getLastMsgContent());
//        if (chatSession.getNewMsgCount() != 0) {
//            holder.newMsg.setVisibility(View.VISIBLE);
//            holder.newMsg.setText(chatSession.getNewMsgCount() + "");
//        } else {
//            holder.newMsg.setVisibility(View.GONE);
//        }

        //holder.header.setImageResource(R.drawable.icon_header_default_patient);
        //todo 需要设置 新的服务器地址
        //ImageLoader.getInstance().displayImage(AppContext.getAppContext().getDownloadUrl(chatSession.getUserProfilePictureId()), holder.header, getOption());

//        ImageLoader.getInstance().displayImage(AppContext.getAppContext().getDownloadUrl(chatSession.productIconId), holder.ic_level
//                , new DisplayImageOptions.Builder().
//                considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).
//                imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).
//                build());
        return view;
    }

    class viewHolder {
        ImageView header, ic_level;
        TextView name, content, date, newMsg;
    }

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
