package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import retrofit2.HttpException

class Repository private constructor(
    private val apiService: ApiService
){

    suspend fun register (
        name: String,
        email: String,
        password: String
    ) : Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            val registerRequest =
                FormBody.Builder()
                    .add("name", name)
                    .add("email", email)
                    .add("password", password)
                    .build()

            try {
                return@withContext Result.Success(apiService.register(registerRequest))
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    suspend fun login (
        email: String,
        password: String
    ) : Result<LoginResponse> {

        return withContext(Dispatchers.IO) {
            val loginRequest =
                FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build()

            try {
                val loginResponse = apiService.login(loginRequest)
                return@withContext Result.Success(loginResponse)
            } catch (e : HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                return@withContext Result.Error(errorResponse.message)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}