package com.nastolkin.games.arkhamhorror.campaignjournal

enum class SaveStatus { LOADING, SAVING, SAVED, FAILED }
enum class JournalError { IDENTITY_REQUIRED, DUPLICATE_IDENTITY, PARTY_FULL }

class JournalValidationException(val reason: JournalError) : IllegalArgumentException(reason.name)

data class JournalState(
    val journal: CampaignJournal? = null,
    val saveStatus: SaveStatus = SaveStatus.LOADING,
    val error: JournalError? = null,
    val failure: Throwable? = null
)
