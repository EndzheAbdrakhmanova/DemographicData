package com.example.endzhe.rertofit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

@Dao
public interface DataDao {

    @Query("select * from dataModel")
    public LiveData<List<DataModel>> getAllDataModel();

    @Delete
    public void deleteDataModel(DataModel dataModel);

    @Insert
    public void insertDataModel(DataModel dataModel);
}
