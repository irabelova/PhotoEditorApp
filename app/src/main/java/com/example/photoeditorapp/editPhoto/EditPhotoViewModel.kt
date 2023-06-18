package com.example.photoeditorapp.editPhoto

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditorapp.utils.SingleLiveEvent
import com.example.photoeditorapp.editPhoto.drawing.DrawingOptions
import com.example.photoeditorapp.editPhoto.text.TextModel
import com.example.photoeditorapp.utils.getFilename
import ja.burhanrashid52.photoeditor.PhotoFilter
import java.io.File

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

    private val _saveFileEvent = SingleLiveEvent<SavingModel>()
    val saveFileEvent: LiveData<SavingModel> = _saveFileEvent

    private val _shareFileEvent = SingleLiveEvent<File>()
    val shareFileEvent: LiveData<File> = _shareFileEvent

    private var changeCount = 0
    private var filterChanged = false
    private var changesAmount = 0
    private var savedFile: File? = null

    fun setFilter(photoFilter: PhotoFilter) {
        filterChanged = true
        changesAmount++
        _filter.value = photoFilter
    }

    fun isShowSaveButton(): Boolean {
        return changesAmount>0
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

    fun incrementChanges() {
        changeCount++
        changesAmount++
    }

    fun undo() {
        if(changeCount > 0) {
            changeCount--
            changesAmount--
            _undo.value = 1
        }

    }

    fun cancelChanges() {
        when (editType.value) {
            EditType.FILTER -> {
                if(filterChanged) {
                    setFilter(PhotoFilter.NONE)
                    changesAmount--
                }
            }
            EditType.TEXT,
            EditType.DRAW  -> {
                _undo.value = changeCount
                changesAmount -= changeCount
                changeCount = 0

            }
            else -> {}
        }
        _editType.value = EditType.COMMON
    }

    fun save(share: Boolean) {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() +
                    File.separator + "PhotoEditor"
            val parentFile = File(path)
            if(!parentFile.exists()) {
                parentFile.mkdirs()
            }
            val fileName = getFilename()
            savedFile = File(parentFile, fileName)
            _saveFileEvent.value = SavingModel(savedFile!!, share)
    }

    fun fileSavingFailed() {
        savedFile = null
    }

    fun shareFile() {
        if(savedFile == null) {
            save(true)
        } else {
            _shareFileEvent.value = savedFile!!
        }
    }


    class EditPhotoFactory(private val uri: Uri) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditPhotoViewModel(uri) as T

        }
    }
}