package com.yikang.heartmark.model.chat;

/**
 * Created by shangxzheng on 2014/8/25.
 */
public class DeviceInfo {

    private String deviceId;//设备id
    private String osVersion;//系统版本
    private String resolution;//分辨率
    private String deviceType;//设备类型
    private String clientVersion;//当前使用的软件版本号
    private String networkOperator;//手机设备运营商（移动，联通，电信），不是手机设备时赋值为null

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

}
