package com.dicoding.storyapp.ui.main.storyDetail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.FragmentStoryDetailBinding
import com.dicoding.storyapp.ui.landing.LandingActivity
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.ViewModelFactory
import com.dicoding.storyapp.utils.datastore
import com.dicoding.storyapp.utils.parseTimeInstant
import kotlinx.coroutines.runBlocking
import java.time.Instant

class StoryDetailFragment : Fragment() {
    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding!!
    private val storiesViewModel by viewModels<StoryDetailViewModel> { ViewModelFactory.getStoryInstance(requireContext()) }
    private val args: StoryDetailFragmentArgs by navArgs()
    private lateinit var prefs : UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_detail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = UserPreferences.getInstance(requireContext().datastore)

//        val storyId = args.storyId ?: ""

        val storyId = arguments?.getString("story_id") ?: ""

        storiesViewModel.getStoryDetail(storyId).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Error -> {
                        showToast(result.error)
                        binding.pbDetail.visibility = View.GONE
                    }
                    Result.Loading -> {
                        binding.pbDetail.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        Glide.with(requireContext())
                            .load(result.data.story.photoUrl)
                            .into(binding.ivDetailPhoto)

                        binding.tvDetailName.text = result.data.story.name
                        binding.tvDetailCreated.text = parseTimeInstant(result.data.story.createdAt ?: Instant.now().toString(), resources.configuration.locales.get(0))
                        binding.tvDetailDescription.text = result.data.story.description
                        binding.pbDetail.visibility = View.GONE
                    }
                }
            }
        }

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

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARGS_KEY = "STORY_ID"
    }

}