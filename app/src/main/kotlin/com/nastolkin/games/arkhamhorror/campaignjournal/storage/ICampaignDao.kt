package com.nastolkin.games.arkhamhorror.campaignjournal.storage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ICampaignDao {
    @Query("SELECT * FROM campaign_journals WHERE campaignId = :campaignId")
    suspend fun load(campaignId: String): CampaignRecord?

    @Upsert
    suspend fun save(record: CampaignRecord)
}
