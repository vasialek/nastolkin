package com.nastolkin.games.arkhamhorror.campaignjournal

interface ICampaignJournalStorage {
    suspend fun load(campaignId: String): CampaignJournal
    suspend fun save(journal: CampaignJournal)
}
