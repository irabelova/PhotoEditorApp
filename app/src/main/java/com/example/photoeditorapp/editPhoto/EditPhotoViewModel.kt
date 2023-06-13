package com.example.photoeditorapp.editPhoto

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ja.burhanrashid52.photoeditor.PhotoFilter

class EditPhotoViewModel(val selectedUri: Uri): ViewModel() {
    private val _editType = MutableLiveData(EditType.COMMON)
    val editType: LiveData<EditType> = _editType

    private val _uri = MutableLiveData<Uri>(selectedUri)
    val uri: LiveData<Uri> = _uri

    private val _filter = MutableLiveData<PhotoFilter>()
    val filter: LiveData<PhotoFilter> = _filter

    fun setFilter(photoFilter: PhotoFilter) {
        _filter.value = photoFilter
    }

    fun setEditType(type: EditType) {
        _editType.value = type
    }


    class EditPhotoFactory(private val uri: Uri) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditPhotoViewModel(uri) as T

        }
    }
}