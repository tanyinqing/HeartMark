package com.yikang.heartmark.common.business.other;

/**
 * Created by guolchen on 2014/12/13.
 */
public interface DataHandler<T> {
    void onData(int code, String str, T obj);
}