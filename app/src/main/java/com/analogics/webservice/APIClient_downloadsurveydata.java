package com.analogics.webservice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Aanil Reddi Gaantla on 25/03/2019.
 */

public class APIClient_downloadsurveydata {

    static String URL="http://103.255.144.234:1418/APSPDCL/analogics/";//APSPDCL Analogics test server

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        //.baseUrl("https://reqres.in")
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .client(client)
                .build();



        return retrofit;
    }

}
