// MySDKStatusCallBack.aidl
package com.example.mylibrary;

// Declare any non-default types here with import statements

interface MySDKStatusCallBack {

      void statusCallBackVisible();

      void statusCallBackInvisible();

      void sendMessage(String message);
}
