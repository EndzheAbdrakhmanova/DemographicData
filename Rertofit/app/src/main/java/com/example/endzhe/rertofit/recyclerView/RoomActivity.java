package com.example.endzhe.rertofit.recyclerView;

import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.endzhe.rertofit.R;
import com.example.endzhe.rertofit.database.AppDatabase;
import com.example.endzhe.rertofit.database.DataDao;
import com.example.endzhe.rertofit.database.DataModel;

import java.util.List;

public class RoomActivity extends AppCompatActivity {
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RoomActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DataDao messageDao = (DataDao) AppDatabase.getInstance(getApplicationContext()).dataDao();
        messageDao.getAllDataModel().observe(this, new Observer<List<DataModel>>() {
            @Override
            public void onChanged(@Nullable List<DataModel> messages) {
                roomAdapter = new RoomAdapter(messages, RoomActivity.this);
                recyclerView.setAdapter(roomAdapter);
            }
        });

    }

    @Override
    protected void onResume() {
        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("last activity",getClass().getSimpleName());
        editor.apply();
        super.onResume();
    }

}
