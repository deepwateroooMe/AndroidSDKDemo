// MySDKStatusCallBack.aidl
package com.example.mylibrary;

interface MySDKStatusCallBack {
    void statusCallBackVisible();
    void statusCallBackInvisible();
    void sendMessage(String message);
}
