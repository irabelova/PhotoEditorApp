package com.example.photoeditorapp.editPhoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditorapp.databinding.SavingMenuFragmentBinding

class SavingMenuFragment: Fragment() {
    private lateinit var binding: SavingMenuFragmentBinding
    private val viewModel: EditPhotoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = SavingMenuFragmentBinding.inflate(inflater, container, false)
        binding.exportButton.setOnClickListener {
            viewModel.save(false)
        }
        binding.shareButton.setOnClickListener {
           viewModel.shareFile()
        }
        return binding.root
    }
}
