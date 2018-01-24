package com.yikang.heartmark.common.business.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yikang.heartmark.activity.LoginActivity;
import com.yikang.heartmark.activity.MainActivity;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.business.chat.ChatManager;
import com.yikang.heartmark.common.db.DbQueryRunner;
import com.yikang.heartmark.common.util.SharedPreferencesUtils;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.DeviceInfo;
import com.yikang.heartmark.model.chat.User;
import com.yikang.heartmark.model.chat.UserInfo;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class AppCommonService {

    private AppCommonService() {
    }


    static class AppCommonServiceHolder {
        static AppCommonService appCommonService = new AppCommonService();
    }

    public static AppCommonService getInstance() {
        return AppCommonServiceHolder.appCommonService;
    }

    private static Logger logger = LoggerFactory.getLogger(AppCommonService.class);

    public String getFormatJID(String jid) {
        return jid + "@" + getXmppHost() + "/" + AppContext.getAppContext().getDeviceInfo().getDeviceId();
    }

    public String getObjectDownloadUrl(String objectId) {
        if (objectId == null || objectId.isEmpty()) return null;
        return AppContext.getAppContext().getServiceAddress(Constants.DOWNLOAD_ADDRESS_NAME) + "/objects/download/?object_id=" + objectId;
    }

    public String getXmppHost() {
        return AppContext.getAppContext().getServiceAddress(Constants.CHAT_ADDRESS_NAME).split(":")[0];
    }

    public int getXmppPort() {
        return Integer.valueOf(AppContext.getAppContext().getServiceAddress(Constants.CHAT_ADDRESS_NAME).split(":")[1]);
    }

    public int getOfflineMsgIQQueryTime() {
        return 5000;
    }

    public boolean getRemoteServerAddressList() {
        try {
//            String clientVersion = "{\"client_version\":\"\"}";
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("client_version", clientVersion);
//            String serviceAddressUrl = Constants.SERVICE_ADDRESS_URL;
//            ResultData resultData = HttpRequestUtil.sendRequestSync(serviceAddressUrl, jsonObj, null);
//            if (resultData.getCode() == Constants.SERVER_SUCCESS) {
//                JSONObject msgData = resultData.getJsonObject();
//                JSONObject serviceAddressJson = msgData.optJSONObject("service_addr_list");
//                AppContext.getAppContext().setServiceAddressJSON(serviceAddressJson);
//                AppContext.getAppContext().setCallNum(serviceAddressJson.optString("call_number"));
//                AppContext.getAppContext().setWebUrl(serviceAddressJson.optString("web_url"));
//                JSONObject pkobj = msgData.getJSONObject("public_key");
//                AppContext.getAppContext().setPublicKey(pkobj);
//                saveServerAddrList2DB(msgData.toString());
//                return true;
//            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return false;
    }


    private boolean saveServerAddrList2DB(String serverList) {
        DbQueryRunner queryRunner = DbQueryRunner.getInstance();
        String sql = "Replace INTO ServiceAddressList (ServerHost, ServiceAddressList) " +
                " values(?,?)";
        String[] params = new String[]{Constants.SERVER_HOST, serverList};
        queryRunner.update(sql, params);
        return true;
    }

    private boolean getServerAddrListFromDB() {
        try {
            String sql = "SELECT ServerHost, ServiceAddressList from ServiceAddressList Where ServerHost=?";
            String[] params = new String[]{Constants.SERVER_HOST};
            List<String> addressList = DbQueryRunner.getInstance().query(sql, new DbQueryRunner.RowHandler() {
                @Override
                public Object handle(Cursor c) {
                    String address = c.getString(c.getColumnIndex("ServiceAddressList"));
                    return address;
                }
            }, params);

            if (addressList.size() > 0) {
                JSONObject jsonObj = new JSONObject(addressList.get(0));
                //todo
//                AppContext.getAppContext().setServiceAddressJSON(jsonObj.optJSONObject("service_addr_list"));
//                AppContext.getAppContext().setPublicKey(jsonObj.optJSONObject("public_key"));
                return true;
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return false;
    }


    public void startFirstStep(Activity context, final Handler handler) {

        final long startTime = System.currentTimeMillis();

        AppContext appContext = AppContext.getAppContext();
        //获取系统参数
        getScreenInfo(context);
        DeviceInfo deviceInfo = getDeviceInfo(context);
        AppContext.getAppContext().setDeviceInfo(deviceInfo);

        Log.i("AppCommonService", " ImageLoad  init status:" + ImageLoader.getInstance().isInited());

        File cacheDir = StorageUtils.getOwnCacheDirectory(appContext, "UniversalImageLoader/Cache");
        // Get singletone instance of ImageLoader
        ImageLoader imageLoader = ImageLoader.getInstance();
        // Create configuration for ImageLoader (all options are optional, use only those you really want to customize)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(appContext)
                //.memoryCacheExtraOptions(480, 800) // max width, max height
                .threadPoolSize(4)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024)) // You can pass your own memory cache implementation
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        //todo
                        //.imageDownloader(ColudImageDownload.getImageDownload()) // connectTimeout (5 s), readTimeout (20 s)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        //.writeDebugLogs()
                .build();
        // Initialize ImageLoader with created configuration. Do it once on Application start.
        imageLoader.init(config);


        //生成aes对称秘钥
        //todo
//        Random random = new Random();
//        byte[] keybuf = new byte[16];
//        random.nextBytes(keybuf);
//        AppContext.getAppContext().setAESKey(keybuf);
//
//        //设置全局LCID
//        setLcid();
//        //加载地址列表
//        if (getServerAddrListFromDB()) {
//            Runnable asyncLoadServlist = new Runnable() {
//                @Override
//                public void run() {
//                    getRemoteServerAddressList();
//                    new SyncDataManager().syncData();
//                }
//            };
//            new Thread(asyncLoadServlist).start();
//            long diffTime = System.currentTimeMillis() - startTime;
//            long delayedTime = diffTime > 3000 ? 0 : 3000 - diffTime;
//            handler.sendEmptyMessageDelayed(0, delayedTime);
//        } else {
//            Runnable asyncLoadServlist = new Runnable() {
//                @Override
//                public void run() {
//                    getRemoteServerAddressList();
//                    new SyncDataManager().syncData();
//
//                    long diffTime = System.currentTimeMillis() - startTime;
//                    long delayedTime = diffTime > 3000 ? 0 : 3000 - diffTime;
//                    handler.sendEmptyMessageDelayed(0, delayedTime);
//                }
//            };
//            new Thread(asyncLoadServlist).start();
//        }
    }

    public void startDoctorSecondStep(Activity context) {
        //判断最后一次是否 登陆成功，如果是启动主doctor界面， 同时亦不启动自动认证
        String lastflag = SharedPreferencesUtils.getString(AppContext.getAppContext(), Constants.DOCTOR_LOGIN_INFO, "last_flag", "");
        if (lastflag != null && lastflag.length() > 0 && Integer.parseInt(lastflag) > 0) {
            // todo 加载医生信息
            //DoctorManager.getInstance().getDoctorFromPrefs(context);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            asyncAutoDoctorAuth();
        } else {
            //启动登陆界面
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        context.finish();
    }


    public void startPatientSecondStep(Activity context) {
        //判断最后一次是否 登陆成功，如果是启动主doctor界面， 同时亦不启动自动认证
//        String lastflag = SharedPreferencesUtils.getString(AppContext.getAppContext(), Constants.PATIENT_LOGIN_INFO, "last_flag", "");
//        if (lastflag != null && lastflag.length() > 0 && Integer.parseInt(lastflag) > 0) {
//            //加载医生信息
//            PatientManager.getInstance().getPatientFromPrefs(context);
//            Intent intent = new Intent(context, com.kanebay.lepu.askdr.patient.ui.MainTabActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            asyncAutoPatientAuth();
//        } else {
//            //启动登陆界面
//            Intent intent = new Intent(context, com.kanebay.lepu.askdr.patient.ui.login.LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//        context.finish();
    }

    private void setLcid() {
//        String lcid;
//        AppContext appContext = AppContext.getAppContext();
//        Locale locale = appContext.getResources().getConfiguration().locale;
//        String location = locale.getCountry();
//        if (location.equals("CN")) {
//            //lcid = new LcidManager().getLCID(Constants.Language_Code_CN);
//            AppContext.getAppContext().setLCID("2052");
//        } else {
//            //lcid = new LcidManager().getLCID(Constants.Language_Code_EN_US);
//            AppContext.getAppContext().setLCID("2052");//TODO  国外也临时设置成2052
//        }
    }

    private void asyncAutoDoctorAuth() {
        Runnable loginRun = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        User user = AppContext.getAppContext().getCurrentUser();
                        UserInfo userInfo = user.getUserInfo();
                        long userId = userInfo.getUserId();
                        String userToken = user.getUserToken();
                        String accessToken = user.getAccessToken();
                        int lastFlag = user.getLastFlag();
                        //todo
//                        int loginResult = LoginManager.getInstance().autoDoctorAuth(userId, userToken, accessToken);
                        int loginResult = Constants.SERVER_SUCCESS;
                        if (loginResult == Constants.SERVER_SUCCESS) {
                            break;
                        } else if (loginResult == Constants.Error.NETWORK_IS_UNREACHABLE) {
                            Thread.sleep(1000 * 5);
                            continue;
                        } else {
                            Handler handler = new Handler(AppContext.getAppContext().getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AppContext appContext = AppContext.getAppContext();
                                    Intent intent = new Intent(appContext, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //注意本行的FLAG设置
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.PARAM_TARGET_FRAGMENT, "com.kanebay.lepu.askdr.doctor.ui.login.controller.SignInFragment");
                                    intent.putExtras(bundle);
                                    appContext.startActivity(intent);
                                }
                            });
                            break;
                        }
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                        break;
                    }
                }
            }
        };
        new Thread(loginRun).start();
    }


    private void asyncAutoPatientAuth() {
        Runnable loginRun = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        User user = AppContext.getAppContext().getCurrentUser();
                        if (user == null) logger.error("user is null");
                        UserInfo userInfo = user.getUserInfo();
                        if (userInfo == null) logger.error("userInfo is null");
                        long userId = userInfo.getUserId();
                        String userToken = user.getUserToken();
                        String accessToken = user.getAccessToken();
                        int lastFlag = user.getLastFlag();
                        //todo
//                        int loginResult = LoginManager.getInstance().autoPatientAuth(userId, userToken, accessToken);
                        int loginResult = Constants.SERVER_SUCCESS;
                        if (loginResult == Constants.SERVER_SUCCESS) {
                            break;
                        } else if (loginResult == Constants.Error.NETWORK_IS_UNREACHABLE) {
                            Thread.sleep(1000 * 10);
                            continue;
                        } else {
                            Handler handler = new Handler(AppContext.getAppContext().getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AppContext appContext = AppContext.getAppContext();
                                    Intent intent = new Intent(appContext, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //注意本行的FLAG设置
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.PARAM_TARGET_FRAGMENT, "com.kanebay.lepu.askdr.patient.ui.login.controller.SignInFragment");
                                    intent.putExtras(bundle);
                                    appContext.startActivity(intent);
                                }
                            });
                            break;
                        }
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                        break;
                    }
                }
            }
        };
        new Thread(loginRun).start();
    }

    public void changeLanguage(FragmentActivity targetAct, Configuration config) {
        /*AppContext appContext = AppContext.getAppContext();
        DisplayMetrics dm = appContext.getBaseContext().getResources().getDisplayMetrics();
        appContext.getResources().updateConfiguration(config, dm);
        new AppConfigManager().saveLanguage(appContext,config.locale.getLanguage());
        //SharedPreferencesUtils.saveString(appContext, "language", config.locale.getLanguage());
        //targetAct.finish();
        Intent intent = new Intent(appContext, MainTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appContext.startActivity(intent);*/
    }

    public void getScreenInfo(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5/2.0）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240/320）
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        int screenHeight = (int) (height / density);//屏幕高度(dp)
//        AppContext.getAppContext().setDpHeight(screenHeight);
//        AppContext.getAppContext().setDpWidth(screenWidth);
//        if (BuildConfig.DEBUG) {
//            Toast.makeText(activity, " width: " + width + " height: " + height
//                    + " density: " + density
//                    + " densitydpi " + densityDpi + "screenWidth: "
//                    + screenWidth + " screenHeight: " + screenHeight, Toast.LENGTH_LONG).show();
//        }
        logger.debug("pixel", "width:" + width + "height:" + height + " density:" + density + " densitydpi" + densityDpi);
    }

    public DeviceInfo getDeviceInfo(Activity activity) {

        DeviceInfo deviceInfo = new DeviceInfo();
        TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        String deviceId = tm.getDeviceId();
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = "null";
        }
        deviceInfo.setDeviceId(deviceId);
        String osVersion = Build.VERSION.RELEASE;
        if (osVersion == null || osVersion.isEmpty()) {
            osVersion = "null";
        }
        deviceInfo.setOsVersion(osVersion);
        deviceInfo.setResolution(dm.widthPixels + "*" + dm.heightPixels);
        deviceInfo.setDeviceType("Android");
        String networkOperator = tm.getNetworkOperator();
        if (networkOperator == null || networkOperator.isEmpty()) {
            networkOperator = "null";
        }
        deviceInfo.setNetworkOperator(networkOperator);
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            deviceInfo.setClientVersion(pInfo.versionName);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            deviceInfo.setClientVersion("null");
            return deviceInfo;
        }
        return deviceInfo;
    }

    public void setupChatService() {
        ChatManager.getInstance().login(getXmppHost(), getXmppPort(),
                AppContext.getAppContext().getCurrentUser().getUserInfo().getUserId(),
                AppContext.getAppContext().getCurrentUser().getAccessToken());
    }

    /**
     * add by 黄亮* 登陆成功*
     */
    public void doctorLoginSuccess(Activity loginActContext) {

        AppContext appContext = AppContext.getAppContext();
        Toast.makeText(loginActContext, "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);

        loginActContext.finish();
    }

//    public void patientLoginSuccess(Activity loginActContext) {
//
//        AppContext appContext = AppContext.getAppContext();
//        loginActContext.finish();
//        Toast.makeText(loginActContext, appContext.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(appContext, MainTabActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        appContext.startActivity(intent);
//
//    }

    /**
     * add by 黄亮  *错误处理*
     */
    public void handleError(Context actContext, int code) {
        AppContext appContext = AppContext.getAppContext();
        //todo
//        if (code == 20) {
//            Toast.makeText(actContext, appContext.getString(R.string.phone_has_register), Toast.LENGTH_SHORT).show();
//        } else if (code == 12) {
//            Toast.makeText(actContext, appContext.getString(R.string.patient_not_register), Toast.LENGTH_SHORT).show();
//        } else if (code == 316) {
//            Toast.makeText(actContext, appContext.getString(R.string.patient_has_invite), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(actContext, appContext.getString(R.string.network_unreached), Toast.LENGTH_SHORT).show();
//        }
    }

    public String getAppRootStorage() {
        return Environment.getExternalStorageDirectory() + File.separator + Constants.APP_DIR + File.separator;
    }

    //Code for archive only
    private void checkAppState() {
//        AsyncTask<Void, Void, Boolean> processInfoQueryTask = new AsyncTask<Void, Void, Boolean>() {
//            @Override
//            protected Boolean doInBackground(Void... params) {
//                logger.debug("onTextMessage: query start at " + new Date());
//                ActivityManager activityManager = (ActivityManager) AppContext.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
//
//                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//                activityManager.getMemoryInfo(mi);
//
//                List<ActivityManager.RunningAppProcessInfo> pids = activityManager.getRunningAppProcesses();
//                int processid = 0;
//                for (ActivityManager.RunningAppProcessInfo info : pids) {
//                    if (info.processName.equalsIgnoreCase("com.kanebay.lepu.askdr.doctor")) {
//
//                        switch (info.importance) {
//                            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
//                                logger.debug("onTextMessage: Process is IMPORTANCE_BACKGROUND");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
//                                logger.debug("onTextMessage: Process is IMPORTANCE_FOREGROUND");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE:
//                                logger.debug("onTextMessage: Process is IMPORTANCE_VISIBLE");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE:
//                                logger.debug("onTextMessage: Process is IMPORTANCE_SERVICE");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE:
//                                logger.debug("onTextMessage: Process is IMPORTANCE_PERCEPTIBLE  ");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY:
//                                logger.debug("onTextMessage: Process is IMPORTANCE_EMPTY");
//                                break;
//                            default:
//                                logger.debug("onTextMessage: Process should not happend like this");
//                                break;
//                        }
//                        if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                            logger.debug("onTextMessage: AppContext is in Foreground!!!");
//                        } else {
//                            logger.debug("onTextMessage: AppContext is NOT in Foreground.");
//                        }
//
//                        switch (info.importanceReasonCode) {
//                            case ActivityManager.RunningAppProcessInfo.REASON_PROVIDER_IN_USE:
//                                logger.debug("onTextMessage: ProcessReason is REASON_PROVIDER_IN_USE ");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.REASON_SERVICE_IN_USE:
//                                logger.debug("onTextMessage: ProcessReason is REASON_SERVICE_IN_USE ");
//                                break;
//                            case ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN:
//                                logger.debug("onTextMessage: ProcessReason is REASON_UNKNOWN ");
//                                break;
//                            default:
//                                logger.debug("onTextMessage: ProcessReason should not happend like this");
//                                break;
//                        }
//                        processid = info.pid;
//                        break;
//                    }
//                }
//
////                if (processid>0){
////                    Debug.MemoryInfo[] ms = activityManager.getProcessMemoryInfo(new int[]{processid});
////                }
////
//                logger.debug("onTextMessage: query end at " + new Date());
//                return true;
//            }
//        };
//
//
//        processInfoQueryTask.execute();
    }

    private static ProgressDialog submitProgress;

    public void startSubmitWaitting() {
        //todo
        //submitProgress = ProgressDialog.show(AppContext.getAppContext().getCurrentActivity(), null, "正在加载数据,请稍候...");
    }

    public void stopWaittingAnimating() {
        submitProgress.dismiss();
    }


    public void logoutXGXMPP() {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    //信鸽
                    ChatManager.getInstance().logout();
                } catch (Exception ex) {
                    Log.e("AppCommonService", "XMPP", ex);
                }
                try {
                    //xmpp
                    //todo 换成百度
                    //XGPushManager.unregisterPush(AppContext.getAppContext());
                } catch (Exception ex) {
                    Log.e("AppCommonService", "logoutXG", ex);
                }
            }
        };
        new Thread(run).start();
    }

}
