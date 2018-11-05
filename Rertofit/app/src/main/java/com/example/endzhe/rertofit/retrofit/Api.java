package com.example.endzhe.rertofit.retrofit;

import com.example.endzhe.rertofit.retrofit.POJO.LifeExpectancyModel;
import com.example.endzhe.rertofit.retrofit.POJO.RankModel;
import com.example.endzhe.rertofit.retrofit.POJO.RankTodayModel;
import com.example.endzhe.rertofit.retrofit.POJO.TotalPopulationModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

//создаем интерфейс запросов
public interface Api {

    @GET("wp-rank/{dob}/{sex}/{country}/on/{date}")
    Call<RankModel> getRank(
            @Path("dob") String dob,
            @Path("sex") String sex,
            @Path("country") String country,
            @Path("date") String date

    );

    // /wp-rank/{dob}/{sex}/{country}/today/
    @GET("wp-rank/{dob}/{sex}/{country}/today/")
    Call<RankTodayModel> getRankToday(
            @Path("dob") String dob,
            @Path("sex") String sex,
            @Path("country") String country
    );


    // /life-expectancy/total/{sex}/{country}/{dob}/
    @GET("life-expectancy/total/{sex}/{country}/{dob}/")
    Call<LifeExpectancyModel> getTotal_life_expectancy(
            @Path("sex") String sex,
            @Path("country") String country,
            @Path("dob") String dob
    );

    // /population/{country}/{date}/
    @GET("population/{country}/{date}/")
    Call<TotalPopulationModel> getPopulation(
            @Path("country") String country,
            @Path("date") String date
    );

}
