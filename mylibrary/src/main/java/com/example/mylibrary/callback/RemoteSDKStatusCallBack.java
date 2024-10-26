package com.example.mylibrary.callback;

// .aidl 远程服务：回调接口封装，3 个 api
public interface RemoteSDKStatusCallBack {
    void statusCallBackVisible();
    void statusCallBackInvisible();
    void sendMessage(String message);
}
