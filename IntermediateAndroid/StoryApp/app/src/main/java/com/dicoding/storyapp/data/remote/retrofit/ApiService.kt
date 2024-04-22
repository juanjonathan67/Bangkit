package com.dicoding.storyapp.data.remote.retrofit

import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body registerRequest: RequestBody
    ) : RegisterResponse

    @POST("login")
    suspend fun login(
        @Body loginRequest: RequestBody
    ) : LoginResponse
}