package com.example.photoeditorapp.editPhoto.filters

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.photoeditorapp.R
import ja.burhanrashid52.photoeditor.PhotoFilter

data class FilterModel(
    val filter: PhotoFilter,
    @StringRes val title: Int,
    @DrawableRes val image: Int
)

val filters = listOf(
    FilterModel(PhotoFilter.AUTO_FIX, R.string.auto_fix, R.drawable.auto_fix),
    FilterModel(PhotoFilter.GRAY_SCALE, R.string.black_white, R.drawable.gray_scale),
    FilterModel(PhotoFilter.BRIGHTNESS, R.string.brightness, R.drawable.brightness),
    FilterModel(PhotoFilter.CONTRAST, R.string.contrast, R.drawable.contrast),
    FilterModel(PhotoFilter.DOCUMENTARY, R.string.documentary, R.drawable.documentary),
    FilterModel(PhotoFilter.FILL_LIGHT, R.string.fill_light, R.drawable.fill_light),
    FilterModel(PhotoFilter.FISH_EYE, R.string.fish_eye, R.drawable.fish_eye),
    FilterModel(PhotoFilter.GRAIN, R.string.grain, R.drawable.grain),
    FilterModel(PhotoFilter.NEGATIVE, R.string.negative, R.drawable.negative),
    FilterModel(PhotoFilter.POSTERIZE, R.string.posterize, R.drawable.posterize),
    FilterModel(PhotoFilter.SATURATE, R.string.saturate, R.drawable.saturate),
    FilterModel(PhotoFilter.SEPIA, R.string.sepia, R.drawable.sepia),
    FilterModel(PhotoFilter.TEMPERATURE, R.string.temperature, R.drawable.temperature),
    FilterModel(PhotoFilter.TINT, R.string.tint, R.drawable.tint),
    FilterModel(PhotoFilter.VIGNETTE, R.string.vignette, R.drawable.vignette)
)
