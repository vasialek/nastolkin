package com.nastolkin.shared.ui

import androidx.annotation.StringRes
import com.nastolkin.R

enum class ThemeMode(@get:StringRes val labelResource: Int) {
    SYSTEM(R.string.theme_system),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark)
}
