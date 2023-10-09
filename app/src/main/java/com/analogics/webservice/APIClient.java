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

public class APIClient {

       //static String URL="http://103.255.144.234:1418/APSPDCL/analogics/";//APSPDCL Analogics test server
        static String URL="http://14.99.140.194:9094/UploadWs/analogics/";
       static String BASE_URL="http://172.16.0.207:9094/TsspdclAuthWs/analogics/";


      private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient client = null;

        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
             client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        }catch (Exception ignored){}

        //.baseUrl("https://reqres.in")
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public static Retrofit getDownloadClient() {
        OkHttpClient client = null;
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        }catch (Exception ignored){}
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build();
        return retrofit;
    }

}
