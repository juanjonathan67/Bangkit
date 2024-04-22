package com.dicoding.storyapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.login("juanjonathan67@gmail.com", "123Juan123;'").observe(this) { result ->
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