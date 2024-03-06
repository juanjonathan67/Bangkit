package com.dicoding.proyekakhirbeginnerandroid

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class DetailActivity : AppCompatActivity() {
    private lateinit var imageDetailView: ImageView
    private lateinit var textDetailTitle: TextView
    private lateinit var textDetailLocation: TextView
    private lateinit var textDetailRating: TextView
    private lateinit var textDetailDescription: TextView

    companion object {
        const val EXTRA_TOUR = "extra_tour"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageDetailView = findViewById(R.id.image_detail_view)
        textDetailTitle = findViewById(R.id.text_detail_title)
        textDetailLocation = findViewById(R.id.text_detail_location)
        textDetailRating = findViewById(R.id.text_detail_rating)
        textDetailDescription = findViewById(R.id.text_detail_description)

        val tour = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Tour>(EXTRA_TOUR, Tour::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Tour>(EXTRA_TOUR)
        }

        if (tour != null) {
            Glide.with(this)
                .load(tour.image)
                .into(imageDetailView)

            textDetailTitle.text = tour.title
            textDetailLocation.text = tour.location
            textDetailRating.text = tour.rating
            textDetailDescription.text = tour.description
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_menu, menu)
        supportActionBar?.title = "Favorite Tourism"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.greenPrimary)))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_page -> {
                val aboutActivityIntent = Intent(this@DetailActivity, AboutActivity::class.java)
                startActivity(aboutActivityIntent)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}