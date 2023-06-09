package com.example.photoeditorapp.editPhoto

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.photoeditorapp.R
import com.example.photoeditorapp.databinding.EditPhotoFragmentBinding
import ja.burhanrashid52.photoeditor.PhotoEditor

class EditPhotoFragment : Fragment() {
    private lateinit var binding: EditPhotoFragmentBinding
    private lateinit var photoEditor: PhotoEditor
    private val viewModel: EditPhotoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EditPhotoFragmentBinding.inflate(inflater, container, false)
        val uri = requireArguments().getParcelable<Uri>(URI_KEY)
        binding.photoEditorView.source.setImageURI(uri)
        binding.photoEditorView.source.scaleType = ImageView.ScaleType.FIT_START
        photoEditor = PhotoEditor.Builder(requireContext(), binding.photoEditorView)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .build()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.editType.observe(viewLifecycleOwner) {
            when(it) {
                EditType.COMMON -> replaceFragment(EditMenuFragment())
                else -> {}
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.edit_options, fragment)
            .commit()
    }

    companion object {
        private const val URI_KEY = "URI_KEY"
        fun newInstance(uri: Uri): EditPhotoFragment {
            val editPhotoFragment = EditPhotoFragment()
            editPhotoFragment.arguments = bundleOf(
                URI_KEY to uri
            )
            return editPhotoFragment
        }
    }
}