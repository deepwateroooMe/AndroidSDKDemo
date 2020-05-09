package com.example.androidsdkdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mylibrary.MySDKStatusCallBack;
import com.example.mylibrary.MyRemoteCtrl;

public class MySDKService extends Service {

    private MyRemoteCtrlImpl mCarcorderRemoteCtrl = new MyRemoteCtrlImpl();
    private MySDKStatusCallBack mMySDKStatusCallBack = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //返回内部代理对象给调用端
        return mCarcorderRemoteCtrl.asBinder();
    }


    public class MyRemoteCtrlImpl extends MyRemoteCtrl.Stub {
        private IBinder mBinder = null;
        private Object deathRecipient;

        @Override
        public void sendMessage(String msg) throws RemoteException {
           if (mMySDKStatusCallBack != null) {
               mMySDKStatusCallBack.sendMessage("我已经接收到你的数据返回给你 =  " + msg);
           }
        }

        @Override
        public void linkToDeath(IBinder binder) throws RemoteException {
            Log.d("mysdk"," 服务端 建立死亡链接  ");
            mBinder = binder;
            binder.linkToDeath(mDeathRecipient, 0);
        }


        @Override
        public void unlinkToDeath(IBinder binder) throws RemoteException {
            Log.d("mysdk"," 服务端 断开客户端死亡链接  ");
            binder.unlinkToDeath(mDeathRecipient, 0);
            mBinder = null;
        }

        @Override
        public void registerMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack) throws RemoteException {
            Log.d("mysdk"," 服务端 接收到 registerMySDKStatusCallBack ");
            mMySDKStatusCallBack = mySDKStatusCallBack;
            // mMySDKStatusCallBack 为第三方通过SDK传递过来的 对象 调用 mySDKStatusCallBack.statusCallBackInvisible()
            // 相当于持有  MySDKStatusCallBack mMySDKStatusCallBack

            //这里不做操作直接返回即可
            if (mMySDKStatusCallBack != null) {
                mySDKStatusCallBack.statusCallBackInvisible();
            }
        }

        @Override
        public void unregisterMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack) throws RemoteException {
            Log.d("mysdk"," 服务端 接收到 unregisterMySDKStatusCallBack ");
            mMySDKStatusCallBack = null;
        }
    }



    IBinder.DeathRecipient mDeathRecipient =  new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
              /*  if (mMySDKStatusCallBack != null) {
                    try {
                        mMySDKStatusCallBack.statusCallBackInvisible();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }*/

                //客户端可以执行释放操作
                Log.d("mysdk"," 调用端已经死亡");
            }

        };

}
