package com.nastolkin.games.arkhamhorror.campaignjournal

import java.util.UUID

class CampaignJournalEditor {
    fun addInvestigator(journal: CampaignJournal, playerName: String, identity: String): CampaignJournal {
        if (journal.activeInvestigators.size >= 4) {
            throw JournalValidationException(JournalError.PARTY_FULL)
        }
        ensureIdentity(journal, identity)
        val investigator = Investigator(UUID.randomUUID().toString(), playerName.trim(), identity.trim())
        return journal.copy(investigators = journal.investigators + investigator)
    }

    fun updateInvestigator(journal: CampaignJournal, investigator: Investigator): CampaignJournal {
        val existing = journal.investigators.first { it.id == investigator.id }
        ensureIdentity(journal, investigator.identity, investigator.id)
        val normalized = investigator.copy(identity = investigator.identity.trim(),
            experience = investigator.experience.coerceAtLeast(0), physicalTrauma = investigator.physicalTrauma.coerceAtLeast(0),
            mentalTrauma = investigator.mentalTrauma.coerceAtLeast(0), status = existing.status)
        return journal.copy(investigators = journal.investigators.map { if (it.id == normalized.id) normalized else it })
    }

    fun retireInvestigator(journal: CampaignJournal, id: String, status: InvestigatorStatus): CampaignJournal {
        require(status != InvestigatorStatus.ACTIVE)
        return journal.copy(investigators = journal.investigators.map {
            if (it.id == id && it.status == InvestigatorStatus.ACTIVE) it.copy(status = status) else it
        })
    }

    fun changeTrust(journal: CampaignJournal, residentId: String, delta: Int): CampaignJournal {
        val progress = journal.residentProgress(residentId)
        val updated = progress.copy(trust = (progress.trust.toLong() + delta).coerceIn(0, 6).toInt())
        return journal.copy(residents = journal.residents + (residentId to updated))
    }

    fun setResidentNotes(journal: CampaignJournal, residentId: String, notes: String): CampaignJournal {
        val updated = journal.residentProgress(residentId).copy(notes = notes)
        return journal.copy(residents = journal.residents + (residentId to updated))
    }

    fun setFlag(journal: CampaignJournal, key: String, checked: Boolean): CampaignJournal {
        return journal.copy(flags = if (checked) journal.flags + key else journal.flags - key)
    }

    private fun ensureIdentity(journal: CampaignJournal, identity: String, excludedId: String? = null) {
        if (identity.isBlank()) {
            throw JournalValidationException(JournalError.IDENTITY_REQUIRED)
        }
        val duplicate = journal.investigators.any { it.id != excludedId && it.identity.equals(identity.trim(), ignoreCase = true) }
        if (duplicate) {
            throw JournalValidationException(JournalError.DUPLICATE_IDENTITY)
        }
    }
}
