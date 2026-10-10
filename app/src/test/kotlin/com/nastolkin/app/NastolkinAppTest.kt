package com.nastolkin.app

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nastolkin.games.zakhvatkoloniy.City
import com.nastolkin.games.zakhvatkoloniy.CityViewModel
import com.nastolkin.games.zakhvatkoloniy.IRandomCityProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "en-rUS")
class NastolkinAppTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun homeListsAvailableGameAndApkVersion() {
        showApp()

        composeRule.onNode(hasText("Board games") and hasAnyAncestor(hasTestTag("game-list"))).assertIsDisplayed()
        composeRule.onNode(hasText("Захват Колоний") and hasAnyAncestor(hasTestTag("game-list"))).assertIsDisplayed()
        composeRule.onNodeWithText("v0.1.0").assertIsDisplayed()
    }

    @Test
    fun openingGameSelectingAndTogglingCity() {
        showApp()
        composeRule.onNode(hasText("Захват Колоний") and hasAnyAncestor(hasTestTag("game-list"))).performClick()
        composeRule.onNodeWithText("No city selected").assertIsDisplayed()

        composeRule.onNodeWithText("Choose city").performClick()
        composeRule.onNodeWithText("Кокосовые острова").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Hide city").performClick()
        composeRule.onNodeWithText("City hidden").assertIsDisplayed()
        composeRule.onNodeWithText("Кокосовые острова").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Show city").performClick()
        composeRule.onNodeWithText("Кокосовые острова").assertIsDisplayed()
    }

    @Test
    fun selectingAgainRevealsHiddenCity() {
        showApp()
        composeRule.onNode(hasText("Захват Колоний") and hasAnyAncestor(hasTestTag("game-list"))).performClick()
        composeRule.onNodeWithText("Choose city").performClick()
        composeRule.onNodeWithContentDescription("Hide city").performClick()

        composeRule.onNodeWithText("Choose city").performClick()

        composeRule.onNodeWithText("Кокосовые острова").assertIsDisplayed()
    }

    @Test
    fun drawerSwitchesScreensAndThemesWithoutLosingSelection() {
        showApp()
        composeRule.onNodeWithContentDescription("Open game menu").performClick()
        composeRule.onNodeWithText("Dark").performClick()
        composeRule.onNodeWithText("Dark").assertIsSelected()
        composeRule.onNodeWithText("Light").performClick()
        composeRule.onNodeWithText("Light").assertIsSelected()
        composeRule.onNodeWithText("System").performClick()
        composeRule.onNodeWithText("System").assertIsSelected()
        composeRule.onNodeWithContentDescription("Open Захват Колоний").performClick()
        composeRule.onNodeWithText("Choose city").performClick()
        composeRule.onNodeWithContentDescription("Open game menu").performClick()
        composeRule.onNodeWithText("All games").performClick()
        composeRule.onNode(hasText("Board games") and hasAnyAncestor(hasTestTag("game-list"))).assertIsDisplayed()
        composeRule.onNode(hasText("Захват Колоний") and hasAnyAncestor(hasTestTag("game-list"))).performClick()

        composeRule.onNodeWithText("Кокосовые острова").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "ru-rRU")
    fun russianInterfacePreservesHistoricalName() {
        showApp()
        composeRule.onNode(hasText("Захват Колоний") and hasAnyAncestor(hasTestTag("game-list"))).performClick()
        composeRule.onNodeWithText("Выбрать город").performClick()

        composeRule.onNodeWithText("Кокосовые острова").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Скрыть город").assertIsDisplayed()
    }

    private fun showApp() {
        val cityViewModel = CityViewModel(RandomCityProviderMock())
        composeRule.setContent { NastolkinApp(cityViewModel) }
    }

    private class RandomCityProviderMock : IRandomCityProvider {
        override fun getRandomCity(): City = City("Кокосовые острова")
    }
}
