package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}