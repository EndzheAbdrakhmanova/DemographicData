package com.example.endzhe.rertofit;

import com.example.endzhe.rertofit.retrofit.Api;
import com.example.endzhe.rertofit.retrofit.Controller;
import com.example.endzhe.rertofit.retrofit.POJO.RankModel;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ApiTest {
    private final Api api = Controller.createApi();

    @Test
    public void testCurrentRank() throws IOException {
        //create query
        Call<RankModel> call = api.getRank(
                "1952-03-11",
                "male",
                "Brazil",
                "2001-05-11"
                );

        Response<RankModel> response = call.execute();

        // Проверим, что запрос завершился норм
        Assert.assertTrue(response.isSuccessful());

        RankModel body = response.body();

        // Проверим, что тело запроса распарсилось нормально
        Assert.assertNotNull(body);

        Integer rank = body.getRank();

        // Проверим, что есть rank
        Assert.assertNotNull(rank);
       Assert.assertTrue(rank.toString().length() > 0);


    }

}
