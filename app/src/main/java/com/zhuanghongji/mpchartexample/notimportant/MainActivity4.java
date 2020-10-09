package com.zhuanghongji.mpchartexample.notimportant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.zhuanghongji.mpchartexample.R;

public class MainActivity4 extends Activity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //由于主要是用于测试，并且便于新手理解，所以activity_main布局写的很简单
        setContentView(R.layout.activity_main4);
        intent = new Intent(this, LongRunningService.class);
        //开启关闭Service
        startService(intent);

        //设置一个Toast来提醒使用者提醒的功能已经开始
        Toast.makeText(this,"提醒的功能已经开启，关闭界面则会取消提醒。",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity被关闭后，关闭Service
        stopService(intent);
    }
}