package com.example.photoeditorapp.mainMenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditorapp.editPhoto.EditPhotoFragment
import com.example.photoeditorapp.R
import com.example.photoeditorapp.databinding.MainMenuFragmentBinding

class MainMenuFragment: Fragment() {
    private lateinit var binding: MainMenuFragmentBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private val viewModel: MainMenuViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setUri(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = MainMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.makePhotoButton.setOnClickListener {

        }
        binding.photoFromDeviceButton.setOnClickListener {
            addImage()
        }
        viewModel.uri.observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, EditPhotoFragment.newInstance(it))
                .commit()
        }
    }

    private fun addImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}