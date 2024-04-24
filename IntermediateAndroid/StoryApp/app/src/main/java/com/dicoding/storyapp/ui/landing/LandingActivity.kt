package com.dicoding.storyapp.ui.landing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityLandingBinding
import com.dicoding.storyapp.ui.main.MainActivity
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.datastore
import kotlinx.coroutines.flow.first

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    private lateinit var prefs : UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = UserPreferences.getInstance(this.datastore)
//        if (prefs.getUserToken().first() != null) startActivity(this@LandingActivity, MainActivity::class.java)
    }
}