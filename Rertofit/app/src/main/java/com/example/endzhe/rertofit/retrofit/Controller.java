package com.example.endzhe.rertofit.retrofit;

import com.example.endzhe.rertofit.Constants;
import com.example.endzhe.rertofit.retrofit.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//класс, в котором создадим объект для зопроса к серверу
public class Controller {
    public static Api createApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }
}
