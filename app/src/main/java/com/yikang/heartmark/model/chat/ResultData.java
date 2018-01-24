package com.yikang.heartmark.model.chat;


import com.yikang.heartmark.constant.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Chang on 10/8/2014.
 */
public class ResultData {
    private int code;
    private String reason;
    private JSONObject jsonObject;
    private byte[] data;

    public ResultData() {
    }

    public ResultData(Map map) {
        code = Integer.valueOf((String) map.get("code"));
        reason = (String) map.get("reason");
        jsonObject = (JSONObject) map.get("data");
    }

    public ResultData(JSONObject jsonObject) throws JSONException {
        setJsonData(jsonObject);
    }

    public void setJsonData(JSONObject jsonObject) throws JSONException {
        this.code = jsonObject.getInt("code");
        this.reason = jsonObject.optString("reason");
        this.jsonObject = jsonObject.optJSONObject("msg_data");
    }

    public void setCodeReason(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    //default local error code = -1
    public void setLocalError(String error) {
        this.code = Constants.LOCAL_ERROR;
        this.reason = error;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
