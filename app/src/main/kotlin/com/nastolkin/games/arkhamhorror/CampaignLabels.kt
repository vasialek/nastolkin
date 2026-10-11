package com.nastolkin.games.arkhamhorror

import androidx.annotation.StringRes
import com.nastolkin.R

@StringRes
fun campaignTitle(id: String): Int = when (id) {
    "hemlock-vale" -> R.string.campaign_hemlock
    "drowned-city" -> R.string.campaign_drowned
    else -> error("Unknown campaign: $id")
}

@StringRes
fun sectionTitle(section: JournalSection): Int = when (section) {
    JournalSection.INVESTIGATORS -> R.string.investigators
    JournalSection.NOTES -> R.string.campaign_notes
    JournalSection.RETIRED -> R.string.retired_investigators
    JournalSection.LOCATIONS -> R.string.explored_locations
    JournalSection.PERIODS -> R.string.days_and_nights
    JournalSection.RESIDENTS -> R.string.residents
}
