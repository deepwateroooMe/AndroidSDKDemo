package com.example.mylibrary.imp;
// 【安卓SDK】逻辑：
// 【亲爱的表哥的活宝妹，任何时候，亲爱的表哥的活宝妹，就是一定要、一定会嫁给活宝妹的亲爱的表哥！！！爱表哥，爱生活！！！】
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.example.mylibrary.MyRemoteCtrl;
import com.example.mylibrary.MySDKStatusCallBack;
import com.example.mylibrary.base.Controller;
import com.example.mylibrary.callback.RemoteSDKStatusCallBack;

// 【安卓SDK】的控制处理逻辑
public class MyController implements Controller {

    private MyRemoteCtrl mMyRemoteCtrl;
    private RemoteSDKStatusCallBack mRemoteSDKStatusCallBack;
    private boolean isbind;
    private volatile static Controller mInstance; // volatile: aidl 实现【1 个服务端】＋【N 个客户端】的1:N 绑定宽带服务
    protected Context mContext;

    // 【服务端】逻辑：
    private MySDKStatusCallBack mMySDKStatusCallBack = new MySDKStatusCallBack.Stub() {
            @Override
            public void statusCallBackVisible() throws RemoteException {
                mRemoteSDKStatusCallBack.statusCallBackVisible();
            }
            @Override
            public void statusCallBackInvisible() throws RemoteException {
                mRemoteSDKStatusCallBack.statusCallBackInvisible();
            }
            @Override
            public void sendMessage(String message) throws RemoteException {
                mRemoteSDKStatusCallBack.sendMessage(message);
            }
        };
    private MyController() {}

    @Override
    public int init(Context context) {
        // 初始化服务
        Log.d("mysdk", " sdk  初始化服务  ");
        mContext = context;
        return initService();
    }
    // 自动：启动【安卓SDK 服务端】定义的、对外服务
    private int initService() {
        Log.d("mysdk", " sdk  initService  ");
        // 【安卓SDK】＋【SDK 服务端】：2 层封装，对外，给N 多【客户端】提供，SDK 绑定服务
        // 一个【安卓SDK】提供给外界公用，它拥有一个【服务器、服务端】，提供的服务，是设计、定义好的、固定写澐
        Intent intent = new Intent("com.example.androidsdkdemo.service.MySDKService");
        intent.setClassName("com.example.androidsdkdemo", "com.example.androidsdkdemo.service.MySDKService");
        isbind = mContext.bindService(intent, this, Service.BIND_AUTO_CREATE);
        return 0;
    }

    @Override
    public void setStateCallback(RemoteSDKStatusCallBack remoteSDKStatusCallBack) {
        Log.d("mysdk", " sdk  setStateCallback  ");
        this.mRemoteSDKStatusCallBack = remoteSDKStatusCallBack;
    }
    @Override
    public void sendMessage(String msg) {
        try {
            Log.d("mysdk", " sdk  setMessage  ");
            mMyRemoteCtrl.sendMessage(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void release() {
        Log.d("mysdk", " sdk  release  ");
        // SDK内部做释放操作
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("mysdk", " sdk  onServiceConnected  ");
        // 这个分支：应该是处理意外情况：就是接到 onServiceConnected() 回调，但服务死掉了？？？
        if (service == null) { // 这个分支
            if (mMyRemoteCtrl != null) {
                try {
                    mMyRemoteCtrl.unlinkToDeath(mMySDKStatusCallBack.asBinder());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mMyRemoteCtrl = null;
        } else { // 这个分支：是相对、正常的处理逻辑
            mMyRemoteCtrl = MyRemoteCtrl.Stub.asInterface(service);
            if (mMyRemoteCtrl != null) {
                try {
                    mMyRemoteCtrl.linkToDeath(mMySDKStatusCallBack.asBinder());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    mMyRemoteCtrl.registerMySDKStatusCallBack(mMySDKStatusCallBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("mysdk", " sdk  onServiceDisconnected  ");
        if (mMyRemoteCtrl != null) { // 取消：各种先前的注册 
            try {
                mMyRemoteCtrl.unregisterMySDKStatusCallBack(mMySDKStatusCallBack);
                mMyRemoteCtrl.unlinkToDeath(mMySDKStatusCallBack.asBinder());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mMyRemoteCtrl = null;
    }

    public static Controller getInstance() {
        if (mInstance == null) {
            synchronized (MyController.class) {
                if (mInstance == null) {
                    mInstance = new MyController();
                }
            }
        }
        return mInstance;
    }
}
