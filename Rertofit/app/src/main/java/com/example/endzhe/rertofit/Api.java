package com.example.endzhe.rertofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//создаем интерфейс запросов
public interface Api {

    @GET("wp-rank/{dob}/{sex}/{country}/on/{date}")
    Call<RankModel> getRank(
            @Path("dob") String dob,
            @Path("sex") String sex,
            @Path ("country") String country,
            @Path ("date") String date

    );

}
