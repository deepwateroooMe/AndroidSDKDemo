package com.example.clienapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mylibrary.MyLibSDK;
import com.example.mylibrary.callback.RemoteSDKStatusCallBack;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyLibSDK.getInstance().init(this);
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


    public void  sedmessage(View view) {
        Log.d("mysdk"," 客户端  sendMessage 我是客户端  " );
        MyLibSDK.getInstance().setMessage("我是客户端");
    }

}
