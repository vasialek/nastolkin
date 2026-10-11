package com.nastolkin.games.arkhamhorror.campaigns.hemlockvale

import com.nastolkin.R

fun residentTitle(id: String): Int = when (id) {
    "rachel" -> R.string.resident_rachel
    "leah" -> R.string.resident_leah
    "simeon" -> R.string.resident_simeon
    "william" -> R.string.resident_william
    "river" -> R.string.resident_river
    "gideon" -> R.string.resident_gideon
    "judith" -> R.string.resident_judith
    "theo" -> R.string.resident_theo
    else -> error("Unknown resident: $id")
}

fun locationTitle(id: String): Int = when (id) {
    "north-point" -> R.string.location_north_point
    "hemlock-harbor" -> R.string.location_hemlock_harbor
    "pearl-ridge" -> R.string.location_pearl_ridge
    "akuan-shore" -> R.string.location_akuan_shore
    "eastwick" -> R.string.location_eastwick
    "western-woods" -> R.string.location_western_woods
    "southern-fields" -> R.string.location_southern_fields
    else -> error("Unknown location: $id")
}

fun scenarioTitle(id: String): Int = when (id) {
    "north-point" -> R.string.scenario_north_point
    "hemlock-harbor" -> R.string.scenario_hemlock_harbor
    "pearl-ridge" -> R.string.scenario_pearl_ridge
    "akuan-shore" -> R.string.scenario_akuan_shore
    "eastwick" -> R.string.scenario_eastwick
    "western-woods" -> R.string.scenario_western_woods
    "southern-fields" -> R.string.scenario_southern_fields
    else -> error("Unknown location: $id")
}
