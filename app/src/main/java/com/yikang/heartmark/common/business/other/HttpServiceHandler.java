package com.yikang.heartmark.common.business.other;

import org.json.JSONObject;

/**
 * Created by guolchen on 2014/12/13.
 */
public abstract class HttpServiceHandler<T> {
    protected DataHandler<T> dataHandler;

    public HttpServiceHandler(DataHandler<T> dataHandler){
        this.dataHandler = dataHandler;
    }

    public abstract void onResponse(int code, String reason, JSONObject msgData);
    public void onError(int code, String reason, Throwable e){}

    public DataHandler<T> getDataHandler() {
        return dataHandler;
    }
}