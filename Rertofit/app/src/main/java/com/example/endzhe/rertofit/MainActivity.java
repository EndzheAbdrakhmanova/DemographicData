package com.example.endzhe.rertofit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.endzhe.rertofit.recyclerView.RoomActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("create","activity created");
        Button btnQuery1 = (Button) findViewById(R.id.btnQuery1);
        btnQuery1.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get last open Activity
        String lastActivity=PreferenceManager.getDefaultSharedPreferences(this)
                .getString("last activity","");
        if(lastActivity.length()>0) {
            if (lastActivity.equals(Query1Activity.class.getSimpleName())) {
                startActivity(new Intent(this, Query1Activity.class));
            } else if (lastActivity.equals(RoomActivity.class.getSimpleName())) {
                startActivity(new Intent(this, RoomActivity.class));
            }


        }



////Установление напоминания
        //доступ к службе Alarm Service
        AlarmManager am=(AlarmManager)getSystemService((Context.ALARM_SERVICE));
        Intent notificationIntent = new Intent(this,HandlerAlarmManager.class);
        PendingIntent contentIntent=PendingIntent.getBroadcast(MainActivity.this,
                1,notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        //удаляем все сигнализации
        am.cancel(contentIntent);
        //задаем повторяющиеся сигнализации с фиксированным интервалом
        am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/15,AlarmManager.INTERVAL_FIFTEEN_MINUTES/15,
                contentIntent);




    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Query1Activity.class);
        startActivity(intent);

    }



    @Override
    protected void onResume() {


        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("last activity",getClass().getSimpleName());
        editor.apply();
        super.onResume();
    }

    }
