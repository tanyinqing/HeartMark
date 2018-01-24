package com.yikang.heartmark.common.business.other;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.yikang.heartmark.activity.LoginActivity;
import com.yikang.heartmark.application.AppContext;


/**
 * Created by guolchen on 2014/12/5.
 */
public class LoginUIInterface {

    public static void logoffUI() {

        AppContext appContext = AppContext.getAppContext();
        if (appContext.getCurrentUser() != null) {

            if (appContext.isShowingLogoutUI())
                return;

            appContext.setShowingLogoutUI(true);
            //退出登陆
            AppCommonService.getInstance().logoutXGXMPP();

//            AlertDialog.Builder builder = new AlertDialog.Builder(AppContext.getAppContext().getCurrentActivity());
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setMessage("亲，你的帐号已在另一台设备上登录了")
                    .setCancelable(false)
                    .setPositiveButton("退出登录", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            AppContext appContext = AppContext.getAppContext();
                            Intent intent;
//                            if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//                                intent = new Intent(appContext, com.kanebay.lepu.askdr.doctor.ui.login.LoginActivity.class);
//                            } else {
                            intent = new Intent(appContext, LoginActivity.class);
//                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            appContext.startActivity(intent);
                            //todo
                            //appContext.getCurrentActivity().finish();

                            AppContext.getAppContext().setShowingLogoutUI(false);
                            //LoginManager.getInstance().clearLoginInfo();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
