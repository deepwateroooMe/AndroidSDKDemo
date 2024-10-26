package com.example.mylibrary;

import android.content.Context;
import com.example.mylibrary.base.Controller;
import com.example.mylibrary.callback.RemoteSDKStatusCallBack;
import com.example.mylibrary.imp.MyController;

// 【安卓SDK】
public class MyLibSDK {
    private volatile static MyLibSDK mInstance;
    private Controller mController;

    private MyLibSDK() {}
    public static MyLibSDK getInstance() {
        if (mInstance == null) {
            synchronized (MyLibSDK.class) {
                if (mInstance == null) {
                    mInstance = new MyLibSDK();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化SDK
     *
     * @param context context
     * @return result
     */
    public int init(Context context) {
        mController = MyController.getInstance();
        return mController.init(context);
    }
    /***
     * 发送消息
     */
    public void sendMessage(String msg) {
        if (mController != null) {
            mController.sendMessage(msg);
        }
    }
    /***
     * 释放SDK
     */
    public void release() {
        if (mController != null) {
            mController.release();
        }
    }
    /***
     * 设置监听
     */
    public void setStateCallback(RemoteSDKStatusCallBack remoteSDKStatusCallBack) {
        if (mController != null) {
            mController.setStateCallback(remoteSDKStatusCallBack);
        }
    }
}
