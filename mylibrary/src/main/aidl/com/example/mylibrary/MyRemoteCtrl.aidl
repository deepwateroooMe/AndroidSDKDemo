package com.example.mylibrary;

import com.example.mylibrary.MySDKStatusCallBack;

// 【安卓SDK】： aidl 公用接口
// 【客户端】：没有再、重复定义、这些接口，是因为【客户端】直接引入了，【安卓SDK.jar 库】
// 【服务端】：居然是，找不到这 2 个 .aidl 的接口！！
interface MyRemoteCtrl {

    void sendMessage(String msg);
    void linkToDeath(IBinder binder);
    void unlinkToDeath(IBinder binder);
    void registerMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack);
    void unregisterMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack);
}
