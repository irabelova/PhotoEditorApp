package com.example.photoeditorapp.editPhoto

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditorapp.R
import com.example.photoeditorapp.databinding.EditPhotoFragmentBinding
import com.example.photoeditorapp.editPhoto.drawing.DrawingFragment
import com.example.photoeditorapp.editPhoto.filters.FilterFragment
import com.example.photoeditorapp.editPhoto.text.TextFragment
import com.example.photoeditorapp.utils.showConfirmGoBackDialog
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType


class EditPhotoFragment : Fragment() {
    private lateinit var binding: EditPhotoFragmentBinding
    private lateinit var photoEditor: PhotoEditor
    private val viewModel: EditPhotoViewModel by viewModels(ownerProducer = { this }) {
        EditPhotoViewModel.EditPhotoFactory(requireArguments().getParcelable(URI_KEY)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EditPhotoFragmentBinding.inflate(inflater, container, false)

        binding.photoEditorView.source.setImageURI(requireArguments().getParcelable(URI_KEY)!!)

        binding.photoEditorView.source.scaleType = ImageView.ScaleType.FIT_START
        photoEditor = PhotoEditor.Builder(requireContext(), binding.photoEditorView)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .build()
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        getCancelItem().setOnMenuItemClickListener {
            viewModel.cancelChanges()
            true
        }
        getApplyItem().setOnMenuItemClickListener {
            viewModel.applyChanges()
            true
        }

        getCancelLastChangeItem().setOnMenuItemClickListener {
            viewModel.undo()
            true
        }

        getSaveItem().setOnMenuItemClickListener {
            replaceFragment(SavingMenuFragment())
            viewModel.setEditType(EditType.SAVE)
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
                viewModel.incrementChanges()
            }

            override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
            }

            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
            }

            override fun onStartViewChangeListener(viewType: ViewType?) {
            }

            override fun onStopViewChangeListener(viewType: ViewType?) {
            }

            override fun onTouchSourceImage(event: MotionEvent?) {
            }
        })
        viewModel.editType.observe(viewLifecycleOwner) {
            when (it) {
                EditType.COMMON -> {
                    photoEditor.setBrushDrawingMode(false)
                    photoEditor.clearHelperBox()
                    replaceFragment(EditMenuFragment())
                    getApplyItem().isVisible = false
                    getCancelItem().isVisible = false
                    getCancelLastChangeItem().isVisible = false
                    if (viewModel.isShowSaveButton()) {
                        getSaveItem().isVisible = true
                    }
                }

                EditType.FILTER -> {
                    photoEditor.setBrushDrawingMode(false)
                    getApplyItem().isVisible = true
                    getCancelItem().isVisible = true
                    getSaveItem().isVisible = false
                    getCancelLastChangeItem().isVisible = false
                    replaceFragment(FilterFragment())
                }
                EditType.DRAW -> {
                    photoEditor.setBrushDrawingMode(true)
                    getApplyItem().isVisible = true
                    getCancelItem().isVisible = true
                    getSaveItem().isVisible = false
                    getCancelLastChangeItem().isVisible = true
                    replaceFragment(DrawingFragment.newInstance(viewModel.drawingOptions.value))
                }
                EditType.TEXT -> {
                    photoEditor.setBrushDrawingMode(false)
                    getApplyItem().isVisible = true
                    getCancelItem().isVisible = true
                    getSaveItem().isVisible = false
                    getCancelLastChangeItem().isVisible = false
                    replaceFragment(TextFragment())
                }
                EditType.SAVE -> {
                    photoEditor.setBrushDrawingMode(false)
                    getApplyItem().isVisible = false
                    getCancelItem().isVisible = false
                    getSaveItem().isVisible = false
                    getCancelLastChangeItem().isVisible = false
                    replaceFragment(SavingMenuFragment())
                }
                else -> {}
            }
        }
        viewModel.filter.observe(viewLifecycleOwner) {
            photoEditor.setFilterEffect(it)
        }
        viewModel.drawingOptions.observe(viewLifecycleOwner) {
            val shapeBuilder = ShapeBuilder()
                .withShapeType(ShapeType.Brush)
                .withShapeSize(it.brushSize)
                .withShapeColor(it.color)
            photoEditor.setShape(shapeBuilder)
        }
        viewModel.undo.observe(viewLifecycleOwner) {
            for (item: Int in 0 until it) {
                photoEditor.undo()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.editType.value != EditType.SAVE) {
                        showConfirmGoBackDialog(
                            onConfirm = {
                                isEnabled = false
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }
                        )
                    } else {
                        replaceFragment(EditMenuFragment())
                        viewModel.setEditType(EditType.COMMON)
                    }
                }
            })
        viewModel.textOptions.observe(viewLifecycleOwner) {
            val textStyleBuilder = TextStyleBuilder()
            textStyleBuilder.withTextSize(it.size)
            textStyleBuilder.withTextColor(it.color)
            photoEditor.addText(it.text, textStyleBuilder)
        }
        val saveSettings: SaveSettings = SaveSettings.Builder()
            .setClearViewsEnabled(false)
            .setTransparencyEnabled(true)
            .setCompressFormat(Bitmap.CompressFormat.PNG)
            .build()
        viewModel.saveFileEvent.observe(viewLifecycleOwner) {
            photoEditor.saveAsFile(it.file.absolutePath, saveSettings, object : PhotoEditor.OnSaveListener{
                override fun onFailure(exception: Exception) {
                    Log.e("SAVING", "Saving failed", exception)
                    viewModel.fileSavingFailed()
                }

                override fun onSuccess(imagePath: String) {
                    Log.d("SAVING", "Success")
                    if(it.share) {
                        viewModel.shareFile()
                    }else {
                        // TODO: Show toost
                    }
                }
            })
        }

        viewModel.shareFileEvent.observe(viewLifecycleOwner) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "image/*"
            intent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.android.fileprovider",
                    it
                )
            )
            val shareIntent = Intent.createChooser(intent, null)
            try {
                startActivity(shareIntent)
            }catch (e: ActivityNotFoundException) {
                Log.e("SAVING", "No activity to share", e)
                // TODO: Show toast
            }
        }
    }

    private fun getCancelItem() = getToolbarItem(R.id.cancel_changes)
    private fun getApplyItem() = getToolbarItem(R.id.apply_changes)
    private fun getCancelLastChangeItem() = getToolbarItem(R.id.cancel_last_change)

    private fun getSaveItem() = getToolbarItem(R.id.save_changes)
    private fun getToolbarItem(id: Int): MenuItem =
        binding.toolbar.menu.findItem(id)


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