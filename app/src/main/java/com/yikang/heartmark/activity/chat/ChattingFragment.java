//package com.yikang.heartmark.activity.chat;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.heartmark.R;
//import com.yikang.heartmark.application.AppContext;
//import com.yikang.heartmark.common.business.other.DataHandler;
//import com.yikang.heartmark.constant.Constants;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Created by guangdye on 2014/11/13.
// */
//public class ChattingFragment extends BaseChattingFragment {
//    private static Logger logger = LoggerFactory.getLogger(ChattingFragment.class);
//
//    @Override
//    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
//        View view = inflater.inflate(R.layout.fragment_chatting, container, false);
//        ImageView imgViewStar = (ImageView) view.findViewById(R.id.imgView_star);
//        imgViewStar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupShortCutDialog();
//            }
//        });
//
//        return view;
//    }
//
//    //常用语句
//    public void popupShortCutDialog() {
////        DialogSelectdItemListener selectdItemListener = new DialogSelectdItemListener<ShortCut>() {
////            @Override
////            public void onItemSelected(int position, ShortCut value) {
////                if (value != null) {
////                    EditText chatInput = (EditText) view.findViewById(R.id.edt_chat_input);
////                    chatInput.setText(((ShortCut) value).getSentence());
////                }
////            }
////        };
////
////        ShortcutDialog dlg = new ShortcutDialog();
////        dlg.setDialogSelectdItemListener(selectdItemListener);
////        dlg.show(getActivity().getSupportFragmentManager(), null);
//    }
//
//    @Override
//    protected void initExtra() {
//        noteTxt = (TextView) view.findViewById(R.id.txt_title_note);
//        noteTxt.setOnClickListener(viewClickListener);
//    }
//
//    @Override
//    protected void gotoDoctorProfile() {
//        //Doctor verion
//        logger.debug("gotoDoctorProfile Doctor (self)");
//        intentToDoctorInfo();
//    }
//
//    @Override
//    protected void gotoPatientProfile() {
//        //Doctor verion
//        logger.debug("gotoDoctorProfile Doctor (self)");
//        intentToPatientInfo();
//    }
//
//    private void intentToDoctorInfo() {
//        logger.debug("intentToDoctorInfo Doctor (self) no implemented");
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.DOCTOR, AppContext.getAppContext().getCurrentUser());
//        performGoAction("gotoDoctorDetail", bundle);
//    }
//
//    private void intentToPatientInfo() {
////        logger.debug("intentToPatientInfo Doctor (self)");
////        Patient patient = DoctorManager.getInstance().getPatientfromDB(chatSession.getUserId());
////
//////        if (patient == null) {
//////            DoctorManager.getInstance().getPatientDetailAsync(getActivity(),
//////                    ((Doctor) AppContext.getAppContext().getCurrentUser()).getDoctorId(), String.valueOf(chatSession.getUserId()), patientInfoDataHandler);
//////        } else {
////        Bundle bundle = new Bundle();
////        bundle.putSerializable(Constants.PATIENT, patient);
////        bundle.putBoolean(Constants.IS_FROM_CHAT, true);
////        performGoAction("gotoPatientDetail", bundle);
//////        }
//    }
//
//    DataHandler patientInfoDataHandler = new DataHandler() {
//        @Override
//        public void onData(int code, String reason, Object obj) {
////            if (code == Constants.SERVER_SUCCESS) {
////                Patient patient = (Patient) obj;
////                Bundle bundle = new Bundle();
////                bundle.putSerializable(Constants.PATIENT, patient);
////                performGoAction("gotoPatientDetail", bundle);
////            } else {
////                HttpResponseManager.getInstance().handleError(getActivity(), Constants.HttpResponse.Doctor.GET_PATIENTINFO, code);
////            }
//        }
//    };
//
//    @Override
//    protected void addImageNote(View view, final int id) {
////        final PopupWindow popWindow = new PopupWindow();
////        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.menu_add_to_notes, null);
////        TextView tv = (TextView) popView.findViewById(R.id.txt_add_to_note);
////        tv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Doctor doctor = (Doctor) AppContext.getAppContext().getCurrentUser();
////                Patient patient = DoctorManager.getInstance().getPatientfromDB(chatSession.getUserId());
////                NoteManager.getInstance().addNote(getActivity(), doctor.getDoctorId(),
////                        patient.getPatientId(), null, Constants.Note.TYPE_CHAT, chatMessages.get(id), addNoteHandler);
////                popWindow.dismiss();
////            }
////        });
////        popWindow.setContentView(popView);
////        popWindow.setOutsideTouchable(true);
////        popWindow.setWidth(250);
////        popWindow.setHeight(100);
////        popWindow.setBackgroundDrawable(new BitmapDrawable());
////        int[] location = new int[2];
////        view.getLocationOnScreen(location);
////        int offsetX;
////        if (chatMessages.get(id).getDirection() == Constants.Chat.CHAT_DIRECTION_RECEIVE) {
////            tv.setBackgroundResource(R.drawable.chat_bg_add_note_left);
////            offsetX = location[0] + popWindow.getWidth() / 2;
////        } else {
////            offsetX = location[0] - popWindow.getWidth() / 2;
////        }
////        popWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, location[1] - popWindow.getHeight() / 2);
//
//    }
//
//    @Override
//    protected void setAdd2NotePopMenu(View view, final int id) {
////        final PopupWindow popWindow = new PopupWindow();
////        final String content = chatMessages.get(id).getContent();
////        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.menu_add_to_notes, null);
////        TextView tv = (TextView) popView.findViewById(R.id.txt_add_to_note);
////        tv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Doctor doctor = (Doctor) AppContext.getAppContext().getCurrentUser();
////                Patient patient = DoctorManager.getInstance().getPatientfromDB(chatSession.getUserId());
////                NoteManager.getInstance().addNote(getActivity(), doctor.getDoctorId(),
////                        patient.getPatientId(), content, Constants.Note.TYPE_CHAT, chatMessages.get(id), addNoteHandler);
////                popWindow.dismiss();
////            }
////        });
////        popWindow.setContentView(popView);
////        popWindow.setOutsideTouchable(true);
////        popWindow.setWidth(250);
////        popWindow.setHeight(100);
////        popWindow.setBackgroundDrawable(new BitmapDrawable());
////        int[] location = new int[2];
////        view.getLocationOnScreen(location);
////        int offsetX;
////        if (chatMessages.get(id).getDirection() == Constants.Chat.CHAT_DIRECTION_RECEIVE) {
////            tv.setBackgroundResource(R.drawable.chat_bg_add_note_left);
////            offsetX = location[0] + popWindow.getWidth() / 2;
////        } else {
////            offsetX = location[0] - popWindow.getWidth() / 2;
////        }
////        popWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, location[1] - popWindow.getHeight() / 2);
//    }
//
////    DataHandler addNoteHandler = new DataHandler() {
////        @Override
////        public void onData(int code, String reason, Object obj) {
////            if (code == Constants.SERVER_SUCCESS) {
////                Toast.makeText(getActivity(), getString(R.string.note_add_success), Toast.LENGTH_SHORT).show();
////            } else {
////                //AppCommonService.getInstance().handleError(getActivity(), code);
////                HttpResponseManager.getInstance().handleError(getActivity(),
////                        Constants.HttpResponse.Doctor.ADD_NOTE,
////                        code);
////            }
////        }
////    };
//
//}