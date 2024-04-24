package com.dicoding.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.utils.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel (private val repository: Repository) : ViewModel() {

    fun register (
        name: String,
        email: String,
        password: String
    ) : LiveData<Result<ErrorResponse>> {
        val result: MutableLiveData<Result<ErrorResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = repository.register(name, email, password)
        }
        return result
    }

    fun login (
        email: String,
        password: String
    ) : LiveData<Result<LoginResponse>> {
        val result: MutableLiveData<Result<LoginResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = repository.login(email, password)
        }
        return result
    }

    fun getStories () : LiveData<Result<StoriesResponse>> {
        val result: MutableLiveData<Result<StoriesResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = repository.getStories()
        }
        return result
    }

}