package com.example.irms.Client;

import com.example.irms.Interface.ApiInterface;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://rms.satconet.co.tz/IRMSRestAPI/api/";
    private static RetrofitClient mInstance;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .protocols(Util.immutableList(Protocol.HTTP_1_1))
            .build();
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getmInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }

}
