package com.example.stuservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.stuservice.BindDelayActivity;
import com.example.stuservice.BindImmediateActivity;

public class BindDelayService extends Service {
    private static final String TAG = "BindDelayService111";

    private final IBinder mBinder = new LocalBinder();//创建一个粘合剂对象

    public class LocalBinder extends Binder{
        public BindDelayService getService(){
            return BindDelayService.this;
        }
    }

    private void refresh(String text) {
        Log.d(TAG, text);
        BindDelayActivity.showText(text);
    }
    @Override
    public void onCreate() { // 创建服务
        super.onCreate();
        refresh("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) { // 启动服务，Android2.0以上使用
        Log.d(TAG, "测试服务到此一游！");
        refresh("onStartCommand. flags=" + flags);
        return START_STICKY;
    }

    @Override
    public void onDestroy() { // 销毁服务
        super.onDestroy();
        refresh("onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) { // 绑定服务。普通服务不存在绑定和解绑流程
        Log.d(TAG, "绑定服务开始旅程！");
        refresh("onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) { // 重新绑定服务
        super.onRebind(intent);
        refresh("onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) { // 解绑服务
        refresh("onUnbind");
        return true; // 返回false表示只能绑定一次，返回true表示允许多次绑定
    }
}