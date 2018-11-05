package com.example.endzhe.rertofit.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DataModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String dob;
    private String sex;
    private String country;
    private String date;
    private String rank;

    public DataModel(String dob, String sex, String country, String date, String rank) {
        this.dob = dob;
        this.sex = sex;
        this.country = country;
        this.date = date;
        this.rank = rank;
    }

    //--SETTER--
    public void setId(int id) {
        this.id = id;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    //--GETTER--
    public int getId() {
        return id;
    }

    public String getDob() {
        return dob;
    }

    public String getSex() {
        return sex;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        return date;
    }

    public String getRank() {
        return rank;
    }
}