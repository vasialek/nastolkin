package com.nastolkin.games.arkhamhorror

interface ICampaignRepository {
    fun getCampaigns(): List<CampaignDefinition>
}
