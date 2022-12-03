package com.example.stuservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.stuservice.service.BindDelayService;
import com.example.stuservice.service.BindImmediateService;
import com.example.stuservice.util.DateUtil;

public class BindDelayActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BindDelayActivity";
    private static TextView tv_delay;
    private Intent mIntent; // 声明一个意图对象
    private static String mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_delay);

        tv_delay = findViewById(R.id.tv_delay);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);

        mDesc = "";
        //创建一个通往 立即绑定服务 的意图
        mIntent = new Intent(this, BindDelayService.class);
    }

    public static void showText(String desc) {
        if (tv_delay != null) {
            mDesc = String.format("%s%s %s\n", mDesc, DateUtil.getNowDateTime("HH:mm:ss"), desc);
            tv_delay.setText(mDesc);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                //绑定服务，如果服务未启动，则系统先启动服务再进行绑定
                startService(mIntent);
                Log.d(TAG, "onClick: 启动服务");
                break;
            case R.id.btn_bind:
                //绑定服务，如果服务未启动，则系统先启动服务再进行绑定
                boolean bindFlag = bindService(mIntent, mServiceConn, Context.BIND_AUTO_CREATE);
                Log.d(TAG, "bindFlag=" + bindFlag);
                break;
            case R.id.btn_unbind:
                if (mBindService != null){
                    unbindService(mServiceConn);
                }
                break;
            case R.id.btn_stop:
                //绑定服务，如果服务未启动，则系统先启动服务再进行绑定
               stopService(mIntent);
                break;
            default:
                break;
        }
    }

    private BindDelayService mBindService;//声明一个服务对象
    private ServiceConnection mServiceConn = new ServiceConnection() {

        //获取到服务对象的操作
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //如果服务运行于另外一个进程，则不能强行装换类型，否则会报错
            mBindService = ((BindDelayService.LocalBinder) service).getService();
            Log.d(TAG, "onServiceConnected: ");
        }

        //无法获取到服务对象时的操作
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBindService = null;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };
}