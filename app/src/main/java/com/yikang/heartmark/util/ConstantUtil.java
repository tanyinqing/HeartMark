package com.yikang.heartmark.util;

import android.content.Context;

public class ConstantUtil {
	
	//用户
	public static final String LOGIN = "login";
	public static final String USER_NAME = "user_name";
	public static final String USER_UID = "user_uid";
	public static final String USER_NAME_info = "user_nameinfo";
	public static final String USER_SEX = "user_sex"; // 性别
	public static final String USER_AGE = "user_age"; // 年龄
	public static final String USER_WEIGHT = "user_weight";// 体重
	public static final String USER_HISTORY = "user_history";// 病史
	public static final String USER_NUMBER = "user_number";// 医号
	public static final String USER_NUMBER_CODE = "user_number_code";// 医号
	public static final String USER_TOKEN = "user_token";// tokenId 用于系统识别是否登录
	

	public static final String JPUSH_ID = "jpush_id";// 极光推送id
	
	public static final String VOICER = "voicer";// 讯飞语音发音人

	public static final String Evaluate = "evaluate";// 评估标志
	
	public static final String ALARM_CELING = "alarm_celing";// 测量是否提醒
	public static final String ALARM_YAO = "alarm_yao";// 用药是否提醒
	public static final String ALARM_HULI = "alarm_huli";// 护理是否提醒
	
	public static final String CELING_FROM = "celingFrom";// 测量界面from key
	public static final String CELING_DESTORY = "celingDestory";// 测量界面destory key
	public static final String CELING_START = "celingStart";// 测量界面是否开始测量  key
	public static final String CELING_DEVICE = "celingDevice";// 测量界面是否开始测量  key
	
	public static final String FRIEND_CHECK = "friendCheck";// 测量界面是否开始测量  key
	
	public static final String SHOW_UPDATE = "showUpdate"; //测量完成返回首页不再弹出更新框标志
	
	public static final int DIALOG_SHOW = 1000;
	public static final int DIALOG_HINT = 1001;
	
	//代表数据的的数据是否与网络中的数据同步
	public final static int SYNC_YES =1;
	public final static int SYNC_NO =0;
	
	public static String getName(Context context){
		return SharedPreferenceUtil.getString(context, USER_NAME);
	}
	public static String getUid(Context context){
		return SharedPreferenceUtil.getString(context, USER_UID);
	}
}
