package com.dicoding.storyapp.ui.main.storyDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.remote.response.StoryResponse
import kotlinx.coroutines.launch

class StoryDetailViewModel (private val repository: Repository) : ViewModel() {
    fun getStoryDetail (storyId: String) : LiveData<Result<StoryResponse>> {
        val result: MutableLiveData<Result<StoryResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = repository.getStoryDetail(storyId)
        }
        return result
    }

}