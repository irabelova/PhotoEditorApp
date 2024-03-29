package com.example.photoeditorapp.editPhoto.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditorapp.databinding.FilterFragmentBinding
import com.example.photoeditorapp.editPhoto.EditPhotoViewModel


class FilterFragment : Fragment() {

    private lateinit var binding: FilterFragmentBinding
    private val viewModel: EditPhotoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FilterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = FilterAdapter (filters) {
            viewModel.setFilter(it.filter)
        }
        binding.filtersListView.adapter = adapter
    }
}