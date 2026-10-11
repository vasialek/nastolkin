package com.nastolkin.games.arkhamhorror

class CampaignRepositoryMemory : ICampaignRepository {
    override fun getCampaigns(): List<CampaignDefinition> {
        val standard = listOf(JournalSection.INVESTIGATORS, JournalSection.NOTES, JournalSection.RETIRED)
        return listOf(
            CampaignDefinition("hemlock-vale", standard + listOf(JournalSection.LOCATIONS, JournalSection.PERIODS, JournalSection.RESIDENTS)),
            CampaignDefinition("drowned-city", standard)
        )
    }
}
