package com.zhuanghongji.mpchartexample.notimportant;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhuanghongji.mpchartexample.R;

import butterknife.OnCheckedChanged;

public class AutoReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_FLAG = 1;
//    private MainActivity3 MainActivity3;
    private boolean isHighImportance = false;
    private NotificationHandler notificationHandler;
    private int counter = 0;
//    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
        notificationHandler = new NotificationHandler(GetContext.toastNews());
//        if (intent.getAction().equals("VIDEO_TIMER")) {
//            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification notification=new Notification(R.drawable.ic_launcher,"用电脑时间过长了！白痴！"
//                    ,System.currentTimeMillis());
////            notification.setLatestEventInfo(context, "快去休息！！！",
////                    "一定保护眼睛,不然遗传给孩子，老婆跟别人跑啊。", null);
//            notification.defaults = Notification.DEFAULT_ALL;
//            manager.notify(1, notification);

//            Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                    new Intent(context, MainActivity3.class), 0);
//             通过Notification.Builder来创建通知，注意API Level
//             API16之后才支持
//            Notification notify = new Notification.Builder(context)
////                    .setSmallIcon(R.drawable.backgroundnew)
//                    .setTicker("TickerText:" + "您有新短消息，请注意查收！")
//                    .setContentTitle("Notification Title")
//                    .setContentText("This is the notification message").setNumber(1).build();
////                    .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
//            // level16及之后增加的，API11可以使用getNotificatin()来替代
//            notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
//            // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示

//            isHighImportance = true;
            Notification.Builder nb = notificationHandler.createNotification("注意", "请吃药", isHighImportance);
            notificationHandler.getManager().notify(++counter, nb.build());
            notificationHandler.publishNotificationSummaryGroup(isHighImportance);
//        }
    }
}