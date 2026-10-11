package com.nastolkin.games.arkhamhorror.campaignjournal.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaign_journals")
data class CampaignRecord(@PrimaryKey val campaignId: String, val payload: String)
