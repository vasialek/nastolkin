package com.nastolkin.games.arkhamhorror

enum class JournalSection { INVESTIGATORS, NOTES, RETIRED, LOCATIONS, PERIODS, RESIDENTS }

data class CampaignDefinition(val id: String, val sections: List<JournalSection>)
