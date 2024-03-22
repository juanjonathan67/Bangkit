package com.dicoding.githubuserapp.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var sectionsPagerAdapter: FollowPagerAdapter
    private lateinit var userDetailViewModel: UserDetailViewModel

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserDetailViewModel::class.java]

        sectionsPagerAdapter = FollowPagerAdapter(this)
        binding.vpFollow.adapter = sectionsPagerAdapter

        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, UserDetailResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        }

        if (user != null) {
            userDetailViewModel.getUserDetails(user.login)
        }

        userDetailViewModel.userDetail.observe(this) { userDetail ->
            setUserDetail(userDetail)
            userDetailViewModel.getUserFollowers(userDetail.login)
            userDetailViewModel.getUserFollowing(userDetail.login)
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

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
                R.id.menu_favorite -> {
                    val favoriteActivityIntent = Intent(this, FavoritesActivity::class.java)
                    startActivity(favoriteActivityIntent)
                    true
                }
                else -> {
                    false
                }
            }
        }
        supportActionBar?.elevation = 0f
    }


    private fun setUserDetail(userDetail: UserDetailResponse) {
        Glide.with(this)
            .load(userDetail.avatarUrl)
            .into(binding.imgUserProfile)
        binding.tvFullName.text = userDetail.name
        binding.tvUserName.text = userDetail.login

        val followArr: Array<Int> = arrayOf(userDetail.followers, userDetail.following)
        TabLayoutMediator(binding.tabs, binding.vpFollow) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position]) + " (" + followArr[position] + ")"
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}