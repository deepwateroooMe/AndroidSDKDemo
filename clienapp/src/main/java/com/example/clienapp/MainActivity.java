package com.example.clienapp;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.mylibrary.MyLibSDK;
import com.example.mylibrary.callback.RemoteSDKStatusCallBack;
// 【客户端】逻辑：
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 【安卓SDK】初始化：会能，自动启动【安卓SDK 服务端的、对外服务】
        MyLibSDK.getInstance().init(this); 
        // 向【安卓SDK 服务端】：注册回调给、我客户端、的回调接口
        MyLibSDK.getInstance().setStateCallback(new RemoteSDKStatusCallBack() {
                @Override
                public void statusCallBackVisible() {
                    Log.d("mysdk"," 客户端  statusCallBackVisible  ");
                }
                @Override
                public void statusCallBackInvisible() {
                    Log.d("mysdk"," 客户端  statusCallBackInvisible  ");
                }
                @Override
                public void sendMessage(String s) {
                    Log.d("mysdk"," 客户端  sendMessage   " + s);
                }
            });
    }

    // 这个方法，写了是、故意、糊弄人的。。。
    public void sedmessage(View view) {
        Log.d("mysdk"," 客户端  sendMessage 我是客户端  " );
        MyLibSDK.getInstance().setMessage("我是客户端");
    }
}
