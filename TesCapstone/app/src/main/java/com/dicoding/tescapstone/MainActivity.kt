package com.dicoding.tescapstone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.tescapstone.databinding.ActivityMainBinding
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btWAtext.setOnClickListener {v ->
            val text = "HALO HALO BANDUNG"
            val phoneNumber = "+6283807456704"
            val uri = "https://api.whatsapp.com/send?phone=${phoneNumber}&text=${URLEncoder.encode(text, "UTF-8")}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(uri))
            startActivity(intent)
        }

    }
}