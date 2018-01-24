package com.yikang.heartmark.model.chat;


import com.yikang.heartmark.constant.Constants;

import java.util.Date;

/**
 * Created by shangxzheng on 2014/8/20.
 */
public class Doctor extends User {
    public static final String JOB_TITLE = "job_title";
    public static final String TEACHING_TITLE = "teaching_title";
    public static final String HOSPITAL_ID = "hospital_id";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String DOCTOR_ID = "doctor_id";

    private String doctorId = "";//混淆后的user_id
    private String jobTitle = "";//技术职称，如主任医师
    private String teachingTitle = ""; //教学职称 教授
    private String hospitalName = "";//所在医院
    private String departmentName = "";//科室信息
    private boolean enableCallMe = false;//是否允许给我打电话

    private int hospitalId = 0;//医院id
    private int departmentId = 0;//科室ID
    private String experiense = "";//医生的经历
    private String jsonTags;//患者给医生打的标签
    private Date createdRealtionTime;
    private int totalPatient = 0;
    private int status = Constants.Relation.RELATION_CODE_NORMAL; //1表示有关系，2表示主动解除关系，3表示对方解除关系

    private boolean isBindAccount = false; //绑定银行账户
    private boolean isOpenVip = false; //开启vip
    private String bankName = "";
    private String bankAccountNum = "";
    private String bankCardName = "";
    /* xinjia*/
    public int product_level;
    public String product_name = "";
    public String product_icon_id = "";

    private String composite_grade = ""; //综合评分
    private String grade_number = "";
    // private FirstRatting firstRatting;
    private float money; //余额
    private String bankInfoTip = "";

    public void setBankInfoTip(String bankInfoTip) {
        this.bankInfoTip = bankInfoTip;
    }

    public String getBankInfoTip() {
        return bankInfoTip;
    }

    public int getProduct_level() {
        return product_level;
    }

    public void setProduct_level(int product_level) {
        this.product_level = product_level;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_icon_id() {
        return product_icon_id;
    }

    public void setProduct_icon_id(String product_icon_id) {
        this.product_icon_id = product_icon_id;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getMoney() {
        return money;
    }

//    public void setFirstRatting(FirstRatting firstRatting) {
//        this.firstRatting = firstRatting;
//    }
//
//    public FirstRatting getFirstRatting() {
//        return firstRatting;
//    }

    private boolean open_service;
//    public DoctorProductInfo product_info;
//
//    public DoctorProductInfo getProduct_info() {
//        return product_info;
//    }
//
//    public void setProduct_info(DoctorProductInfo product_info) {
//        this.product_info = product_info;
//    }

    public boolean isOpen_service() {
        return open_service;
    }

    public void setOpen_service(boolean open_service) {
        this.open_service = open_service;
    }

//    public RattingInfo rattingInfo;
//
//    public RattingInfo getRattingInfo() {
//        return rattingInfo;
//    }
//
//    public void setRattingInfo(RattingInfo rattingInfo) {
//        this.rattingInfo = rattingInfo;
//    }


    public void setComposite_grade(String composite_grade) {
        this.composite_grade = composite_grade;
    }

    public void setGrade_number(String grade_number) {
        this.grade_number = grade_number;
    }

    public String getComposite_grade() {
        return composite_grade;
    }

    public String getGrade_number() {
        return grade_number;
    }

    public void setOpenVip(boolean isOpenVip) {
        this.isOpenVip = isOpenVip;
    }

    public boolean isOpenVip() {
        return isOpenVip;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBindAccount(boolean isBindAccount) {
        this.isBindAccount = isBindAccount;
    }

    public boolean isBindAccount() {
        return isBindAccount;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getExperiense() {
        return experiense;
    }

    public void setExperiense(String experiense) {
        this.experiense = experiense;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTeachingTitle() {
        return teachingTitle;
    }

    public void setTeachingTitle(String teachingTitle) {
        this.teachingTitle = teachingTitle;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isEnableCallMe() {
        return enableCallMe;
    }

    public void setEnableCallMe(boolean enableCallMe) {
        this.enableCallMe = enableCallMe;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getJsonTags() {
        return jsonTags;
    }

    public void setJsonTags(String jsonTags) {
        this.jsonTags = jsonTags;
    }

    public Date getCreatedRealtionTime() {
        return createdRealtionTime;
    }

    public void setCreatedRealtionTime(Date createdRealtionTime) {
        this.createdRealtionTime = createdRealtionTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalPatient() {
        return totalPatient;
    }

    public void setTotalPatient(int totalPatient) {
        this.totalPatient = totalPatient;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", teachingTitle='" + teachingTitle + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", enableCallMe=" + enableCallMe +
                ", hospitalId=" + hospitalId +
                ", departmentId=" + departmentId +
                ", experiense='" + experiense + '\'' +
                ", jsonTags='" + jsonTags + '\'' +
                ", createdRealtionTime=" + createdRealtionTime +
                ", status=" + status +
                '}';
    }
}
