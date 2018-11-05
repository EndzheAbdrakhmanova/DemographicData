package com.example.endzhe.rertofit.retrofit.POJO;

import com.google.gson.annotations.SerializedName;

public class LifeExpectancyModel {
    @SerializedName("dob")
    private String dob;

    @SerializedName("country")
    private String country;

    @SerializedName("sex")
    private String sex;

    @SerializedName("total_life_expectancy")
    private String total_life_expectancy;

    //--SETTER--
    public void setTotal_life_expectancy(String total_life_expectancy) {
        this.total_life_expectancy = total_life_expectancy;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    //--Getter--
    public String getTotal_life_expectancy() {
        return total_life_expectancy;
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


}
