package com.nastolkin.games.arkhamhorror

import com.nastolkin.games.arkhamhorror.campaignjournal.*
import com.nastolkin.games.arkhamhorror.campaigns.hemlockvale.HemlockRepositoryMemory
import kotlin.test.*

class CampaignJournalTest {
    private val editor = CampaignJournalEditor()

    @Test
    fun `both campaigns include standard sections and only Hemlock adds custom sections`() {
        val campaigns = CampaignRepositoryMemory().getCampaigns()
        assertEquals(listOf("hemlock-vale", "drowned-city"), campaigns.map { it.id })
        assertEquals(JournalSection.entries.toList(), campaigns[0].sections)
        assertEquals(listOf(JournalSection.INVESTIGATORS, JournalSection.NOTES, JournalSection.RETIRED), campaigns[1].sections)
    }

    @Test
    fun `new journals contain no invented investigators`() {
        assertTrue(CampaignJournal("hemlock-vale").investigators.isEmpty())
    }

    @Test
    fun `only four active investigators are allowed but retired records are retained`() {
        var journal = CampaignJournal("hemlock-vale")
        repeat(4) { journal = editor.addInvestigator(journal, "Player $it", "Investigator $it") }
        assertFailsWith<JournalValidationException> { editor.addInvestigator(journal, "Fifth", "New") }
        journal = editor.retireInvestigator(journal, journal.investigators.first().id, InvestigatorStatus.KILLED)
        journal = editor.addInvestigator(journal, "Fifth", "New")
        assertEquals(5, journal.investigators.size)
        assertEquals(4, journal.activeInvestigators.size)
        assertEquals(InvestigatorStatus.KILLED, journal.investigators.first().status)
    }

    @Test
    fun `empty and duplicate investigator identities are rejected including retired ones`() {
        val journal = editor.addInvestigator(CampaignJournal("hemlock-vale"), "Player", "  Roland Banks ")
        assertFailsWith<JournalValidationException> { editor.addInvestigator(journal, "Player", " ") }
        val retired = editor.retireInvestigator(journal, journal.investigators.first().id, InvestigatorStatus.INSANE)
        assertFailsWith<JournalValidationException> { editor.addInvestigator(retired, "Other", "roland banks") }
    }

    @Test
    fun `experience and trauma cannot become negative and story cards are preserved`() {
        val journal = editor.addInvestigator(CampaignJournal("hemlock-vale"), "Player", "Roland")
        val investigator = journal.investigators.first().copy(experience = -1, physicalTrauma = -2, mentalTrauma = 3, storyCards = "Story asset")
        val updated = editor.updateInvestigator(journal, investigator).investigators.first()
        assertEquals(0, updated.experience)
        assertEquals(0, updated.physicalTrauma)
        assertEquals(3, updated.mentalTrauma)
        assertEquals("Story asset", updated.storyCards)
    }

    @Test
    fun `trust can increase and decrease but stays within zero and six`() {
        val repository = HemlockRepositoryMemory()
        val resident = repository.getResidents().first()
        var journal = CampaignJournal("hemlock-vale")
        repeat(10) { journal = editor.changeTrust(journal, resident.id, 1) }
        assertEquals(6, journal.residentProgress(resident.id).trust)
        journal = editor.changeTrust(journal, resident.id, -1)
        assertEquals(5, journal.residentProgress(resident.id).trust)
        repeat(10) { journal = editor.changeTrust(journal, resident.id, -1) }
        assertEquals(0, journal.residentProgress(resident.id).trust)
    }

    @Test
    fun `resident notes and trust are independent and shared across investigators`() {
        var journal = CampaignJournal("hemlock-vale")
        journal = editor.setResidentNotes(journal, "rachel", "An event")
        journal = editor.changeTrust(journal, "rachel", 1)
        assertEquals(ResidentProgress(1, "An event"), journal.residentProgress("rachel"))
        assertEquals(ResidentProgress(), journal.residentProgress("leah"))
    }

    @Test
    fun `location and day flags can be checked and unchecked independently`() {
        var journal = CampaignJournal("hemlock-vale")
        journal = editor.setFlag(journal, "location:north-point", true)
        journal = editor.setFlag(journal, "day:1", true)
        journal = editor.setFlag(journal, "night:1", true)
        journal = editor.setFlag(journal, "day:1", false)
        assertEquals(setOf("location:north-point", "night:1"), journal.flags)
    }

    @Test
    fun `Hemlock catalog preserves all locations pages and residents`() {
        val repository = HemlockRepositoryMemory()
        assertEquals(listOf(10, 14, 19, 23, 27, 34, 46), repository.getLocations().map { it.page })
        assertEquals(listOf("north-point", "hemlock-harbor", "pearl-ridge", "akuan-shore", "eastwick", "western-woods", "southern-fields"), repository.getLocations().map { it.id })
        assertEquals(listOf("rachel", "leah", "simeon", "william", "river", "gideon", "judith", "theo"), repository.getResidents().map { it.id })
    }
}
