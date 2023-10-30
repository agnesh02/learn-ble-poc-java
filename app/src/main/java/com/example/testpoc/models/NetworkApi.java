package com.example.testpoc.models;

import com.example.testpoc.utils.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkApi {
    @GET("/users")
    Call<List<User>> getData();
}
