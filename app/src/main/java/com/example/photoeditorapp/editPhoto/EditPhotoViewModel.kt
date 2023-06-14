package com.example.photoeditorapp.editPhoto

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditorapp.SingleLiveEvent
import com.example.photoeditorapp.editPhoto.drawing.DrawingOptions
import ja.burhanrashid52.photoeditor.PhotoFilter

class EditPhotoViewModel(val selectedUri: Uri) : ViewModel() {
    private val _editType = MutableLiveData(EditType.COMMON)
    val editType: LiveData<EditType> = _editType

    private val _uri = MutableLiveData<Uri>(selectedUri)
    val uri: LiveData<Uri> = _uri

    private val _filter = MutableLiveData<PhotoFilter>()
    val filter: LiveData<PhotoFilter> = _filter

    private val _drawingOptions = MutableLiveData(DrawingOptions())
    val drawingOptions: LiveData<DrawingOptions> = _drawingOptions

    private val _undoAll = SingleLiveEvent<Int>()
    val undoAll: LiveData<Int> = _undoAll

    private var changeCount = 0

    fun setFilter(photoFilter: PhotoFilter) {
        _filter.value = photoFilter
    }

    fun changeBrushColor(color: Int) {
        _drawingOptions.value = drawingOptions.value!!.copy(color = color)
    }

    fun changeBrushSize(size: Int) {
        _drawingOptions.value = drawingOptions.value!!.copy(brushSize = size.toFloat())
    }

    fun setEditType(type: EditType) {
        _editType.value = type
    }

    fun applyChanges() {
        _editType.value = EditType.COMMON
    }

    fun incrementChangeCount() {
        changeCount++
    }

    fun decrementChangeCount() {
        changeCount--
    }

    fun cancelChanges() {
        when (editType.value) {
            EditType.FILTER -> setFilter(PhotoFilter.NONE)
            EditType.DRAW -> {
                _undoAll.value = changeCount
                changeCount = 0
            }
            else -> {}
        }
        _editType.value = EditType.COMMON
    }


    class EditPhotoFactory(private val uri: Uri) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditPhotoViewModel(uri) as T

        }
    }
}