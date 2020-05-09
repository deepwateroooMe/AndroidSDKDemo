package com.example.mylibrary;

import com.example.mylibrary.MySDKStatusCallBack;

interface MyRemoteCtrl {

        void sendMessage(String msg);

        void linkToDeath(IBinder binder);
        void unlinkToDeath(IBinder binder);

        void registerMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack);

        void unregisterMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack);
}
