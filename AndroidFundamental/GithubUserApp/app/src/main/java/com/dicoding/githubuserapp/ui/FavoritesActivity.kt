package com.dicoding.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.layoutManager = layoutManager

        supportActionBar?.hide()

        binding.topAppBar.setNavigationOnClickListener { finish() }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    val mainActivityIntent = Intent(this, MainActivity::class.java)
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(mainActivityIntent)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}