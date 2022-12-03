package com.example.stuservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.example.stuservice.service.BindImmediateService;
import com.example.stuservice.util.DateUtil;

public class BindImmediateActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView tv_immediate;
    private Intent mIntent; // 声明一个意图对象
    private static String mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_immediate);

        tv_immediate = findViewById(R.id.tv_immediate);
        findViewById(R.id.btn_start_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);

        mDesc = "";
        //创建一个通往 立即绑定服务 的意图
        mIntent = new Intent(this, BindImmediateService.class);
    }

    public static void showText(String desc) {
        if (tv_immediate != null) {
            mDesc = String.format("%s%s %s\n", mDesc, DateUtil.getNowDateTime("HH:mm:ss"), desc);
            tv_immediate.setText(mDesc);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_bind:
                //绑定服务，如果服务未启动，则系统先启动服务再进行绑定
                bindService(mIntent, mFirstConn, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                if (mBindService != null){
                    unbindService(mFirstConn);
                    mBindService = null;
                }
                break;
            default:
                break;
        }
    }

    private BindImmediateService mBindService;//声明一个服务对象
    private ServiceConnection mFirstConn = new ServiceConnection() {

        //获取到服务对象的操作
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //如果服务运行于另外一个进程，则不能强行装换类型，否则会报错
            mBindService = ((BindImmediateService.LocalBinder) service).getService();
        }

        //无法获取到服务对象时的操作
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBindService = null;
        }
    };
}