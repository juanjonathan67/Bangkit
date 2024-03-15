package com.dicoding.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.response.FollowResponse
import com.dicoding.githubuserapp.data.response.FollowResponseItem
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding

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
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollower.layoutManager = layoutManager

        userDetailViewModel.userFollowers.observe(viewLifecycleOwner) {userFollowers ->
            for (follower in userFollowers) {
                userDetailViewModel.getUserFollowerDetails(follower.login)
            }
        }

        userDetailViewModel.followersDetail.observe(viewLifecycleOwner) {followerDetails ->
            setUserFollowers(followerDetails)
        }
    }

    private fun setUserFollowers(userFollowers: List<UserDetailResponse?>) {
        val followerAdapter = FollowAdapter(userFollowers)
        followerAdapter.submitList(userFollowers)
        binding.rvFollower.adapter = followerAdapter

        followerAdapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserDetailResponse?) {
                showUserDetails(data)
            }
        })
    }

    private fun showUserDetails(data: UserDetailResponse?) {
        requireActivity().intent.putExtra(UserDetailActivity.EXTRA_USER, data)
        requireActivity().recreate()
    }

}