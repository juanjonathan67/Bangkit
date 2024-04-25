package com.dicoding.storyapp.ui.main.stories

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.FragmentStoriesBinding
import com.dicoding.storyapp.ui.landing.LandingActivity
import com.dicoding.storyapp.ui.main.storyDetail.StoryDetailFragment
import com.dicoding.storyapp.ui.main.storyDetail.StoryDetailFragmentArgs
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.ViewModelFactory
import com.dicoding.storyapp.utils.datastore
import kotlinx.coroutines.runBlocking

class StoriesFragment : Fragment() {
    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!
    private val storiesViewModel by viewModels<StoriesViewModel> { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var prefs : UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = UserPreferences.getInstance(requireContext().datastore)

        setStories(emptyList())

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.language_settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.action_logout -> {
                    runBlocking {
                        prefs.deleteUserToken()
                    }
                    val landingIntent = Intent(requireActivity(), LandingActivity::class.java)
                    landingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(landingIntent)
                    true
                }
                else -> {
                    false
                }
            }
        }

        storiesViewModel.getStories().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Error -> {
                        showToast(result.error)
                        binding.pbLogin.visibility = View.GONE
                    }
                    Result.Loading -> {
                        binding.pbLogin.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        setStories(result.data.listStory)
                        binding.pbLogin.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setStories(storyList: List<ListStoryItem>) {
        val storiesAdapter = StoriesAdapter()
        storiesAdapter.submitList(storyList)
        binding.rvStories.adapter = storiesAdapter
        binding.rvStories.layoutManager = LinearLayoutManager(requireContext())

        storiesAdapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(story: ListStoryItem) {
                val action = StoriesFragmentDirections.actionStoriesFragmentToStoryDetailFragment(storyId = story.id)
                findNavController().navigate(action)
//                val storyDetailFragment = StoryDetailFragment()
//                val args = Bundle()
//                args.putString(StoryDetailFragment.ARGS_KEY, story.id)
//                storyDetailFragment.arguments = args
//                parentFragmentManager.beginTransaction().add(R.id.mainFragmentContainer, storyDetailFragment).commit()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}