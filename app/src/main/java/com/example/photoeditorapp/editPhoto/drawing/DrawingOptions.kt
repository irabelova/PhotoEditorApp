package com.example.photoeditorapp.editPhoto.drawing

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrawingOptions(
    @ColorInt val color: Int = Color.BLACK,
    val brushSize: Float = 4f
): Parcelable
