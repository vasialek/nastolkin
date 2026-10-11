package com.nastolkin.games.arkhamhorror

import com.nastolkin.games.arkhamhorror.campaignjournal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CampaignJournalViewModelTest {
    @BeforeTest fun prepareDispatcher() { Dispatchers.setMain(StandardTestDispatcher()) }
    @AfterTest fun restoreDispatcher() { Dispatchers.resetMain() }

    @Test
    fun `loads saved investigators and saves the latest rapid edits`() = runTest {
        val storage = CampaignJournalStorageMemory()
        storage.journals["hemlock-vale"] = CampaignJournalEditor().addInvestigator(CampaignJournal("hemlock-vale"), "Player", "Roland")
        val viewModel = CampaignJournalViewModel("hemlock-vale", storage)
        runCurrent()
        assertEquals("Roland", viewModel.state.value.journal!!.activeInvestigators.single().identity)
        viewModel.setNotes("First")
        viewModel.setNotes("Latest")
        runCurrent()
        assertEquals("Latest", storage.journals.getValue("hemlock-vale").notes)
        assertEquals(SaveStatus.SAVED, viewModel.state.value.saveStatus)
    }

    @Test
    fun `failed save preserves edits and can be retried`() = runTest {
        val storage = CampaignJournalStorageMemory()
        val viewModel = CampaignJournalViewModel("hemlock-vale", storage)
        runCurrent()
        storage.failWrites = true
        viewModel.setNotes("Keep this")
        runCurrent()
        assertEquals(SaveStatus.FAILED, viewModel.state.value.saveStatus)
        assertEquals("Keep this", viewModel.state.value.journal!!.notes)
        storage.failWrites = false
        viewModel.retry()
        runCurrent()
        assertEquals("Keep this", storage.journals.getValue("hemlock-vale").notes)
    }

    @Test
    fun `campaigns save independently and validation does not change records`() = runTest {
        val storage = CampaignJournalStorageMemory()
        val first = CampaignJournalViewModel("hemlock-vale", storage)
        val second = CampaignJournalViewModel("drowned-city", storage)
        runCurrent()
        first.setNotes("Hemlock")
        second.setNotes("City")
        first.addInvestigator("Player", " ")
        runCurrent()
        assertEquals(JournalError.IDENTITY_REQUIRED, first.state.value.error)
        assertEquals("Hemlock", storage.journals.getValue("hemlock-vale").notes)
        assertEquals("City", storage.journals.getValue("drowned-city").notes)
    }

    @Test
    fun `failed load preserves diagnostic and retry never overwrites existing data`() = runTest {
        val storage = CampaignJournalStorageMemory()
        storage.journals["hemlock-vale"] = CampaignJournal("hemlock-vale", notes = "Existing notes")
        storage.failReads = true
        val viewModel = CampaignJournalViewModel("hemlock-vale", storage)
        runCurrent()
        assertNull(viewModel.state.value.journal)
        assertEquals(SaveStatus.FAILED, viewModel.state.value.saveStatus)
        assertEquals("Read unavailable", viewModel.state.value.failure?.message)
        viewModel.setNotes("Must not overwrite")
        storage.failReads = false
        viewModel.retry()
        runCurrent()
        assertEquals("Existing notes", viewModel.state.value.journal!!.notes)
        assertNull(viewModel.state.value.failure)
    }

    private class CampaignJournalStorageMemory : ICampaignJournalStorage {
        val journals = mutableMapOf<String, CampaignJournal>()
        var failWrites = false
        var failReads = false
        override suspend fun load(campaignId: String): CampaignJournal {
            if (failReads) { throw IllegalStateException("Read unavailable") }
            return journals[campaignId] ?: CampaignJournal(campaignId)
        }
        override suspend fun save(journal: CampaignJournal) {
            if (failWrites) { throw IllegalStateException("Storage unavailable") }
            journals[journal.campaignId] = journal
        }
    }
}
