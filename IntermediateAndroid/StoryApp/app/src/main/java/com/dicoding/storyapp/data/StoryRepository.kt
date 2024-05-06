package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiAuthService
import com.dicoding.storyapp.data.remote.retrofit.ApiStoryService
import com.dicoding.storyapp.utils.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiStoryService: ApiStoryService,
){

    suspend fun uploadStory (
        imageFile: File,
        description: String
    ) : Result<ErrorResponse> {
        return withContext(Dispatchers.IO) {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            try {
                val errorResponse = apiStoryService.uploadStory(file = multipartBody, description = requestBody)
                return@withContext Result.Success(errorResponse)
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    suspend fun getStories () : Result<StoriesResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val storiesResponse = apiStoryService.getStories()
                return@withContext Result.Success(storiesResponse)
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    suspend fun getStoryDetail (storyId: String) : Result<StoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val storyDetailResponse = apiStoryService.getStoryDetail(storyId)
                return@withContext Result.Success(storyDetailResponse)
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiStoryService: ApiStoryService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiStoryService)
            }.also { instance = it }
    }
}