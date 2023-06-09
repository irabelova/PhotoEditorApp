package com.example.photoeditorapp.editPhoto


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.photoeditorapp.databinding.EditMenuFragmentBinding


class EditMenuFragment: Fragment() {
    private lateinit var binding: EditMenuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EditMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}