package com.dicoding.storyapp.data

import android.net.Uri
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val prefs: UserPreferences
){

    suspend fun register (
        name: String,
        email: String,
        password: String
    ) : Result<ErrorResponse> {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext Result.Success(apiService.register(name, email, password))
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    suspend fun login (
        email: String,
        password: String
    ) : Result<LoginResponse> {

        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(email, password)
                prefs.saveUserToken(loginResponse.loginResult.token)
                return@withContext Result.Success(loginResponse)
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    suspend fun uploadStoryGuest (
        imageFile: File,
        description: String
    ) : Result<ErrorResponse> {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        return withContext(Dispatchers.IO) {
            try {
                val errorResponse = apiService.uploadStory(file = multipartBody, description = requestBody)
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
                val storiesResponse = apiService.getStories()
                return@withContext Result.Success(storiesResponse)
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            apiService: ApiService,
            prefs: UserPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, prefs)
            }.also { instance = it }
    }
}