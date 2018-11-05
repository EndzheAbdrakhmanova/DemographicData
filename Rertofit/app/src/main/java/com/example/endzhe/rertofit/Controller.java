package com.example.endzhe.rertofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//класс, в кот.-м будем инициализировать Retrofit
public class Controller {
    public static Api createApi(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(Api.class);
    }
}
