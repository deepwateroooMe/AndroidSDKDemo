package com.example.mylibrary.base;

import android.content.Context;
import android.content.ServiceConnection;
import com.example.mylibrary.callback.RemoteSDKStatusCallBack;

public interface Controller extends ServiceConnection {
    /**
     * init sdk
     * @param context context
     * @return result
     */
    int init(Context context);

    /**
     * setStateCallback
     * @param remoteSDKStatusCallBack
     */
    void setStateCallback(RemoteSDKStatusCallBack remoteSDKStatusCallBack);
    void sendMessage(String msg); // setMessage() 感觉方法名，狠别扭？ sendMessage()?

    /***
     * release sdk
     */
    void release();
}
