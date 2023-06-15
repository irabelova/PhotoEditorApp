package com.example.photoeditorapp.editPhoto.text

import android.graphics.Color
import androidx.annotation.ColorInt

data class TextModel(
    val text: String = "",
    @ColorInt val color: Int = Color.BLACK,
    val size: Float = 20f
)