package com.dicoding.myappbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.myappbar.databinding.ActivityMenuBinding
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView
            .editText
            .setOnEditorActionListener { textView, actionId, event ->
                binding.searchView.hide()
                Toast.makeText(this@MenuActivity, binding.searchView.text, Toast.LENGTH_SHORT).show()
                false
            }

    }
}