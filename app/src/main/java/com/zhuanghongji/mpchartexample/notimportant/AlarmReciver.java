package com.zhuanghongji.mpchartexample.notimportant;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhuanghongji.mpchartexample.R;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //设置通知内容并在onReceive()这个函数执行时开启
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new Notification(R.drawable.ic_launcher,"用电脑时间过长了！白痴！"
                ,System.currentTimeMillis());
//        notification.setLatestEventInfo(context, "快去休息！！！",
//                "一定保护眼睛,不然遗传给孩子，老婆跟别人跑啊。", null);
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(1, notification);


        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }


}

