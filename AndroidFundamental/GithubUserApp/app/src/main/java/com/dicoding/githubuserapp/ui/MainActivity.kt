package com.dicoding.githubuserapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager

        val listUserAdapter = ListUserAdapter(mutableListOf())
        listUserAdapter.submitList(mutableListOf())
        binding.rvMain.adapter = listUserAdapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        supportActionBar?.hide()

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView
            .editText
            .setOnEditorActionListener { _, _, _ ->
                binding.searchView.hide()
                mainViewModel.getUser(binding.searchView.text.toString())
                false
            }

        mainViewModel.user.observe(this) { users ->
            for (user in users) {
                mainViewModel.getUserDetails(user.login)
            }
        }

        mainViewModel.userDetail.observe(this) {userDetail ->
            setUserData(userDetail)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.topAppBar.setNavigationOnClickListener { recreate() }
    }

    private fun setUserData(listUser: List<UserDetailResponse?>) {
        val listUserAdapter = ListUserAdapter(listUser)
        listUserAdapter.submitList(listUser)
        binding.rvMain.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserDetailResponse?) {
                showUserDetails(data)
            }
        })

    }

    private fun showUserDetails(data: UserDetailResponse?) {
        val userDetailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
        userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, data)
        startActivity(userDetailIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}