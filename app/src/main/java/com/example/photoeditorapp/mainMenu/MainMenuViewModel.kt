package com.example.photoeditorapp.mainMenu

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photoeditorapp.SingleLiveEvent

class MainMenuViewModel: ViewModel() {

    private val _uri = SingleLiveEvent<Uri>()
    val uri: LiveData<Uri> = _uri

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

}