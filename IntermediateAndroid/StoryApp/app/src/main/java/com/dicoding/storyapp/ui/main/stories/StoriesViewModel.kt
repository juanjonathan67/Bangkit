package com.dicoding.storyapp.ui.main.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import kotlinx.coroutines.launch

class StoriesViewModel (private val repository: Repository) : ViewModel() {
    fun getStories () : LiveData<Result<StoriesResponse>> {
        val result: MutableLiveData<Result<StoriesResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = repository.getStories()
        }
        return result
    }

}