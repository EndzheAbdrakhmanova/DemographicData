package com.example.endzhe.rertofit.retrofit.POJO;

import com.google.gson.annotations.SerializedName;

public class TotalPopulationModel {
    @SerializedName("total_population")
    private TotalPopulation totalPopulation;

    public TotalPopulation getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(TotalPopulation totalPopulation) {
        this.totalPopulation = totalPopulation;
    }
}
