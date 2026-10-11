package com.nastolkin.games.arkhamhorror.campaignjournal.storage

import com.nastolkin.games.arkhamhorror.campaignjournal.CampaignJournal
import com.nastolkin.games.arkhamhorror.campaignjournal.ICampaignJournalStorage
import kotlinx.serialization.json.Json

class CampaignJournalStorageRoom(database: CampaignDatabase) : ICampaignJournalStorage {
    private val campaignDao = database.campaignDao()
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun load(campaignId: String): CampaignJournal {
        val record = campaignDao.load(campaignId) ?: return CampaignJournal(campaignId)
        val journal = json.decodeFromString<CampaignJournal>(record.payload)
        check(journal.campaignId == campaignId)
        return journal
    }

    override suspend fun save(journal: CampaignJournal) {
        campaignDao.save(CampaignRecord(journal.campaignId, json.encodeToString(journal)))
    }
}
