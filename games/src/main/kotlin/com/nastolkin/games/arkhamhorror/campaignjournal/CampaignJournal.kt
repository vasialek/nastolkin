package com.nastolkin.games.arkhamhorror.campaignjournal

import kotlinx.serialization.Serializable

@Serializable
data class CampaignJournal(
    val campaignId: String,
    val investigators: List<Investigator> = emptyList(),
    val notes: String = "",
    val flags: Set<String> = emptySet(),
    val residents: Map<String, ResidentProgress> = emptyMap()
) {
    val activeInvestigators: List<Investigator> get() = investigators.filter { it.status == InvestigatorStatus.ACTIVE }
    fun residentProgress(id: String): ResidentProgress = residents[id] ?: ResidentProgress()
}

@Serializable
data class Investigator(
    val id: String,
    val playerName: String,
    val identity: String,
    val experience: Int = 0,
    val physicalTrauma: Int = 0,
    val mentalTrauma: Int = 0,
    val storyCards: String = "",
    val status: InvestigatorStatus = InvestigatorStatus.ACTIVE
)

@Serializable
enum class InvestigatorStatus { ACTIVE, KILLED, INSANE }

@Serializable
data class ResidentProgress(val trust: Int = 0, val notes: String = "")
