package com.example.mylibrary.imp;

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

public class MyController implements Controller {


    private MyRemoteCtrl mMyRemoteCtrl;
    private RemoteSDKStatusCallBack mRemoteSDKStatusCallBack;
    private boolean isbind;
    private volatile static Controller mInstance;
    protected Context mContext;

    //死亡链接
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


    private MyController() {
    }

    @Override
    public int init(Context context) {
        //初始化服务
        Log.d("mysdk", " sdk  初始化服务  ");
        mContext = context;
        return initService();
    }

    @Override
    public void setStateCallback(RemoteSDKStatusCallBack remoteSDKStatusCallBack) {
        Log.d("mysdk", " sdk  setStateCallback  ");
        this.mRemoteSDKStatusCallBack = remoteSDKStatusCallBack;
    }

    private int initService() {
        Log.d("mysdk", " sdk  initService  ");
        Intent intent = new Intent("com.example.androidsdkdemo.service.MySDKService");
        intent.setClassName("com.example.androidsdkdemo", "com.example.androidsdkdemo.service.MySDKService");
        isbind = mContext.bindService(intent, this, Service.BIND_AUTO_CREATE);
        return 0;
    }

    @Override
    public void setMessage(String msg) {
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
        //SDK内部做释放操作

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("mysdk", " sdk  onServiceConnected  ");
        if (service == null) {
            if (mMyRemoteCtrl != null) {
                try {
                    mMyRemoteCtrl.unlinkToDeath(mMySDKStatusCallBack.asBinder());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mMyRemoteCtrl = null;
        } else {
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
        if (mMyRemoteCtrl != null) {
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
