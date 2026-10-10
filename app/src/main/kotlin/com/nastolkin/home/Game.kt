package com.nastolkin.home

import androidx.annotation.StringRes
import com.nastolkin.R

enum class Game(@get:StringRes val titleResource: Int, @get:StringRes val descriptionResource: Int) {
    ZAKHVAT_KOLONIY(R.string.zakhvat_koloniy, R.string.random_city)
}
