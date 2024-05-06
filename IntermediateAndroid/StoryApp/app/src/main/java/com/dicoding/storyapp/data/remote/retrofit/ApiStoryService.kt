package com.dicoding.storyapp.data.remote.retrofit

import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiStoryService {
    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : ErrorResponse

    @GET("stories")
    suspend fun getStories() : StoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(@Path("id") id: String) : StoryResponse
}