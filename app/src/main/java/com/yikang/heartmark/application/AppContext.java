package com.yikang.heartmark.application;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import com.iflytek.cloud.SpeechUtility;
import com.yikang.heartmark.model.chat.DeviceInfo;
import com.yikang.heartmark.model.chat.User;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class AppContext extends Application {

    public static AppContext context;  //全局的上下文对象
    private User currentUser;//当前登录用户信息
    private DeviceInfo deviceInfo;
    private Handler chatMessageHandler;
    private Handler chatSessionHandler;
    private boolean showingLogoutUI;

    //todo
    public String getDownloadUrl(String id) {
        return "";
    }

    public String getServiceAddress(String serviceName) {
//        String strTemp;
        //todo
//        try {
//            if (serviceAddressJSON == null) {
//                logger.warn("serviceAddressJSON is null");
//                return null;
//            }
//            strTemp = this.serviceAddressJSON.optString(serviceName);
//        } catch (Exception e) {
//            return null;
//        }
        return serviceName;
    }

    public void setShowingLogoutUI(boolean showingLogoutUI) {
        this.showingLogoutUI = showingLogoutUI;
    }

    public boolean isShowingLogoutUI() {
        return showingLogoutUI;
    }

    public void setChatSessionHandler(Handler chatSessionHandler) {
        this.chatSessionHandler = chatSessionHandler;
    }

    public void setChatMessageHandler(Handler chatMessageHandler) {
        this.chatMessageHandler = chatMessageHandler;
    }

    public Handler getChatMessageHandler() {
        return chatMessageHandler;
    }

    public Handler getChatSessionHandler() {
        return chatSessionHandler;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void onCreate() {
        // 讯飞语音
        SpeechUtility.createUtility(AppContext.this, "appid=549920ec");
        super.onCreate();
        context = this;
        // 初始化极光推送  todo UnsatisfiedLinkError 会出现错误，暂时禁用等待解决
        /*JPushInterface.setDebugMode(true);
        JPushInterface.init(this);*/
    }

    // 运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    
    // 为了实现每次使用该类时不创建新的对象而创建的静态对象    单例模式
    private static AppContext instance;

    // 构造方法
    public AppContext() {
    }

    // 实例化一次
    public synchronized static AppContext getAppContext() {
//        if (null == instance) {
//            instance = new AppContext();
//        }
//        return instance;
        return context;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // 关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    // 杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
