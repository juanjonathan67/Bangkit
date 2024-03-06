package com.dicoding.proyekakhirbeginnerandroid

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SplashActivity : AppCompatActivity() {
    private lateinit var recyclerTours: RecyclerView
    private val listTour = ArrayList<Tour>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_splash)

        recyclerTours = findViewById(R.id.recycler_tours)
        recyclerTours.setHasFixedSize(true)

        listTour.addAll(getListTours())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_menu, menu)
        supportActionBar?.title = "Favorite Tourism"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.greenPrimary)))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_page -> {
                val aboutActivityIntent = Intent(this@SplashActivity, AboutActivity::class.java)
                startActivity(aboutActivityIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getListTours(): ArrayList<Tour> {
        val tourTitle = resources.getStringArray(R.array.tour_title)
        val tourDescription = resources.getStringArray(R.array.tour_description)
        val tourLocation = resources.getStringArray(R.array.tour_location)
        val tourRating = resources.getStringArray(R.array.tour_rating)
        val tourPhoto = resources.getStringArray(R.array.tour_photo)
        val listTour = ArrayList<Tour>()
        for (i in tourTitle.indices) {
            val tour = Tour(tourTitle[i], tourDescription[i], tourLocation[i], tourRating[i], tourPhoto[i])
            listTour.add(tour)
        }
        return listTour
    }

    private fun showRecyclerList() {
        recyclerTours.layoutManager = LinearLayoutManager(this)
        val listTourAdapter = ListTourAdapter(listTour)
        recyclerTours.adapter = listTourAdapter

        listTourAdapter.setOnItemClickCallback(object : ListTourAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Tour) {
                showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(tour: Tour) {
        val detailActivityIntent = Intent(this@SplashActivity, DetailActivity::class.java)
        detailActivityIntent.putExtra(DetailActivity.EXTRA_TOUR, tour)
        startActivity(detailActivityIntent)
    }
}