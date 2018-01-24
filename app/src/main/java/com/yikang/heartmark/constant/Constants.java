/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.yikang.heartmark.constant;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class Constants {

    public static final int ACCESS_TOKEN_KICKED = 5;
    public static final int PTOD = 1;
    public static final int DTOP = 2;
    public static final String RATTING = "800";
    public static final int UNFRIENDYOU = 2; //解除关系

    public static class AppTypes {
        public static final int DOCTOR = 1;
        public static final int PATIENT = 2;
    }

    public static final int VIP_LEVEL_DEEP = 1;    //	1深度咨询 2家庭医生
    public static final int VIP_LEVEL_HOME = 2;

    public static final String APP_CONFIG = "askdr_config";
    public static final String DOCTOR_LOGIN_INFO = "doctor_login_config";
    public static final String PATIENT_LOGIN_INFO = "patient_login_config";

    /* Sever Address*/
//    public static final String SERVER_HOST = "192.168.0.23:8080";
      public static final String SERVER_HOST = "192.168.0.108:8111";
//    public static final String SERVER_HOST = "124.193.185.42:8080";

    public static final String SERVICE_ADDRESS_URL = "http://" + SERVER_HOST + "/health-regauth/v1/serveraddress/list";//公司外网测试ip

    public static final String APP_DIR = "askdr";

    public static final String SERVICE_ADDRESS_NAME = "health_service";
    public static final String REG_AUTH_ADDRESS_NAME = "health_regauth";
    public static final String DOWNLOAD_ADDRESS_NAME = "object_download";
    public static final String UPLOAD_ADDRESS_NAME = "object_upload";
    public static final String CHAT_ADDRESS_NAME = "chat_server";

    public static final String USER_INFO = "user_info";
    public static final String DEPARTID = "DEPARTID";
    public static final String DEPARTNAME = "DEPARTNAME";
    public static final String HOSPITALID = "HOSPITALID";
    public static final String HOSPITALNAME = "HOSPITALNAME";

    public static final String PATIENT = "patient";
    public static final String Icon_Map = "Icon_Map";
    public static final String RECOMMEND = "RECOMMEND";
    public static final String SELECT_PATIENT = "SELECT_PATIENT";
    public static final String SELECT_TAG = "SELECT_TAG";
    public static final String IS_FROM_EDIT_TAG_GROUP = "IS_FROM_EDIT_TAG_GROUP";
    public static final String IS_TO_ADD_NEW_TAG = "IS_TO_ADD_NEW_TAG";
    public static final String IS_FROM_PATIENT_AND_TAG_SELECT = "IS_FROM_PATIENT_AND_TAG_SELECT";
    public static final String IS_FROM_CHATSESSION = "IS_FROM_CHATSESSION";
    public static final String IS_FROM_CHAT = "IS_FROM_CHAT";

    public static final String IS_FROM_DOCTOR = "IS_FROM_DOCTOR";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";
    public static final String ADD = "ADD";
    public static final String ACTION = "ACTION";
    public static final String SUCCESS = "SUCCESS";


    public static final String MONEY = "MONEY";
    public static final String BANK_NAME = "bank_name";
    public static final String BANK_ACCOUNT_NUM = "bank_account_number";
    public static final String NAME_ON_CARD = "name_on_card";

    public static final String PHONE = "PHONE";
    public static final String CONTENT = "CONTENT";
    public static final String FROM_PROFILE_TO_RECOM = "FROM_PROFILE_TO_RECOM";

    public static final String TAG = "TAG";
    public static final String IS_NEW_ADD = "IS_NEW_ADD";
    public static final String PROFILE_PIC_ID = "PROFILE_PIC_ID";

    public static final String VIPBANK = "VIPBANK";
    public static final String DOCTOR = "doctor";
    public static final String PROVINCELIST = "PROVINCELIST";

    public static final String STORAGE_UPLOAD = "upload";
    public static final String STORAGE_DOWNLOAD = "download";
    public static final String REGISTER = "register";

    public static final String IMAGE_BUCKET_PARAM_NAME = "imageBucket";

    public static final String PHOTO_SELECTED_MAX_ALLOW_SELECT_NUMBER = "max_allow_select_Number";
    public static final String PHOTO_SELECTED_SELECTED_IMAGES = "Selected_Image_List";
    public static final String PHOTO_SELECTED_ALL_IMAGES = "all_images";
    public static final String PHOTO_SELECTED_CURRENT_POSITION = "currentPosition";

    public static final String FILTER_ACTION_TO_CHAT_SESSIION = "action_to_chatsession";


    public static final String LANGUAGE = "zh";

    public static final String GENDER_CODE_MALE = "1";
    public static final String GENDER_CODE_FEMALE = "2";

    public static final int SYNC_TIMEOUT = 60 * 1000;
    public static final int REQUEST_TIMEOUT = 60 * 1000;

    public static final int GET_LIST = 332;//获取新列表

    /* Server return value*/

    //TODO Chang 10-05 没有一个地方检查 SERVER ERROR？
    public static final int SERVER_ERROR = 0;

    public static final int LOCAL_ERROR = -1;
    public static final int SERVER_SUCCESS = 1;
    public static final int RELEASE_SHIP = 119;
    public static final int SERVER_WRONG_PWD_OR_ACCOUNT = 2;
    public static final int SERVER_CONNECT_SUCCESS = 200;
    public static final int SERVER_ACCOUNT_REGED = 20;
    public static final int SERVER_ACCOUNT_NO_REGED = 21;
    public static final int SERVER_PWD_WRONG = 22;
    public static final int SERVER_NICKNAME_UESED = 157;
    public static final int POLLINFO_DELTTED = 307;
    public static final int SERVER_EXPIRED = 163;
    public static final int CONNECT_FAILED = -2;


    public static final int TAG_EXIST = 317;//已存在标签
    public static final int OTHER_VERSION_ACCOUNT = 159;//已存在标签

    /* End Server return value*/

    /*field type */

    public static final String DEVICE_ID = "device_id";
    public static final String OS_VERSION = "os_version";
    public static final String RESOLUTION = "resolution";
    public static final String DEVICE_TYPE = "device_type";
    public static final String CLIENT_VERSION = "client_version";
    public static final String NETWORK_OPERATOR = "network_operator";
    public static final String LCID = "lcid";

    public static final int SERVER_ADDRESS_LIST_EMPTY = -103;

    public static final String PARAM_TARGET_FRAGMENT = "__param_target_fragment";
    public static final String PARAM_BUNDLE_PARAM = "__param_bundle_param";

    public static final int RELATION_IS_NOT_EXISTED = 109;

    public static final String DOCTOR_VERSION_PACKAGE_NAME = "com.kanebay.lepu.askdr.doctor";
    public static final int XG_DOCTOR_ACCESS_ID = 2100079608;
    public static final String XG_DOCTOR_ACCESS_KEY = "A7B5VS1XB85W";
    public static final String XG_DOCTOR_SECRET_KEY = "412cc5df121a18d8e6c512b48c2a446d";

    public static final String PATIENT_VERSION_PACKAGE_NAME = "com.kanebay.lepu.askdr.patient";
    public static final int XG_PATIENT_ACCESS_ID = 2100080061;
    public static final String XG_PATIENT_ACCESS_KEY = "A45L1C8CQA2X";
    public static final String XG_PATIENT_SECRET_KEY = "f88302f2c7609c5fd0a84c74521b57c0";

    public static final String XG_NOTIFICATION_DATA = "notification_data";
    public static final String XG_NOTIFICATION_UI_SHOW = "com.kanebay.lepu.askdr.notification.SHOW";

    //All Error Constants go here
    public static class Error {
        public static final int NETWORK_IS_UNREACHABLE = 10001;//网络不可用

        public static final int XMPP_CONNECTION_ERROR = 1001;
        public static final int XMPP_LOGIN_ERROR = 1002;
        public static final int MEDIA_UPLOAD_ERROR = 1101;
        public static final int MEDIA_DOWNLOAD_ERROR = 1102;

        //http response error
        public static final int ACCOUNT_HAS_REGISTER = 20;//手机已注册
        public static final int ACCOUNT_NOT_REGISTER = 21;//手机未注册
        public static final int ACCOUNT_TYPE_ERROR = 159;//注册账号类型不正确(手机号码)

        public static final int ACCOUNT_OR_PASSWORD_ERROR = 22;//帐号或密码不正确
        public static final int PASSWORD_INVVALID = 155;//密码非法字符
        public static final int PASSWORD_OVER_LENGTH = 156;//密码长度太长
        public static final int ACCOUNT_INVALID = 24;//手机号非法
        public static final int TOKEN_TIMEOUT = 4;//token超时
        public static final int ACCOUNT_BEEN_KICKED = 5;//被人踢
        public static final int ORGINAL_PASSWORD_ERROR = 22;//原密码错误
        public static final int USER_DONT_EXIST = 158;//用户不存在
        public static final int USER_BEEN_FORBIDDEN = 167;//用户被禁
        public static final int NEW_PASSWORD_INVALID = 155;//新密码非法字符
        public static final int NEW_PASSWORD_OVER_LENGTH = 156;//新密码长度太长
        //        public static final int LOGIN_FORBIDDEN_ = 167;//禁止登录，自动认证服务的错误码，不需要处理
//        public static final int TOKEN_INVALID = 116;//无效的认证，自动认证服务的错误码
        public static final int RELATION_DONT_EXIST = 119;//关系不存在,业务层已经处理
        public static final int PATIENT_UNREGISTER = 12;

        public static final int HAS_INVITED = 316;  // 已经邀请过了

        public static final int TAG_HAS_EXIST = 317;

        public static final int HAS_RECOMMENDATE = 318; //已推荐过该病人
        public static final int NO_RECOMMENDATE_RELATION = 319; //  319推荐人(doctor_id)和被推荐人(target_patient_id)不存在医患关系

        public static final int APP_TYPE_ERROR = 320;  //对方号版本错误
    }

    public static class Chat {

        public static final int CHAT_LOGIN_STATE_NEW = 0;
        public static final int CHAT_LOGIN_STATE_STARTED = 1;
        public static final int CHAT_LOGIN_STATE_SUCCESS = 2;
        public static final int CHAT_LOGIN_STATE_ERROR = 3;

        public static final int CHAT_NEW_MESSAGE = 100;
        public static final int CHAT_OFFLINE_MESSAGE = 101;
        public static final int CHAT_MESSAGE_SENT = 102;
        public static final int CHAT_MESSAGE_RESENT = 103;
        public static final int CHAT_MESSAGE_RECEIVED = 104;
        public static final int CHAT_MESSAGE_CHANGED = 105;
        public static final int CHAT_MESSAGE_REPLACE = 106;

        public static final int CHAT_MESSAGE_DOWNLOAD_MEDIA = 300;

        public static final int CHAT_DIRECTION_SEND = 1;
        public static final int CHAT_DIRECTION_RECEIVE = 2;

        public static final int CHAT_MESSAGE_TYPE_TEXT = 1;
        public static final int CHAT_MESSAGE_TYPE_IMAGE = 2;
        public static final int CHAT_MESSAGE_TYPE_AUDIO = 3;
        public static final int CHAT_MESSAGE_TYPE_VIDEO = 4;

        public static final int CHAT_FILE_UPLOAD = 1;
        public static final int CHAT_FILE_DOWNLOAD = 2;

        public static final String CHAT_SESSION = "chat_session";
        public static final String CHAT_MESSAGE = "chat_message";
        public static final String CHAT_MEDIA = "media";
        public static final String CHAT_BODY = "body";
        public static final String CHAT_FROM_SERVER = "server";
        public static final long CHAT_FROM_SERVER_ID = -9999;

        public static final String CHAT_LAST_MSG_IMAGE_TEXT = "[图片]";
        public static final String CHAT_LAST_MSG_AUDIO_TEXT = "[语音]";
        public static final String CHAT_LAST_MSG_VIDEO_TEXT = "[视频]";

        public static final int CHAT_UPLOAD_TIMEOUT = 120 * 1000;

        public static final int CHAT_SEND_STATUS_NA = 0;
        public static final int CHAT_SEND_STATUS_SENT = 1;
        public static final int CHAT_SEND_STATUS_RECEIVED = 2;
        public static final int CHAT_SEND_STATUS_OFFLINE = 3;
        public static final int CHAT_SEND_STATUS_LOST = 4;
        public static final int CHAT_SEND_STATUS_BLOCKED = 5;
        public static final int CHAT_SEND_STATUS_UPLOAD_FAILED = 6;

        public static final int CHAT_MESSAGE_CODE_OFFLINE = 1;
        public static final int CHAT_MESSAGE_CODE_NOFOUND = 2;
        public static final int CHAT_MESSAGE_CODE_BLOCKED = 3;

        public static final String CHAT_IMAGE_THUMBNAIL = "thumbnailImageId";
        public static final String CHAT_IMAGE_OBJECT_ID = "objectId";
        public static final String CHAT_IMAGE_ATTACHFILE = "attachFile";
        public static final int CHAT_IMAGE_STYLE_LARGE = 1; //"large";
        public static final int CHAT_IMAGE_STYLE_RAW = 2; //"raw";

        public static final String CHAT_MIME_TYPE_IMAGE = "image/jpeg";
        public static final String CHAT_MIME_TYPE_AMR = "audio/amr";
        public static final String CHAT_MIME_TYPE_MP3 = "audio/mp3";
        public static final String CHAT_MIME_TYPE_MP4 = "video/mp4";

    }

    public static class Http {

        public static final String HTTP_MESSAGE_TYPE_TEXT = "text";
        public static final String HTTP_MESSAGE_TYPE_IMAGE = "image";
        public static final String HTTP_MESSAGE_TYPE_AUDIO = "audio";
        public static final String HTTP_MESSAGE_TYPE_VIDEO = "video";

        public static final String HTTP_IMAGE_STYLE_LARGE = "large"; //1
        public static final String HTTP_IMAGE_STYLE_RAW = "raw"; //2

        //Download image type
        public static final int HTTP_DOWNLOAD_IMAGE_NO = 0;
        public static final int HTTP_DOWNLOAD_IMAGE_THUMBNAIL = 1;
        public static final int HTTP_DOWNLOAD_IMAGE_COMPRESSED = 2;
        public static final int HTTP_DOWNLOAD_IMAGE_ORGINAL = 3;

    }

    public static class HttpResponse {
        public static final int GET_ADDRESSLIST = 101;
        public static final int GET_CHECKCODE = 102;
        public static final int UPLOAD_ONE = 105;
        public static final int GET_PROVINCES = 106;
        public static final int GET_HOSPITALS = 107;
        public static final int GET_DEPARTMENTS = 108;

        public static final int BATCH_UPDATE_TAG = 121;

        public static class Doctor {

            //普通 1深度咨询 2家庭医生
            public static final int LEVEL_ONE = 0;
            public static final int LEVEL_TWO = 1;
            public static final int LEVEL_THREE = 2;
            public static final int CHECK_REGISTERED = 201;
            public static final int REGISTER = 202;
            public static final int LOGIN = 203;
            public static final int RENEW_PASSWORD = 204;
            public static final int CHANGE_PASSWORD = 205;
            public static final int CHANGE_ACCOUNT = 206;
            public static final int AUTOAUTH = 207;
            public static final int LOGOUT = 208;

            public static final int GET_PATIENTLIST = 221;
            public static final int GET_INFO = 222;//获取医生详细信息
            public static final int GET_PATIENTINFO = 223;//医生获取患者详细信息
            public static final int RELEASE = 224;//医生解除医患关系
            public static final int UPDATE_INFO = 225;//更新医生信息

            public static final int ENABLE_CALLME = 231;
            public static final int ADD_NOTE = 232;//添加笔记
            public static final int GET_NOTELIST = 233;
            public static final int UPDATE_NOTE = 234;
            public static final int DELETE_NOTE = 235;
            public static final int ADD_NOTICE = 236;//添加公告
            public static final int GET_NOTICELIST = 237;
            public static final int INVITE_PATIENT = 238;
            public static final int GET_INVITELIST = 239;

            public static final int TAG_SELF = 251;
            public static final int TAG_PATIENT = 252;
            public static final int GET_PATIENT_TAGS = 253;
            public static final int DELETE_PATIENT_TAG = 254;
            public static final int UPDATE_TAG_TEXT = 255;

            public static final int ADD_RECOMMENDATION = 261;
            public static final int COMMON = 266; //todo
        }

        public static class Patient {


            public static final int CHECK_REGISTERED = 301;
            public static final int REGISTER = 302;
            public static final int LOGIN = 303;
            public static final int RENEW_PASSWORD = 304;
            public static final int CHANGE_PASSWORD = 305;
            public static final int CHANGE_ACCOUNT = 306;
            public static final int AUTOAUTH = 307;
            public static final int LOGOUT = 308;

            public static final int APP_TYPE_ERROR = 320;  //这是医生号码
            public static final int GET_DOCTORLIST = 321;
            public static final int GET_INFO = 322;//获取患者详细信息
            public static final int GET_DOCTORINFO = 323;//患者获取医生详细信息
            public static final int RELEASE = 324;//患者解除医患关系
            public static final int UPDATE_INFO = 325;//更新患者信息

            public static final int GET_NOTICELIST = 331;//获取公告列表

        }
    }

    public static class Bank {
        public static final int NO_BANK_BOUND = 321;// 321未绑定银行卡信息
        public static final int NO_ENOUGH_MONEY = 322;  // 322没有足够的资金
        public static final int ACCOUNT_NUM_ERROR = 324;  // 15-19 卡号
    }

    //退款
    public static class CashBack {
        public static final int NO_BUY_SERVICE = 323;  // 未购买该医生的套餐服务
        public static final int BANKCARD_ERROR = 324;  //银行卡信息错误
        public static final int PASSWORDERROR = 22;  // 密码错误
        public static final int BACKCASH = 325;//退款金额不正确
    }


    public static final int PATIENT_TO_DOCTOR = 1;// 1病人打给医生 2医生打给病人
    public static final int DOCTOR_TO_PATIENT = 2;

    public static class Call {
        public static final int NOT_EXIST = 700;//    code 700拨打的号码(询医中不存在)
        public static final int NO_TIME = 701;//  code 701剩余分钟数不够
        public static final int TYPE_ERROR = 702;//  code 702 calltype 类型错误
    }

    public static class Note {
        public static final int TYPE_TEXT = 1;//文字笔记
        public static final int TYPE_CHAT = 2;//聊天笔记
        public static final String NOTE = "note";
    }

    public static class Relation {
        public static final int CREATE_RELATION = 1;//服务器返回关系改变的type
        public static final int RELEASE_RELATION = 2;//服务器返回关系改变的type

        public static final String RELATION_CODE_NORMAL_TEXT = "1";//正常关系
        public static final String RELATION_CODE_RELEASE_TEXT = "2";//主动解除关系
        public static final String RELATION_CODE_RELEASED_TEXT = "3";//被动解除关系
        public static final int RELATION_CODE_NORMAL = 1;//正常关系
        public static final int RELATION_CODE_RELEASE = 2;//主动解除关系
        public static final int RELATION_CODE_RELEASED = 3;//被动解除关系

        public static final String RELATION = "relation";
        public static final int MONEY_NOT_ENOUGH = 322;//余额不足抵扣月费

    }

    public static class Notice {
        public static String GET_NEW_NOTICES_LIST_SUCCESS = "get_new_notices_success";
        public static String NEW_NOTICES_CURSOR = "NEW_NOTICES_CURSOR";
        public static String ORIGION_CURSOR = "ORIGION_CURSOR";
    }

    public static class Counts {
        public static String NEW_NOTICE_COUNT = "new_notice_count";
        public static String NEW_RELATION_COUNT = "new_relation_count";
        public static String NEW_INVITED_PATIENT_COUNT = "new_invited_patient_count";
        public static String NOTICE = "notice";

        //医生版计数消息
        public static final int MSG_INIT_NEW_INVITE_COUNT_DOCTOR = 601;//新邀请计数清零，通知PatientList小红点消失
        public static final int MSG_NEW_RELATION_DOCTOR = 602;
        public static final int MSG_NEW_INVITED_DOCTOR = 603;
        public static final int MSG_NEW_NOTICE_DOCTOR = 604;//医生发布公告后，刷新sessionFragment中的公告最后一条内容
        public static final int MSG_NEW_VIP_PATIENT = 606;

        public static final int MSG_NEW_PATIENT = 605; //新接受的病人
        public static String NEW_PATIENT_FLAG = "%$#@*!#$%^";

        //患者版计数消息
        public static final int MSG_NEW_NOTICE = 650;
        public static final int MSG_NEW_NOTICE_PATIENT = 651;
        public static final int MSG_NEW_RELATION_PATIENT = 652;
        public static final int MSG_INIT_NOTICE_COUNT_PATIENT = 653;////新公告计数清零，通知SessionFragment公告小红点消失


        public static String INVITED_TIME = "invited_time";
        public static String CREATED_TIME = "created_time";
        public static String TXT_COUNT_TIP_SHOW = "TXT_COUNT_TIP_SHOW";
        public static String TXT_COUNT_TIP_HIDE = "TXT_COUNT_TIP_HIDE";
        public static final int TXT_COUNT_TIP = 2;
    }

    public static class Invite {
        public static final int INVITE_UNCONFIRM = 1;//邀请未确认
        public static final int INVITE_CONFIRM = 2;//邀请已确认
        public static final int NOT_INVITED = 0;
        public static final int INVITED = 1;
        public static final int HAVE_BEEN_INVITED = 2;

        public static String GET_NEW_INVITE_LIST_SUCCESS = "get_new_invited_success";
    }

    public static class Recommendation {
        public static final int HAS_RECOMMENED = 1;//邀请未确认
        public static final int NOT_RECOMMENED = 2;//邀请已确认
        public static final int RECEIVE = 3;
        public static final int GO_TO_CHAT = 4;
    }

    public static class MediaObjectTypes {
        public static final int OBJECT = 1;
        public static final int THUMBNAIL = 2;
        public static final int LARGE = 3;
    }

    public static class Selected {
        public static final int ISSELECTED = 1;
        public static final int ISNOTSELECTED = 2;
        public static final int DELETE = 3;
    }

    public static class Receiver {
        public static final int ISPATIENT = 1;
        public static final int ISTAG = 2;
    }


    public static final int ISPATIENT = 2;
    public static final int ISDOCTOR = 1;


    public static final int DELETE_CLICK = 3;
    public static final String SELECT = "SELECT";


    //相片
    public static final String PhotoSelected_AllImages = "all_images";

    public static final String PhotoSelected_CurrentPosition = "currentPosition";

    public static final String PhotoSelected_MaxAllowSelectNumber = "max_allow_select_Number";

    public static final String PhotoSelected_SelectedImages = "Selected_Image_List";

    public static String PhotoSelected_Start = "PhotoSelected_Start";

    public static final String Location_Action = "locationAction";

    public static final String Location_Action_Latitude = "latitude";

    public static final String Location_Action_Longitude = "longitude";

    public static final String ImageBucket_ParamName = "imageBucket";

    public static final String Images_Preview = "imagelist_preview";

    public static final String Poll_ProductTypes = "Poll_ProductTypes";

    public static final String Poll_Scene = "Poll_Scene";

    public static final String Poll_Description = "Poll_Description";

    public static final String Poll_Brand = "Poll_Brand";

    public static final String Poll_Price = "Poll_Price";

}
