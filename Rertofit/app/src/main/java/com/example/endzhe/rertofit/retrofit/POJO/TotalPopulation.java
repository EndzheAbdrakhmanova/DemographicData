package com.example.endzhe.rertofit.retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalPopulation {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("population")
    @Expose
    private Integer population;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

}
