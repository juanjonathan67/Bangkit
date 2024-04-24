package com.dicoding.storyapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.ViewModelFactory
import com.dicoding.storyapp.utils.datastore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var prefs : UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.getStories().observe(this@MainActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLog("Loading Now")
                    }
                    is Result.Error -> {
                        showLog(result.error)
                    }
                    is Result.Success -> {
                        showLog(result.data.toString())
                    }
                }
            }
        }
    }

    private fun showLog(text: String) {
        Log.d("Testing Coroutine", text)
    }
}