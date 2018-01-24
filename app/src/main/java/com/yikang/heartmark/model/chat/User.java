package com.yikang.heartmark.model.chat;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guolchen on 2014/12/16.
 */
public class User implements Serializable {
    public static final String USER_ID = "user_id";
    public static final String USER_TOKEN = "user_token";
    public static final String ACCESS_TOKEN = "access_token";
    //public static final String USER_NAME = "user_name";
    public static final String NICK_NAME = "nick_name";//昵称
    public static final String GENDER = "gender";//性别
    public static final String BIRTHDAY = "birthday";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String LOCATION_CODE = "location_code";
    public static final String LAST_FLAG = "last_flag";
    public static final String PROFILE_PICTURE_THUMBNAIL_ID = "profile_picture_thumbnail_id";
    public static final String PROFILE_PICTURE_ORIGINAL_ID = "profile_picture_original_id";
    public static final String LAST_LOGIN_TIME = "last_login_time";
    public static final String IS_LOGIN = "isLogin";
    public static final String PROFILE_PIC_ID = "profile_pic_id";
    public static final String ABOUT_ME = "about_me";
    public static final String BACKGROUND_ID = "background_id";
    public static final String ALPHA = "alpha";
    public static final String HOSPITAL_ID = "hospital_id";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String HOSPITAL_NAME = "hospital_name";
    public static final String DEPARTMENT_NAME = "department_name";


    //    private long userId;        //用户id
//    private String userName;    //用户名
//    private long mobileNo;    //手机号
    private String birthday = "";    //出生年月日  todo 数据库中为Date
    private String email = "";       //邮箱
    private String profilePicOriginalId = "";//用户原始图像
    private String regTime = "";//timestamp
    private String lastLoginTime = "";//timestamp
    private String userToken = "";
    private String accessToken = "";
    private String pushToken = "";
    private String deviceId = "";
    private String deviceType = "";
    private String resolution = "";
    private long referenceId = 0;//
    //    private String locationCode;//用户城市编码
    private String location = "";//
    private String Lcid = "";
    private String os = "";//
    private String clientVersion = "";//
    private String networkOperator = "";//
    //    private int gender;//性别 1男 2女 默认0 //todo
    private String password = "";
    private String hashSalt = "";
    private String lastAuthTime = "";//timestamp
    private int banFlag = 0;//tinyint
    private int grade = 0;
    private String backgroundImageId = "";
    private String aboutMe = "";
    private int userType = 0;//tinyint
    //    private String nickName;
    private String region = "";
    //    private String alpha;           //名字首字母
    private int lastFlag = 0;//1 匿名注册, 2 匿名登陆  3正式手机注册  4 手机注册自动登陆 5 用户名密码登陆 6退出登陆
    private List<Tag> tags = null;//医生给自己打的标签,使用字符串数组//todo pushtoken, aleph, profilePicOriginalId

    private UserInfo userInfo = new UserInfo();

    public User() {
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLcid() {
        return Lcid;
    }

    public void setLcid(String lcid) {
        Lcid = lcid;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashSalt() {
        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }

    public String getLastAuthTime() {
        return lastAuthTime;
    }

    public void setLastAuthTime(String lastAuthTime) {
        this.lastAuthTime = lastAuthTime;
    }

    public int getBanFlag() {
        return banFlag;
    }

    public void setBanFlag(int banFlag) {
        this.banFlag = banFlag;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getBackgroundImageId() {
        return backgroundImageId;
    }

    public void setBackgroundImageId(String backgroundImageId) {
        this.backgroundImageId = backgroundImageId;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getLastFlag() {
        return lastFlag;
    }

    public void setLastFlag(int lastFlag) {
        this.lastFlag = lastFlag;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getProfilePicOriginalId() {
        return profilePicOriginalId;
    }

    public void setProfilePicOriginalId(String profilePicOriginalId) {
        this.profilePicOriginalId = profilePicOriginalId;
    }

}
