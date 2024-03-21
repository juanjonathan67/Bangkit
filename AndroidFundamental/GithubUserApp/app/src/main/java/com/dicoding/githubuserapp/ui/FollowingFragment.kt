package com.dicoding.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userDetailViewModel = ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowing.layoutManager = layoutManager

        userDetailViewModel.userFollowing.observe(viewLifecycleOwner) {userFollowing ->
            for (following in userFollowing) {
                userDetailViewModel.getUserFollowingDetails(following.login)
            }
        }

        userDetailViewModel.followingDetail.observe(viewLifecycleOwner) {userFollowers ->
            setUserFollowing(userFollowers)
        }
    }

    private fun setUserFollowing(userFollowing: List<UserDetailResponse?>) {
        val followingAdapter = FollowAdapter()
        followingAdapter.submitList(userFollowing)
        binding.rvFollowing.adapter = followingAdapter

        followingAdapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserDetailResponse?) {
                showUserDetails(data)
            }
        })
    }

    private fun showUserDetails(data: UserDetailResponse?) {
        val userDetailIntent = Intent(requireActivity(), UserDetailActivity::class.java)
        userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, data)
        startActivity(userDetailIntent)
    }
}