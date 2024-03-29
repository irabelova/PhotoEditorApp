package com.example.photoeditorapp.editPhoto


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditorapp.databinding.EditMenuFragmentBinding


class EditMenuFragment: Fragment() {
    private lateinit var binding: EditMenuFragmentBinding
    private val viewModel: EditPhotoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EditMenuFragmentBinding.inflate(inflater, container, false)
        binding.setFilterButton.setOnClickListener {
            viewModel.setEditType(EditType.FILTER)
        }
        binding.drawButton.setOnClickListener {
            viewModel.setEditType(EditType.DRAW)
        }
        binding.textButton.setOnClickListener {
            viewModel.setEditType(EditType.TEXT)
        }
        return binding.root
    }
}