package com.example.photoeditorapp.mainMenu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditorapp.R
import com.example.photoeditorapp.databinding.MainMenuFragmentBinding
import com.example.photoeditorapp.editPhoto.EditPhotoFragment
import com.example.photoeditorapp.utils.createImageFile
import java.io.IOException


class MainMenuFragment : Fragment() {
    private lateinit var binding: MainMenuFragmentBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var pickImageFromCamera: ActivityResultLauncher<Uri>
    private lateinit var  requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private val viewModel: MainMenuViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setUri(uri)
            }
        }
        pickImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                viewModel.confirmTmpUri()
            }
        }

        requestCameraPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    startCamera()
                }else {
                    // TODO: Show toast 
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
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle(R.string.cancel)
                    builder.setMessage(R.string.alert_dialog_message)
                    builder.setIcon(R.drawable.ic_cancel_last_change)
                    builder.setPositiveButton(R.string.yes) { dialog, _ ->
                        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                        intent.data = Uri.parse("package:com.example.photoeditorapp");
                        startActivity(intent)
                        dialog.cancel()
                    }
                    builder.setNegativeButton(R.string.no) { dialog, _ ->
                        dialog.cancel()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(true)
                    alertDialog.show()

                }
                else -> {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

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
    
    private fun startCamera() {
        getPhotoFileUri()?.let {
            viewModel.setTmpUri(it)
            pickImageFromCamera.launch(it)
        }
    }

    private fun getPhotoFileUri(): Uri? {
        return try {
            val file = createImageFile(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            FileProvider.getUriForFile(
                requireContext(),
                "com.example.android.fileprovider",
                file
            )
        } catch (ex: IOException) {
            Log.e("File Creation", "can't create file", ex)
            null
        }


    }

}