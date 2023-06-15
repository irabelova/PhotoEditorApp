package com.example.photoeditorapp.editPhoto

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditorapp.utils.SingleLiveEvent
import com.example.photoeditorapp.editPhoto.drawing.DrawingOptions
import com.example.photoeditorapp.editPhoto.text.TextModel
import ja.burhanrashid52.photoeditor.PhotoFilter

class EditPhotoViewModel(val selectedUri: Uri) : ViewModel() {
    private val _editType = MutableLiveData(EditType.COMMON)
    val editType: LiveData<EditType> = _editType

    private val _uri = MutableLiveData(selectedUri)
    val uri: LiveData<Uri> = _uri

    private val _filter = MutableLiveData<PhotoFilter>()
    val filter: LiveData<PhotoFilter> = _filter

    private val _drawingOptions = MutableLiveData(DrawingOptions())
    val drawingOptions: LiveData<DrawingOptions> = _drawingOptions

    private val _textOptions = MutableLiveData<TextModel>()
    val textOptions: LiveData<TextModel> = _textOptions

    private var textModel = TextModel()

    private val _undo = SingleLiveEvent<Int>()
    val undo: LiveData<Int> = _undo

    private var changeCount = 0
    private var filterChanged = false

    fun setFilter(photoFilter: PhotoFilter) {
        filterChanged = true
        _filter.value = photoFilter
    }

    fun changeBrushColor(color: Int) {
        _drawingOptions.value = drawingOptions.value!!.copy(color = color)
    }

    fun changeBrushSize(size: Int) {
        _drawingOptions.value = drawingOptions.value!!.copy(brushSize = size.toFloat())
    }

    fun changeTextColor(color: Int) {
        textModel = textModel.copy(color = color)
    }

    fun changeTextSize(size: Int) {
        textModel = textModel.copy(size = (size + 15).toFloat())
    }

    fun changeText(text: String) {
        textModel = textModel.copy(text = text)
    }

    fun confirmTextChange() {
        _textOptions.value = textModel.copy()
    }

    fun setEditType(type: EditType) {
        _editType.value = type
    }

    fun applyChanges() {
        if(editType.value == EditType.TEXT && textModel.text.isNotEmpty()) {
            confirmTextChange()
        }
        filterChanged = false
        changeCount = 0
        _editType.value = EditType.COMMON
    }

    fun incrementChangeCount() {
        changeCount++
    }

    fun undo() {
        if(changeCount > 0) {
            changeCount--
            _undo.value = 1
        }

    }

    fun cancelChanges() {
        when (editType.value) {
            EditType.FILTER -> {
                if(filterChanged) {
                    setFilter(PhotoFilter.NONE)
                }
            }
            EditType.TEXT,
            EditType.DRAW  -> {
                _undo.value = changeCount
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