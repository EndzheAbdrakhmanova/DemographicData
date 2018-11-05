package com.example.endzhe.rertofit.retrofit;

import com.google.gson.annotations.SerializedName;

public class RankTodayModel {
    @SerializedName("dob")
    private String dob;

    @SerializedName("country")
    private String country;

    @SerializedName("rank")
    private Integer rank;

    @SerializedName("sex")
    private String sex;


//    @SerializedName("population ")
//    private String population ;
//
//    @SerializedName("total_life_expectancy")
//    private float total_life_expectancy;




    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    public Integer getRankToday() {
        return rank;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


}
