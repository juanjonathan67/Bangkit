package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.datastore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val prefs = UserPreferences.getInstance(context.datastore)
        val token = runBlocking { prefs.getUserToken().first() }
        val apiService = ApiConfig.getApiService(token)
        return Repository.getInstance(apiService, prefs)
    }
}