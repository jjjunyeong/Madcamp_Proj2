package com.example.myapplication3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/checkuser")
    Call<Void> checkUser (@Body HashMap<String, String> map);

    @POST("/travel")
    Call<Void> addTravel (@Body HashMap<String, List<String>> map);

    @POST("/gettravel")
    Call<List<TravelResult>> getTravel (@Body HashMap<String, String> map);

    @POST("/removetravel")
    Call<Void> removeTravel (@Body HashMap<String, String> map);

}