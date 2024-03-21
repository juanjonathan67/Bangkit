package com.dicoding.githubuserapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.response.FollowResponseItem
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _userFollowers = MutableLiveData<List<FollowResponseItem>>()
    val userFollowers: LiveData<List<FollowResponseItem>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<FollowResponseItem>>()
    val userFollowing: LiveData<List<FollowResponseItem>> = _userFollowing

    private val _followersDetail = MutableLiveData<List<UserDetailResponse?>>()
    val followersDetail: LiveData<List<UserDetailResponse?>> = _followersDetail

    private val _followingDetail = MutableLiveData<List<UserDetailResponse?>>()
    val followingDetail: LiveData<List<UserDetailResponse?>> = _followingDetail

    private var listTempFollower: MutableList<UserDetailResponse?> = mutableListOf()
    private var listTempFollowing: MutableList<UserDetailResponse?> = mutableListOf()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "UserDetailViewModel"
    }

    fun getUserDetails(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetails(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listTempFollower = mutableListOf()
                    _followersDetail.postValue(listTempFollower.toList())
                    listTempFollowing = mutableListOf()
                    _followingDetail.postValue(listTempFollowing.toList())
                    _userDetail.postValue(response.body())
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.postValue(response.body())
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.postValue(response.body())
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowerDetails(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetails(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listTempFollower.add(response.body())
                    _followersDetail.postValue(listTempFollower.toList())
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowingDetails(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetails(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    listTempFollowing.add(response.body())
                    _followingDetail.postValue(listTempFollowing.toList())
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}