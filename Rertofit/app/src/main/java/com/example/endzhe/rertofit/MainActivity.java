package com.example.endzhe.rertofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.endzhe.rertofit.recyclerView.RoomActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//            else {
//                startActivity(new Intent(this, MainActivity.class));
//            }
//            Class <?> cls= null;
//            try {
//                cls = Class.forName(lastActivity);
//                Intent intentActivity=new Intent(this,cls);
//                startActivity(intentActivity);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }

        }

        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList=sensorManager.getSensorList(Sensor.TYPE_PRESSURE);
        for (int i=0;i<sensorList.size();i++){
            Log.v("list","сенсор:"+sensorList.get(i).getName());
        }



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
