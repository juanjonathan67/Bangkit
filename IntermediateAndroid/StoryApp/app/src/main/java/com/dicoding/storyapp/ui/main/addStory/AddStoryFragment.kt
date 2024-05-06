package com.dicoding.storyapp.ui.main.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.FragmentAddStoryBinding
import com.dicoding.storyapp.ui.landing.LandingActivity
import com.dicoding.storyapp.ui.main.CameraActivity
import com.dicoding.storyapp.ui.main.CameraActivity.Companion.CAMERAX_RESULT
import com.dicoding.storyapp.utils.UserPreferences
import com.dicoding.storyapp.utils.ViewModelFactory
import com.dicoding.storyapp.utils.datastore
import com.dicoding.storyapp.utils.uriToFile
import com.dicoding.storyapp.utils.urlToFile
import kotlinx.coroutines.runBlocking
import java.net.URL

class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private val addStoryViewModel by viewModels<AddStoryViewModel> { ViewModelFactory.getStoryInstance(requireContext()) }
    private var currentImageUri: String? = null
    private var isUrl = false
    private lateinit var policy : ThreadPolicy
    private lateinit var prefs : UserPreferences

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri.toString()
            Glide.with(requireContext())
                .load(currentImageUri)
                .into(binding.ivImageSelected)
            isUrl = false
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE) ?: ""
            Glide.with(requireContext())
                .load(currentImageUri)
                .into(binding.ivImageSelected)
            isUrl = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        prefs = UserPreferences.getInstance(requireContext().datastore)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.rgImageMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLocalImage -> {
                    binding.tfImageUrl.visibility = View.GONE
                    binding.llLocalButtons.visibility = View.VISIBLE
                }
                R.id.rbImageUrl -> {
                    binding.llLocalButtons.visibility = View.GONE
                    binding.tfImageUrl.visibility = View.VISIBLE
                }
            }
        }

        binding.btChooseImage.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btTakePicture.setOnClickListener {
            val cameraXIntent = Intent(requireActivity(), CameraActivity::class.java)
            launcherIntentCameraX.launch(cameraXIntent)
        }

        binding.tfImageUrl.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                currentImageUri = binding.tfImageUrl.editText?.text.toString()
                Glide.with(requireContext())
                    .load(currentImageUri)
                    .into(binding.ivImageSelected)
                isUrl = true
            }
        }

        binding.buttonAdd.setOnClickListener {
            if (currentImageUri != null) {
                val file = if (isUrl) {
                    urlToFile(URL(currentImageUri), requireContext())
                } else {
                    uriToFile(currentImageUri!!.toUri(), requireContext())
                }
                addStoryViewModel.uploadStory(
                    file,
                    binding.tfStoryDescription.editText?.text.toString()
                ).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Error -> {
                                binding.pbAddStory.visibility = View.GONE
                                showToast(result.error)
                            }
                            Result.Loading -> {
                                binding.pbAddStory.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.pbAddStory.visibility = View.GONE
                                showToast(result.data.message)
                                findNavController().popBackStack()
                            }
                        }
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
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}