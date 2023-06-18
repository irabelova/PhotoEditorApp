package com.example.photoeditorapp.mainMenu

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photoeditorapp.utils.SingleLiveEvent

class MainMenuViewModel: ViewModel() {

    private val _uri = SingleLiveEvent<Uri>()
    val uri: LiveData<Uri> = _uri

    private var tmpUri: Uri? = null

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun setTmpUri(uri: Uri) {
        tmpUri = uri
    }

    fun confirmTmpUri() {
        tmpUri?.let { _uri.value = it }
    }

}