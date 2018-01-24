package com.yikang.heartmark.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.business.other.DataHandler;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.controller.base.BaseChattingFragment;
import com.yikang.heartmark.model.chat.Patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by changliu on 2015/01/27.
 * <p/>
 * 病人版本
 */
public class ChattingFragment extends BaseChattingFragment {
    private static Logger logger = LoggerFactory.getLogger(ChattingFragment.class);
    private Patient patient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  patient = (Patient) AppContext.getAppContext().getCurrentUser();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_chatting_patient, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.ic_level);
        // view.findViewById(R.id.imgView_complain).setOnClickListener(ComolainClick);
        return view;
    }

    @Override
    protected void initExtra() {
//        TextView txtDoctorDetail = (TextView) view.findViewById(R.id.txt_doctor_detail);
//        txtDoctorDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intentToDoctorInfo();
//            }
//        });
    }

    @Override
    protected void gotoDoctorProfile() {
        //Patient verion
        logger.debug("gotoDoctorProfile Patient (self)");
        intentToDoctorInfo();
    }

    @Override
    protected void gotoPatientProfile() {
        //Patient verion
        logger.debug("gotoPatientProfile Patient (self)");
        intentToPatientInfo();
    }

    private void intentToPatientInfo() {
        logger.debug("intentToPatientInfo Patient (self) no implemented");
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PATIENT, AppContext.getAppContext().getCurrentUser());

    }

    private void intentToDoctorInfo() {
//        logger.debug("intentToDoctorInfo Patient (self)");
//        Doctor doctor = PatientManager.getInstance().getDoctorfromDB(chatSession.getUserId());
//
//        if (doctor == null) {
//            PatientManager.getInstance().getDoctorDetailAsync(getActivity(),
//                    ((Patient) AppContext.getAppContext().getCurrentUser()).getPatientId(), String.valueOf(chatSession.getUserId()), doctorInfoDataHandler);
//        } else {
////            Bundle bundle = new Bundle();
////            bundle.putSerializable(Constants.DOCTOR, doctor);
////            performGoAction("gotoDoctorDetail", bundle);
//        }
    }

    DataHandler doctorInfoDataHandler = new DataHandler() {
        @Override
        public void onData(int code, String reason, Object obj) {
//            if (code == Constants.SERVER_SUCCESS) {
//                Doctor doctor = (Doctor) obj;
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.DOCTOR, doctor);
//                performGoAction("gotoDoctorDetail", bundle);
//            } else {
//                HttpResponseManager.getInstance().handleError(getActivity(), Constants.HttpResponse.Doctor.GET_PATIENTINFO, code);
//            }
        }
    };

}
