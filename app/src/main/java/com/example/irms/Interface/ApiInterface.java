package com.example.irms.Interface;

import com.example.irms.Model.AcceptSessionResponse;
import com.example.irms.Model.ChangePasswordResponse;
import com.example.irms.Model.GetCategoryResponse;
import com.example.irms.Model.GetCurrencyResponse;
import com.example.irms.Model.LoginResponse;
import com.example.irms.Model.SessionDetailsResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("User/ValidateLogin")
    Call<LoginResponse> createUser(
            @Header("Authorization") String Auth,
            @Body RequestBody params);

    @POST("Session/SessionDetail")
    Call<SessionDetailsResponse> getDetails(
            @Header("Authorization") String Auth);

    @POST("Session/AcceptSession")
    Call<AcceptSessionResponse> getAcceptDetails(
            @Header("Authorization") String Auth);

    @POST("Session/SurrenderSession")
    Call<AcceptSessionResponse> getSurrenderDetails(
            @Header("Authorization") String Auth);

    @POST("User/ChangePassword")
    Call<ChangePasswordResponse> getChangePassword(
            @Header("Authorization") String Auth,
            @Body RequestBody params
    );

    @POST("Currency/GetCurrency")
    Call<List<GetCurrencyResponse>> getCurrencyCode(
            @Header("Authorization") String Auth
    );

    @POST("Category/GetByBEID")
    Call<List<GetCategoryResponse>> getCategory(
            @Header("Authorization") String Auth,
            @Body RequestBody params
    );

}
