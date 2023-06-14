package com.example.photoeditorapp.editPhoto.drawing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.photoeditorapp.databinding.DrawingFragmentBinding
import com.example.photoeditorapp.editPhoto.EditPhotoViewModel

class DrawingFragment : Fragment() {
    private lateinit var binding: DrawingFragmentBinding
    private val viewModel: EditPhotoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DrawingFragmentBinding.inflate(inflater, container, false)
        val options: DrawingOptions? = arguments?.getParcelable(OPTIONS_KEY)
        if(options != null) {
            binding.brushSize.progress = options.brushSize.toInt()
            binding.colorSeekBar.color = options.color
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.colorSeekBar.setOnColorChangeListener { _, color ->
            viewModel.changeBrushColor(color)
        }
        binding.brushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                viewModel.changeBrushSize(value)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    companion object {
        private const val OPTIONS_KEY = "OPTIONS_KEY"
        fun newInstance(options: DrawingOptions?): DrawingFragment {
            val fragment = DrawingFragment()
            fragment.arguments = bundleOf(
                OPTIONS_KEY to options
            )
            return fragment
        }
    }
}