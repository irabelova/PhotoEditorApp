package com.example.photoeditorapp.editPhoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditPhotoViewModel: ViewModel() {
    private val _editType = MutableLiveData(EditType.COMMON)
    val editType: LiveData<EditType> = _editType


}