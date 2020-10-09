package com.zhuanghongji.mpchartexample.notimportant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.zhuanghongji.mpchartexample.R;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class MainActivity3 extends AppCompatActivity {
    private static final String TAG = "";
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextMessage)
    EditText editTextMessage;
    @BindView(R.id.switchImportance)
    Switch switchImportance;

    @BindString(R.string.switch_notifications_on) String switchTextOn;
    @BindString(R.string.switch_notifications_off) String switchTextOff;

    private boolean isHighImportance = false;
    private NotificationHandler notificationHandler;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this); // Right after setContentView
        notificationHandler = new NotificationHandler(this);
//
        Intent intent = new Intent(this, AutoReceiver.class);
        intent.setAction("VIDEO_TIMER");
        sendBroadcast(intent);
////        intent.putExtra("action","VIDEO_TIMER");
////        NotificationHandler[] arr = new NotificationHandler[10];
////        arr[0] = notificationHandler;
////        intent.putExtra("notificationHandler",arr);
////        startActivity(intent);
//        // PendingIntent这个类用于处理即将发生的事情 
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        // AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用相对时间
//        // SystemClock.elapsedRealtime()表示手机开始到现在经过的时间
////        long triggerAtTime = System.currentTimeMillis() + 10 * 1000;
////        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, sender);
//        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime(), 10 * 1000, sender);

//        Intent intent = new Intent(this, AutoReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.MINUTE, 01);
//        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            Toast.makeText(this,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
// 进行闹铃注册
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime, DAY, sender);
        Log.i(TAG,"time ==== " + time +", selectTime ===== "
                + selectTime + ", systemTime ==== " + systemTime +", firstTime === " + firstTime);
        Toast.makeText(this,"设置重复闹铃成功! ", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.buttonSend)
    public void click() {
        sendNotification();
    }

    @OnCheckedChanged(R.id.switchImportance)
    public void change(CompoundButton buttonView, boolean isChecked) {
        isHighImportance = isChecked;
        switchImportance.setText((isChecked) ? switchTextOn : switchTextOff);
    }

    private void sendNotification() {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            Notification.Builder nb = notificationHandler.createNotification(title, message, isHighImportance);
            notificationHandler.getManager().notify(++counter, nb.build());
            notificationHandler.publishNotificationSummaryGroup(isHighImportance);
        }
    }

//    private void sendNotificatonFotTime(){
//        Date time = new Date();
////                        SimpleDateFormat sf = new SimpleDateFormat("ss");
//        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
//        String timestr = sf.format(time);
//        String times[] = {"08:00:00","12:00:00","18:00:00"};
//        for (int i=0; i<3; i++){
//            if (times[i].equals(timestr)){
//
//            }
//        }
//    }
}
