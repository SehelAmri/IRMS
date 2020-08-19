package com.example.irms.Interface;

import com.example.irms.Model.AcceptSessionResponse;
import com.example.irms.Model.LoginResponse;
import com.example.irms.Model.SessionDetailsResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("Login/ValidateLogin")
    Call<LoginResponse> createUser(
            @Header("Authorization") String Auth,
             @Body RequestBody params);
@POST("SessionDetail/SessionDetail")
    Call<SessionDetailsResponse> getDetails(
  @Header("Authorization") String Auth,
  @Body RequestBody params);
    @POST("AcceptSession/AcceptSession")
    Call<AcceptSessionResponse> getAcceptDetails(
            @Header("Authorization") String Auth,
            @Body RequestBody params);
    @POST("SurrenderSession/SurrenderSession")
    Call<AcceptSessionResponse> getSurrenderDetails(
            @Header("Authorization") String Auth,
            @Body RequestBody params);
}
