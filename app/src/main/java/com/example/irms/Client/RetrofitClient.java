package com.example.irms.Client;

import android.util.Base64;

import com.example.irms.Interface.ApiInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://192.168.1.71/AndroidRestAPI/api/";
    OkHttpClient okHttpClient = UnsafeHttpClient.okHttpClient();
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized RetrofitClient getmInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);
    }

}
