package com.nastolkin.games.arkhamhorror

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.nastolkin.games.arkhamhorror.campaignjournal.*
import com.nastolkin.shared.ui.NastolkinTheme
import com.nastolkin.shared.ui.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "en-rUS-w411dp-h891dp")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CampaignScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun newCampaignUsesSharedJournalAndOnlySavedInvestigatorsAppear() {
        showApp()
        composeRule.onNodeWithText("The Drowned City").performClick()
        composeRule.onNodeWithText("No investigators yet").assertIsDisplayed()
        composeRule.onNodeWithText("Add investigator").performClick()
        composeRule.onNodeWithTag("player-name").performTextInput("Alice")
        composeRule.onNodeWithTag("investigator-identity").performTextInput("Roland")
        composeRule.onNodeWithText("Save").performClick()
        composeRule.onNodeWithText("Roland").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Choose section").performClick()
        composeRule.onNodeWithText("Residents").assertDoesNotExist()
        composeRule.onNodeWithText("Notes").performClick()
        composeRule.onNodeWithTag("campaign-notes").performTextInput("A city note")
        composeRule.onNodeWithContentDescription("Choose campaign").performClick()
        composeRule.onNodeWithText("The Feast of Hemlock Vale").performClick()
        composeRule.onNodeWithText("No investigators yet").assertIsDisplayed()
    }

    @Test
    fun residentsStartCollapsedAndTrustHasAccessibleBounds() {
        showApp()
        composeRule.onNodeWithText("The Feast of Hemlock Vale").performClick()
        composeRule.onNodeWithContentDescription("Choose section").performClick()
        composeRule.onNodeWithText("Residents").performClick()
        composeRule.onNodeWithTag("resident-notes-rachel").assertDoesNotExist()
        composeRule.onNodeWithTag("trust-minus-rachel").assertIsNotEnabled()
        composeRule.onNodeWithTag("trust-plus-rachel").assertWidthIsAtLeast(48.dp).assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithTag("trust-minus-rachel").assertWidthIsAtLeast(48.dp).assertHeightIsAtLeast(48.dp)
        repeat(6) { composeRule.onNodeWithTag("trust-plus-rachel").performClick() }
        composeRule.onNodeWithTag("trust-plus-rachel").assertIsNotEnabled()
        composeRule.onNodeWithTag("trust-minus-rachel").performClick()
        composeRule.onNodeWithTag("trust-plus-rachel").assertIsEnabled()
        composeRule.onNodeWithText("Mother Rachel").performClick()
        composeRule.onNodeWithTag("resident-notes-rachel").performTextInput("A promise")
        composeRule.onNodeWithTag("resident-toggle-rachel").performClick()
        composeRule.onNodeWithTag("resident-notes-rachel").assertDoesNotExist()
        composeRule.onNodeWithTag("resident-toggle-rachel").performClick()
        composeRule.onNodeWithTag("resident-notes-rachel").assertTextContains("A promise")
    }

    private fun showApp() {
        val storage = CampaignJournalStorageMemory()
        composeRule.setContent { NastolkinTheme(ThemeMode.LIGHT) { ArkhamScreen(storage) } }
    }

    private class CampaignJournalStorageMemory : ICampaignJournalStorage {
        private val journals = mutableMapOf<String, CampaignJournal>()
        override suspend fun load(campaignId: String) = journals[campaignId] ?: CampaignJournal(campaignId)
        override suspend fun save(journal: CampaignJournal) { journals[journal.campaignId] = journal }
    }
}
