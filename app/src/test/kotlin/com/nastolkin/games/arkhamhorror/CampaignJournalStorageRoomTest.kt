package com.nastolkin.games.arkhamhorror

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nastolkin.games.arkhamhorror.campaignjournal.*
import com.nastolkin.games.arkhamhorror.campaignjournal.storage.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class CampaignJournalStorageRoomTest {
    @Test
    fun allJournalFieldsSurviveDatabaseReopeningAndCampaignsStaySeparate() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val name = "journal-test.db"
        context.deleteDatabase(name)
        var database = Room.databaseBuilder(context, CampaignDatabase::class.java, name).build()
        var storage = CampaignJournalStorageRoom(database)
        val editor = CampaignJournalEditor()
        var journal = editor.addInvestigator(CampaignJournal("hemlock-vale"), "Игрок", "Роланд")
        journal = journal.copy(notes = "Заметка", flags = setOf("day:1", "location:north-point"))
        journal = editor.updateInvestigator(journal, journal.investigators.first().copy(experience = 7, physicalTrauma = 1, mentalTrauma = 2, storyCards = "Актив"))
        journal = editor.retireInvestigator(journal, journal.investigators.first().id, InvestigatorStatus.INSANE)
        journal = editor.setResidentNotes(editor.changeTrust(journal, "rachel", 1), "rachel", "Событие")
        storage.save(journal)
        storage.save(CampaignJournal("drowned-city", notes = "Другой журнал"))
        database.close()
        database = Room.databaseBuilder(context, CampaignDatabase::class.java, name).build()
        storage = CampaignJournalStorageRoom(database)
        try {
            assertEquals(journal, storage.load("hemlock-vale"))
            assertEquals("Другой журнал", storage.load("drowned-city").notes)
            assertTrue(storage.load("drowned-city").investigators.isEmpty())
        } finally {
            database.close()
            context.deleteDatabase(name)
        }
    }
}
