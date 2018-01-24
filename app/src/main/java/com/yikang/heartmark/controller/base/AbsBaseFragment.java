package com.yikang.heartmark.controller.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.yikang.heartmark.controller.adapter.ChattingAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chuanyhu on 2014/8/2.
 * fragment 核心业务类--待扩展
 */
public abstract class AbsBaseFragment extends Fragment {

    protected Logger logger = LoggerFactory.getLogger(((Object) this).getClass());

    private Map<Integer, ReturnResult> retResultMap = new HashMap<Integer, ReturnResult>();
    private AtomicInteger requestCodeGen = new AtomicInteger(1);
    private Bundle backArguments;

    private int backReturnCode;

    public void setBackArguments(Bundle backArguments, String fromFragmentName) {
        this.backArguments = backArguments;
    }

    public void resetBackReturnCode() {
        backReturnCode = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void clearData() {
    }

//    public void performGoAction(String goActionName, Bundle param) {
//        backReturnCode = 0;
//        try {
//            UIFlowManager flowManager = UIFlowManager.getInstance();
//            UIComDesc targetUiCom = flowManager.getTargetUI(this, goActionName);
//            if (targetUiCom.getType() == UIComDesc.UIComType_Activity) {
//                flowManager.performGoActivityAction(this.getActivity(), goActionName, targetUiCom, param);
//            } else if (targetUiCom.getType() == UIComDesc.UIComType_Fragment) {
//                flowManager.performGoFragmentAction(this.getActivity(), this, goActionName, param, 0);
//            }
//        } catch (Exception ex) {
//            logger.error(ex.getMessage() + "");
//        }
//    }
//
//    public void performGoAction(String goActionName, Bundle param, ReturnResult reqResult) {
//        backReturnCode = 0;
//        UIFlowManager flowManager = UIFlowManager.getInstance();
//        UIComDesc targetUiCom = flowManager.getTargetUI(this, goActionName);
//
//        if (targetUiCom.getType() == UIComDesc.UIComType_Activity) {
//            int requestCode = requestCodeGen.incrementAndGet();
//            retResultMap.put(requestCode, reqResult);
//            flowManager.performGoActivityAction(this.getActivity(), this, goActionName, param, requestCode, reqResult);
//        } else {
//            int requestCode = requestCodeGen.incrementAndGet();
//            retResultMap.put(requestCode, reqResult);
//            backReturnCode = requestCode;
//            flowManager.performGoFragmentAction(this.getActivity(), this, goActionName, param, requestCode);
//        }
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ReturnResult retResult = retResultMap.get(requestCode);
        if (retResult == null) {
            logger.warn("can't find ReturnResult for code:" + requestCode);
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            retResultMap.remove(requestCode);
            return;
        }

        Bundle resultData = data == null ? null : data.getExtras();
        try {
            retResult.onResult(resultCode, resultData);
        } catch (Throwable th) {
            logger.error(th.toString(), th);
        }
        retResultMap.remove(requestCode);
    }
//
//    public void performBack(Bundle param) {
//        UIFlowManager flowManager = UIFlowManager.getInstance();
//        flowManager.popBackStack(this.getActivity(), this, param);
//    }
//
//    public void performBack() {
//        UIFlowManager flowManager = UIFlowManager.getInstance();
//        flowManager.popBackStack(this.getActivity(), this);
//    }
//
//
//    public void performBackToRoot() {
//        UIFlowManager flowManager = UIFlowManager.getInstance();
//        flowManager.popToRoot(this.getActivity(), this);
//    }
//
//    public void performBackToMainRoot() {
//        UIFlowManager flowManager = UIFlowManager.getInstance();
//        flowManager.popToMainRoot(this.getActivity(), this);
//    }


    @Override
    public void onResume() {
        super.onResume();
//        if (getActivity() instanceof BaseFragmentActivity) {
//            ((BaseFragmentActivity) getActivity()).setCurrentFragment(this);
//        }

        if (backReturnCode == 0) {
            return;
        }

        final ReturnResult retResult = retResultMap.get(backReturnCode);
        if (retResult == null) {
            logger.warn("can't find ReturnResult for code:" + backReturnCode);
        }

        try {

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (backArguments != null) {
                        retResult.onResult(0, backArguments);
                    }

                }
            });

        } catch (Throwable th) {
            logger.error(th.toString(), th);
        }
        retResultMap.remove(backReturnCode);
        backReturnCode = 0;

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    public interface FragmentCallback {
        public void requestResendPhoto(int id);

        public void addTextToNote(View view, int id);

        public void addImageToNote(View view, int id);

        public void reSendContent(int id);

//        public void intentPatientInfo();

        public void showLargePhoto(int id);


      public void playVoice(int id, ChattingAdapter.OnVoicePlayEnd onVoicePlayEnd);

        public void stopVoid(int id);

        public void gotoProfile(String direction);
    }

}
