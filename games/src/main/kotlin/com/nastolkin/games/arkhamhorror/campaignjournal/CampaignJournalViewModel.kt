package com.nastolkin.games.arkhamhorror.campaignjournal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CampaignJournalViewModel(
    private val campaignId: String,
    private val campaignJournalStorage: ICampaignJournalStorage
) : ViewModel() {
    private val editor = CampaignJournalEditor()
    private val mutableState = MutableStateFlow(JournalState())
    private val pendingSaves = Channel<CampaignJournal>(Channel.CONFLATED)
    val state = mutableState.asStateFlow()

    init {
        load()
        viewModelScope.launch {
            for (journal in pendingSaves) { persist(journal) }
        }
    }

    fun setNotes(notes: String) = edit { it.copy(notes = notes) }
    fun addInvestigator(playerName: String, identity: String) = edit { editor.addInvestigator(it, playerName, identity) }
    fun updateInvestigator(investigator: Investigator) = edit { editor.updateInvestigator(it, investigator) }
    fun retireInvestigator(id: String, status: InvestigatorStatus) = edit { editor.retireInvestigator(it, id, status) }
    fun setFlag(key: String, checked: Boolean) = edit { editor.setFlag(it, key, checked) }
    fun changeTrust(residentId: String, delta: Int) = edit { editor.changeTrust(it, residentId, delta) }
    fun setResidentNotes(residentId: String, notes: String) = edit { editor.setResidentNotes(it, residentId, notes) }
    fun clearError() { mutableState.value = mutableState.value.copy(error = null) }

    fun retry() {
        val journal = state.value.journal
        if (journal == null) {
            load()
        } else {
            mutableState.value = state.value.copy(saveStatus = SaveStatus.SAVING)
            pendingSaves.trySend(journal)
        }
    }

    private fun edit(transform: (CampaignJournal) -> CampaignJournal) {
        val journal = state.value.journal ?: return
        try {
            val updated = transform(journal)
            mutableState.value = JournalState(updated, SaveStatus.SAVING)
            pendingSaves.trySend(updated)
        } catch (exception: JournalValidationException) {
            mutableState.value = state.value.copy(error = exception.reason)
        }
    }

    private fun load() {
        mutableState.value = state.value.copy(saveStatus = SaveStatus.LOADING)
        viewModelScope.launch {
            try {
                mutableState.value = JournalState(campaignJournalStorage.load(campaignId), SaveStatus.SAVED)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                mutableState.value = state.value.copy(saveStatus = SaveStatus.FAILED, failure = exception)
            }
        }
    }

    private suspend fun persist(journal: CampaignJournal) {
        try {
            campaignJournalStorage.save(journal)
            if (state.value.journal == journal) {
                mutableState.value = state.value.copy(saveStatus = SaveStatus.SAVED, failure = null)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            mutableState.value = state.value.copy(saveStatus = SaveStatus.FAILED, failure = exception)
        }
    }
}
