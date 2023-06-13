package com.example.photoeditorapp.editPhoto.filters

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ja.burhanrashid52.photoeditor.PhotoFilter

data class FilterModel (
    val filter: PhotoFilter,
    @StringRes val title: Int,
    @DrawableRes val image: Int
        )