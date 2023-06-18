package com.example.photoeditorapp.editPhoto

import java.io.File

data class SavingModel(
    val file: File,
    val share: Boolean
)