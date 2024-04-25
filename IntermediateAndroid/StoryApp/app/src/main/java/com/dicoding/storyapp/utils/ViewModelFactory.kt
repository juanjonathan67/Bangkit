package com.dicoding.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.Repository
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.ui.landing.login.LoginViewModel
import com.dicoding.storyapp.ui.landing.register.RegisterViewModel
import com.dicoding.storyapp.ui.main.stories.StoriesViewModel
import com.dicoding.storyapp.ui.main.storyDetail.StoryDetailViewModel

class ViewModelFactory private constructor (private val repository: Repository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        LoginViewModel::class.java -> LoginViewModel(repository)
        RegisterViewModel::class.java -> RegisterViewModel(repository)
        StoriesViewModel::class.java -> StoriesViewModel(repository)
        StoryDetailViewModel::class.java -> StoryDetailViewModel(repository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}