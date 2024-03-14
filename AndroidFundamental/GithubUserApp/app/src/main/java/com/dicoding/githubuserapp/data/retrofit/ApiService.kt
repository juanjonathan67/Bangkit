package com.dicoding.githubuserapp.data.retrofit

import com.dicoding.githubuserapp.data.response.FollowResponseItem
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetails(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>


    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>

}