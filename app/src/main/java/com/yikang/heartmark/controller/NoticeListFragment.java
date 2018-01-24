//package com.yikang.heartmark.controller;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.lepu.pasm.R;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.pasm.application.MyApplication;
//import com.pasm.business.NoticeManager;
//import com.pasm.controller.adapter.NoticesAdapter;
//import com.pasm.controller.base.AbsBaseFragment;
//import com.pasm.wiget.listview.XListView;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import common.db.AppContext;
//import common.db.Constants;
//import model.Notice;
//import model.Patient;
//
///**
// * Created by guangdye on 2014/11/13.
// * 公告板
// */
//public class NoticeListFragment extends AbsBaseFragment implements XListView.IXListViewListener {
//    View view;
//    ImageView backImg;
//    XListView listView;
//
//    List<Notice> notices = new ArrayList<Notice>();
//    NoticesAdapter adapter;
//    ProgressBar progressBar;
//    TextView noticeTip;
//    ImageLoader imageLoader = ImageLoader.getInstance();
//    Patient patient;
//    private Handler mHandler;
//    private String cursor;
//    private Integer num = 20;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        patient = (Patient) AppContext.getInstance().getCurrentUser();
//        mHandler = new Handler();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_notices_patient, container, false);
//        TextView announceNotice = (TextView) view.findViewById(R.id.txt_announcement);
//        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
//        RelativeLayout relLayoutTitle = (RelativeLayout) view.findViewById(R.id.relLayout_title);
//        announceNotice.setVisibility(View.GONE);
//        txtTitle.setText(R.string.notice_brand);
//        relLayoutTitle.setBackgroundResource(R.color.bg_title_patient);
//
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
//        backImg = (ImageView) view.findViewById(R.id.imgView_title_back);
//        listView = (XListView) view.findViewById(R.id.lstView_notices);
//        listView.setPullLoadEnable(true);
//        listView.setPullRefreshEnable(true);
//        listView.setXListViewListener(this);
//       // listView.setFooterViewInVisible();
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_wait);
//        noticeTip = (TextView) view.findViewById(R.id.txt_notice_tip);
//
//        backImg.setOnClickListener(listener);
//
//        adapter = new NoticesAdapter(getActivity(), notices, imageLoader);
//        listView.setAdapter(adapter);
//
//        setNewNoticeListHandle();
//        //进行缓存
//        NoticeManager.getInstance().getNewNoticePatient(MyApplication.getApplication(), patient.getPatientId(), num, 0);
//        return view;
//    }
//
//
//    private View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.imgView_title_back:
//                    //performBack();
//                    break;
//            }
//        }
//    };
//
//
//    private void setNewNoticeListHandle() {
//        Handler h = new Handler() {
//            public void handleMessage(Message msg) {
//                Bundle data = msg.getData();
//                switch (msg.what) {
//                    case Constants.Counts.MSG_NEW_NOTICE: //新患者关系
//                        progressBar.setVisibility(View.GONE);
//                        boolean getNoticeListSuccess = data.getBoolean(Constants.Notice.GET_NEW_NOTICES_LIST_SUCCESS);
//                        cursor = data.getString(Constants.Notice.NEW_NOTICES_CURSOR);
//                        //拿到公告列表成功后，新公告的小红点消失
//                        if (getNoticeListSuccess) {
//                            initNoticeCount();
//                        }
//                        loadNotice();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//
//        AppContext.getInstance().setNewNoticeListHandler(h);
//    }
//
//    private void loadNotice() {
//        List<Notice> tempNotices = NoticeManager.getInstance().getNoticeListFromCachePatient(AppContext.getAppContext());
//        if (tempNotices != null) {
//            Set<String> idSet = new HashSet();
//            for (Notice info : notices) {
//                idSet.add(info.getNoticeId() + "");
//            }
//
//            for (int i = tempNotices.size() - 1; i >= 0; i--) {
//                if (idSet.contains(tempNotices.get(i).getNoticeId())) {
//                    tempNotices.remove(i);
//                }
//            }
//
//            notices.addAll(tempNotices);
//            adapter.notifyDataSetChanged();
//          //  listView.setFooterViewVisible();
//
//        }
//    }
//
//
//    private void initNoticeCount() {
//        if (null != AppContext.getAppContext().getNewNoticeHandler()) {
//            Message msg = new Message();
//            msg.what = Constants.Counts.MSG_INIT_NOTICE_COUNT_PATIENT;
//            AppContext.getAppContext().getNewNoticeHandler().sendMessage(msg);
//        }
//    }
//
//    private void onLoad() {
//        listView.stopRefresh();
//        listView.stopLoadMore();
//    }
//
//    /**
//     * 刷新
//     */
//    @Override
//    public void onRefresh() {
//       // listView.setFooterViewInVisible();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notices.clear();
//                NoticeManager.getInstance().getNewNoticePatient(AppContext.getAppContext(), patient.getPatientId(), num, 0);
//                onLoad();
//            }
//        }, 2000);
//    }
//
//    /**
//     * 加载更多
//     */
//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onLoad();
//                if (cursor != null && !cursor.equals("-1")) {
//                    NoticeManager.getInstance().getNewNoticePatient(MyApplication.getApplication(), patient.getPatientId(), num, Integer.valueOf(cursor));
//                } else if (cursor != null && cursor.equals("-1")) {
//                  //  listView.setFooterViewInVisible();
////                    listView.removeFooter();
//                }
//            }
//        }, 2000);
//    }
//}
