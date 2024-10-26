package com.example.androidsdkdemo.service;
// 【安卓SDK】服务器、【服务端】：提供 .aidl 服务，供【客户端】绑定连接

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.Nullable;

// 【TODO：】狠奇怪，【服务端】居然是，找不到【安卓SDK】的 .aidl 接口类！！！
import com.example.mylibrary.MyRemoteCtrl;
import com.example.mylibrary.MySDKStatusCallBack;
// 【.aidl服务端】：
public class MySDKService extends Service {
    private MyRemoteCtrlImpl mCarcorderRemoteCtrl = new MyRemoteCtrlImpl(); // 下面：自定义的类
    private MySDKStatusCallBack mMySDKStatusCallBack = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 返回内部代理对象给调用端
        return mCarcorderRemoteCtrl.asBinder();
    }

    // 扩展：【安卓SDK】服务端逻辑，回调给客户端
    // 扩展：覆写，5 个公用 api 的【服务端】处理逻辑
    public class MyRemoteCtrlImpl extends MyRemoteCtrl.Stub { // 基类MyRemoteCtrl.Stub：是个 .aidl 接口，被库，自动生成的 java 类
        private IBinder mBinder = null;
        private Object deathRecipient; // <<<<<<<<<<<<<<<<<<<< 
        @Override
        public void sendMessage(String msg) throws RemoteException {
            if (mMySDKStatusCallBack != null) {
                mMySDKStatusCallBack.sendMessage("我已经接收到你的数据返回给你 =  " + msg);
            }
        }
        // 这2 个【注册取消】：方向，搞不懂。。。【TODO：】
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
            // 这里不做操作直接返回即可
            if (mMySDKStatusCallBack != null) {
                mySDKStatusCallBack.statusCallBackVisible();
            }
        }
        @Override
        public void unregisterMySDKStatusCallBack(MySDKStatusCallBack mySDKStatusCallBack) throws RemoteException {
            Log.d("mysdk"," 服务端 接收到 unregisterMySDKStatusCallBack ");
            mMySDKStatusCallBack = null;
        }
    }
    // 实例1 个：死亡接收？这里不太懂【TODO：】
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
                // 客户端可以执行释放操作
                Log.d("mysdk"," 调用端已经死亡");
            }
        };
}
