package com.example.endzhe.rertofit;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import static android.content.Context.NOTIFICATION_SERVICE;

public class HandlerAlarmManager extends BroadcastReceiver {

    //этот метод будет вызываться по событию
    @Override
    public void onReceive(Context context, Intent intent) {

        //для открытия приложения при нажатии на уведомление
        Intent mainIntent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        //создание
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentIntent(pendingIntent);

        builder.setContentTitle("Зайдите в приложение");
        builder.setContentText("ಠ_ಠ ");
        builder.setAutoCancel(true);
        builder.setShowWhen(true);
        builder.setPriority(2);
        builder.setWhen(System.currentTimeMillis());

        long []vibrate=new long[]{1000,1000,1000,1000,1000};
        builder.setVibrate(vibrate);
        Notification notification = builder.build();

        //отправка
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1, notification);

    }


}